package com.derek.doraemon.activity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.derek.doraemon.R;
import com.derek.doraemon.constants.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by derek on 15/10/11.
 */
public class TakePhotoActivity extends BaseActivity {
    public static final String EXTRA_IMAGE = "image";
    public static final int RESULT_CODE_OK = 100;
    private static final int PICK_FROM_CAMERA = 100;
    private static final int CROP_PHOTO = 200;
    private static final int PICK_FROM_FILE = 300;

    private Uri mImageCaptureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        File file = new File(Constants.PET_FOLDER);
        if (!file.exists()) {
            file.mkdir();
        }

        mImageCaptureUri = Uri.fromFile(new File(Constants.PET_FOLDER
            + "temp.jpg"));

        findViewById(R.id.btn_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCameraClicked();
            }
        });
        findViewById(R.id.btn_chose_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGalleryClicked();
            }
        });
    }

    private void onCameraClicked() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
            mImageCaptureUri);
        try {
            intent.putExtra("return-data", true);

            startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onGalleryClicked() {
        Intent intent = new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(
            Intent.createChooser(intent, "Complete action using"),
            PICK_FROM_FILE);
    }

    private void doCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
            intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app",
                Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 210);
            intent.putExtra("outputY", 210);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);

            i.setComponent(new ComponentName(res.activityInfo.packageName,
                res.activityInfo.name));

            startActivityForResult(i, CROP_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();
                break;
            case PICK_FROM_FILE:
                if (data != null) {
                    mImageCaptureUri = data.getData();
                    doCrop();
                }
                break;
            case CROP_PHOTO:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();*/
                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_IMAGE, saveAvatar(photo));
                        setResult(RESULT_CODE_OK, intent);
                    }
                }
                finish();
                break;
        }
    }

    private String saveAvatar(Bitmap bitmap) {
        String path = Constants.PET_FOLDER + "avatar" + ".jpg";
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                System.out.println("saveBitmap ==>>" + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("saveBitmap ==>>" + e.getMessage());
        }
        return path;
    }
}

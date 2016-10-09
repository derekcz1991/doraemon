package com.derek.doraemon.activity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.derek.doraemon.R;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.utils.CommonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by derek on 16/8/18.
 */
public class PublishActivity extends BaseActivity {
    public static final String PET_FOLDER = Environment
        .getExternalStorageDirectory() + "/doraemon/";
    public static final int RESULT_CODE_OK = 100;
    private static final int PICK_FROM_CAMERA = 100;
    private static final int CROP_PHOTO = 200;
    private static final int PICK_FROM_FILE = 300;

    @BindView(R.id.postImg)
    ImageView postImg;
    @BindView(R.id.contentText)
    TextView contentText;

    private Uri mImageCaptureUri;
    private boolean isPhotoSelected;
    private File photoFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        ButterKnife.bind(this);

        File file = new File(PET_FOLDER);
        if (!file.exists()) {
            file.mkdir();
        }

        mImageCaptureUri = Uri.fromFile(new File(PET_FOLDER + "temp.jpg"));
    }

    @OnClick(R.id.pubBtn)
    public void publish() {
        if (TextUtils.isEmpty(contentText.getText().toString())) {
            CommonUtils.toast("写点什么...");
            return;
        }
        if (!isPhotoSelected) {
            CommonUtils.toast("选个照片吧...");
            return;
        }

        // create RequestBody instance from file
        RequestBody requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);

        MultipartBody.Part body =
            MultipartBody.Part.createFormData("photo", photoFile.getName(), requestFile);

        NetManager.getInstance().uploadPhoto(body).enqueue(
            new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    NetManager.getInstance().post(contentText.getText().toString(), (String) resp.getData()).enqueue(
                        new RequestCallback(new RequestCallback.Callback() {
                            @Override
                            public void success(Resp resp) {
                                CommonUtils.toast(resp.getMessage());
                                finish();
                            }

                            @Override
                            public boolean fail(Resp resp) {
                                return false;
                            }
                        }));
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }

            }));
    }

    @OnClick(R.id.cancelBtn)
    public void cancelBtn() {
        finish();
    }

    @OnClick(R.id.cameraBtn)
    public void takeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
            mImageCaptureUri);
        try {
            intent.putExtra("return-data", true);

            startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.photoBtn)
    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(
            Intent.createChooser(intent, "Complete action using"),
            PICK_FROM_FILE);
    }

    @OnClick(R.id.adoptBtn)
    public void checkAdopt() {

    }

    @OnClick(R.id.breedBtn)
    public void checkBreed() {

    }

    @OnClick(R.id.findBtn)
    public void checkFind() {

    }

    @OnClick(R.id.qaBtn)
    public void checkQa() {

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

            intent.putExtra("crop", "true");
            intent.putExtra("outputX", 400);
            intent.putExtra("outputY", 400);
            intent.putExtra("aspectX", 3);
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

                        saveAvatar(photo);
                        isPhotoSelected = true;
                        postImg.setVisibility(View.VISIBLE);
                        postImg.setImageBitmap(photo);
                    }
                }
                break;
        }
    }

    private void saveAvatar(Bitmap bitmap) {
        String path = PET_FOLDER + "avatar" + ".jpg";
        photoFile = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(photoFile);
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
    }
}

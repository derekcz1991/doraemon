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
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.derek.doraemon.R;
import com.derek.doraemon.constants.Constants;
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
public class PublishActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_TYPE = "type";

    public static final int RESULT_CODE_OK = 100;
    private static final int PICK_FROM_CAMERA = 100;
    private static final int CROP_PHOTO = 200;
    private static final int PICK_FROM_FILE = 300;

    @BindView(R.id.postImg)
    ImageView postImg;
    @BindView(R.id.contentText)
    TextView contentText;

    private TextView typeText;
    private CheckedTextView sspCT;
    private CheckedTextView xcCT;
    private CheckedTextView lyCT;
    private CheckedTextView stCT;
    private CheckedTextView pzCT;
    private CheckedTextView hdCT;
    private CheckedTextView mzmCT;

    private Uri mImageCaptureUri;
    private boolean isPhotoSelected;
    private File photoFile;

    private int type;
    private int kind = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra(EXTRA_TYPE, 0);

        if (type == 0) {
            setContentView(R.layout.activity_publish);
        } else if (type == 1) {
            setContentView(R.layout.activity_publish_welfare);
        } else if (type == 2) {
            setContentView(R.layout.activity_publish_moment);
        }

        ButterKnife.bind(this);

        File file = new File(Constants.PET_FOLDER);
        if (!file.exists()) {
            file.mkdir();
        }
        mImageCaptureUri = Uri.fromFile(new File(Constants.PET_FOLDER + "temp.jpg"));

        if (type == 1) {
            typeText = (TextView) findViewById(R.id.typeText);
            sspCT = (CheckedTextView) findViewById(R.id.sspBtn);
            xcCT = (CheckedTextView) findViewById(R.id.xcBtn);
            lyCT = (CheckedTextView) findViewById(R.id.lyBtn);
            sspCT.setOnClickListener(this);
            xcCT.setOnClickListener(this);
            lyCT.setOnClickListener(this);
        } else if (type == 2) {
            typeText = (TextView) findViewById(R.id.typeText);
            stCT = (CheckedTextView) findViewById(R.id.stBtn);
            pzCT = (CheckedTextView) findViewById(R.id.pzBtn);
            hdCT = (CheckedTextView) findViewById(R.id.hdBtn);
            mzmCT = (CheckedTextView) findViewById(R.id.mzmBtn);
            stCT.setOnClickListener(this);
            pzCT.setOnClickListener(this);
            hdCT.setOnClickListener(this);
            mzmCT.setOnClickListener(this);
        }
    }

    @OnClick(R.id.pubBtn)
    public void publish() {
        if (TextUtils.isEmpty(contentText.getText().toString())) {
            CommonUtils.toast("写点什么");
            return;
        }
        if (!isPhotoSelected) {
            CommonUtils.toast("选个照片吧");
            return;
        }
        if (type == 1 && kind == -1) {
            CommonUtils.toast("请选择话题分类");
            return;
        }

        // create RequestBody instance from file
        RequestBody requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);

        MultipartBody.Part body =
            MultipartBody.Part.createFormData("photo", photoFile.getName(), requestFile);

        NetManager.getInstance().uploadPhoto(body, type).enqueue(
            new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    NetManager.getInstance().post(contentText.getText().toString(), (String) resp.getData(), type, kind).enqueue(
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sspBtn:
                resetWelfareType();
                sspCT.setChecked(true);
                typeText.setText("选择话题分类: 随手拍");
                kind = 1;
                break;
            case R.id.xcBtn:
                resetWelfareType();
                xcCT.setChecked(true);
                typeText.setText("选择话题分类: 寻宠");
                kind = 2;
                break;
            case R.id.lyBtn:
                resetWelfareType();
                lyCT.setChecked(true);
                typeText.setText("选择话题分类: 领养");
                kind = 3;
                break;
            case R.id.stBtn:
                resetMomentType();
                stCT.setChecked(true);
                typeText.setText("选择话题分类: 晒图");
                break;
            case R.id.pzBtn:
                resetMomentType();
                pzCT.setChecked(true);
                typeText.setText("选择话题分类: 配种");
                break;
            case R.id.hdBtn:
                resetMomentType();
                hdCT.setChecked(true);
                typeText.setText("选择话题分类: 活动");
                break;
            case R.id.mzmBtn:
                resetMomentType();
                mzmCT.setChecked(true);
                typeText.setText("选择话题分类: 墓志铭");
                break;
        }
    }

    private void resetWelfareType() {
        sspCT.setChecked(false);
        xcCT.setChecked(false);
        lyCT.setChecked(false);
    }

    private void resetMomentType() {
        stCT.setChecked(false);
        pzCT.setChecked(false);
        hdCT.setChecked(false);
        mzmCT.setChecked(false);
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
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 100);
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
        String path = Constants.PET_FOLDER + "avatar" + ".jpg";
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

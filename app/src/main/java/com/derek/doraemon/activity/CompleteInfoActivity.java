package com.derek.doraemon.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.derek.doraemon.R;
import com.derek.doraemon.model.UserDetail;
import com.derek.doraemon.netapi.NetManager;
import com.derek.doraemon.netapi.RequestCallback;
import com.derek.doraemon.netapi.Resp;
import com.derek.doraemon.view.CircleImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by derek on 16/7/14.
 */
public class CompleteInfoActivity extends BaseTitleActivity {
    public static final String EXTRA_USER_DETAIL = "userDetail";

    @BindView(R.id.userImageView)
    CircleImageView userImageView;
    @BindView(R.id.petNameText)
    TextView petNameText;
    @BindView(R.id.petAgeText)
    TextView petAgeText;
    @BindView(R.id.petTypeText)
    TextView petTypeText;
    @BindView(R.id.petCategoryText)
    TextView petCategoryText;
    @BindView(R.id.nickNameText)
    TextView nickNameText;
    @BindView(R.id.jobText)
    TextView jobText;
    @BindView(R.id.constellationText)
    TextView constellationText;
    @BindView(R.id.emailText)
    TextView emailText;
    @BindView(R.id.introText)
    TextView introText;

    private UserDetail userDetail;
    private String avatarPath;

    @Override
    protected boolean showNavIcon() {
        return true;
    }

    @Override
    protected View onCreateContentView() {
        setTitleText("完善个人信息");
        View view = View.inflate(this, R.layout.activity_complete_info, null);
        ButterKnife.bind(this, view);

        userDetail = (UserDetail) getIntent().getSerializableExtra(EXTRA_USER_DETAIL);
        if (userDetail != null) {
            Picasso.with(this)
                .load(NetManager.getInstance().getHost() + userDetail.getAvatarUrl())
                .placeholder(R.drawable.icon_profile)
                .into(userImageView);
            petNameText.setText(userDetail.getPetName());
            petTypeText.setText(userDetail.getPetBreed());
            petCategoryText.setText(userDetail.getPetType());
            nickNameText.setText(userDetail.getNickName());
            emailText.setText(userDetail.getEmail());
            petAgeText.setText(String.valueOf(userDetail.getPetAge()));
            jobText.setText(userDetail.getProfession());
            constellationText.setText(userDetail.getConstellation());
            introText.setText(userDetail.getIntro());
        }
        return view;
    }

    @OnClick(R.id.startBtn)
    public void submit() {
        if (petNameText.getText().toString().isEmpty()
            || petAgeText.getText().toString().isEmpty()
            || petTypeText.getText().toString().isEmpty()
            || nickNameText.getText().toString().isEmpty()
            || introText.getText().toString().isEmpty()) {
            Toast.makeText(CompleteInfoActivity.this, "请完善信息", Toast.LENGTH_SHORT).show();
            return;
        }

        NetManager.getInstance()
            .completeUserInfo("1", petTypeText.getText().toString(),
                petCategoryText.getText().toString(), petNameText.getText().toString(),
                petAgeText.getText().toString(), nickNameText.getText().toString(),
                jobText.getText().toString(), constellationText.getText().toString(),
                introText.getText().toString())
            .enqueue(new RequestCallback(new RequestCallback.Callback() {
                @Override
                public void success(Resp resp) {
                    if (userDetail == null) {
                        startActivity(new Intent(CompleteInfoActivity.this, HomeActivity.class));
                    }
                    finish();
                }

                @Override
                public boolean fail(Resp resp) {
                    return false;
                }
            }));

        if (avatarPath != null) {
            File photoFile = new File(avatarPath);
            // create RequestBody instance from file
            RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);

            MultipartBody.Part body =
                MultipartBody.Part.createFormData("avatar", photoFile.getName(), requestFile);

            NetManager.getInstance().uploadAvatar(body).enqueue(
                new RequestCallback(new RequestCallback.Callback() {
                    @Override
                    public void success(Resp resp) {

                    }

                    @Override
                    public boolean fail(Resp resp) {
                        return false;
                    }
                }));
        }
    }

    @OnClick(R.id.userImageView)
    public void onPhotoClick() {
        startActivityForResult(new Intent(this, TakePhotoActivity.class), 1);
    }

    private void updateUserIcon(String path) {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == TakePhotoActivity.RESULT_CODE_OK) {
            String path = data.getStringExtra(TakePhotoActivity.EXTRA_IMAGE);
            //byte[] byteArray = data.getByteArrayExtra(TakePhotoActivity.EXTRA_IMAGE);
            //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            userImageView.setImageBitmap(BitmapFactory.decodeFile(path));
            avatarPath = path;
        }
    }
}

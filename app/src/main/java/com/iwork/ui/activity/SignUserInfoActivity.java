package com.iwork.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.ui.view.BottomListMenu;
import com.iwork.utils.FileConfig;
import com.iwork.utils.FileUtil;
import com.iwork.utils.ImageUtil;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUserInfoActivity extends BaseActivity {

    @Bind(R.id.registe_iv_user)
    ImageView registeIvUser;
    @Bind(R.id.registe_user_cname)
    EditText registeUserCname;
    @Bind(R.id.registe_ed_user_ename)
    EditText registeEdUserEname;
    @Bind(R.id.registe_ed_user_mail)
    EditText registeEdUserMail;
    @Bind(R.id.regist_btn_submit_user)
    Button registBtnSubmitUser;
    @Bind(R.id.regist_user_name)
    ImageView registUserName;
    @Bind(R.id.regist_rl_userinfo)
    RelativeLayout registRlUserinfo;

    private View parentView;
    private File mAvatarOriginFile;
    private File mCropFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_sign_user_info, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        mAvatarOriginFile = FileConfig.getPhotoOutputFile();
    }

    private BottomListMenu _bottomListMenu;
    public static final int REQUEST_CODE_SELECT_PIC = 1005;
    public static final int REQUEST_CODE_CAPTURE_PIC = 1006;
    public static final int REQUEST_CODE_CROP_PIC = 1007;

    @OnClick(R.id.registe_iv_user)
    public void setUserImage() {
        if (_bottomListMenu == null) {
            _bottomListMenu = new BottomListMenu(this, parentView, getResources().getStringArray(R.array.avatar_menu));
            _bottomListMenu.setListMenuListener(new BottomListMenu.ListMenuListener() {
                @Override
                public void onItemSelected(int position, String itemStr) {
                    if (position == 0) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mAvatarOriginFile));
                        startActivityForResult(intent, REQUEST_CODE_CAPTURE_PIC);
                    } else if (position == 1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
                    }
                }
            });
        }
        _bottomListMenu.showDialog();
    }

    @OnClick(R.id.registe_btn_submit)
    public void onNext(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_PIC:
                    ContentResolver resolver = getContentResolver();
                    Uri originalUri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        ImageUtil.writeToFile(bitmap, mAvatarOriginFile.getAbsolutePath(), 90, true);
                        startCropIntent(Uri.fromFile(mAvatarOriginFile));
                    }
                    break;
                case REQUEST_CODE_CAPTURE_PIC:
                    if (mAvatarOriginFile != null) {
                        startCropIntent(Uri.fromFile(mAvatarOriginFile));
                    }
                    break;
                case REQUEST_CODE_CROP_PIC:
                    if (mCropFile != null) {
                        updateAvatar(mCropFile.getAbsolutePath());
                    }

                    break;
            }
        }
    }

    private void updateAvatar(String absolutePath) {
        Bitmap bitmap = ImageUtil.createBitmap(absolutePath);
        registeIvUser.setImageBitmap(bitmap);
    }

    // 打开裁剪界面
    private void startCropIntent(Uri uri) {
        Uri targetUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = FileUtil.getPath(this, uri);
            targetUri = Uri.fromFile(new File(url));
            intent.setDataAndType(targetUri, "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        mCropFile = FileConfig.getPhotoOutputFile();
        Uri saveUri = Uri.fromFile(mCropFile);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CODE_CROP_PIC);
    }

}

package com.iwork.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.Base.BaseApplication;
import com.iwork.helper.ToastHelper;
import com.iwork.model.QinNiuToken;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.ui.view.BottomListMenu;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.FileConfig;
import com.iwork.utils.FileUtil;
import com.iwork.utils.ImageUtil;
import com.iwork.utils.NetConstant;
import com.iwork.utils.TextUtil;
import com.iwork.utils.TimeUtil;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.socks.library.KLog;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

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
    @Bind(R.id.regist_user_titlebar)
    TitleBar titleBar;
    private View parentView;
    private File mAvatarOriginFile;
    private File mCropFile;
    private String qiniuToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_sign_user_info, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        mAvatarOriginFile = FileConfig.getPhotoOutputFile();
        CommonRequest.getQiniuToken(callback);
        showInputMethod();
        titleBar.setTitle("设置用户信息");
        titleBar.setBackDrawableListener(backListener);
    }

    /**
     * 标题栏返回按钮点击监听
     */
    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private ResultCallback<QinNiuToken> callback = new ResultCallback<QinNiuToken>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(QinNiuToken response) {
            if (response.getInfoCode() == NetConstant.PARAM_OK) {
                qiniuToken = response.getData();
            }
        }
    };
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

    @Override
    protected void onPause() {
        super.onPause();
        hideInputMethod();
    }

    @OnClick(R.id.regist_btn_submit_user)
    public void onNext() {
        String zh_name = registeEdUserEname.getText().toString();
        if (!TextUtil.isEmpty(zh_name)) {
            BaseApplication.getAppContext().getmUserInfo().zh_name = zh_name;
        } else {
            ToastHelper.showShortError("请填写您的姓名");
            return;
        }
        CharSequence mail = registeEdUserMail.getText();
        if (!TextUtils.isEmpty(mail) && Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            BaseApplication.getAppContext().getmUserInfo().email = mail.toString();
        } else {
            ToastHelper.showShortError("请正确填写您的邮箱");
            return;
        }
        saveData();
        Intent intent = new Intent(this, PositionInfoActivity.class);
        startActivity(intent);
    }

    private String img_url;

    private void saveData() {
        String zh_name = registeUserCname.getText().toString();
        String en_name = registeEdUserEname.getText().toString();
        String email = registeEdUserMail.getText().toString();
        if (!TextUtils.isEmpty(zh_name)) {
            BaseApplication.getAppContext().getmUserInfo().zh_name = zh_name;
        }
        if (!TextUtils.isEmpty(en_name))
            BaseApplication.getAppContext().getmUserInfo().en_name = en_name;
        if (!TextUtils.isEmpty(email)) {
            BaseApplication.getAppContext().getmUserInfo().email = email;
        }
        if (!TextUtils.isEmpty(img_url)) {
            BaseApplication.getAppContext().getmUserInfo().pic_url = img_url;
        }
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
        UploadManager uploadManager = new UploadManager();
        String key = getImageKey();
        String token = qiniuToken;
        uploadManager.put(absolutePath, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                KLog.i("---qiniures", response.toString());
                img_url = NetConstant.BASE_QINIU_URL+key;

            }
        }, null);
    }

    /**
     * 生成上传七牛图片的key
     *
     * @return
     */
    private String getImageKey() {
        return "PNG-"+ TimeUtil.formatDates(System.currentTimeMillis());
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

    /**
     * 显示输入法
     */
    public void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(registeUserCname, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏输入法
     */
    public void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(registeUserCname.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

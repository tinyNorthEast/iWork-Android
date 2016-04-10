package com.iwork.ui.activity.myself;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.impetusconsulting.iwork.R;
import com.iwork.Base.BaseActivity;
import com.iwork.helper.ResourcesHelper;
import com.iwork.helper.ToastHelper;
import com.iwork.model.CommonModel;
import com.iwork.model.QinNiuToken;
import com.iwork.net.CommonRequest;
import com.iwork.okhttp.callback.ResultCallback;
import com.iwork.preferences.Preferences;
import com.iwork.ui.view.BottomListMenu;
import com.iwork.ui.view.CircleTransform;
import com.iwork.ui.view.ListPickerWindow;
import com.iwork.ui.view.TitleBar;
import com.iwork.utils.FileConfig;
import com.iwork.utils.FileUtil;
import com.iwork.utils.ImageUtil;
import com.iwork.utils.NetConstant;
import com.iwork.utils.TextUtil;
import com.iwork.utils.TimeUtil;
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

public class SetUserInfoActivity extends BaseActivity {

    @Bind(R.id.setuser_titlebar)
    TitleBar setuserTitlebar;
    @Bind(R.id.myself_setmypassword)
    RelativeLayout myselfSetmypassword;
    @Bind(R.id.myself_setmyexpersence)
    RelativeLayout myselfSetmyexpersence;
    @Bind(R.id.myself_setmyemail_ed)
    EditText myselfSetmyemailEd;
    @Bind(R.id.myself_setmyename_ed)
    EditText myselfSetmyenameEd;
    @Bind(R.id.myself_setmycompany_ed)
    EditText myselfSetmycompanyEd;
    @Bind(R.id.myself_setmyexpersence_tv)
    TextView myselfSetmyexpersenceTv;
    @Bind(R.id.set_head_icon_iv)
    ImageView setHeadIconIv;
    @Bind(R.id.set_head_icon_rl)
    RelativeLayout setHeadIcon_layout;
    @Bind(R.id.myself_setmysignature_ed)
    EditText myselfSetmysignatureEd;
    @Bind(R.id.myself_setmysignature)
    LinearLayout myselfSetmysignature;

    private ListPickerWindow<String> mExplistPickerWindow;
    private int experience;
    private File mAvatarOriginFile;
    private File mCropFile;
    private String qiniuToken;
    private View parentView;
    private String img_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_set_user_info, null);
        setContentView(parentView);
        ButterKnife.bind(this);
        setuserTitlebar.setTitle("修改个人信息");
        setuserTitlebar.setBackDrawableListener(backListener);
        setuserTitlebar.setRightTextView("保存", R.color.white, saveListener);
        mAvatarOriginFile = FileConfig.getPhotoOutputFile();
        if (TextUtil.isEmpty(Preferences.getInstance().getQiNiuToken())) {
            CommonRequest.getQiniuToken(callback);
        } else {
            qiniuToken = Preferences.getInstance().getQiNiuToken();
        }
        if (!TextUtil.isEmpty(Preferences.getInstance().getUserHeadUrl())) {
            Glide.with(this).load(Preferences.getInstance().getUserHeadUrl()).transform(new CircleTransform(this)).error(R.drawable.head_icon).placeholder(R.drawable.head_icon).into(setHeadIconIv);
        }
        if (!TextUtil.isEmpty(Preferences.getInstance().getmail())) {
            myselfSetmyemailEd.setHint(Preferences.getInstance().getmail());
        }
        if (!TextUtil.isEmpty(Preferences.getInstance().getEnName())) {
            myselfSetmyenameEd.setHint(Preferences.getInstance().getEnName());
        }
        if (!TextUtil.isEmpty(Preferences.getInstance().getMyselfSignature())){
            myselfSetmysignatureEd.setHint(Preferences.getInstance().getMyselfSignature());
        }
        myselfSetmyexpersenceTv.setText(getResources().getStringArray(R.array.experience)[Preferences.getInstance().getExperience()]);
    }

    private ResultCallback<QinNiuToken> callback = new ResultCallback<QinNiuToken>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(QinNiuToken response) {
            if (response.getInfoCode() == NetConstant.PARAM_OK) {
                qiniuToken = response.getData();
                Preferences.getInstance().setQiNiuToken(qiniuToken);
            }
        }
    };
    private BottomListMenu _bottomListMenu;
    public static final int REQUEST_CODE_SELECT_PIC = 1005;
    public static final int REQUEST_CODE_CAPTURE_PIC = 1006;
    public static final int REQUEST_CODE_CROP_PIC = 1007;

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String email = myselfSetmyemailEd.getText().toString().trim();
            String en_name = myselfSetmyenameEd.getText().toString().trim();
            String company = myselfSetmycompanyEd.getText().toString().trim();
            String signature = myselfSetmysignatureEd.getText().toString().trim();
            if (TextUtil.isEmpty(email) && TextUtil.isEmpty(en_name) && TextUtil.isEmpty(company) && TextUtil.isEmpty(img_url)) {
                ToastHelper.showShortError("请确认您要修改的信息");
            }
            if (!TextUtil.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ToastHelper.showShortError("请正确填写您的邮箱");
                return;
            }
            CommonRequest.setUserInfo(en_name, email, company, experience, img_url,signature, new ResultCallback<CommonModel>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(CommonModel response) {
                    if (response.getInfoCode() == 0) {
                        ToastHelper.showShortCompleted("修改信息成功");
                        finish();
                    } else {
                        ToastHelper.showShortError(response.getMessage());
                    }
                }
            });

        }
    };

    @OnClick(R.id.myself_setmypassword)
    public void setPassword() {
        Intent intent = new Intent(this, SetMyselfPassword.class);
        startActivity(intent);
    }

    @OnClick(R.id.myself_setmyexpersence)
    public void setMyselfSetmyexpersence() {
        mExplistPickerWindow = new ListPickerWindow<String>(this, getCurrentFocus(), ResourcesHelper.getStringArray(R.array.experience));
        mExplistPickerWindow.setListPickerListener(new ListPickerWindow.ListPickerListener<String>() {
            @Override
            public void onItemSelected(int position, String object) {
                myselfSetmyexpersenceTv.setText(object);
                experience = position;
                KLog.i("---exp", position);
            }
        });
        mExplistPickerWindow.showDialog();
    }

    @OnClick(R.id.set_head_icon_rl)
    public void setHeadIcon() {
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
        setHeadIconIv.setImageBitmap(ImageUtil.round(bitmap, 200, true));
        UploadManager uploadManager = new UploadManager();
        String key = getImageKey();
        String token = qiniuToken;
        uploadManager.put(absolutePath, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                img_url = NetConstant.BASE_QINIU_URL + key;
            }
        }, null);
    }

    /**
     * 生成上传七牛图片的key
     *
     * @return
     */
    private String getImageKey() {
        return "PNG_" + TimeUtil.formatDates(System.currentTimeMillis());
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

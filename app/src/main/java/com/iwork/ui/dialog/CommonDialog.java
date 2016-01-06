package com.iwork.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.impetusconsulting.iwork.R;
import com.iwork.helper.ResourcesHelper;
import com.iwork.utils.TextUtil;


/**
 * @auther Zhangjiantao function 对话框通用类
 * @since 2015/6/8
 */
public class CommonDialog extends Dialog {

    private String title;
    private String btnSubmitTxt;
    private String btnCancelTxt;

    private CommonDialogListener listener;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (null == listener) {
                return;
            }
            switch (view.getId()) {
                case R.id.commonDialog_btn_submit:
                    if (ButtonType.ONE.toString().equals(buttonType.name())) {
                        listener.submitOnly();
                    } else {
                        listener.submit();
                    }
                    break;
                case R.id.commonDialog_btn_cancel:
                    listener.cancel();
                    dismiss();
                    break;
            }

        }
    };

    /**
     * 新增公共对话框的回退事件监听器
     */
    private OnKeyListener onKeyBackListener = new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (listener != null) {
                    return listener.onKeyBackPressed();
                }
            }
            return false;
        }
    };


    private Button commonDialogbtncancel;
    private View view;
    private ButtonType buttonType;
    private EditText commonEdtext;
    @Override
    public void dismiss() {
        super.dismiss();
    }

    public enum ButtonType {
        /**
         * 只有一个Button
         */
        ONE,
        /**
         * 两个Button
         */
        TWO,
    }

    public CommonDialog(Context context) {
        super(context, R.style.CommonDialog);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog);
        initLayout();
    }

    private void initLayout() {
        Button commonDialogbtnsubmit = (Button) findViewById(R.id.commonDialog_btn_submit);
        this.commonDialogbtncancel = (Button) findViewById(R.id.commonDialog_btn_cancel);
        TextView commonDialogtitletxtmulti = (TextView) findViewById(R.id.commonDialog_title_txt_multi);
        TextView commonDialogtitletxtsingle = (TextView) findViewById(R.id.commonDialog_title_txt_single);
        this.view = findViewById(R.id.commonDialog_view);
        commonDialogbtnsubmit.setOnClickListener(onClickListener);
        commonDialogbtncancel.setOnClickListener(onClickListener);
        //new add by yangxiaomin 新加回退事件监听器
        this.setOnKeyListener(onKeyBackListener);
        this.commonEdtext = (EditText) findViewById(R.id.commonDialog_ed);

        if (TextUtils.isEmpty(title)) {
            return;
        }
        if (TextUtil.length(title) > 15) {
            commonDialogtitletxtmulti.setVisibility(View.VISIBLE);
            commonDialogtitletxtsingle.setVisibility(View.INVISIBLE);
            commonDialogtitletxtmulti.setText(title);
        } else {
            commonDialogtitletxtmulti.setVisibility(View.INVISIBLE);
            commonDialogtitletxtsingle.setVisibility(View.VISIBLE);
            commonDialogtitletxtsingle.setText(title);
        }
        if (TextUtils.isEmpty(btnSubmitTxt)) {
            btnSubmitTxt = ResourcesHelper.getString(R.string.submit);
        }
        if (TextUtils.isEmpty(btnCancelTxt)) {
            btnCancelTxt = ResourcesHelper.getString(R.string.cancel);
        }
        commonDialogbtnsubmit.setText(btnSubmitTxt);
        commonDialogbtncancel.setText(btnCancelTxt);

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBtnType(ButtonType btnType) {
        this.buttonType = btnType;
    }

    public void setListener(CommonDialogListener listener) {
        this.listener = listener;
    }

    public String getEdtext(){
        return commonEdtext.getText().toString().trim();
    }

    public void setEdVisable(int visable){
        commonEdtext.setVisibility(visable);
    }
    public void setEdHintText(String text){
        commonEdtext.setHint(text);
    }
    public void setEdText(String text){
        commonEdtext.setText(text);
    }
    // public void setBtnOnlyTxt(String text) {
    // if (!TextUtils.isEmpty(text)) {
    // btnSubmitTxt = text;
    // }
    // }

    public void setBtnSubmitTxt(String text) {
        if (!TextUtils.isEmpty(text)) {
            btnSubmitTxt = text;
        }
    }

    public void setBtnCancelTxt(String text) {
        if (!TextUtils.isEmpty(text)) {
            btnCancelTxt = text;
        }
    }

    @Override
    public void show() {
        super.show();
        setButtonVisbility();
    }

    private void setButtonVisbility() {
        if (null == buttonType) {
            commonDialogbtncancel.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            buttonType = ButtonType.TWO;
            return;
        }
        if (ButtonType.ONE.toString().equals(buttonType.name())) {
            commonDialogbtncancel.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else if (ButtonType.TWO.toString().equals(buttonType.name())) {
            commonDialogbtncancel.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }
    }

    public static abstract class CommonDialogListener {
        /**
         * 只有一个按钮的确认事件
         */
        public void submitOnly() {
        }

        /**
         * 只有两个按钮的确认事件
         */
        public void submit() {

        }

        /**
         * 取消按钮事件
         */
        public void cancel() {

        }

        /**
         * 当点击回退时
         */
        public boolean onKeyBackPressed() {
            //default return false
            return false;
        }
    }
}

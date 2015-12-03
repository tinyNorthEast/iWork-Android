package com.iwork.ui.dialog;

import android.app.Activity;
import android.content.Context;

import com.iwork.Base.ActivityManager;
import com.iwork.Base.BaseApplication;
import com.iwork.helper.ResourcesHelper;


/**
 * @auther ZhangJianTao function 对话框帮助类
 * @since 2015/6/10
 */
public class DialogHelper {

    private static CommonDialog dialog;

    public static class DialogBuilder {
        private CommonDialog dialog;

        public DialogBuilder(Context context) {
            dialog = new CommonDialog(context);
        }

        public DialogBuilder setTitleConTent(String title) {
            dialog.setTitle(title);
            return this;
        }

        public DialogBuilder setButtonType(CommonDialog.ButtonType buttonType) {
            dialog.setBtnType(buttonType);
            return this;
        }

        public DialogBuilder setListener(CommonDialog.CommonDialogListener listener) {
            dialog.setListener(listener);
            return this;
        }

        public DialogBuilder setSubmitBtnText(String text) {
            dialog.setBtnSubmitTxt(text);
            return this;
        }

        public DialogBuilder setCancelBtnText(String text) {
            dialog.setBtnCancelTxt(text);
            return this;
        }

        public DialogBuilder setCanceledOnToucheOutside(boolean b) {
            dialog.setCanceledOnTouchOutside(b);
            return this;
        }

        public DialogBuilder setCancelable(boolean b) {
            dialog.setCancelable(b);
            return this;
        }

        public CommonDialog creat() {
            return dialog;
        }

        public boolean isShowing() {
            return dialog.isShowing();
        }
    }

    public static class LoadingDialogBuilder {
        private LoadingDialog loadingDialog;

        public LoadingDialogBuilder(Context context) {
            loadingDialog = new LoadingDialog(context);
        }

        public LoadingDialogBuilder setLoadingContent(String text) {
            loadingDialog.setLoadingContent(text);
            return this;
        }

        public LoadingDialogBuilder setCanceledOnTouchOutside(boolean b) {
            loadingDialog.setCanceledOnTouchOutside(b);
            return this;
        }

        public LoadingDialogBuilder setCancelable(boolean b) {
            loadingDialog.setCancelable(b);
            return this;
        }

        public LoadingDialog creat() {
            return loadingDialog;
        }
    }

    /**
     * 显示对话框
     * 
     * @param title
     *            标题
     * @param submitTxt
     *            提交按钮上的内容
     * @param cancelTxt
     *            取消按钮上的内容
     * @param listener
     *            取消按钮上的内容
     * @return 显示的对话框dialog CommonDialog
     */
    public static CommonDialog showDialog(String title, String submitTxt, String cancelTxt, CommonDialog.CommonDialogListener listener) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        Activity act = ActivityManager.getInstance().getTopActivityInApp();
        if (act == null)
            return null;
        dialog = new CommonDialog(act);
        dialog.setTitle(title);
        dialog.setBtnType(CommonDialog.ButtonType.TWO);
        dialog.setCancelable(false);
        dialog.setBtnSubmitTxt(submitTxt);
        dialog.setBtnCancelTxt(cancelTxt);
        dialog.setListener(listener);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 显示对话框
     * 
     * @param title
     *            标题
     * @param submitTxt
     *            提交按钮上的内容
     * @param listener
     *            取消按钮上的内容
     * @return 显示的对话框dialog CommonDialog
     */
    public static CommonDialog showDialog(String title, String submitTxt, CommonDialog.CommonDialogListener listener) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (dialog == null) {
            Activity act = ActivityManager.getInstance().getTopActivityInApp();
            if (act == null || act.isFinishing())
                return null;
            dialog = new CommonDialog(act);
        }
        dialog.setTitle(title);
        dialog.setBtnType(CommonDialog.ButtonType.ONE);
        dialog.setBtnSubmitTxt(submitTxt);
        dialog.setListener(listener);
        dialog.setCancelable(false);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 创建自定义对话框并展示(重载)-新加了Context
     * 
     * @param context
     * @param title
     * @param submitTxt
     * @param listener
     * @return
     */
    public static CommonDialog showDialog(Context context, String title, String submitTxt, CommonDialog.CommonDialogListener listener) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new CommonDialog(context);
        dialog.setTitle(title);
        dialog.setBtnType(CommonDialog.ButtonType.ONE);
        dialog.setBtnSubmitTxt(submitTxt);
        dialog.setListener(listener);
        dialog.setCancelable(false);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     *
     * @param titleResId
     *            对话框标题资源ID
     * @param submitTxtResId
     *            提交按钮上的内容 资源id
     * @param cancelTxtResId
     *            取消按钮上的内容 资源ID
     * @param listener
     *            取消、确定按钮的回调监听
     * @return
     */
    public static CommonDialog showDialog(int titleResId, int submitTxtResId, int cancelTxtResId, CommonDialog.CommonDialogListener listener) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (dialog == null) {
            Activity act = ActivityManager.getInstance().getTopActivityInApp();
            if (act == null || act.isFinishing())
                return null;
            dialog = new CommonDialog(act);
        }
        dialog.setTitle(ResourcesHelper.getString(titleResId));
        dialog.setBtnType(CommonDialog.ButtonType.TWO);
        dialog.setCancelable(false);
        dialog.setBtnSubmitTxt(ResourcesHelper.getString(submitTxtResId));
        dialog.setBtnCancelTxt(ResourcesHelper.getString(cancelTxtResId));
        dialog.setListener(listener);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     *
     * @param titleResId
     *            对话框标题资源ID
     * @param submitTxtResId
     *            提交按钮上的内容 资源id
     * @param listener
     *            取消、确定按钮的回调监听
     * @return
     */
    public static CommonDialog showDialog(int titleResId, int submitTxtResId, CommonDialog.CommonDialogListener listener) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new CommonDialog(BaseApplication.getAppContext());
        dialog.setTitle(titleResId);
        dialog.setBtnType(CommonDialog.ButtonType.ONE);
        dialog.setBtnSubmitTxt(ResourcesHelper.getString(submitTxtResId));
        dialog.setListener(listener);
        dialog.setCancelable(false);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }

    public static void closeDialog() {
        if (dialog != null && dialog.isShowing() && dialog.getOwnerActivity()!=null && !dialog.getOwnerActivity().isFinishing()) {
            dialog.cancel();
            dialog = null;
        }
    }
}

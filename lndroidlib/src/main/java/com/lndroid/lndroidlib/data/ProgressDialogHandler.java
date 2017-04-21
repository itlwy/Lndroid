package com.lndroid.lndroidlib.data;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * Created by mac on 16/11/19.
 */

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    private Dialog mDialog;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;
    private String message;
    private String mDefaultMessage = "数据处理中...请稍后";


    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener, String message,
                                 Dialog dialog, boolean cancelable) {
        super();
        setup(context, mProgressCancelListener, dialog, message, cancelable);
    }

    public void setup(Context context, ProgressCancelListener mProgressCancelListener, Dialog dialog, String message,
                      boolean cancelable) {
        this.mDialog = dialog;
        this.message = TextUtils.isEmpty(message) ? mDefaultMessage : message;
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog() {
        if (mDialog == null) {
            ProgressDialog pd = new ProgressDialog(context);
            pd.setCancelable(cancelable);
            pd.setMessage(message);
            if (cancelable) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }
            mDialog = pd;
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}

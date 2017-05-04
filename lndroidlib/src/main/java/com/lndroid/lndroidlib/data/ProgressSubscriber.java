package com.lndroid.lndroidlib.data;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.lndroid.lndroidlib.base.api.IBaseView;
import com.lndroid.lndroidlib.utils.SnackbarUtil;

import rx.Subscriber;

/**
 * Created by mac on 16/11/19.
 */

public abstract class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private IBaseView view;
    private ProgressDialogHandler mProgressDialogHandler;
    private boolean isShowDialog = true;

    private Context context;

    public void setShowDialog(boolean showDialog) {
        isShowDialog = showDialog;
    }

    public ProgressSubscriber(Context context, IBaseView view, String message) {
        init(context, view, null, message);
    }

    public ProgressSubscriber(Context context) {
        init(context, null, null, null);
    }

    public ProgressSubscriber(Context context, IBaseView view, Dialog dialog) {
        init(context, view, dialog, null);
    }

    private void init(Context context, IBaseView view, Dialog dialog, String message) {
        this.context = context;
        this.view = view;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, message, dialog, true);
    }

    public void showProgressDialog() {
        if (mProgressDialogHandler != null && isShowDialog) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialogHandler != null && isShowDialog) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
        if (view != null) {
            view.showSuccessView();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (view != null) {
            if (e instanceof HttpException && ((HttpException) e).getResultCode() == HttpException.NO_DATA) {
                view.showEmptyView();
            } else {
//                view.showErrorView(e.getMessage());
                SnackbarUtil.IndefiniteSnackbar(view.getLayoutView(), e.getMessage(), 10 * 1000
                        , SnackbarUtil.Default).setAction("关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        } else {
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dismissProgressDialog();
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}

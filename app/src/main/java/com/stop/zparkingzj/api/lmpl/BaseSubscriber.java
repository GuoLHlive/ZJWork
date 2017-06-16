package com.stop.zparkingzj.api.lmpl;

import android.content.Context;
import android.widget.Toast;


import com.stop.zparkingzj.progress.ProgressCancelListener;
import com.stop.zparkingzj.progress.ProgressDialogHandler;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/29.
 */

/*
*   订阅处理
* */
public abstract class BaseSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private static final String TAG = "AppError";
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;

    public BaseSubscriber(Context context) {
        this.context = context;
        if (this.context !=null){
            mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
        }
    }

    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
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
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        try {
            Toast.makeText(context, "网络连接失败...请重新刷卡登录", Toast.LENGTH_SHORT).show();
        } catch (Exception e1) {
            Toast.makeText(context,"发生未知错误...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNext(T t) {
       onSuccess(t);
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    //处理
    protected abstract void onSuccess(T result);



}

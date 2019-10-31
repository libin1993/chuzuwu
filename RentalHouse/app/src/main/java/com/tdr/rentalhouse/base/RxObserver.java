package com.tdr.rentalhouse.base;


import android.content.Intent;

import com.tdr.rentalhouse.application.MyApplication;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.mvp.login.LoginActivity;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.SPUtils;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Author：Libin on 2019/05/31 09:34
 * Email：1993911441@qq.com
 * Describe：回调封装
 */
public class RxObserver<T extends BaseBean> implements Observer<T> {

    private Callback<T> mCallback;

    public RxObserver(Callback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(T t) {
        LogUtils.log(t.toString());
        if (t.getStatus() == 2) {
            mCallback.onSuccess(t);
        } else if (t.getStatus() == 1) {
            SPUtils.getInstance().clear(SPUtils.FILE_USER);
            Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getInstance().startActivity(intent);
        } else {
            mCallback.onFail(t.getMessage());
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        LogUtils.log(e.toString());
        mCallback.onFail(e.toString());
    }

    @Override
    public void onComplete() {

    }

}

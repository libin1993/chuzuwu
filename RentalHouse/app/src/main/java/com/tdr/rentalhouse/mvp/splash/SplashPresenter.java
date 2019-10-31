package com.tdr.rentalhouse.mvp.splash;

import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.UserBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/24 10:16
 * Description：
 */
public class SplashPresenter extends BasePresenterImpl<BaseView> implements SplashContact.Presenter {
    @Override
    public void getInfo(final int what, int accountId) {
        if (!isViewAttached())
            return;
        RetrofitUtils.getInstance()
                .getService()
                .getInfo(accountId)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        getView().onSuccess(what, userBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().onFail(what, msg);
                    }
                }));
    }
}

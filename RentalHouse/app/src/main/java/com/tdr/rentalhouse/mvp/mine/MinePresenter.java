package com.tdr.rentalhouse.mvp.mine;

import android.location.Address;

import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.AddressBean;
import com.tdr.rentalhouse.bean.UserBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/18 17:01
 * Description：
 */
public class MinePresenter extends BasePresenterImpl<BaseView> implements MineContact.Presenter {
    @Override
    public void getAllAddress(final int what) {
        if (!isViewAttached()) {
            return;
        }

        RetrofitUtils.getInstance()
                .getService()
                .getAllAddress()
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<AddressBean>() {
                    @Override
                    public void onSuccess(AddressBean addressBean) {
                        getView().onSuccess(what, addressBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().onFail(what, msg);
                    }
                }));
    }

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

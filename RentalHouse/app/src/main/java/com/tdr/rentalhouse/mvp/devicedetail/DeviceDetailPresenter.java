package com.tdr.rentalhouse.mvp.devicedetail;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.DeviceDetailBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Libin on 2020-03-19 17:36
 * Email：1993911441@qq.com
 * Describe：
 */
public class DeviceDetailPresenter extends BasePresenterImpl<BaseView> implements DeviceDetailContact.Presenter{
    @Override
    public void deviceDetail(final int what, int bindId) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .deviceDetail(bindId)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<DeviceDetailBean>() {


                    @Override
                    public void onSuccess(DeviceDetailBean deviceDetailBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, deviceDetailBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void editDeviceName(final int what, int bindId, String deviceName) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .editDeviceName(bindId,deviceName)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BaseBean>() {


                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, null);
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }
}

package com.tdr.rentalhouse.mvp.installotherdevice;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.SelfBuildingDeviceBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.mvp.roomlist.RoomInfoContact;
import com.tdr.rentalhouse.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Author：Libin on 2020-03-19 18:15
 * Email：1993911441@qq.com
 * Describe：
 */
public class OtherDevicePresenter extends BasePresenterImpl<BaseView> implements OtherDeviceContact.Presenter{
    @Override
    public void deviceName(final int what, String  code) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .deviceName(code)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, baseBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void installOtherDevice(final int what, Map<String, Object> map, ArrayList<String> list) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .installOtherDevice(map,list)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, null);
                    }

                    @Override
                    public void onFail(String msg) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void upload(final int what, Map<String, RequestBody> map) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .upload(map)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        if (!isViewAttached())
                            return;
                        getView().onSuccess(what, baseBean);
                    }

                    @Override
                    public void onFail(String msg) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

}

package com.tdr.rentalhouse.mvp.devicelist;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.CommunityBean;
import com.tdr.rentalhouse.bean.DeviceListBean;
import com.tdr.rentalhouse.bean.SelfBuildingDeviceBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Libin on 2020-03-19 10:47
 * Email：1993911441@qq.com
 * Describe：
 */
public class DeviceListPresenter extends BasePresenterImpl<BaseView> implements DeviceListContact.Presenter{
    @Override
    public void getSelfBuildingDevice(final int what, int id,int currentPage) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getSelfBuildingDevice(id,currentPage)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<SelfBuildingDeviceBean>() {


                    @Override
                    public void onSuccess(SelfBuildingDeviceBean selfBuildingDeviceBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, selfBuildingDeviceBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void getBuildingDevice(final int what, int id, int unitId,int currentPage) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getBuildingDevice(id,unitId,currentPage)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<DeviceListBean>() {
                    @Override
                    public void onSuccess(DeviceListBean deviceListBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, deviceListBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void deleteDevice(final int what, int bindId) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .deleteDevice(bindId)
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

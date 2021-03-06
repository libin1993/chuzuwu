package com.tdr.rentalhouse.mvp.selectaddress;

import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.AddressBean;
import com.tdr.rentalhouse.bean.CommunityBean;
import com.tdr.rentalhouse.bean.FindAddressBean;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.bean.SelfBuildingDeviceBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

import java.util.Map;

/**
 * Author：Li Bin on 2019/7/22 13:13
 * Description：
 */
public class SelectAddressPresenter extends BasePresenterImpl<BaseView> implements SelectAddressContact.Presenter {
    @Override
    public void findAddress(final int what, Map<String, Object> map) {
        if (!isViewAttached()) {
            return;
        }

        RetrofitUtils.getInstance()
                .getService()
                .findAddress(map)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<FindAddressBean>() {
                    @Override
                    public void onSuccess(FindAddressBean findAddressBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, findAddressBean.getData());
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
    public void getHouseInfo(final int what, int id) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getHouseInfo(id)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<HouseBean>() {

                    @Override
                    public void onSuccess(HouseBean houseBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, houseBean.getData());
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
    public void getCommunityInfo(final int what, int id, String guid) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getCommunityInfo(id, guid)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<CommunityBean>() {

                    @Override
                    public void onSuccess(CommunityBean communityBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, communityBean.getData());
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
    public void getCommunityDetail(final int what, int id) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getSelfBuildingDevice(id, 1)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<SelfBuildingDeviceBean>() {
                    @Override
                    public void onSuccess(SelfBuildingDeviceBean selfBuildingDeviceBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, selfBuildingDeviceBean.getData());
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
    public void searchAddress(final int what, Map<String, Object> map) {
        if (!isViewAttached()) {
            return;
        }

        RetrofitUtils.getInstance()
                .getService()
                .searchAddress(map)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<FindAddressBean>() {
                    @Override
                    public void onSuccess(FindAddressBean findAddressBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, findAddressBean.getData());

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

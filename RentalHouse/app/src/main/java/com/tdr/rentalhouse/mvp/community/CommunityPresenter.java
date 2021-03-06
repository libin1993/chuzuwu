package com.tdr.rentalhouse.mvp.community;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.AddAddressBean;
import com.tdr.rentalhouse.bean.CommunityBean;
import com.tdr.rentalhouse.bean.SelfBuildingDeviceBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/22 15:02
 * Description：
 */
public class CommunityPresenter extends BasePresenterImpl<BaseView> implements CommunityContact.Presenter {
    @Override
    public void getCommunityInfo(final int what, int id,String guid) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getCommunityInfo(id,guid)
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
    public void getFireControlCommunity(final int what, int id) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getSelfBuildingDevice(id,1)
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
    public void deleteUnit(final int what, int id) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .deleteUnit(id)
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
}

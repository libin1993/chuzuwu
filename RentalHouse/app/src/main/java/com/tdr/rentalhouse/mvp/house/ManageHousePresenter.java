package com.tdr.rentalhouse.mvp.house;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.FloorBean;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/23 11:11
 * Description：
 */
public class ManageHousePresenter extends BasePresenterImpl<BaseView> implements ManageHouseContact.Presenter {
    @Override
    public void getFloor(final int what, int id) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getFloor(id)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<FloorBean>() {

                    @Override
                    public void onSuccess(FloorBean floorBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, floorBean.getData().getFloorList());
                    }

                    @Override
                    public void onFail(String msg) {
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
                        getView().hideLoading();
                        getView().onSuccess(what, houseBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }
}

package com.tdr.rentalhouse.mvp.scancode;

import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.bean.ScanCodeBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/30 16:36
 * Description：
 */
public class ScanCodePresenter extends BasePresenterImpl<BaseView> implements ScanCodeContact.Presenter {
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

    @Override
    public void scanCode(final int what, String code, String areaCode) {
        if (!isViewAttached())
            return;
        RetrofitUtils.getInstance()
                .getService()
                .scanCode(code, areaCode)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<ScanCodeBean>() {
                    @Override
                    public void onSuccess(ScanCodeBean scanCodeBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, scanCodeBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }
}

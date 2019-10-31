package com.tdr.rentalhouse.mvp.addaddress;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.AddAddressBean;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Author：Li Bin on 2019/7/19 13:22
 * Description：
 */
public class AddAddressPresenter extends BasePresenterImpl<BaseView> implements AddAddressContact.Presenter {
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
                        getView().onSuccess(what, baseBean);
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

    @Override
    public void addAddress(final int what, Map<String, Object> map) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .addAddress(map)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<AddAddressBean>() {
                    @Override
                    public void onSuccess(AddAddressBean addAddressBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, addAddressBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));

    }

    @Override
    public void editAddress(final int what, Map<String, Object> map) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .editCommunity(map)
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

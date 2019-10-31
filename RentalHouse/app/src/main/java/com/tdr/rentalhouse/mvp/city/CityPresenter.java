package com.tdr.rentalhouse.mvp.city;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.CityBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/8/9 13:20
 * Description：
 */
public class CityPresenter extends BasePresenterImpl<BaseView> implements CityContact.Presenter {
    @Override
    public void getCityList(final int what, String code) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getCityList(code)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<CityBean>() {
                    @Override
                    public void onSuccess(CityBean cityBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, cityBean.getData().getList());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }

}

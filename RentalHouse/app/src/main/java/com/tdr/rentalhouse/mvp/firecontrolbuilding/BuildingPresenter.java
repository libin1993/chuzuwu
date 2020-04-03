package com.tdr.rentalhouse.mvp.firecontrolbuilding;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.BuildingBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;


/**
 * Author：Libin on 2020-03-17 14:38
 * Email：1993911441@qq.com
 * Describe：
 */
public class BuildingPresenter extends BasePresenterImpl<BaseView> implements BuildingContact.Presenter{

    @Override
    public void getBuildingInfo(final int what, int id) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getBuildingInfo(id)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<BuildingBean>() {

                    @Override
                    public void onSuccess(BuildingBean buildingBean) {
                        if (!isViewAttached())
                            return;
                        getView().hideLoading();
                        getView().onSuccess(what, buildingBean.getData());
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

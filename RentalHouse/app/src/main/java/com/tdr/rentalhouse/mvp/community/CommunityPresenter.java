package com.tdr.rentalhouse.mvp.community;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.AddAddressBean;
import com.tdr.rentalhouse.bean.CommunityBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/22 15:02
 * Description：
 */
public class CommunityPresenter extends BasePresenterImpl<BaseView> implements CommunityContact.Presenter {
    @Override
    public void getCommunityInfo(final int what, int id) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .getCommunityInfo(id)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<CommunityBean>() {

                    @Override
                    public void onSuccess(CommunityBean communityBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, communityBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
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

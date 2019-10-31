package com.tdr.rentalhouse.mvp.communitydetail;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.CommunityDetailBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/22 17:01
 * Description：
 */
public class CommunityDetailPresenter extends BasePresenterImpl<BaseView> implements CommunityDetailContact.Presenter {
    @Override
    public void communityDetail(final int what, int id) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .communityDetail(id)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<CommunityDetailBean>() {

                    @Override
                    public void onSuccess(CommunityDetailBean communityDetailBean) {
                        getView().hideLoading();
                        getView().onSuccess(what, communityDetailBean.getData());
                    }


                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }
}

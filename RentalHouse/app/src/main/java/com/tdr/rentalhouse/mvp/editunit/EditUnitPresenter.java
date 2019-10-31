package com.tdr.rentalhouse.mvp.editunit;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.CommunityBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

import java.util.Map;

/**
 * Author：Li Bin on 2019/7/22 16:15
 * Description：
 */
public class EditUnitPresenter extends BasePresenterImpl<BaseView> implements EditUnitContact.Presenter {

    @Override
    public void editUnit(final int what, Map<String, Object> map) {
        if (!isViewAttached())
            return;

        RetrofitUtils.getInstance()
                .getService()
                .editUnit(map)
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

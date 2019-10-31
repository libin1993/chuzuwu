package com.tdr.rentalhouse.mvp.updatepwd;

import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.FindAddressBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;

/**
 * Author：Li Bin on 2019/7/23 16:59
 * Description：
 */
public class UpdatePwdPresenter extends BasePresenterImpl<BaseView> implements UpdatePwdContact.Presenter {
    @Override
    public void updatePwd(final int what, String oldPwd, String newPwd) {
        if (!isViewAttached()) {
            return;
        }

        RetrofitUtils.getInstance()
                .getService()
                .updatePwd(oldPwd, newPwd)
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

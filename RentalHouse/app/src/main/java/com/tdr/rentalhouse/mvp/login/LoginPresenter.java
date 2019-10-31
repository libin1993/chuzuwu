package com.tdr.rentalhouse.mvp.login;


import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BasePresenterImpl;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RxObserver;
import com.tdr.rentalhouse.base.Transformer;
import com.tdr.rentalhouse.bean.UserBean;
import com.tdr.rentalhouse.inter.Callback;
import com.tdr.rentalhouse.utils.RetrofitUtils;
import com.tdr.rentalhouse.utils.SPUtils;

/**
 * Author：Libin on 2019/6/6 09:41
 * Description：
 */
public class LoginPresenter extends BasePresenterImpl<BaseView> implements LoginContact.Presenter {

    @Override
    public void login(final int what, String username, String password) {
        if (!isViewAttached())
            return;
        RetrofitUtils.getInstance()
                .getService()
                .login(username, password)
                .compose(Transformer.switchSchedulers())
                .subscribe(new RxObserver(new Callback<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        getView().onSuccess(what, userBean.getData());
                    }

                    @Override
                    public void onFail(String msg) {
                        getView().hideLoading();
                        getView().onFail(what, msg);
                    }
                }));
    }
}

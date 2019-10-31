package com.tdr.rentalhouse.mvp.login;


import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Libin on 2019/6/6 09:36
 * Description：
 */
public class LoginContact {

    interface Presenter extends BasePresenter<BaseView> {
        void login(int what, String username, String password);
    }
}

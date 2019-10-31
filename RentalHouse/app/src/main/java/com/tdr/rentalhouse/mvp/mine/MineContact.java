package com.tdr.rentalhouse.mvp.mine;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.mvp.login.LoginContact;

/**
 * Author：Li Bin on 2019/7/18 17:00
 * Description：
 */
public class MineContact {

    interface Presenter extends BasePresenter<BaseView> {
        void getAllAddress(int what);
        void getInfo(int what,int accountId);
    }
}

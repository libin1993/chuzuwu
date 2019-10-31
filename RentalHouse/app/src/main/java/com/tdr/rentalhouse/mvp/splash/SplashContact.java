package com.tdr.rentalhouse.mvp.splash;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Li Bin on 2019/7/24 10:15
 * Description：
 */
public class SplashContact {
    interface Presenter extends BasePresenter<BaseView> {

        void getInfo(int what,int accountId);

    }
}

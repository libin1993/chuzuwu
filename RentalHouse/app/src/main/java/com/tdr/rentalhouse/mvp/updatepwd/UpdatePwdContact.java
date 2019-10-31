package com.tdr.rentalhouse.mvp.updatepwd;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Li Bin on 2019/7/19 17:45
 * Description：
 */
public class UpdatePwdContact {

    interface Presenter extends BasePresenter<BaseView> {
        void updatePwd(int what, String oldPwd, String newPwd);
    }
}

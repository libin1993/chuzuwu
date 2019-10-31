package com.tdr.rentalhouse.mvp.communitydetail;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Li Bin on 2019/7/22 17:01
 * Description：
 */
public class CommunityDetailContact {
    interface Presenter extends BasePresenter<BaseView> {
        void communityDetail(int what, int id);
    }
}

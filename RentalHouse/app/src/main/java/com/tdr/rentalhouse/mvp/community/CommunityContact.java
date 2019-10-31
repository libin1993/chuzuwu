package com.tdr.rentalhouse.mvp.community;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import okhttp3.RequestBody;

/**
 * Author：Li Bin on 2019/7/22 15:02
 * Description：
 */
public class CommunityContact {

    interface Presenter extends BasePresenter<BaseView> {
        void getCommunityInfo(int what, int id);

        void deleteUnit(int what, int id);
    }
}

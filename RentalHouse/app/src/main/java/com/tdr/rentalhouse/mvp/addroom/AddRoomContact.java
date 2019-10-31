package com.tdr.rentalhouse.mvp.addroom;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

import retrofit2.http.Field;

/**
 * Author：Li Bin on 2019/7/23 13:02
 * Description：
 */
public class AddRoomContact {
    interface Presenter extends BasePresenter<BaseView> {
        void addRoom(int what, long id, long unitId, String list);
    }
}

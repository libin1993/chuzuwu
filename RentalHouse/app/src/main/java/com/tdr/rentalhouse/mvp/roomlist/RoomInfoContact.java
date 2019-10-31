package com.tdr.rentalhouse.mvp.roomlist;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Li Bin on 2019/7/24 09:22
 * Description：
 */
public class RoomInfoContact {
    interface Presenter extends BasePresenter<BaseView> {
        void roomList(int what, int  id);
    }
}

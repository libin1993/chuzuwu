package com.tdr.rentalhouse.mvp.editroom;

import com.tdr.rentalhouse.base.BasePresenter;
import com.tdr.rentalhouse.base.BaseView;

/**
 * Author：Li Bin on 2019/7/23 13:26
 * Description：
 */
public class EditRoomContact {
    interface Presenter extends BasePresenter<BaseView> {
        void editRoom(int what, int id, int communityId, int unitId, String floorNo, String roomNo);

        void deleteHouse(int what, int id);
    }
}

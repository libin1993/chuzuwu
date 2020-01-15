package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

/**
 * Author：Libin on 2020/1/13 0013 17:30
 * Email：1993911441@qq.com
 * Describe：
 */
public class EquipmentBean extends BaseBean<EquipmentBean.DataBean> {


    public static class DataBean {
        /**
         * RoomUnique : 00000291
         * BuildingUnique : 157893736872
         */

        private String RoomUnique;
        private String BuildingUnique;

        public String getRoomUnique() {
            return RoomUnique;
        }

        public void setRoomUnique(String RoomUnique) {
            this.RoomUnique = RoomUnique;
        }

        public String getBuildingUnique() {
            return BuildingUnique;
        }

        public void setBuildingUnique(String BuildingUnique) {
            this.BuildingUnique = BuildingUnique;
        }
    }
}

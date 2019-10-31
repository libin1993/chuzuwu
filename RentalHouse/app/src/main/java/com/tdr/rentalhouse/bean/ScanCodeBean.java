package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

/**
 * Author：Li Bin on 2019/7/23 15:56
 * Description：
 */
public class ScanCodeBean extends BaseBean<ScanCodeBean.DataBean> {

    public static class DataBean {
        /**
         * Id : 19
         * Code : 999012
         */

        private int Id;
        private int FloorId;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getFloorId() {
            return FloorId;
        }

        public void setFloorId(int floorId) {
            FloorId = floorId;
        }
    }
}

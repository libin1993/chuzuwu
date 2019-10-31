package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

/**
 * Author：Li Bin on 2019/7/22 10:52
 * Description：
 */
public class AddAddressBean extends BaseBean<AddAddressBean.DataBean> {

    public static class DataBean {
        /**
         * Id : 23
         * OutlookOne : /Upload/1/2019/07/22//cb9f1886-8d2c-4709-90cb-8deb77b06b53.jpg
         * CommunityName : 222
         * DetailAddress : 济宁市任城区南张街道办事处房家村村委会5522
         */

        private int Id;
        private String OutlookOne;
        private String CommunityName;
        private String DetailAddress;
        private Integer FloorId;

        public Integer getFloorId() {
            return FloorId;
        }

        public void setFloorId(Integer floorId) {
            FloorId = floorId;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getOutlookOne() {
            return OutlookOne;
        }

        public void setOutlookOne(String OutlookOne) {
            this.OutlookOne = OutlookOne;
        }

        public String getCommunityName() {
            return CommunityName;
        }

        public void setCommunityName(String CommunityName) {
            this.CommunityName = CommunityName;
        }

        public String getDetailAddress() {
            return DetailAddress;
        }

        public void setDetailAddress(String DetailAddress) {
            this.DetailAddress = DetailAddress;
        }
    }
}

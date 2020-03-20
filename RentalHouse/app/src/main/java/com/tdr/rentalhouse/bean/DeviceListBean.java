package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Libin on 2020-03-19 10:58
 * Email：1993911441@qq.com
 * Describe：商品房设施列表
 */
public class DeviceListBean extends BaseBean<DeviceListBean.DataBean> {

    public static class DataBean {
        /**
         * CommunityName : 锦江凤凰城
         * BlockNumber : 1
         * UnitNumber : 1
         * AddressDetail : 杭州市萧山区宁围街道二桥村委会锦江凤凰城1单元1层101室
         * LandlordName : lhq
         * Phone : 18358581613
         * ManageId : 40
         * List : [{"DevIcon":null,"DeviceName":"6666设备","DeviceNumber":"6666","DeviceType":"1068","DeviceBindId":14}]
         */

        private String CommunityName;
        private String BlockNumber;
        private String UnitNumber;
        private String AddressDetail;
        private String LandlordName;
        private String Phone;
        private int ManageId;
        private java.util.List<ListBean> List;

        public String getCommunityName() {
            return CommunityName;
        }

        public void setCommunityName(String CommunityName) {
            this.CommunityName = CommunityName;
        }

        public String getBlockNumber() {
            return BlockNumber;
        }

        public void setBlockNumber(String BlockNumber) {
            this.BlockNumber = BlockNumber;
        }

        public String getUnitNumber() {
            return UnitNumber;
        }

        public void setUnitNumber(String UnitNumber) {
            this.UnitNumber = UnitNumber;
        }

        public String getAddressDetail() {
            return AddressDetail;
        }

        public void setAddressDetail(String AddressDetail) {
            this.AddressDetail = AddressDetail;
        }

        public String getLandlordName() {
            return LandlordName;
        }

        public void setLandlordName(String LandlordName) {
            this.LandlordName = LandlordName;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String Phone) {
            this.Phone = Phone;
        }

        public int getManageId() {
            return ManageId;
        }

        public void setManageId(int ManageId) {
            this.ManageId = ManageId;
        }

        public List<ListBean> getList() {
            return List;
        }

        public void setList(List<ListBean> List) {
            this.List = List;
        }

        public static class ListBean {
            /**
             * DevIcon : null
             * DeviceName : 6666设备
             * DeviceNumber : 6666
             * DeviceType : 1068
             * DeviceBindId : 14
             */

            private Object DevIcon;
            private String DeviceName;
            private String DeviceNumber;
            private String DeviceType;
            private int DeviceBindId;
            private String DeviceTypeName;
            private List<String> DevicePicture;

            public List<String> getDevicePicture() {
                return DevicePicture;
            }

            public void setDevicePicture(List<String> devicePicture) {
                DevicePicture = devicePicture;
            }

            public String getDeviceTypeName() {
                return DeviceTypeName;
            }

            public void setDeviceTypeName(String deviceTypeName) {
                DeviceTypeName = deviceTypeName;
            }

            public Object getDevIcon() {
                return DevIcon;
            }

            public void setDevIcon(Object DevIcon) {
                this.DevIcon = DevIcon;
            }

            public String getDeviceName() {
                return DeviceName;
            }

            public void setDeviceName(String DeviceName) {
                this.DeviceName = DeviceName;
            }

            public String getDeviceNumber() {
                return DeviceNumber;
            }

            public void setDeviceNumber(String DeviceNumber) {
                this.DeviceNumber = DeviceNumber;
            }

            public String getDeviceType() {
                return DeviceType;
            }

            public void setDeviceType(String DeviceType) {
                this.DeviceType = DeviceType;
            }

            public int getDeviceBindId() {
                return DeviceBindId;
            }

            public void setDeviceBindId(int DeviceBindId) {
                this.DeviceBindId = DeviceBindId;
            }
        }
    }
}

package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Libin on 2020-03-19 10:48
 * Email：1993911441@qq.com
 * Describe：
 */
public class SelfBuildingDeviceBean extends BaseBean<SelfBuildingDeviceBean.DataBean> {


    public static class DataBean {
        /**
         * Id : 10
         * OutlookOne : null
         * CommunityName : 锦江凤凰城
         * Address : 杭州市萧山区宁围街道二桥村委会锦江凤凰城
         * UnitList : [{"Id":5,"BuildingNumber":"1","UnitNumber":"1"}]
         * List : [{"DevIcon":null,"DeviceName":"520设备","DeviceNumber":"520","DeviceType":"1068","DeviceBindId":12},{"DevIcon":null,"DeviceName":"521设备","DeviceNumber":"521","DeviceType":"1068","DeviceBindId":13}]
         * LandlordName : null
         * Phone : null
         */

        private int Id;
        private String OutlookOne;
        private String CommunityName;
        private String Address;
        private String LandlordName;
        private String Phone;
        private String RDNumber;
        private String guid;
        private List<UnitListBean> UnitList;
        private List<DeviceListBean> List;

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getRDNumber() {
            return RDNumber;
        }

        public void setRDNumber(String RDNumber) {
            this.RDNumber = RDNumber;
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

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
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

        public List<UnitListBean> getUnitList() {
            return UnitList;
        }

        public void setUnitList(List<UnitListBean> UnitList) {
            this.UnitList = UnitList;
        }

        public List<DeviceListBean> getList() {
            return List;
        }

        public void setList(List<DeviceListBean> DeviceList) {
            this.List = DeviceList;
        }

        public static class UnitListBean {
            /**
             * Id : 5
             * BuildingNumber : 1
             * UnitNumber : 1
             */

            private int Id;
            private String BuildingNumber;
            private String UnitNumber;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getBuildingNumber() {
                return BuildingNumber;
            }

            public void setBuildingNumber(String BuildingNumber) {
                this.BuildingNumber = BuildingNumber;
            }

            public String getUnitNumber() {
                return UnitNumber;
            }

            public void setUnitNumber(String UnitNumber) {
                this.UnitNumber = UnitNumber;
            }
        }

        public static class DeviceListBean {
            /**
             * DevIcon : null
             * DeviceName : 520设备
             * DeviceNumber : 520
             * DeviceType : 1068
             * DeviceBindId : 12
             */

            private Object DevIcon;
            private String DeviceName;
            private String DeviceNumber;
            private String DeviceType;
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

            private int DeviceBindId;
            private String DeviceTypeName;

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

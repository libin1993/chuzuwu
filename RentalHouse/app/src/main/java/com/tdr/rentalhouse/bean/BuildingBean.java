package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Libin on 2020-03-17 15:54
 * Email：1993911441@qq.com
 * Describe：
 */
public class BuildingBean extends BaseBean<BuildingBean.DataBean> {


    public static class DataBean {
        /**
         * OutLookOne : null
         * AreaName : 杭州市萧山区宁围街道二桥村委会锦江凤凰城1幢1单元
         * BuildingNumber : 1
         * UnitNumber : 1
         * FloorList : [{"BigRoomId":-1,"HouseName":"楼道","DeviceCount":0},{"BigRoomId":-2,"HouseName":"通道","DeviceCount":0},{"BigRoomId":3,"HouseName":"101","DeviceCount":1}]
         */

        private Object OutLookOne;
        private String AreaName;
        private String BuildingNumber;
        private String UnitNumber;
        private List<FloorListBean> FloorList;

        public Object getOutLookOne() {
            return OutLookOne;
        }

        public void setOutLookOne(Object OutLookOne) {
            this.OutLookOne = OutLookOne;
        }

        public String getAreaName() {
            return AreaName;
        }

        public void setAreaName(String AreaName) {
            this.AreaName = AreaName;
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

        public List<FloorListBean> getFloorList() {
            return FloorList;
        }

        public void setFloorList(List<FloorListBean> FloorList) {
            this.FloorList = FloorList;
        }

        public static class FloorListBean {
            /**
             * BigRoomId : -1
             * HouseName : 楼道
             * DeviceCount : 0
             */

            private int BigRoomId;
            private String HouseName;
            private int DeviceCount;

            public int getBigRoomId() {
                return BigRoomId;
            }

            public void setBigRoomId(int BigRoomId) {
                this.BigRoomId = BigRoomId;
            }

            public String getHouseName() {
                return HouseName;
            }

            public void setHouseName(String HouseName) {
                this.HouseName = HouseName;
            }

            public int getDeviceCount() {
                return DeviceCount;
            }

            public void setDeviceCount(int DeviceCount) {
                this.DeviceCount = DeviceCount;
            }
        }
    }
}

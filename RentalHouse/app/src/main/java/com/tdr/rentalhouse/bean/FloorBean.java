package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Li Bin on 2019/7/23 11:10
 * Description：
 */
public class FloorBean extends BaseBean<FloorBean.DataBean> {

    public static class DataBean {
        /**
         * AreaName : 亲亲家园
         * BuildingNumber : 1幢
         * UnitNumber : 1单元
         * FloorNumber : null
         * HouseNumber : null
         * floorList : [{"FloorName":"1层","houseList":[{"Id":31,
         *
         * HouseName":"1"},{"Id":32,"HouseName":"2"}]},{"FloorName":"2层","houseList":[{"Id":33,"HouseName":"1"}]},{"FloorName":"3层","houseList":[{"Id":34,"HouseName":"1"}]},{"FloorName":"4层","houseList":[{"Id":35,"HouseName":"1"}]}]
         */

        private String AreaName;
        private String BuildingNumber;
        private String UnitNumber;
        private String FloorNumber;
        private String HouseNumber;
        private List<FloorListBean> floorList;

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

        public String getFloorNumber() {
            return FloorNumber;
        }

        public void setFloorNumber(String FloorNumber) {
            this.FloorNumber = FloorNumber;
        }

        public String getHouseNumber() {
            return HouseNumber;
        }

        public void setHouseNumber(String HouseNumber) {
            this.HouseNumber = HouseNumber;
        }

        public List<FloorListBean> getFloorList() {
            return floorList;
        }

        public void setFloorList(List<FloorListBean> floorList) {
            this.floorList = floorList;
        }

        public static class FloorListBean {
            /**
             * FloorName : 1层
             * houseList : [{"Id":31,"HouseName":"1"},{"Id":32,"HouseName":"2"}]
             */

            private String FloorName;
            private List<HouseListBean> houseList;

            public String getFloorName() {
                return FloorName;
            }

            public void setFloorName(String FloorName) {
                this.FloorName = FloorName;
            }

            public List<HouseListBean> getHouseList() {
                return houseList;
            }

            public void setHouseList(List<HouseListBean> houseList) {
                this.houseList = houseList;
            }

            public static class HouseListBean {
                /**
                 * Id : 31
                 * HouseName : 1
                 */

                private int Id;
                private String HouseName;

                public int getId() {
                    return Id;
                }

                public void setId(int Id) {
                    this.Id = Id;
                }

                public String getHouseName() {
                    return HouseName;
                }

                public void setHouseName(String HouseName) {
                    this.HouseName = HouseName;
                }
            }
        }
    }
}

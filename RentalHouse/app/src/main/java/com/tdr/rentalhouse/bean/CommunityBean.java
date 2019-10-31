package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Li Bin on 2019/7/22 14:59
 * Description：
 */
public class CommunityBean extends BaseBean<CommunityBean.DataBean> {

    public static class DataBean {
        /**
         * Id : 1
         * OutlookOne : null
         * CommunityName : 迎春小区
         * Address : 济宁市邹城市城前镇城前村民委员会测试路108号迎春小区
         * list : [{"Id":1,"Name":"1幢1单元"}]
         */

        private int Id;
        private String OutlookOne;
        private String CommunityName;
        private String Address;
        private List<ListBean> list;

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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * Id : 1
             * Name : 1幢1单元
             */

            private int Id;
            private String Name;
            private String BuildingNumber;
            private String UnitNumber;

            public String getBuildingNumber() {
                return BuildingNumber;
            }

            public void setBuildingNumber(String buildingNumber) {
                BuildingNumber = buildingNumber;
            }

            public String getUnitNumber() {
                return UnitNumber;
            }

            public void setUnitNumber(String unitNumber) {
                UnitNumber = unitNumber;
            }

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }
        }
    }
}

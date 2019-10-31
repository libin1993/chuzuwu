package com.tdr.rentalhouse.bean;

import android.content.Intent;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Li Bin on 2019/7/26 15:31
 * Description：
 */
public class HouseBean extends BaseBean<HouseBean.DataBean> {

    public static class DataBean {
        /**
         * Id : 1
         * LandlordName : 张三
         * IDNumber : 1264647897494964
         * Phone : 1
         * QRId : null
         * Code : null
         * OutlookOne : null
         * Longitude : 50
         * Latitude : 60
         * RDNumber : 测试路108号
         * CommunityName : 迎春小区
         * BuildingNumber : 1
         * UnitNumber : 1
         * FloorNumber : 1
         * RoomNumber : 2
         * ArchitecturalTypes : 2
         * list : [{"Id":1,"RoomNumber":"502号房","SortNo":null}]
         */

        private int Id;
        private String LandlordName;
        private String IDNumber;
        private String Phone;
        private String Code;
        private String AreaCode;
        private String OutlookOne;
        private String Longitude;
        private String Latitude;
        private String RDNumber;
        private String CommunityName;
        private String BuildingNumber;
        private String UnitNumber;
        private String FloorNumber;
        private String RoomNumber;
        private int ArchitecturalTypes;
        private int BussinessType;
        private List<ListBean> list;
        private String CityName;
        private String AreaName;
        private String SteetName;
        private String ComminityName;

        public int getBussinessType() {
            return BussinessType;
        }

        public void setBussinessType(int bussinessType) {
            BussinessType = bussinessType;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String cityName) {
            CityName = cityName;
        }

        public String getAreaName() {
            return AreaName;
        }

        public void setAreaName(String areaName) {
            AreaName = areaName;
        }

        public String getSteetName() {
            return SteetName;
        }

        public void setSteetName(String steetName) {
            SteetName = steetName;
        }

        public String getComminityName() {
            return ComminityName;
        }

        public void setComminityName(String comminityName) {
            ComminityName = comminityName;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getLandlordName() {
            return LandlordName;
        }

        public void setLandlordName(String LandlordName) {
            this.LandlordName = LandlordName;
        }

        public String getIDNumber() {
            return IDNumber;
        }

        public void setIDNumber(String IDNumber) {
            this.IDNumber = IDNumber;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String Phone) {
            this.Phone = Phone;
        }

        public String getAreaCode() {
            return AreaCode;
        }

        public void setAreaCode(String areaCode) {
            AreaCode = areaCode;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getOutlookOne() {
            return OutlookOne;
        }

        public void setOutlookOne(String OutlookOne) {
            this.OutlookOne = OutlookOne;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String Longitude) {
            this.Longitude = Longitude;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String Latitude) {
            this.Latitude = Latitude;
        }

        public String getRDNumber() {
            return RDNumber;
        }

        public void setRDNumber(String RDNumber) {
            this.RDNumber = RDNumber;
        }

        public String getCommunityName() {
            return CommunityName;
        }

        public void setCommunityName(String CommunityName) {
            this.CommunityName = CommunityName;
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

        public String getRoomNumber() {
            return RoomNumber;
        }

        public void setRoomNumber(String RoomNumber) {
            this.RoomNumber = RoomNumber;
        }

        public int getArchitecturalTypes() {
            return ArchitecturalTypes;
        }

        public void setArchitecturalTypes(int ArchitecturalTypes) {
            this.ArchitecturalTypes = ArchitecturalTypes;
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
             * RoomNumber : 502号房
             * SortNo : null
             */

            private int Id;
            private String RoomNumber;
            private Integer SortNo;
            private int IsDefault;

            public int getIsDefault() {
                return IsDefault;
            }

            public void setIsDefault(int isDefault) {
                this.IsDefault = isDefault;
            }

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getRoomNumber() {
                return RoomNumber;
            }

            public void setRoomNumber(String RoomNumber) {
                this.RoomNumber = RoomNumber;
            }

            public Integer getSortNo() {
                return SortNo;
            }

            public void setSortNo(Integer SortNo) {
                this.SortNo = SortNo;
            }
        }
    }
}

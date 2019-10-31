package com.tdr.rentalhouse.bean;

import com.google.gson.annotations.SerializedName;
import com.tdr.rentalhouse.base.BaseBean;

import java.io.Serializable;

/**
 * Author：Li Bin on 2019/7/22 17:04
 * Description：
 */
public class CommunityDetailBean extends BaseBean<CommunityDetailBean.DataBean> {

    public static class DataBean implements Serializable {
        /**
         * ProvinceCode : null
         * CityCode :
         * AreaCode : 370883000000
         * Street : 370883102000
         * Community : 370883102200
         * ArchitecturalTypes : 2
         * CommunityName : 迎春小区
         * RDNumber : 测试路108号
         * Longitude : 116.587245
         * Latitude : 35.415393
         * OutlookOne : null
         * OutlookTwo : null
         * OutlookThree : null
         * CreateTime : 2019-07-04T20:11:07
         * CreateId : null
         * PoliceStationsCode : 3708020009006
         * Describe : 济宁市邹城市城前镇城前村民委员会测试路108号迎春小区
         * Id : 1
         */

        private Object ProvinceCode;
        private String CityCode;
        private String AreaCode;
        private String Street;
        private String Community;
        private int ArchitecturalTypes;
        private String CommunityName;
        private String RDNumber;
        private String Longitude;
        private String Latitude;
        private String OutlookOne;
        private String OutlookTwo;
        private String OutlookThree;
        private String CreateTime;
        private Object CreateId;
        private String PoliceStationsCode;
        private String PoliceStationsName;

        private int Id;

        public String getPoliceStationsName() {
            return PoliceStationsName;
        }

        public void setPoliceStationsName(String policeStationsName) {
            PoliceStationsName = policeStationsName;
        }

        private String PositionName;

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

        private String CityName;
        private String AreaName;
        private String SteetName;
        private String ComminityName;

        public String getPositionName() {
            return PositionName;
        }

        public void setPositionName(String positionName) {
            PositionName = positionName;
        }

        public Object getProvinceCode() {
            return ProvinceCode;
        }

        public void setProvinceCode(Object ProvinceCode) {
            this.ProvinceCode = ProvinceCode;
        }

        public String getCityCode() {
            return CityCode;
        }

        public void setCityCode(String CityCode) {
            this.CityCode = CityCode;
        }

        public String getAreaCode() {
            return AreaCode;
        }

        public void setAreaCode(String AreaCode) {
            this.AreaCode = AreaCode;
        }

        public String getStreet() {
            return Street;
        }

        public void setStreet(String Street) {
            this.Street = Street;
        }

        public String getCommunity() {
            return Community;
        }

        public void setCommunity(String Community) {
            this.Community = Community;
        }

        public int getArchitecturalTypes() {
            return ArchitecturalTypes;
        }

        public void setArchitecturalTypes(int ArchitecturalTypes) {
            this.ArchitecturalTypes = ArchitecturalTypes;
        }

        public String getCommunityName() {
            return CommunityName;
        }

        public void setCommunityName(String CommunityName) {
            this.CommunityName = CommunityName;
        }

        public String getRDNumber() {
            return RDNumber;
        }

        public void setRDNumber(String RDNumber) {
            this.RDNumber = RDNumber;
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

        public String getOutlookOne() {
            return OutlookOne;
        }

        public void setOutlookOne(String OutlookOne) {
            this.OutlookOne = OutlookOne;
        }

        public String getOutlookTwo() {
            return OutlookTwo;
        }

        public void setOutlookTwo(String OutlookTwo) {
            this.OutlookTwo = OutlookTwo;
        }

        public String getOutlookThree() {
            return OutlookThree;
        }

        public void setOutlookThree(String OutlookThree) {
            this.OutlookThree = OutlookThree;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }

        public Object getCreateId() {
            return CreateId;
        }

        public void setCreateId(Object CreateId) {
            this.CreateId = CreateId;
        }

        public String getPoliceStationsCode() {
            return PoliceStationsCode;
        }

        public void setPoliceStationsCode(String PoliceStationsCode) {
            this.PoliceStationsCode = PoliceStationsCode;
        }


        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }
    }
}

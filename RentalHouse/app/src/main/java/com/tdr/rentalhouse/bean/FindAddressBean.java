package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Li Bin on 2019/7/22 13:17
 * Description：
 */
public class FindAddressBean extends BaseBean<List<FindAddressBean.DataBean>> {


    public static class DataBean {
        /**
         * Id : 1
         * ArchitecturalTypes : 2
         * CityCode : 370800000000
         * AreaCode : 370828000000
         * Street : 370828102000
         * Community : 370828102204
         * Longitude : 40.081433
         * Latitude : 116.587866
         * Describe : 济宁市金乡县胡集镇金店村民委员会测试路108号迎春小区
         */

        private int Id;
        private int ArchitecturalTypes;
        private String CityCode;
        private String AreaCode;
        private String Street;
        private String Community;
        private String Longitude;
        private String Latitude;
        private String Describe;
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

        public int getArchitecturalTypes() {
            return ArchitecturalTypes;
        }

        public void setArchitecturalTypes(int ArchitecturalTypes) {
            this.ArchitecturalTypes = ArchitecturalTypes;
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

        public String getDescribe() {
            return Describe;
        }

        public void setDescribe(String Describe) {
            this.Describe = Describe;
        }
    }
}

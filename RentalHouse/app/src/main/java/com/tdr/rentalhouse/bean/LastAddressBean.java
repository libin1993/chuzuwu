package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

/**
 * Author：Libin on 2020-03-17 12:27
 * Email：1993911441@qq.com
 * Describe：
 */
public class LastAddressBean extends BaseBean<LastAddressBean.DataBean> {


    public static class DataBean {
        /**
         * ProvinceName : 浙江省
         * ProvinceCode : 330000000000
         * CityName : 杭州市
         * CityCode : 330100000000
         * CountyName : 萧山区
         * CountyCode : 330109000000
         * CommunityName : 宁围街道
         * CommunityCode : 330109013000
         * StreetcodeName : 二桥村委会
         * StreetcodeCode : 330109013221
         */

        private String ProvinceName;
        private String ProvinceCode;
        private String CityName;
        private String CityCode;
        private String CountyName;
        private String CountyCode;
        private String CommunityName;
        private String CommunityCode;
        private String StreetcodeName;
        private String StreetcodeCode;

        public String getProvinceName() {
            return ProvinceName;
        }

        public void setProvinceName(String ProvinceName) {
            this.ProvinceName = ProvinceName;
        }

        public String getProvinceCode() {
            return ProvinceCode;
        }

        public void setProvinceCode(String ProvinceCode) {
            this.ProvinceCode = ProvinceCode;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String CityName) {
            this.CityName = CityName;
        }

        public String getCityCode() {
            return CityCode;
        }

        public void setCityCode(String CityCode) {
            this.CityCode = CityCode;
        }

        public String getCountyName() {
            return CountyName;
        }

        public void setCountyName(String CountyName) {
            this.CountyName = CountyName;
        }

        public String getCountyCode() {
            return CountyCode;
        }

        public void setCountyCode(String CountyCode) {
            this.CountyCode = CountyCode;
        }

        public String getCommunityName() {
            return CommunityName;
        }

        public void setCommunityName(String CommunityName) {
            this.CommunityName = CommunityName;
        }

        public String getCommunityCode() {
            return CommunityCode;
        }

        public void setCommunityCode(String CommunityCode) {
            this.CommunityCode = CommunityCode;
        }

        public String getStreetcodeName() {
            return StreetcodeName;
        }

        public void setStreetcodeName(String StreetcodeName) {
            this.StreetcodeName = StreetcodeName;
        }

        public String getStreetcodeCode() {
            return StreetcodeCode;
        }

        public void setStreetcodeCode(String StreetcodeCode) {
            this.StreetcodeCode = StreetcodeCode;
        }
    }
}

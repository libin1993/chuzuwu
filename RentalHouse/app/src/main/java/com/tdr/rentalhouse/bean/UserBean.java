package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

/**
 * Author：Li Bin on 2019/7/18 14:12
 * Description：
 */
public class UserBean extends BaseBean<UserBean.DataBean> {

    public static class DataBean {
        /**
         * Token : DF776DF17C0970FE5A22754D63F85AF6B6B3EFD9E2A90EDF
         * AccountName : admin
         * UserName : 系统管理员
         * Contact :
         * Administrative : 0
         * RoleName : 平台系统管理员
         */

        private String Token;
        private String AccountName;
        private int AccountId;
        private String UserName;
        private String Contact;
        private int Administrative;
        private String RoleName;
        private String PoliceStationName;
        private String PoliceStationCode;
        private String OfficeCode;
        private String OfficeName;
        private String AreaName;

        public String getOfficeCode() {
            return OfficeCode;
        }

        public void setOfficeCode(String officeCode) {
            OfficeCode = officeCode;
        }

        public String getOfficeName() {
            return OfficeName;
        }

        public void setOfficeName(String officeName) {
            OfficeName = officeName;
        }

        public String getAreaName() {
            return AreaName;
        }

        public void setAreaName(String areaName) {
            AreaName = areaName;
        }

        public String getPoliceStationCode() {
            return PoliceStationCode;
        }

        public void setPoliceStationCode(String policeStationCode) {
            PoliceStationCode = policeStationCode;
        }

        private String GoverCommunityName;
        private String GoverCommunityCode;

        public String getGoverCommunityCode() {
            return GoverCommunityCode;
        }

        public void setGoverCommunityCode(String goverCommunityCode) {
            GoverCommunityCode = goverCommunityCode;
        }


        public int getAccountId() {
            return AccountId;
        }

        public void setAccountId(int accountId) {
            AccountId = accountId;
        }


        public String getPoliceStationName() {
            return PoliceStationName;
        }

        public void setPoliceStationName(String policeStationName) {
            PoliceStationName = policeStationName;
        }

        public String getGoverCommunityName() {
            return GoverCommunityName;
        }

        public void setGoverCommunityName(String goverCommunityName) {
            GoverCommunityName = goverCommunityName;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String Token) {
            this.Token = Token;
        }

        public String getAccountName() {
            return AccountName;
        }

        public void setAccountName(String AccountName) {
            this.AccountName = AccountName;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getContact() {
            return Contact;
        }

        public void setContact(String Contact) {
            this.Contact = Contact;
        }

        public int getAdministrative() {
            return Administrative;
        }

        public void setAdministrative(int Administrative) {
            this.Administrative = Administrative;
        }

        public String getRoleName() {
            return RoleName;
        }

        public void setRoleName(String RoleName) {
            this.RoleName = RoleName;
        }
    }
}



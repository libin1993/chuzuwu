package com.tdr.rentalhouse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author：Li Bin on 2019/7/23 11:39
 * Description：
 */
public class HouseInfoBean implements Serializable {
    private String cityName;
    private String cityCode;
    private String areaName;
    private String areaCode;
    private String streetName;
    private String streetCode;
    private String residentialName;
    private String residentialCode;
    private int communityId;
    private String communityName;
    private String buildingName;
    private int unitId;
    private String unitName;
    private String FloorName;
    private int houseId;
    private String houseName;
    private int buildingType;
    private String img;
    private int roomId;
    private String roomName;
    private String landlordName;
    private String idNo;
    private String phone;
    private String qrCode;

    private String addressCode;
    private int businessType;

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    private String lng;
    private int ManageId;
    private int EquipRoomBindId;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getResidentialCode() {
        return residentialCode;
    }

    public void setResidentialCode(String residentialCode) {
        this.residentialCode = residentialCode;
    }

    private String areaNumber;

    public String getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(String areaNumber) {
        this.areaNumber = areaNumber;
    }

    public int getManageId() {
        return ManageId;
    }

    public void setManageId(int manageId) {
        ManageId = manageId;
    }

    public int getEquipRoomBindId() {
        return EquipRoomBindId;
    }

    public void setEquipRoomBindId(int equipRoomBindId) {
        EquipRoomBindId = equipRoomBindId;
    }

    private List<Room> roomList;

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public static class Room implements Serializable {
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

        public void setId(int id) {
            Id = id;
        }

        public String getRoomNumber() {
            return RoomNumber;
        }

        public void setRoomNumber(String roomNumber) {
            RoomNumber = roomNumber;
        }

        public Integer getSortNo() {
            return SortNo;
        }

        public void setSortNo(Integer sortNo) {
            SortNo = sortNo;
        }
    }

    public String getLandlordName() {
        return landlordName;
    }

    public void setLandlordName(String landlordName) {
        this.landlordName = landlordName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }


    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    private String lat;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(int buildingType) {
        this.buildingType = buildingType;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getResidentialName() {
        return residentialName;
    }

    public void setResidentialName(String residentialName) {
        this.residentialName = residentialName;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getFloorName() {
        return FloorName;
    }

    public void setFloorName(String floorName) {
        FloorName = floorName;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }
}

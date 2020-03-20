package com.tdr.rentalhouse.bean;

/**
 * Author：Libin on 2020-03-19 14:48
 * Email：1993911441@qq.com
 * Describe：
 */
public class UnitListBean {

    /**
     * Id : 1
     * Name : 1幢1单元
     */

    private int Id;

    private String BuildingNumber;
    private String UnitNumber;

    public UnitListBean(int id, String buildingNumber, String unitNumber) {
        Id = id;
        BuildingNumber = buildingNumber;
        UnitNumber = unitNumber;
    }

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

}

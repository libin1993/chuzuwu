package com.tdr.rentalhouse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author：Li Bin on 2019/7/22 11:00
 * Description：
 */
@Entity(nameInDb = "history_address")
public class HistoryAddress {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "community_id")
    private int community_id;

    @Property(nameInDb = "address")
    private String address;

    @Property(nameInDb = "building_type")
    private int building_type;

    @Property(nameInDb = "floor_id")
    private int floor_id;
    
    @Property(nameInDb = "user_id")
    private int user_id;

    @Property(nameInDb = "install_type")
    private int install_type;

    @Generated(hash = 1889585393)
    public HistoryAddress(Long id, int community_id, String address,
            int building_type, int floor_id, int user_id, int install_type) {
        this.id = id;
        this.community_id = community_id;
        this.address = address;
        this.building_type = building_type;
        this.floor_id = floor_id;
        this.user_id = user_id;
        this.install_type = install_type;
    }

    @Generated(hash = 401529754)
    public HistoryAddress() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCommunity_id() {
        return this.community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBuilding_type() {
        return this.building_type;
    }

    public void setBuilding_type(int building_type) {
        this.building_type = building_type;
    }

    public int getFloor_id() {
        return this.floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getInstall_type() {
        return this.install_type;
    }

    public void setInstall_type(int install_type) {
        this.install_type = install_type;
    }





}

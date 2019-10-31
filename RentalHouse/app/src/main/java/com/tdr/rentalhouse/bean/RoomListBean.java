package com.tdr.rentalhouse.bean;

import com.tdr.rentalhouse.base.BaseBean;

import java.util.List;

/**
 * Author：Li Bin on 2019/7/24 09:17
 * Description：
 */
public class RoomListBean extends BaseBean<RoomListBean.DataBean> {

    public static class DataBean {
        private int BussinessType;

        public int getBussinessType() {
            return BussinessType;
        }

        public void setBussinessType(int bussinessType) {
            BussinessType = bussinessType;
        }

        private FloorInfosBean FloorInfos;

        public FloorInfosBean getFloorInfos() {
            return FloorInfos;
        }

        public void setFloorInfos(FloorInfosBean floorInfos) {
            FloorInfos = floorInfos;
        }

        private List<RoomEntityBean> RoomEntity;

        public List<RoomEntityBean> getRoomEntity() {
            return RoomEntity;
        }

        public void setRoomEntity(List<RoomEntityBean> RoomEntity) {
            this.RoomEntity = RoomEntity;
        }

        public static class  FloorInfosBean{
            private int ManageId;
            private Integer EquipRoomBindId;
            private String EquipmentNumber;
            private Integer EquipmentState;
            private Integer DeviceStatus;


            public Integer getDeviceStatus() {
                return DeviceStatus;
            }

            public void setDeviceStatus(Integer deviceStatus) {
                DeviceStatus = deviceStatus;
            }

            public Integer getEquipRoomBindId() {
                return EquipRoomBindId;
            }

            public void setEquipRoomBindId(Integer equipRoomBindId) {
                EquipRoomBindId = equipRoomBindId;
            }

            public String getEquipmentNumber() {
                return EquipmentNumber;
            }

            public void setEquipmentNumber(String equipmentNumber) {
                EquipmentNumber = equipmentNumber;
            }

            public Integer getEquipmentState() {
                return EquipmentState;
            }

            public void setEquipmentState(Integer equipmentState) {
                EquipmentState = equipmentState;
            }

            public int getManageId() {
                return ManageId;
            }

            public void setManageId(int manageId) {
                ManageId = manageId;
            }
        }

        public static class RoomEntityBean {
            /**
             * RoomId : 3
             * RoomNumber : 3
             * EquipRoomBindId : null
             * EquipmentNumber : null
             * EquipmentState : null
             * ManageId : 3
             */

            private int RoomId;
            private String RoomNumber;
            private Integer EquipRoomBindId;
            private String EquipmentNumber;
            private Integer EquipmentState;
            private int ManageId;
            private Integer DeviceStatus;


            public Integer getDeviceStatus() {
                return DeviceStatus;
            }

            public void setDeviceStatus(Integer deviceStatus) {
                DeviceStatus = deviceStatus;
            }

            public int getRoomId() {
                return RoomId;
            }

            public void setRoomId(int RoomId) {
                this.RoomId = RoomId;
            }

            public String getRoomNumber() {
                return RoomNumber;
            }

            public void setRoomNumber(String RoomNumber) {
                this.RoomNumber = RoomNumber;
            }

            public Integer getEquipRoomBindId() {
                return EquipRoomBindId;
            }

            public void setEquipRoomBindId(Integer EquipRoomBindId) {
                this.EquipRoomBindId = EquipRoomBindId;
            }

            public String getEquipmentNumber() {
                return EquipmentNumber;
            }

            public void setEquipmentNumber(String EquipmentNumber) {
                this.EquipmentNumber = EquipmentNumber;
            }

            public Integer getEquipmentState() {
                return EquipmentState;
            }

            public void setEquipmentState(Integer EquipmentState) {
                this.EquipmentState = EquipmentState;
            }

            public int getManageId() {
                return ManageId;
            }

            public void setManageId(int ManageId) {
                this.ManageId = ManageId;
            }
        }
    }
}

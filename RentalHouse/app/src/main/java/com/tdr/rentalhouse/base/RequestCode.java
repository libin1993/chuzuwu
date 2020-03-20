package com.tdr.rentalhouse.base;

/**
 * Author：Libin on 2019/7/6 15:20
 * Description：
 */
public class RequestCode {
    public static class Permission {
        public static final int NECESSARY_PERMISSION = 0x0001;
        public static final int CAMERA_PERMISSION = 0x0002;
    }

    public static class NetCode {
        public static final int LOGIN = 0x1001;   //登录
        public static final int ADD_ADDRESS = 0x1002;   //地址采集
        public static final int GET_INFO = 0x1003;   //用户信息
        public static final int UPLOAD = 0x1004;   //上传图片
        public static final int UPDATE_PWD = 0x1005;   //修改密码
        public static final int FEEDBACK = 0x1006;   //反馈
        public static final int FIND_ADDRESS = 0x1007;   //查找地址
        public static final int COMMUNITY_INFO = 0x1008;   //小区信息
        public static final int ADD_UNIT = 0x1009;   //添加单元
        public static final int EDIT_UNIT = 0x100a;   //编辑单元
        public static final int DELETE_UNIT = 0x100b;   //删除单元
        public static final int COMMUNITY_DETAIL = 0x100c;   //小区详情
        public static final int EDIT_COMMUNITY = 0x100d;   //小区详情
        public static final int GET_FLOOR = 0x100e;   //楼层房间
        public static final int ADD_ROOM = 0x100f;   //新增楼层房间
        public static final int EDIT_ROOM = 0x1010;   //编辑楼层房间
        public static final int BIND_HOUSE = 0x1011;   //房屋绑定
        public static final int SCAN_CODE = 0x1012;   //扫码
        public static final int ROOM_LIST = 0x1013;   //房屋列表
        public static final int HOUSE_INFO = 0x1014;   //房屋信息
        public static final int INSTALL_EQUIPMENT = 0x1015;   //安装设备
        public static final int DELETE_HOUSE = 0x1016;   //删除楼层房间
        public static final int IS_EQUIPMENT_BIND = 0x1018;   //设备号校验
        public static final int FIND_NEARBY_ADDRESS = 0x1019;   //查找附近地址
        public static final int CITY_LIST = 0x101A;   //市列表
        public static final int AREA_LIST = 0x101B;   //区列表
        public static final int DEVICE_TYPE= 0x101C;   //设备编号
        public static final int BUSINESS_TYPE= 0x101D;   //业务类型
        public static final int LAST_ADDRESS= 0x101E;   //上次区域


        public static final int SEARCH_ADDRESS= 0x101F;   //消防报装查找地址
        public static final int BUILDING_INFO= 0x1021;   //消防报装楼栋信息
        public static final int SELF_BUILDING_DEVICE= 0x1022;   //消防报装自建房设备列表
        public static final int BUILDING_DEVICE= 0x1023;   //消防报装商品房设备列表
        public static final int ADD_DEVICE= 0x1024;   //消防报装添加/编辑ai烟感设备
        public static final int ADD_OTHER_DEVICE= 0x1025;   //消防报装添加/编辑其他设备
        public static final int DEVICE_DETAIL= 0x1026;   //消防报装设备详情
        public static final int DELETE_DEVICE= 0x1027;   //消防报装设备删除
        public static final int DEVICE_NAME= 0x1028;   //获得设备类型名称
        public static final int FIRE_CONTROL_DEVICE_TYPE= 0x1029;   //设备编号识别
        public static final int SEARCH_NEARBY_ADDRESS= 0x102A;   //消防报装附近地址
        public static final int EDIT_DEVICE_NAME= 0x102B;   //消防报装修改点位信息
    }
}

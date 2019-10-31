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
        public static final int IS_EQUIPMENT_BIND = 0x1018;   //设备是否已绑定
        public static final int FIND_NEARBY_ADDRESS = 0x1009;   //查找附近地址
        public static final int CITY_LIST = 0x100A;   //市列表
        public static final int AREA_LIST = 0x100B;   //区列表
        public static final int DEVICE_TYPE= 0x100C;   //设备编号
        public static final int BUSINESS_TYPE= 0x100D;   //业务类型
    }
}

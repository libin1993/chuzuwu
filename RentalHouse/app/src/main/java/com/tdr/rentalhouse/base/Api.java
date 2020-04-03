package com.tdr.rentalhouse.base;


import com.tdr.rentalhouse.bean.AddAddressBean;
import com.tdr.rentalhouse.bean.AddressBean;
import com.tdr.rentalhouse.bean.BuildingBean;
import com.tdr.rentalhouse.bean.BusinessTypeBean;
import com.tdr.rentalhouse.bean.CityBean;
import com.tdr.rentalhouse.bean.CommunityBean;
import com.tdr.rentalhouse.bean.CommunityDetailBean;
import com.tdr.rentalhouse.bean.DeviceDetailBean;
import com.tdr.rentalhouse.bean.DeviceListBean;
import com.tdr.rentalhouse.bean.EquipmentBean;
import com.tdr.rentalhouse.bean.FindAddressBean;
import com.tdr.rentalhouse.bean.FloorBean;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.bean.LastAddressBean;
import com.tdr.rentalhouse.bean.RoomListBean;
import com.tdr.rentalhouse.bean.ScanCodeBean;
import com.tdr.rentalhouse.bean.SelfBuildingDeviceBean;
import com.tdr.rentalhouse.bean.UserBean;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/**
 * Author：Libin on 2019/05/31 09:34
 * Email：1993911441@qq.com
 * Describe：接口
 */
public interface Api {
    //内网开发
    String HOST = "http://10.130.0.207:1012/";         //出租屋报装地址
    String FIRE_HOST = "http://10.130.0.207:8810/";    //消防报装地址
    String IMG_HOST = "http://10.130.0.207:1012";      //图片地址
    String VERSION = "开发版";


    //外网开发
//    String HOST = "http://yf-androidapi.ioegrid.com:22800/";         //出租屋报装地址
//    String FIRE_HOST = "http://yf-androidapi0.ioegrid.com:22800/";   //消防报装地址
//    String IMG_HOST = "http://yf-androidapi.ioegrid.com:22800";      //图片地址
//    String VERSION = "开发版";


    //报装系统正式
//    String HOST = "http://bzappapi.iotone.cn/";        //出租屋报装地址
//    String IMG_HOST = "http://bzappapi.iotone.cn";     //图片地址
//    String VERSION = "";

    //出租屋
//    String HOST = "http://183.129.130.119:17090/";       //出租屋报装地址
//    String IMG_HOST = "http://183.129.130.119:17090";    //图片地址
//    String VERSION = "";

    //演示
//    String HOST = "http://ys-czwappapi.iotone.cn:22800/";       //出租屋报装地址
//    String IMG_HOST = "http://ys-czwappapi.iotone.cn:22800";    //图片地址
//    String VERSION = "";


    //外网测试
//    String HOST = "http://183.129.130.119:13127/";        //出租屋报装地址
//    String FIRE_HOST = "http://183.129.130.119:13162/";   //消防报装地址
//    String IMG_HOST = "http://183.129.130.119:13127";     //图片地址
//    String VERSION = "测试版";


    //内网测试
//    String HOST = "http://10.130.0.208:13127/";        //出租屋报装地址
//    String FIRE_HOST = "http://10.130.0.208:13162/";   //消防报装地址
//    String IMG_HOST = "http://10.130.0.208:13127";     //图片地址
//    String VERSION = "测试版";


    String URL_RENTAL_HOUSE = "url_name:rental_house";    //出租屋
    String URL_FIRE_CONTROL = "url_name:fire_control";   //消防


    //登录
    @FormUrlEncoded
    @Headers(URL_RENTAL_HOUSE)
    @POST("api/Login")
    Observable<UserBean> login(@Field("AccountName") String account, @Field("Password") String password);

    //获取登录信息

    @GET("api/Login")
    @Headers(URL_RENTAL_HOUSE)
    Observable<UserBean> getInfo(@Query("AccountId") int accountId);

    //获取所有地址
    @GET("api/Area/GetAllList")
    @Headers(URL_RENTAL_HOUSE)
    Observable<AddressBean> getAllAddress();


    //地址采集
    @FormUrlEncoded
    @POST("api/Residential")
    @Headers(URL_RENTAL_HOUSE)
    Observable<AddAddressBean> addAddress(@FieldMap Map<String, Object> map);

    //上次区域
    @GET("api/Area/GetMyArea")
    @Headers(URL_RENTAL_HOUSE)
    Observable<LastAddressBean> getLastAddress();

    //上传图片
    @Multipart
    @POST("api/File")
    @Headers(URL_RENTAL_HOUSE)
    Observable<BaseBean> upload(@PartMap() Map<String, RequestBody> map);

    //查找地址
    @GET("api/Residential")
    @Headers(URL_RENTAL_HOUSE)
    Observable<FindAddressBean> findAddress(@QueryMap Map<String, Object> map);


    //修改密码
    @FormUrlEncoded
    @Headers(URL_RENTAL_HOUSE)
    @POST("api/Password/UpdatePassword")
    Observable<BaseBean> updatePwd(@Field("OldPassword") String oldPwd, @Field("NewPassword") String newPwd);


    //意见反馈
    @FormUrlEncoded
    @Headers(URL_RENTAL_HOUSE)
    @POST("api/FeedBack")
    Observable<BaseBean> feedback(@Field("Content") String content);

    //小区信息
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/Residential")
    Observable<CommunityBean> getCommunityInfo(@Query("id") int id, @Query("guid") String guid);

    //添加单元
    @FormUrlEncoded
    @POST("api/Unit")
    Observable<BaseBean> addUnit(@FieldMap Map<String, Object> map);

    //编辑单元
    @FormUrlEncoded
    @Headers(URL_RENTAL_HOUSE)
    @POST("api/UnitEdit")
    Observable<BaseBean> editUnit(@FieldMap Map<String, Object> map);


    //删除单元
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/UnitDelete")
    Observable<BaseBean> deleteUnit(@Query("UnitId") int id);

    //小区详情
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/ResidentialEdit")
    Observable<CommunityDetailBean> communityDetail(@Query("id") int id);

    //编辑小区
    @Headers(URL_RENTAL_HOUSE)
    @FormUrlEncoded
    @POST("api/ResidentialEdit")
    Observable<BaseBean> editCommunity(@FieldMap Map<String, Object> map);

    //楼层房间
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/UnitEdit")
    Observable<FloorBean> getFloor(@Query("id") int id, @Query("guid") String guid);

    //增加楼层房间
    @Headers(URL_RENTAL_HOUSE)
    @FormUrlEncoded
    @POST("api/Floor")
    Observable<BaseBean> addRoom(@Field("ResidentialId") long id, @Field("UnitId") long unitId,
                                 @Field("list") String list);


    //修改楼层房间
    @Headers(URL_RENTAL_HOUSE)
    @FormUrlEncoded
    @POST("api/FloorEdit/UpdateFloor")
    Observable<BaseBean> editRoom(@Field("Id") int id, @Field("ResidentialId") int communityId,
                                  @Field("UnitId") int unitId, @Field("FloorNo") String floorNo,
                                  @Field("RoomNo") String roomNo);

    //删除楼层房间
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/FloorDelete")
    Observable<BaseBean> deleteHouse(@Query("FloorId") int id);

    //房屋绑定
    @Headers(URL_RENTAL_HOUSE)
    @FormUrlEncoded
    @POST("api/FloorEdit/UpdateHOwnerInfo")
    Observable<BaseBean> bindHouse(@Field("Id") int floorId, @Field("LandlordName") String landlordName,
                                   @Field("IDNumber") String idNumber, @Field("Phone") String phone,
                                   @Field("BussinessType") int businessTye,
                                   @Field("Code") String code, @Field("AreaCode") String areaCode,
                                   @Field("QRContent") String qrCode, @Field("Longitude") String lng,
                                   @Field("Latitude") String lat, @Field("list") String list);

    //扫描房屋二维码
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/QRCode")
    Observable<ScanCodeBean> scanCode(@Query("code") String code, @Query("AreaCode") String areaCode);

    //获取房屋信息
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/FloorEdit/GetHOwnerInfo")
    Observable<HouseBean> getHouseInfo(@Query("FloorId") int id);

    //安装设备房屋信息
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/Dev/HouseInfo")
    Observable<RoomListBean> getRoomList(@Query("FloorId") int id);


    //绑定设备
    @FormUrlEncoded
    @Headers(URL_RENTAL_HOUSE)
    @POST("api/Dev/InstallOrReplace")
    Observable<BaseBean> installEquipment(@FieldMap Map<String, Object> map);


    //校验设备二维码
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/ScanDeviceQR")
    Observable<EquipmentBean> isEquipmentBind(@Query("EquipNo") Long equipmentNumber, @Query("EquipType") Long equipmentType,
                                              @Query("BussinessType") int businessTye, @Query("FloorId") int floorId,
                                              @Query("RoomId") int roomId);


    //市列表
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/Area/GetAreaListByCity")
    Observable<CityBean> getCityList(@Query("UnionCode") String code);




    //获取业务类型
    @Headers(URL_RENTAL_HOUSE)
    @GET("api/Dev/GetBusinessType")
    Observable<BusinessTypeBean> getBusinessType();


    //消防报装查找地址
    @GET("api/AddressDev/GetSearchList")
    @Headers(URL_FIRE_CONTROL)
    Observable<FindAddressBean> searchAddress(@QueryMap Map<String, Object> map);


    //消防报装楼栋信息
    @Headers(URL_FIRE_CONTROL)
    @GET("api/AddressDev/GetUnitInfo")
    Observable<BuildingBean> getBuildingInfo(@Query("Id") int id);


    //消防报装自建房设施列表
    @Headers(URL_FIRE_CONTROL)
    @GET("api/AddressDev/GetResidentalInfo")
    Observable<SelfBuildingDeviceBean> getSelfBuildingDevice(@Query("Id") int id, @Query("CurrentPage") int currentPage);


    //消防报装商品房设施列表
    @Headers(URL_FIRE_CONTROL)
    @GET("api/AddressDev/GetDeviceList")
    Observable<DeviceListBean> getBuildingDevice(@Query("BigRoomId") int id, @Query("UnitId") int unitId, @Query("CurrentPage") int currentPage);


    //消防报装添加/编辑ai烟感设备
    @FormUrlEncoded
    @Headers(URL_FIRE_CONTROL)
    @POST("api/AddressDev/InstallOrReplace")
    Observable<BaseBean> installDevice(@FieldMap Map<String, Object> map);

    //消防报装添加/编辑其他设备
    @FormUrlEncoded
    @Headers(URL_FIRE_CONTROL)
    @POST("api/AddressDev/InstallOrReplaceOther")
    Observable<BaseBean> installOtherDevice(@FieldMap Map<String, Object> map, @Field("DevicePicture") ArrayList<String> list);


    //消防报装设备详情
    @Headers(URL_FIRE_CONTROL)
    @GET("api/AddressDev/DeviceDetail")
    Observable<DeviceDetailBean> deviceDetail(@Query("BindId") int bindId);

    //消防报装删除设备
    @Headers(URL_FIRE_CONTROL)
    @GET("api/AddressDev/RemoveDevice")
    Observable<BaseBean> deleteDevice(@Query("BindId") int bindId);

    //消防报装获得设备类型名称
    @Headers(URL_FIRE_CONTROL)
    @GET("api/AddressDev/GetDeviceTypeName")
    Observable<BaseBean<String>> deviceName(@Query("Code") String code);

    //消防报装设备编号识别
    @Headers(URL_FIRE_CONTROL)
    @GET("api/AddressDev/ScanDeviceQR")
    Observable<EquipmentBean> deviceType(@Query("EquipNo") String equipmentNumber, @Query("EquipType") String equipmentType,
                                         @Query("UnitId") int unitId);

    //消防报装修改点位信息
    @Headers(URL_FIRE_CONTROL)
    @GET("api/AddressDev/UpdateDevceName")
    Observable<BaseBean> editDeviceName(@Query("BindId") int bindId, @Query("DeviceName") String deviceName);

}


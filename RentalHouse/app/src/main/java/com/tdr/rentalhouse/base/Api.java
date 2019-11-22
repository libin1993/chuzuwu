package com.tdr.rentalhouse.base;


import com.tdr.rentalhouse.bean.AddAddressBean;
import com.tdr.rentalhouse.bean.AddressBean;
import com.tdr.rentalhouse.bean.BusinessTypeBean;
import com.tdr.rentalhouse.bean.CityBean;
import com.tdr.rentalhouse.bean.CommunityBean;
import com.tdr.rentalhouse.bean.CommunityDetailBean;
import com.tdr.rentalhouse.bean.FindAddressBean;
import com.tdr.rentalhouse.bean.FloorBean;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.bean.RoomListBean;
import com.tdr.rentalhouse.bean.ScanCodeBean;
import com.tdr.rentalhouse.bean.UserBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
    //测试
//    String HOST = "http://10.130.0.207:1012/";
//    String IMG_HOST = "http://10.130.0.207:1012";


    //出租屋正式
//    String HOST = "http://183.129.130.119:17090/";
//    String IMG_HOST = "http://183.129.130.119:17090";

    //报装系统正式
//    String HOST = "http://183.129.130.119:17100/";
//    String IMG_HOST = "http://183.129.130.119:17100";


    //测试（不上架）
//    String HOST = "http://10.130.0.208:13127/";
//    String IMG_HOST = "http://10.130.0.208:13127";


    //演示
//    String HOST = "http://10.120.0.18:8000/";
//    String IMG_HOST = "http://10.120.0.18:8000";


    //域名
    String HOST = "http://ys-czwappapi.iotone.cn:22800/";
    String IMG_HOST = "http://ys-czwappapi.iotone.cn:22800";


    //登录
    @FormUrlEncoded
    @POST("api/Login")
    Observable<UserBean> login(@Field("AccountName") String account, @Field("Password") String password);

    //获取登录信息
    @GET("api/Login")
    Observable<UserBean> getInfo(@Query("AccountId") int accountId);

    //获取所有地址
    @GET("api/Area/GetAllList")
    Observable<AddressBean> getAllAddress();


    //地址采集
    @FormUrlEncoded
    @POST("api/Residential")
    Observable<AddAddressBean> addAddress(@FieldMap Map<String, Object> map);

    //上传图片
    @Multipart
    @POST("api/File")
    Observable<BaseBean> upload(@PartMap() Map<String, RequestBody> map);

    //查找地址
    @GET("api/Residential")
    Observable<FindAddressBean> findAddress(@QueryMap Map<String, Object> map);


    //修改密码
    @FormUrlEncoded
    @POST("api/Password/UpdatePassword")
    Observable<BaseBean> updatePwd(@Field("OldPassword") String oldPwd, @Field("NewPassword") String newPwd);


    //意见反馈
    @FormUrlEncoded
    @POST("api/FeedBack")
    Observable<BaseBean> feedback(@Field("Content") String content);

    //小区信息
    @GET("api/Residential")
    Observable<CommunityBean> getCommunityInfo(@Query("id") int id);

    //添加单元
    @FormUrlEncoded
    @POST("api/Unit")
    Observable<BaseBean> addUnit(@FieldMap Map<String, Object> map);

    //编辑单元
    @FormUrlEncoded
    @POST("api/UnitEdit")
    Observable<BaseBean> editUnit(@FieldMap Map<String, Object> map);


    //删除单元
    @GET("api/UnitDelete")
    Observable<BaseBean> deleteUnit(@Query("UnitId") int id);

    //小区详情
    @GET("api/ResidentialEdit")
    Observable<CommunityDetailBean> communityDetail(@Query("id") int id);

    //编辑小区
    @FormUrlEncoded
    @POST("api/ResidentialEdit")
    Observable<BaseBean> editCommunity(@FieldMap Map<String, Object> map);

    //楼层房间
    @GET("api/UnitEdit")
    Observable<FloorBean> getFloor(@Query("id") int id);

    //增加楼层房间
    @FormUrlEncoded
    @POST("api/Floor")
    Observable<BaseBean> addRoom(@Field("ResidentialId") long id, @Field("UnitId") long unitId,
                                 @Field("list") String list);


    //修改楼层房间
    @FormUrlEncoded
    @POST("api/FloorEdit/UpdateFloor")
    Observable<BaseBean> editRoom(@Field("Id") int id, @Field("ResidentialId") int communityId,
                                  @Field("UnitId") int unitId, @Field("FloorNo") String floorNo,
                                  @Field("RoomNo") String roomNo);

    //删除楼层房间
    @GET("api/FloorDelete")
    Observable<BaseBean> deleteHouse(@Query("FloorId") int id);

    //房屋绑定
    @FormUrlEncoded
    @POST("api/FloorEdit/UpdateHOwnerInfo")
    Observable<BaseBean> bindHouse(@Field("Id") int floorId, @Field("LandlordName") String landlordName,
                                   @Field("IDNumber") String idNumber, @Field("Phone") String phone,
                                   @Field("BussinessType") int businessTye,
                                   @Field("Code") String code, @Field("AreaCode") String areaCode,
                                   @Field("QRContent") String qrCode, @Field("Longitude") String lng,
                                   @Field("Latitude") String lat, @Field("list") String list);

    //扫描二维码
    @GET("api/QRCode")
    Observable<ScanCodeBean> scanCode(@Query("code") String code, @Query("AreaCode") String areaCode);

    //获取房屋信息
    @GET("api/FloorEdit/GetHOwnerInfo")
    Observable<HouseBean> getHouseInfo(@Query("FloorId") int id);

    //安装设备房屋信息
    @GET("api/Dev/HouseInfo")
    Observable<RoomListBean> getRoomList(@Query("FloorId") int id);


    //绑定设备
    @FormUrlEncoded
    @POST("api/Dev/InstallOrReplace")
    Observable<BaseBean> installEquipment(@FieldMap Map<String, Object> map);


    //设备是否已绑定
    @GET("api/ScanDeviceQR")
    Observable<BaseBean> isEquipmentBind(@Query("EquipNo") Long equipmentNumber, @Query("EquipType") Long equipmentType,
                                         @Query("BussinessType") int businessTye);


    //市列表
    @GET("api/Area/GetAreaListByCity")
    Observable<CityBean> getCityList(@Query("UnionCode") String code);

    //判断设备编号
    @GET("api/Dev/DeviceType")
    Observable<BaseBean> deviceType(@Query("DeviceNo") String code);


    //获取所有地址
    @GET("api/Dev/GetBusinessType")
    Observable<BusinessTypeBean> getBusinessType();


}


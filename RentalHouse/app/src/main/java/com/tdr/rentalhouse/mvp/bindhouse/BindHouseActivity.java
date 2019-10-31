package com.tdr.rentalhouse.mvp.bindhouse;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.bean.BusinessTypeBean;
import com.tdr.rentalhouse.mvp.addaddress.AddAddressActivity;
import com.tdr.rentalhouse.mvp.communitydetail.CommunityDetailActivity;
import com.tdr.rentalhouse.mvp.scancode.ScanQRCodeActivity;
import com.tdr.rentalhouse.base.Api;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.LocationBean;
import com.tdr.rentalhouse.bean.ScanResult;
import com.tdr.rentalhouse.utils.Base64Utils;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.GsonUtils;
import com.tdr.rentalhouse.utils.LimitInputTextWatcher;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.PermissionUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;

/**
 * Author：Li Bin on 2019/7/15 14:41
 * Description：房屋绑定
 */
public class BindHouseActivity extends BaseMvpActivity<BindHouseContact.Presenter> implements BaseView, AMapLocationListener {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.tv_unit_name)
    TextView tvUnitName;
    @BindView(R.id.rv_unit)
    RecyclerView rvUnit;

    private EditText etLandlordName;
    private EditText etIdCard;
    private EditText etPhoneNumber;
    private TextView tvHouseCode;
    private TextView tvHouseLat;
    private TextView tvBusinessType;

    private List<HouseInfoBean.Room> dataList = new ArrayList<>();
    private BaseQuickAdapter<HouseInfoBean.Room, BaseViewHolder> adapter;
    private HouseInfoBean houseInfoBean;
    private AMapLocationClient mLocationClient;

    private LocationBean locationBean;
    private View tvNullRoom;
    private View viewLine;
    private SimpleDraweeView ivCommunity;
    private TextView tvAddress;
    private TextView tvName;
    private String areaCode;
    private String code;
    private String qrCode;
    private int businessType;

    private List<BusinessTypeBean.DataBean> typeList = new ArrayList<>();
    private OptionsPickerView pickerView;


    @Override
    protected BindHouseContact.Presenter initPresenter() {
        return new BindHousePresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_house);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getData();
        initView();

        if (houseInfoBean.getType() != 4) {
            initLocation();
        }

    }

    /**
     * 配置定位参数
     */
    private void initLocation() {

        //初始化定位参数
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(this);

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(Hight_Accuracy);

        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        //启动定位
        mLocationClient.startLocation();
    }


    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");
    }


    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("房屋绑定");
        tvTitleMore.setVisibility(View.VISIBLE);
        tvTitleMore.setText("完成");
        if (houseInfoBean.getBuildingType() == 1 || houseInfoBean.getBuildingType() == 4) {
            tvUnitName.setVisibility(View.GONE);
        } else {
            tvUnitName.setVisibility(View.VISIBLE);
            String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢/";
            if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                address += houseInfoBean.getUnitName() + "单元/";
            }
            address += houseInfoBean.getFloorName() + "层/" + houseInfoBean.getHouseName() + "室";
            tvUnitName.setText(address);
        }


        pickerView = new OptionsPickerBuilder(BindHouseActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                businessType = typeList.get(options1).getBusinessType();
                tvBusinessType.setText(typeList.get(options1).getShowName());
            }
        }).setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(15)//滚轮文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setSubmitColor(getResources().getColor(R.color.blue_3f5))//确定按钮文字颜色
                .setSubCalSize(13)
                .setCancelColor(getResources().getColor(R.color.gray_33))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.gray_f2))//标题背景颜色 Night mode
                .build();


        rvUnit.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<HouseInfoBean.Room, BaseViewHolder>(R.layout.layout_room_item_edit, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, HouseInfoBean.Room item) {
                helper.setText(R.id.tv_room_name, item.getRoomNumber());
                helper.setVisible(R.id.iv_delete_room, item.getIsDefault() != 1);
                helper.addOnClickListener(R.id.iv_delete_room);
                helper.addOnClickListener(R.id.tv_room_name);

            }

        };

        if (houseInfoBean.getBuildingType() == 1 || houseInfoBean.getBuildingType() == 4) {
            View header1 = LayoutInflater.from(this).inflate(R.layout.layout_manager_address_header, null);
            ivCommunity = header1.findViewById(R.id.iv_estate);
            tvName = header1.findViewById(R.id.tv_estate_name);
            tvAddress = header1.findViewById(R.id.tv_estate_address);

            ivCommunity.setImageURI(Api.IMG_HOST + houseInfoBean.getImg());
            tvName.setText(houseInfoBean.getAreaNumber());


            String address = houseInfoBean.getCityName();
            if (!TextUtils.isEmpty(houseInfoBean.getAreaName())) {
                address += houseInfoBean.getAreaName();
            }

            if (!TextUtils.isEmpty(houseInfoBean.getStreetName())) {
                address += houseInfoBean.getStreetName();
            }

            if (!TextUtils.isEmpty(houseInfoBean.getResidentialName())) {
                address += houseInfoBean.getResidentialName();
            }

            if (!TextUtils.isEmpty(houseInfoBean.getAreaNumber())) {
                address += houseInfoBean.getAreaNumber();
            }


            tvAddress.setText(address);

            header1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BindHouseActivity.this, CommunityDetailActivity.class);
                    intent.putExtra("id", houseInfoBean.getCommunityId());
                    startActivity(intent);
                }
            });
            adapter.addHeaderView(header1);
        }


        final View view = LayoutInflater.from(this).inflate(R.layout.layout_bind_room_header, null);
        etLandlordName = view.findViewById(R.id.et_landlord_name);
        etLandlordName.addTextChangedListener(new LimitInputTextWatcher(etLandlordName));
        etIdCard = view.findViewById(R.id.et_id_card);
        etPhoneNumber = view.findViewById(R.id.et_phone_number);
        tvHouseCode = view.findViewById(R.id.tv_house_code);
        tvHouseLat = view.findViewById(R.id.tv_house_lat);
        tvBusinessType = view.findViewById(R.id.tv_business_type);
        LinearLayout llBusinessType = view.findViewById(R.id.ll_business_type);
        LinearLayout llLocation = view.findViewById(R.id.ll_house_location);
        LinearLayout llHouseCode = view.findViewById(R.id.ll_house_no);


        llHouseCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });

        llBusinessType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getBusinessType(RequestCode.NetCode.BUSINESS_TYPE);
//                selectBusinessType();
            }
        });

        adapter.addHeaderView(view);


        View viewFooter = LayoutInflater.from(this).inflate(R.layout.layout_room_footer_add, null);
        tvNullRoom = viewFooter.findViewById(R.id.tv_null_room);
        viewLine = viewFooter.findViewById(R.id.view_line_add);
        LinearLayout llAddRoom = viewFooter.findViewById(R.id.ll_add_room_item);
        adapter.addFooterView(viewFooter);

        llAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HouseInfoBean.Room room = new HouseInfoBean.Room();

                if (dataList.size() == 0) {
                    room.setSortNo(dataList.size() + 1);
                    room.setRoomNumber("房间" + (dataList.size() + 1));
                } else {
                    int sortNo = dataList.get(dataList.size() - 1).getSortNo();
                    room.setSortNo(sortNo + 1);

                    ++sortNo;
                    String roomName;
                    while (true) {
                        roomName = "房间" + sortNo;

                        boolean isContain = false;
                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).getRoomNumber().contains(roomName)) {
                                isContain = true;
                                break;
                            }
                        }
                        if (isContain) {
                            ++sortNo;
                        } else {
                            break;
                        }

                    }

                    room.setRoomNumber(roomName);
                }
                room.setIsDefault(2);
                dataList.add(room);

                tvNullRoom.setVisibility(dataList.size() > 0 ? View.GONE : View.VISIBLE);
                viewLine.setVisibility(dataList.size() > 0 ? View.GONE : View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        });

        rvUnit.setAdapter(adapter);


        if (houseInfoBean.getType() == 4) {
            etLandlordName.setText(houseInfoBean.getLandlordName());
            etPhoneNumber.setText(houseInfoBean.getPhone());
            etIdCard.setText(houseInfoBean.getIdNo());
            code = houseInfoBean.getQrCode();
            businessType = houseInfoBean.getBusinessType();
            areaCode = houseInfoBean.getAddressCode();
            tvHouseCode.setText(houseInfoBean.getQrCode());
            if (houseInfoBean.getBusinessType() == 1) {
                tvBusinessType.setText("出租屋");
            } else {
                tvBusinessType.setText("旅业");
            }

            locationBean = new LocationBean();
            locationBean.setName(houseInfoBean.getLng() + "," + houseInfoBean.getLat());
            locationBean.setLat(Double.parseDouble(houseInfoBean.getLat()));
            locationBean.setLng(Double.parseDouble(houseInfoBean.getLng()));


            tvHouseLat.setText(houseInfoBean.getLng() + "," + houseInfoBean.getLat());


            if (ObjectUtils.getInstance().isNotNull(houseInfoBean.getRoomList())) {
                dataList.addAll(houseInfoBean.getRoomList());
            }
            tvNullRoom.setVisibility(dataList.size() > 0 ? View.GONE : View.VISIBLE);
            viewLine.setVisibility(dataList.size() > 0 ? View.GONE : View.VISIBLE);
            adapter.notifyDataSetChanged();

        }

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_delete_room:
                        dataList.remove(position);
                        adapter.notifyDataSetChanged();
                        tvNullRoom.setVisibility(dataList.size() > 0 ? View.GONE : View.VISIBLE);
                        viewLine.setVisibility(dataList.size() > 0 ? View.GONE : View.VISIBLE);
                        break;
                    case R.id.tv_room_name:
                        updateRoomName(position);
                        break;
                }

            }
        });
    }

    /**
     * 业务类型
     */
    private void selectBusinessType() {

        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_popup_business_type, null);
        final PopupWindow mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                FormatUtils.getInstance().dp2px(156));

        //设置Popup具体控件
        TextView tvRentalHouse = contentView.findViewById(R.id.tv_rental_house);
        TextView tvHotel = contentView.findViewById(R.id.tv_hotel);
        TextView tvCancel = contentView.findViewById(R.id.tv_close_popup);


        //设置Popup具体参数
        mPopupWindow.setContentView(contentView);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.setFocusable(true);//点击空白，popup不自动消失
        mPopupWindow.setTouchable(true);//popup区域可触摸
        mPopupWindow.setOutsideTouchable(true);//非popup区域可触摸

        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        StatusBarUtils.getInstance().setBackgroundAlpha(BindHouseActivity.this, true);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                StatusBarUtils.getInstance().setBackgroundAlpha(BindHouseActivity.this, false);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        tvRentalHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                businessType = 1;
                tvBusinessType.setText("出租屋");
                mPopupWindow.dismiss();
            }
        });


        tvHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                businessType = 2;
                tvBusinessType.setText("旅业");
                mPopupWindow.dismiss();
            }
        });


    }

    /**
     * @param position 修改房间名
     */
    private void updateRoomName(final int position) {
        final PopupWindow mPopupWindow = new PopupWindow();
        View mPopBackView = LayoutInflater.from(this).inflate(R.layout.ppw_update_room, null);

        //设置Popup具体控件
        TextView tvCancel = mPopBackView.findViewById(R.id.tv_cancel_update);
        TextView tvConfirm = mPopBackView.findViewById(R.id.tv_update_room);
        final EditText etName = mPopBackView.findViewById(R.id.tv_update_name);

        etName.setText(dataList.get(position).getRoomNumber());
        etName.addTextChangedListener(new LimitInputTextWatcher(etName, LimitInputTextWatcher.DEFAULT_REGEX));


        //设置Popup具体参数
        mPopupWindow.setContentView(mPopBackView);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置宽
        mPopupWindow.setHeight(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置高
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.setFocusable(true);//点击空白，popup不自动消失
        mPopupWindow.setTouchable(true);//popup区域可触摸
        mPopupWindow.setOutsideTouchable(true);//非popup区域可触摸


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    ToastUtils.getInstance().showToast("请输入房间名称");
                    return;
                }

                boolean flag = true;
                for (int i = 0; i < dataList.size(); i++) {
                    if (i != position && dataList.get(i).getRoomNumber().equals(etName.getText().toString().trim())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    mPopupWindow.dismiss();
                    dataList.get(position).setRoomNumber(etName.getText().toString().trim());
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.getInstance().showToast("已存在相同房间名称");
                }
            }
        });

        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 相机权限
     */
    private void getPermission() {
        if (PermissionUtils.getInstance().hasPermission(this, Manifest.permission.CAMERA)) {
            startActivity(new Intent(BindHouseActivity.this, ScanQRCodeActivity.class));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RequestCode.Permission.CAMERA_PERMISSION);
        }
    }


    @OnClick({R.id.iv_title_back, R.id.tv_title_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_more:
                if (FastClickUtils.isSingleClick()) {
                    submitData();
                }
                break;
        }
    }

    /**
     * 上传数据
     */
    private void submitData() {
        String landlordName = etLandlordName.getText().toString().trim();
        String idNo = etIdCard.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(landlordName)) {
            ToastUtils.getInstance().showToast("请输入房东姓名");
            return;
        }

        if (!FormatUtils.getInstance().IsIDCard(idNo)) {
            ToastUtils.getInstance().showToast("请输入有效的身份证号码");
            return;
        }

        if (!FormatUtils.getInstance().IsHandset(phone)) {
            ToastUtils.getInstance().showToast("请输入有效的手机号码");
            return;
        }

//        if (TextUtils.isEmpty(code)) {
//            ToastUtils.getInstance().showToast("请扫描房屋二维码");
//            return;
//        }
        if (TextUtils.isEmpty(tvBusinessType.getText().toString().trim())) {
            ToastUtils.getInstance().showToast("请选择所属业务类型");
            return;
        }

        if (TextUtils.isEmpty(tvHouseLat.getText().toString().trim())) {
            ToastUtils.getInstance().showToast("请输入房屋经纬度");
            return;
        }

        LoadingUtils.getInstance().showLoading(this, "加载中");


        String list = "";
        if (dataList.size() > 0) {
            list = GsonUtils.listToString(dataList);
        }

        mPresenter.bindHouse(RequestCode.NetCode.BIND_HOUSE, houseInfoBean.getHouseId(),
                landlordName, idNo, phone, businessType, code, areaCode, qrCode, String.valueOf(locationBean.getLng()),
                String.valueOf(locationBean.getLat()), list);

    }

    @Override
    public void onSuccess(int what, Object object) {

        switch (what) {
            case RequestCode.NetCode.BIND_HOUSE:
                if (houseInfoBean.getType() == 4) {
                    ToastUtils.getInstance().showToast("编辑成功");
                    EventBus.getDefault().post("edit_room");
                } else {
                    ToastUtils.getInstance().showToast("绑定成功");
                }

                finish();
                break;
            case RequestCode.NetCode.BUSINESS_TYPE:
                BusinessTypeBean dataBean= (BusinessTypeBean) object;
                typeList.clear();
                if (dataBean != null && ObjectUtils.getInstance().isNotNull(dataBean.getData())){
                    typeList.addAll(dataBean.getData());
                    List<String> businessList = new ArrayList<>();
                    for (BusinessTypeBean.DataBean bean : typeList) {
                        businessList.add(bean.getShowName());
                    }
                    pickerView.setPicker(businessList);
                    pickerView.show();
                }

                break;
        }

    }

    @Override
    public void onFail(int what, String msg) {
        ToastUtils.getInstance().showToast(msg);
    }

    @Override
    public void hideLoading() {
        LoadingUtils.getInstance().dismiss();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanResult(ScanResult scanResult) {
        String result = scanResult.getResult();
        if (result.contains("?AH")) {
            String[] split = result.split("\\?");
            String type1 = split[1].substring(2);
            LogUtils.log(type1);
            byte[] decode = Base64Utils.decode(type1);


            String str = FormatUtils.getInstance().bytes2Hex(decode);

            LogUtils.log(str);
            if (str.length() == 22) {
                areaCode = str.substring(0, 6);
                code = str.substring(6, 18);
                qrCode = result;
                tvHouseCode.setText(code);

                adapter.notifyDataSetChanged();
            } else {
                ToastUtils.getInstance().showToast("请扫描有效房屋二维码");
            }

        } else {
            ToastUtils.getInstance().showToast("请扫描有效房屋二维码");
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void editAddress(HouseInfoBean house) {

        ivCommunity.setImageURI(Api.IMG_HOST + house.getImg());
        tvName.setText(house.getAreaNumber());


        String address = house.getCityName();
        if (!TextUtils.isEmpty(house.getAreaName())) {
            address += house.getAreaName();
        }

        if (!TextUtils.isEmpty(house.getStreetName())) {
            address += house.getStreetName();
        }

        if (!TextUtils.isEmpty(house.getResidentialName())) {
            address += house.getResidentialName();
        }

        if (!TextUtils.isEmpty(house.getAreaNumber())) {
            address += house.getAreaNumber();
        }


        tvAddress.setText(address);

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && locationBean == null) {
            locationBean = new LocationBean();
            locationBean.setName(aMapLocation.getAddress());
            locationBean.setLat(aMapLocation.getLatitude());
            locationBean.setLng(aMapLocation.getLongitude());

            tvHouseLat.setText(locationBean.getName());

            mLocationClient.stopLocation();
            mLocationClient = null;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode.Permission.CAMERA_PERMISSION) {
            if (!PermissionUtils.getInstance().hasPermission(this, Manifest.permission.CAMERA)) {
                PermissionUtils.getInstance().showPermissionDialog(BindHouseActivity.this,
                        Manifest.permission.CAMERA, "拍照", new PermissionUtils.OnPermissionListener() {
                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onReQuest() {
                                getPermission();
                            }
                        });
            } else {
                startActivity(new Intent(BindHouseActivity.this, ScanQRCodeActivity.class));
            }
        }
    }


}

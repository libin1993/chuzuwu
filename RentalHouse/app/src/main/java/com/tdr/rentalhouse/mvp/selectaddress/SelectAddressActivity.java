package com.tdr.rentalhouse.mvp.selectaddress;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.RoomListBean;
import com.tdr.rentalhouse.bean.SelfBuildingDeviceBean;
import com.tdr.rentalhouse.inter.PopupOnClickListener;
import com.tdr.rentalhouse.mvp.bindhouse.BindHouseActivity;
import com.tdr.rentalhouse.mvp.devicelist.DeviceListActivity;
import com.tdr.rentalhouse.mvp.houseinfo.HouseInfoActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.mvp.community.CommunityActivity;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.FindAddressBean;
import com.tdr.rentalhouse.bean.HistoryAddress;
import com.tdr.rentalhouse.mvp.addaddress.AddAddressActivity;
import com.tdr.rentalhouse.mvp.roomlist.RoomInfoActivity;
import com.tdr.rentalhouse.mvp.scancode.ScanQRCodeActivity;
import com.tdr.rentalhouse.ui.SearchAddressActivity;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.PopupWindowUtils;
import com.tdr.rentalhouse.utils.SPUtils;
import com.tdr.rentalhouse.utils.SQLiteUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;
import com.tdr.rentalhouse.widget.RVDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;

/**
 * Author：Libin on 2019/7/3 16:49
 * Description：
 */
public class SelectAddressActivity extends BaseMvpActivity<SelectAddressContact.Presenter> implements BaseView, AMapLocationListener {
    @BindView(R.id.mv_address)
    MapView mapView;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.iv_title_more)
    ImageView ivTitleMore;
    @BindView(R.id.et_input_address)
    EditText etAddress;
    @BindView(R.id.tv_clear_txt)
    TextView tvClearTxt;
    @BindView(R.id.rv_select_address)
    RecyclerView rvSelectAddress;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.rgs_address)
    RadioGroup rgsAddress;
    @BindView(R.id.view_clear_line)
    View viewClearLine;
    @BindView(R.id.rv_history_address)
    RecyclerView rvHistoryAddress;

    private AMap aMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocation aMapLocation;

    private List<FindAddressBean.DataBean> findAddressList = new ArrayList<>();
    private List<HistoryAddress> historyAddressList = new ArrayList<>();
    private BaseQuickAdapter<FindAddressBean.DataBean, BaseViewHolder> findAddressAdapter;
    private BaseQuickAdapter<HistoryAddress, BaseViewHolder> historyAddressAdapter;

    //0:采集  1：安装
    private int type;
    //报装类型  1：出租屋  2：消防
    private int installType = 1;

    private int communityId;

    private int buildingType;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        ButterKnife.bind(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        getData();
        initView();
        initMap();
        initLocation();
        initHistory();
    }

    private void getData() {
        type = getIntent().getIntExtra("type", 0);
        installType = getIntent().getIntExtra("install_type", 1);
    }


    @Override
    protected SelectAddressContact.Presenter initPresenter() {
        return new SelectAddressPresenter();
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_more, R.id.iv_title_more, R.id.tv_clear_txt, R.id.iv_location})
    public void onViewClicked(View view) {
        if (FastClickUtils.isSingleClick()) {
            switch (view.getId()) {
                case R.id.iv_title_back:
                    finish();
                    break;
                case R.id.iv_title_more:
                    Intent intent = new Intent(SelectAddressActivity.this, AddAddressActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    break;
                case R.id.tv_clear_txt:
                    if (!TextUtils.isEmpty(etAddress.getText().toString().trim())) {
                        etAddress.setText(null);
                    }

                    break;
                case R.id.iv_location:
                    getLocation();
                    break;
            }
        }

    }

    private void getLocation() {
        if (aMapLocation == null) {
            return;
        }
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        //将地图移动到定位点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
    }

    /**
     * 历史地址
     */
    private void initHistory() {
        historyAddressList.clear();
        List<HistoryAddress> query = SQLiteUtils.getInstance().query(installType);
        if (ObjectUtils.getInstance().isNotNull(query)) {
            historyAddressList.addAll(query);
        }
        historyAddressAdapter.notifyDataSetChanged();
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("地址查询");
        if (type != 1) {
            ivTitleMore.setVisibility(View.VISIBLE);
        }

        rvSelectAddress.setLayoutManager(new LinearLayoutManager(this));
        rvSelectAddress.addItemDecoration(new RVDividerItemDecoration(this, FormatUtils.getInstance().dp2px(12)));
        findAddressAdapter = new BaseQuickAdapter<FindAddressBean.DataBean, BaseViewHolder>(R.layout.layout_rv_address_item, findAddressList) {
            @Override
            protected void convert(BaseViewHolder helper, FindAddressBean.DataBean item) {
                helper.setText(R.id.tv_address, item.getDescribe());
            }
        };
        rvSelectAddress.setAdapter(findAddressAdapter);
        findAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (FastClickUtils.isSingleClick()) {
                    FindAddressBean.DataBean dataBean = findAddressList.get(position);
                    saveHistoryAddress(dataBean);
                    initHistory();
                    communityId = dataBean.getId();
                    buildingType = dataBean.getArchitecturalTypes();
                    toHouse(dataBean.getArchitecturalTypes(), dataBean.getFloorId());
                }

            }
        });


        rvHistoryAddress.setLayoutManager(new LinearLayoutManager(this));
        rvHistoryAddress.addItemDecoration(new RVDividerItemDecoration(this, FormatUtils.getInstance().dp2px(12)));
        historyAddressAdapter = new BaseQuickAdapter<HistoryAddress, BaseViewHolder>(R.layout.layout_rv_address_item, historyAddressList) {
            @Override
            protected void convert(BaseViewHolder helper, HistoryAddress item) {
                helper.setText(R.id.tv_address, item.getAddress());
            }

        };
        rvHistoryAddress.setAdapter(historyAddressAdapter);

        historyAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (FastClickUtils.isSingleClick()) {
                    HistoryAddress historyAddress = historyAddressList.get(position);
                    communityId = historyAddress.getCommunity_id();
                    buildingType = historyAddress.getBuilding_type();
                    toHouse(historyAddress.getBuilding_type(), historyAddress.getFloor_id());
                }
            }
        });


        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    tvClearTxt.setVisibility(View.VISIBLE);
                    viewClearLine.setVisibility(View.VISIBLE);
                    etAddress.setCursorVisible(true);

                } else {
                    tvClearTxt.setVisibility(View.GONE);
                    viewClearLine.setVisibility(View.GONE);
                    etAddress.setCursorVisible(false);
                    rgsAddress.setVisibility(View.VISIBLE);
                    getNearbyAddress();
                }
            }
        });


        etAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(v.getText().toString().trim())) {
                    ((RadioButton) rgsAddress.getChildAt(0)).setChecked(true);
                    rgsAddress.setVisibility(View.GONE);
                    LoadingUtils.getInstance().showLoading(SelectAddressActivity.this, "加载中");
                    Map<String, Object> map = new HashMap<>();
                    map.put("SearchTxt", v.getText().toString().trim());
                    if (installType ==1){
                        mPresenter.findAddress(RequestCode.NetCode.FIND_ADDRESS, map);
                    }else if (installType == 2){
                        mPresenter.searchAddress(RequestCode.NetCode.SEARCH_ADDRESS, map);
                    }

                    return true;
                }
                return false;
            }
        });


        rgsAddress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == group.getChildAt(0).getId()) {
                    rvSelectAddress.setVisibility(View.VISIBLE);
                    rvHistoryAddress.setVisibility(View.GONE);
                } else {
                    rvSelectAddress.setVisibility(View.GONE);
                    rvHistoryAddress.setVisibility(View.VISIBLE);
                }
            }
        });

        ((RadioButton) rgsAddress.getChildAt(0)).setChecked(true);
    }

    /**
     * 保存历史地址
     *
     * @param dataBean
     */
    private void saveHistoryAddress(FindAddressBean.DataBean dataBean) {
        HistoryAddress history = SQLiteUtils.getInstance().queryAddress(dataBean.getId());
        if (history != null) {
            SQLiteUtils.getInstance().delete(history);
        }

        HistoryAddress historyAddress = new HistoryAddress();
        historyAddress.setUser_id(SPUtils.getInstance().getAccountId());
        historyAddress.setCommunity_id(dataBean.getId());
        historyAddress.setAddress(dataBean.getDescribe());
        historyAddress.setBuilding_type(dataBean.getArchitecturalTypes());
        historyAddress.setFloor_id(dataBean.getFloorId() != null && (dataBean.getArchitecturalTypes() == 1
                || dataBean.getArchitecturalTypes() == 4) ? dataBean.getFloorId() : 0);
        historyAddress.setInstall_type(installType);
        SQLiteUtils.getInstance().insert(historyAddress);
    }


    /**
     * @param buildingType
     * @param floorId
     */
    private void toHouse(int buildingType, Integer floorId) {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        if (installType ==1){
            if (buildingType == 1 || buildingType == 4) {
                mPresenter.getHouseInfo(RequestCode.NetCode.HOUSE_INFO, floorId != null ? floorId : 0);
            } else {
                mPresenter.getCommunityInfo(RequestCode.NetCode.COMMUNITY_INFO, communityId,null);
            }
        }else if (installType == 2){
            mPresenter.getCommunityDetail(RequestCode.NetCode.SELF_BUILDING_DEVICE, communityId);
        }

    }


    /**
     * * 初始化AMap类对象 aMap 地图控制器
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();//地图控制器对象

        }

        UiSettings mUiSettings = aMap.getUiSettings();

        mUiSettings.setZoomControlsEnabled(false);//地图缩放控件是否可见
        //aMap  为地图控制器对象

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_position));
        myLocationStyle.anchor(0.5f, 1.0f);
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0);// 设置圆形的边框粗细
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));//地图缩放比列
        aMap.setMyLocationEnabled(true);//设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false

    }


    /**
     * 标点
     *
     * @return
     */
    private void addMark() {

        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        if (ObjectUtils.getInstance().isNotNull(mapScreenMarkers)) {
            for (Marker mapScreenMarker : mapScreenMarkers) {
                mapScreenMarker.remove();
            }
        }


        ArrayList<MarkerOptions> markerOptionList = new ArrayList<>();


        for (FindAddressBean.DataBean dataBean : findAddressList) {
            if (dataBean !=null && !TextUtils.isEmpty(dataBean.getLatitude())){
                MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 1.0f)
                        .position(new LatLng(Double.parseDouble(dataBean.getLatitude()), Double.parseDouble(dataBean.getLongitude())))
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_map_mark)))
                        .setFlat(true);//设置marker平贴地图效果
                markerOptionList.add(markerOption);
            }
        }


        if (markerOptionList.size() > 0) {
            aMap.addMarkers(markerOptionList, true);

            LatLng latLng = new LatLng(Double.parseDouble(findAddressList.get(0).getLatitude()),
                    Double.parseDouble(findAddressList.get(0).getLongitude()));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }


    }


    /**
     * 配置定位参数
     */
    private void initLocation() {

        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationClient = new AMapLocationClient(this);
        mLocationClient.setLocationListener(this);

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(Hight_Accuracy);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);


        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        //启动定位
        mLocationClient.startLocation();

    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getLongitude() != 0) {
            if (this.aMapLocation == null) {
                this.aMapLocation = aMapLocation;
               getNearbyAddress();
            }
        }
    }

    /**
     * 获取附近地址
     */
    private void getNearbyAddress(){
        if (aMapLocation == null || aMapLocation.getLatitude() ==0 || aMapLocation.getLongitude() ==0){
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("Longitude", aMapLocation.getLongitude());
        map.put("Latitude", aMapLocation.getLatitude());
        if (installType == 1){
            mPresenter.findAddress(RequestCode.NetCode.FIND_NEARBY_ADDRESS, map);
        }else if (installType == 2){
            mPresenter.searchAddress(RequestCode.NetCode.SEARCH_NEARBY_ADDRESS, map);
        }

    }

    @Override
    public void onSuccess(int what, Object object) {
        switch (what) {

            case RequestCode.NetCode.FIND_ADDRESS:
            case RequestCode.NetCode.SEARCH_ADDRESS:
                List<FindAddressBean.DataBean> dataBeanList = (List<FindAddressBean.DataBean>) object;

                findAddressList.clear();
                if (ObjectUtils.getInstance().isNotNull(dataBeanList)) {
                    findAddressList.addAll(dataBeanList);
                } else {
                    PopupWindowUtils.getInstance().showPopWindow(SelectAddressActivity.this,
                            "房屋未采集，是否进行地址采集？", "确定", new PopupOnClickListener() {
                                @Override
                                public void onCancel() {
                                    PopupWindowUtils.getInstance().dismiss();
                                }

                                @Override
                                public void onConfirm() {
                                    PopupWindowUtils.getInstance().dismiss();
                                    Intent intent = new Intent(SelectAddressActivity.this, AddAddressActivity.class);
                                    intent.putExtra("type", 1);
                                    startActivity(intent);
                                }
                            });
                }

                addMark();
                findAddressAdapter.notifyDataSetChanged();
                break;
            case RequestCode.NetCode.FIND_NEARBY_ADDRESS:
            case RequestCode.NetCode.SEARCH_NEARBY_ADDRESS:
                List<FindAddressBean.DataBean> addressList = (List<FindAddressBean.DataBean>) object;

                findAddressList.clear();
                if (ObjectUtils.getInstance().isNotNull(addressList)) {
                    findAddressList.addAll(addressList);
                }
                addMark();
                findAddressAdapter.notifyDataSetChanged();
                break;
            case RequestCode.NetCode.HOUSE_INFO:
                HouseBean.DataBean dataBean = (HouseBean.DataBean) object;
                if (dataBean != null) {
                    Intent intent = new Intent();
                    if (type == 1) {
                        if (installType == 1){
                            intent.setClass(SelectAddressActivity.this,RoomInfoActivity.class);
                        }else if (installType == 2){
                            intent.setClass(SelectAddressActivity.this, DeviceListActivity.class);
                        }

                    } else {
                        if (TextUtils.isEmpty(dataBean.getLandlordName())) {
                            intent.setClass(SelectAddressActivity.this, BindHouseActivity.class);
                        } else {
                            intent.setClass(SelectAddressActivity.this, HouseInfoActivity.class);
                        }
                    }

                    HouseInfoBean houseInfoBean = new HouseInfoBean();
                    houseInfoBean.setHouseId(dataBean.getId());
                    houseInfoBean.setAreaNumber(dataBean.getRDNumber());
                    houseInfoBean.setBuildingType(dataBean.getArchitecturalTypes());
                    houseInfoBean.setCommunityId(communityId);
                    houseInfoBean.setInstallType(installType);

                    houseInfoBean.setImg(dataBean.getOutlookOne());
                    houseInfoBean.setCityName(dataBean.getCityName());
                    houseInfoBean.setAreaName(dataBean.getAreaName());
                    houseInfoBean.setStreetName(dataBean.getSteetName());
                    houseInfoBean.setResidentialName(dataBean.getComminityName());

                    intent.putExtra("house", houseInfoBean);
                    startActivity(intent);
                }

                break;
            case RequestCode.NetCode.COMMUNITY_INFO:
                Intent intent = new Intent(SelectAddressActivity.this, CommunityActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("install_type", installType);
                intent.putExtra("id", communityId);
                startActivity(intent);
                break;
            case RequestCode.NetCode.SELF_BUILDING_DEVICE:
                if (buildingType == 2 || buildingType ==3){
                    Intent intent1 = new Intent(SelectAddressActivity.this, CommunityActivity.class);
                    intent1.putExtra("type", type);
                    intent1.putExtra("install_type", installType);
                    intent1.putExtra("id", communityId);
                    startActivity(intent1);
                }else {
                    Intent intent1 = new Intent(SelectAddressActivity.this, DeviceListActivity.class);
                    SelfBuildingDeviceBean.DataBean buildingBean  = (SelfBuildingDeviceBean.DataBean) object;
                    HouseInfoBean houseInfoBean = new HouseInfoBean();
                    houseInfoBean.setCommunityName(buildingBean.getCommunityName());
                    houseInfoBean.setAreaNumber(buildingBean.getRDNumber());
                    houseInfoBean.setLandlordName(buildingBean.getLandlordName());
                    houseInfoBean.setPhone(buildingBean.getPhone());
                    houseInfoBean.setType(type);
                    houseInfoBean.setCommunityId(communityId);
                    houseInfoBean.setInstallType(installType);
                    houseInfoBean.setBuildingType(buildingType);

                    intent1.putExtra("house", houseInfoBean);
                    startActivity(intent1);
                }

                break;

        }

    }

    @Override
    public void onFail(int what, String msg) {
        switch (what) {
            case RequestCode.NetCode.FIND_ADDRESS:
            case RequestCode.NetCode.HOUSE_INFO:
            case RequestCode.NetCode.COMMUNITY_INFO:
            case RequestCode.NetCode.SELF_BUILDING_DEVICE:
                PopupWindowUtils.getInstance().showPopWindow(SelectAddressActivity.this,
                        "房屋未采集，是否进行地址采集？", "确定", new PopupOnClickListener() {
                            @Override
                            public void onCancel() {
                                PopupWindowUtils.getInstance().dismiss();
                            }

                            @Override
                            public void onConfirm() {
                                PopupWindowUtils.getInstance().dismiss();
                                Intent intent = new Intent(SelectAddressActivity.this, AddAddressActivity.class);
                                intent.putExtra("type", 1);
                                startActivity(intent);
                            }
                        });
                break;
        }

        switch (what) {
            case RequestCode.NetCode.HOUSE_INFO:
            case RequestCode.NetCode.COMMUNITY_INFO:
                HistoryAddress history = SQLiteUtils.getInstance().queryAddress(communityId);
                if (history != null) {
                    SQLiteUtils.getInstance().delete(history);
                }
                break;
        }
    }


    @Override
    public void hideLoading() {
        LoadingUtils.getInstance().dismiss();
    }
}

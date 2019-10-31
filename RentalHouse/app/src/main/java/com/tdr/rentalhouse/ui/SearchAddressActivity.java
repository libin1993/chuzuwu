package com.tdr.rentalhouse.ui;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseActivity;
import com.tdr.rentalhouse.bean.LocationBean;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.widget.RVDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amap.api.location.AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;

public class SearchAddressActivity extends BaseActivity implements AMapLocationListener, Inputtips.InputtipsListener, AMap.OnMapClickListener {
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.et_search_address)
    EditText etSearchAddress;
    @BindView(R.id.rv_address)
    RecyclerView rvAddress;
    @BindView(R.id.tv_clear_search)
    TextView tvClearText;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.view_clear_search)
    View viewClearSearch;
    @BindView(R.id.iv_gps)
    ImageView ivGps;

    private AMap aMap;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    private List<Tip> addressList = new ArrayList<>();
    private BaseQuickAdapter<Tip, BaseViewHolder> adapter;

    private AMapLocation mAMapLocation;
    private LocationBean locationBean;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
        ButterKnife.bind(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        initView();
        initMap();
        initLocation();

    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("定位");
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.addItemDecoration(new RVDividerItemDecoration(this, FormatUtils.getInstance().dp2px(12)));
        adapter = new BaseQuickAdapter<Tip, BaseViewHolder>(R.layout.layout_search_address_item, addressList) {
            @Override
            protected void convert(BaseViewHolder helper, Tip item) {
                helper.setText(R.id.tv_address_item, item.getDistrict() + item.getAddress() + item.getName());
                int position = helper.getAdapterPosition();
                helper.setVisible(R.id.iv_address_item, position == index);
            }
        };


        rvAddress.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                index = position;
                adapter.notifyDataSetChanged();

                LatLonPoint point = addressList.get(position).getPoint();
                locationBean = new LocationBean();
                locationBean.setLat(point.getLatitude());
                locationBean.setLng(point.getLongitude());
                locationBean.setName(addressList.get(position).getName());


                LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));

            }
        });


        etSearchAddress.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s.toString().trim())) {
                search(s.toString());

                etSearchAddress.setCursorVisible(true);
            } else {
                etSearchAddress.setCursorVisible(false);

                addressList.clear();
                adapter.notifyDataSetChanged();
                rvAddress.setVisibility(View.GONE);
            }
        }
    };


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
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0);// 设置圆形的边框粗细
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMaxZoomLevel(20);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));//地图缩放比列
        aMap.setMyLocationEnabled(true);//设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
        aMap.setOnMapClickListener(this);
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
        mLocationOption.setOnceLocation(false);


        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        //启动定位
        mLocationClient.startLocation();
    }


    /**
     * @param keyword 查找地址
     */
    private void search(String keyword) {

        InputtipsQuery inputtipsQuery = new InputtipsQuery(keyword, "");//初始化一个输入提示搜索对象，并传入参数
        inputtipsQuery.setCityLimit(true);//将获取到的结果进行城市限制筛选
        Inputtips inputtips = new Inputtips(this, inputtipsQuery);//定义一个输入提示对象，传入当前上下文和搜索对象
        inputtips.setInputtipsListener(this);//设置输入提示查询的监听，实现输入提示的监听方法onGetInputtips()
        inputtips.requestInputtipsAsyn();//输入查询提示的异步接口实现

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
            LogUtils.log(aMapLocation.getLongitude() + "," + aMapLocation.getLatitude());
            if (mAMapLocation == null) {
                mAMapLocation = aMapLocation;
                etSearchAddress.setText(aMapLocation.getPoiName());
                etSearchAddress.setSelection(aMapLocation.getPoiName().length());
            }
        }
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        addressList.clear();
        if (list != null && list.size() > 0 && !TextUtils.isEmpty(etSearchAddress.getText().toString().trim())) {
            addressList.addAll(list);
            rvAddress.setVisibility(View.VISIBLE);

            tvClearText.setVisibility(View.VISIBLE);
            viewClearSearch.setVisibility(View.VISIBLE);


            index = 0;

            LatLonPoint point = addressList.get(0).getPoint();
            locationBean = new LocationBean();
            locationBean.setLat(point.getLatitude());
            locationBean.setLng(point.getLongitude());
            locationBean.setName(addressList.get(0).getName());
            adapter.notifyDataSetChanged();

        } else {
            rvAddress.setVisibility(View.GONE);
            tvClearText.setVisibility(View.GONE);
            viewClearSearch.setVisibility(View.GONE);
        }

        removeMark();
        addMark();
        adapter.notifyDataSetChanged();
    }


    /**
     * 移除mark
     */
    private void removeMark() {
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        if (ObjectUtils.getInstance().isNotNull(mapScreenMarkers)) {
            for (Marker mapScreenMarker : mapScreenMarkers) {
                mapScreenMarker.remove();
            }
        }
    }


    /**
     * 标点
     *
     * @return
     */
    private void addMark() {


        ArrayList<MarkerOptions> markerOptionList = new ArrayList<>();

        for (Tip tip : addressList) {
            MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 1.0f)
                    .position(new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_map_mark)))
                    .setFlat(true);//设置marker平贴地图效果
            markerOptionList.add(markerOption);
        }


        if (markerOptionList.size() > 0) {
            aMap.addMarkers(markerOptionList, true);

            LatLng latLng = new LatLng(addressList.get(0).getPoint().getLatitude(),
                    addressList.get(0).getPoint().getLongitude());
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }

    }


    @OnClick({R.id.iv_title_back, R.id.tv_clear_search, R.id.iv_gps})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_clear_search:
                if (locationBean != null) {
                    EventBus.getDefault().post(locationBean);
                    finish();
                }
                break;
            case R.id.iv_gps:
                getLocation();
                break;
        }
    }

    private void getLocation() {
        if (mAMapLocation == null) {
            return;
        }
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        //将地图移动到定位点
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(
                new LatLng(mAMapLocation.getLatitude(), mAMapLocation.getLongitude())));
    }


    @Override
    public void onMapClick(LatLng latLng) {
        LogUtils.log(latLng.longitude + "," + latLng.latitude);

        tvClearText.setVisibility(View.VISIBLE);
        viewClearSearch.setVisibility(View.VISIBLE);
        rvAddress.setVisibility(View.VISIBLE);

        locationBean = new LocationBean();
        locationBean.setLat(latLng.latitude);
        locationBean.setLng(latLng.longitude);
        locationBean.setName(latLng.longitude + "," + latLng.latitude);


        etSearchAddress.removeTextChangedListener(textWatcher);
        etSearchAddress.setText(null);
        etSearchAddress.addTextChangedListener(textWatcher);
        addressList.clear();
        index = 0;


        Tip tip = new Tip();
        tip.setDistrict("");
        tip.setAddress("");
        tip.setName(latLng.longitude + "," + latLng.latitude);
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        tip.setPostion(latLonPoint);
        addressList.add(tip);

        adapter.notifyDataSetChanged();


        removeMark();
        addMark();


//
//        GeocodeSearch geoCoderSearch = new GeocodeSearch(this);
//        geoCoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//
//            @Override
//            public void onGeocodeSearched(GeocodeResult result, int rCode) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
//                List<PoiItem> pois = result.getRegeocodeAddress().getPois();
//                LogUtils.log(pois.size()+"");
//                if (ObjectUtils.getInstance().isNotNull(pois)){
//                    etSearchAddress.setText(pois.get(0).getCityName()+pois.get(0).getTitle());
//                    etSearchAddress.setSelection(etSearchAddress.getText().length());
//                }
//            }
//        });
//        //和上面一样
//        // 第一个参数表示一个Latlng(经纬度)，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
//        LatLonPoint lp = new LatLonPoint(latLng.latitude, latLng.longitude);
//        RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
//        geoCoderSearch.getFromLocationAsyn(query);

    }
}

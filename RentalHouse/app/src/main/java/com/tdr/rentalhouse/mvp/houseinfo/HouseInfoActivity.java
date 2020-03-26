package com.tdr.rentalhouse.mvp.houseinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.Api;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.mvp.bindhouse.BindHouseActivity;
import com.tdr.rentalhouse.mvp.communitydetail.CommunityDetailActivity;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
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

/**
 * Author：Li Bin on 2019/7/17 09:44
 * Description：
 */
public class HouseInfoActivity extends BaseMvpActivity<HouseInfoContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.tv_unit_name)
    TextView tvHouse;
    @BindView(R.id.rv_unit)
    RecyclerView rvHouse;

    List<HouseBean.DataBean.ListBean> dataList = new ArrayList<>();
    BaseQuickAdapter<HouseBean.DataBean.ListBean, BaseViewHolder> adapter;

    private HouseInfoBean houseInfoBean;
    private HouseBean.DataBean dataBean;

    private SimpleDraweeView ivCommunity;
    private TextView tvName;
    private TextView tvAddress;

    @Override
    protected HouseInfoContact.Presenter initPresenter() {
        return new HouseInfoPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_house);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getData();
        initView();
        initData();
    }

    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");
    }

    private void initData() {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.getHouseInfo(RequestCode.NetCode.HOUSE_INFO, houseInfoBean.getHouseId());

    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("房屋信息");
        tvTitleMore.setVisibility(View.VISIBLE);
        tvTitleMore.setText("编辑");
        rvHouse.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<HouseBean.DataBean.ListBean, BaseViewHolder>(R.layout.layout_room_item_edit, dataList) {

            @Override
            protected void convert(BaseViewHolder helper, HouseBean.DataBean.ListBean item) {
                helper.setText(R.id.tv_room_name, item.getRoomNumber());
            }

        };

        rvHouse.setAdapter(adapter);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_more})
    public void onViewClicked(View view) {
        if (FastClickUtils.isSingleClick()) {
            switch (view.getId()) {
                case R.id.iv_title_back:
                    finish();
                    break;
                case R.id.tv_title_more:
                    if (dataBean != null) {
                        Intent intent = new Intent(HouseInfoActivity.this, BindHouseActivity.class);
                        houseInfoBean.setType(4);
                        houseInfoBean.setLandlordName(dataBean.getLandlordName());
                        houseInfoBean.setIdNo(dataBean.getIDNumber());
                        houseInfoBean.setPhone(dataBean.getPhone());
                        houseInfoBean.setQrCode(dataBean.getCode());
                        houseInfoBean.setAddressCode(dataBean.getAreaCode());
                        houseInfoBean.setBusinessType(dataBean.getBussinessType());
                        houseInfoBean.setLat(dataBean.getLatitude());
                        houseInfoBean.setLng(dataBean.getLongitude());
                        houseInfoBean.setCityName(dataBean.getCityName());
                        houseInfoBean.setAreaName(dataBean.getAreaName());
                        houseInfoBean.setStreetName(dataBean.getSteetName());
                        houseInfoBean.setResidentialName(dataBean.getComminityName());

                        if (dataList.size() > 0) {
                            List<HouseInfoBean.Room> roomList = new ArrayList<>();
                            for (HouseBean.DataBean.ListBean listBean : dataList) {
                                HouseInfoBean.Room room = new HouseInfoBean.Room();
                                room.setId(listBean.getId());
                                room.setRoomNumber(listBean.getRoomNumber());
                                room.setSortNo(listBean.getSortNo());
                                room.setIsDefault(listBean.getIsDefault());
                                roomList.add(room);
                            }

                            houseInfoBean.setRoomList(roomList);

                        }
                        intent.putExtra("house", houseInfoBean);
                        startActivity(intent);
                    }


                    break;
            }
        }

    }

    @Override
    public void onSuccess(int what, Object object) {
        dataBean = (HouseBean.DataBean) object;
        dataList.clear();
        adapter.removeAllHeaderView();
        if (dataBean != null) {
            if (dataBean.getArchitecturalTypes() == 1 || dataBean.getArchitecturalTypes() == 4) {
                tvHouse.setVisibility(View.GONE);
                addHeader1();
            } else {
                tvHouse.setVisibility(View.VISIBLE);
                String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢/";
                if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                    address += houseInfoBean.getUnitName() + "单元/";
                }
                address += houseInfoBean.getFloorName() + "层/" + houseInfoBean.getHouseName() + "室";
                tvHouse.setText(address);
            }

            if (ObjectUtils.getInstance().isNotNull(dataBean.getList())) {
                dataList.addAll(dataBean.getList());
            }

            addHeader2();
        }

        adapter.notifyDataSetChanged();
    }

    private void addHeader2() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_room_header, null);
        TextView tvLandlordName = view.findViewById(R.id.tv_landlord_name);
        TextView tvCard = view.findViewById(R.id.tv_id_card);
        TextView tvPhone = view.findViewById(R.id.tv_phone_number);
        TextView tvCode = view.findViewById(R.id.tv_house_qrcode);
        TextView tvBusinessType = view.findViewById(R.id.tv_business);
        TextView tvPosition = view.findViewById(R.id.tv_house_lat_lng);

        tvLandlordName.setText(dataBean.getLandlordName());
        tvCard.setText(dataBean.getIDNumber());
        tvPhone.setText(dataBean.getPhone());
        tvCode.setText(dataBean.getCode());
        if (dataBean.getBussinessType() == 1){
            tvBusinessType.setText("出租屋");
        }else {
            tvBusinessType.setText("旅业");
        }

        if (!TextUtils.isEmpty(dataBean.getLatitude()) && !TextUtils.isEmpty(dataBean.getLongitude())){
            tvPosition.setText(dataBean.getLongitude() + "," + dataBean.getLatitude());
        }

        adapter.addHeaderView(view);
        adapter.notifyDataSetChanged();
    }

    private void addHeader1() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_manager_address_header, null);
        ivCommunity = view.findViewById(R.id.iv_estate);
        tvName = view.findViewById(R.id.tv_estate_name);
        tvAddress = view.findViewById(R.id.tv_estate_address);
        ivCommunity.setImageURI(Api.IMG_HOST + dataBean.getOutlookOne());
        tvName.setText(dataBean.getRDNumber());


        String address = dataBean.getCityName();
        if (!TextUtils.isEmpty(dataBean.getAreaName())) {
            address += dataBean.getAreaName();
        }

        if (!TextUtils.isEmpty(dataBean.getSteetName())) {
            address += dataBean.getSteetName();
        }

        if (!TextUtils.isEmpty(dataBean.getComminityName())) {
            address += dataBean.getComminityName();
        }

        if (!TextUtils.isEmpty(dataBean.getRDNumber())) {
            address += dataBean.getRDNumber();
        }


        tvAddress.setText(address);


        adapter.addHeaderView(view);
        adapter.notifyDataSetChanged();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseInfoActivity.this, CommunityDetailActivity.class);
                intent.putExtra("id", houseInfoBean.getCommunityId());
                startActivity(intent);
            }
        });
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
    public void editRoom(String msg) {
        if ("edit_room".equals(msg)) {
            initData();
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
}

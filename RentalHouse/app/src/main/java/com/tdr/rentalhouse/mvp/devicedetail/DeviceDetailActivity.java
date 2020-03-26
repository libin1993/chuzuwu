package com.tdr.rentalhouse.mvp.devicedetail;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.entity.EventEntity;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.Api;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.DeviceDetailBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.utils.DateUtils;
import com.tdr.rentalhouse.utils.LimitInputTextWatcher;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Libin on 2020-03-19 17:35
 * Email：1993911441@qq.com
 * Describe：设施详情
 */
public class DeviceDetailActivity extends BaseMvpActivity<DeviceDetailPresenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_device_address)
    TextView tvDeviceAddress;
    @BindView(R.id.tv_device_building)
    TextView tvDeviceBuilding;
    @BindView(R.id.iv_device_picture)
    ImageView ivDevicePicture;
    @BindView(R.id.iv_edit_device)
    ImageView ivEditDevice;
    @BindView(R.id.tv_device_position_info)
    TextView tvDevicePositionInfo;
    @BindView(R.id.tv_code_device)
    TextView tvCodeDevice;
    @BindView(R.id.tv_type_device)
    TextView tvTypeDevice;
    @BindView(R.id.tv_device_install_time)
    TextView tvDeviceInstallTime;

    private HouseInfoBean houseInfoBean;
    private DeviceDetailBean.DataBean deviceDetailBean;
    private String deviceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        ButterKnife.bind(this);
        getData();
        initData();
    }

    private void initData() {
        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.deviceDetail(RequestCode.NetCode.DEVICE_DETAIL, houseInfoBean.getEquipRoomBindId());
    }

    private void initView() {
        Glide.with(this).load(Api.IMG_HOST + deviceDetailBean.getDeviceIcon()).into(ivDevicePicture);
        tvDevicePositionInfo.setText(deviceDetailBean.getDeviceName());
        tvCodeDevice.setText(deviceDetailBean.getDeviceCode());
        tvTypeDevice.setText(deviceDetailBean.getDeviceType());
        tvDeviceInstallTime.setText(DateUtils.getInstance().formatTime("yyyy-mm-dd",
                "yyyy/mm/dd", deviceDetailBean.getDeviceTime()));
    }

    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");

        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("设施详情");

        if (houseInfoBean.getHouseId() == -1 ||houseInfoBean.getHouseId() == -2) {
            tvDeviceAddress.setText(houseInfoBean.getCommunityName());
            String address = "楼幢：" + houseInfoBean.getBuildingName() + "幢          ";
            if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                address += "单元：" + houseInfoBean.getUnitName() + "单元          ";
            }
            address += houseInfoBean.getHouseName();
            tvDeviceBuilding.setText(address);
            ivEditDevice.setVisibility(View.VISIBLE);
        } else {
            if (houseInfoBean.getBuildingType() ==2 || houseInfoBean.getBuildingType() ==3){
                tvDeviceAddress.setText(houseInfoBean.getCommunityName());
            }else {
                tvDeviceAddress.setText(houseInfoBean.getAreaNumber());
            }

            tvDeviceBuilding.setText("房东：" + houseInfoBean.getLandlordName() + "          联系电话：" + houseInfoBean.getPhone());
            ivEditDevice.setVisibility(View.GONE);
        }
    }


    @Override
    protected DeviceDetailPresenter initPresenter() {
        return new DeviceDetailPresenter();
    }

    @Override
    public void onSuccess(int what, Object object) {
        switch (what){
            case RequestCode.NetCode.DEVICE_DETAIL:
                deviceDetailBean = (DeviceDetailBean.DataBean) object;
                if (deviceDetailBean != null) {
                    initView();
                }
                break;
            case RequestCode.NetCode.EDIT_DEVICE_NAME:
                tvDevicePositionInfo.setText(deviceName);
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

    @OnClick({R.id.iv_title_back, R.id.iv_edit_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_edit_device:
                editDeviceName();
                break;
        }
    }

    private void editDeviceName() {
        final PopupWindow mPopupWindow = new PopupWindow();
        View mPopBackView = LayoutInflater.from(this).inflate(R.layout.popup_update_room, null);

        //设置Popup具体控件
        TextView tvCancel = mPopBackView.findViewById(R.id.tv_cancel_update);
        TextView tvConfirm = mPopBackView.findViewById(R.id.tv_update_room);
        final EditText etName = mPopBackView.findViewById(R.id.tv_update_name);

        etName.setText(tvDevicePositionInfo.getText());
        etName.addTextChangedListener(new LimitInputTextWatcher(etName, LimitInputTextWatcher.CHINESE_REGEX));
        etName.setMaxEms(8);


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
                    ToastUtils.getInstance().showToast("请输入点位信息");
                    return;
                }

                mPopupWindow.dismiss();
                if (!etName.getText().toString().trim().equals(tvDevicePositionInfo.getText().toString().trim())){
                    LoadingUtils.getInstance().showLoading(DeviceDetailActivity.this, "加载中");
                    deviceName = etName.getText().toString().trim();
                    mPresenter.editDeviceName(RequestCode.NetCode.EDIT_DEVICE_NAME, houseInfoBean.getEquipRoomBindId(),deviceName);
                }
            }
        });

        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


}

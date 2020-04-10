package com.tdr.rentalhouse.mvp.devicelist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guanaj.easyswipemenulibrary.EasySwipeMenuLayout;
import com.inuker.bluetooth.library.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.Api;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.DeviceBean;
import com.tdr.rentalhouse.bean.DeviceListBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.SelfBuildingDeviceBean;
import com.tdr.rentalhouse.mvp.checkequipment.CheckEquipmentActivity;
import com.tdr.rentalhouse.mvp.devicedetail.DeviceDetailActivity;
import com.tdr.rentalhouse.mvp.installotherdevice.OtherDeviceActivity;
import com.tdr.rentalhouse.ui.ConnectBluetoothActivity;
import com.tdr.rentalhouse.utils.BluetoothUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;
import com.tdr.rentalhouse.widget.RVDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Author：Libin on 2020-03-19 10:45
 * Email：1993911441@qq.com
 * Describe：设施列表
 */
public class DeviceListActivity extends BaseMvpActivity<DeviceListContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_device_area)
    TextView tvDeviceArea;
    @BindView(R.id.tv_device_location)
    TextView tvDeviceLocation;
    @BindView(R.id.rv_device)
    RecyclerView rvDevice;
    @BindView(R.id.srl_device)
    SmartRefreshLayout srlDevice;

    private HouseInfoBean houseInfoBean;
    private List<DeviceBean> deviceList = new ArrayList<>();
    private BaseQuickAdapter<DeviceBean, BaseViewHolder> adapter;
    private int index;

    //当前页
    private int page = 1;

    //刷新
    private boolean isRefresh;
    //加载更多
    private boolean isLoadMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);

        getData();
        LoadingUtils.getInstance().showLoading(this, "加载中");
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        page = 1;
        LoadingUtils.getInstance().showLoading(this, "加载中");
        initData();
    }
    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");

        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        srlDevice.setRefreshHeader(new ClassicsHeader(this));
        srlDevice.setRefreshFooter(new ClassicsFooter(this));
        tvTitleName.setText("设施列表");
        if (houseInfoBean.getHouseId() == -1 || houseInfoBean.getHouseId() == -2) {
            tvTitleMore.setVisibility(View.VISIBLE);
            tvTitleMore.setText("添加设施");
            tvTitleMore.setTextColor(ContextCompat.getColor(this, R.color.orange_fda));
        }

        rvDevice.setLayoutManager(new LinearLayoutManager(this));
        rvDevice.addItemDecoration(new RVDividerItemDecoration(this, FormatUtils.getInstance().dp2px(12)));
        adapter = new BaseQuickAdapter<DeviceBean, BaseViewHolder>(R.layout.layout_rv_device_item, deviceList) {
            @Override
            protected void convert(BaseViewHolder helper, DeviceBean item) {
                EasySwipeMenuLayout swipeMenuLayout = helper.getView(R.id.esm_device);
                if (houseInfoBean.getHouseId() >= 0) {
                    swipeMenuLayout.setCanLeftSwipe(false);
                    swipeMenuLayout.setCanRightSwipe(false);
                } else {
                    swipeMenuLayout.setCanLeftSwipe(true);
                    swipeMenuLayout.setCanRightSwipe(true);
                }

                ImageView img = helper.getView(R.id.iv_device);
                Glide.with(DeviceListActivity.this)
                        .load(Api.IMG_HOST + item.getDevIcon())
                        .into(img);

                LogUtils.log(Api.IMG_HOST + item.getDevIcon());

                TextView tvPosition = helper.getView(R.id.tv_device_info);

                tvPosition.setText(item.getDeviceName());

                helper.setText(R.id.tv_device_code, "设施编码： " + item.getDeviceNumber());
                helper.setText(R.id.tv_device_name, "设施类型： " + item.getDeviceTypeName());

                helper.addOnClickListener(R.id.ll_device_info);
                helper.addOnClickListener(R.id.tv_exchange_device);
                helper.addOnClickListener(R.id.tv_delete_device);

            }
        };
        rvDevice.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_device_info:
                        Intent intent = new Intent(DeviceListActivity.this, DeviceDetailActivity.class);
                        houseInfoBean.setEquipRoomBindId(deviceList.get(position).getDeviceBindId());
                        intent.putExtra("house", houseInfoBean);
                        startActivity(intent);
                        break;
                    case R.id.tv_exchange_device:
                        houseInfoBean.setType(1);
                        houseInfoBean.setEquipRoomBindId(deviceList.get(position).getDeviceBindId());
                        addOrExchangeDevice();
                        break;
                    case R.id.tv_delete_device:
                        index = position;
                        deleteDevice(deviceList.get(position).getDeviceBindId()
                                ,deviceList.get(position).getDeviceName(),
                                deviceList.get(position).getDeviceNumber());
                        break;
                }
            }
        });

        srlDevice.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isLoadMore = true;
                page++;
                initData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                initData();
            }
        });

    }

    private void initData() {
        if (houseInfoBean.getBuildingType() == 1 || houseInfoBean.getBuildingType() == 4) {
            mPresenter.getSelfBuildingDevice(RequestCode.NetCode.SELF_BUILDING_DEVICE, houseInfoBean.getCommunityId(), page);
        } else {
            mPresenter.getBuildingDevice(RequestCode.NetCode.BUILDING_DEVICE, houseInfoBean.getHouseId(), houseInfoBean.getUnitId(), page);
        }

    }

    private void initView() {

        if (houseInfoBean.getHouseId() == -1 ||houseInfoBean.getHouseId() == -2) {
            tvDeviceArea.setText(houseInfoBean.getCommunityName());
            String address = "楼幢：" + houseInfoBean.getBuildingName() + "幢          ";
            if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                address += "单元：" + houseInfoBean.getUnitName() + "单元          ";
            }
            address += houseInfoBean.getHouseName();
            tvDeviceLocation.setText(address);
        } else {
            if (houseInfoBean.getBuildingType() ==2 || houseInfoBean.getBuildingType() ==3){
                String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢/";
                if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                    address += houseInfoBean.getUnitName() + "单元/";
                }
                address += houseInfoBean.getHouseName() + "室";

                tvDeviceArea.setText(address);
            }else {
                tvDeviceArea.setText(houseInfoBean.getAreaNumber());
            }

            tvDeviceLocation.setText("房东：" + houseInfoBean.getLandlordName() + "          联系电话：" + houseInfoBean.getPhone());
        }

    }

    private void deleteDevice(final int deviceBindId,String deviceName,String deviceNumber) {
        final PopupWindow mPopupWindow = new PopupWindow();
        final View mPopBackView = LayoutInflater.from(this).inflate(R.layout.popup_delete_device, null);

        //设置Popup具体控件
        TextView tvCancel = mPopBackView.findViewById(R.id.tv_popup_cancel);
        TextView tvConfirm = mPopBackView.findViewById(R.id.tv_popup_confirm);
        TextView tvTitle = mPopBackView.findViewById(R.id.tv_popup_title);
        TextView tvMsg = mPopBackView.findViewById(R.id.tv_popup_content);

        tvTitle.setText(deviceName);
        tvMsg.setText("是否确认拆除设施"+deviceNumber+"？");


        //设置Popup具体参数
        mPopupWindow.setContentView(mPopBackView);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setWidth(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置宽
        mPopupWindow.setHeight(LinearLayoutCompat.LayoutParams.MATCH_PARENT);//设置高
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.setFocusable(true);//点击空白，popup不自动消失
        mPopupWindow.setTouchable(true);//popup区域可触摸
        mPopupWindow.setOutsideTouchable(false);//非popup区域可触摸

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                LoadingUtils.getInstance().showLoading(DeviceListActivity.this, "删除中");
                mPresenter.deleteDevice(RequestCode.NetCode.DELETE_DEVICE, deviceBindId);
            }
        });

        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }



    @Override
    protected DeviceListContact.Presenter initPresenter() {
        return new DeviceListPresenter();
    }

    @Override
    public void onSuccess(int what, Object object) {
        switch (what) {
            case RequestCode.NetCode.BUILDING_DEVICE:
                DeviceListBean.DataBean dataBean = (DeviceListBean.DataBean) object;
                if (isRefresh) {
                    srlDevice.finishRefresh();
                    isRefresh = false;
                    deviceList.clear();
                } else if (isLoadMore) {
                    srlDevice.finishLoadMore();
                } else {
                    int managerId = dataBean.getManageId();
                    houseInfoBean.setManageId(managerId);
                    houseInfoBean.setLandlordName(dataBean.getLandlordName());
                    houseInfoBean.setPhone(dataBean.getPhone());
                    deviceList.clear();
                    initView();
                }


                if (ObjectUtils.getInstance().isNotNull(dataBean.getList())) {
                    for (DeviceListBean.DataBean.ListBean listBean : dataBean.getList()) {
                        DeviceBean deviceBean = new DeviceBean();
                        deviceBean.setDeviceBindId(listBean.getDeviceBindId());
                        deviceBean.setDeviceName(listBean.getDeviceName());
                        deviceBean.setDeviceNumber(listBean.getDeviceNumber());
                        deviceBean.setDeviceType(listBean.getDeviceType());
                        deviceBean.setDeviceTypeName(listBean.getDeviceTypeName());
                        deviceBean.setDevIcon(listBean.getDevIcon());
                        deviceBean.setDevicePicture(listBean.getDevicePicture());
                        deviceList.add(deviceBean);
                    }
                }else {
                    if (isLoadMore) {
                        page--;
                        isLoadMore = false;
                    }
                }

                adapter.notifyDataSetChanged();
                break;
            case RequestCode.NetCode.SELF_BUILDING_DEVICE:
                if (isRefresh) {
                    srlDevice.finishRefresh();
                    isRefresh = false;
                    deviceList.clear();
                } else if (isLoadMore) {
                    srlDevice.finishLoadMore();
                } else {
                    deviceList.clear();
                    initView();
                }

                SelfBuildingDeviceBean.DataBean dataBean1 = (SelfBuildingDeviceBean.DataBean) object;
                if (ObjectUtils.getInstance().isNotNull(dataBean1.getList())) {
                    for (SelfBuildingDeviceBean.DataBean.DeviceListBean deviceListBean : dataBean1.getList()) {
                        DeviceBean deviceBean = new DeviceBean();
                        deviceBean.setDeviceBindId(deviceListBean.getDeviceBindId());
                        deviceBean.setDeviceName(deviceListBean.getDeviceName());
                        deviceBean.setDeviceNumber(deviceListBean.getDeviceNumber());
                        deviceBean.setDeviceType(deviceListBean.getDeviceType());
                        deviceBean.setDeviceTypeName(deviceListBean.getDeviceTypeName());
                        deviceBean.setDevIcon(deviceListBean.getDevIcon());
                        deviceBean.setDevicePicture(deviceListBean.getDevicePicture());
                        deviceList.add(deviceBean);
                    }
                }else {
                    if (isLoadMore) {
                        page--;
                        isLoadMore = false;
                    }
                }

                adapter.notifyDataSetChanged();
                break;

            case RequestCode.NetCode.DELETE_DEVICE:
                deviceList.remove(index);
                adapter.notifyItemRemoved(index);
                adapter.notifyItemRangeChanged(index, deviceList.size());
                adapter.notifyItemRangeChanged(index, deviceList.size() - index);
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

    @OnClick({R.id.iv_title_back, R.id.tv_title_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_more:
                houseInfoBean.setType(0);
                houseInfoBean.setEquipRoomBindId(0);
                addOrExchangeDevice();
                break;
        }
    }


    private void addOrExchangeDevice() {
        final View contentView = LayoutInflater.from(this).inflate(R.layout.popup_add_device, null);

        //设置Popup具体控件
        TextView tvDevice = contentView.findViewById(R.id.tv_add_smoke_detector);
        TextView tvOtherDevice = contentView.findViewById(R.id.tv_add_other_device);

        final PopupWindow popupWindow = new PopupWindow(contentView, FormatUtils.getInstance().dp2px(250),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(contentView);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        final WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 0.7f;
        getWindow().setAttributes(layoutParams);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                layoutParams.alpha = 1.0f;
                getWindow().setAttributes(layoutParams);
            }
        });

        tvDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent();
                if (BluetoothUtils.getInstance().getBluetoothBean() != null &&
                        BluetoothUtils.getInstance().getClient().getConnectStatus(BluetoothUtils
                                .getInstance().getBluetoothBean().getAddress()) == Constants.STATUS_DEVICE_CONNECTED) {
                    intent.setClass(DeviceListActivity.this, CheckEquipmentActivity.class);
                } else {
                    intent.setClass(DeviceListActivity.this, ConnectBluetoothActivity.class);
                }

                intent.putExtra("house", houseInfoBean);
                startActivity(intent);

            }
        });

        tvOtherDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(DeviceListActivity.this, OtherDeviceActivity.class);
                intent.putExtra("house", houseInfoBean);
                startActivity(intent);

            }
        });

    }
}

package com.tdr.rentalhouse.mvp.checkequipment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.ActionBean;
import com.tdr.rentalhouse.bean.BluetoothBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.ScanResult;
import com.tdr.rentalhouse.mvp.scancode.ScanQRCodeActivity;
import com.tdr.rentalhouse.utils.Base64Utils;
import com.tdr.rentalhouse.utils.BigDecimalUtils;
import com.tdr.rentalhouse.utils.ClientManager;
import com.tdr.rentalhouse.utils.DateUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

/**
 * Author：Li Bin on 2019/7/17 17:29
 * Description：设备检测
 */
public class CheckEquipmentActivity extends BaseMvpActivity<CheckEquipmentContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.iv_network_status)
    ImageView ivNetworkStatus;
    @BindView(R.id.iv_bluetooth_status)
    ImageView ivBluetoothStatus;
    @BindView(R.id.tv_address_room)
    TextView tvAddressRoom;
    @BindView(R.id.tv_equipment_type)
    TextView tvEquipmentType;
    @BindView(R.id.btn_check_equipment)
    Button btnCheckEquipment;
    @BindView(R.id.ll_test)
    LinearLayout llTest;
    @BindView(R.id.rv_install)
    RecyclerView rvInstall;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.et_equipment_code)
    EditText etEquipmentCode;
    @BindView(R.id.iv_scan_code)
    ImageView ivScanCode;
    private ImageView ivAddPicture;

    private BluetoothBean bluetoothBean;
    private List<ActionBean> dataList = new ArrayList<>();
    private BaseQuickAdapter<ActionBean, BaseViewHolder> adapter;
    private int equipmentHeight;
    private TextView tvHeight;
    private HouseInfoBean houseInfoBean;
    private List<LocalMedia> pictureList = new ArrayList<>();

    //设备编号
    private String code = null;
    //设备类型
    private String type = null;
    private List<String> imgList = new ArrayList<>();
    private View header;

    @Override
    protected CheckEquipmentContact.Presenter initPresenter() {
        return new CheckEquipmentPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_equipment);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getData();
        initView();
    }

    private void getData() {
        bluetoothBean = (BluetoothBean) getIntent().getSerializableExtra("bluetooth");
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("安装测试");
        tvTitleMore.setText("完成");
        if (houseInfoBean.getBuildingType() == 1 || houseInfoBean.getBuildingType() == 4) {
            tvAddressRoom.setText(houseInfoBean.getRoomName());
        } else {
            String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢/";
            if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                address += houseInfoBean.getUnitName() + "单元/";
            }
            address += houseInfoBean.getFloorName() + "层/" + houseInfoBean.getHouseName() + "室/" + houseInfoBean.getRoomName();
            tvAddressRoom.setText(address);
        }

        etEquipmentCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                code = null;
                type = null;
                btnCheckEquipment.setEnabled(false);
                btnNext.setEnabled(false);
                tvEquipmentType.setText(null);
                if (!TextUtils.isEmpty(s.toString().trim()) && s.toString().trim().length() >=5) {

                    String deviceNo = s.toString().trim();
                    mPresenter.isEquipmentBind(RequestCode.NetCode.IS_EQUIPMENT_BIND,
                            Long.parseLong(deviceNo.substring(4)),
                            Long.parseLong(deviceNo.substring(0, 4), 16), houseInfoBean.getBusinessType());

                }
            }
        });

        rvInstall.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<ActionBean, BaseViewHolder>(R.layout.layout_equipment_item, dataList) {
            @Override
            protected void convert(BaseViewHolder helper, ActionBean item) {
                int adapterPosition = helper.getAdapterPosition();
                if (adapterPosition % 2 != 0) {
                    helper.setBackgroundRes(R.id.ll_equipment_item, R.color.white);
                } else {
                    helper.setBackgroundRes(R.id.ll_equipment_item, R.drawable.gradient_bg);
                }
                helper.setText(R.id.tv_enter_time, item.getTime());
                helper.setText(R.id.tv_enter_status, item.getOrientation() == 1 ? "进门" : "出门");
                helper.setText(R.id.tv_human_height, BigDecimalUtils.getInstance().mul(item.getHeight(), 0.01, 2) + "米");
            }

        };
        rvInstall.setAdapter(adapter);

        header = LayoutInflater.from(this).inflate(R.layout.layout_equipment_header, null);
        tvHeight = header.findViewById(R.id.tv_equipment_height);

        View footer = LayoutInflater.from(this).inflate(R.layout.layout_equipemnt_footer, null);
        ivAddPicture = footer.findViewById(R.id.iv_equipment);
        ImageView ivDeletePicture = footer.findViewById(R.id.iv_delete_picture);
        ivAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        ivDeletePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureList.clear();
                ivAddPicture.setImageResource(0);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.addHeaderView(header);
        adapter.addFooterView(footer);
        rvInstall.setAdapter(adapter);


        notifyBluetooth();
        ClientManager.getClient(CheckEquipmentActivity.this).registerConnectStatusListener(bluetoothBean.getAddress(), new BleConnectStatusListener() {
            @Override
            public void onConnectStatusChanged(String mac, int status) {
                if (status == Constants.STATUS_DISCONNECTED) {
                    finish();
                }
            }
        });


    }


    @OnClick({R.id.iv_title_back, R.id.iv_scan_code, R.id.btn_check_equipment, R.id.tv_title_more, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_scan_code:
                startActivity(new Intent(CheckEquipmentActivity.this, ScanQRCodeActivity.class));
                break;
            case R.id.btn_check_equipment:
                writeToBluetooth("03", "0");
                rvInstall.setVisibility(View.VISIBLE);
                llTest.setVisibility(View.GONE);
                break;
            case R.id.tv_title_more:
                uploadFile();
                break;
            case R.id.btn_next:
                rvInstall.setVisibility(View.VISIBLE);
                llTest.setVisibility(View.GONE);
                tvTitleMore.setVisibility(View.VISIBLE);
                equipmentHeight = 1000;
                header.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 照片选择弹出
     */
    private void selectPicture() {

        PictureSelector.create(CheckEquipmentActivity.this)
                .openCamera(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }


    /**
     * 上传文件
     */
    private void uploadFile() {
        if (TextUtils.isEmpty(code)) {
            ToastUtils.getInstance().showToast("请扫描设备二维码");
            return;
        }

        if (!ObjectUtils.getInstance().isNotNull(pictureList)) {
            ToastUtils.getInstance().showToast("请添加设备图片");
            return;
        }


        Map<String, RequestBody> map = new HashMap<>();
        for (int i = 0; i < pictureList.size(); i++) {

            File file = new File(pictureList.get(i).getCompressPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            map.put("file" + i + "\"; filename=\"" + file.getName(), requestFile);
        }

        LoadingUtils.getInstance().showLoading(CheckEquipmentActivity.this, "加载中");
        mPresenter.upload(RequestCode.NetCode.UPLOAD, map);
    }

    /**
     * 上传数据
     */
    private void submitData() {
        Map<String, Object> map = new HashMap<>();


        if (houseInfoBean.getEquipRoomBindId() != 0) {
            map.put("EquipRoomBindId", houseInfoBean.getEquipRoomBindId());
        }

        map.put("EquipmentNumber", Long.parseLong(code, 16));
        map.put("EquipmentType", Long.parseLong(type, 16));
        map.put("InstallationHeight", equipmentHeight);
        map.put("PicUrl", imgList.get(0));
        map.put("ManageId", houseInfoBean.getManageId());
        map.put("BussinessType",houseInfoBean.getBusinessType());


        mPresenter.installEquipment(RequestCode.NetCode.INSTALL_EQUIPMENT, map);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanResult(ScanResult scanResult) {

        String result = scanResult.getResult();
        LogUtils.log(result);
        if (result.contains("?AI")) {
            String[] split = result.split("\\?");
            String type1 = split[1].substring(2);
            LogUtils.log(type1);
            byte[] decode = Base64Utils.decode(type1);

            String str = FormatUtils.getInstance().bytes2Hex(decode);
            if (str.length() == 16) {
                etEquipmentCode.setText(str.substring(0, 4) + FormatUtils.getInstance().hexToLong(str.substring(4, 12), 8));
                etEquipmentCode.setSelection(etEquipmentCode.getText().toString().length());
            } else {
                ToastUtils.getInstance().showToast("当前设备不是AI烟感");
            }
        } else {
            if (!TextUtils.isEmpty(result) && result.length() >= 5 &&
                    result.startsWith("042") && FormatUtils.getInstance().isNumeric(result)){
                etEquipmentCode.setText(result.length()<14 ? result : result.substring(0,14));
                etEquipmentCode.setSelection(etEquipmentCode.getText().toString().length());
            }else {
                ToastUtils.getInstance().showToast("当前设备不是AI烟感");
            }
        }
    }


    /**
     * 写入
     */
    private void writeToBluetooth(final String command, String content) {
        final String data = FormatUtils.getInstance().getWriteData("AA", command, content);

        LogUtils.log("write：" + data);
        ClientManager.getClient(this).write(bluetoothBean.getAddress(), bluetoothBean.getServiceUUID(),
                bluetoothBean.getWriteCUUID(), FormatUtils.getInstance().hexStringToBytes(data), bleWriteResponse);
    }

    private BleWriteResponse bleWriteResponse = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                LogUtils.log("写入成功");
            } else {
                LogUtils.log("写入失败");
                finish();
            }
        }
    };


    /**
     * 通知
     */
    private void notifyBluetooth() {
        ClientManager.getClient(this).notify(bluetoothBean.getAddress(), bluetoothBean.getServiceUUID(),
                bluetoothBean.getNotifyCUUID(), bleNotifyResponse);
    }


    private BleNotifyResponse bleNotifyResponse = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            String data = FormatUtils.getInstance().byteToString(value);
            LogUtils.log("notify:" + data);

            if (!TextUtils.isEmpty(data) && data.length() == 40) {
                switch (data.substring(0, 4)) {
                    case "AA04":
                        writeToBluetooth("01", type + code);
                        break;
                    case "AA02":
                        writeToBluetooth("07", "0");
                        break;
                    case "AA08":
                        switch (data.substring(4, 6)) {
                            case "02":
                            case "03":
                                ToastUtils.getInstance().showToast("AI烟感异常,结束报装");
                                finish();
                                break;
                            case "04":
                            case "05":
                                ToastUtils.getInstance().showToast("网络异常,结束报装");
                                finish();
                                break;
                            case "06":
                            case "07":
                                ToastUtils.getInstance().showToast("AI烟感、网络异常,结束报装");
                                finish();
                                break;
                        }
                        break;
                    case "AA09":
                        writeToBluetooth("0A", "0");
                        break;
                    case "AA0B":
                        writeToBluetooth("0C", "0");
                        int status = Integer.parseInt(new BigInteger(data.substring(16, 18), 16).toString(10));
                        LogUtils.log("status:" + status);
                        if (status < 50) {
                            ToastUtils.getInstance().showToast("AI烟感通信成功率低");
                        }
                        break;
                    case "AA0D":
                        writeToBluetooth("0E", "0");

                        String str1 = data.substring(4, 6);
                        String str2 = data.substring(6, 8);
                        String str3 = new BigInteger(str1, 16).toString(10);
                        String str4 = new BigInteger(str2, 16).toString(10);

                        int a = Integer.parseInt(str3) * 100;
                        equipmentHeight = a + Integer.parseInt(str4);

                        tvHeight.setText(BigDecimalUtils.getInstance().mul(equipmentHeight, 0.001, 3) + "米");
                        LogUtils.log(BigDecimalUtils.getInstance().mul(equipmentHeight, 0.001, 3) + "米");
                        adapter.notifyDataSetChanged();
                        break;
                    case "AA0F":
                        writeToBluetooth("10", "0");
                        break;
                    case "AA11":
                        writeToBluetooth("12", "0");

                        tvTitleMore.setVisibility(View.VISIBLE);

                        ActionBean actionBean = new ActionBean();
                        actionBean.setTime(DateUtils.getInstance().getDate(DateUtils.hour_minute_second, System.currentTimeMillis()));
                        String str5 = data.substring(4, 6);
                        if (str5.equals("01")) {
                            actionBean.setOrientation(1);
                        } else {
                            actionBean.setOrientation(0);
                        }
                        String str6 = data.substring(6, 8);
                        int height1 = Integer.parseInt(new BigInteger(str6, 16).toString(10));
                        actionBean.setHeight(height1);

                        if (dataList.size() >= 5) {
                            dataList.remove(dataList.size() - 1);
                        }

                        dataList.add(0, actionBean);

                        adapter.notifyDataSetChanged();

                        break;
                }
            }

        }

        @Override
        public void onResponse(int code) {
            LogUtils.log("notify" + code);
        }
    };

    /**
     * 取消通知
     */
    private void unNotifyBluetooth() {

        ClientManager.getClient(this).unnotify(bluetoothBean.getAddress(), bluetoothBean.getServiceUUID(),
                bluetoothBean.getNotifyCUUID(), new BleUnnotifyResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> localMediaList = PictureSelector.obtainMultipleResult(data);
            pictureList.clear();
            if (ObjectUtils.getInstance().isNotNull(localMediaList)) {
                pictureList.addAll(localMediaList);
            }

            if (ObjectUtils.getInstance().isNotNull(pictureList)) {
                ivAddPicture.setImageURI(Uri.fromFile(new File(pictureList.get(0).getPath())));
            } else {
                ivAddPicture.setImageResource(0);
            }

            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        ClientManager.getClient(this).disconnect(bluetoothBean.getAddress());
        super.onDestroy();
    }

    @Override
    public void onSuccess(int what, Object object) {
        switch (what) {
            case RequestCode.NetCode.UPLOAD:
                List<String> dataList = ((BaseBean<List<String>>) object).getData();
                if (ObjectUtils.getInstance().isNotNull(dataList)) {
                    imgList.addAll(dataList);
                }
                submitData();
                break;
            case RequestCode.NetCode.INSTALL_EQUIPMENT:
                ToastUtils.getInstance().showToast("绑定成功");
                EventBus.getDefault().post("bind_success");
                finish();
                break;
            case RequestCode.NetCode.IS_EQUIPMENT_BIND:

                tvEquipmentType.setText("AI烟感");
                btnCheckEquipment.setEnabled(true);
                btnNext.setEnabled(true);

                String equipNo = etEquipmentCode.getText().toString().trim();
                type = equipNo.substring(0, 4);
                code = FormatUtils.getInstance().longToHex(Long.parseLong(equipNo.length() >= 14 ? equipNo.substring(4, 14):equipNo.substring(4)),8);
                LogUtils.log(type+","+code);
                break;
            case RequestCode.NetCode.DEVICE_TYPE:

//                String deviceNo = etEquipmentCode.getText().toString().trim();
//                mPresenter.isEquipmentBind(RequestCode.NetCode.IS_EQUIPMENT_BIND,
//                        Long.parseLong(deviceNo.substring(4, 12)),
//                        Long.parseLong(deviceNo.substring(0, 4), 16), houseInfoBean.getBusinessType());

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

}

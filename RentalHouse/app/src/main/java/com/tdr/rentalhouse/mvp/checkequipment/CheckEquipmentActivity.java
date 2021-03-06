package com.tdr.rentalhouse.mvp.checkequipment;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.tdr.rentalhouse.bean.EquipmentBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.ScanResult;
import com.tdr.rentalhouse.mvp.scancode.ScanQRCodeActivity;
import com.tdr.rentalhouse.utils.Base64Utils;
import com.tdr.rentalhouse.utils.BigDecimalUtils;
import com.tdr.rentalhouse.utils.BluetoothUtils;
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
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.et_equipment_code)
    EditText etEquipmentCode;
    @BindView(R.id.iv_scan_code)
    ImageView ivScanCode;
    @BindView(R.id.tv_equipment_height)
    TextView tvEquipmentHeight;
    @BindView(R.id.iv_equipment)
    ImageView ivAddPicture;
    @BindView(R.id.iv_delete_picture)
    ImageView ivDeletePicture;
    @BindView(R.id.ll_install)
    LinearLayout llInstall;
    @BindView(R.id.ll_skip)
    LinearLayout llSkip;

    private int equipmentHeight;
    private HouseInfoBean houseInfoBean;
    private List<LocalMedia> pictureList = new ArrayList<>();

    //设备编号
    private String code = null;
    //设备类型
    private String type = null;
    private List<String> imgList = new ArrayList<>();

    //报装设备电量过低
    private boolean equipmentQuantity = false;
    //房间唯一标志
    private String roomUniqueCode;
    //楼栋唯一标志
    private String buildingUniqueCode;
    //RF通讯成功率
    private int RFSignal;
    //NB信号
    private int NBSignal;
    //AI烟感电量
    private double AIQuantity;
    private boolean isSkip;

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
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("安装测试");
        tvTitleMore.setText("完成");
        if (houseInfoBean.getInstallType() == 1) {
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
        } else if (houseInfoBean.getInstallType() == 2) {
            if (houseInfoBean.getBuildingType() == 1 || houseInfoBean.getBuildingType() == 4) {
                tvAddressRoom.setText(houseInfoBean.getHouseName());
            } else {
                String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢/";
                if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                    address += houseInfoBean.getUnitName() + "单元/";
                }
                address += houseInfoBean.getHouseName();
                tvAddressRoom.setText(address);
            }
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
                String deviceNo = s.toString().trim();


                if (houseInfoBean.getInstallType() == 1) {
                    if (FormatUtils.getInstance().isEquipNo(deviceNo)) {

                        mPresenter.isEquipmentBind(RequestCode.NetCode.IS_EQUIPMENT_BIND,
                                Long.parseLong(deviceNo.substring(4)),
                                Long.parseLong(deviceNo.substring(0, 4), 16), houseInfoBean.getBusinessType(),
                                houseInfoBean.getHouseId(), houseInfoBean.getRoomId());
                    }
                } else if (houseInfoBean.getInstallType() == 2){
                    if (deviceNo.length() >=5){
                        mPresenter.deviceType(RequestCode.NetCode.FIRE_CONTROL_DEVICE_TYPE,
                                deviceNo.substring(4),
                                String.valueOf(Long.parseLong(deviceNo.substring(0, 4), 16)),
                                houseInfoBean.getUnitId());
                    }
                }
            }
        });

        notifyBluetooth();
        BluetoothUtils.getInstance().getClient().registerConnectStatusListener(BluetoothUtils.getInstance().getBluetoothBean().getAddress(), new BleConnectStatusListener() {
            @Override
            public void onConnectStatusChanged(String mac, int status) {
                if (status == Constants.STATUS_DISCONNECTED) {
                    finish();
                }
            }
        });

    }


    @OnClick({R.id.iv_title_back, R.id.iv_scan_code, R.id.btn_check_equipment, R.id.tv_title_more,
            R.id.btn_next, R.id.iv_equipment, R.id.iv_delete_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_scan_code:
                startActivity(new Intent(CheckEquipmentActivity.this, ScanQRCodeActivity.class));
                break;
            case R.id.btn_check_equipment:
                isSkip = false;
                writeToBluetooth("02", type + code);
                llInstall.setVisibility(View.VISIBLE);
                llTest.setVisibility(View.GONE);
                break;
            case R.id.tv_title_more:
                uploadFile();
                break;
            case R.id.btn_next:
                isSkip = true;
                llInstall.setVisibility(View.VISIBLE);
                llTest.setVisibility(View.GONE);
                tvTitleMore.setVisibility(View.VISIBLE);
                equipmentHeight = 1000;
                llSkip.setVisibility(View.GONE);
                break;
            case R.id.iv_equipment:
                selectPicture();
                break;
            case R.id.iv_delete_picture:
                pictureList.clear();
                ivAddPicture.setImageResource(0);
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
                .cropCompressQuality(60)
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

        if (houseInfoBean.getInstallType() == 1) {
            if (houseInfoBean.getEquipRoomBindId() != 0) {
                map.put("EquipRoomBindId", houseInfoBean.getEquipRoomBindId());
            }

            map.put("EquipmentNumber", Long.parseLong(code, 16));
            map.put("EquipmentType", Long.parseLong(type, 16));
            map.put("InstallationHeight", equipmentHeight);
            map.put("PicUrl", imgList.get(0));
            map.put("ManageId", houseInfoBean.getManageId());
            map.put("BussinessType", houseInfoBean.getBusinessType());
            map.put("RF", RFSignal);
            map.put("NB", NBSignal);
            map.put("Voltage", AIQuantity);


            mPresenter.installEquipment(RequestCode.NetCode.INSTALL_EQUIPMENT, map);
        } else   if (houseInfoBean.getInstallType() == 2){
            if (houseInfoBean.getType() == 1) {
                map.put("EquipRoomBindId", houseInfoBean.getEquipRoomBindId());
            }

            map.put("EquipmentNumber", Long.parseLong(code, 16));
            map.put("EquipmentType", Long.parseLong(type, 16));
            map.put("InstallationHeight", equipmentHeight);
            map.put("PicUrl", imgList.get(0));
            map.put("ManageId", houseInfoBean.getManageId());
            map.put("RF", RFSignal);
            map.put("NB", NBSignal);
            map.put("Voltage", AIQuantity);
            map.put("InstallPosition", houseInfoBean.getInstallPosition());

            mPresenter.installDevice(RequestCode.NetCode.ADD_DEVICE, map);
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanResult(ScanResult scanResult) {

        String result = scanResult.getResult();
        LogUtils.log(result);
//        if (houseInfoBean.getInstallType() == 1) {
//            if (result.contains("?X2")) {
//                String[] split = result.split("\\?");
//                String type1 = split[1].substring(2);
//                LogUtils.log(type1);
//                byte[] decode = Base64Utils.decode(type1);
//
//                String str = FormatUtils.getInstance().bytes2Hex(decode);
//                if (str.length() == 16) {
//                    etEquipmentCode.setText(str.substring(0, 4) + FormatUtils.getInstance().hexToLong(str.substring(4, 12), 8));
//                    etEquipmentCode.setSelection(etEquipmentCode.getText().toString().length());
//                } else {
//                    ToastUtils.getInstance().showToast("当前设备不是AI烟感");
//                }
//            } else {
//                if (FormatUtils.getInstance().isEquipNo(result)) {
//                    etEquipmentCode.setText(result.length() < 14 ? result : result.substring(0, 14));
//                    etEquipmentCode.setSelection(etEquipmentCode.getText().toString().length());
//                } else {
//                    ToastUtils.getInstance().showToast("当前设备不是AI烟感");
//                }
//            }
//        } else if (houseInfoBean.getInstallType() == 2) {
//            if (result.contains("?X2")) {
//                String[] split = result.split("\\?");
//                String type1 = split[1].substring(2);
//                LogUtils.log(type1);
//                String decode = Base64Utils.decodeToString(type1);
//
//                if (decode.length() == 14) {
//                    etEquipmentCode.setText(decode);
//                    etEquipmentCode.setSelection(etEquipmentCode.getText().toString().length());
//                } else {
//                    ToastUtils.getInstance().showToast("当前设备不是AI烟感");
//                }
//            } else {
//                ToastUtils.getInstance().showToast("当前设备不是AI烟感");
//            }
//        }


        if (result.contains("?X2")) {
            String[] split = result.split("\\?");
            String type1 = split[1].substring(2);
            LogUtils.log(type1);
            String decode = Base64Utils.decodeToString(type1);

            if (decode.length() == 14) {
                etEquipmentCode.setText(decode);
                etEquipmentCode.setSelection(etEquipmentCode.getText().toString().length());
            } else {
                ToastUtils.getInstance().showToast("当前设备不是AI烟感");
            }
        } else {
            ToastUtils.getInstance().showToast("当前设备不是AI烟感");
        }

    }


    /**
     * 写入
     */
    private void writeToBluetooth(String data) {

        LogUtils.log("write：" + data);
        BluetoothUtils.getInstance().getClient().write(BluetoothUtils.getInstance().getBluetoothBean().getAddress(),
                BluetoothUtils.getInstance().getBluetoothBean().getServiceUUID(),
                BluetoothUtils.getInstance().getBluetoothBean().getWriteCUUID(),
                FormatUtils.getInstance().hexStringToBytes(data), bleWriteResponse);
    }

    /**
     * 写入
     */
    private void writeToBluetooth(String command, String content) {
        String data = FormatUtils.getInstance().getWriteData("AA", command, content);

        LogUtils.log("write：" + data);
        BluetoothUtils.getInstance().getClient().write(BluetoothUtils.getInstance().getBluetoothBean().getAddress(),
                BluetoothUtils.getInstance().getBluetoothBean().getServiceUUID(),
                BluetoothUtils.getInstance().getBluetoothBean().getWriteCUUID(),
                FormatUtils.getInstance().hexStringToBytes(data), bleWriteResponse);
//        ToastUtils.getInstance().showToast("write:" + data);
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
        BluetoothUtils.getInstance().getClient().notify(BluetoothUtils.getInstance().getBluetoothBean().getAddress(),
                BluetoothUtils.getInstance().getBluetoothBean().getServiceUUID(),
                BluetoothUtils.getInstance().getBluetoothBean().getNotifyCUUID(), bleNotifyResponse);
    }


    private BleNotifyResponse bleNotifyResponse = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            String data = FormatUtils.getInstance().byteToString(value);
            LogUtils.log("notify:" + data);

//            ToastUtils.getInstance().showToast("notify:" + data);
            if (!TextUtils.isEmpty(data) && data.length() == 40) {
                switch (data.substring(0, 4)) {
                    //报装工具电量
                    case "AA01":
                        double quantity = BigDecimalUtils.getInstance().mul(Integer.parseInt(new BigInteger(data.substring(4, 6), 16).toString(10)), 0.1, 1);
                        if (!equipmentQuantity && quantity < 3.7) {
                            equipmentQuantity = true;
                            ToastUtils.getInstance().showToast("报装工具电量不足，请及时充电");
                        }
                        break;
                    case "AA03":
                        writeToBluetooth("04", buildingUniqueCode + roomUniqueCode + "00");
                        break;
                    case "AA05":
                        ToastUtils.getInstance().showToast("请将报装工具安装在设备安装底座上，按下报装工具按键，等待报装工具返回");

                        break;
                    case "AA06":
                        String gatewayType = new BigInteger(data.substring(4, 8), 16).toString(10);  //网关设备类型
                        String gatewayId = new BigInteger(data.substring(8, 16), 16).toString(10); //网关设备ID
                        RFSignal = Integer.parseInt(new BigInteger(data.substring(16, 18), 16).toString(10)) * 10;
                        NBSignal = Integer.parseInt(new BigInteger(data.substring(18, 20), 16).toString(10));
                        AIQuantity = BigDecimalUtils.getInstance().mul(Integer.parseInt(new BigInteger(data.substring(20, 22), 16).toString(10)), 0.1, 1);
                        equipmentHeight = Integer.parseInt(new BigInteger(data.substring(22, 24), 16).toString(10)) * 20;
                        tvEquipmentHeight.setText(BigDecimalUtils.getInstance().mul(equipmentHeight, 0.001, 3) + "米");
                        LogUtils.log(gatewayType + "," + gatewayId + "," + equipmentHeight + "," + RFSignal + "," + NBSignal + "," + AIQuantity);
                        if (RFSignal < 50) {
                            ToastUtils.getInstance().showToast("RF通讯成功率低，请更换设备或更换安装位置");
                        }
                        if (NBSignal < 5) {
                            ToastUtils.getInstance().showToast("NB信号差，请更换设备或更换安装位置");
                        }
                        if (AIQuantity < 3.7) {
                            ToastUtils.getInstance().showToast("AI烟感电量不足，请及时充电");
                        }

                        writeToBluetooth("07", "0");
                        tvTitleMore.setVisibility(View.VISIBLE);

                        break;
                    case "AA08":
                        String equipmentTrouble = data.substring(4, 6); //设备故障码
                        String AITrouble = data.substring(6, 8); //AI烟感故障码
                        String NBTrouble = data.substring(8, 10); //NB故障码


                        if ("01".equals(equipmentTrouble) || "02".equals(equipmentTrouble) || "03".equals(equipmentTrouble)) {
                            String equipmentStr = "报装工具故障，故障原因：";
                            switch (equipmentTrouble) {
                                case "01":
                                    equipmentStr += "姿态异常";
                                    break;
                                case "02":
                                    equipmentStr += "高度异常";
                                    break;
                                case "03":
                                    equipmentStr += "姿态异常、高度异常";
                                    break;
                            }
                            tvTitleMore.setVisibility(View.GONE);
                            LogUtils.log(equipmentStr);
                            writeToBluetooth("09", "0");
                            ToastUtils.getInstance().showToast(equipmentStr);
                            finish();
                        }


                        if (!"00".equals(AITrouble)) {
                            StringBuilder sb = new StringBuilder("AI烟感故障，故障原因：");
                            String s = FormatUtils.getInstance().byteToBit((byte) Integer.parseInt(AITrouble, 16));
                            LogUtils.log(s);
                            if (s.substring(0, 1).equals("1")) {
                                sb.append("超声波故障、");
                            }
                            if (s.substring(1, 2).equals("1")) {
                                sb.append("热释电故障、");
                            }
                            if (s.substring(2, 3).equals("1")) {
                                sb.append("烟感故障、");
                            }
                            if (s.substring(3, 4).equals("1")) {
                                sb.append("加速度故障、");
                            }
                            if (s.substring(4, 5).equals("1")) {
                                sb.append("NTC故障、");
                            }
                            if (s.substring(5, 6).equals("1")) {
                                sb.append("无线故障、");
                            }
                            if (s.substring(6, 7).equals("1")) {
                                sb.append("FLASH故障、");
                            }
                            if (s.substring(7, 8).equals("1")) {
                                sb.append("时间故障、");
                            }
                            LogUtils.log(sb.toString());
                            tvTitleMore.setVisibility(View.GONE);
                            writeToBluetooth("09", "0");
                            ToastUtils.getInstance().showToast(sb.substring(0, sb.length() - 1));
                            finish();
                        }


                        if ("01".equals(NBTrouble)) {
                            LogUtils.log("NB故障");
                            tvTitleMore.setVisibility(View.GONE);
                            writeToBluetooth("09", "0");
                            ToastUtils.getInstance().showToast("NB故障");
                            finish();
                        }

                        break;
                    case "AA0B":
//                        hideLoading();
//
//                        finish();
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

        BluetoothUtils.getInstance().getClient().unnotify(BluetoothUtils.getInstance().getBluetoothBean().getAddress(),
                BluetoothUtils.getInstance().getBluetoothBean().getServiceUUID(),
                BluetoothUtils.getInstance().getBluetoothBean().getNotifyCUUID(), new BleUnnotifyResponse() {
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
                Glide.with(this).load(Uri.fromFile(new File(pictureList.get(0).getCompressPath()))).into(ivAddPicture);
            } else {
                ivAddPicture.setImageResource(0);
            }

        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        unNotifyBluetooth();
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
            case RequestCode.NetCode.ADD_DEVICE:
                if (!isSkip){
                    writeToBluetooth("0A", "0");
                }

                ToastUtils.getInstance().showToast("绑定成功");
                EventBus.getDefault().post("bind_success");

                finish();
                break;
            case RequestCode.NetCode.IS_EQUIPMENT_BIND:
            case RequestCode.NetCode.FIRE_CONTROL_DEVICE_TYPE:
                EquipmentBean.DataBean dataBean = ((EquipmentBean.DataBean) object);
                if (dataBean != null) {
                    buildingUniqueCode = dataBean.getBuildingUnique();
                    roomUniqueCode = dataBean.getRoomUnique();

                    tvEquipmentType.setText("AI烟感");
                    btnCheckEquipment.setEnabled(true);
                    btnNext.setEnabled(true);

                    String equipNo = etEquipmentCode.getText().toString().trim();
                    type = equipNo.substring(0, 4).toUpperCase();
                    code = FormatUtils.getInstance().longToHex(Long.parseLong(equipNo.length() >= 14
                            ? equipNo.substring(4, 14) : equipNo.substring(4)), 8);
                    LogUtils.log(type + "," + code+","+buildingUniqueCode+","+roomUniqueCode);
                }
                break;
        }
    }

    @Override
    public void onFail(int what, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoading() {
        LoadingUtils.getInstance().dismiss();
    }


    //调用系统相机activity被销毁
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}

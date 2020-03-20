package com.tdr.rentalhouse.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.application.MyApplication;
import com.tdr.rentalhouse.base.BaseActivity;
import com.tdr.rentalhouse.bean.BluetoothBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.mvp.checkequipment.CheckEquipmentActivity;
import com.tdr.rentalhouse.utils.BluetoothUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import java.math.BigInteger;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

/**
 * Author：Li Bin on 2019/7/17 17:25
 * Description：蓝牙连接
 */
public class ConnectBluetoothActivity extends BaseActivity {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_name_room)
    TextView tvNameRoom;
    @BindView(R.id.iv_connect_bluetooth)
    ImageView ivConnectBluetooth;
    @BindView(R.id.tv_connect_bluetooth)
    TextView tvConnectBluetooth;
    @BindView(R.id.ll_connect_bluetooth)
    LinearLayout llConnectBluetooth;
    @BindView(R.id.et_bluetooth_name)
    EditText etBluetoothName;
    @BindView(R.id.btn_connect_bluetooth)
    Button btnConnectBluetooth;
    @BindView(R.id.ll_bluetooth)
    LinearLayout llBluetooth;
    @BindView(R.id.iv_network_checking)
    ImageView ivNetworkChecking;
    @BindView(R.id.tv_network_checking)
    TextView tvNetworkChecking;
    @BindView(R.id.ll_network)
    LinearLayout llNetwork;


    private String bluetoothName;
    private BluetoothBean bluetoothBean;
    private int bluetoothStatus = 0;

    private HouseInfoBean houseInfoBean;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_bluetooth);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");
    }


    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("设备安装");
        if (houseInfoBean.getInstallType() == 1){
            if (houseInfoBean.getBuildingType() == 1 || houseInfoBean.getBuildingType() == 4) {
                tvNameRoom.setText(houseInfoBean.getRoomName());
            } else {
                String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢/";
                if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                    address += houseInfoBean.getUnitName() + "单元/";
                }
                address += houseInfoBean.getFloorName() + "层/" + houseInfoBean.getHouseName() + "室/" + houseInfoBean.getRoomName();
                tvNameRoom.setText(address);
            }
        }else if (houseInfoBean.getInstallType() == 2){
            if (houseInfoBean.getBuildingType() == 1 || houseInfoBean.getBuildingType() == 4) {
                tvNameRoom.setText(houseInfoBean.getHouseName());
            } else {
                String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢/";
                if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                    address += houseInfoBean.getUnitName() + "单元/";
                }
                address += houseInfoBean.getHouseName();
                tvNameRoom.setText(address);
            }
        }

        etBluetoothName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnConnectBluetooth.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
            }
        });


        etBluetoothName.setText("MLT-BT05");

    }

    @OnClick({R.id.iv_title_back, R.id.btn_connect_bluetooth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                if (bluetoothStatus == 0) {
                    finish();
                } else {
                    bluetoothStatus = 0;
                    connectStatus();
                }

                break;
            case R.id.btn_connect_bluetooth:
                bluetoothName = etBluetoothName.getText().toString().trim();
                bluetoothBean = new BluetoothBean();

                bluetoothStatus = 1;
                connectStatus();
                checkBluetooth();
                break;
        }
    }

    /**
     * 连接蓝牙状态
     */
    private void connectStatus() {
        switch (bluetoothStatus) {
            case 0:

                llBluetooth.setVisibility(View.VISIBLE);
                llNetwork.setVisibility(View.GONE);
                llConnectBluetooth.setVisibility(View.GONE);
                etBluetoothName.setEnabled(true);
                btnConnectBluetooth.setEnabled(true);
                break;
            case 1:
                llBluetooth.setVisibility(View.VISIBLE);
                llNetwork.setVisibility(View.GONE);
                llConnectBluetooth.setVisibility(View.VISIBLE);
                ivConnectBluetooth.setImageResource(R.mipmap.ic_bluetooth_connecting);
                tvConnectBluetooth.setText("正在连接蓝牙…");
                etBluetoothName.setEnabled(false);
                btnConnectBluetooth.setEnabled(false);
                break;
            case 2:
                llBluetooth.setVisibility(View.VISIBLE);
                llNetwork.setVisibility(View.GONE);
                llConnectBluetooth.setVisibility(View.VISIBLE);
                ivConnectBluetooth.setImageResource(R.mipmap.ic_bluetooth_fail);
                tvConnectBluetooth.setText("蓝牙连接失败，点击重试");
                etBluetoothName.setEnabled(false);
                btnConnectBluetooth.setEnabled(true);
                break;
            case 3:
                llBluetooth.setVisibility(View.GONE);
                llNetwork.setVisibility(View.VISIBLE);
                ivNetworkChecking.setImageResource(R.mipmap.ic_network_checking);
                tvNetworkChecking.setText("网络检测中，请稍后…");
                break;
            case 4:
                llBluetooth.setVisibility(View.GONE);
                llNetwork.setVisibility(View.VISIBLE);
                ivNetworkChecking.setImageResource(R.mipmap.ic_network_fail);
                tvNetworkChecking.setText("网络不佳，终止安装");
                break;
        }
    }


    /**
     * 检查蓝牙
     */
    private void checkBluetooth() {
        if (!MyApplication.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtils.getInstance().showToast("设备没有蓝牙功能");
            bluetoothStatus = 2;
            connectStatus();
            return;
        }

        if (BluetoothUtils.getInstance().getClient().isBluetoothOpened()) {
            scanBluetooth();
        } else {
            BluetoothUtils.getInstance().getClient().openBluetooth();
        }

        BluetoothUtils.getInstance().getClient().registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                if (openOrClosed) {
                    scanBluetooth();
                } else {
                    ToastUtils.getInstance().showToast("蓝牙已关闭");
                    bluetoothStatus = 2;
                    connectStatus();
                }
            }
        });

    }


    /**
     * 扫描蓝牙
     */
    private void scanBluetooth() {

        BluetoothUtils.getInstance().getClient().search(BluetoothUtils.getInstance().getRequest(), new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                LogUtils.log(device.getAddress() + "," + device.getName());
                if (!TextUtils.isEmpty(device.getName()) && device.getName().equals(bluetoothName)) {
                    bluetoothBean.setName(device.getName());
                    bluetoothBean.setAddress(device.getAddress());
                    connectBluetooth();
                    BluetoothUtils.getInstance().getClient().stopSearch();
                }
            }

            @Override
            public void onSearchStopped() {
                if (TextUtils.isEmpty(bluetoothBean.getName())) {
                    ToastUtils.getInstance().showToast("未查找到相应蓝牙");
                    bluetoothStatus = 2;
                    connectStatus();
                }
            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }

    /**
     * 连接蓝牙
     */
    private void connectBluetooth() {

        BluetoothUtils.getInstance().getClient().connect(bluetoothBean.getAddress(), BluetoothUtils.getInstance().getOption(), new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile data) {
                LogUtils.log(code + "aaa");
                if (code == REQUEST_SUCCESS) {
                    for (BleGattService service : data.getServices()) {
                        if (FormatUtils.getInstance().uplowContains(service.getUUID().toString(), "FFE0")) {
                            bluetoothBean.setServiceUUID(service.getUUID());
                        }
                        for (BleGattCharacter character : service.getCharacters()) {
                            LogUtils.log(service.getUUID().toString() + "," + character.getUuid().toString());
                            if (FormatUtils.getInstance().uplowContains(character.getUuid().toString(), "FFE1")) {
                                bluetoothBean.setWriteCUUID(character.getUuid());
                            }

                            if (FormatUtils.getInstance().uplowContains(character.getUuid().toString(), "FFE1")) {
                                bluetoothBean.setNotifyCUUID(character.getUuid());
                            }
                        }
                    }

                    LogUtils.log(bluetoothBean.toString());

                    BluetoothUtils.getInstance().setBluetoothBean(bluetoothBean);

//                    bluetoothStatus = 3;
//                    connectStatus();
//                    notifyBluetooth();

//                    BluetoothUtils.getInstance().getClient().registerConnectStatusListener(bluetoothBean.getAddress(), new BleConnectStatusListener() {
//                        @Override
//                        public void onConnectStatusChanged(String mac, int status) {
//
//                            LogUtils.log(status + "," + Constants.STATUS_DEVICE_CONNECTED + "," + Constants.STATUS_DEVICE_DISCONNECTED);
//                            if (status == Constants.STATUS_DISCONNECTED) {
//                                LogUtils.log("蓝牙已断开连接");
//                                bluetoothStatus = 2;
//                                connectStatus();
//                            }
//                        }
//                    });

                    Intent intent = new Intent(ConnectBluetoothActivity.this, CheckEquipmentActivity.class);
                    intent.putExtra("house", houseInfoBean);
                    startActivity(intent);
                    finish();

                } else {
                    bluetoothStatus = 2;
                    connectStatus();
                }
            }
        });
    }


    /**
     * 写入
     */
    private void writeToBluetooth(final String command, String content) {
        final String data = FormatUtils.getInstance().getWriteData("AA", command, content);

        LogUtils.log("write：" + data);
        BluetoothUtils.getInstance().getClient().write(bluetoothBean.getAddress(), bluetoothBean.getServiceUUID(),
                bluetoothBean.getWriteCUUID(), FormatUtils.getInstance().hexStringToBytes(data), new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                            LogUtils.log("写入成功");
                            if ("05".equals(command)) {
                                countDownTimer = new CountDownTimer(10000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {

                                        unNotifyBluetooth();
                                        Intent intent = new Intent(ConnectBluetoothActivity.this, CheckEquipmentActivity.class);
                                        intent.putExtra("house", houseInfoBean);
                                        startActivity(intent);
                                        finish();

                                    }
                                };
                                countDownTimer.start();
                            }
                        } else {
                            bluetoothStatus = 2;
                            connectStatus();
                            LogUtils.log("写入失败");
                        }
                    }
                });
    }


    /**
     * 通知
     */
    private void notifyBluetooth() {

        BluetoothUtils.getInstance().getClient().notify(bluetoothBean.getAddress(), bluetoothBean.getServiceUUID(),
                bluetoothBean.getNotifyCUUID(), new BleNotifyResponse() {
                    @Override
                    public void onResponse(int code) {

                    }

                    @Override
                    public void onNotify(UUID service, UUID character, byte[] value) {

                        String data = FormatUtils.getInstance().byteToString(value);
                        LogUtils.log("notify:" + data);
                        if (!TextUtils.isEmpty(data) && data.length() == 40) {
                            if ("AA06".equals(data.substring(0, 4))) {
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                    countDownTimer = null;
                                }
                                int status = Integer.parseInt(new BigInteger(data.substring(16, 18), 16).toString(10));
                                if (status >= 50) {
                                    unNotifyBluetooth();
                                    Intent intent = new Intent(ConnectBluetoothActivity.this, CheckEquipmentActivity.class);
                                    intent.putExtra("house", houseInfoBean);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    bluetoothStatus = 4;
                                    connectStatus();
                                }
                            }
                        }

                    }
                });
    }

    /**
     * 取消通知
     */
    private void unNotifyBluetooth() {

        BluetoothUtils.getInstance().getClient().unnotify(bluetoothBean.getAddress(), bluetoothBean.getServiceUUID(),
                bluetoothBean.getNotifyCUUID(), new BleUnnotifyResponse() {
                    @Override
                    public void onResponse(int code) {

                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (bluetoothStatus == 0) {
            finish();
        } else {
            bluetoothStatus = 0;
            connectStatus();
        }
    }

}

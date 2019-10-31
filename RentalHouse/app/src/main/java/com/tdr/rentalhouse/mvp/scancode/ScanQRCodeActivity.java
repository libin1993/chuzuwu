package com.tdr.rentalhouse.mvp.scancode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseActivity;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.FindAddressBean;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.ScanCodeBean;
import com.tdr.rentalhouse.bean.ScanResult;
import com.tdr.rentalhouse.inter.PopupOnClickListener;
import com.tdr.rentalhouse.mvp.addaddress.AddAddressActivity;
import com.tdr.rentalhouse.mvp.bindhouse.BindHouseActivity;
import com.tdr.rentalhouse.mvp.community.CommunityActivity;
import com.tdr.rentalhouse.mvp.houseinfo.HouseInfoActivity;

import com.tdr.rentalhouse.mvp.roomlist.RoomInfoActivity;
import com.tdr.rentalhouse.mvp.selectaddress.SelectAddressActivity;
import com.tdr.rentalhouse.utils.Base64Utils;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.PopupWindowUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bertsir.zbar.CameraPreview;
import cn.bertsir.zbar.Qr.Symbol;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.ScanCallback;
import cn.bertsir.zbar.utils.QRUtils;
import cn.bertsir.zbar.view.ScanView;

/**
 * Author：Libin on 2019/7/6 13:36
 * Description：
 */
public class ScanQRCodeActivity extends BaseMvpActivity<ScanCodePresenter> implements ScanCallback, BaseView {
    @BindView(R.id.cp_scan)
    CameraPreview cpScan;
    @BindView(R.id.scan_view)
    ScanView scanView;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.cb_flash_light)
    CheckBox cbFlashLight;
    @BindView(R.id.tv_select)
    TextView tvSelect;

    private MyHandler myHandler = new MyHandler(this);
    private int type;

    @Override
    protected ScanCodePresenter initPresenter() {
        return new ScanCodePresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Symbol.is_only_scan_center = true;
        Symbol.scanType = QrConfig.TYPE_QRCODE;
        setContentView(R.layout.activity_scan_qrcode);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void getData() {
        type = getIntent().getIntExtra("type", 0);

    }


    private void initView() {
        if (type == 1) {
            tvTitleName.setText("设备安装");
            tvSelect.setVisibility(View.VISIBLE);
        } else {
            tvTitleName.setText("扫码");
            tvSelect.setVisibility(View.GONE);
        }
        tvTitleMore.setText("相册");
        tvTitleMore.setVisibility(View.VISIBLE);

        scanView.startScan();
        cbFlashLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cpScan.setFlash(isChecked);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cpScan != null) {
            cpScan.setScanCallback(this);
            cpScan.start();
        }
        scanView.onResume();
    }


    @Override
    public void onScanResult(String content) {
        scanResult(content);
    }


    /**
     * 扫描结果
     *
     * @param result
     */
    private void scanResult(String result) {

        vibrate();
        if (type == 1) {
            if (result.contains("?AH")) {
                String[] split = result.split("\\?");
                String type1 = split[1].substring(2);

                byte[] decode = Base64Utils.decode(type1);

                String str = FormatUtils.getInstance().bytes2Hex(decode);
                LogUtils.log(str);
                if (str.length() == 22) {
                    String code = str.substring(6, 18);
                    String areaCode = str.substring(0, 6);
                    LoadingUtils.getInstance().showLoading(ScanQRCodeActivity.this, "加载中");
                    mPresenter.scanCode(RequestCode.NetCode.SCAN_CODE, code,areaCode);
                } else {
                    ToastUtils.getInstance().showToast("请扫描有效房屋二维码");
                    finish();
                }


            } else {
                ToastUtils.getInstance().showToast("请扫描有效房屋二维码");
                finish();
            }
        } else {
            EventBus.getDefault().post(new ScanResult(result));
            finish();
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (cpScan != null) {
            cpScan.stop();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (cpScan != null) {
            cpScan.setFlash(false);
            cpScan.stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (cpScan != null) {
            cpScan.setFlash(false);
            cpScan.stop();
        }

        myHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
        vibrator.cancel();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            if (ObjectUtils.getInstance().isNotNull(PictureSelector.obtainMultipleResult(data))) {
                final String imagePath = PictureSelector.obtainMultipleResult(data).get(0).getPath();
                if (TextUtils.isEmpty(imagePath)) {
                    ToastUtils.getInstance().showToast("获取图片失败！");
                    finish();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //优先使用zbar识别一次二维码
                            String qrContent = QRUtils.getInstance().decodeQRcode(imagePath);
                            if (!TextUtils.isEmpty(qrContent)) {
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = qrContent;
                                myHandler.sendMessage(msg);
                            } else {
                                //尝试用zxing再试一次识别二维码
                                String qrCode = QRUtils.getInstance().decodeQRcodeByZxing(imagePath);
                                if (!TextUtils.isEmpty(qrCode)) {
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = qrCode;
                                    myHandler.sendMessage(msg);
                                } else {
                                    myHandler.sendEmptyMessage(2);
                                }

                            }
                        } catch (Exception e) {
                            myHandler.sendEmptyMessage(2);

                        }
                    }
                }).start();
            }

        }
    }

    @Override
    public void onSuccess(int what, Object object) {
        switch (what) {
            case RequestCode.NetCode.SCAN_CODE:
                ScanCodeBean.DataBean scanBean = (ScanCodeBean.DataBean) object;
                if (scanBean != null) {
                    mPresenter.getHouseInfo(RequestCode.NetCode.HOUSE_INFO, scanBean.getFloorId());
                }
                break;
            case RequestCode.NetCode.HOUSE_INFO:
                HouseBean.DataBean dataBean = (HouseBean.DataBean) object;
                if (dataBean != null) {
                    Intent intent = new Intent(ScanQRCodeActivity.this, RoomInfoActivity.class);

                    HouseInfoBean houseInfoBean = new HouseInfoBean();
                    houseInfoBean.setCommunityName(dataBean.getCommunityName());
                    houseInfoBean.setBuildingName(dataBean.getBuildingNumber());
                    houseInfoBean.setUnitName(dataBean.getUnitNumber());
                    houseInfoBean.setFloorName(dataBean.getFloorNumber());
                    houseInfoBean.setHouseName(dataBean.getRoomNumber());
                    houseInfoBean.setHouseId(dataBean.getId());
                    houseInfoBean.setAreaNumber(dataBean.getRDNumber());
                    houseInfoBean.setBuildingType(dataBean.getArchitecturalTypes());
                    houseInfoBean.setImg(dataBean.getOutlookOne());
                    houseInfoBean.setCityName(dataBean.getCityName());
                    houseInfoBean.setAreaName(dataBean.getAreaName());
                    houseInfoBean.setStreetName(dataBean.getSteetName());
                    houseInfoBean.setResidentialName(dataBean.getComminityName());

                    intent.putExtra("house", houseInfoBean);
                    startActivity(intent);
                    finish();
                }

                break;
        }
    }

    @Override
    public void onFail(int what, String msg) {

        switch (what) {
            case RequestCode.NetCode.SCAN_CODE:
                PopupWindowUtils.getInstance().showPopWindow(ScanQRCodeActivity.this,
                        "房屋未采集，是否进行地址采集？", "确定", new PopupOnClickListener() {
                            @Override
                            public void onCancel() {
                                PopupWindowUtils.getInstance().dismiss();
                                finish();
                            }

                            @Override
                            public void onConfirm() {
                                PopupWindowUtils.getInstance().dismiss();
                                Intent intent = new Intent(ScanQRCodeActivity.this, AddAddressActivity.class);
                                intent.putExtra("type", 1);
                                startActivity(intent);
                                finish();
                            }
                        });
                break;
            case RequestCode.NetCode.HOUSE_INFO:
                ToastUtils.getInstance().showToast(msg);
                finish();
                break;
        }

    }

    @Override
    public void hideLoading() {
        LoadingUtils.getInstance().dismiss();
    }


    private static class MyHandler extends Handler {
        //WeakReference 弱引用
        private final WeakReference<ScanQRCodeActivity> mActivity;

        public MyHandler(ScanQRCodeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ScanQRCodeActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        activity.scanResult(String.valueOf(msg.obj));
                        break;
                    case 2:
                        ToastUtils.getInstance().showToast("识别失败！");
                        activity.finish();
                        break;
                }

            }
        }
    }


    @OnClick({R.id.iv_title_back, R.id.tv_title_more, R.id.tv_select})
    public void onViewClicked(View view) {
        if (FastClickUtils.isSingleClick()) {
            switch (view.getId()) {
                case R.id.iv_title_back:
                    finish();
                    break;
                case R.id.tv_title_more:
                    selectPicture();
                    break;
                case R.id.tv_select:
                    Intent intent = new Intent(ScanQRCodeActivity.this, SelectAddressActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    finish();

                    break;
            }
        }

    }

    /**
     * 打开相册
     */
    private void selectPicture() {
        PictureSelector.create(ScanQRCodeActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .isCamera(false)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


}

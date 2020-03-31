package com.tdr.rentalhouse.mvp.installotherdevice;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.ScanResult;
import com.tdr.rentalhouse.mvp.scancode.ScanQRCodeActivity;
import com.tdr.rentalhouse.utils.Base64Utils;
import com.tdr.rentalhouse.utils.FormatUtils;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author：Libin on 2020-03-19 18:15
 * Email：1993911441@qq.com
 * Describe：
 */
public class OtherDeviceActivity extends BaseMvpActivity<OtherDevicePresenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.tv_fire_control_address)
    TextView tvFireControlAddress;
    @BindView(R.id.tv_fire_control_building)
    TextView tvFireControlBuilding;
    @BindView(R.id.et_device_code)
    EditText etDeviceCode;
    @BindView(R.id.iv_scan_qrcode)
    ImageView ivScanQrcode;
    @BindView(R.id.tv_device_type)
    TextView tvDeviceType;

    @BindView(R.id.rv_add_device_picture)
    RecyclerView rvAddDevice;
    @BindView(R.id.et_device_name)
    EditText etDeviceName;
    private HouseInfoBean houseInfoBean;

    private List<LocalMedia> pictureList = new ArrayList<>();
    private List<String> imgList = new ArrayList<>();
    private BaseQuickAdapter<LocalMedia, BaseViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_device);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getData();
        initView();
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        if (houseInfoBean.getType() == 1){
            tvTitleName.setText("更换其他设施");
        }else {
            tvTitleName.setText("新增其他设施");
        }

        tvTitleMore.setVisibility(View.VISIBLE);
        tvTitleMore.setText("提交");
        tvTitleMore.setTextColor(ContextCompat.getColor(this,R.color.blue_3e7));


        if (houseInfoBean.getHouseId() == -1 ||houseInfoBean.getHouseId() == -2) {
            tvFireControlAddress.setText(houseInfoBean.getCommunityName());
            String address = "楼幢：" + houseInfoBean.getBuildingName() + "幢          ";
            if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                address += "单元：" + houseInfoBean.getUnitName() + "单元          ";
            }
            address += houseInfoBean.getHouseName();
            tvFireControlBuilding.setText(address);
        } else {
            if (houseInfoBean.getBuildingType() ==2 || houseInfoBean.getBuildingType() ==3){
                String address = houseInfoBean.getCommunityName() + "/" + houseInfoBean.getBuildingName() + "幢/";
                if (!TextUtils.isEmpty(houseInfoBean.getUnitName())) {
                    address += houseInfoBean.getUnitName() + "单元/";
                }
                address += houseInfoBean.getHouseName() + "室";

                tvFireControlAddress.setText(address);
            }else {
                tvFireControlAddress.setText(houseInfoBean.getAreaNumber());
            }
            tvFireControlBuilding.setText("房东：" + houseInfoBean.getLandlordName() + "          联系电话：" + houseInfoBean.getPhone());
        }

        etDeviceName.addTextChangedListener(new LimitInputTextWatcher(etDeviceName, LimitInputTextWatcher.CHINESE_REGEX));

        etDeviceCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvDeviceType.setText(null);
                if (!TextUtils.isEmpty(s.toString().trim())) {

                    String deviceNo = s.toString().trim().toUpperCase();
                    if (deviceNo.length() >=5){
                        mPresenter.deviceName(RequestCode.NetCode.DEVICE_NAME,deviceNo);
                    }
                }
            }
        });


        rvAddDevice.setLayoutManager(new LinearLayoutManager(this, GridLayoutManager.HORIZONTAL, false));
        adapter = new BaseQuickAdapter<LocalMedia, BaseViewHolder>(R.layout.layout_rv_piture_item, pictureList) {
            @Override
            protected void convert(BaseViewHolder helper, LocalMedia item) {
                SimpleDraweeView ivPicture = helper.getView(R.id.iv_picture_checked);
                ivPicture.setImageURI(Uri.fromFile(new File(item.getCompressPath())));

                helper.addOnClickListener(R.id.iv_picture_cancel);
            }

        };

        View view = LayoutInflater.from(this).inflate(R.layout.layout_rv_picture_footer, null);
        view.setLayoutParams(new RadioGroup.LayoutParams(FormatUtils.getInstance().dp2px(110), FormatUtils.getInstance().dp2px(120)));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });

        adapter.addFooterView(view);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                pictureList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, pictureList.size() - position);
            }
        });


        rvAddDevice.setAdapter(adapter);
    }

    /**
     * 照片选择弹出
     */
    private void selectPicture() {
        if (pictureList.size() >= 3) {
            ToastUtils.getInstance().showToast("最多可选3张图片");
            return;
        }

        PictureSelector.create(OtherDeviceActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(3 - pictureList.size())
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .isCamera(true)
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropGrid(false)
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .cropCompressQuality(60)
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> localMediaList = PictureSelector.obtainMultipleResult(data);
            if (ObjectUtils.getInstance().isNotNull(localMediaList)) {

                pictureList.addAll(localMediaList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        //清除裁剪和压缩后的缓存
        PictureFileUtils.deleteCacheDirFile(this);
    }


    private void getData() {
        houseInfoBean = (HouseInfoBean) getIntent().getSerializableExtra("house");

    }

    private void upload() {
        if (TextUtils.isEmpty(etDeviceCode.getText().toString().trim())){
            ToastUtils.getInstance().showToast("请输入设施编码");
            return;
        }

        if (TextUtils.isEmpty(tvDeviceType.getText().toString().trim())){
            ToastUtils.getInstance().showToast("请输入有效的设施编码");
            return;
        }

        if (TextUtils.isEmpty(etDeviceName.getText().toString().trim())){
            ToastUtils.getInstance().showToast("请输入点位信息");
            return;
        }

        if (pictureList.size() == 0){
            ToastUtils.getInstance().showToast("请添加设施安装图片");
            return;
        }

        LoadingUtils.getInstance().showLoading(this,"加载中");
        if (imgList.size() > 0){
            submitData();
        }else {
            Map<String, RequestBody> map = new HashMap<>();
            for (int i = 0; i < pictureList.size(); i++) {
                LogUtils.log(pictureList.get(i).getCompressPath());
                File file = new File(pictureList.get(i).getCompressPath());
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
                map.put("file" + i + "\"; filename=\"" + file.getName(), requestFile);
            }

            mPresenter.upload(RequestCode.NetCode.UPLOAD, map);
        }
    }

    private void submitData(){
        Map<String, Object> map = new HashMap<>();
        if (houseInfoBean.getType() ==1) {
            map.put("EquipRoomBindId", houseInfoBean.getEquipRoomBindId());
        }
        map.put("DeviceCode", etDeviceCode.getText().toString().trim().toUpperCase());
        map.put("ManageId", houseInfoBean.getManageId());
        map.put("DeviceName", etDeviceName.getText().toString().trim());
        map.put("InstallPosition", houseInfoBean.getInstallPosition());

        mPresenter.installOtherDevice(RequestCode.NetCode.ADD_OTHER_DEVICE, map, (ArrayList<String>) imgList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void scanResult(ScanResult scanResult) {

        String result = scanResult.getResult();
        if (result.contains("?X")) {
            String[] split = result.split("\\?");
            String type1 = split[1].substring(2);
            String decode = Base64Utils.decodeToString(type1);

            if (decode.length() == 14) {
                etDeviceCode.setText(decode);
                etDeviceCode.setSelection(etDeviceCode.getText().toString().length());
            } else {
                ToastUtils.getInstance().showToast("当前设备不是消防设施");
            }
        } else {
            ToastUtils.getInstance().showToast("当前设备不是消防设施");
        }

    }

    /**
     * 相机权限
     */
    private void getPermission() {
        if (PermissionUtils.getInstance().hasPermission(this, Manifest.permission.CAMERA)) {
            startActivity(new Intent(OtherDeviceActivity.this, ScanQRCodeActivity.class));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RequestCode.Permission.CAMERA_PERMISSION);
        }
    }

    @Override
    protected OtherDevicePresenter initPresenter() {
        return new OtherDevicePresenter();
    }

    @Override
    public void onSuccess(int what, Object object) {
        switch (what){
            case RequestCode.NetCode.DEVICE_NAME:
                String deviceType = (String) object;
                tvDeviceType.setText(deviceType);
                break;
            case RequestCode.NetCode.ADD_OTHER_DEVICE:
                if (houseInfoBean.getType() == 1){
                    tvTitleName.setText("更换成功");
                }else {
                    tvTitleName.setText("安装成功");
                }
                finish();
                break;
            case RequestCode.NetCode.UPLOAD:
                List<String> dataList = ((BaseBean<List<String>>) object).getData();
                if (ObjectUtils.getInstance().isNotNull(dataList)) {
                    imgList.addAll(dataList);
                }
                submitData();
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

    @OnClick({R.id.iv_title_back, R.id.tv_title_more, R.id.iv_scan_qrcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_more:
                upload();
                break;
            case R.id.iv_scan_qrcode:
                getPermission();

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode.Permission.CAMERA_PERMISSION) {
            if (!PermissionUtils.getInstance().hasPermission(this, Manifest.permission.CAMERA)) {
                PermissionUtils.getInstance().showPermissionDialog(OtherDeviceActivity.this,
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
                startActivity(new Intent(OtherDeviceActivity.this, ScanQRCodeActivity.class));
            }
        }
    }

}

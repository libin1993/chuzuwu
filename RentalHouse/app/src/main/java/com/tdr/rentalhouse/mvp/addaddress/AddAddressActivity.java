package com.tdr.rentalhouse.mvp.addaddress;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.Api;
import com.tdr.rentalhouse.base.BaseBean;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.AddAddressBean;
import com.tdr.rentalhouse.bean.CommunityDetailBean;
import com.tdr.rentalhouse.bean.HouseBean;
import com.tdr.rentalhouse.bean.HouseInfoBean;
import com.tdr.rentalhouse.bean.LocationBean;
import com.tdr.rentalhouse.bean.PictureBean;
import com.tdr.rentalhouse.inter.PopupOnClickListener;
import com.tdr.rentalhouse.mvp.bindhouse.BindHouseActivity;
import com.tdr.rentalhouse.mvp.city.CityActivity;
import com.tdr.rentalhouse.mvp.community.CommunityActivity;
import com.tdr.rentalhouse.mvp.houseinfo.HouseInfoActivity;
import com.tdr.rentalhouse.ui.SearchAddressActivity;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.FormatUtils;
import com.tdr.rentalhouse.utils.LimitInputTextWatcher;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.LogUtils;
import com.tdr.rentalhouse.utils.ObjectUtils;
import com.tdr.rentalhouse.utils.PopupWindowUtils;
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

//import com.smarttop.library.bean.City;
//import com.smarttop.library.bean.County;
//import com.smarttop.library.bean.Province;
//import com.smarttop.library.bean.Street;
//import com.smarttop.library.utils.LogUtil;
//import com.smarttop.library.widget.AddressSelector;
//import com.smarttop.library.widget.BottomDialog;
//import com.smarttop.library.widget.OnAddressSelectedListener;

/**
 * Author：Libin on 2019/7/4 10:26
 * Description：地址采集
 */
public class AddAddressActivity extends BaseMvpActivity<AddAddressContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_select_address)
    TextView tvSelectAddress;
    @BindView(R.id.ll_select_address)
    LinearLayout llSelectAddress;
    @BindView(R.id.tv_select_building_type)
    TextView tvSelectBuildingType;
    @BindView(R.id.ll_building_type)
    LinearLayout llBuildingType;
    @BindView(R.id.et_area_name)
    EditText etAreaName;
    @BindView(R.id.ll_area_name)
    LinearLayout llAreaName;
    @BindView(R.id.view_area_name)
    View viewAreaName;
    @BindView(R.id.et_area_no)
    EditText etAreaNo;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.rv_add_picture)
    RecyclerView rvAddPicture;
    @BindView(R.id.btn_add_address)
    Button btnAddAddress;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;

//    private BottomDialog mAddressPicker = null; //地址选择器

    private List<LocalMedia> pictureList = new ArrayList<>();
    private List<PictureBean> pictureBeanList = new ArrayList<>();
    private BaseQuickAdapter<PictureBean, BaseViewHolder> adapter;

    private List<String> typeList = new ArrayList<>();
    private OptionsPickerView pickerView;

    //市
    private String cityCode;
    //区
    private String areaCode;
    //街道
    private String streetCode;
    //社区
    private String communityCode;
    //社区id
    private int communityId;
    //建筑类型
    private int buildingType;
    //定位
    private LocationBean locationBean;
    //图片链接
    private List<String> imgList = new ArrayList<>();
    private int type;
    private CommunityDetailBean.DataBean communityBean;

    @Override
    protected AddAddressContact.Presenter initPresenter() {
        return new AddAddressPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        getData();
        initView();
    }


    private void getData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        if (type == 2) {
            communityBean = (CommunityDetailBean.DataBean) intent.getSerializableExtra("community");

            cityCode = communityBean.getCityCode();
            areaCode = communityBean.getAreaCode();
            streetCode = communityBean.getStreet();
            communityCode = communityBean.getCommunity();
            buildingType = communityBean.getArchitecturalTypes();

            locationBean = new LocationBean();
            locationBean.setLat(communityBean.getLatitude());
            locationBean.setLng(communityBean.getLongitude());
            locationBean.setName(communityBean.getPositionName());


            if (communityBean.getOutlookOne() != null) {
                imgList.add(communityBean.getOutlookOne());
            }

            if (communityBean.getOutlookTwo() != null) {
                imgList.add(communityBean.getOutlookTwo());
            }

            if (communityBean.getOutlookThree() != null) {
                imgList.add(communityBean.getOutlookThree());
            }

            for (String s : imgList) {
                pictureBeanList.add(new PictureBean(s, true));
            }

            String address = communityBean.getCityName();
            if (!TextUtils.isEmpty(communityBean.getAreaName())) {
                address += communityBean.getAreaName();
            }

            if (!TextUtils.isEmpty(communityBean.getSteetName())) {
                address += communityBean.getSteetName();
            }

            if (!TextUtils.isEmpty(communityBean.getComminityName())) {
                address += communityBean.getComminityName();
            }

            tvSelectAddress.setText(address);


            llBuildingType.setEnabled(false);

            switch (buildingType) {
                case 1:
                    tvSelectBuildingType.setText("自建房");
                    tvCompanyName.setText("小区名称");
                    etAreaName.setHint("请输入小区名称");
                    llAreaName.setVisibility(View.GONE);
                    viewAreaName.setVisibility(View.GONE);
                    break;
                case 2:
                    tvSelectBuildingType.setText("商品房");
                    etAreaName.setText(communityBean.getCommunityName());
                    tvCompanyName.setText("小区名称");
                    etAreaName.setHint("请输入小区名称");
                    llAreaName.setVisibility(View.VISIBLE);
                    viewAreaName.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    etAreaName.setText(communityBean.getCommunityName());
                    tvSelectBuildingType.setText("企业单位");
                    tvCompanyName.setText("企业名称");
                    etAreaName.setHint("请输入企业名称");
                    llAreaName.setVisibility(View.VISIBLE);
                    viewAreaName.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    tvSelectBuildingType.setText("旅店");
                    tvCompanyName.setText("小区名称");
                    etAreaName.setHint("请输入小区名称");
                    llAreaName.setVisibility(View.GONE);
                    viewAreaName.setVisibility(View.GONE);
                    break;
            }

            etAreaNo.setText(communityBean.getRDNumber());
            tvLocation.setText(communityBean.getPositionName());

        }
    }


    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("地址采集");

        etAreaName.addTextChangedListener(new LimitInputTextWatcher(etAreaName, LimitInputTextWatcher.CHINESE_REGEX));
        etAreaNo.addTextChangedListener(new LimitInputTextWatcher(etAreaNo, LimitInputTextWatcher.CHINESE_NUMBER_REGEX));

        if (type == 1){
            tvTitleMore.setVisibility(View.VISIBLE);
            tvTitleMore.setText("上次区域");
            tvTitleMore.setTextColor(ContextCompat.getColor(this,R.color.orange_fda));
        }else {
            tvTitleMore.setVisibility(View.GONE);
        }

//        //初始化地址选择器
//        mAddressPicker = new BottomDialog(this);
//        mAddressPicker.setIndicatorBackgroundColor(R.color.blue_0d8);
//        mAddressPicker.setTextSelectedColor(R.color.blue_0d8);
//        mAddressPicker.setTextUnSelectedColor(R.color.gray_33);
//        mAddressPicker.setOnAddressSelectedListener(this);
//        mAddressPicker.setDialogDismisListener(new AddressSelector.OnDialogCloseListener() {
//            @Override
//            public void dialogclose() {
//                mAddressPicker.dismiss();
//            }
//        });

        typeList.add("自建房");
        typeList.add("商品房");
        typeList.add("企业单位");
        typeList.add("旅店");


        pickerView = new OptionsPickerBuilder(AddAddressActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                buildingType = options1 + 1;
                if (options1 == 1 || options1 == 2) {
                    llAreaName.setVisibility(View.VISIBLE);
                    viewAreaName.setVisibility(View.VISIBLE);
                } else {
                    llAreaName.setVisibility(View.GONE);
                    viewAreaName.setVisibility(View.GONE);
                }

                if (options1 == 2) {
                    tvCompanyName.setText("企业名称");
                    etAreaName.setHint("请输入企业名称");
                } else {
                    tvCompanyName.setText("小区名称");
                    etAreaName.setHint("请输入小区名称");
                }

                tvSelectBuildingType.setText(typeList.get(options1));
                isFinish();
            }
        }).setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(15)//滚轮文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setSubmitColor(getResources().getColor(R.color.blue_3f5))//确定按钮文字颜色
                .setSubCalSize(13)
                .setCancelColor(getResources().getColor(R.color.gray_33))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.gray_f2))//标题背景颜色 Night mode
                .build();
        pickerView.setPicker(typeList);


        rvAddPicture.setLayoutManager(new LinearLayoutManager(this, GridLayoutManager.HORIZONTAL, false));
        adapter = new BaseQuickAdapter<PictureBean, BaseViewHolder>(R.layout.layout_rv_piture_item, pictureBeanList) {
            @Override
            protected void convert(BaseViewHolder helper, PictureBean item) {
                SimpleDraweeView ivPicture = helper.getView(R.id.iv_picture_checked);
                if (item.isUrl()) {
                    ivPicture.setImageURI(Api.IMG_HOST + item.getImg());
                } else {
                    ivPicture.setImageURI(Uri.fromFile(new File(item.getImg())));
                }

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

                if (pictureBeanList.get(position).isUrl()) {
                    for (String s : imgList) {
                        if (s.equals(pictureBeanList.get(position).getImg())) {
                            imgList.remove(s);
                            break;
                        }
                    }
                } else {
                    for (LocalMedia localMedia : pictureList) {
                        if (localMedia.getCompressPath().equals(pictureBeanList.get(position).getImg())) {
                            pictureList.remove(localMedia);
                            break;
                        }
                    }
                }

                pictureBeanList.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, pictureBeanList.size() - position);
            }
        });


        rvAddPicture.setAdapter(adapter);

        etAreaName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isFinish();
            }
        });

        etAreaNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isFinish();
            }
        });

        isFinish();

    }

    @OnClick({R.id.iv_title_back, R.id.ll_select_address, R.id.ll_building_type, R.id.ll_location,
            R.id.btn_add_address,R.id.tv_title_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.ll_select_address:
                if (FastClickUtils.isSingleClick()) {
//                    mAddressPicker.show();
                    startActivity(new Intent(AddAddressActivity.this, CityActivity.class));
                }

                break;
            case R.id.ll_building_type:
                if (FastClickUtils.isSingleClick()) {
                    pickerView.show();
                }

                break;
            case R.id.ll_location:
                if (FastClickUtils.isSingleClick()) {
                    startActivity(new Intent(AddAddressActivity.this, SearchAddressActivity.class));
                }

                break;
            case R.id.btn_add_address:
                if (FastClickUtils.isSingleClick()) {
                    LoadingUtils.getInstance().showLoading(AddAddressActivity.this, "加载中");
                    if (pictureList.size() > 0) {
                        uploadFile();
                    } else {
                        submitData();
                    }
                }
                break;
            case R.id.tv_title_more:

                break;
        }
    }

    /**
     * 上传文件
     */
    private void uploadFile() {
        Map<String, RequestBody> map = new HashMap<>();
        for (int i = 0; i < pictureList.size(); i++) {
            LogUtils.log(pictureList.get(i).getCompressPath());
            File file = new File(pictureList.get(i).getCompressPath());
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            map.put("file" + i + "\"; filename=\"" + file.getName(), requestFile);
        }


        mPresenter.upload(RequestCode.NetCode.UPLOAD, map);
    }

    /**
     * 采集地址
     */
    private void submitData() {
        Map<String, Object> map = new HashMap<>();
        map.put("CityCode", cityCode);
        if (!TextUtils.isEmpty(areaCode)) {
            map.put("AreaCode", areaCode);
        }
        if (!TextUtils.isEmpty(streetCode)) {
            map.put("Street", streetCode);
        }
        if (!TextUtils.isEmpty(communityCode)) {
            map.put("Community", communityCode);
        }
        map.put("ArchitecturalTypes", buildingType);

        if (buildingType == 2 || buildingType == 3) {
            map.put("CommunityName", etAreaName.getText().toString().trim());
        }

        map.put("RDNumber", etAreaNo.getText().toString().trim());
        map.put("Longitude", locationBean.getLng());
        map.put("Latitude", locationBean.getLat());

        if (ObjectUtils.getInstance().isNotNull(imgList)) {
            for (int i = 0; i < imgList.size(); i++) {
                switch (i) {
                    case 0:
                        map.put("OutlookOne", imgList.get(0));
                        break;
                    case 1:
                        map.put("OutlookTwo", imgList.get(1));
                        break;
                    case 2:
                        map.put("OutlookThree", imgList.get(2));
                        break;
                }
            }
        }

        if (type == 1) {
            mPresenter.addAddress(RequestCode.NetCode.ADD_ADDRESS, map);
        } else {
            map.put("Id", communityBean.getId());
            mPresenter.editAddress(RequestCode.NetCode.EDIT_COMMUNITY, map);
        }

    }

    /**
     * 是否填完
     */
    private void isFinish() {
        if (TextUtils.isEmpty(cityCode)) {
            btnAddAddress.setEnabled(false);
            return;
        }

        if (buildingType == 0) {
            btnAddAddress.setEnabled(false);
            return;
        } else if (buildingType == 2 || buildingType == 3) {
            if (TextUtils.isEmpty(etAreaName.getText().toString().trim())) {
                btnAddAddress.setEnabled(false);
                return;
            }
        }

        if (TextUtils.isEmpty(etAreaNo.getText().toString().trim())) {
            btnAddAddress.setEnabled(false);
            return;
        }

        if (locationBean == null) {
            btnAddAddress.setEnabled(false);
            return;
        }

        btnAddAddress.setEnabled(true);
    }

    /**
     * 照片选择弹出
     */
    private void selectPicture() {
        if (pictureBeanList.size() >= 3) {
            ToastUtils.getInstance().showToast("最多可选3张图片");
            return;
        }

        PictureSelector.create(AddAddressActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(3 - pictureBeanList.size())
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
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    /**
     * 定位
     *
     * @param locationBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void location(LocationBean locationBean) {
        this.locationBean = locationBean;
        tvLocation.setText(locationBean.getName());
        isFinish();
    }


    /**
     * @param houseInfoBean 选择地址
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectAddress(HouseInfoBean houseInfoBean) {
        String address = "";
        if (!TextUtils.isEmpty(houseInfoBean.getCityCode())) {
            cityCode = houseInfoBean.getCityCode();
            address += houseInfoBean.getCityName();

        }


        if (!TextUtils.isEmpty(houseInfoBean.getAreaCode())) {
            areaCode = houseInfoBean.getAreaCode();
            address += houseInfoBean.getAreaName();
        }


        if (!TextUtils.isEmpty(houseInfoBean.getStreetCode())) {
            streetCode = houseInfoBean.getStreetCode();
            address += houseInfoBean.getStreetName();
        }


        if (!TextUtils.isEmpty(houseInfoBean.getResidentialCode())) {
            communityCode = houseInfoBean.getResidentialCode();
            address += houseInfoBean.getResidentialName();
        }


        tvSelectAddress.setText(address);

        isFinish();
    }


//    @Override
//    public void onAddressSelected(Province province, City city, County county, Street street) {
//        String address = "";
//        if (province != null && !TextUtils.isEmpty(province.code)) {
//            cityCode = province.code;
//            address += province.name;
//
//        }
//
//
//        if (city != null && !TextUtils.isEmpty(city.code)) {
//            areaCode = city.code;
//            address += city.name;
//        }
//
//
//        if (county != null && !TextUtils.isEmpty(county.code)) {
//            streetCode = county.code;
//            address += county.name;
//        }
//
//
//        if (street != null && !TextUtils.isEmpty(street.code)) {
//            communityCode = street.code;
//            address += street.name;
//        }
//
//
//        tvSelectAddress.setText(address);
//
//        mAddressPicker.dismiss();
//        isFinish();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            List<LocalMedia> localMediaList = PictureSelector.obtainMultipleResult(data);
            if (ObjectUtils.getInstance().isNotNull(localMediaList)) {

                pictureList.addAll(localMediaList);

                for (LocalMedia localMedia : localMediaList) {
                    pictureBeanList.add(new PictureBean(localMedia.getCompressPath(), false));
                }

                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //清除裁剪和压缩后的缓存
        PictureFileUtils.deleteCacheDirFile(this);
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
            case RequestCode.NetCode.ADD_ADDRESS:
                final AddAddressBean.DataBean dataBean = (AddAddressBean.DataBean) object;
                if (dataBean == null) {
                    ToastUtils.getInstance().showToast("提交失败");
                    return;
                }
                communityId = dataBean.getId();
                String title;
                String confirmMsg;
                if (buildingType == 1 || buildingType == 4) {
                    title = "提交成功，是否创建该房屋房间";
                    confirmMsg = "创建房间";
                } else {
                    title = "提交成功，是否创建该小区楼房";
                    confirmMsg = "创建楼房";
                }
                PopupWindowUtils.getInstance().showPopWindow(AddAddressActivity.this, title, confirmMsg, new PopupOnClickListener() {
                    @Override
                    public void onCancel() {
                        PopupWindowUtils.getInstance().dismiss();
                        finish();
                    }

                    @Override
                    public void onConfirm() {
                        PopupWindowUtils.getInstance().dismiss();
                        if (buildingType == 1 || buildingType == 4) {
                            LoadingUtils.getInstance().showLoading(AddAddressActivity.this, "加载中");
                            mPresenter.getHouseInfo(RequestCode.NetCode.HOUSE_INFO, dataBean.getFloorId());
                        } else {
                            Intent intent = new Intent(AddAddressActivity.this, CommunityActivity.class);
                            intent.putExtra("id", dataBean.getId());
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;

            case RequestCode.NetCode.EDIT_COMMUNITY:
                EventBus.getDefault().post("edit_address");
                finish();
                break;
            case RequestCode.NetCode.HOUSE_INFO:
                HouseBean.DataBean houseBean = (HouseBean.DataBean) object;
                if (houseBean != null) {
                    Intent intent;
                    if (TextUtils.isEmpty(houseBean.getLandlordName())) {
                        intent = new Intent(AddAddressActivity.this, BindHouseActivity.class);
                    } else {
                        intent = new Intent(AddAddressActivity.this, HouseInfoActivity.class);
                    }
                    HouseInfoBean houseInfoBean = new HouseInfoBean();
                    houseInfoBean.setCommunityId(communityId);
                    houseInfoBean.setHouseId(houseBean.getId());
                    houseInfoBean.setAreaNumber(houseBean.getRDNumber());
                    houseInfoBean.setBuildingType(houseBean.getArchitecturalTypes());
                    houseInfoBean.setImg(houseBean.getOutlookOne());
                    houseInfoBean.setCityName(houseBean.getCityName());
                    houseInfoBean.setAreaName(houseBean.getAreaName());
                    houseInfoBean.setStreetName(houseBean.getSteetName());
                    houseInfoBean.setResidentialName(houseBean.getComminityName());

                    intent.putExtra("house", houseInfoBean);
                    startActivity(intent);
                    finish();
                }
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





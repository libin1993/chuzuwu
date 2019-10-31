package com.tdr.rentalhouse.mvp.addunit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/12 16:48
 * Description：
 */
public class AddUnitActivity extends BaseMvpActivity<AddUnitContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_more)
    TextView tvTitleMore;
    @BindView(R.id.tv_house_no)
    TextView tvHouseNo;
    @BindView(R.id.et_house_no)
    EditText etHouseNo;
    @BindView(R.id.tv_unit_no)
    TextView tvUnitNo;
    @BindView(R.id.et_unit_no)
    EditText etUnitNo;
    @BindView(R.id.tv_house_range)
    TextView tvHouseRange;
    @BindView(R.id.et_start_house_no)
    EditText etStartHouseNo;
    @BindView(R.id.et_end_house_no)
    EditText etEndHouseNo;
    @BindView(R.id.tv_unit_range)
    TextView tvUnitRange;
    @BindView(R.id.et_start_unit_no)
    EditText etStartUnitNo;
    @BindView(R.id.et_end_unit_no)
    EditText etEndUnitNo;
    @BindView(R.id.iv_type1)
    ImageView ivType1;
    @BindView(R.id.ll_type1)
    LinearLayout llType1;
    @BindView(R.id.iv_type2)
    ImageView ivType2;
    @BindView(R.id.ll_type2)
    LinearLayout llType2;

    private boolean flag = true;
    private int id;

    @Override
    protected AddUnitContact.Presenter initPresenter() {
        return new AddUnitPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void getData() {
        id = getIntent().getIntExtra("id", 0);
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("新增楼房");
        tvTitleMore.setVisibility(View.VISIBLE);
        tvTitleMore.setText("完成");


        etStartHouseNo.addTextChangedListener(twStart);

        etEndHouseNo.addTextChangedListener(twEnd);

    }


    private TextWatcher twStart = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(etStartHouseNo.getText().toString())) {

                int number = Integer.parseInt(etStartHouseNo.getText().toString());
                etStartHouseNo.removeTextChangedListener(twStart);
                if (number == 0) {
                    etStartHouseNo.setText("1");
                } else if (number > 100) {
                    etStartHouseNo.setText("100");
                } else {
                    etStartHouseNo.setText(number + "");
                }

                etStartHouseNo.setSelection(etStartHouseNo.getText().toString().length());
                etStartHouseNo.addTextChangedListener(twStart);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private TextWatcher twEnd = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(etEndHouseNo.getText().toString())) {
                etEndHouseNo.removeTextChangedListener(twEnd);
                int number = Integer.parseInt(etEndHouseNo.getText().toString());

                if (number == 0) {
                    etEndHouseNo.setText("1");
                } else if (number > 100) {
                    etEndHouseNo.setText("100");
                } else {
                    etEndHouseNo.setText(number + "");
                }

                etEndHouseNo.setSelection(etEndHouseNo.getText().toString().length());
                etEndHouseNo.addTextChangedListener(twEnd);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @OnClick({R.id.iv_title_back, R.id.tv_title_more, R.id.ll_type1, R.id.ll_type2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_more:
                if (FastClickUtils.isSingleClick()) {
                    submit();
                }

                break;
            case R.id.ll_type1:
                flag = true;
                initType();
                break;
            case R.id.ll_type2:
                flag = false;
                initType();
                break;
        }
    }

    private void submit() {
        Map<String, Object> map = new HashMap<>();
        if (flag) {
            String houseNo = etHouseNo.getText().toString().trim();
            String unitNo = etUnitNo.getText().toString().trim();
            if (TextUtils.isEmpty(houseNo)) {
                ToastUtils.getInstance().showToast("请输入楼幢号");
                return;
            }

            if (!TextUtils.isEmpty(unitNo)) {
                map.put("UnitNumber", unitNo);
            }

            map.put("CreateType", 1);
            map.put("BuildingNumber", houseNo);

        } else {
            String startHouseNo = etStartHouseNo.getText().toString().trim();
            String endHouseNo = etEndHouseNo.getText().toString().trim();
            String startUnitNo = etStartUnitNo.getText().toString().trim();
            String endUnitNo = etEndUnitNo.getText().toString().trim();


            if (TextUtils.isEmpty(startHouseNo)) {
                ToastUtils.getInstance().showToast("请输入开始楼幢号");
                return;
            }

            if (TextUtils.isEmpty(endHouseNo)) {
                ToastUtils.getInstance().showToast("请输入结束楼幢号");
                return;
            }

            int startHouse = Integer.parseInt(startHouseNo);
            int endHouse = Integer.parseInt(endHouseNo);

            if (startHouse < 1 || startHouse > 100 || endHouse < 1 || endHouse > 100) {
                ToastUtils.getInstance().showToast("请输入1-100楼幢号");
                return;
            }


            if (startHouse > endHouse) {
                ToastUtils.getInstance().showToast("开始楼幢号不能大于结束楼幢号");
                return;
            }


            if (TextUtils.isEmpty(startUnitNo) && !TextUtils.isEmpty(endUnitNo)) {
                ToastUtils.getInstance().showToast("请输入开始单元号");
                return;
            }

            if (!TextUtils.isEmpty(startUnitNo) && TextUtils.isEmpty(endUnitNo)) {
                ToastUtils.getInstance().showToast("请输入结束单元号");
                return;
            }

            if (!TextUtils.isEmpty(startUnitNo) && (Integer.parseInt(startUnitNo) < 1 || Integer.parseInt(startUnitNo) > 5)) {
                ToastUtils.getInstance().showToast("请输入1-5单元号");
                return;
            }

            if (!TextUtils.isEmpty(endUnitNo) && (Integer.parseInt(endUnitNo) < 1 || Integer.parseInt(endUnitNo) > 5)) {
                ToastUtils.getInstance().showToast("请输入1-5单元号");
                return;
            }


            if (!TextUtils.isEmpty(startUnitNo) && !TextUtils.isEmpty(endUnitNo)) {
                int startUnit = Integer.parseInt(startUnitNo);
                int endUnit = Integer.parseInt(endUnitNo);

                if (startUnit > endUnit) {
                    ToastUtils.getInstance().showToast("开始单元号不能大于结束单元号");
                    return;
                }

                map.put("StartUnitNumber", startUnitNo);
                map.put("EndUnitNumber", endUnitNo);
            }

            map.put("CreateType", 2);
            map.put("StartBuildingNumber", startHouseNo);
            map.put("EndBuildingNumber", endHouseNo);

        }

        map.put("ResidentialId", id);
        map.put("RuleType", 1);


        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.addUnit(RequestCode.NetCode.ADD_UNIT, map);

    }

    private void initType() {
        if (flag) {
            ivType1.setImageResource(R.drawable.ring_checked);
            ivType2.setImageResource(R.drawable.ring_normal);
        } else {
            ivType1.setImageResource(R.drawable.ring_normal);
            ivType2.setImageResource(R.drawable.ring_checked);
        }


        tvHouseNo.setEnabled(flag);
        etHouseNo.setEnabled(flag);
        tvUnitNo.setEnabled(flag);
        etUnitNo.setEnabled(flag);

        tvHouseRange.setEnabled(!flag);
        etStartHouseNo.setEnabled(!flag);
        etEndHouseNo.setEnabled(!flag);

        tvUnitRange.setEnabled(!flag);
        etStartUnitNo.setEnabled(!flag);
        etEndUnitNo.setEnabled(!flag);
    }

    @Override
    public void onSuccess(int what, Object object) {
        EventBus.getDefault().post("add_unit");
        finish();
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

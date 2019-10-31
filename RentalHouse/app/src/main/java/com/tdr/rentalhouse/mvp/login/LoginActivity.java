package com.tdr.rentalhouse.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.bean.UserBean;
import com.tdr.rentalhouse.ui.MainActivity;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.MD5Utils;
import com.tdr.rentalhouse.utils.SPUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/18 13:57
 * Description：登录
 */
public class LoginActivity extends BaseMvpActivity<LoginContact.Presenter> implements BaseView {
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.cb_login)
    CheckBox cbLogin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        ivTitleBack.setVisibility(View.GONE);
        tvTitleName.setText("登录");

        cbLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //如果选中，显示密码
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                etPassword.setSelection(etPassword.getText().toString().length());
            }
        });

        etUsername.addTextChangedListener(new TextWatcher() {
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

        etPassword.addTextChangedListener(new TextWatcher() {
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
    }

    private void isFinish() {
        String account = etUsername.getText().toString().trim();
        String pwd = etPassword.getText().toString().trim();

        btnLogin.setEnabled(!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd));
    }

    @Override
    protected LoginContact.Presenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onSuccess(int what, Object object) {
        UserBean.DataBean dataBean = (UserBean.DataBean) object;
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.TOKEN, dataBean.getToken());
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.ACCOUNT_ID, dataBean.getAccountId());
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.USERNAME, dataBean.getUserName());
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.POLICE_STATION_CODE, dataBean.getPoliceStationCode());
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.POLICE_STATION_NAME, dataBean.getPoliceStationName());
        SPUtils.getInstance().put(SPUtils.FILE_USER, SPUtils.COMMUNITY_NAME, dataBean.getGoverCommunityName());
        startActivity(new Intent(this, MainActivity.class));
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

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (FastClickUtils.isSingleClick()) {
            String account = etUsername.getText().toString().trim();
            String pwd = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(account)) {
                ToastUtils.getInstance().showToast("请输入账号");
                return;
            }

            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.getInstance().showToast("请输入密码");
                return;
            }

            LoadingUtils.getInstance().showLoading(this, "加载中");
            mPresenter.login(RequestCode.NetCode.LOGIN, account, MD5Utils.getInstance().md5Encode(pwd));
        }
    }
}

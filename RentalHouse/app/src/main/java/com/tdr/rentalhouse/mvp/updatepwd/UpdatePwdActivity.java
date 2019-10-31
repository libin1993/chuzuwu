package com.tdr.rentalhouse.mvp.updatepwd;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
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
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.MD5Utils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/17 15:11
 * Description：修改密码
 */
public class UpdatePwdActivity extends BaseMvpActivity<UpdatePwdContact.Presenter> implements BaseView {
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.btn_update_pwd)
    Button btnUpdatePwd;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.cb_old_pwd)
    CheckBox cbOldPwd;
    @BindView(R.id.cb_new_pwd)
    CheckBox cbNewPwd;
    @BindView(R.id.cb_confirm_pwd)
    CheckBox cbConfirmPwd;


    @Override
    protected UpdatePwdContact.Presenter initPresenter() {
        return new UpdatePwdPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("修改密码");
        cbOldPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //如果选中，显示密码
                    etOldPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    etOldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                etOldPwd.setSelection(etOldPwd.getText().toString().length());
            }
        });

        cbNewPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //如果选中，显示密码
                    etNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                etNewPwd.setSelection(etNewPwd.getText().toString().length());
            }
        });

        cbConfirmPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //如果选中，显示密码
                    etConfirmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    etConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                etConfirmPwd.setSelection(etConfirmPwd.getText().toString().length());
            }
        });

        etOldPwd.addTextChangedListener(new TextWatcher() {
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

        etNewPwd.addTextChangedListener(new TextWatcher() {
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

        etConfirmPwd.addTextChangedListener(new TextWatcher() {
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

    /**
     * 是否按要求输入
     */
    private void isFinish() {
        String oldPwd = etOldPwd.getText().toString().trim();
        String newPwd = etNewPwd.getText().toString().trim();
        String confirmPwd = etConfirmPwd.getText().toString().trim();

        if (TextUtils.isEmpty(oldPwd)) {
            btnUpdatePwd.setEnabled(false);
            return;
        }

        if (oldPwd.length() < 6) {
            btnUpdatePwd.setEnabled(false);
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            btnUpdatePwd.setEnabled(false);
            return;
        }

        if (newPwd.length() < 6) {
            btnUpdatePwd.setEnabled(false);
            return;
        }

        if (TextUtils.isEmpty(confirmPwd)) {
            btnUpdatePwd.setEnabled(false);
            return;
        }

        if (confirmPwd.length() < 6 || !confirmPwd.equals(newPwd)) {
            btnUpdatePwd.setEnabled(false);
            return;
        }

        btnUpdatePwd.setEnabled(true);
    }


    @OnClick({R.id.iv_title_back, R.id.btn_update_pwd})
    public void onViewClicked(View view) {
        if (FastClickUtils.isSingleClick()) {
            switch (view.getId()) {
                case R.id.iv_title_back:
                    finish();
                    break;
                case R.id.btn_update_pwd:
                    updatePwd();
                    break;
            }
        }

    }

    /**
     * 修改密码
     */
    private void updatePwd() {
        String oldPwd = MD5Utils.getInstance().md5Encode(etOldPwd.getText().toString().trim());
        String newPwd = MD5Utils.getInstance().md5Encode(etNewPwd.getText().toString().trim());


        LoadingUtils.getInstance().showLoading(this, "加载中");
        mPresenter.updatePwd(RequestCode.NetCode.UPDATE_PWD, oldPwd, newPwd);
    }

    @Override
    public void onSuccess(int what, Object object) {
        ToastUtils.getInstance().showToast("修改成功");
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

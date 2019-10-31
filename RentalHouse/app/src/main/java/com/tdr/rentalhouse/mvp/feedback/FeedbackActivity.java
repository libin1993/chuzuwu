package com.tdr.rentalhouse.mvp.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseActivity;
import com.tdr.rentalhouse.base.BaseMvpActivity;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.base.RequestCode;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.LoadingUtils;
import com.tdr.rentalhouse.utils.StatusBarUtils;
import com.tdr.rentalhouse.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author：Li Bin on 2019/7/17 15:23
 * Description：意见反馈
 */
public class FeedbackActivity extends BaseMvpActivity<FeedbackContact.Presenter> implements BaseView {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.et_feedback)
    EditText etFeedback;
    @BindView(R.id.btn_feedback)
    Button btnFeedback;

    @Override
    protected FeedbackContact.Presenter initPresenter() {
        return new FeedbackPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        StatusBarUtils.getInstance().setStatusBarHeight(viewStatusBar);
        tvTitleName.setText("意见反馈");

        etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnFeedback.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.btn_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.btn_feedback:
                if (FastClickUtils.isSingleClick()){
                    LoadingUtils.getInstance().showLoading(FeedbackActivity.this, "加载中");
                    mPresenter.feedback(RequestCode.NetCode.FEEDBACK, etFeedback.getText().toString().trim());
                }

                break;
        }
    }

    @Override
    public void onSuccess(int what, Object object) {
        ToastUtils.getInstance().showToast("提交成功");
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

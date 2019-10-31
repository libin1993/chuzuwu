package com.tdr.rentalhouse.mvp.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tdr.rentalhouse.inter.PopupOnClickListener;
import com.tdr.rentalhouse.mvp.feedback.FeedbackActivity;
import com.tdr.rentalhouse.R;
import com.tdr.rentalhouse.base.BaseView;
import com.tdr.rentalhouse.mvp.updatepwd.UpdatePwdActivity;
import com.tdr.rentalhouse.base.BaseMvpFragment;
import com.tdr.rentalhouse.mvp.login.LoginActivity;
import com.tdr.rentalhouse.utils.FastClickUtils;
import com.tdr.rentalhouse.utils.PopupWindowUtils;
import com.tdr.rentalhouse.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Author：Libin on 2019/7/6 18:01
 * Description：
 */
public class MineFragment extends BaseMvpFragment<MineContact.Presenter> implements BaseView {
    @BindView(R.id.iv_avatar)
    SimpleDraweeView ivAvatar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_police_name)
    TextView tvPoliceName;
    @BindView(R.id.tv_manage_area)
    TextView tvManageArea;
    @BindView(R.id.rl_update_pwd)
    RelativeLayout rlUpdatePwd;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedback;
    @BindView(R.id.rl_log_out)
    RelativeLayout rlLogOut;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvUsername.setText(SPUtils.getInstance().getUsername());
        tvPoliceName.setText("派出所：" + SPUtils.getInstance().getPoliceStationName());
        tvManageArea.setText("管辖社区：" + SPUtils.getInstance().getCommunityName());
    }

    public static MineFragment newInstance() {

        Bundle args = new Bundle();

        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected MineContact.Presenter initPresenter() {
        return new MinePresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_update_pwd, R.id.rl_feedback, R.id.rl_log_out})
    public void onViewClicked(View view) {
        if (FastClickUtils.isSingleClick()){
            switch (view.getId()) {
                case R.id.rl_update_pwd:
                    startActivity(new Intent(getActivity(), UpdatePwdActivity.class));
                    break;
                case R.id.rl_feedback:
                    startActivity(new Intent(getActivity(), FeedbackActivity.class));
                    break;
                case R.id.rl_log_out:
                    PopupWindowUtils.getInstance().showPopWindow(getActivity(), "确定退出登录吗？", "确定", new PopupOnClickListener() {
                        @Override
                        public void onCancel() {
                            PopupWindowUtils.getInstance().dismiss();
                        }

                        @Override
                        public void onConfirm() {
                            PopupWindowUtils.getInstance().dismiss();
                            SPUtils.getInstance().clear(SPUtils.FILE_USER);
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                    break;
            }
        }

    }

    @Override
    public void onSuccess(int what, final Object object) {


    }

//    private void insertData() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<AddressBean.DataBean> list = (List<AddressBean.DataBean>) object;
//                List<address_dict> dataList = new ArrayList<>();
//                LogUtils.log("aaaaa" + list.size());
//
//                for (AddressBean.DataBean dataBean : list) {
//                    address_dict address = new address_dict();
//                    address.setId(Long.parseLong(dataBean.getUnionCode()));
//                    if (!TextUtils.isEmpty(dataBean.getPid())) {
//                        if (dataBean.getName().equals("济宁市")) {
//                            address.setParentId(0L);
//                        } else {
//                            address.setParentId(Long.parseLong(dataBean.getPid()));
//                        }
//
//                    } else {
//                        address.setParentId(370811009000L);
//                    }
//
//                    address.setCode(dataBean.getUnionCode());
//                    address.setName(dataBean.getName());
//                    dataList.add(address);
//
//
//                }
//                SQLiteUtils.getInstance().insert(dataList);
//
//            }
//        }).start();
//    }

    @Override
    public void onFail(int what, String msg) {

    }

    @Override
    public void hideLoading() {

    }
}

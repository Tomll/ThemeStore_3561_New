package com.autoai.themestore.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.autoai.themestore.MyApplication;
import com.autoai.themestore.R;
import com.autoai.themestore.util.Util;
import com.autoai.themestore.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongrp on 2018/9/30.
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.imageViewUserIcon)
    CircleImageView imageViewUserIcon;
    @BindView(R.id.textViewUserName)
    TextView textViewUserName;
    @BindView(R.id.button_login)
    Button buttonLogin;
    //    @BindView(R.id.buttonLocalTheme)  // TODO: 2018/10/16
//    CardView buttonLocalTheme;  // TODO: 2018/10/16
    @BindView(R.id.buttonMyPurchase)
    CardView buttonMyPurchase;
    @BindView(R.id.buttonMyCollection)
    CardView buttonMyCollection;
    @BindView(R.id.buttonMyMessage)
    CardView buttonMyMessage;
    @BindView(R.id.buttonContactUs)
    CardView buttonContactUs;
    @BindView(R.id.tv_my_message)
    TextView tvMyMessage;
    @BindView(R.id.tv_my_collection)
    TextView tvMyCollection;
    @BindView(R.id.tv_my_purchase)
    TextView tvMyPurchase;
    @BindView(R.id.progressLogin)
    FrameLayout progressLogin;
    int loginState;
    String faceUrl;
    String userName;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void initViewBefore() {
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View rootView) {
    }

    @Override
    public void onResume() {
        super.onResume();
        //每次onResume,获取登录状态,用户数据,并执行登录退出操作
//        loginState = Settings.Global.getInt(mActivity.getContentResolver(), "login_state", 0);
//        faceUrl = Settings.Global.getString(mActivity.getContentResolver(), "faceUrl");
//        userName = Settings.Global.getString(mActivity.getContentResolver(), "userName");
        loginState = 1;
        faceUrl = "https://pic.qqtn.com/up/2018-8/2018082909465972970.jpg";
        userName = "辛巴";
        Util.d(this, "onResume:  \nlogin_state: " + loginState + "\nfaceUrl: " + faceUrl + "\nuserName: " + userName);
        if (!MyApplication.sp.getBoolean("logOutByHand", false) && loginState == 1) {//个人中心已登录
            login();
        } else {
            logout();
        }
    }

    //点击回调
    @OnClick({R.id.imageViewUserIcon, R.id.button_login, /* R.id.buttonLocalTheme, */ R.id.buttonMyPurchase, R.id.buttonMyCollection, R.id.buttonMyMessage, R.id.buttonContactUs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewUserIcon://用户头像
                break;
            case R.id.button_login://登录/退出登录
                if (buttonLogin.getText().equals(getString(R.string.login))) {
                    if (loginState == 0) {//个人中心未登录
                        Util.toastShort(mActivity, R.string.please_login_center_first);
                    } else if (loginState == 1) {//个人中心已登录
                        progressLogin.setVisibility(View.VISIBLE);
                        //2秒后登录
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                              login();
                            }
                        },2000);
                    }
                } else if (buttonLogin.getText().equals(getString(R.string.logout))) {
                    logout();
                }
                break;
            /*case R.id.buttonLocalTheme://本地主题卡片
                addFragment(new LocalThemeFragment());
                break;*/ // TODO: 2018/10/16
            case R.id.buttonMyPurchase://我的购买卡片
                if (buttonLogin.getText().equals(getString(R.string.login))) {
                    Util.toastShort(mActivity, R.string.please_login_first);
                    return;
                }
                addFragment(new MyPurchaseFragment());
                break;
            case R.id.buttonMyCollection://我的收藏卡片
                if (buttonLogin.getText().equals(getString(R.string.login))) {
                    Util.toastShort(mActivity, R.string.please_login_first);
                    return;
                }
                addFragment(new MyCollectionFragment());
                break;
            case R.id.buttonMyMessage://我的消息卡片
                if (buttonLogin.getText().equals(getString(R.string.login))) {
                    Util.toastShort(mActivity, R.string.please_login_first);
                    return;
                }
                addFragment(new MyMessageFragment());
                break;
            case R.id.buttonContactUs://联系我们卡片
                addFragment(new ContactUsFragment());
                break;
        }

    }


    //登录逻辑
    public void login() {
        //延时两秒
        progressLogin.setVisibility(View.GONE);
        //真实登录逻辑
        MyApplication.editor.putBoolean("logOutByHand", false).commit();
        if (null != userName) {
            textViewUserName.setText(userName);
        } else {
            textViewUserName.setText("");
        }
        if (null != faceUrl) {
            Glide.with(mActivity).load(faceUrl).into(imageViewUserIcon);
        }
        buttonLogin.setText(R.string.logout);
        setCardEnable(true);
    }

    //退出逻辑
    public void logout() {
        //清空用户名,头像
        MyApplication.editor.putBoolean("logOutByHand", true).commit();
        imageViewUserIcon.setImageResource(R.mipmap.head);
        textViewUserName.setText(R.string.unlogin);
        buttonLogin.setText(R.string.login);
        setCardEnable(false);
    }

    //设置"购买""消息""收藏"三张卡片的使能状态
    public void setCardEnable(/*int id,*/ boolean isEnable) {
        if (isEnable) {
            tvMyMessage.setEnabled(true);
            tvMyCollection.setEnabled(true);
            tvMyPurchase.setEnabled(true);
        } else {
            tvMyMessage.setEnabled(false);
            tvMyCollection.setEnabled(false);
            tvMyPurchase.setEnabled(false);
        }
    }


}

package com.mapbar.wesmart.themestore.fragment;

import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.util.LogUtil;
import com.mapbar.wesmart.themestore.widget.CircleImageView;

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
    int loginState;
    String faceUrl;
    String userName;

    @Override
    public void initViewBefore() {
        //获取登录状态,用户数据
        loginState = Settings.Global.getInt(mActivity.getContentResolver(), "login_state", 0);
        faceUrl = Settings.Global.getString(mActivity.getContentResolver(), "faceUrl");
        userName = Settings.Global.getString(mActivity.getContentResolver(), "userName");
        LogUtil.d(this, "initViewBefore:  \nlogin_state: " + loginState + "\nfaceUrl: " + faceUrl + "\nuserName: " + userName);
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View rootView) {
//        "http://wx1.sinaimg.cn/orj360/006pnLoLgy1ft6yichmarj30j60j675x.jpg"
        if (loginState == 1 && null != userName && null != faceUrl) {//个人中心已登录
            Glide.with(mActivity).load(faceUrl).into(imageViewUserIcon);
            textViewUserName.setText(userName);
            buttonLogin.setText(R.string.logout);
        }
    }

    //点击回调
    @OnClick({R.id.imageViewUserIcon, R.id.button_login, /* R.id.buttonLocalTheme, */ R.id.buttonMyPurchase, R.id.buttonMyCollection, R.id.buttonMyMessage, R.id.buttonContactUs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewUserIcon://用户头像
                break;
            case R.id.button_login://登录/退出登录
                if (buttonLogin.getText().equals("登录")) {
                    if (loginState == 0) {//个人中心未登录
                        Toast.makeText(mActivity, "请先登录个人中心账号", Toast.LENGTH_SHORT).show();
                    } else if (loginState == 1 && null != userName && null != faceUrl) {//个人中心已登录
                        Glide.with(mActivity).load(faceUrl).into(imageViewUserIcon);
                        textViewUserName.setText(userName);
                        buttonLogin.setText(R.string.logout);
                    }
                } else if (buttonLogin.getText().equals("退出")) {
                    //清空用户名,头像
                    imageViewUserIcon.setImageResource(R.mipmap.head);
                    textViewUserName.setText(R.string.unlogin);
                    buttonLogin.setText(R.string.login);
                }
                break;
            /*case R.id.buttonLocalTheme://本地主题卡片
                addFragment(new LocalThemeFragment());
                break;*/ // TODO: 2018/10/16
            case R.id.buttonMyPurchase://我的购买卡片
                addFragment(new MyPurchaseFragment());
                break;
            case R.id.buttonMyCollection://我的收藏卡片
                addFragment(new MyCollectionFragment());
                break;
            case R.id.buttonMyMessage://我的消息卡片
                addFragment(new MyMessageFragment());
                break;
            case R.id.buttonContactUs://联系我们卡片
                addFragment(new ContactUsFragment());
                break;
        }

    }


    //设置卡片的使能状态
    public void setCardEnable(int id, boolean isEnable) {
        switch (id) {
            case R.id.buttonMyMessage:
                if (isEnable) {
                    buttonMyMessage.setClickable(true);
                    tvMyMessage.setEnabled(true);
                } else {
                    buttonMyMessage.setClickable(false);
                    tvMyMessage.setEnabled(false);
                }
                break;
            case R.id.buttonMyCollection:
                if (isEnable) {
                    buttonMyCollection.setClickable(true);
                    tvMyCollection.setEnabled(true);
                } else {
                    buttonMyCollection.setClickable(false);
                    tvMyCollection.setEnabled(false);
                }
                break;
            case R.id.buttonMyPurchase:
                if (isEnable) {
                    buttonMyPurchase.setClickable(true);
                    tvMyPurchase.setEnabled(true);
                } else {
                    buttonMyPurchase.setClickable(false);
                    tvMyPurchase.setEnabled(false);
                }
                break;
        }
    }


}

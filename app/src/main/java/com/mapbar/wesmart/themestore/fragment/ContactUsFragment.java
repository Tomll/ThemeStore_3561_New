package com.mapbar.wesmart.themestore.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dongrp on 2018/9/30.
 */

public class ContactUsFragment extends BaseFragment {

    @BindView(R.id.button_back)
    TextView buttonBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.text_mapbar_url)
    TextView textMapbarUrl;
    @BindView(R.id.text_mapbar_tel)
    TextView textMapbarTel;
    Unbinder unbinder;

    @Override
    public void initViewBefore() {

    }

    @Override
    public int getContentId() {
        return R.layout.fragment_contact_us;
    }

    @Override
    public void initView(View rootView) {
        title.setText(R.string.contact_us);
    }


    @OnClick({R.id.button_back, R.id.text_mapbar_url, R.id.text_mapbar_tel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                mActivity.goBack();
                LogUtil.d(this, "goBack");
                break;
            case R.id.text_mapbar_url://官网网址
                //调用系统默认浏览器，访问网址
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(textMapbarUrl.getText().toString())));
                break;
            case R.id.text_mapbar_tel://客服电话
                //跳转到拨号界面，用户手动点击拨打
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + textMapbarTel.getText())));
                break;
        }
    }


}

package com.autoai.themestore.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.autoai.themestore.util.Util;
import com.autoai.themestore.R;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dongrp on 2018/9/30.
 */

public class ContactUsFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.text_mapbar_url)
    TextView textMapbarUrl;
    @BindView(R.id.text_mapbar_tel)
    TextView textMapbarTel;
    @BindView(R.id.text_mapbar_qq)
    TextView textMapbarQQ;
    Unbinder unbinder;
    ClipboardManager cm;

    @Override
    public void initViewBefore() {
        cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_contact_us;
    }

    @Override
    public void initView(View rootView) {
        title.setText(R.string.contact_us);
    }


    @OnClick({R.id.button_back, R.id.text_mapbar_url, R.id.text_mapbar_tel, R.id.text_mapbar_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                mActivity.goBack();
                Util.d(this, "goBack");
                break;
            case R.id.text_mapbar_url://官网网址
                //startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(textMapbarUrl.getText().toString())));//调用系统默认浏览器，访问网址
//                cm.setText(textMapbarUrl.getText().toString());
//                Util.toastShort(mActivity, R.string.copy_net_address);
                break;
            case R.id.text_mapbar_tel://客服电话
                //跳转到拨号界面，用户手动点击拨打
//                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + textMapbarTel.getText())));
                break;
            case R.id.text_mapbar_qq://QQ号
//                cm.setText(textMapbarQQ.getText().toString());
//                Util.toastShort(mActivity, R.string.copy_qq_number);
                break;
        }
    }


}

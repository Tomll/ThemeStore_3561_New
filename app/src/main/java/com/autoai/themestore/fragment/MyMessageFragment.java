package com.autoai.themestore.fragment;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.autoai.themestore.R;
import com.autoai.themestore.util.Util;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongrp on 2018/9/30.
 */

public class MyMessageFragment extends BaseFragment {

    @BindView(R.id.frameLayoutNoContent)
    FrameLayout frameLayoutNoContent;
    @BindView(R.id.textViewNoContent)
    TextView textViewNoContent;
    @BindView(R.id.title)
    TextView title;

    @Override
    public void initViewBefore() {

    }

    @Override
    public int getContentId() {
        return R.layout.fragment_my_message;
    }

    @Override
    public void initView(View rootView) {
        title.setText(R.string.my_message);
        textViewNoContent.setText(R.string.no_message);
        Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.no_message);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getMinimumHeight());//必须设置bounds，否则drawable不显示
        textViewNoContent.setCompoundDrawables(null, drawable, null, null);
    }

    @OnClick({R.id.button_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                mActivity.goBack();
                Util.d(this, "goBack");
                break;
        }
    }
}

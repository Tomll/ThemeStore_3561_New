package com.mapbar.wesmart.themestore.fragment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.util.Util;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongrp on 2018/9/30.
 */

public class MyMessageFragment extends BaseFragment {

    @BindView(R.id.button_back)
    TextView buttonBack;
    @BindView(R.id.frameLayoutNoContent)
    FrameLayout frameLayoutNoContent;
    @BindView(R.id.imageViewNoContent)
    ImageView imageViewNoContent;
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
        imageViewNoContent.setImageResource(R.mipmap.no_message);
        textViewNoContent.setText(R.string.no_message);
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

package com.mapbar.wesmart.themestore.fragment;

import android.view.View;
import android.widget.TextView;

import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongrp on 2018/9/30.
 */

public class MyCollectionFragment extends BaseFragment {
    @BindView(R.id.button_back)
    TextView buttonBack;
    @BindView(R.id.textViewNoContent)
    TextView textViewNoContent;
    @BindView(R.id.title)
    TextView title;

    @Override
    public void initViewBefore() {

    }

    @Override
    public int getContentId() {
        return R.layout.fragment_my_collection;
    }

    @Override
    public void initView(View rootView) {
        title.setText(R.string.my_collection);
    }

    @OnClick({R.id.button_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                mActivity.goBack();
                LogUtil.d(this, "goBack");
                break;
        }
    }

}

package com.autoai.themestore.fragment;

import android.annotation.SuppressLint;
import android.view.View;

import com.autoai.themestore.util.Util;
import com.autoai.themestore.widget.auto_scroll_viewpager.TipPointGroup;
import com.autoai.themestore.R;
import com.autoai.themestore.widget.auto_scroll_viewpager.AutoViewPager;
import com.autoai.themestore.widget.auto_scroll_viewpager.AutoViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by dongrp on 2018/9/30.
 */

public class ThemeFragment extends BaseFragment {

    @BindView(R.id.autoViewPager)
    AutoViewPager autoViewPager;
    @BindView(R.id.tipPointGroup)
    TipPointGroup tipPointGroup;

    ArrayList<String> previewUrlList = new ArrayList<>();
    private AutoViewPagerAdapter<String> autoViewPagerAdapter;

    public ThemeFragment() {
    }

    @SuppressLint("ValidFragment")
    public ThemeFragment(ArrayList<String> previewUrlList) {
        this.previewUrlList = previewUrlList;
    }

    @Override
    public void initViewBefore() {

    }

    @Override
    public int getContentId() {
        return R.layout.fragment_theme;
    }

    @Override
    public void initView(View rootView) {
        //创建AutoViewPagerAdapter适配器
        autoViewPagerAdapter = new AutoViewPagerAdapter<>(mActivity, previewUrlList, new AutoViewPagerAdapter.OnAutoViewPagerItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String s) {
                Util.d(this, "onItemClick:    " + position);
            }
        });
        //初始化指示点布局
        tipPointGroup.init(autoViewPagerAdapter.getRealCount(), R.drawable.selector_banner_position_point, 10, 10);
        //设置适配器 并 传入指示点布局,如果用户不需要指示点布局，那么传入null即可（指示点布局传入前必须先进行初始化）
        autoViewPager.init(autoViewPagerAdapter, tipPointGroup, true);
    }


}

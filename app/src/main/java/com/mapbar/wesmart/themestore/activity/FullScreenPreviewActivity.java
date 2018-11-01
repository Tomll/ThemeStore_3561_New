package com.mapbar.wesmart.themestore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.widget.auto_scroll_viewpager.AutoViewPager;
import com.mapbar.wesmart.themestore.widget.auto_scroll_viewpager.AutoViewPagerAdapter;
import com.mapbar.wesmart.themestore.widget.auto_scroll_viewpager.TipPointGroup;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dongrp on 2018/10/16.
 */

public class FullScreenPreviewActivity extends AutoLayoutActivity {
    @BindView(R.id.autoViewPager)
    AutoViewPager autoViewPager;
    @BindView(R.id.tipPointGroup)
    TipPointGroup tipPointGroup;
    int position;
    ArrayList<String> previewUrlList = new ArrayList<>();
    private AutoViewPagerAdapter<String> autoViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_preview);
        ButterKnife.bind(this);//绑定初始化ButterKnife
        initData();
        initView();
    }

    //初始化数据
    public void initData() {
        Intent intent = getIntent();
        previewUrlList = intent.getStringArrayListExtra("previewUrlList");
        position = intent.getIntExtra("position", 0);
    }

    //初始化view及适配器
    public void initView() {
        fullScreen();
        //创建AutoViewPagerAdapter适配器
        autoViewPagerAdapter = new AutoViewPagerAdapter<>(this, previewUrlList, new AutoViewPagerAdapter.OnAutoViewPagerItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String s) {
                finish();//全屏预览时，单击图片，退出界面
            }
        });
        //初始化指示点布局
        tipPointGroup.init(autoViewPagerAdapter.getRealCount(), R.drawable.selector_theme_detail_position_point, 40, 3);
        //设置适配器 并 传入指示点布局,如果用户不需要指示点布局，那么传入null即可（指示点布局传入前必须先进行初始化）
        autoViewPager.init(autoViewPagerAdapter, tipPointGroup, true);
        autoViewPager.setCurrentItem(position);

    }

    //隐藏状态栏 底部导航栏,全屏显示
    public void fullScreen() {
        View mDecorView = getWindow().getDecorView().findViewById(android.R.id.content);
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


}

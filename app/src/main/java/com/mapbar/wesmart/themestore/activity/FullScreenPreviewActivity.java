package com.mapbar.wesmart.themestore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.bean.ThemeInfo;
import com.mapbar.wesmart.themestore.util.LogUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dongrp on 2018/10/16.
 */

public class FullScreenPreviewActivity extends AutoLayoutActivity {
    @BindView(R.id.fullScreenViewPager)
    ViewPager fullScreenViewPager;
    @BindView(R.id.position_point1)
    TextView positionPoint1;
    @BindView(R.id.position_point2)
    TextView positionPoint2;
    @BindView(R.id.position_point3)
    TextView positionPoint3;
    ArrayList<String> previewUrlList = new ArrayList<>();
    ArrayList<ImageView> previewImgList = new ArrayList<>();
    ThemeInfo themeInfo;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_preview);
        ButterKnife.bind(this);//绑定初始化ButterKnife
        LogUtil.d(this, "onCreate: --------------------------------");
        initData();
        initView();
    }

    //初始化数据
    public void initData() {
        //从传来的themeInfo 对象中得到预览图路径url
        Intent intent = getIntent();
        themeInfo = (ThemeInfo) intent.getSerializableExtra("themeInfo");
        position = intent.getIntExtra("position", 0);
        previewUrlList.add(themeInfo.getPreView().getWidgetUrl());
        previewUrlList.add(themeInfo.getPreView().getIconUrl());
        previewUrlList.add(themeInfo.getPreView().getLockScreenUrl());
        //初始化viewPager中的三张预览图，并填充数据
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(previewUrlList.get(i)).into(imageView);
            previewImgList.add(imageView);
        }
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

    //初始化view及适配器
    public void initView() {
        fullScreen();
        fullScreenViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.d(this, "onPageSelected: " + position);
                setPositionPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //viewPager设置PagerAdapter适配器
        fullScreenViewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return previewImgList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public View instantiateItem(ViewGroup container, int position) {
                container.addView(previewImgList.get(position));
                return previewImgList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(previewImgList.get(position));
            }
        });
        fullScreenViewPager.setCurrentItem(position);
        setPositionPoint(position);//详情界面默认从第0个预览图开始预览
    }

    //设置指示点位置
    public void setPositionPoint(int position) {
        switch (position) {
            case 0:
                positionPoint1.setSelected(true);
                positionPoint2.setSelected(false);
                positionPoint3.setSelected(false);
                break;
            case 1:
                positionPoint1.setSelected(false);
                positionPoint2.setSelected(true);
                positionPoint3.setSelected(false);
                break;
            case 2:
                positionPoint1.setSelected(false);
                positionPoint2.setSelected(false);
                positionPoint3.setSelected(true);
                break;

        }
    }

}

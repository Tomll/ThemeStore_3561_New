package com.mapbar.wesmart.themestore.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mapbar.wesmart.themestore.MyApplication;
import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.activity.FullScreenPreviewActivity;
import com.mapbar.wesmart.themestore.bean.ThemeInfo;
import com.mapbar.wesmart.themestore.util.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.mapbar.wesmart.themestore.R.id.theme_collection_checkbox;

/**
 * Created by dongrp on 2018/10/15.
 */

public class ThemeDetailFragment extends BaseFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.button_back)
    TextView buttonBack;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.theme_price)
    TextView themePrice;
    @BindView(R.id.theme_synopsis)
    TextView themeSynopsis;
    @BindView(R.id.theme_designer)
    TextView themeDesigner;
    @BindView(R.id.theme_download_count)
    TextView themeDownloadCount;
    @BindView(R.id.theme_publish_time)
    TextView themePublishTime;
    @BindView(R.id.position_point1)
    TextView positionPoint1;
    @BindView(R.id.position_point2)
    TextView positionPoint2;
    @BindView(R.id.position_point3)
    TextView positionPoint3;
    @BindView(R.id.theme_apply_button)
    Button themeApplyButton;
    @BindView(theme_collection_checkbox)
    CheckBox themeCollectionCheckbox;
    @BindView(R.id.progressFrameLayout)
    FrameLayout progressFrameLayout;

    ThemeApplySuccessReceiver themeApplySuccessReceiver;
    ArrayList<String> previewUrlList = new ArrayList<>();
    ArrayList<ImageView> previewImgList = new ArrayList<>();
    ThemeInfo themeInfo;
    boolean isLocalTheme;

    public ThemeDetailFragment() {
    }

    @SuppressLint("ValidFragment")
    public ThemeDetailFragment(ThemeInfo themeInfo, boolean isLocalTheme) {
        //从构造传来的themeInfo 对象中得到预览图路径url
        this.isLocalTheme = isLocalTheme;
        this.themeInfo = themeInfo;
        previewUrlList.add(themeInfo.getPreView().getWidgetUrl());
        previewUrlList.add(themeInfo.getPreView().getIconUrl());
        previewUrlList.add(themeInfo.getPreView().getLockScreenUrl());
    }

    @Override
    public void initViewBefore() {
        //初始化viewPager中的三张预览图，并填充数据
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mActivity).load(previewUrlList.get(i)).into(imageView);
            previewImgList.add(imageView);
        }
        //注册主题应用成功的广播接收器
        themeApplySuccessReceiver = new ThemeApplySuccessReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("autoai.intent.action.theme.ThemeResult");
        mActivity.registerReceiver(themeApplySuccessReceiver, intentFilter);
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_theme_detail;
    }

    @Override
    public void initView(View rootView) {
        title.setText(themeInfo.getThemeName());//标题设置为 主题名
        //viewPager设置页面变化监听器
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return previewImgList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public View instantiateItem(ViewGroup container, final int position) {
                //给item设置点击监听
                previewImgList.get(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), FullScreenPreviewActivity.class);
                        intent.putExtra("themeInfo", themeInfo);
                        intent.putExtra("position", viewPager.getCurrentItem());
                        getActivity().startActivity(intent);
                        LogUtil.d(this, "onClick: item " + position);
                    }
                });
                container.addView(previewImgList.get(position));
                return previewImgList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(previewImgList.get(position));
            }
        });
        setPositionPoint(0);//详情界面默认从第0个预览图开始预览

        //以下是主题描述信息适配
        if (themeInfo.getPrice() > 0) { //如果价格大于0,说明主题收费,设置具体的价格 (布局默认是免费的)
            themePrice.setText("¥ " + themeInfo.getPrice());//设置价格
        }
        themeSynopsis.setText(themeInfo.getIntro());//主题简介
        themeDesigner.setText("  设计师: " + themeInfo.getAuthor());//设计师
        themeDownloadCount.setText("  " + themeInfo.getDownloadCount() + "次          大小: " + themeInfo.getThemeSize() + "MB");//下载次数,大小
        themePublishTime.setText("  发布时间: " + themeInfo.getReleaseDate());//发布时间

        if (isLocalTheme) {//本地主题，通过读取记录文件显示收藏状态
            if (MyApplication.sp.getBoolean(themeInfo.getId() + "_collect", false)) {//设置是否收藏，（布局默认没收藏）
                themeCollectionCheckbox.setChecked(true);
            }
        } else {//网络主题，直接读取themeInfo.isCollected()值
            if (themeInfo.isCollected()) {//设置是否收藏，（布局默认没收藏）
                themeCollectionCheckbox.setChecked(true);
            }
        }
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

    @Override
    public void onPause() {
        super.onPause();
        progressFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //反注册广播
        mActivity.unregisterReceiver(themeApplySuccessReceiver);
    }

    //点击回调
    @OnClick({R.id.button_back, R.id.viewPager, R.id.theme_apply_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                mActivity.goBack();
                LogUtil.d(this, "goBack");
                break;
            case R.id.theme_apply_button://应用主题
                //发送换肤广播
                Intent intent = new Intent();
                intent.setAction("autoai.intent.action.theme.ThemeChange");
                intent.putExtra("themePath", themeInfo.getDownloadPath());
                mActivity.sendBroadcast(intent);
                progressFrameLayout.setVisibility(View.VISIBLE);
                LogUtil.d(this, "apply_theme : " + themeInfo.getDownloadPath());
                break;
        }
    }

    @OnCheckedChanged({R.id.theme_collection_checkbox})
    public void onCheckedChanged(CompoundButton view, boolean isChanged) {
        //onCheckedChanged会回掉 选中和取消选中的两个事件，我们只关注被选中的事件，所以加上isChange判断
        //只有收藏一个checkBox所以就不switch了
        if (isChanged) {
            MyApplication.editor.putBoolean(themeInfo.getId() + "_collect", true);
            themeCollectionCheckbox.setText("  取消收藏");
        } else {
            MyApplication.editor.putBoolean(themeInfo.getId() + "_collect", false);
            themeCollectionCheckbox.setText("  收藏");
        }
        //MyApplication.editor.commit();
    }


    /**
     * 主题适配成功广播接收器
     */
    class ThemeApplySuccessReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean themeResult = intent.getBooleanExtra("themeResult", false);
            LogUtil.d(this, "onReceive: " + intent.getAction() + "   themeResult = " + themeResult);
            if (themeResult) {
                //Toast.makeText(mActivity, R.string.theme_apply_success, Toast.LENGTH_LONG).show();
                //回到桌面
                Intent intent2 = new Intent(Intent.ACTION_MAIN);
                intent2.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent2);
            } else {
                Toast.makeText(mActivity, R.string.theme_apply_fail, Toast.LENGTH_LONG).show();
                progressFrameLayout.setVisibility(View.GONE);
            }
        }
    }

}

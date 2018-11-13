package com.mapbar.wesmart.themestore.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mapbar.wesmart.themestore.MyApplication;
import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.activity.FullScreenPreviewActivity;
import com.mapbar.wesmart.themestore.bean.ThemeInfo;
import com.mapbar.wesmart.themestore.util.Util;
import com.mapbar.wesmart.themestore.widget.auto_scroll_viewpager.AutoViewPager;
import com.mapbar.wesmart.themestore.widget.auto_scroll_viewpager.AutoViewPagerAdapter;
import com.mapbar.wesmart.themestore.widget.auto_scroll_viewpager.TipPointGroup;

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
    AutoViewPager autoViewPager;
    @BindView(R.id.tipPointGroup)
    TipPointGroup tipPointGroup;
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
    @BindView(R.id.theme_apply_button)
    Button themeApplyButton;
    @BindView(theme_collection_checkbox)
    CheckBox themeCollectionCheckbox;
    @BindView(R.id.progressFrameLayout)
    FrameLayout progressFrameLayout;

    ThemeInfo themeInfo;
    boolean isLocalTheme;
    ThemeApplySuccessReceiver themeApplySuccessReceiver;
    ArrayList<String> previewUrlList = new ArrayList<>();
    private AutoViewPagerAdapter<String> mAutoViewPagerAdapter;


    //通过setArguments的方法传递参数，避免配置项发生变化后，成员变量参数丢失
    public static ThemeDetailFragment newInstance(ThemeInfo themeInfo, boolean isLocalTheme) {
        ThemeDetailFragment themeDetailFragment = new ThemeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isLocalTheme", isLocalTheme);
        bundle.putSerializable("themeInfo", themeInfo);
        themeDetailFragment.setArguments(bundle);
        return themeDetailFragment;
    }

    @Override
    public void initViewBefore() {
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
        Bundle bundle = getArguments();
        isLocalTheme = bundle.getBoolean("isLocalTheme");
        themeInfo = (ThemeInfo) bundle.getSerializable("themeInfo");
        //预览图数据
        previewUrlList.add(themeInfo.getPreView().getWidgetUrl());
        previewUrlList.add(themeInfo.getPreView().getIconUrl());
        previewUrlList.add(themeInfo.getPreView().getLockScreenUrl());

        title.setText(themeInfo.getThemeName());//标题设置为 主题名
        //创建AutoViewPagerAdapter适配器
        mAutoViewPagerAdapter = new AutoViewPagerAdapter<>(mActivity, previewUrlList, new AutoViewPagerAdapter.OnAutoViewPagerItemClickListener<String>() {
            @Override
            public void onItemClick(int position, String s) {
                Intent intent = new Intent(getActivity(), FullScreenPreviewActivity.class);
                intent.putStringArrayListExtra("previewUrlList", previewUrlList);
                intent.putExtra("position", position);
                getActivity().startActivity(intent);
                Util.d(this, "onClick: item " + position);
            }
        });
        //初始化指示点布局,
        tipPointGroup.init(mAutoViewPagerAdapter.getRealCount(), R.drawable.selector_theme_detail_position_point, 30, 3);
        //设置适配器 并 传入指示点布局,如果用户不需要指示点布局，那么传入null即可（指示点布局传入前必须先进行初始化）
        autoViewPager.init(mAutoViewPagerAdapter, tipPointGroup, true);

        //以下是主题描述信息适配
        if (themeInfo.getPrice() > 0) { //如果价格大于0,说明主题收费,设置具体的价格 (布局默认是免费的)
            themePrice.setText("¥ " + themeInfo.getPrice());//设置价格
        }
        themeSynopsis.setText(themeInfo.getIntro());//主题简介
        //通配符%1$s %2$d %3$f含义：$s代表字符型参数  $d代表整型参数  $f代表浮点型参数, %1 %2 %3 代表第几个参数 ，$s
        themeDesigner.setText(String.format(getString(R.string.designer), themeInfo.getAuthor()));//设计者
        themeDownloadCount.setText(String.format(getString(R.string.downloads_and_size), themeInfo.getDownloadCount(), String.valueOf(themeInfo.getThemeSize())));//下载次数,大小
        themePublishTime.setText(String.format(getString(R.string.release_date), themeInfo.getReleaseDate()));//发布日期
        //搜藏状态
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
    @OnClick({R.id.button_back, R.id.theme_apply_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                mActivity.goBack();
                Util.d(this, "goBack");
                break;
            case R.id.theme_apply_button://应用主题
                //发送换肤广播
                Intent intent = new Intent();
                intent.setAction("autoai.intent.action.theme.ThemeChange");
                intent.putExtra("themePath", themeInfo.getDownloadPath());
                mActivity.sendBroadcast(intent);
                progressFrameLayout.setVisibility(View.VISIBLE);
                Util.d(this, "apply_theme : " + themeInfo.getDownloadPath());
                break;
        }
    }

    @OnCheckedChanged({R.id.theme_collection_checkbox})
    public void onCheckedChanged(CompoundButton view, boolean isChanged) {
        //onCheckedChanged会回掉 选中和取消选中的两个事件，我们只关注被选中的事件，所以加上isChange判断
        //只有收藏一个checkBox所以就不switch了
        if (isChanged) {
            MyApplication.editor.putBoolean(themeInfo.getId() + "_collect", true);
            themeCollectionCheckbox.setText(" " + getString(R.string.cancle) + getString(R.string.collect));
        } else {
            MyApplication.editor.putBoolean(themeInfo.getId() + "_collect", false);
            themeCollectionCheckbox.setText(getString(R.string.collect));
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
            Util.d(this, "onReceive: " + intent.getAction() + "   themeResult = " + themeResult);
            if (themeResult) {
                //Util.toastShort(mActivity, R.string.theme_apply_success);
                //回到桌面
                Intent intent2 = new Intent(Intent.ACTION_MAIN);
                intent2.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent2);
            } else {
                Util.toastShort(mActivity, R.string.theme_apply_fail);
                progressFrameLayout.setVisibility(View.GONE);
            }
        }
    }


}

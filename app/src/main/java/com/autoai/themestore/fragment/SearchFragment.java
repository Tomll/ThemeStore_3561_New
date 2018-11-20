package com.autoai.themestore.fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.autoai.themestore.MyApplication;
import com.autoai.themestore.util.Util;
import com.autoai.themestore.widget.FlowLayout;
import com.autoai.themestore.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongrp on 2018/9/30.
 */

public class SearchFragment extends BaseFragment {

    //搜索栏
    @BindView(R.id.edtTxt_search_content)
    EditText edtTxtSearchContent;
    //未搜索到xx内容
    @BindView(R.id.layoutNoContent)
    FrameLayout frameLayoutNoContent;
    @BindView(R.id.textViewNoContent)
    TextView textViewNoContent;
    //搜索历史
    @BindView(R.id.layoutSearchHistory)
    ScrollView layoutSearchHistory;
    @BindView(R.id.flow_layout_history)
    FlowLayout mFlowLayoutHistory;
    //搜索热门
    @BindView(R.id.flow_layout_hot)
    FlowLayout mFlowLayoutHot;


    public static final int SEARCH_HISTORY_CONTAINER_SIZE = 10;//记录搜索历史总条数
    public static final String SEARCH_HISTORY_KEYWORD = "search_history_keyword";
    private List<String> mHistoryKeywords = new ArrayList<>();//搜索历史集合
    private String[] mHotSearchKeywords = new String[]{"苹果主题", "小米主题", "华为主题", "中国风"};//搜索热门关键词，正式环境下应该是通过网络请求的数据

    @Override
    public void initViewBefore() {
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_search;
    }

    @Override
    public void initView(View rootView) {
        //给ediText设置action监听
        edtTxtSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//通过软键盘的“完成/搜索”按钮，ediText传来搜索Action
                    String searchText = edtTxtSearchContent.getText().toString();
                    //输入检查
                    if (!inputCheck(searchText))
                        return false;
                    search(searchText);
                }
                return false;
            }
        });
        //初始化搜索历史布局
        initSearchHistoryFlowLayout();
        //初始化搜索热门布局
        initSearchHotFlowLayout();
    }


    //初始化搜索历史布局
    private void initSearchHistoryFlowLayout() {
        //所有历史记录存储在一条key = SEARCH_HISTORY_KEYWORD 的字符串中，通过“&”分隔开来
        String history = MyApplication.sp.getString(SEARCH_HISTORY_KEYWORD, "");
        if (!TextUtils.isEmpty(history)) {
            mHistoryKeywords.clear();
            for (Object o : history.split("&")) {
                mHistoryKeywords.add((String) o);
            }
        }
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        for (int i = 0; i < mHistoryKeywords.size(); i++) {
            TextView tv = (TextView) layoutInflater.inflate(R.layout.search_history_label_tv, mFlowLayoutHistory, false);
            final String str = mHistoryKeywords.get(i);
            tv.setText(str);
            //点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search(str);
                }
            });
            mFlowLayoutHistory.addView(tv);
        }
    }

    //初始化搜索热门布局
    private void initSearchHotFlowLayout() {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        for (int i = 0; i < mHotSearchKeywords.length; i++) {
            TextView tv = (TextView) layoutInflater.inflate(R.layout.search_hot_label_tv, mFlowLayoutHot, false);
            final String str = mHotSearchKeywords[i];
            tv.setText(str);
            //点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search(str);
                }
            });
            mFlowLayoutHot.addView(tv);
        }
    }

    //输入检查
    public boolean inputCheck(String searchText) {
        if (TextUtils.isEmpty(searchText) || searchText.trim().length() == 0) {
            Util.toastShort(mActivity, R.string.can_not_emptyinput);
            return false;
        }
        return true;
    }

    //搜索
    public void search(String searchText) {
        saveSearchHistory(searchText);
        layoutSearchHistory.setVisibility(View.GONE);
        frameLayoutNoContent.setVisibility(View.VISIBLE);
        edtTxtSearchContent.setText(searchText);
        textViewNoContent.setText(String.format(mActivity.getResources().getString(R.string.no_search_content), searchText));
    }

    //取消搜索
    public void cancelSearch() {
        edtTxtSearchContent.setText("");
        textViewNoContent.setText("");
        frameLayoutNoContent.setVisibility(View.GONE);
        layoutSearchHistory.setVisibility(View.VISIBLE);
    }

    //储存一条搜索历史
    public void saveSearchHistory(final String text) {
        String oldText = MyApplication.sp.getString(SEARCH_HISTORY_KEYWORD, "");
        //Util.d(this, "saveSearchHistory:  \n text: " + text + "\n oldText:" + oldText + "\n ldText.contains(text):" + oldText.contains(text));
        if (!(oldText.contains(text))) {
            if (mHistoryKeywords.size() >= SEARCH_HISTORY_CONTAINER_SIZE) {
                oldText = oldText.substring(0, oldText.lastIndexOf("&"));
                mHistoryKeywords.remove(mHistoryKeywords.size() - 1);
                mFlowLayoutHistory.removeViewAt(mFlowLayoutHistory.getChildCount() - 1);
            }
            //sp存储搜索历史
            MyApplication.editor.putString(SEARCH_HISTORY_KEYWORD, text + "&" + oldText).commit();
            //添加搜索历史到 数据集合中
            mHistoryKeywords.add(text);
            //刷新mFlowLayout
            TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.search_history_label_tv, mFlowLayoutHistory, false);
            tv.setText(text);
            //点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search(text);
                }
            });
            mFlowLayoutHistory.addView(tv, 0);
        }
    }

    //清空所有搜索历史
    public void cleanSearchHistory() {
        MyApplication.editor.remove(SEARCH_HISTORY_KEYWORD).commit();//清除sp中的所有历史数据
        mHistoryKeywords.clear();//清除数据集合中的数据
        mFlowLayoutHistory.removeAllViews();//刷新mFlowLayout
    }

    @OnClick({R.id.layout_search, R.id.button_clear, R.id.clear_search_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_search://放大镜按钮
                String searchText = edtTxtSearchContent.getText().toString();
                //输入检查
                if (!inputCheck(searchText))
                    return;
                search(searchText);
                break;
            case R.id.button_clear://取消按钮
                cancelSearch();
                break;
            case R.id.clear_search_history://清除搜索历史记录
                cleanSearchHistory();
                break;
        }
    }

}

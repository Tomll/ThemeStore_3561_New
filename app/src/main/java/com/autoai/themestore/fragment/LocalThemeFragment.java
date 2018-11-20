package com.autoai.themestore.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.autoai.themestore.MyApplication;
import com.autoai.themestore.bean.ThemeInfo;
import com.autoai.themestore.util.Util;
import com.autoai.themestore.widget.theme_recycle_view.ThemeRecycleView;
import com.autoai.themestore.widget.theme_recycle_view.ThemeRecycleViewAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.autoai.themestore.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.autoai.themestore.R.id.recyclerView;

/**
 * Created by dongrp on 2018/9/30.
 */

public class LocalThemeFragment extends BaseFragment {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.button_back)
    TextView buttonBack;
    @BindView(recyclerView)
    ThemeRecycleView themeRecycleView;
    ThemeRecycleViewAdapter themeRecycleViewAdapter;
    ArrayList<ThemeInfo> themeList = new ArrayList<>();

    @Override
    public void initViewBefore() {
        //初始化数据
        String localThemesJson = "system/etc/themes/themes.json";//系统预置主题路径
        //String localThemesJson = "/storage/emulated/0/internalDisk/themes/themes.json";//本地主题路径
        String themesJson = getLocalThemesJson(localThemesJson);
        //读数据
        ArrayList<ThemeInfo> themeList1 = parseHaveHeaderJsonArray(themesJson);
        themeList.clear();
        themeList.addAll(themeList1);
        Util.d(this, "initViewBefore  themeList.size(): " + themeList.size());
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_local_theme;
    }

    @Override
    public void initView(View rootView) {
        title.setText(R.string.local_theme);
        buttonBack.setVisibility(View.GONE);// TODO: 2018/10/16
        //初始化themeRecycleView，并设置适配器themeRecycleViewAdapter
        themeRecycleViewAdapter = new ThemeRecycleViewAdapter(mActivity, themeList);
        themeRecycleView.init(themeRecycleViewAdapter, new ThemeRecycleViewAdapter.RecycleView_OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Util.d(this, "click item " + position);
                ThemeDetailFragment themeDetailFragment = ThemeDetailFragment.newInstance(themeList.get(position), true);
                addFragment(themeDetailFragment);
            }
        }, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        String systemUsingThemeID = ThemeRecycleViewAdapter.getSystemProperty("persist.sys.current_theme");
        if (!MyApplication.sp.getString("usingThemeID", "").equals(systemUsingThemeID)) {
            MyApplication.editor.putString("usingThemeID", systemUsingThemeID).commit();
            themeRecycleViewAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.button_back/*, R.id.delet*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                mActivity.goBack();
                Util.d(this, "goBack");
                break;
            /*case R.id.delet:
                //1本地文件删除
                *//*for (ThemeInfo themeInfo : themeRecycleViewAdapter.getSelectedData()) {
                    themeList.remove(themeInfo); //2类中的临时数据集合删除
                }*//*
                themeRecycleViewAdapter.removeSelectedData(); //3适配器中数据删除并刷新
                break;*/
        }
    }


    //读取本地目录下的主题json文件,输出json字符串
    public static String getLocalThemesJson(String filePath) {
        String str;
        File file = new File(filePath);
        try {
            FileInputStream in = new FileInputStream(file);
            // size 为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            str = new String(buffer, "utf-8");
        } catch (IOException e) {
            return null;
        }
        return str;

    }

    //解析有头部标签的 jsonArray
    private ArrayList<ThemeInfo> parseHaveHeaderJsonArray(String jsonStr) {
        Gson gson = new Gson();
        ArrayList<ThemeInfo> themeInfoList = new ArrayList<>();//容器集合
        //先转JsonObject
        JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
        //再转JsonArray 加上数据头"themes"
        JsonArray jsonArray = jsonObject.getAsJsonArray("themes");
        //循环遍历
        for (JsonElement theme : jsonArray) {
            ThemeInfo ThemeInfo = gson.fromJson(theme, ThemeInfo.class);
            themeInfoList.add(ThemeInfo);
        }
        return themeInfoList;
    }


}

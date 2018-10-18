package com.mapbar.wesmart.themestore.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.adapter.ThemeRecycleAdapter;
import com.mapbar.wesmart.themestore.bean.ThemeInfo;
import com.mapbar.wesmart.themestore.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongrp on 2018/9/30.
 */

public class LocalThemeFragment extends BaseFragment implements ThemeRecycleAdapter.RecycleView_OnItemClickListener,
        ThemeRecycleAdapter.RecycleView_OnItemLongClickListener, ThemeRecycleAdapter.RecycleView_OnItemCheckedChangedListener {

    @BindView(R.id.button_back)
    TextView buttonBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<ThemeInfo> themeList = new ArrayList<>();
    ThemeRecycleAdapter themeRecycleAdapter;

    @Override
    public void initViewBefore() {
        //初始化数据
        //String themesJson = getAssetJson("themes.json", mActivity);
        String localThemesJson = "/storage/emulated/0/internalDisk/themes/themes.json";//本地主题路径
        String themesJson = getLocalThemesJson(localThemesJson);
        //读了4遍数据
        ArrayList<ThemeInfo> themeList1 = parseHaveHeaderJsonArray(themesJson);
        themeList.clear();
        themeList.addAll(themeList1);
        themeList.addAll(themeList1);
        themeList.addAll(themeList1);
        themeList.addAll(themeList1);
        themeList.addAll(themeList1);
        themeList.addAll(themeList1);
        LogUtil.d(this, "initViewBefore  themeList.size(): " + themeList.size());
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_local_theme;
    }

    @Override
    public void initView(View rootView) {
        title.setText(R.string.local_theme);
        buttonBack.setVisibility(View.GONE);// TODO: 2018/10/16
        themeRecycleAdapter = new ThemeRecycleAdapter(mActivity, themeList);//初始化adapter
        // LinearLayoutManager layoutManager = new LinearLayoutManager(this);// 创建recyclerView的布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 3);// 创建布局管理器，设置为3列的布局
        recyclerView.setLayoutManager(gridLayoutManager);// 给recyclerView设置布局管理器
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置增加或删除条目的动画
        recyclerView.addItemDecoration(new SpaceItemDecoration(5));// 设置item的间距(需自定义：SpacesItemDecoration类)
        // 设置点击监听（需自定义：在MyRecycleViewAdapter中实现）
        themeRecycleAdapter.setOnItemClickListener(this);//设置item单击监听
        themeRecycleAdapter.setOnItemLongClickListener(this);//设置item长按监听
        themeRecycleAdapter.setOnItemCheckedChangedListener(this);//设置item的选中监听
        recyclerView.setAdapter(themeRecycleAdapter);//给recycleView设置adapter
    }


    @OnClick({R.id.button_back, R.id.title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                mActivity.goBack();
                LogUtil.d(this, "goBack");
                break;
        }
    }


    //ThemeRecycleAdapter 中的 item 的click回调
    @Override
    public void onItemClick(View view, int position) {
        LogUtil.d(this, "click item " + position);
        addFragment(new ThemeDetailFragment(themeList.get(position)));
    }

    //ThemeRecycleAdapter 中的 item 的longClick回调
    @Override
    public void onItemLongClick(View view, int position) {
        LogUtil.d(this, "long click " + position);
        //themeRecycleAdapter.switchEditMode(true);
    }

    @Override
    public void onItemCheckedChanged(View view, int position, boolean isChecked) {
        LogUtil.d(this, position + "  isCheck = " + isChecked);

    }


    //RecyclerView的item间距类
    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.top = mSpace;
            outRect.bottom = mSpace;
            /*if (parent.getChildAdapterPosition(view) == 0) {
                outRect.left = 0;
            }*/
            /*if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
            }*/
        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
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

    //读取asset目录下的json文件,输出json字符串
    public static String getAssetJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
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
            //通过反射 得到ThemeInfo.class
            //LogUtil.d(this, "parseHaveHeaderJsonArray:   " + theme);
            ThemeInfo ThemeInfo = gson.fromJson(theme, ThemeInfo.class);
            themeInfoList.add(ThemeInfo);
        }
        return themeInfoList;
    }


}

package com.mapbar.wesmart.themestore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.bean.ThemeInfo;
import com.mapbar.wesmart.themestore.util.LogUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by dongrp on 2018/10/12.
 */

public class ThemeRecycleAdapter extends RecyclerView.Adapter<ThemeRecycleAdapter.MyViewHolder> {

    private Context mContext;
    private List<ThemeInfo> themeList;
    private RecycleView_OnItemClickListener itemClickListener;
    private RecycleView_OnItemLongClickListener itemLongClickListener;
    private RecycleView_OnItemCheckedChangedListener itemCheckedChangedListener;
    private boolean isShowCheckBox = false;

    // 构造
    public ThemeRecycleAdapter(Context context, List<ThemeInfo> themeList) {
        this.mContext = context;
        this.themeList = themeList;
    }

    // 继承RecyclerView.Adapter需要重写的4个方法
    // 1、设置item的条数
    @Override
    public int getItemCount() {
        return themeList.size();
    }

    // 4、填充数据到onCreateViewHolder方法返回的holder中的控件上
    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ThemeInfo themeInfo = themeList.get(position);//获取position位置对应的themeInfo对象
        //设置预览图
        Glide.with(mContext).load(themeInfo.getPreView().getWidgetUrl()).into(holder.iv_theme_preview);
        //设置预览图方法二
        /*try {
            Glide.with(mContext).load(themeInfo.getPreView().getWidgetUrl()).placeholder(R.mipmap).into(holder.iv_theme_preview);
            InputStream ims = mContext.getAssets().open(themeInfo.getPreView().getWidgetUrl());// get input stream
            Drawable d = Drawable.createFromStream(ims, null);// load image as Drawable
            holder.iv_theme_preview.setImageDrawable(d);// set image to ImageView
        } catch (IOException ex) {
            return;
        }*/
        //设置“使用中”角标
        String usingThemeID = getSystemProperty("persist.sys.current_theme");//获取系统当前使用的主题的ID
        LogUtil.d(this, "onBindViewHolder: usingThemeID = " + usingThemeID);
        if (null == usingThemeID || "".equals(usingThemeID)) {
            usingThemeID = "101";//默认使用 themeID = 101的主题
        }
        if (null != usingThemeID && String.valueOf(themeInfo.getId()).equals(usingThemeID)) {
            holder.iv_using.setVisibility(View.VISIBLE);
        } else {
            holder.iv_using.setVisibility(View.GONE);
        }
        holder.tv_theme_name.setText(themeInfo.getThemeName());//设置主题名称
        //如果价格大于0,那么是收费主题，就设置具体价格和左侧收费图标（布局中默认是免费的）
        if (themeInfo.getPrice() > 0) {
            holder.tv_theme_price.setText(" " + themeInfo.getPrice());//设置价格
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.no_free);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getMinimumHeight());//必须设置bounds，否则drawable不显示
            holder.tv_theme_price.setCompoundDrawables(drawable, null, null, null);//设置收费图标
        }
        holder.tv_theme_downloads.setText(themeInfo.getDownloadCount() + "次下载");//设置下载量
        if (isShowCheckBox) {
            if (position == 0 || position == 1 || position == 2) { //前三个系统默认主题不能被编辑
                holder.checkBoxContainer.setVisibility(View.GONE);
            } else {
                holder.checkBoxContainer.setVisibility(View.VISIBLE);
            }
        } else {
            holder.checkBoxContainer.setVisibility(View.GONE);
        }
    }

    // 3、返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_theme, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    // 2、自定义的MyViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView iv_theme_preview, iv_using;
        TextView tv_theme_name, tv_theme_downloads, tv_theme_price;
        FrameLayout checkBoxContainer;
        CheckBox checkBoxEdit;

        public MyViewHolder(View view) {
            super(view);
            AutoUtils.autoSize(view);//AutoLayout在RecycleView中的使用配置
            iv_theme_preview = (ImageView) view.findViewById(R.id.iv_theme_preview);
            iv_using = (ImageView) view.findViewById(R.id.iv_using);
            tv_theme_name = (TextView) view.findViewById(R.id.tv_theme_name);
            tv_theme_downloads = (TextView) view.findViewById(R.id.tv_theme_downloads);
            tv_theme_price = (TextView) view.findViewById(R.id.tv_them_price);
            checkBoxContainer = (FrameLayout) view.findViewById(R.id.checkbox_container);
            checkBoxEdit = (CheckBox) view.findViewById(R.id.checkbox_edit);
            // 注册点击监听
            iv_theme_preview.setOnClickListener(this);
            iv_theme_preview.setOnLongClickListener(this);
            checkBoxEdit.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (null != itemCheckedChangedListener) {
                itemCheckedChangedListener.onItemCheckedChanged(buttonView, getPosition(), isChecked);
            }
        }
    }

    /**
     * 该方法切换编辑模式开关：显示、隐藏 编辑模式下的CheckBox
     * isShowCheckBox : true 打开，false:关闭
     */
    public void switchEditMode(boolean isShowCheckBox) {
        this.isShowCheckBox = isShowCheckBox;
        notifyDataSetChanged();
    }


    /**
     * 定义setOnItemClickListener的方法，供外部使用
     */
    public void setOnItemClickListener(RecycleView_OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 定义setOnItemLongClickListener的方法，供外部使用
     */
    public void setOnItemLongClickListener(RecycleView_OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void setOnItemCheckedChangedListener(RecycleView_OnItemCheckedChangedListener itemCheckedChangedListener) {
        this.itemCheckedChangedListener = itemCheckedChangedListener;
    }


    /**
     * 自定义RecycleView的OnItemClick接口
     */
    public interface RecycleView_OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 自定义RecycleView的OnItemLongClick接口
     */
    public interface RecycleView_OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    /**
     * 自定义RecycleView的OnItemCheckedChanged接口
     */
    public interface RecycleView_OnItemCheckedChangedListener {
        void onItemCheckedChanged(View view, int position, boolean isChecked);
    }


    public static String getSystemProperty(String key) {
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, key);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }


}
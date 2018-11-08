package com.mapbar.wesmart.themestore.widget.theme_recycle_view;

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
import com.mapbar.wesmart.themestore.util.Util;
import com.zhy.autolayout.utils.AutoUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dongrp on 2018/10/12.
 */

public class ThemeRecycleViewAdapter extends RecyclerView.Adapter<ThemeRecycleViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ThemeInfo> themeInfoList;
    private RecycleView_OnItemClickListener itemClickListener;
    private RecycleView_OnItemLongClickListener itemLongClickListener;
    private RecycleView_OnItemCheckedChangedListener itemCheckedChangedListener;
    private ArrayList<ThemeInfo> selectedThemeList = new ArrayList<>();
    private boolean isShowCheckBox = false;
    private ThemeRecycleView mView;

    // 构造
    public ThemeRecycleViewAdapter(Context context, List<ThemeInfo> themeInfoList) {
        this.mContext = context;
        this.themeInfoList = themeInfoList;
    }

    //初始化方法
    public void init(ThemeRecycleView themeRecycleView,
                     RecycleView_OnItemClickListener recycleViewOnItemClickListener,
                     RecycleView_OnItemCheckedChangedListener recycleViewOnItemCheckedChangedListener,
                     RecycleView_OnItemLongClickListener recycleView_onItemLongClickListener) {
        mView = themeRecycleView;
        if (null != recycleViewOnItemClickListener)
            setOnItemClickListener(recycleViewOnItemClickListener);
        if (null != recycleViewOnItemCheckedChangedListener)
            setOnItemCheckedChangedListener(recycleViewOnItemCheckedChangedListener);
        if (null != recycleView_onItemLongClickListener)
            setOnItemLongClickListener(recycleView_onItemLongClickListener);
        mView.setAdapter(ThemeRecycleViewAdapter.this);
    }

    //刷新数据方法，传入数据集合
    public void notifyDataSetChanged(List<ThemeInfo> themeInfoList) {
        this.themeInfoList = themeInfoList;
        notifyDataSetChanged();
    }

    //该方法打开/关闭 编辑模式：显示、隐藏 编辑模式下的CheckBox。参数 isShowCheckBox : true 打开，false:关闭
    public void switchEditMode(boolean isShowCheckBox) {
        this.isShowCheckBox = isShowCheckBox;
        notifyDataSetChanged();
    }

    //获取选中数据集合
    public ArrayList<ThemeInfo> getSelectedData() {
        return selectedThemeList;
    }

    //删除选中的数据的方法
    public void removeSelectedData() {
        Util.d(this, "selectedThemeListSize : " + selectedThemeList.size());
        for (ThemeInfo themeInfo : selectedThemeList) {
            this.themeInfoList.remove(themeInfo);
        }
        selectedThemeList.clear();
        isShowCheckBox = false;
        notifyDataSetChanged();
    }


    // 继承RecyclerView.Adapter需要重写的4个方法
    // 1、设置item的条数
    @Override
    public int getItemCount() {
        return themeInfoList.size();
    }

    // 4、填充数据到onCreateViewHolder方法返回的holder中的控件上
    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ThemeInfo themeInfo = themeInfoList.get(position);//获取position位置对应的themeInfo对象
        //设置预览图
        Glide.with(mContext).load(themeInfo.getPreView().getWidgetUrl()).into(holder.iv_theme_preview);
        //设置“使用中”角标
        String usingThemeID = getSystemProperty("persist.sys.current_theme");//获取系统当前使用的主题的ID
        Util.d(this, "onBindViewHolder: usingThemeID = " + usingThemeID);
        if (null == usingThemeID || "".equals(usingThemeID)) {
            usingThemeID = "101";//默认使用 themeID = 101的主题
        }
        if (null != usingThemeID && String.valueOf(themeInfo.getId()).equals(usingThemeID)) {
            holder.iv_using.setVisibility(View.VISIBLE);
        } else {
            holder.iv_using.setVisibility(View.GONE);
        }
        //设置主题名称
        holder.tv_theme_name.setText(themeInfo.getThemeName());
        //如果价格大于0,那么是收费主题，就设置具体价格和左侧收费图标（布局中默认是免费的）
        if (themeInfo.getPrice() > 0) {
            holder.tv_theme_price.setText(" " + themeInfo.getPrice());//设置价格
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.no_free);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getMinimumHeight());//必须设置bounds，否则drawable不显示
            holder.tv_theme_price.setCompoundDrawables(drawable, null, null, null);//设置收费图标
        }
        //设置下载量
        holder.tv_theme_downloads.setText(themeInfo.getDownloadCount() + "次下载");
        //设置编辑模式，是否显示checkBoxContainer
        if (isShowCheckBox) {
            if (position == 0 || position == 1 || position == 2 || String.valueOf(themeInfo.getId()).equals(usingThemeID)) { //前三个系统默认主题，以及使用中的主题不能被编辑
                holder.checkBoxContainer.setVisibility(View.GONE);
            } else {
                holder.checkBoxContainer.setVisibility(View.VISIBLE);
            }
        } else {
            holder.checkBoxContainer.setVisibility(View.GONE);
        }
        holder.checkBoxEdit.setChecked(false);//每次onBindViewHolder都将checkBoxEdit恢复到默认的false状态
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
            checkBoxContainer.setOnClickListener(this);
            checkBoxEdit.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_theme_preview:
                    int position = getPosition();
                    if (null != itemClickListener && !isShowCheckBox) {
                        itemClickListener.onItemClick(v, position);
                    } else if (isShowCheckBox && (position == 0 || position == 1 || position == 2)) {
                        Util.toastShort(mContext, R.string.can_not_delete_preset_theme);
                    } else if (isShowCheckBox && getSystemProperty("persist.sys.current_theme").equals(String.valueOf(themeInfoList.get(position).getId()))) {
                        Util.toastShort(mContext, R.string.can_not_delete_using_theme);
                    }
                    break;
                case R.id.checkbox_container:
                    checkBoxEdit.toggle();
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != itemLongClickListener) {
                itemLongClickListener.onItemLongClick(v, getPosition());
            }
            //switchEditMode(true);目前不通过长按的方式触发编辑模式
            return true;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (null != itemCheckedChangedListener) {
                itemCheckedChangedListener.onItemCheckedChanged(buttonView, getPosition(), isChecked);
            }
            ThemeInfo themeInfo = themeInfoList.get(getPosition());
            if (isChecked && !selectedThemeList.contains(themeInfo)) {
                selectedThemeList.add(themeInfo);
            } else if (!isChecked && selectedThemeList.contains(themeInfo)) {
                selectedThemeList.remove(themeInfo);
            }
        }
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
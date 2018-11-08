package com.mapbar.wesmart.themestore.widget.theme_recycle_view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dongrp on 2018/11/7.
 * 自定义的ThemeRecycleView，因为项目中多处 使用同一个主题列表，所以同样的代码提取出来做一个自定义View,以提高代码复用性
 */

public class ThemeRecycleView extends RecyclerView {
    private Context context;

    public ThemeRecycleView(Context context) {
        super(context);
        this.context = context;
    }

    public ThemeRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * ThemeRecycleView 初始化方法
     *
     * @param themeRecycleViewAdapter：适配器
     * @param recycleViewOnItemClickListener：单击监听器
     * @param recycleViewOnItemCheckedChangedListener：checkChange监听器
     * @param recycleViewOnItemLongClickListener：长按监听器
     */
    public void init(ThemeRecycleViewAdapter themeRecycleViewAdapter,
                     ThemeRecycleViewAdapter.RecycleView_OnItemClickListener recycleViewOnItemClickListener,
                     ThemeRecycleViewAdapter.RecycleView_OnItemCheckedChangedListener recycleViewOnItemCheckedChangedListener,
                     ThemeRecycleViewAdapter.RecycleView_OnItemLongClickListener recycleViewOnItemLongClickListener) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);// 创建布局管理器，设置为3列的布局
        ThemeRecycleView.this.setLayoutManager(gridLayoutManager);// 给recyclerView设置布局管理器
        ThemeRecycleView.this.setItemAnimator(new DefaultItemAnimator());// 设置增加或删除条目的动画
        ThemeRecycleView.this.addItemDecoration(new SpaceItemDecoration(5));// 设置item的间距(需自定义：SpacesItemDecoration类)
        //themeRecycleAdapter进行初始化
        themeRecycleViewAdapter.init(ThemeRecycleView.this, recycleViewOnItemClickListener, recycleViewOnItemCheckedChangedListener, recycleViewOnItemLongClickListener);
    }


    //内部类：用于设置RecyclerView的item间距
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

}

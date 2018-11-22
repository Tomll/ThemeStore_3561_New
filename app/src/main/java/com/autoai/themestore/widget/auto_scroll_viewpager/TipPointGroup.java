package com.autoai.themestore.widget.auto_scroll_viewpager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by dongrp on 2018/10/31.
 * 这个自定义布局：用作viewpager下方的页面指示点。当然也可以用到别的需要指示点的布局中去，该布局可以自定义指示点样式。
 */

public class TipPointGroup extends LinearLayout {
    private Context context;
    private ArrayList<ImageView> pointList = new ArrayList<>();

    public TipPointGroup(Context context) {
        super(context);
        this.context = context;
    }

    public TipPointGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * 初始化TipPointGroup：根据传入的size值，创建相应数量的pointImage 并添加到TipPointGroup中
     *
     * @param size：指示点的总数量
     * @param pointImageDrawableID：指示点的backgroundDrawableID，用户需要根据自己项目实际需求定义drawable selector
     * @param width：pointImage的宽度
     * @param height：pointImage的高度
     */
    public void init(int size, int pointImageDrawableID, int width, int height) {
        for (int i = 0; i < size; i++) {
            ImageView pointImage = new ImageView(context);
            LayoutParams layoutParams = new LayoutParams(width, height);
            layoutParams.setMarginEnd(10);
            pointImage.setLayoutParams(layoutParams);
            pointImage.setBackground(context.getDrawable(pointImageDrawableID));
            //添加pointImage
            pointList.add(pointImage);
            addView(pointImage);
        }
    }

    /**
     * 设置选中的指示点
     *
     * @param position：选中的指示点的下标
     */
    public void setSelectedPoint(int position) {
        for (int i = 0; i < pointList.size(); i++) {
            ImageView imageView = pointList.get(i);
            if (i == position) {
                imageView.setSelected(true);
            } else {
                imageView.setSelected(false);
            }
        }
    }


}

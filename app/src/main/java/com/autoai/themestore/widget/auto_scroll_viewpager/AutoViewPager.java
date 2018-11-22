package com.autoai.themestore.widget.auto_scroll_viewpager;

import android.content.Context;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dongrp on 2016/10/10.
 * 自定义ViewPager控件,传入图片数据集合即可自动适配，并可定制化添加指示点布局，还可选择是否自动循环轮播
 */

public class AutoViewPager extends ViewPager {

    private AutoHandler mHandler = new AutoHandler();
    private TipPointGroup tipPointGroup;
    private boolean autoScroll;//是否自动滚动
    private int currentItem;
    private Timer mTimer;

    public AutoViewPager(Context context) {
        super(context);
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 初始化AutoViewPager：传入适配器，以及指示点布局
     *
     * @param adapter：AutoViewPagerAdapter适配器
     * @param tipPointGroup：指示点组布局，如果不需要指示点布局，那么传入null即可（指示点布局传入前必须先进行初始化）
     * @param autoScroll：设置viewpager是否自动循环播放
     */
    public void init(AutoViewPagerAdapter adapter, TipPointGroup tipPointGroup, boolean autoScroll) {
        this.autoScroll = autoScroll;
        //初始化指示点布局
        if (null != tipPointGroup) {
            this.tipPointGroup = tipPointGroup;
        }
        //创建viewpager切换速度控制器
        //FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(getContext(), new AccelerateInterpolator());
        //调用适配器初始化方法
        adapter.init(AutoViewPager.this, null);
    }

    public void onPageSelected(int position) {
        if (null != tipPointGroup) {
            tipPointGroup.setSelectedPoint(position);
        }
    }


    /**
     * 开始定时循环播放
     */
    public void start() {
        if (!autoScroll) {
            return;
        }
        //先停止
        stop();
        if (mTimer == null) {
            mTimer = new Timer();
        }
        //启动循环播放定时任务
        mTimer.schedule(new AutoScrollTask(), 5000, 5000);

    }

    /**
     * 停止定时循环播放
     */
    public void stop() {
        //先取消定时器
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    //拦截Touch事件，并进行相应的处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //onTouchEvent中无法拦截到ACTION_DOWN的动作
                stop();
                break;
            case MotionEvent.ACTION_UP:
                start();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //左右滑动viewpager的时候onInterceptTouchEvent检测不到up事件，onTouchEvent可以，所以添加此逻辑
            case MotionEvent.ACTION_UP:
                start();
                break;
        }
        return super.onTouchEvent(ev);
    }

    //完成定时循环播放任务的TimerTask类
    private class AutoScrollTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    currentItem = getCurrentItem();
                    if (currentItem == getAdapter().getCount() - 1) {
                        currentItem = 0;
                    } else {
                        currentItem++;
                    }
                    setCurrentItem(currentItem);
                }
            });
        }
    }

    private final static class AutoHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }


    //此类用于控制viewpager的切换速度
    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 1000;//默认切换时间ms

        public FixedSpeedScroller(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }

}
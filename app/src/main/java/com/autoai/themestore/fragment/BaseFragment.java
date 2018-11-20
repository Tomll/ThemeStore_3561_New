package com.autoai.themestore.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autoai.themestore.util.Util;
import com.autoai.themestore.R;
import com.autoai.themestore.activity.MainActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.autoai.themestore.activity.MainActivity.fragmentManager;
import static com.autoai.themestore.activity.MainActivity.fragmentTransaction;

/**
 * Created by dongrp on 2018/9/30.
 */

public abstract class BaseFragment extends Fragment {

    public MainActivity mActivity;
    private Unbinder bind;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBefore();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getContentId(), container, false);
        bind = ButterKnife.bind(this, rootView);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            bind.unbind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //此方法向MainActivity中的contentLayout布局中添加Fragment,并将添加的fragment加入回退栈
    public void addFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);//设置系统预置的Fragment的切换动画
        fragmentTransaction.add(R.id.contentLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Util.d(this, "add " + fragment.getClass().getSimpleName());
    }


    //以下的抽象方法，具体由子类实现。通过抽象和继承的方法 来规定基础代码的通用逻辑结构，简化子类的代码逻辑和结构
    //初始化view之前，进行的一些操作
    public abstract void initViewBefore();

    //由子类来填写布局id,父类根据id来inflate布局
    public abstract int getContentId();

    //由子类实现初始化逻辑
    public abstract void initView(View rootView);

}

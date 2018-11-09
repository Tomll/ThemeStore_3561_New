package com.mapbar.wesmart.themestore.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mapbar.wesmart.themestore.R;
import com.mapbar.wesmart.themestore.fragment.LocalThemeFragment;
import com.mapbar.wesmart.themestore.fragment.MineFragment;
import com.mapbar.wesmart.themestore.fragment.SearchFragment;
import com.mapbar.wesmart.themestore.util.Util;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class MainActivity extends AutoLayoutActivity /*implements BaseFragment.BackHandledInterface*/ {
    @BindView(R.id.contentLayout)
    FrameLayout contentLayout;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;

    public static FragmentTransaction fragmentTransaction;
    public static FragmentManager fragmentManager;
    //ThemeFragment themeFragment; // TODO: 2018/10/16
    LocalThemeFragment localThemeFragment;
    MineFragment mineFragment;
    SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 125);
        } else {
            fragmentManager = getFragmentManager();
            //themeFragment = new ThemeFragment();//"网络主题" // TODO: 2018/10/16
            localThemeFragment = new LocalThemeFragment();//"一期先使用本地主题"
            mineFragment = new MineFragment();//"我的"
            searchFragment = new SearchFragment();//"搜索"
            radioButton1.setChecked(true);//默认显示 "主题" Fragment
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fragmentManager = getFragmentManager();
            //themeFragment = new ThemeFragment();//"网络主题" // TODO: 2018/10/16
            localThemeFragment = new LocalThemeFragment();//"一期先使用本地主题"
            mineFragment = new MineFragment();//"我的"
            searchFragment = new SearchFragment();//"搜索"
            radioButton1.setChecked(true);//默认显示 "主题" Fragment
        }
    }

    @OnCheckedChanged({R.id.radioButton1, R.id.radioButton2, R.id.radioButton3})
    public void onCheckedChanged(CompoundButton view, boolean isChanged) {
        //onCheckedChanged会回掉 选中和取消选中的两个事件，我们只关注被选中的事件，所以加上isChange判断
        if (isChanged) {
            switch (view.getId()) {
                case R.id.radioButton1:
                    radioButton2.setChecked(false);
                    radioButton3.setChecked(false);
                    Util.d(this, "check radioButton1");
//                    addFragmentToContentLayout(themeFragment); // TODO: 2018/10/16
                    addFragmentToContentLayout(localThemeFragment);
                    break;
                case R.id.radioButton2:
                    radioButton1.setChecked(false);
                    radioButton3.setChecked(false);
                    Util.d(this, "check radioButton2");
                    addFragmentToContentLayout(mineFragment);
                    break;
                case R.id.radioButton3:
                    radioButton1.setChecked(false);
                    radioButton2.setChecked(false);
                    Util.d(this, "check radioButton3");
                    addFragmentToContentLayout(searchFragment);
                    break;
            }
        }
    }


    //向布局中添加fragment
    public static void addFragmentToContentLayout(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        //tag可以为null或者相对应的tag，flags只有0和1(POP_BACK_STACK_INCLUSIVE)两种情况
        // 如果tag为null，flags为0时，弹出回退栈中最上层的那个fragment。
        // 如果tag为null ，flags为1时，弹出回退栈中所有fragment。
        // 如果tag不为null，那就会找到这个tag所对应的fragment，flags为0时，弹出该
        // fragment以上的Fragment，如果是1，弹出该fragment（包括该fragment）以上的fragment。
        fragmentManager.popBackStack(null, 1);//弹出回退栈中所有的fragment
        fragmentTransaction.add(R.id.contentLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * 界面回退逻辑
     */
    public void goBack() {
        Fragment topShowingFragment = fragmentManager.findFragmentById(R.id.contentLayout);
//        if (topShowingFragment.getClass().getSimpleName().equals("ThemeFragment") // TODO: 2018/10/16
        if (topShowingFragment.getClass().getSimpleName().equals("LocalThemeFragment")
                || topShowingFragment.getClass().getSimpleName().equals("MineFragment")
                || topShowingFragment.getClass().getSimpleName().equals("SearchFragment")) {
            finish();
            Util.d(this, "Finish MainActivity");
        } else {
            fragmentManager.findFragmentById(R.id.contentLayout);
            fragmentManager.popBackStack(null, 0);//弹出栈顶的fragment
        }
    }


}

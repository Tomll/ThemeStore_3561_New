package com.mapbar.wesmart.themestore.fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbar.wesmart.themestore.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dongrp on 2018/9/30.
 */

public class SearchFragment extends BaseFragment {

    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;
    @BindView(R.id.edtTxt_search_content)
    EditText edtTxtSearchContent;
    @BindView(R.id.txt_search_count)
    TextView txtSearchCount;
    @BindView(R.id.layout_clear)
    LinearLayout layoutClear;
    @BindView(R.id.button_clear)
    TextView buttonClear;
    @BindView(R.id.frameLayoutNoContent)
    FrameLayout frameLayoutNoContent;
    @BindView(R.id.textViewNoContent)
    TextView textViewNoContent;

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
                if (actionId == EditorInfo.IME_ACTION_SEARCH){//通过软键盘的“完成”按钮，ediText传来搜索Action
                    if (TextUtils.isEmpty(edtTxtSearchContent.getText())){
                        Toast.makeText(mActivity,"输入内容不能为空",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    frameLayoutNoContent.setVisibility(View.VISIBLE);
                    textViewNoContent.setText(String.format(mActivity.getResources().getString(R.string.no_search_content),edtTxtSearchContent.getText().toString()));
                }
                return false;
            }
        });

    }

    @OnClick({R.id.layout_search, R.id.layout_clear, R.id.button_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_search://放大镜按钮
                if (TextUtils.isEmpty(edtTxtSearchContent.getText())){
                    Toast.makeText(mActivity,"输入内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                frameLayoutNoContent.setVisibility(View.VISIBLE);
                textViewNoContent.setText(String.format(mActivity.getResources().getString(R.string.no_search_content),edtTxtSearchContent.getText().toString()));
                break;
            case R.id.layout_clear://删除按钮
                edtTxtSearchContent.setText("");
                textViewNoContent.setText("");
                frameLayoutNoContent.setVisibility(View.GONE);

                break;
            case R.id.button_clear://取消按钮
                edtTxtSearchContent.setText("");
                textViewNoContent.setText("");
                frameLayoutNoContent.setVisibility(View.GONE);

                break;
        }
    }

}

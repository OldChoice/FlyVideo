package com.myq.flyvideo.mainfly.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myq.flyvideo.AGVideo.AGVideoActivity;
import com.myq.flyvideo.R;

import gr.free.grfastuitils.activitybase.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button btnNext, btnMenu, btnDown, btnweb, btnList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        setListener();


    }

    @Override
    public void findView() {
        btnNext = findViewById(R.id.btnnext);
        btnMenu = findViewById(R.id.btn_menulist);
        btnDown = findViewById(R.id.btn_down);
        btnweb = findViewById(R.id.btn_web);
        btnList = findViewById(R.id.btn_list);

    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        btnweb.setOnClickListener(this);
        btnList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnnext:
                toStartActivity(AGVideoActivity.class);
                break;
            case R.id.btn_menulist:
                toStartActivity(MenuListActivity.class);
                break;
            case R.id.btn_down:
                toStartActivity(DownLodeActivity.class);
                break;
            case R.id.btn_web:
                toStartActivity(WebViewActivity.class);
                break;
            case R.id.btn_list:
                toStartActivity(TVListActivity.class);
                break;

        }
    }

}
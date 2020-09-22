package com.myq.flyvideo.mainfly.activity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myq.flyvideo.AGVideo.AGEpsodeEntity;
import com.myq.flyvideo.R;
import com.myq.flyvideo.mainfly.adapter.TVListAdapter;
import com.myq.flyvideo.mainfly.getdata.TVListVo;
import com.myq.flyvideo.myview.DividerDecoration;
import com.myq.flyvideo.utils.TVUrls;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.jzvd.JZDataSource;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.tools.MyToast;

import static cn.jzvd.Jzvd.backPress;

/**
 * Create by guorui on 2020/9/21
 * Last update 2020/9/21
 * Description:视频直播列表
 **/
public class TVListActivity extends BaseActivity implements BGAOnRVItemClickListener {

    private JzvdStd mJzvdStd;
    private Jzvd.JZAutoFullscreenListener mSensorEventListener;
    private SensorManager mSensorManager;
    private RecyclerView recyclerView;
    private TVListAdapter adapter;
    private List<TVListVo> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recycle);
        mJzvdStd = findViewById(R.id.jz_video);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new Jzvd.JZAutoFullscreenListener();
        mJzvdStd.setUp(TVUrls.urls[0], TVUrls.names[0]);
        Glide.with(this).load(TVUrls.videoPosterList[0]).into(mJzvdStd.posterImageView);
        mJzvdStd.startVideo();

        initValues();
        findView();
        setView();
        setListener();

        lists = new ArrayList<>();
        for (int i = 0; i < TVUrls.names.length; i++) {
            TVListVo tvListVo = new TVListVo();
            tvListVo.setName(TVUrls.names[i]);
            tvListVo.setUrl(TVUrls.urls[i]);
            lists.add(tvListVo);
        }
        adapter.setData(lists);


    }


    @Override
    public void initValues() {


    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.search_number_recycle);
        adapter = new TVListAdapter(recyclerView);

    }

    @Override
    public void setView() {

    }

    @Override
    public void setListener() {
        setRecycle();
        adapter.setOnRVItemClickListener(this);

    }

    private void setRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TVListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(TVListActivity.this, LinearLayoutManager.VERTICAL, Color.parseColor("#f4f4f4"), 1, 0, 0));

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        MyToast.showShort(adapter.getItem(position).getName());
        JZDataSource mJzDataSource = new JZDataSource(TVUrls.urls[position], TVUrls.names[position]);
        mJzvdStd.changeUrl(mJzDataSource, 0);

    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //home back
        Jzvd.goOnPlayOnResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JZUtils.clearSavedProgress(this, null);
        //home back
        Jzvd.goOnPlayOnPause();
    }


    @Override
    public void onBackPressed() {
        if (backPress()) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Jzvd.releaseAllVideos();
    }


}
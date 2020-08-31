package com.myq.flyvideo.mainfly.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.myq.flyvideo.AGVideo.AGEpsodeEntity;
import com.myq.flyvideo.AGVideo.AGVideo;
import com.myq.flyvideo.AGVideo.JZMediaExo;
import com.myq.flyvideo.AGVideo.popup.VideoEpisodePopup;
import com.myq.flyvideo.AGVideo.popup.VideoSpeedPopup;
import com.myq.flyvideo.R;
import com.myq.flyvideo.utils.ScreenRotateUtils;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.pedant.SweetAlert.SweetAlertDialog;
import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.tools.LoadUtils;
import gr.free.grfastuitils.tools.ThreadPool;

/**
 * Create by guorui on 2020/8/31
 * Last update 2020-8-31 17:14:38
 * Description:播放界面
 **/
public class PlayVideoActivity extends BaseActivity implements AGVideo.JzVideoListener, ScreenRotateUtils.OrientationChangeListener
        , VideoSpeedPopup.SpeedChangeListener, VideoEpisodePopup.EpisodeClickListener {
    private AGVideo mPlayer;
    private JZDataSource mJzDataSource;
    private List<AGEpsodeEntity> episodeList;
    private TabLayout episodes;
    private int playingNum = 0;
    //倍数弹窗
    private VideoSpeedPopup videoSpeedPopup;
    private VideoEpisodePopup videoEpisodePopup;

    private ArrayList<String> vUrlLists;
    private ArrayList<String> vNameLists;
    private String strName;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        initVideoData();
        initView();
        ScreenRotateUtils.getInstance(getApplicationContext()).setOrientationChangeListener(this);
    }

    //初始化视频数据
    private void initVideoData() {

        vUrlLists = getIntent().getStringArrayListExtra("urls");
        vNameLists = getIntent().getStringArrayListExtra("names");
        strName = getIntent().getStringExtra("vName");

        episodeList = new ArrayList<>();
        for (int i = 0; i < vUrlLists.size(); i++) {
            episodeList.add(new AGEpsodeEntity(vUrlLists.get(i), strName + vNameLists.get(i)));
        }
    }

    private void initView() {
        webView = new WebView(this);
        episodes = findViewById(R.id.video_episodes);
        mPlayer = findViewById(R.id.ag_player);
        initEpisodesTablayout();
        mPlayer.setJzVideoListener(this);
//        mJzDataSource = new JZDataSource(episodeList.get(0).getVideoUrl(), episodeList.get(0).getVideoName());
//        mPlayer.setUp(mJzDataSource, JzvdStd.SCREEN_NORMAL, JZMediaExo.class);
//        mPlayer.startVideo();
        toStartLoadVideo(episodeList.get(0).getVideoUrl(), episodeList.get(0).getVideoName(), null);
    }

    private void initEpisodesTablayout() {
        episodes.clearOnTabSelectedListeners();
        episodes.removeAllTabs();
        for (int i = 0; i < episodeList.size(); i++) {
            episodes.addTab(episodes.newTab().setText(vNameLists.get(i)));
        }
        //用来循环适配器中的视图总数
        for (int i = 0; i < episodes.getTabCount(); i++) {
            //获取每一个tab对象
            TabLayout.Tab tabAt = episodes.getTabAt(i);
            //将每一个条目设置我们自定义的视图
            tabAt.setCustomView(R.layout.tab_video_episodes);
            //通过tab对象找到自定义视图的ID
            TextView textView = tabAt.getCustomView().findViewById(R.id.tab_video_episodes_tv);
            //设置tab上的文字
            textView.setText(episodes.getTabAt(i).getText());
            textView.setTextSize(14);
            int current = playingNum;
            if (i == current) {
                //选中后字体
                textView.setTextColor(getResources().getColor(R.color.ThemeColor));
            } else {
                textView.setTextColor(getResources().getColor(R.color.font_color));

            }
        }

        episodes.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //定义方法，判断是否选中
                AGEpsodeEntity entity = episodeList.get(tab.getPosition());
                toStartLoadVideo(entity.getVideoUrl(), entity.getVideoName(), tab);
//                System.out.println("zzzzzzzzzzzzzzzzzzzzz"+"---"+tab.getPosition()+"----"+entity.getVideoName());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //定义方法，判断是否选中
                updateTabView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        isNext(episodes.getSelectedTabPosition());
    }


    @Override
    protected void onResume() {
        Jzvd.goOnPlayOnResume();
        super.onResume();
        ScreenRotateUtils.getInstance(this).start(this);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenRotateUtils.getInstance(this).stop();
        Jzvd.goOnPlayOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Jzvd.releaseAllVideos();
        ScreenRotateUtils.getInstance(this.getApplicationContext()).setOrientationChangeListener(null);
        webView.destroy();
    }

    /**
     * 用来改变tabLayout选中后的字体大小及颜色
     *
     * @param tab
     * @param isSelect
     */
    private void updateTabView(TabLayout.Tab tab, boolean isSelect) {
        //找到自定义视图的控件ID
        TextView tv_tab = tab.getCustomView().findViewById(R.id.tab_video_episodes_tv);
        if (isSelect) {
            //设置标签选中
            tv_tab.setSelected(true);
            //选中后字体
            tv_tab.setTextColor(getResources().getColor(R.color.ThemeColor));
        } else {
            //设置标签取消选中
            tv_tab.setSelected(false);
            //恢复为默认字体
            tv_tab.setTextColor(getResources().getColor(R.color.font_color));
        }
    }

    /**
     * 判断是否有下一集
     */
    private void isNext(int position) {
        //判断是否还有下一集
        if (position == (episodeList.size() - 1)) {
            mPlayer.changeNextBottonUi(false);
        } else {
            mPlayer.changeNextBottonUi(true);
        }
    }

    /**
     * 更换播放地址
     */
    private void playChangeUrl() {
        long progress = 0;
        mPlayer.changeUrl(mJzDataSource, progress);
    }

    @Override
    public void orientationChange(int orientation) {
        if (Jzvd.CURRENT_JZVD != null
                && (mPlayer.state == Jzvd.STATE_PLAYING || mPlayer.state == Jzvd.STATE_PAUSE)
                && mPlayer.screen != Jzvd.SCREEN_TINY) {
            if (orientation >= 45 && orientation <= 315 && mPlayer.screen == Jzvd.SCREEN_NORMAL) {
                changeScreenFullLandscape(ScreenRotateUtils.orientationDirection);
            } else if (((orientation >= 0 && orientation < 45) || orientation > 315) && mPlayer.screen == Jzvd.SCREEN_FULLSCREEN) {
                changeScrenNormal();
            }
        }
    }


    /**
     * 竖屏并退出全屏
     */
    private void changeScrenNormal() {
        if (mPlayer != null && mPlayer.screen == Jzvd.SCREEN_FULLSCREEN) {
            mPlayer.autoQuitFullscreen();
        }
    }

    /**
     * 横屏
     */
    private void changeScreenFullLandscape(float x) {
        //从竖屏状态进入横屏
        if (mPlayer != null && mPlayer.screen != Jzvd.SCREEN_FULLSCREEN) {
            if ((System.currentTimeMillis() - Jzvd.lastAutoFullscreenTime) > 2000) {
                mPlayer.autoFullscreen(x);
                Jzvd.lastAutoFullscreenTime = System.currentTimeMillis();
            }
        }
    }


    /**
     * 关闭倍速播放弹窗和选集弹窗
     */
    private void dismissSpeedPopAndEpisodePop() {
        if (videoSpeedPopup != null) {
            videoSpeedPopup.dismiss();
        }
        if (videoEpisodePopup != null) {
            videoEpisodePopup.dismiss();
        }
    }

    /**
     * 改变播放倍速
     *
     * @param speed
     */
    private void changeSpeed(float speed) {
        Object[] object = {speed};
        mPlayer.mediaInterface.setSpeed(speed);
        mJzDataSource.objects[0] = object;
        Toast.makeText(this, "正在以" + speed + "X倍速播放", Toast.LENGTH_SHORT).show();
        mPlayer.speedChange(speed);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void nextClick() {
        int position = episodes.getSelectedTabPosition() + 1;
        AGEpsodeEntity entity = episodeList.get(position);
        mJzDataSource = new JZDataSource(entity.getVideoUrl(), entity.getVideoName());
        TabLayout.Tab tab = episodes.getTabAt(position);
        if (tab != null) {
            tab.select();
        }
    }

    @Override
    public void backClick() {
        if (mPlayer.screen == mPlayer.SCREEN_FULLSCREEN) {
            dismissSpeedPopAndEpisodePop();
            AGVideo.backPress();
        } else {
            finish();
        }
    }

    @Override
    public void throwingScreenClick() {
        Toast.makeText(this, "投屏", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void selectPartsClick() {
        if (videoEpisodePopup == null) {
            videoEpisodePopup = new VideoEpisodePopup(this, episodeList);
            videoEpisodePopup.setEpisondeClickListener(this);
        }
        videoEpisodePopup.setPlayNum(1);
        videoEpisodePopup.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT, 0, 0);
    }

    @Override
    public void speedClick() {
        if (videoSpeedPopup == null) {
            videoSpeedPopup = new VideoSpeedPopup(this);
            videoSpeedPopup.setSpeedChangeListener(this);
        }
        videoSpeedPopup.showAtLocation(getWindow().getDecorView(), Gravity.RIGHT, 0, 0);
    }


    @Override
    public void speedChange(float speed) {
        changeSpeed(speed);
    }

    @Override
    public void onEpisodeClickListener(AGEpsodeEntity entity, int position) {
        TabLayout.Tab tab = episodes.getTabAt(position);
        if (tab != null) {
            tab.select();
        }
    }

    private Handler handler = new Handler();
    private SweetAlertDialog loadingDialog;

    //获取地址并播放
    private void toStartLoadVideo(String strUrl, String strName, TabLayout.Tab tab) {
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadUrl(strUrl);
                        webView.setWebViewClient(new WebViewClient() {
                            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }

                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                super.onPageStarted(view, url, favicon);
                                loadingDialog = new LoadUtils().showBaseDialog(PlayVideoActivity.this, loadingDialog, "视频加载中...");
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                if (loadingDialog != null) {
                                    loadingDialog.dismiss();
                                }
                            }

                            @Override
                            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                                //抓取所有地址
                                String[] movTypes = {".m3u8", ".mp4", ".3gp", ".wmv", ".avi", ".rm"};
                                String strUrls = url;
                                try {
                                    //判断地址长度是否大于10，而且头是Http
                                    if (strUrls.length() > 10 && strUrls.substring(0, 4).equals("http")) {
                                        // System.out.println(strUrls);
                                        //判断是否地址后面带?参数，然后去掉只留没参数地址
                                        strUrls = strUrls.indexOf("?") > 4 ? strUrls.substring(0, strUrls.indexOf("?")) : strUrls;
                                        for (int i = 0; i < movTypes.length; i++) {
                                            //如果最后地址尾在的位置和尾长度合等于整个地址长度，那么是视频地址
                                            if (strUrls.lastIndexOf(movTypes[i]) + movTypes[i].length() == strUrls.length()) {
                                                //获取一个视频地址就行了
                                                System.out.println(strUrls);
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        view.stopLoading();
                                                        if (tab == null) {
                                                            mJzDataSource = new JZDataSource(url, strName);
                                                            mPlayer.setUp(mJzDataSource, JzvdStd.SCREEN_NORMAL, JZMediaExo.class);
                                                            mPlayer.startVideo();
                                                        } else {
                                                            mJzDataSource = new JZDataSource(url, strName);
                                                            updateTabView(tab, true);
                                                            playChangeUrl();
                                                            isNext(tab.getPosition());
                                                            episodes.selectTab(tab);
                                                        }
                                                    }
                                                });
                                                break;
                                            }
                                        }
                                    }

                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            @Override
                            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                                handler.proceed();  //接受所有证书
                            }
                        });
                    }
                });

            }
        });
    }

}
package com.myq.flyvideo.mainfly.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fanchen.sniffing.DefaultFilter;
import com.fanchen.sniffing.SniffingUICallback;
import com.fanchen.sniffing.SniffingVideo;
import com.fanchen.sniffing.web.SniffingUtil;
import com.myq.flyvideo.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.tools.LoadUtils;

public class WebViewActivity extends BaseActivity  implements SniffingUICallback {

    private WebView webView;
    private EditText etInput;
    private long exitTime = 0;
    private SweetAlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_web_view);

        etInput = findViewById(R.id.etinput);
        webView = findViewById(R.id.web);

        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                webss();
                return false;
            }
        });
        etInput.setText("http://m.20mao.com/Play/1-65337-1-48.Html");

//        webss();

        com.fanchen.sniffing.x5.SniffingUtil.get().activity(this).referer("http://m.20mao.com/Play/1-65337-1-48.Html")
                .callback(this).connTimeOut(30 * 1000).readTimeOut(30 * 1000)
                .filter(new DefaultFilter()).url(etInput.getText().toString().trim()).start();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SniffingUtil.get().releaseAll();
        com.fanchen.sniffing.x5.SniffingUtil.get().releaseAll();
    }

    @Override
    public void onSniffingStart(View webView, String url) {
        loadingDialog = new LoadUtils().showBaseDialog(WebViewActivity.this, loadingDialog, "加载中...");
    }

    @Override
    public void onSniffingFinish(View webView, String url) {
        loadingDialog.dismiss();
    }

    @Override
    public void onSniffingSuccess(View webView, String url, List<SniffingVideo> videos) {
//        textView.setText(videos.toString());
        System.out.println(videos.size()+"----");
        System.out.println(videos.get(0).getUrl());
    }

    @Override
    public void onSniffingError(View webView, String url, int errorCode) {
        Toast.makeText(this,"解析失败...",Toast.LENGTH_SHORT).show();
    }



    private void webss() {
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDatabaseEnabled(true);
//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setSupportZoom(true);//支持缩放
        webView.getSettings().supportMultipleWindows();//多窗口
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//关闭webview中缓存
        webView.getSettings().setAllowFileAccess(true);//设置可以访问文件
        webView.getSettings().setNeedInitialFocus(true);//当webview调用requestFocus时为webview设置节点
        webView.getSettings().setLoadsImagesAutomatically(true);//支持自动加载图片
        webView.getSettings().setBuiltInZoomControls(true);//支持缩放
//        webView.setInitialScale(100);//设置缩放比例
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);//设置滚动条隐藏
        webView.getSettings().setGeolocationEnabled(true);//启用地理定位
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);//设置渲染优先级
        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("https://www.baidu.com/");
        webView.loadUrl(etInput.getText().toString());
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
                loadingDialog = new LoadUtils().showBaseDialog(WebViewActivity.this, loadingDialog, "加载中...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // TODO Auto-generated method stub
                //super.onReceivedSslError(view, handler, error);
                Log.d("hhhhhhhhhhh", "onReceivedSslError.....................");
                handler.proceed();  //接受所有证书
            }
        });
    }

}
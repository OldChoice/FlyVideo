package com.myq.flyvideo.mainfly.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fanchen.sniffing.DefaultFilter;
import com.fanchen.sniffing.LogUtil;
import com.fanchen.sniffing.SniffingUICallback;
import com.fanchen.sniffing.SniffingVideo;
import com.fanchen.sniffing.Util;
import com.fanchen.sniffing.node.Node;
import com.fanchen.sniffing.web.SniffingUtil;
import com.myq.flyvideo.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.tools.LoadUtils;
import gr.free.grfastuitils.tools.ThreadPool;

/**
 * Create by guorui on 2020/8/31
 * Last update 2020/8/31
 * Description:
 **/
public class WebViewActivity extends BaseActivity {

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
//        webView = new WebView(this);
//        webView.setLayoutParams(new ViewGroup.LayoutParams(1, 1));

        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                webss();
                return false;
            }
        });
//        etInput.setText("http://m.20mao.com/Play/1-65337-1-48.Html");
        etInput.setText("http://m.20mao.com/Play/1-46649-1-1.Html");

        webss();


    }


    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }


    private void webss1() {
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
//        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

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
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                //抓取所有地址
                String[] movTypes = {".m3u8", ".mp4", ".3gp", ".wmv", ".avi", ".rm"};
                try {
                    //判断地址长度是否大于10，而且头是Http
                    if (url.length() > 10 && url.substring(0, 4).equals("http")) {
                        System.out.println(url);
                        for (int i = 0; i < movTypes.length; i++) {
                            //如果最后地址尾在的位置和尾长度合等于整个地址长度，那么是视频地址
                            if (url.lastIndexOf(movTypes[i]) + movTypes[i].length() == url.length()) {
                                System.out.println(url);
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
                // TODO Auto-generated method stub
                //super.onReceivedSslError(view, handler, error);
                Log.d("hhhhhhhhhhh", "onReceivedSslError.....................");
                handler.proceed();  //接受所有证书
            }
        });
    }


    private void webss() {
        Handler handler = new Handler();
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadUrl(etInput.getText().toString());
//                        webView.loadUrl("https://github.com");
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
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        view.stopLoading();
//                                    }
//                                }, 1000);
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
                                        //判断是否地址后面带?参数，然后去掉只留没参数地址
                                        strUrls = strUrls.indexOf("?") > 4 ? strUrls.substring(0, strUrls.indexOf("?")) : strUrls;
                                        for (int i = 0; i < movTypes.length; i++) {
                                            //如果最后地址尾在的位置和尾长度合等于整个地址长度，那么是视频地址
                                            if (strUrls.lastIndexOf(movTypes[i]) + movTypes[i].length() == strUrls.length()) {
                                                //获取一个视频地址就行了
                                                System.out.println(strUrls);
                                                System.out.println(url);
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        view.stopLoading();
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
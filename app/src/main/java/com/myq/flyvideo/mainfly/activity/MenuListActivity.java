package com.myq.flyvideo.mainfly.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.myq.flyvideo.mainfly.adapter.MenuListAdapter;
import com.myq.flyvideo.mainfly.getdata.MovieListVo;
import com.myq.flyvideo.R;
import com.myq.flyvideo.utils.MyUrls;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.myview.DeletableEditText;
import gr.free.grfastuitils.tools.LoadUtils;
import gr.free.grfastuitils.tools.MyToast;
import gr.free.grfastuitils.tools.ThreadPool;

/**
 * Create by guorui on 2020/8/11
 * Last update 2021-10-14 16:48:52
 * Description:视屏搜索目录
 **/
public class MenuListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private DeletableEditText etSearch;
    private Handler handler;
    private MenuListAdapter menuListAdapter;
    private SweetAlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        initValues();
        findView();
        setView();
        setListener();
        getMovies("楚汉传奇");
        etSearch.setText("楚汉传奇");
//        getMovies("熊猫");


    }

    @Override
    public void initValues() {
        handler = new Handler();

    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.menu_list_recycle);
        etSearch = findViewById(R.id.menu_list_et_search);


    }

    @Override
    public void setView() {
        setRecycle();
        menuListAdapter = new MenuListAdapter(MenuListActivity.this, null);
        recyclerView.setAdapter(menuListAdapter);
    }

    private void setRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MenuListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setAdapter(menuListAdapter);
//        recyclerView.addItemDecoration(new DividerDecoration(MenuListActivity.this, LinearLayoutManager.VERTICAL, Color.parseColor("#f4f4f4"), 1, 0, 0));


    }

    @Override
    public void setListener() {
        etSearch.setOnKeyListener(keyListener);
        menuListAdapter.setOnitemClickLintener(position -> {
            MovieListVo.ListBean listBean = menuListAdapter.getItem(position);
            System.out.println(position + "-----" + listBean.getName() + "-----" + listBean.getUrl());
            getMovieDetails(listBean.getUrl(), listBean.getName());

        });

    }

    /**
     * 搜索触发事件
     */
    private View.OnKeyListener keyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                clearInputMethod();
                menuListAdapter.clear();
                getMovies(etSearch.getText().toString());
            }
            return false;
        }
    };

    /**
     * 让软键盘消失
     */
    protected void clearInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    //获取天龙电影目录
    private void getMovies(String mov) {
        loadingDialog = new LoadUtils().showBaseDialog(MenuListActivity.this, loadingDialog, "视频搜索中...");
        MovieListVo movieListVo = new MovieListVo();
        ThreadPool.getInstance().execute(() -> {
            try {
                Document doc = Jsoup.connect(MyUrls.tianlong + "/Search/-------------.html?wd=" + mov + "&Submit=搜索")
                        .data("query", "Java")
                        .userAgent("Mozilla")
                        .cookie("auth", "token")
                        .timeout(300000)
                        .get();
                Elements eUrl = doc.select("ul");
                //获取到的最后一段为视屏信息，然后解析视频列表
                Elements list = eUrl.get(eUrl.size() - 1).select("li");
                // System.out.println(list);
                List<MovieListVo.ListBean> listBeans = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Elements listitem1 = list.get(i).select("a");
                    String url = listitem1.get(0).attr("href");
                    Elements listitem2 = list.get(i).select("img");
                    String img = listitem2.get(0).attr("src");
                    String name = listitem2.get(0).attr("alt");
                    String msg = list.get(i).select("div").text();
                    MovieListVo.ListBean listBean = new MovieListVo.ListBean();
                    listBean.setName(name);
                    listBean.setImg(img);
                    listBean.setMsg(msg);
                    listBean.setUrl(url);
                    listBean.setSource("天龙影院");
                    listBeans.add(listBean);
                    movieListVo.setList(listBeans);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.post(() -> {
                menuListAdapter.updataAdd(movieListVo.getList());
                loadingDialog.dismiss();

            });
        });
    }

    //获取当前视频信息列表
    private void getMovieDetails(String strUrl, String strName) {
        ArrayList<String> vUrlLists = new ArrayList();
        ArrayList<String> vNameLists = new ArrayList();
        loadingDialog = new LoadUtils().showBaseDialog(MenuListActivity.this, loadingDialog, "视频解析中...");
        ThreadPool.getInstance().execute(() -> {
            try {
                Document doc = Jsoup.connect(MyUrls.tianlong + strUrl)
                        .timeout(300000)
                        .get();
                Elements eUrl = doc.select("div");
                // System.out.println(eUrl);
                // System.out.println(eUrl.size());
                // System.out.println(eUrl.get(33));
                if (eUrl.size() > 47) {
                    //视屏都在33位置里好多通道资源
                    Elements lists = eUrl.get(33).select("div");
                    //视屏通道数是总div减去头部一个尾部两个共三个，剩下的都是通道数
                    if (lists.size() > 4) {
                        handler.post(() -> {
                            List<String> setDialog = new ArrayList<>();
                            for (int a = 2; a < lists.size() - 1; a++) {
                                Elements es = lists.get(a).select("a");
                                setDialog.add("通道" + (a - 1) + "共:" + es.size() + "个视频");
                            }

                            new MaterialDialog.Builder(this)
                                    .items(setDialog)
                                    .itemsCallback((dialog, view, position, text) -> {
                                        Elements list = lists.get(position + 2).select("a");
                                        for (int i = 0; i < list.size(); i++) {
                                            String url = list.get(i).attr("href");
                                            if (url.length() > 40) {
                                                String vUrl = MyUrls.tianlong + url.substring(24, url.length() - 9);
                                                vUrlLists.add(vUrl);
                                                vNameLists.add(list.get(i).text());
                                                // System.out.println(list.get(i).text());
                                                // System.out.println(url.substring(24, url.length() - 9));
                                            }
                                        }

                                        handler.post(() -> {
                                            // 有可能没有视频
                                            if (vUrlLists.size() > 0) {
                                                Intent intent = new Intent(MenuListActivity.this, PlayVideoActivity.class);
                                                intent.putStringArrayListExtra("urls", vUrlLists);
                                                intent.putStringArrayListExtra("names", vNameLists);
                                                intent.putExtra("vName", strName);
                                                startActivity(intent);
                                            } else {
                                                MyToast.showShort("无视频");
                                            }

                                        });
                                    })
                                    .show();

                        });
                    } else {
                        Elements list = lists.get(2).select("a");
                        for (int i = 0; i < list.size(); i++) {
                            String url = list.get(i).attr("href");
                            if (url.length() > 40) {
                                String vUrl = MyUrls.tianlong + url.substring(24, url.length() - 9);
                                vUrlLists.add(vUrl);
                                vNameLists.add(list.get(i).text());
                                // System.out.println(list.get(i).text());
                                // System.out.println(url.substring(24, url.length() - 9));
                            }
                        }


                        handler.post(() -> {
                            // 有可能没有视频
                            if (vUrlLists.size() > 0) {
                                Intent intent = new Intent(MenuListActivity.this, PlayVideoActivity.class);
                                intent.putStringArrayListExtra("urls", vUrlLists);
                                intent.putStringArrayListExtra("names", vNameLists);
                                intent.putExtra("vName", strName);
                                startActivity(intent);
                            } else {
                                MyToast.showShort("无视频");
                            }

                        });
                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            loadingDialog.dismiss();
        });
    }

}
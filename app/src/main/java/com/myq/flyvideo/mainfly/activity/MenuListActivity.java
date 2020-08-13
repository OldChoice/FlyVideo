package com.myq.flyvideo.mainfly.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.myq.flyvideo.mainfly.adapter.MenuListAdapter;
import com.myq.flyvideo.mainfly.getdata.MovieListVo;
import com.myq.flyvideo.R;
import com.myq.flyvideo.utils.URLEncodeing;
import com.myq.flyvideo.myview.DividerDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.tools.LoadUtils;
import gr.free.grfastuitils.tools.ThreadPool;

/**
 * Create by guorui on 2020/8/11
 * Last update 2020-8-13 10:48:13
 * Description:视屏搜索目录
 **/
public class MenuListActivity extends BaseActivity {

    private RecyclerView recyclerView;
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
        getMovies("大主宰");
        getMovies("熊猫");


    }

    @Override
    public void initValues() {
        handler = new Handler();

    }

    @Override
    public void findView() {
        recyclerView = findViewById(R.id.menulist_recycle);


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
        recyclerView.addItemDecoration(new DividerDecoration(MenuListActivity.this, LinearLayoutManager.VERTICAL, Color.parseColor("#f4f4f4"), 1, 0, 0));


    }

    @Override
    public void setListener() {
        menuListAdapter.setOnitemClickLintener(new MenuListAdapter.OnitemClick() {
            @Override
            public void onItemClick(int position) {
                MovieListVo.ListBean listBean = menuListAdapter.getItem(position);
                System.out.println(position + "-----" + listBean.getName());


            }
        });

    }

    //获取电影目录
    private void getMovies(String mov) {
        loadingDialog = new LoadUtils().showBaseDialog(MenuListActivity.this, loadingDialog,"视频搜索中...");
        MovieListVo movieListVo = new MovieListVo();
        ThreadPool.getInstance().execute(() -> {
            try {
                Document doc = Jsoup.connect("http://www.tl86tv.com/Search.asp?keyword=" + URLEncodeing.toURLEncoded(mov))
//                        .data("query", "Java")
//                        .userAgent("Mozilla")
//                        .cookie("auth", "token")
//                        .timeout(3000)
                        .get();
                Elements eurl = doc.select("ul");
                //获取到的最后一段为视屏信息，然后解析视频列表
                Elements list = eurl.get(eurl.size() - 1).select("li");
                System.out.println(list);
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

    //获取电影剧集
    private void getMovieList(String mov) {
        ThreadPool.getInstance().execute(() -> {
            try {
                Document doc = Jsoup.connect("http://www.tl86tv.com/Search.asp?keyword=" + URLEncodeing.toURLEncoded(mov))
                        .data("query", "Java")
                        .userAgent("Mozilla")
                        .cookie("auth", "token")
                        .timeout(3000)
                        .get();
                Elements eurl = doc.select("ul");


            } catch (IOException e) {
                e.printStackTrace();
            }
            handler.post(() -> {

            });
        });
    }

}
package com.myq.flyvideo.mainfly.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.myq.flyvideo.AGVideo.AGVideoActivity;
import com.myq.flyvideo.mainfly.getdata.MovieListVo;
import com.myq.flyvideo.R;
import com.myq.flyvideo.utils.URLEncodeing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gr.free.grfastuitils.activitybase.BaseActivity;
import gr.free.grfastuitils.tools.ThreadPool;

public class MainActivity extends BaseActivity {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNext = findViewById(R.id.btnnext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AGVideoActivity.class));
            }
        });
        Button btnMenu = findViewById(R.id.btn_menulist);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MenuListActivity.class));
            }
        });
        handler = new Handler();
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                getData22();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });


    }

    private void getData1() {
        try {
//            Document doc = Jsoup.connect("http://m.20mao.com").get();
//            Document doc = Jsoup.connect("http://m.20mao.com")
            Document doc = Jsoup.connect("http://www.tl86tv.com/Search.asp?keyword=" + URLEncodeing.toURLEncoded("大主宰"))
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();
//            System.out.println(doc.toString());
            Elements elements = doc.select("ul");
            Element element = elements.get(elements.size() - 1);
//            System.out.println(element.toString());
//            String linkHref = element.attr("href");
//            System.out.println(linkHref);
//            String linkText = element.text();
//            System.out.println(linkText);

            Elements elements1 = element.select("li");
            System.out.println(elements1.size());
            System.out.println(elements1.get(1));

            Elements elements2 = elements1.get(1).select("a");
            System.out.println(elements2.size());
            System.out.println(elements2.get(1));


            String linkHref = elements1.get(1).attr("href");
            System.out.println(linkHref + "-----");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MovieListVo movieListVo = new MovieListVo();

    private void getData() {
        try {
            Document doc = Jsoup.connect("http://www.tl86tv.com/Search.asp?keyword=" + URLEncodeing.toURLEncoded("大主宰"))
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();
            Elements eurl = doc.select("ul");
            //获取到的最后一段为视屏信息，然后解析视频列表
            Elements list = eurl.get(eurl.size() - 1).select("li");
            System.out.println(list.get(1));
            List<MovieListVo.ListBean> listBeans=new ArrayList<>();
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
    }
    private void getData22() {
        try {
            Document doc = Jsoup.connect("http://www.tl86tv.com/Movie/65337.Html")
                    .get();
//            System.out.println(doc);
            Elements eurl = doc.select("div");
//            System.out.println(eurl);
//            System.out.println(eurl.size());
            System.out.println(eurl.get(47));

            Elements list = eurl.get(eurl.size() - 1).select("span");
            System.out.println(list);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Elements getElements(Elements elements, String str) {
        return elements.select(str);
    }


}
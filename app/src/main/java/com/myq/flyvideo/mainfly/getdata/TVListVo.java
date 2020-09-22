package com.myq.flyvideo.mainfly.getdata;

import java.util.List;

/**
 * Create by guorui on 2020/9/21
 * Last update 2020/9/21
 * Description:
 **/
public class TVListVo {
    private String name;//名字
    private String url;//地址
    private String img;//图片
    private String msg;//介绍等
    private String source;//来源


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

package com.myq.flyvideo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Create by guorui on 2020/8/10
 * Last update 2020/8/10
 * Description:
 **/
public class URLEncodeing {

    //文字转换为gb2312的编码
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "Utf-8");
            str = URLEncoder.encode(str, "Gb2312");
            return str;
        } catch (Exception localException) {
        }
        return "";
    }

    //编码转换为文字
    public static String toURLDecoder(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String url = new String(paramString.getBytes(), "Gb2312");
            url = URLDecoder.decode(url, "Gb2312");
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}

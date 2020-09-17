package com.myq.flyvideo.mainfly.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.common.HttpOption;
import com.arialyy.aria.core.common.RequestEnum;
import com.arialyy.aria.core.download.m3u8.M3U8LiveOption;
import com.arialyy.aria.core.download.m3u8.M3U8VodOption;
import com.arialyy.aria.core.processor.IHttpFileLenAdapter;
import com.myq.flyvideo.R;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import gr.free.grfastuitils.tools.ThreadPool;
/**
* Create by guorui on 2020/9/2
* Last update 2020/9/2
* Description:https://github.com/AriaLyy/Aria
**/
public class DownLodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_lode);

        Aria.download(this).register();
//
//        // 修改最大下载数，调用完成后，立即生效
//        // 如当前下载任务数是4，修改完成后，当前任务数会被Aria自动调度任务数
//        Aria.get(this).getDownloadConfig().setMaxTaskNum(3);

//        long taskId1 = Aria.download(DownLodeActivity.this)
//                .load("https://y.qq.com/download/import/QQMusic-import-1.2.1.zip")     //读取下载地址
//                .setFilePath(getDBPath()+"11/aa1.zip", true) //设置文件保存的完整路径
//                .create();   //创建并启动下载
//        long taskId = Aria.download(DownLodeActivity.this)
//                .load("https://y.qq.com/download/import/QQMusic-import-1.2.1.zip")     //读取下载地址
//                .setFilePath(getDBPath()+"11/aa.zip", true) //设置文件保存的完整路径
//                .create();   //创建并启动下载

//
//        HttpOption option = new HttpOption();
//        option.useServerFileName(true);
//        option.addHeader("1", "@")
//                .useServerFileName(true)
//                .setFileLenAdapter(new FileLenAdapter());
//        option.setRequestType(RequestEnum.POST);
//        System.out.println(mUrl);
//        System.out.println(mFilePath);
//        long mTaskId = Aria.download(DownLodeActivity.this)
//                .load(mUrl)
//                .setFilePath(mFilePath, true)
//                .option(option)
//                .ignoreCheckPermissions()
//                .create();

//        Aria.download(this)
//                .loadGroup(Arrays.asList("https://y.qq.com/download/import/QQMusic-import-1.2.1.zip"))
//                .ignoreFilePathOccupy()
//                .setSubFileName(Arrays.asList("erqer.zip"))
//                .setDirPath("/storage/emulated/0/1")
//                .setGroupAlias("alsa")
//                .unknownSize()
//                .create();
//        Aria.download(this)
//                .loadGroup(Arrays.asList("https://codeload.github.com/AriaLyy/Aria/zip/master"))
//                .setSubFileName(Arrays.asList("zdfa.zip"))
//                .setDirPath("/storage/emulated/0/1")
//                .ignoreFilePathOccupy()
//                .setGroupAlias("alsa")
//                .unknownSize()
//                .create();
//        Aria.download(this)
//                .loadGroup(Arrays.asList("https://www.821583.com/20190405/417X52Nn/index.m3u8"))
//                .setSubFileName(Arrays.asList("zfadf.mp4"))
//                .setDirPath("/storage/emulated/0/1")
//                .ignoreFilePathOccupy()
//                .setGroupAlias("alsa")
//                .unknownSize()
//                .create();
        M3U8VodOption option = new M3U8VodOption();
        long taskId = Aria.download(this)
                .load("https://www.821583.com/20190405/417X52Nn/index.m3u8") // 设置直播文件下载地址
                .setFilePath(getDBPath()+"11/zdfa.ts", true) // 设置直播文件保存路径
                .m3u8VodOption(option)   // 调整下载模式为m3u8直播
                .create();
//        Aria.download(this).load("https://www.821583.com/20190405/417X52Nn/index.m3u8").m3u8VodOption().jumPeerIndex(10);

    }

    static class FileLenAdapter implements IHttpFileLenAdapter {
        @Override
        public long handleFileLen(Map<String, List<String>> headers) {

            List<String> sLength = headers.get("Content-Length");
            if (sLength == null || sLength.isEmpty()) {
                return -1;
            }
            String temp = sLength.get(0);

            return Long.parseLong(temp);
        }
    }
    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().toString();
        } else {
            return "";
        }
    }

    public static String getDBPath() {
        String sdCardPath = getSDPath();
        if (TextUtils.isEmpty(sdCardPath)) {
            return "";
        } else {
            //写数据库基础路径
            return sdCardPath + File.separator ;
        }
    }

}
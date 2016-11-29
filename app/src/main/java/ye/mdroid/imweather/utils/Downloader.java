package ye.mdroid.imweather.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ye.mdroid.imweather.activity.MainActivity;

/**
 * Created by ye on 16-11-29.
 */

public class Downloader {
    private static String tag = Downloader.class.getSimpleName();
    private static int flag = 9;

    /**
     * 打印源码
     *
     * @param urls
     * @throws IOException
     */
    public static void printf(String urls) throws IOException {
        URL url = new URL(urls);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); //请求方式 get
        connection.setConnectTimeout(5000); //连接超时时间5s
        connection.setReadTimeout(10000); //读取超时时间10s

        int res = connection.getResponseCode();
        String codes = null;
        if (res == 200) {
            InputStream in = connection.getInputStream();
            codes = StreamUtils.stream2String(in);
        }
        System.out.println(codes);
    }

    /**
     * 将文件下载到本地
     *
     * @param urls     下载的url
     * @param filePath 下载路径
     * @throws IOException
     */
    public static void download(String title, String urls, String filePath) throws IOException {
        Log.i(tag, filePath.substring(filePath.lastIndexOf("/") + 1) + " 开始下载!");
        URL url = new URL(urls);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); //请求方式 get
        connection.setConnectTimeout(5000); //连接超时时间5s

        int res = connection.getResponseCode();
        String codes;
        if (res == 200) {
            InputStream in = connection.getInputStream();
            codes = StreamUtils.stream2String(in);
            System.out.println(title + ">>>>>>" + codes);
            StreamUtils.stream2File(codes, filePath);
        }
        synchronized (MainActivity.class) {
            flag--;
            if (flag == 0) {
                Log.i(tag, "所有资源下载完成！");
            }
        }
    }
}

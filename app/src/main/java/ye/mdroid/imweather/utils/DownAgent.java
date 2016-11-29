package ye.mdroid.imweather.utils;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

/**
 * Created by ye on 16-11-29.
 */

public class DownAgent {
    private String tag = DownAgent.class.getSimpleName();
    private String urls = "https://free-api.heweather.com/v5/";
    private String keys = "c832c5f36ea8477bbdcf79d41371065c";

    private Context context;


    public DownAgent(Context context) {
        this.context = context;
    }

    //下载7-10天预报
    public void test01() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "forecast?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
//        System.out.println(urls);
        //Downloader.printf(urls);
        Downloader.download("7-10", urls, this.context.getExternalFilesDir("down") + "/forecast_huxian.json");
    }


    //2.下载实况天气 /now     city key lang
    public void test02() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "now?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
//        System.out.println(urls);
        //Downloader.printf(urls);
        Downloader.download("实况天气", urls, this.context.getExternalFilesDir("down") + "/now_huxian.json");
    }

    //3.下载每小时预报 /hourly     city key lang
    public void test03() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "hourly?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
//        System.out.println(urls);
        //Downloader.printf(urls);
        Downloader.download("每小时预报", urls, this.context.getExternalFilesDir("down") + "/hourly_huxian.json");
    }

    //4.下载生活指数 /suggestion      city key
    public void test04() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "suggestion?";
        urls += "city=huxian";
        urls += "&key=" + keys;
//        System.out.println(urls);
        //Downloader.printf(urls);
        Downloader.download("生活指数", urls, this.context.getExternalFilesDir("down") + "/suggestion_huxian.json");
    }

    //5.下载灾害预警 /alarm      city key
    public void test05() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "alarm?";
        urls += "city=huxian";
        urls += "&key=" + keys;
//        System.out.println(urls);
        //Downloader.printf(urls);
        Downloader.download("灾害预警", urls, this.context.getExternalFilesDir("down") + "/alarm_huxian.json");
    }

    //6.下载天气预报集合接口 /weather     city key lang
    public void test06() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "weather?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
//        System.out.println(urls);
        //Downloader.printf(urls);
        Downloader.download("集合接口", urls, this.context.getExternalFilesDir("down") + "/weather_huxian.json");
    }

    //7.下载景点天气 /scenic      city key lang
    public void test07() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "scenic?";
        urls += "city=CN101110106"; //仅支持id
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
//        System.out.println(urls);
        //Downloader.printf(urls);
        Downloader.download("景点天气", urls, this.context.getExternalFilesDir("down") + "/scenic_huxian.json");
    }

    //8.下载历史天气 /historical        city date   key   lang
    public void test08() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "historical?";
        urls += "city=CN101010100";
        urls += "&date=2016-10-28";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
//        System.out.println(urls);
        //Downloader.printf(urls);
        Downloader.download("历史天气16-10-28", urls, this.context.getExternalFilesDir("down") + "/historical_huxian.json");
    }

    //9.下载城市查询 /search      city key
    public void test09() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "search?";
        urls += "city=huxian";
        urls += "&key=" + keys;
//        System.out.println(urls);
        //Downloader.printf(urls);
        Downloader.download("城市查询", urls, this.context.getExternalFilesDir("down") + "/search_huxian.json");
    }


    public void exec() {
        try {
            Log.i(tag, "下载开始...");
            test01();
            test02();
            test03();
            test04();
            test05();
            test06();
            test07();
            test08();
            test09();
        } catch (IOException e) {
            Log.i(tag, "agent ex >>>" + e.toString());
            return;
        }
    }
}

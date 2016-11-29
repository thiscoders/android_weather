package ye.mdroid.imweather.utils;

import android.content.Context;

import java.io.IOException;

/**
 * Created by ye on 16-11-29.
 */

public class DownAgent {
    private String TAG = DownAgent.class.getSimpleName();
    private String urls = "https://free-api.heweather.com/v5/";
    private String keys = "c832c5f36ea8477bbdcf79d41371065c";

    private Context context;

    private String downPath;


    public DownAgent(Context context) {
        this.context = context;
        downPath = this.context.getExternalFilesDir("down") + "";
    }

    //下载7-10天预报
    public void test01() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "forecast?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
        Downloader.download("7-10", urls, downPath + "/forecast_huxian.json");
    }


    //2.下载实况天气 /now     city key lang
    public void test02() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "now?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
        Downloader.download("实况天气", urls, downPath + "/now_huxian.json");
    }

    //3.下载每小时预报 /hourly     city key lang
    public void test03() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "hourly?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
        Downloader.download("每小时预报", urls, downPath + "/hourly_huxian.json");
    }

    //4.下载生活指数 /suggestion      city key
    public void test04() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "suggestion?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        Downloader.download("生活指数", urls, downPath + "/suggestion_huxian.json");
    }

    //5.下载灾害预警 /alarm      city key
    public void test05() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "alarm?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        Downloader.download("灾害预警", urls, downPath + "/alarm_huxian.json");
    }

    //6.下载天气预报集合接口 /weather     city key lang
    public void test06() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "weather?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
        Downloader.download("集合接口", urls, downPath + "/weather_huxian.json");
    }

    //7.下载景点天气 /scenic      city key lang
    public void test07() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "scenic?";
        urls += "city=CN101110106"; //仅支持id
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
        Downloader.download("景点天气", urls, downPath + "/scenic_huxian.json");
    }

    //8.下载历史天气 /historical        city date   key   lang
    public void test08() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "historical?";
        urls += "city=CN101010100";
        urls += "&date=2016-10-28";
        urls += "&key=" + keys;
        urls += "&lang=zh_cn";
        Downloader.download("历史天气16-10-28", urls, downPath + "/historical_huxian.json");
    }

    //9.下载城市查询 /search      city key
    public void test09() throws IOException {
        urls = "https://free-api.heweather.com/v5/";
        urls += "search?";
        urls += "city=huxian";
        urls += "&key=" + keys;
        Downloader.download("城市查询", urls, downPath + "/search_huxian.json");
    }


    public void exec(int i) throws IOException {
        switch (i) {
            case 1:
                test01();
                break;
            case 2:
                test02();
                break;
            case 3:
                test03();
                break;
            case 4:
                test04();
                break;
            case 5:
                test05();
                break;
            case 6:
                test06();
                break;
            case 7:
                test07();
                break;
            case 8:
                test08();
                break;
            case 9:
                test09();
                break;
        }
    }
}

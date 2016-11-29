package ye.mdroid.imweather.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import ye.mdroid.imweather.R;
import ye.mdroid.imweather.cvs.ChartView2;
import ye.mdroid.imweather.utils.DownAgent;
import ye.mdroid.imweather.utils.Downloader;
import ye.mdroid.imweather.utils.StreamUtils;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private final int MENU_UPDATE = 1;
    private final int MENU_PARSER = 2;
    private final int MENU_ABOUT = 3;
    private ProgressBar pb_ing;
    private AsyncDowner downer;

    private ImageView iv_now_cond_code;
    private TextView tv_now_citys;
    private TextView tv_now_fl;
    private TextView tv_now_hum;
    private TextView tv_now_pcpn;
    private TextView tv_now_vis;
    private TextView tv_now_winddict;
    private TextView tv_now_windlev;
    private TextView tv_now_loc;
    private TextView tv_now_windspe;
    private TextView tv_now_cond_txt;
    private TextView tv_now_pres;

    private ChartView2 cv2_fc;

    private TextView tv_sug_air_lev;
    private TextView tv_sug_comf_lev;
    private TextView tv_sug_cw_lev;
    private TextView tv_sug_drsg_lev;
    private TextView tv_sug_flu_lev;
    private TextView tv_sug_sport_lev;
    private TextView tv_sug_trav_lev;
    private TextView tv_sug_uv_lev;

    private TextView tv_sug_air;
    private TextView tv_sug_comf;
    private TextView tv_sug_cw;
    private TextView tv_sug_drsg;
    private TextView tv_sug_flu;
    private TextView tv_sug_sport;
    private TextView tv_sug_trav;
    private TextView tv_sug_uv;

    /*private TextView tv_alarm_title;
    private TextView tv_alarm_level;
    private TextView tv_alarm_stat;
    private TextView tv_alarm_txt;
    private TextView tv_alarm_type;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_ing = (ProgressBar) findViewById(R.id.pb_ing);
        iv_now_cond_code = (ImageView) findViewById(R.id.iv_now_cond_code);
        tv_now_citys = (TextView) findViewById(R.id.tv_now_citys);
        tv_now_fl = (TextView) findViewById(R.id.tv_now_fl);
        tv_now_loc = (TextView) findViewById(R.id.tv_now_loc);
        tv_now_hum = (TextView) findViewById(R.id.tv_now_hum); //湿度
        tv_now_pcpn = (TextView) findViewById(R.id.tv_now_pcpn); //降水量
        tv_now_vis = (TextView) findViewById(R.id.tv_now_vis); //能见度
        tv_now_winddict = (TextView) findViewById(R.id.tv_now_winddict); //风向
        tv_now_windlev = (TextView) findViewById(R.id.tv_now_windlev); //等级
        tv_now_windspe = (TextView) findViewById(R.id.tv_now_windspe); //等级
        tv_now_cond_txt = (TextView) findViewById(R.id.tv_now_cond_txt); //天气状况描述
        tv_now_pres = (TextView) findViewById(R.id.tv_now_pres); //气压
        cv2_fc = (ChartView2) findViewById(R.id.cv2_fc);


        tv_sug_air_lev = (TextView) findViewById(R.id.tv_sug_air_lev);
        tv_sug_comf_lev = (TextView) findViewById(R.id.tv_sug_comf_lev);
        tv_sug_cw_lev = (TextView) findViewById(R.id.tv_sug_cw_lev);
        tv_sug_drsg_lev = (TextView) findViewById(R.id.tv_sug_drsg_lev);
        tv_sug_flu_lev = (TextView) findViewById(R.id.tv_sug_flu_lev);
        tv_sug_sport_lev = (TextView) findViewById(R.id.tv_sug_sport_lev);
        tv_sug_trav_lev = (TextView) findViewById(R.id.tv_sug_trav_lev);
        tv_sug_uv_lev = (TextView) findViewById(R.id.tv_sug_uv_lev);

        tv_sug_air = (TextView) findViewById(R.id.tv_sug_air);
        tv_sug_comf = (TextView) findViewById(R.id.tv_sug_comf);
        tv_sug_cw = (TextView) findViewById(R.id.tv_sug_cw);
        tv_sug_drsg = (TextView) findViewById(R.id.tv_sug_drsg);
        tv_sug_flu = (TextView) findViewById(R.id.tv_sug_flu);
        tv_sug_sport = (TextView) findViewById(R.id.tv_sug_sport);
        tv_sug_trav = (TextView) findViewById(R.id.tv_sug_trav);
        tv_sug_uv = (TextView) findViewById(R.id.tv_sug_uv);


        /*tv_alarm_title = (TextView) findViewById(R.id.tv_alarm_title);
        tv_alarm_level = (TextView) findViewById(R.id.tv_alarm_level);
        tv_alarm_stat = (TextView) findViewById(R.id.tv_alarm_stat);
        tv_alarm_txt = (TextView) findViewById(R.id.tv_alarm_txt);
        tv_alarm_type = (TextView) findViewById(R.id.tv_alarm_type);*/

        downer = new AsyncDowner();

        downImages();

        parserIt();
    }

    private void parserIt() {
        parserNow();
        parserForecast();
        parserSuggest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_UPDATE, 0, "刷新数据");
        menu.add(0, MENU_PARSER, 0, "解析数据");
        menu.add(0, MENU_ABOUT, 0, "关于软件");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case MENU_UPDATE:
                // TODO: 16-11-28  刷新数据以后可以通过下拉刷新实现
                if (downer != null && downer.getStatus() == AsyncTask.Status.RUNNING) {
                    downer.cancel(true);//设置异步任务停止标记
                }
                new AsyncDowner().execute();
                break;
            case MENU_PARSER:
                parserNow();
                parserForecast();
                parserSuggest();
                break;
            case MENU_ABOUT:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //解析数据
    public void parserNow() {
        new Thread() {
            @Override
            public void run() {
                File file = new File(MainActivity.this.getExternalFilesDir("down"), "now_huxian.json");
                try {
                    String content = StreamUtils.file2String(file);
                    JSONObject object = new JSONObject(content);

                    JSONArray nowf = object.getJSONArray("HeWeather5");
                    JSONObject now = (JSONObject) nowf.get(0);
                    final String status = now.getString("status");
                    if (!status.equals("ok")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "获取实况天气失败！" + status, Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    JSONObject basic = now.getJSONObject("basic");
                    final String city = basic.getString("city");
                    JSONObject update = basic.getJSONObject("update");
                    final String loc = "更新时间:" + update.getString("loc");

                    JSONObject nows = now.getJSONObject("now");
                    JSONObject cond = nows.getJSONObject("cond");
                    final String code = cond.getString("code");
                    final String txt = cond.getString("txt") + "/";

                    final String fl = nows.getString("fl") + "℃\t\t";//体感温度(摄氏度)
                    final String hum = "相对湿度:" + nows.getString("hum") + "%\t\t";//相对湿度(%)
                    final String pcpn = "降水量:" + nows.getString("pcpn") + "mm\t\t";//降水量(mm)
                    final String pres = "气压:" + nows.getString("pres") + "pa\t\t";//气压
//                    final String tmp = "温度:" + nows.getString("tmp") + "摄氏度\t\t";//温度
                    final String vis = "能见度:" + nows.getString("vis") + "\t\t";//能见度
                    JSONObject wind = nows.getJSONObject("wind");
//                    final String deg = "风向(360):" + wind.getString("deg") + "\t\t";  //风向(360)
                    final String dir = "风向:" + wind.getString("dir") + "\t\t";//风向
                    final String sc = "等级:" + wind.getString("sc") + "\t\t"; //风力等级
                    final String spd = "风速:" + wind.getString("spd") + "kmph\t\t"; //风速(kmph)

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_now_citys.setText("\t" + city);
                            tv_now_loc.setText(loc);
                            tv_now_fl.setText("(" + fl + ")");
                            tv_now_hum.setText(hum);
                            tv_now_pcpn.setText(pcpn);
                            tv_now_pres.setText(pres);
                            tv_now_vis.setText(vis);
                            tv_now_cond_txt.setText(txt);
                            tv_now_winddict.setText("\t" + dir);
                            tv_now_windlev.setText(sc);
                            tv_now_windspe.setText(spd);
                            Bitmap srcBitmap = BitmapFactory.decodeFile(MainActivity.this.getExternalFilesDir("images") + "/w" + code + ".png");
                            handleNowCond(srcBitmap);

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //解析7-10天预报
    public void parserForecast() {
        File file = new File(this.getExternalFilesDir("down"), "forecast_huxian.json");
        String content = null;
        try {
            content = StreamUtils.file2String(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content == null || content.equals("")) {
            Toast.makeText(MainActivity.this, "资源未下载,请点击刷新数据!", Toast.LENGTH_LONG).show();
            return;
        }
        cv2_fc.mfunc(content);
    }

    //解析生活指数
    public void parserSuggest() {
        final File file = new File(MainActivity.this.getExternalFilesDir("down"), "suggestion_huxian.json");
        new Thread() {
            @Override
            public void run() {
                try {
                    String content = StreamUtils.file2String(file);
                    if (content == null || content.equals("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "资源未下载,请点击刷新数据!", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }
                    JSONObject object = new JSONObject(content);
                    JSONArray weath = object.getJSONArray("HeWeather5");
                    JSONObject fath = (JSONObject) weath.get(0);

                    final String status = fath.getString("status");
                    if (!status.equals("ok")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "获取生活指数失败！" + status, Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }

                    JSONObject sugg = fath.getJSONObject("suggestion");

                    JSONObject air = sugg.getJSONObject("air");
                    final String airbrf = air.getString("brf");
                    final String airtxt = air.getString("txt");
                    JSONObject comf = sugg.getJSONObject("comf");
                    final String comfbrf = comf.getString("brf");
                    final String comftxt = comf.getString("txt");
                    JSONObject cw = sugg.getJSONObject("cw");
                    final String cwbrf = cw.getString("brf");
                    final String cwtxt = cw.getString("txt");
                    JSONObject drsg = sugg.getJSONObject("drsg");
                    final String drsgbrf = drsg.getString("brf");
                    final String drsgtxt = drsg.getString("txt");
                    JSONObject flu = sugg.getJSONObject("flu");
                    final String flubrf = flu.getString("brf");
                    final String flutxt = flu.getString("txt");
                    JSONObject sport = sugg.getJSONObject("sport");
                    final String sportbrf = sport.getString("brf");
                    final String sporttxt = sport.getString("txt");
                    JSONObject trav = sugg.getJSONObject("trav");
                    final String travbrf = trav.getString("brf");
                    final String travtxt = trav.getString("txt");
                    JSONObject uv = sugg.getJSONObject("uv");
                    final String uvbrf = uv.getString("brf");
                    final String uvtxt = uv.getString("txt");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tv_sug_air_lev.setText("※空气指数(" + airbrf + "):");
                            tv_sug_air.setText("\t\t" + airtxt);
                            tv_sug_comf_lev.setText("※舒适度指数(" + comfbrf + "):");
                            tv_sug_comf.setText("\t\t" + comftxt);
                            tv_sug_cw_lev.setText("※洗车指数(" + cwbrf + "):");
                            tv_sug_cw.setText("\t\t" + cwtxt);
                            tv_sug_drsg_lev.setText("※穿衣指数(" + drsgbrf + "):");
                            tv_sug_drsg.setText("\t\t" + drsgtxt);
                            tv_sug_flu_lev.setText("※感冒指数(" + flubrf + "):");
                            tv_sug_flu.setText("\t\t" + flutxt);
                            tv_sug_sport_lev.setText("※运动指数(" + sportbrf + "):");
                            tv_sug_sport.setText("\t\t" + sporttxt);
                            tv_sug_trav_lev.setText("※旅游指数(" + travbrf + "):");
                            tv_sug_trav.setText("\t\t" + travtxt);
                            tv_sug_uv_lev.setText("※紫外线指数(" + uvbrf + "):");
                            tv_sug_uv.setText("\t\t" + uvtxt);
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        ;
    }

    //解析灾害预警
    public void parserAlarm() {
        final File file = new File(MainActivity.this.getExternalFilesDir("down"), "alarm_huxian.json");
        new Thread() {
            @Override
            public void run() {
                try {


                    String content = StreamUtils.file2String(file);
                    if (content == null || content.equals("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "资源未下载,请点击刷新数据!", Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }
                    JSONObject object = new JSONObject(content);
                    JSONArray weath = object.getJSONArray("HeWeather5");
                    JSONObject fath = (JSONObject) weath.get(0);

                    final String status = fath.getString("status");
                    if (!status.equals("ok")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "获取生活指数失败！" + status, Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    //设置图片
    private void handleNowCond(Bitmap srcBitmap) {
       /* Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        Canvas canvas = new Canvas(copyBitmap);

        Paint paint = new Paint();

        Matrix matrix = new Matrix();

        matrix.setScale(1.1f, 1.1f);

        canvas.drawBitmap(srcBitmap, matrix, paint);*/

        iv_now_cond_code.setImageBitmap(srcBitmap);
    }

    //判断图片是否下载到本地，如果未下载就开始下载
    private void downImages() {
        File file = new File(MainActivity.this.getExternalFilesDir("images") + "w999.png");
        if (file.exists()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    new DownAgent(MainActivity.this, new Downloader()).test10();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private class AsyncDowner extends AsyncTask<Void, Void, Void> {
        private boolean isok = false;
        private Downloader downloader;
        private DownAgent agent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloader = new Downloader();
            pb_ing.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            agent = new DownAgent(MainActivity.this, downloader);
            try {
                for (int i = 1; i < 10; i++) {
                    if (isCancelled()) { //获取状态，完成异步任务
                        return null;
                    }
                    agent.exec(i);
                }
            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请检查网络!!!", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }
            isok = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pb_ing.setVisibility(View.GONE);
            if (isok) {
                Toast.makeText(MainActivity.this, "所有资源下载完成！", Toast.LENGTH_SHORT).show();
                parserIt();
            }
        }
    }

}

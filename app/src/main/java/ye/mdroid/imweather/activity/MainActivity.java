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
    private ChartView2 cv2_fc;

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


    private AsyncDowner downer;

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

        downer = new AsyncDowner();

        parserNow();
        parserForecast();
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
                    String status = now.getString("status");
                    Log.i(TAG, "status..." + status);
                    if (!status.equals("ok")) {
                        Log.e(TAG, "实况天气数据获取失败！");
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
                            tv_now_citys.setText(city);
                            tv_now_fl.setText(fl);
                            tv_now_hum.setText(hum);
                            tv_now_pcpn.setText(pcpn);
                            tv_now_pres.setText(pres);
                            tv_now_vis.setText(vis);
                            tv_now_cond_txt.setText(txt);
                            tv_now_winddict.setText(dir);
                            tv_now_windlev.setText(sc);
                            tv_now_windspe.setText(spd);
                            tv_now_loc.setText(loc);
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
        cv2_fc.mfunc(content);
    }

    private void handleNowCond(Bitmap srcBitmap) {
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());

        Canvas canvas = new Canvas(copyBitmap);

        Paint paint = new Paint();

        Matrix matrix = new Matrix();

        matrix.setScale(1.1f, 1.1f);

        canvas.drawBitmap(srcBitmap, matrix, paint);

        iv_now_cond_code.setImageBitmap(srcBitmap);
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
            if (isok)
                Toast.makeText(MainActivity.this, "所有资源下载完成！", Toast.LENGTH_SHORT).show();
        }
    }

}

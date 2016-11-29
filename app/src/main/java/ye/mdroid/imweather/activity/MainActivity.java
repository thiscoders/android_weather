package ye.mdroid.imweather.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import ye.mdroid.imweather.R;
import ye.mdroid.imweather.cvs.ChartView2;
import ye.mdroid.imweather.utils.DownAgent;
import ye.mdroid.imweather.utils.Downloader;
import ye.mdroid.imweather.utils.StreamUtils;

public class MainActivity extends AppCompatActivity {
    private final int MENU_UPDATE = 1;
    private final int MENU_ABOUT = 2;

    private ProgressBar pb_ing;
    private ChartView2 cv2_forecast;

    private AsyncDowner downer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_ing = (ProgressBar) findViewById(R.id.pb_ing);

        downer = new AsyncDowner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_UPDATE, 0, "刷新数据");
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
                    downer.cancel(true);
                }
                new AsyncDowner().execute();
                break;
            case MENU_ABOUT:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
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
                    if (isCancelled()) {
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

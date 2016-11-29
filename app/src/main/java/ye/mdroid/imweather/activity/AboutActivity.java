package ye.mdroid.imweather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import ye.mdroid.imweather.R;
import ye.mdroid.imweather.utils.DownAgent;
import ye.mdroid.imweather.utils.Downloader;

/**
 * Created by ye on 16-11-28.
 */

public class AboutActivity extends AppCompatActivity {

    private boolean upres = false;
    private boolean agupres = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void upres(View view) {
        Toast.makeText(AboutActivity.this, "八阿哥来了，走着瞧吧!", Toast.LENGTH_SHORT).show();
        //下载图标
        new Thread() {
            @Override
            public void run() {
                try {
                    if (!upres) {
                        new DownAgent(AboutActivity.this, new Downloader()).test10();
                        upres = true;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!agupres) {
                                Toast.makeText(AboutActivity.this, "图标已经下载完成!", Toast.LENGTH_SHORT).show();
                                agupres = true;
                                return;
                            }
                            Toast.makeText(AboutActivity.this, "图标已经存在!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

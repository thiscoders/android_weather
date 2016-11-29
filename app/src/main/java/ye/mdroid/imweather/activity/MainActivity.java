package ye.mdroid.imweather.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ye.mdroid.imweather.R;

public class MainActivity extends AppCompatActivity {
    private final int MENU_UPDATE = 1;
    private final int MENU_CHOOSEC = 2;
    private final int MENU_ABOUT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_UPDATE, 0, "刷新数据");
        menu.add(0, MENU_CHOOSEC, 0, "选择城市");
        menu.add(0, MENU_ABOUT, 0, "关于软件");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case MENU_UPDATE:
                // TODO: 16-11-28  刷新数据以后可以通过下拉刷新实现 
                Toast.makeText(MainActivity.this, "刷新数据", Toast.LENGTH_SHORT).show();
                break;
            case MENU_CHOOSEC:
                intent = new Intent(MainActivity.this, CitysActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case MENU_ABOUT:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void click1(View view) {
        Toast.makeText(MainActivity.this, "lalal", Toast.LENGTH_SHORT).show();
    }
}

package ye.mdroid.imweather.cvs;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ye.mdroid.imweather.R;
import ye.mdroid.imweather.activity.MainActivity;
import ye.mdroid.imweather.domain.MPoint;
import ye.mdroid.imweather.utils.MDateUtils;

/**
 * Created by ye on 16-11-29.
 */

public class ChartView2 extends View {
    private final String TAG = ChartView2.class.getSimpleName();

    private int mbgcolor;
    private int mmaxcolor;
    private int mmincolor;

    private Paint bgpaint;
    private Paint maxpaint;
    private Paint minpaint;
    private Paint tpaint;
    private Paint ppaint;

    private List<MPoint> maxList;
    private List<MPoint> minList;
    private String[] cweeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};


    public ChartView2(Context context) {
        this(context, null);
    }

    public ChartView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources.Theme theme = context.getTheme();
        TypedArray array = theme.obtainStyledAttributes(attrs, R.styleable.ChartView2, defStyleAttr, 0);
        if (array == null) {
            Log.i(TAG, "传递的参数为null，控件终止绘画！");
            return;
        }
        for (int i = 0; i < array.getIndexCount(); i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.ChartView2_mbgcolor:
                    mbgcolor = array.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.ChartView2_mmaxcolor:
                    mmaxcolor = array.getColor(attr, Color.YELLOW);
                    break;
                case R.styleable.ChartView2_mmincolor:
                    mmincolor = array.getColor(attr, Color.BLUE);
                    break;
            }
        }
        array.recycle();//回收资源
        init();
    }

    private void init() {
        bgpaint = new Paint();
        bgpaint.setColor(mbgcolor);

        maxpaint = new Paint();
        maxpaint.setStrokeWidth(5);
        maxpaint.setColor(mmaxcolor);

        minpaint = new Paint();
        minpaint.setStrokeWidth(5);
        minpaint.setColor(mmincolor);

        tpaint = new Paint();
        tpaint.setColor(Color.BLUE);
        tpaint.setTextSize(20);

        ppaint = new Paint();
        ppaint.setTextSize(20);

        maxList = new ArrayList<MPoint>();
        minList = new ArrayList<MPoint>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgpaint);
        if (maxList.size() == 0 || minList.size() == 0) {
            Log.i(TAG, "列表为空！");
            return;
        }
        float start = 111;
        float loop = 140;
        float end;

        //画最大温度的曲线 //1062...450 --->5
        for (int i = 0; i < maxList.size() - 1; i++) {
            end = start + loop;
            canvas.drawLine(start, (float) 300 - maxList.get(i).getMy(), end, (float) 300 - maxList.get(i + 1).getMy(), maxpaint);
            canvas.drawCircle(start, (float) 300 - maxList.get(i).getMy(), 3, tpaint);
            canvas.drawText((maxList.get(i).getMy() / 20) + "℃", start - 20, (float) 300 - maxList.get(i).getMy() - 5, tpaint);
            canvas.drawLine(start, 360, start, (float) 300 - maxList.get(i).getMy(), tpaint);//画竖直线
            start = end;
        }
        canvas.drawCircle(start, (float) 300 - maxList.get(6).getMy(), 3, tpaint);
        canvas.drawText((maxList.get(6).getMy() / 20) + "℃", start - 10, (float) 300 - maxList.get(6).getMy() - 5, tpaint);
        canvas.drawLine(start, 360, start, (float) 300 - maxList.get(6).getMy(), tpaint);//画竖直线

        //画最小温度曲线
        start = 111; //start归位
        for (int i = 0; i < minList.size() - 1; i++) {
            end = start + loop;
            canvas.drawLine(start, (float) 300 - minList.get(i).getMy(), end, (float) 300 - minList.get(i + 1).getMy(), minpaint);
            canvas.drawCircle(start, (float) 300 - minList.get(i).getMy(), 3, tpaint);
            canvas.drawText((minList.get(i).getMy() / 20) + "℃", start - 20, (float) 300 - minList.get(i).getMy() - 5, tpaint);
            start = end;
        }
        canvas.drawCircle(start, (float) 300 - minList.get(6).getMy(), 3, tpaint);
        canvas.drawText((minList.get(6).getMy() / 20) + "℃", start - 10, (float) 300 - minList.get(6).getMy() - 5, tpaint);

        //写出周
        tpaint.setColor(Color.BLACK);
        start = 108; //start归位
        int windex = MDateUtils.getWeeksIndex();
        for (int i = windex; i < 7; i++) {
            if (i == windex) {
                canvas.drawText("今天", start - 18, 380, tpaint);
                start += loop;
                continue;
            }
            canvas.drawText(cweeks[i], start - 18, 380, tpaint);
            start += loop;
        }
        for (int i = 0; i < windex; i++) {
            canvas.drawText(cweeks[i], start - 18, 380, tpaint);
            start += loop;
        }

        ppaint.setStrokeWidth(5);
        ppaint.setColor(mmaxcolor);
        canvas.drawLine(20, 20, 130, 20, ppaint);
        canvas.drawText("max", 138, 26, ppaint);
        ppaint.setColor(mmincolor);
        canvas.drawLine(20, 50, 130, 50, ppaint);
        canvas.drawText("min", 138, 56, ppaint);
        ppaint.setColor(Color.BLACK);
        canvas.drawPoint(20, 20, ppaint);
        canvas.drawPoint(130, 20, ppaint);
        canvas.drawPoint(20, 50, ppaint);
        canvas.drawPoint(130, 50, ppaint);

    }

    public void mfunc(String jsonContent) {
        try {
            JSONObject jsonObject = new JSONObject(jsonContent);

            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");

            JSONObject object = (JSONObject) jsonArray.get(0);

            String status = object.getString("status");

            if (!status.equals("ok")) {
                Log.e(TAG, "7-10天天气预测请求数据出错！");
                return;
            }

            JSONObject basic = object.getJSONObject("basic");

            JSONArray daily_forecast = object.getJSONArray("daily_forecast");

            //清空数据
            maxList.clear();
            minList.clear();
            MPoint maxps;
            MPoint minps;
            for (int i = 0; i < daily_forecast.length(); i++) {
                JSONObject daily = (JSONObject) daily_forecast.get(i);
                //温度
                JSONObject tmp = daily.getJSONObject("tmp");
                String max = tmp.getString("max");
                String min = tmp.getString("min");
                maxps = new MPoint();
                maxps = new MPoint();
                maxps.setMy(Integer.parseInt(max) * 15);
                maxList.add(maxps);

                minps = new MPoint();
                minps = new MPoint();
                minps.setMy(Integer.parseInt(min) * 15);
                minList.add(minps);
            }
            invalidate();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

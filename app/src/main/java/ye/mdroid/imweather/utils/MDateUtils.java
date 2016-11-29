package ye.mdroid.imweather.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ye on 16-11-29.
 */

public class MDateUtils {
    private static String[] cweeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    private static String[] eweeks = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    /**
     * 获取当前星期的索引
     *
     * @return
     */
    public static int getWeeksIndex() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String week = sdf.format(new Date());
        for (int i = 0; i < 7; i++) {
            if (week.equals(eweeks[i]) || week.equals(cweeks[i])) {
                return i;
            }
        }
        return -1;
    }

}

package could.bluepay.renyumvvm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bluepay on 2017/11/24.
 */

public class TimeUtils {

    public static String getMonthDayDate(String time) {
        String monthDayTime = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("M月d日");
        try {
            Date dateTime = formatter.parse(time);
            monthDayTime = formatter1.format(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return monthDayTime;
    }
}

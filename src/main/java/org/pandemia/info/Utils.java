package org.pandemia.info;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public static Calendar nowCalendar = Calendar.getInstance();
    public static String nowDate = formatDate(nowCalendar);

    public static String formatDate(Calendar calendar) {
        return formatDate(calendar.getTime());
    }

    public static String formatDate(Date date) {
        return formatter.format(date);
    }

    public static String formatNowDate() {
        return nowDate;
    }

    public static Calendar nowIncremented(int days) {
        Calendar calendar1 = (Calendar) nowCalendar.clone();
        calendar1.add(Calendar.DAY_OF_MONTH, days);
        return calendar1;
    }

    public static Calendar nowDecremented(int days) {
        Calendar calendar1 = (Calendar) nowCalendar.clone();
        calendar1.add(Calendar.DAY_OF_MONTH, -days);
        return calendar1;
    }

    public static String nowIncrementedFormat(int days) {
        return formatDate(nowIncremented(days));
    }

    public static String nowDecrementedFormat(int days) {
        return formatDate(nowDecremented(days));
    }
}

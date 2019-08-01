package am.highapps.theguardiannews.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeHelper {

    public static String parsToStringDate(String date) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            Date dataToIncrease = sdf.parse(date);
            sdf.applyPattern(outputPattern);
            return sdf.format(dataToIncrease);
        } catch (ParseException e) {
            return "";
        }
    }

    public static Date parseToDate(String inputDate) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            return format.parse(inputDate);
        } catch (ParseException e) {
            return null;
        }
    }
}

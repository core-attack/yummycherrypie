package yummycherrypie.pl;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by CoreAttack on 01.12.2015.
 */
public class DateExtension {

    public static final String[] MONTHS = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
            "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    public static String getDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd._.yyyy");
        String sdate = sdf.format(date);
        Date d = new Date(date);
        int month = d.getMonth() + 1;
        String newDate = sdate.replace("_", String.valueOf(month < 10 ? "0" + month : month));
        return newDate;
    }

    public static String getDate(int day, int month, int year){
        return String.format("%02d.%02d.%02d", day, month, year);
    }

    public static String getDateString(long date){
        Format format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(new Date(date));
    }

    public static Date getDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = new Date();
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static long getDateLong(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = new Date();
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    public static long getDateLong(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy");
        Date d = new Date();
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    public static String getDateTime(long dateTime){
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = new Date(dateTime);
        return format.format(date);
    }

    public static long getDateTime(String dateTime){
        try {
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);
            return format.parse(dateTime).getTime();
        }
        catch (ParseException pe){
            return -1;
        }
    }

    public static String getDateTime(long dateTime, DateFormat format){
        Date date = new Date(dateTime);
        return format.format(date);
    }

    public static long getDateTime(String dateTime, DateFormat format){
        try {
            return format.parse(dateTime).getTime();
        }
        catch (ParseException pe){
            return -1;
        }
    }

    public static String getTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        return sdf.format(time);
    }

    public static long getTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date d = new Date();
        try {
            d = sdf.parse(time);
        } catch (ParseException e) {
            return -1;
        }
        return d.getTime();
    }

    public static Date convertToDate(String dateString, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}

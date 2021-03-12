package yummycherrypie.pl.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.default_records.BookingTypesDefaultRecords;
import yummycherrypie.dal.repositories.BookingTypeRepository;
import yummycherrypie.system.R;
import yummycherrypie.pl.Colors;
import yummycherrypie.pl.DateExtension;

/**
 * Created by piskarev on 11.09.2015.
 */
public class CalendarAdapter extends BaseAdapter {

    public String[] days;

    private static final int FIRST_DAY_OF_WEEK = 1; // Sunday = 0, Monday = 1

    private DBHelper dbHelper;
    private BookingTypeRepository dbr;

    private Context mContext;

    private java.util.Calendar calendar;
    private Calendar selectedDate;
    private ArrayList<String> items;

    public CalendarAdapter(Context c, Calendar monthCalendar) {
        dbHelper = new DBHelper(c);
        dbr = new BookingTypeRepository(dbHelper);

        calendar = monthCalendar;
        selectedDate = (Calendar)monthCalendar.clone();
        mContext = c;
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<String>();
        refreshDays();
    }

    public int getCount() {
        return days.length;
    }

    public Object getItem(int position) {
        return days[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_calendar, null);
        }

        v.setLayoutParams(new GridView.LayoutParams(101, 101));

        TextView dayView = (TextView) v.findViewById(R.id.date);
        TextView fullDayView = (TextView) v.findViewById(R.id.fulldate);

        Calendar currentDay = Calendar.getInstance();

        if(days[position].equals("")) {
            dayView.setClickable(false);
            dayView.setFocusable(false);
        }
        else {
            Calendar curDay = Calendar.getInstance();
            curDay.setTime(DateExtension.getDate(days[position]));
            dayView.setTextColor(Colors.COLOR_DEFAULT_DAY_TEXT);

            ImageView ivCake = (ImageView) v.findViewById(R.id.icon_cake);
            ImageView ivCookie = (ImageView) v.findViewById(R.id.icon_cookies);
            ImageView ivCupcake = (ImageView) v.findViewById(R.id.icon_cupcake);
            ImageView ivCakepops = (ImageView) v.findViewById(R.id.icon_cakepops);
            ImageView ivPastry = (ImageView) v.findViewById(R.id.icon_pastry);
            ImageView ivMarriage = (ImageView) v.findViewById(R.id.icon_marriage);
            ImageView ivSpiceCake = (ImageView) v.findViewById(R.id.icon_spice_cake);

            currentDay.setTime(DateExtension.getDate(days[position]));

            if (currentDay.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                v.setBackgroundColor(Colors.COLOR_DEFAULT_DAY_BCKG);
            else {
                v.setBackgroundColor(Colors.COLOR_DEFAULT_DAY_BCKG_NOT_THIS_MONTH);
                ivCake.setAlpha(100);
                ivCookie.setAlpha(100);
                ivCupcake.setAlpha(100);
                ivCakepops.setAlpha(100);
                ivPastry.setAlpha(100);
                ivMarriage.setAlpha(100);
                ivSpiceCake.setAlpha(100);
            }

            if (curDay.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
                    && curDay.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                    && curDay.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                v.setBackgroundColor(Colors.COLOR_CURRENT_DAY);
            }

            byte firstField = 0;
            ivCookie.setVisibility(View.INVISIBLE);
            ivPastry.setVisibility(View.INVISIBLE);

            byte secondField = 0;
            ivCake.setVisibility(View.INVISIBLE);
            ivCakepops.setVisibility(View.INVISIBLE);

            byte thirdField = 0;
            ivCupcake.setVisibility(View.INVISIBLE);
            ivMarriage.setVisibility(View.INVISIBLE);
            ivSpiceCake.setVisibility(View.INVISIBLE);

            boolean isSecondInThirdFieldVisible = false;

            if (dbr.showIconInCalendar(currentDay.getTimeInMillis(), BookingTypesDefaultRecords.BOOKING_TYPE_COOKIE_ID)) {
                ivCookie.setImageResource(R.drawable.icon_cookies_min);
                ivCookie.setVisibility(View.VISIBLE);
                firstField += 1;
            }
            if (dbr.showIconInCalendar(currentDay.getTimeInMillis(), BookingTypesDefaultRecords.BOOKING_TYPE_PASTRY_ID)) {
                if (firstField == 0) {
                    ivPastry.setImageResource(R.drawable.icon_pastry_min);
                    ivPastry.setVisibility(View.VISIBLE);
                }
                firstField += 1;
            }

            if (dbr.showIconInCalendar(currentDay.getTimeInMillis(), BookingTypesDefaultRecords.BOOKING_TYPE_CAKE_ID)) {
                ivCake.setImageResource(R.drawable.icon_cake_min);
                ivCake.setVisibility(View.VISIBLE);
                secondField += 1;
            }
            if (dbr.showIconInCalendar(currentDay.getTimeInMillis(), BookingTypesDefaultRecords.BOOKING_TYPE_CAKEPOPS_ID)) {
                if (secondField == 0) {
                    ivCakepops.setImageResource(R.drawable.icon_cakepops_min);
                    ivCakepops.setVisibility(View.VISIBLE);
                }
                secondField += 1;
            }

            if (dbr.showIconInCalendar(currentDay.getTimeInMillis(), BookingTypesDefaultRecords.BOOKING_TYPE_CUPCAKE_ID)) {
                ivCupcake.setImageResource(R.drawable.icon_cupcake_min);
                ivCupcake.setVisibility(View.VISIBLE);
                thirdField += 1;
            }
            if (dbr.showIconInCalendar(currentDay.getTimeInMillis(), BookingTypesDefaultRecords.BOOKING_TYPE_MARRIAGE_ID)) {
                if (thirdField == 0) {
                    ivMarriage.setImageResource(R.drawable.icon_cake_marriage_min);
                    ivMarriage.setVisibility(View.VISIBLE);
                    isSecondInThirdFieldVisible = true;
                }
                thirdField += 1;
            }
            if (dbr.showIconInCalendar(currentDay.getTimeInMillis(), BookingTypesDefaultRecords.BOOKING_TYPE_SPICE_CAKE_ID)) {
                if (thirdField == 0) {
                    ivSpiceCake.setImageResource(R.drawable.icon_spice_cake_min);
                    ivSpiceCake.setVisibility(View.VISIBLE);
                }
                thirdField += 1;
            }

            if (firstField == 2 && secondField == 0) {
                ivPastry.setVisibility(View.INVISIBLE);
                ivCakepops.setImageResource(R.drawable.icon_pastry_min);
                ivCakepops.setVisibility(View.VISIBLE);
            } else if (firstField == 2 && thirdField == 0) {
                ivPastry.setVisibility(View.INVISIBLE);
                ivMarriage.setImageResource(R.drawable.icon_pastry_min);
                ivMarriage.setVisibility(View.VISIBLE);
            } else if (secondField == 2 && firstField == 0){
                ivCakepops.setVisibility(View.INVISIBLE);
                ivPastry.setImageResource(R.drawable.icon_cakepops_min);
                ivPastry.setVisibility(View.VISIBLE);
            } else if (secondField == 2 && thirdField == 0){
                ivCakepops.setVisibility(View.INVISIBLE);
                ivMarriage.setImageResource(R.drawable.icon_cakepops_min);
                ivMarriage.setVisibility(View.VISIBLE);
            } else if (firstField == 0 && secondField != 0 && thirdField > 1) {
                if (isSecondInThirdFieldVisible){
                    ivPastry.setImageResource(R.drawable.icon_cake_marriage_min);
                    ivPastry.setVisibility(View.VISIBLE);
                }
                else {
                    ivPastry.setImageResource(R.drawable.icon_spice_cake_min);
                    ivPastry.setVisibility(View.VISIBLE);
                }
            } else if (firstField != 0 && secondField == 0 && thirdField > 1) {
                if (isSecondInThirdFieldVisible){
                    ivCakepops.setImageResource(R.drawable.icon_cake_marriage_min);
                    ivCakepops.setVisibility(View.VISIBLE);
                }
                else {
                    ivCakepops.setImageResource(R.drawable.icon_spice_cake_min);
                    ivCakepops.setVisibility(View.VISIBLE);
                }
            }else if (firstField == 0 && secondField == 0 && thirdField == 3) {
                ivPastry.setImageResource(R.drawable.icon_spice_cake);
                ivPastry.setVisibility(View.VISIBLE);
                ivCakepops.setImageResource(R.drawable.icon_cake_marriage_min);
                ivCakepops.setVisibility(View.VISIBLE);
            }
        }

        dayView.setText(String.format("%02d", currentDay.get(Calendar.DAY_OF_MONTH)));
        String s = days[position];
        fullDayView.setText(s);

        return v;
    }

    public void refreshDays()
    {
        items.clear();

        days = new String[42];//7 * 6 lines

        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = (int) calendar.get(Calendar.DAY_OF_WEEK);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.mm.yyyy");
        Date formatedDate = calendar.getTime();

        int monthIndex = Calendar.MONTH - 1;
        Calendar previousMonth = Calendar.getInstance();
        previousMonth.setTime(calendar.getTime());
        previousMonth.add(Calendar.MONTH, -1);

        Calendar nextMonth = Calendar.getInstance();
        nextMonth.setTime(calendar.getTime());
        nextMonth.add(Calendar.MONTH, 1);

        int lastDayPreviousMonth = previousMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        int j = FIRST_DAY_OF_WEEK;

        if(firstDay > 1) {
            for(j = 0; j < firstDay - FIRST_DAY_OF_WEEK; j++) {
                days[j] = DateExtension.getDate(lastDayPreviousMonth - (firstDay - FIRST_DAY_OF_WEEK - j) + 2,
                        previousMonth.get(Calendar.MONTH) + 1,
                        previousMonth.get(Calendar.YEAR));
            }
        }
        else {
            for(j = 0; j < FIRST_DAY_OF_WEEK * 6; j++) {
                days[j] = DateExtension.getDate(lastDayPreviousMonth - (FIRST_DAY_OF_WEEK * 6 - j) + 1,
                        previousMonth.get(Calendar.MONTH) + 1,
                        previousMonth.get(Calendar.YEAR));
            }
            j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
        }

        int dayNumber = 1;
        boolean isNextMonth = false;
        for(int i = j - 1; i < days.length; i++) {
            if (dayNumber > lastDay) {
                dayNumber = 1;
                isNextMonth = true;
            }
            if (!isNextMonth) {
                days[i] = DateExtension.getDate(dayNumber,
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.YEAR));
            } else {
                days[i] = DateExtension.getDate(dayNumber,
                        nextMonth.get(Calendar.MONTH) + 1,
                        nextMonth.get(Calendar.YEAR));
            }
            dayNumber++;
        }
    }

    /**
     * освобождение неиспользуемых ресурсов
     */
    public void onDestroy() {
        if (dbr != null){
            dbr.close();
        }
    }
}

package yummycherrypie.pl.activities;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import yummycherrypie.pl.activities.bookings.AddBooking;
import yummycherrypie.pl.activities.bookings.BookingsPerDate;
import yummycherrypie.pl.adapters.CalendarAdapter;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.business_logic.Extensions.OtherExtensions;
import yummycherrypie.system.R;

public class MyCalendarView extends BaseFragmentActivity {
    private DBHelper dbHelper;
    private BookingRepository dbr;

    public Calendar month;
    public CalendarAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        dbHelper = new DBHelper(this);
        dbr = new BookingRepository(dbHelper);

        month = Calendar.getInstance();

        adapter = new CalendarAdapter(this, month);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        TextView title  = (TextView) findViewById(R.id.title);
        title.setText(String.format("%s %d", DateExtension.MONTHS[month.get(Calendar.MONTH)], month.get(Calendar.YEAR)));

        TextView previous  = (TextView) findViewById(R.id.previous);
        previous.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
                } else {
                    month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
                }
                refreshCalendar();
            }
        });

        TextView next  = (TextView) findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) {
                    month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
                } else {
                    month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
                }
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView fullDate = (TextView)v.findViewById(R.id.fulldate);
                if(fullDate != null && !fullDate.getText().equals("")) {
                    Date d = DateExtension.getDate(fullDate.getText().toString());
                    if (d != null) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.setTime(d);
                        selectedDate.set(Calendar.HOUR_OF_DAY, OtherExtensions.DEFAULT_BOOKING_TIME);
                        selectedDate.set(Calendar.MINUTE, selectedDate.getActualMinimum(Calendar.MINUTE));
                        selectedDate.set(Calendar.SECOND, selectedDate.getActualMinimum(Calendar.SECOND));

                        Format format = new SimpleDateFormat("dd.MM.yyyy");
                        Toast.makeText(getApplicationContext(), format.format(selectedDate.getTime()), Toast.LENGTH_SHORT)
                                .show();

                        GoToCreatingBooking(selectedDate.getTimeInMillis());
                    }else{
                        Toast.makeText(getApplicationContext(), "Не удалось определить дату", Toast.LENGTH_SHORT)
                                .show();
                    }
                }

            }
        });
    }

    //переходим в активити по созданию заказа
    private void GoToCreatingBooking(long dateInMilliseconds){
        //если нет заказов на эту дату, открывай создание заказа, если есть, то список заказов
        try	{
            Cursor c = dbr.getAllBookings(dateInMilliseconds);
            Intent intent;
            if (!c.moveToFirst()){
                intent = new Intent(this, AddBooking.class);
            }
            else
                intent = new Intent(this, BookingsPerDate.class);
            intent.setData(Uri.parse("data:" + dateInMilliseconds + "," + (-1)));
            startActivityForResult(intent, 1);
        }
        catch(Exception e)
        {
            Toast.makeText(this, "При создании заказа возникла ошибка", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // получаем значение типа торта из другого activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        refreshCalendar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
        refreshCalendar();
    }

    public void refreshCalendar()
    {
        TextView title  = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();

        title.setText(String.format("%s %d", DateExtension.MONTHS[month.get(Calendar.MONTH)], month.get(Calendar.YEAR)));
    }

    /*public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();
            // format random values. You can implement a dedicated class to provide real values
            for(int i=0;i<31;i++) {
                Random r = new Random();

                if(r.nextInt(10)>6)
                {
                    items.add(Integer.toString(i));
                }
            }

            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        dbr.close();
    }
}
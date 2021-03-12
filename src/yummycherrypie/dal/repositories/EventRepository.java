package yummycherrypie.dal.repositories;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import yummycherrypie.base_classes.Event;
import yummycherrypie.business_logic.Period;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class EventRepository extends DataBaseRepository {

    public EventRepository(DBHelper dbHelper){
        super(dbHelper);
    }

    /**
     * возвращает событие по ИД
     */
    public Event getEvent(long eventId) {
        Cursor c = db.query(DBHelper.TABLE_EVENTS, null, DBHelper.COLUMN_ID + " = ?",
                new String[] { Long.toString(eventId) }, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    return new Event(c);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return null;
    }

    /**
     * Привязывает заказ к событию
     * */
    public void setEventForBooking(long bookingId, long eventId){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_BOOKINGS_EVENT_ID, eventId);
        update(DBHelper.TABLE_BOOKINGS, cv, bookingId);
    }

    /**
     * Возвращает первым элементом массива суммарную себестоимость,
     * вторым - суммарную стоимость
     * всех заказов события
     * */
    public double[] getSummaryPrices(long eventId){

        String s = String.format(
                "select sum(%s) as rp, " +
                        "sum(%s - %s * (%s / 100)) as cp " +
                        "from %s " +
                        "where %s = %s",
                DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE,
                DBHelper.COLUMN_BOOKINGS_CAKE_PRICE, DBHelper.COLUMN_BOOKINGS_CAKE_PRICE, DBHelper.COLUMN_BOOKINGS_DISCOUNT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_EVENT_ID,
                eventId);
        Cursor c = db.rawQuery(s, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int recipePriceColIndex = c.getColumnIndex("rp");
                    int cakePriceColIndex = c.getColumnIndex("cp");
                    if (recipePriceColIndex == -1 && cakePriceColIndex == -1)
                        return new double[]{ 0, 0 };
                    return new double[] { c.getDouble(recipePriceColIndex), c.getDouble(cakePriceColIndex) };
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return new double[]{ 0, 0 };
    }

    /**
     * Возвращает все текущие заказы (позднее или равны сегодняшней даты)
     */
    public Cursor getAllCurrentEvents(){
        Calendar cal = Calendar.getInstance();
        long startDate = Period.getBorderOfDay(cal.getTime()).GetStartDateLong();
        return db.query(DBHelper.TABLE_EVENTS, null, DBHelper.COLUMN_EVENTS_DATE + " >= ?",
                new String[] { Long.toString(startDate) }, null, null, DBHelper.COLUMN_EVENTS_DATE);
    }

    /**
     * Возвращает события
     */
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> listItems = new ArrayList<Event>();
        Cursor c = db.query(DBHelper.TABLE_EVENTS, null, null, null, null, null, DBHelper.COLUMN_EVENTS_DATE);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Event().getColumnIndexes(c);
                    do {
                        listItems.add(new Event(c, map));
                    } while (c.moveToNext());
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return listItems;
    }
}

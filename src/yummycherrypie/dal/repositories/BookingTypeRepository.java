package yummycherrypie.dal.repositories;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Map;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.BookingType;
import yummycherrypie.business_logic.Period;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.default_records.BookingTypesDefaultRecords;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class BookingTypeRepository extends DataBaseRepository {

    public BookingTypeRepository(DBHelper dbHelper){
        super(dbHelper);
    }

    /**
     * Показывать ли иконку данного типа заказа в дне календаря?
     * Если хотя бы один заказ указанного типа, показываем иконку
     */
    public boolean showIconInCalendar(long date, long bookingTypeId){

        Period period = Period.getBorderOfDay(date);
        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null,
                DBHelper.COLUMN_BOOKINGS_DATE + " >= ? and "
                        + DBHelper.COLUMN_BOOKINGS_DATE + " <= ?",
                new String[]{Long.toString(period.GetStartDateLong()), Long.toString(period.GetEndDateLong())}, null,
                null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Booking().getColumnIndexes(c);
                    do {
                        if (bookingTypeId == c.getLong(c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID)))
                            return true;
                    } while (c.moveToNext());
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return false;
    }

    /**
     * Показывать ли иконку данного типа заказа в дне календаря?
     * Если хотя бы один заказ указанного типа, показываем иконку
     */
    public boolean showOtherBookingTypeIconInCalendar(long date){
        Period period = Period.getBorderOfDay(date);
        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null,
                DBHelper.COLUMN_BOOKINGS_DATE + " >= ? and "
                        + DBHelper.COLUMN_BOOKINGS_DATE + " <= ?",
                new String[]{Long.toString(period.GetStartDateLong()), Long.toString(period.GetEndDateLong())}, null,
                null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Booking().getColumnIndexes(c);
                    do {
                        if (c.getLong(c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID)) > BookingTypesDefaultRecords.BOOKING_TYPE_COOKIE_ID)
                            return true;
                    } while (c.moveToNext());
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return false;
    }

    /**
     * Возвращает тип заказа
     */
    public BookingType getBookingType(long id) {
        Cursor c = db.query(DBHelper.TABLE_BOOKING_TYPES, null, DBHelper.COLUMN_ID + " = ?",
                new String[]{Long.toString(id)}, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    return new BookingType(c);
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return null;
    }

    /**
     * Возвращает типы заказа
     */
    public ArrayList<BookingType> getAllBookingTypes() {
        ArrayList<BookingType> listItems = new ArrayList<BookingType>();
        Cursor c = db.query(DBHelper.TABLE_BOOKING_TYPES, null, null, null, null, null, DBHelper.COLUMN_BOOKING_TYPES_NAME);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new BookingType().getColumnIndexes(c);
                    do {
                        listItems.add(new BookingType(c, map));
                    } while (c.moveToNext());
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return listItems;
    }

    /**
     * Возвращает типы заказа
     */
    public Cursor getAllBookingTypesCursor() {
        return db.query(DBHelper.TABLE_BOOKING_TYPES, null, null, null, null, null, DBHelper.COLUMN_BOOKING_TYPES_NAME);
    }
}

package yummycherrypie.dal.repositories;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Map;

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class BookingManRepository extends DataBaseRepository {

    private DBHelper dbHelper;

    public BookingManRepository(DBHelper dbHelper){
        super(dbHelper);
        this.dbHelper = dbHelper;
    }

    /**
     * Возвращает заказчика
     */
    public BookingMan getBookingMan(long id) {
        Cursor c = db.query(DBHelper.TABLE_BOOKING_MEN, null, DBHelper.COLUMN_ID + " = ?",
                new String[]{Long.toString(id)}, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    return new BookingMan(c);
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return null;
    }

    /**
     * Возвращает заказчиков
     */
    public ArrayList<BookingMan> getAllBookingMen() {
        ArrayList<BookingMan> listItems = new ArrayList<BookingMan>();
        Cursor c = db.query(DBHelper.TABLE_BOOKING_MEN, null, null, null, null, null, DBHelper.COLUMN_BOOKING_MEN_NAME);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new BookingMan().getColumnIndexes(c);
                    do {
                        listItems.add(new BookingMan(c, map));
                    } while (c.moveToNext());
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return listItems;
    }

    @Override
    public void delete(String tableName, long id) {
        if (dbHelper.TABLE_BOOKINGS.equals(tableName)) {
            ArrayList<RecipeLine> list =  new RecipeLineRepository(dbHelper).getAllRecipeLinesForBooking(id);
            for(RecipeLine r : list)
            {
                delete(dbHelper.TABLE_RECIPE_LINES, r.getId());
            }
        }

        int d_id = db.delete(tableName, DBHelper.COLUMN_ID + " = ?",
                new String[]{Long.toString(id)});
    }

    /**
     * Есть ли заказчик с таким телефоном
     */
    public BookingMan isExist(String phone) {
        if (phone.isEmpty()){
            return null;
        }

        Cursor c = db.query(DBHelper.TABLE_BOOKING_MEN, null, DBHelper.COLUMN_BOOKING_MEN_PHONE + " = ?",
                new String[]{ phone }, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new BookingMan().getColumnIndexes(c);
                    return new BookingMan(c, map);
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return null;
    }
}

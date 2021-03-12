package yummycherrypie.pl.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.base_classes.BookingType;
import yummycherrypie.base_classes.Component;
import yummycherrypie.base_classes.Event;
import yummycherrypie.base_classes.Recipe;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.dal.repositories.BookingTypeRepository;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.dal.repositories.DataBaseRepository;
import yummycherrypie.dal.repositories.EventRepository;
import yummycherrypie.dal.repositories.RecipeLineRepository;
import yummycherrypie.dal.repositories.RecipeRepository;
import yummycherrypie.system.R;

/**
 * Created by Nikolay_Piskarev on 12/7/2015.
 */
public class TableRecordAdapter extends CursorAdapter {

    private final LayoutInflater mInflater;
    private Context context;
    private String tableName;

    public TableRecordAdapter(Context context, Cursor cursor, String tableName) {
        super(context, cursor, false);
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.tableName = tableName;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.item_table_record, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        DBHelper dbHelper = new DBHelper(context);
        DataBaseRepository dbr;
        if (tableName.equals(DBHelper.TABLE_BOOKING_MEN)) {
            dbr = new BookingManRepository(dbHelper);
            BookingMan bm = new BookingMan(cursor);
            ((TextView) view.findViewById(R.id.tvRecord)).setText(bm.toString());
        } else if (tableName.equals(DBHelper.TABLE_BOOKINGS)) {
            dbr = new BookingRepository(dbHelper);
            Booking b = new Booking(cursor);
            ((TextView) view.findViewById(R.id.tvRecord)).setText(b.toString());
        } else if (tableName.equals(DBHelper.TABLE_BOOKING_TYPES)) {
            dbr = new BookingTypeRepository(dbHelper);
            BookingType bt = new BookingType(cursor);
            ((TextView) view.findViewById(R.id.tvRecord)).setText(bt.toString());
        } else if (tableName.equals(DBHelper.TABLE_COMPONENTS)) {
            dbr = new ComponentRepository(dbHelper);
            Component c = new Component(cursor);
            ((TextView) view.findViewById(R.id.tvRecord)).setText(c.toString());
        } else if (tableName.equals(DBHelper.TABLE_EVENTS)) {
            dbr = new EventRepository(dbHelper);
            Event e = new Event(cursor);
            ((TextView) view.findViewById(R.id.tvRecord)).setText(e.toString());
        } else if (tableName.equals(DBHelper.TABLE_RECIPE_LINES)) {
            dbr = new RecipeLineRepository(dbHelper);
            RecipeLine rl = new RecipeLine(cursor);
            ((TextView) view.findViewById(R.id.tvRecord)).setText(rl.toString());
        } else if (tableName.equals(DBHelper.TABLE_RECIPES)) {
            dbr = new RecipeRepository(dbHelper);
            Recipe r = new Recipe(cursor);
            ((TextView) view.findViewById(R.id.tvRecord)).setText(r.toString());
        }
    }
}

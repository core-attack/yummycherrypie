package yummycherrypie.pl.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.base_classes.Event;
import yummycherrypie.pl.CurrencyExtension;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.system.R;

/**
 * Created by CoreAttack on 27.07.2015.
 */
public class EventAdapter extends AlternateItemsCursorAdapter {

    private final LayoutInflater mInflater;

    public EventAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return mInflater.inflate(R.layout.item_event, parent, false);
    }

    public void bindView(View view, Context context, Cursor cursor){
        super.bindView(view, context, cursor);

        DBHelper dbHelper = new DBHelper(context);
        BookingManRepository dbr = new BookingManRepository(dbHelper);

        try{
            Event event = new Event(cursor);

            ((TextView) view.findViewById(R.id.tvEventDate))
                    .setText(DateExtension.getDateString(event.getDateLong()));

            ((TextView) view.findViewById(R.id.tvEventPrice)).setText(CurrencyExtension.getCurrencyDecimal(event.getPrice()));

            ((TextView) view.findViewById(R.id.tvEventPlace)).setText(String.format("%s", event.getPlace()));

            BookingMan bm = dbr.getBookingMan(event.getBookingManId());
            if (bm != null)
                ((TextView) view.findViewById(R.id.tvBookingManName)).setText(String.format("%s (%s)", bm.getName(), bm.getPhone()));
            else
                ((TextView) view.findViewById(R.id.tvBookingManName)).setText("Заказчик не выбран");
        }
        finally {
            if (dbr != null){
                dbr.close();
            }
        }
    }
}

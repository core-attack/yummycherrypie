package yummycherrypie.pl.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.system.R;

/**
 * Created by CoreAttack on 27.07.2015.
 */
public class BookingManAdapter extends AlternateItemsCursorAdapter {

    private LayoutInflater lInflater;
    private Context mContext;
    private Context appContext;

    public BookingManAdapter(Context context, Cursor c){
        super(context, c);
        this.mContext = context;
        this.lInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return lInflater.inflate(R.layout.item_booking_man, parent, false);
    }

    public void bindView(@NonNull View view, Context context, @NonNull Cursor cursor){
        super.bindView(view, context, cursor);

        BookingMan bm = new BookingMan(cursor);
        if (!bm.getName().isEmpty()) {
            ((TextView) view.findViewById(R.id.tvBookingManName))
                    .setText(StringExtension.makeShortString(bm.getName(), 25));
        } else {
            ((TextView) view.findViewById(R.id.tvBookingManName))
                    .setText("Имя не введено");
        }

        ((TextView) view.findViewById(R.id.tvBookingManPhone))
                .setText(bm.getPhone());
    }
}

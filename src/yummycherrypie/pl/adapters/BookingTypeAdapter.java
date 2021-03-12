package yummycherrypie.pl.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import yummycherrypie.base_classes.BookingType;
import yummycherrypie.system.R;
import yummycherrypie.pl.ImageViewExtension;

/**
 * Created by piskarev on 22.09.2015.
 */
public class BookingTypeAdapter extends AlternateItemsCursorAdapter {

    private LayoutInflater lInflater;
    private Cursor cr;
    private Context mContext;
    private Context appContext;

    public BookingTypeAdapter(Context context, Cursor c){
        super(context, c);
        this.mContext = context;
        this.lInflater = LayoutInflater.from(context);
        this.cr = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return lInflater.inflate(R.layout.item_booking_type, parent, false);
    }

    public void bindView(@NonNull View view, Context context, @NonNull Cursor cursor){
        super.bindView(view, context, cursor);

        view.setLayoutParams(new GridView.LayoutParams(240, 240));

        BookingType bt = new BookingType(cursor);

        if (bt != null) {

            if (!bt.getName().isEmpty()) {
                ((TextView) view.findViewById(R.id.tvName)).setText(bt.getName());
            } else {
                ((TextView) view.findViewById(R.id.tvName)).setText("Наименование не задано");
            }
            ImageView icon = ((ImageView) view.findViewById(R.id.ivImage));


            ImageViewExtension.chooseImage(icon, bt.getId(), ImageViewExtension.Size.MIDDLE);
        }
    }
}
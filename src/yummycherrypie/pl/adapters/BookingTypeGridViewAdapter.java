package yummycherrypie.pl.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import yummycherrypie.base_classes.BookingType;
import yummycherrypie.system.R;
import yummycherrypie.pl.Colors;
import yummycherrypie.pl.ImageViewExtension;

/**
 * Created by Nikolay_Piskarev on 12/14/2015.
 */
public class BookingTypeGridViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<BookingType> bookingTypes;
    long selectedBookingType;

    public BookingTypeGridViewAdapter(Context context, ArrayList<BookingType> bTypes, long selectedBookingType){
        this.context = context;
        bookingTypes = bTypes;
        this.selectedBookingType = selectedBookingType;
    }

    @Override
    public int getCount() {
        return bookingTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return bookingTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookingTypes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_booking_type_small, null);
        }

        v.setLayoutParams(new GridView.LayoutParams(114, 114));

        if (selectedBookingType == bookingTypes.get(position).getId()) {
            v.setBackgroundColor(Colors.COLOR_DEFAULT_BOOKING_TYPE_BACKGROUND_GRID_VIEW_ITEM_SELECTED);
        }else{
            v.setBackgroundColor(Colors.COLOR_DEFAULT_BOOKING_TYPE_BACKGROUND_GRID_VIEW_ITEM);
        }

        ImageView imageView = (ImageView) v.findViewById(R.id.ivImage);
        ImageViewExtension.chooseImage(imageView, bookingTypes.get(position).getId(), ImageViewExtension.Size.MIN);
        return v;
    }
}

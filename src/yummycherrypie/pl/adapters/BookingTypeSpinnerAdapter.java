package yummycherrypie.pl.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import yummycherrypie.pl.activities.bookings.AddBooking;
import yummycherrypie.base_classes.BookingType;
import yummycherrypie.system.R;

/**
 * Created by piskarev on 22.09.2015.
 */
public class BookingTypeSpinnerAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList data;
    private BookingType tempValues=null;
    private LayoutInflater inflater;

    public Resources res;

    /**
     * CustomAdapter Constructor
     * */
    public BookingTypeSpinnerAdapter(
            AddBooking activitySpinner,
            int textViewResourceId,
            ArrayList objects,
            Resources resLocal
    ){
        super(activitySpinner, textViewResourceId, objects);

        activity = activitySpinner;
        data     = objects;
        res      = resLocal;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.item_booking_type, parent, false);
        tempValues = null;
        tempValues = (BookingType) data.get(position);

        TextView label = (TextView)row.findViewById(R.id.tvName);
        ImageView icon = (ImageView)row.findViewById(R.id.ivImage);

        label.setText(tempValues.getName());
        //if (tempValues.getId() > 4)
        //    icon.setVisibility(View.INVISIBLE);
        //else{
        //    icon.setVisibility(View.VISIBLE);
        //    icon.setImageURI(Uri.parse("@drawable/" + tempValues.getImageUrl()));
        //}

        return row;
    }
}

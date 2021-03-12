package yummycherrypie.pl.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.system.R;
import yummycherrypie.pl.StringExtension;

/**
 * Created by Nikolay_Piskarev on 12/18/2015.
 */

public class SpinnerSubtextAdapter extends BaseAdapter {
    Typeface font;
    Context context;
    ArrayList<BookingMan> data;
    LayoutInflater inflater;

    public SpinnerSubtextAdapter(Context context, ArrayList<BookingMan> data) {
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        font = Typeface.createFromAsset(context.getAssets(), StringExtension.DEFAULT_FONT);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View actionBarView = inflater.inflate(R.layout.item_spinner_subtext, null);
        TextView title = (TextView) actionBarView
                .findViewById(R.id.ab_basemaps_title);
        TextView subtitle = (TextView) actionBarView
                .findViewById(R.id.ab_basemaps_subtitle);
        title.setText(data.get(position).getName());
        title.setTypeface(font);
        subtitle.setText(data.get(position).getPhone());
        subtitle.setTypeface(font);
        return actionBarView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View actionBarDropDownView = inflater.inflate(
                R.layout.item_spinner_subtext, null);
        if (position % 2 == 0){
            actionBarDropDownView.setBackgroundColor(Color.parseColor("#1Afd80a4"));
        }
        else{
            actionBarDropDownView.setBackgroundColor(Color.parseColor("#4Dffffff"));
        }
        TextView title = (TextView) actionBarDropDownView
                .findViewById(R.id.ab_basemaps_title);
        TextView subtitle = (TextView) actionBarDropDownView
                .findViewById(R.id.ab_basemaps_subtitle);
        title.setText(data.get(position).getName());
        title.setTypeface(font);
        subtitle.setText(data.get(position).getPhone());
        subtitle.setTypeface(font);
        return actionBarDropDownView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getPosition(BookingMan item) {
        int i = data.indexOf(item);
        return data.indexOf(item);
    }

}
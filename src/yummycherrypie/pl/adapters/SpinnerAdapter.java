package yummycherrypie.pl.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import yummycherrypie.pl.Colors;
import yummycherrypie.pl.StringExtension;

/**
 * Created by Nikolay_Piskarev on 12/8/2015.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {
    Typeface font = Typeface.createFromAsset(getContext().getAssets(), StringExtension.DEFAULT_FONT);

    public SpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(font);
        view.setTextColor(Colors.COLOR_DEFAULT_TEXT);
        if (position % 2 == 0){
            view.setBackgroundColor(Color.parseColor("#1Afd80a4"));
        }
        else{
            view.setBackgroundColor(Color.parseColor("#4Dffffff"));
        }
        return view;
    }
}


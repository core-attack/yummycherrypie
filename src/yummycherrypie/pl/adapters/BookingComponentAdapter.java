package yummycherrypie.pl.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import yummycherrypie.base_classes.Component;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.system.R;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.WeightExtension;

/**
 * Created by CoreAttack on 27.07.2015.
 */
public class BookingComponentAdapter extends ArrayAdapter<RecipeLine> {

    private ArrayList data;
    private RecipeLine tempValues = null;
    private LayoutInflater inflater;
    private ComponentRepository compRepo;
    private int layout;

    public Resources res;

    public BookingComponentAdapter(Context context,
                                   int layout,
                                   ArrayList objects,
                                   ComponentRepository compRepo){
        super(context, layout, objects);

        data     = objects;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.compRepo = compRepo;
        this.layout = layout;
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

        View row = inflater.inflate(layout, parent, false);

        if (data.get(position) != null) {

            tempValues = null;
            tempValues = (RecipeLine) data.get(position);

            Component c = compRepo.getComponent(tempValues.getComponentId());
            if (c != null) {
                if (c.isCountable()) {
                    ((TextView) row.findViewById(R.id.tvRecipeComponentWeight))
                            .setText(String.format(Locale.ROOT, "%d шт.", tempValues.getCount()));
                } else {
                    ((TextView) row.findViewById(R.id.tvRecipeComponentWeight))
                            .setText(WeightExtension.getWeightWithGramm(tempValues.getWeight()));
                }
                ((TextView) row.findViewById(R.id.tvrecipeComponentName))
                        .setText(StringExtension.makeShortString(String.format("• %s", c.getName())));
            }
        } else {
            ((TextView) row.findViewById(R.id.tvrecipeComponentName))
                    .setText("");
            ((TextView) row.findViewById(R.id.tvRecipeComponentWeight))
                    .setText("");
        }
        return row;
    }
}

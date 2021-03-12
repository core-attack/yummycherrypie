package yummycherrypie.pl.adapters;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import yummycherrypie.base_classes.Component;
import yummycherrypie.pl.CurrencyExtension;
import yummycherrypie.business_logic.Extensions.OtherExtensions;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.system.R;
import yummycherrypie.pl.WeightExtension;

/**
 * Created by CoreAttack on 27.07.2015.
 */
public class ComponentAdapter extends AlternateItemsCursorAdapter {

    private LayoutInflater lInflater;
    private Cursor cr;
    private int layout;
    private Context mContext;
    private Context appContext;
    private int componentNameLength = 20;


    public ComponentAdapter(Context context, Cursor c){
        super(context, c);
        this.layout = layout;
        this.mContext = context;
        this.lInflater = LayoutInflater.from(context);
        this.cr = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return lInflater.inflate(R.layout.item_component, parent, false);
    }

    public void bindView(@NonNull View view, Context context, @NonNull Cursor cursor){
        super.bindView(view, context, cursor);

        Component c = new Component(cursor);
        if (!c.getName().isEmpty()) {
            ((TextView) view.findViewById(R.id.tvComponentName))
                    .setText(StringExtension.makeShortString(c.getName(), componentNameLength));
        } else {
            ((TextView) view.findViewById(R.id.tvComponentName))
                    .setText("Наименование не задано");
        }

        ((TextView) view.findViewById(R.id.tvComponentPrice))
                .setText(String.format(OtherExtensions.DEFAULT_LOCALE, "%s ",
                        CurrencyExtension.getCurrencyWithoutRouble(c.getPrice())));

        if (c.isCountable())
            ((TextView) view.findViewById(R.id.tvComponentWeight))
                    .setText( "/ 1 шт.");
        else
            ((TextView) view.findViewById(R.id.tvComponentWeight))
                    .setText(String.format(OtherExtensions.DEFAULT_LOCALE, " / %s ",
                            WeightExtension.getWeightWithGramm(c.getWeight())));
    }
}

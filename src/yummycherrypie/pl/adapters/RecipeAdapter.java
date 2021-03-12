package yummycherrypie.pl.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yummycherrypie.base_classes.Recipe;
import yummycherrypie.pl.CountExtension;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.system.R;
import yummycherrypie.pl.WeightExtension;

/**
 * Created by CoreAttack on 27.07.2015.
 */
public class RecipeAdapter extends AlternateItemsCursorAdapter {

    private LayoutInflater lInflater;
    private Cursor cr;
    private int layout;
    private Context mContext;
    private Context appContext;

    public RecipeAdapter(Context context, Cursor c){
        super(context, c);
        this.layout = layout;
        this.mContext = context;
        this.lInflater = LayoutInflater.from(context);
        this.cr = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return lInflater.inflate(R.layout.item_recipe, parent, false);
    }

    public void bindView(@NonNull View view, Context context, @NonNull Cursor cursor){
        super.bindView(view, context, cursor);

        Recipe r = new Recipe(cursor);

        if (!r.getName().isEmpty()) {
            ((TextView) view.findViewById(R.id.tvRecipeName))
                    .setText(StringExtension.makeShortString(r.getName(), 25));
        } else {
            ((TextView) view.findViewById(R.id.tvRecipeName))
                    .setText("Наименование не задано");
        }

        if (r.isCountable()) {
            ((TextView) view.findViewById(R.id.tvRecipeCake))
                    .setText(CountExtension.getWeightWithCount(r.getCountProduct()));
        } else {
            ((TextView) view.findViewById(R.id.tvRecipeCake))
                .setText(WeightExtension.getWeightWithGramm(r.getCakeWeight()));
        }
    }
}

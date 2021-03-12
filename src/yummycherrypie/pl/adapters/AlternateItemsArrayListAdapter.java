package yummycherrypie.pl.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import java.util.ArrayList;

import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.system.R;

/**
 * Created by CoreAttack on 27.07.2015.
 */
public class AlternateItemsArrayListAdapter extends ArrayAdapter<RecipeLine> {

    private LayoutInflater inflater;
    private int layout;
    private boolean chet = false;

    public AlternateItemsArrayListAdapter(Context context,
                                          int layout,
                                          ArrayList objects){
        super(context, layout, objects);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent, View row) {
        if (!chet){
            ((FrameLayout) row.findViewById(R.id.bckg1)).setVisibility(View.VISIBLE);
            ((FrameLayout) row.findViewById(R.id.bckg2)).setVisibility(View.INVISIBLE);
        }
        else{
            ((FrameLayout) row.findViewById(R.id.bckg2)).setVisibility(View.VISIBLE);
            ((FrameLayout) row.findViewById(R.id.bckg1)).setVisibility(View.INVISIBLE);
        }
        chet = !chet;

        return row;
    }

    //todo адаптер запоминает значение chet и при обновлении рецепта фон первого пункта когда-то розовый, когда-то белый. ИСПРАВИТЬ!
}

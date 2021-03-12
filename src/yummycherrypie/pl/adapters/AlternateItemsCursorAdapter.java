package yummycherrypie.pl.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;

import yummycherrypie.system.R;

/**
 * Created by Nikolay_Piskarev on 12/10/2015.
 */
public class AlternateItemsCursorAdapter extends CursorAdapter {

    private boolean chet = false;

    public AlternateItemsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (!chet){
            ((FrameLayout) view.findViewById(R.id.bckg1)).setVisibility(View.VISIBLE);
            ((FrameLayout) view.findViewById(R.id.bckg2)).setVisibility(View.INVISIBLE);
        }
        else{
            ((FrameLayout) view.findViewById(R.id.bckg2)).setVisibility(View.VISIBLE);
            ((FrameLayout) view.findViewById(R.id.bckg1)).setVisibility(View.INVISIBLE);
        }
        chet = !chet;
    }
}

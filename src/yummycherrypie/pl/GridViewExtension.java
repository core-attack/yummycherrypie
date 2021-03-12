package yummycherrypie.pl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

/**
 * Created by Nikolay_Piskarev on 12/23/2015.
 */
public class GridViewExtension {

    /**
     * Разворачивает gridview
     * */
    public static void setGridViewHeightBasedOnChildren(Context c, GridView gridView, int countColumns) {
        try {
            ListAdapter gridViewAdapter = gridView.getAdapter();
            if (gridViewAdapter == null) {
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < gridViewAdapter.getCount(); i++) {
                View listItem = gridViewAdapter.getView(i, null, gridView);
                totalHeight += listItem.getLayoutParams().height;
            }
            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            if (gridViewAdapter.getCount() % 2 != 0){
                //если колонок нечетное количество, обрезает половину высоты последней
                params.height = (totalHeight / countColumns) + (gridViewAdapter.getView(0, null, gridView).getLayoutParams().height / 2);
            }
            else {
                params.height = (totalHeight / countColumns);
            }
            gridView.setLayoutParams(params);
            gridView.requestLayout();
        }
        catch (Exception e)
        {
            Toast.makeText(c, "Не удалось изменить высоту списка", Toast.LENGTH_SHORT);
        }
    }
}

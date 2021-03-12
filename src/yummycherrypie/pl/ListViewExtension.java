package yummycherrypie.pl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by CoreAttack on 01.12.2015.
 */
public class ListViewExtension {

    /**
     * Разворачивает listview
     * */
    public static void setListViewHeightBasedOnChildren(Context c, ListView listView) {
        try {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                totalHeight += listItem.getLayoutParams().height;
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
        catch (Exception e)
        {
            Toast.makeText(c, "Не удалось изменить высоту списка", Toast.LENGTH_SHORT);
        }
    }

    public static void setListViewHeightBasedOnChildren(Context c, ListView listView, CursorAdapter adapter) {
        try {
            if (adapter == null) {
                // pre-condition
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listView);
                totalHeight += listItem.getLayoutParams().height;
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
        catch (Exception e)
        {
            Toast.makeText(c, "Не удалось изменить высоту списка", Toast.LENGTH_SHORT);
        }
    }
}

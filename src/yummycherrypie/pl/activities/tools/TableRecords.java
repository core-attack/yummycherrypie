package yummycherrypie.pl.activities.tools;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.activities.bookings.AddBooking;
import yummycherrypie.pl.adapters.TableRecordAdapter;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.DataBaseRepository;
import yummycherrypie.system.R;

public class TableRecords extends BaseFragmentActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private final String[] tables = {DBHelper.TABLE_BOOKING_MEN, DBHelper.TABLE_BOOKING_TYPES,
            DBHelper.TABLE_BOOKINGS, DBHelper.TABLE_COMPONENTS, DBHelper.TABLE_EVENTS,
    DBHelper.TABLE_RECIPE_LINES, DBHelper.TABLE_RECIPES};

    private ListView listView;
    private Spinner spinner;

    private CursorAdapter ca;

    private DataBaseRepository dbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_records);

        DBHelper dbHelper = new DBHelper(this);
        dbr = new DataBaseRepository(dbHelper);

        loadTables();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        try {
            Intent intent = new Intent(this, AddBooking.class);
            intent.setData(Uri.parse("data:" + (new Date()).getTime() + "," + id));
            startActivityForResult(intent, 1);
        }
        catch(Exception e)
        {
            Toast.makeText(this, "При создании заказа возникла ошибка", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, dbr, spinner.getSelectedItem().toString());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ca.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private static class MyCursorLoader extends CursorLoader {

        DataBaseRepository db;
        String tableName;

        public MyCursorLoader(Context context, DataBaseRepository db, String tableName) {
            super(context);
            this.db = db;
            this.tableName = tableName;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllRecords(tableName, DBHelper.COLUMN_ID + " DESC");
            return cursor;
        }
    }

    private void loadTables(){
        spinner = (Spinner) findViewById(R.id.spTables);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, tables);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Таблица");
        spinner.setSelection(0);

        Context context = this;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadRecords(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadRecords(String tableName){
        listView = (ListView) findViewById(R.id.lvRecords);
        ca = new TableRecordAdapter(this, null, tableName);
        listView.setAdapter(ca);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);
        getSupportLoaderManager().restartLoader(0, null, this);
    }


}

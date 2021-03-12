package yummycherrypie.pl.activities.booking_types;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import yummycherrypie.base_classes.BookingType;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingTypeRepository;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.adapters.BookingTypeAdapter;
import yummycherrypie.system.R;


public class BookingTypes extends BaseFragmentActivity implements OnClickListener,
        OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private final int MENU_UPDATE = 1;
    private final int MENU_DELETE = 2;

    private DBHelper dbHelper;
    private BookingTypeRepository dbr;

    private ListView listBookingTypes;
    private LinearLayout btnAddBookingType;

    private BookingTypeAdapter btAdapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_types);

        btnAddBookingType = (LinearLayout) findViewById(R.id.buttonAddBookingType);
        btnAddBookingType.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        dbr = new BookingTypeRepository(dbHelper);

        loadBookingTypes();
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonAddBookingType:
                try{
                    intent = new Intent(this, AddBookingType.class);
                    intent.setData(Uri.parse("bookingTypeId:" + (-1)));
                    startActivityForResult(intent, 1);
                    break;
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При создании типа заказа возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
        }

    }

    // получаем значение типа торта из другого activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        getSupportLoaderManager().getLoader(0).forceLoad();
        Toast.makeText(this, "Тип заказа сохранен", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.listViewBookingMen:
                menu.add(0, MENU_UPDATE, 0, "Изменить");
                menu.add(0, MENU_DELETE, 0, "Удалить");

                break;
        }

        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onContextItemSelected(item);
                return true;
            }
        };

        for (int i = 0, n = menu.size(); i < n; i++)
            menu.getItem(i).setOnMenuItemClickListener(listener);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_UPDATE:
                try {
                    Intent intent = new Intent(this, AddBookingType.class);
                    AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
                            .getMenuInfo();
                    intent.setData(Uri.parse("bookingTypeId:" + acmi.id));
                    startActivityForResult(intent, 1);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При обновлении типа заказа возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case MENU_DELETE:
                try
                {
                    final AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
                    BookingType bt = dbr.getBookingType(acmi.id);

                    new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Подтвердите удаление")
                        .setMessage("Удалить запись '" + bt.getName() + "'?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbr.delete(DBHelper.TABLE_BOOKING_TYPES, acmi.id);
                                    getSupportLoaderManager().getLoader(0).forceLoad();
                                    Toast.makeText(getApplicationContext(), "Тип заказа удален", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При удалении типа заказа возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        try {
            Intent intent = new Intent(this, AddBookingType.class);
            intent.setData(Uri.parse("bookingTypeId:" + id));
            startActivityForResult(intent, 1);
        }
        catch(Exception e)
        {
            Toast.makeText(this, "При создании типа заказа возникла ошибка", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void loadBookingTypes() {
        btAdapter = new BookingTypeAdapter(this, null);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(btAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddBookingType.class);
                intent.setData(Uri.parse("bookingTypeId:" + id));
                startActivityForResult(intent, 1);

            }
        });

        //listBookingTypes = (ListView) findViewById(R.id.listViewBookingTypes);
        //listBookingTypes.setAdapter(btAdapter);
        //listBookingTypes.setOnItemClickListener(this);
        //registerForContextMenu(listBookingTypes);
        getSupportLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, dbr);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        btAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {

        BookingTypeRepository db;

        public MyCursorLoader(Context context, BookingTypeRepository db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            return db.getAllBookingTypesCursor();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbr != null)
            dbr.close();
    }

}

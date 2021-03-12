package yummycherrypie.pl.activities.events;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import yummycherrypie.base_classes.Event;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.dal.repositories.EventRepository;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.adapters.EventAdapter;
import yummycherrypie.system.R;

/**
 * Событие
 * Created by piskarev on 23.09.2015.
 */
public class Events extends BaseFragmentActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private final int MENU_UPDATE = 1;
    private final int MENU_DELETE = 2;

    private DBHelper dbHelper;
    private EventRepository dbr;

    private EventAdapter eventAdapter;

    private ListView listEvents;
    private LinearLayout btnAddEvent;

    private long eventId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        btnAddEvent = (LinearLayout) findViewById(R.id.buttonAddNewEvent);
        btnAddEvent.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        dbr = new EventRepository(dbHelper);

        loadCurrentEvents();
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonAddNewEvent:
                try{
                    intent = new Intent(this, AddEvent.class);
                    intent.setData(Uri.parse("eventId:" + (-1)));
                    startActivityForResult(intent, 1);
                    break;
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При создании события возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        getSupportLoaderManager().getLoader(0).forceLoad();
        Toast.makeText(this, "Событие сохранен", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        try {
            Intent intent = new Intent(this, AddEvent.class);
            intent.setData(Uri.parse("eventId:" + id));
            startActivityForResult(intent, 1);
        }
        catch(Exception e)
        {
            Toast.makeText(this, "При создании события возникла ошибка", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.listViewAllEvents:
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
        AdapterView.AdapterContextMenuInfo acmi;
        switch (item.getItemId()) {
            case MENU_UPDATE:
                try {
                    Intent intent = new Intent(this, AddEvent.class);
                    acmi = (AdapterView.AdapterContextMenuInfo) item
                            .getMenuInfo();
                    intent.setData(Uri.parse("eventId:" + acmi.id));
                    startActivityForResult(intent, 1);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При обновлении события возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            case MENU_DELETE:
                try
                {
                    acmi = (AdapterView.AdapterContextMenuInfo) item
                            .getMenuInfo();
                    eventId = acmi.id;

                    Event e = dbr.getEvent(eventId);

                    new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Подтвердите удаление")
                        .setMessage("Удалить запись '" + e.getName() + "'?")
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new AlertDialog.Builder(getApplicationContext())
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Удалить связанные заказы?")
                                            .setMessage("Вы уверены, что хотите удалить все записи заказов для этого события?")
                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    BookingRepository br = new BookingRepository(dbHelper);
                                                    br.deleteAllEventsBookings(eventId);
                                                    Toast.makeText(getBaseContext(), "Связанные заказы удалены", Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            })
                                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(getApplicationContext(), "Связанные заказы не удалены", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            }).show();
                                    dbr.delete(DBHelper.TABLE_EVENTS, eventId);
                                    getSupportLoaderManager().getLoader(0).forceLoad();
                                    Toast.makeText(getApplicationContext(), "Событие удалено", Toast.LENGTH_SHORT)
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
                    Toast.makeText(this, "При удалении события возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    //загружает только текущие и будущие заказы
    private void loadCurrentEvents() {
        listEvents = (ListView) findViewById(R.id.listViewAllEvents);
        eventAdapter = new EventAdapter(this, null);
        listEvents.setAdapter(eventAdapter);
        listEvents.setOnItemClickListener(this);
        registerForContextMenu(listEvents);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, dbr);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        eventAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private static class MyCursorLoader extends CursorLoader {

        EventRepository db;

        public MyCursorLoader(Context context, EventRepository db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllCurrentEvents();
            return cursor;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbr != null)
            dbr.close();
    }

}

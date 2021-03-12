package yummycherrypie.pl.activities.bookings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.pl.activities.events.AddEvent;
import yummycherrypie.pl.adapters.BookingAdapterWithDate;
import yummycherrypie.system.R;

public class BookingsFragment extends Fragment implements OnClickListener,
        OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private final int MENU_UPDATE = 1;
    private final int MENU_DELETE = 2;

    private DBHelper dbHelper;
    private BookingRepository dbr;

    private ListView listBookings;
    private Button btnAddBooking;
    private Button btnAddEvent;

    private BookingAdapterWithDate bookingAdapter;

    private static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    /**
     * 0 - показывать только прошедшие заказы
     * 1 - показывать текущие и будущие заказы за текущий месяц
     * 2 - показывать будущие заказы (не включая текущий месяц)
     * */
    private int pageNumber;

    public static BookingsFragment newInstance(int page) {
        BookingsFragment pageFragment = new BookingsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity().getApplicationContext());
        dbr = new BookingRepository(dbHelper);

        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookings, null);

        btnAddBooking = (Button) view.findViewById(R.id.buttonAddNewBooking);
        btnAddEvent = (Button) view.findViewById(R.id.buttonAddNewEvent);
        btnAddEvent.setOnClickListener(this);
        btnAddBooking.setOnClickListener(this);

        listBookings = (ListView) view.findViewById(R.id.listViewAllBookings);
        loadBookings();

        return view;
    }

    public void refreshCursor(){
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonAddNewBooking:
                try {
                    intent = new Intent(getActivity().getApplicationContext(), AddBooking.class);
                    intent.setData(Uri.parse("data:" + (new Date()).getTime() + "," + (-1)));
                    startActivityForResult(intent, 1);
                    break;
                }
                catch(Exception e)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "При создании заказа возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.buttonAddNewEvent:
                try{
                    intent = new Intent(getActivity().getApplicationContext(), AddEvent.class);
                    intent.setData(Uri.parse("eventId:" + (-1)));
                    startActivityForResult(intent, 1);
                    break;
                }
                catch(Exception e)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "При создании события возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
        Toast.makeText(getActivity().getApplicationContext(), "Заказ сохранен", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        try {
            Intent intent = new Intent(getActivity().getApplicationContext(), AddBooking.class);
            intent.setData(Uri.parse("data:" + (new Date()).getTime() + "," + id));
            startActivityForResult(intent, 1);
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "При создании заказа возникла ошибка", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.listViewAllBookings: {
                menu.add(0, MENU_UPDATE, 0, "Изменить");
                menu.add(0, MENU_DELETE, 0, "Удалить");
                break;
            }
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
                    Intent intent = new Intent(getActivity().getApplicationContext(), AddBooking.class);
                    AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                            .getMenuInfo();
                    intent.setData(Uri.parse("data:" + (new Date()).getTime() + "," + acmi.id));
                    startActivityForResult(intent, 1);
                    getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
                }
                catch(Exception e)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "При обновлении заказа возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case MENU_DELETE:
                try
                {
                    final AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    Booking b = dbr.getBooking(acmi.id);
                    new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Подтвердите удаление")
                        .setMessage("Удалить запись заказа от '" + DateExtension.getDateString(b.getDateLong()) + "'?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbr.delete(DBHelper.TABLE_BOOKINGS, acmi.id);
                                getActivity().getSupportLoaderManager().getLoader(0).forceLoad();
                                Toast.makeText(getActivity().getApplicationContext(), "Заказ удален", Toast.LENGTH_SHORT)
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
                    Toast.makeText(getActivity().getApplicationContext(), "При удалении заказа возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    //загружает только текущие и будущие заказы
    private void loadBookings() {

        bookingAdapter = new BookingAdapterWithDate(getActivity().getApplicationContext(), null);
        listBookings.setAdapter(bookingAdapter);
        listBookings.setOnItemClickListener(this);
        registerForContextMenu(listBookings);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(getActivity().getApplicationContext(), dbr, pageNumber);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        bookingAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public static class MyCursorLoader extends CursorLoader {

        BookingRepository db;
        int pageNumber;

        public MyCursorLoader(Context context, BookingRepository db, int pageNumber) {
            super(context);
            this.db = db;
            this.pageNumber = pageNumber;
        }

        @Override
        public Cursor loadInBackground() {
            switch (pageNumber){
                case 0: return db.getAllPastBookings();
                case 1: return db.getAllCurrentMonthBookings();
                case 2: return db.getAllFutureBookings();
            }
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbr != null)
            dbr.close();
    }

}
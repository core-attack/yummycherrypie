package yummycherrypie.pl.activities.events;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.base_classes.Event;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.dal.repositories.EventRepository;
import yummycherrypie.pl.CurrencyExtension;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.pl.ListViewExtension;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.activities.booking_men.AddBookingManDialog;
import yummycherrypie.pl.activities.bookings.AddBooking;
import yummycherrypie.pl.activities.bookings.AddOrChooseBooking;
import yummycherrypie.pl.adapters.BookingAdapterWithDate;
import yummycherrypie.pl.adapters.SpinnerAdapter;
import yummycherrypie.system.R;

/**
 * Created by piskarev on 17.09.2015.
 */
public class AddEvent extends BaseFragmentActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        AddBookingManDialog.IDialogListener {

    private final int MENU_UPDATE = 1;
    private final int MENU_DELETE = 2;

    private DBHelper dbHelper;
    private EventRepository eventRepo;
    private BookingManRepository bmRepo;
    private BookingRepository bookingRepo;

    private TextView tvEventDate;
    private EditText etEventName;
    private EditText etEventPlace;
    private EditText etEventPrice;
    private EditText etEventRecipePrice;
    private LinearLayout btnSaveEvent;
    private LinearLayout btnAddBookingMan;
    private LinearLayout btnAddBooking;
    private Spinner spBookingMen;
    private ListView listViewBookings;

    private Date currentDate;
    private int DIALOG_DATE = 1;
    private long bookingManId = -1;
    private long eventId = -1;

    private Map<String, Long> bmenMap;
    private Map<Integer, Long> bmenIds;

    BookingAdapterWithDate ba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        dbHelper = new DBHelper(this);
        eventRepo = new EventRepository(dbHelper);
        bmRepo = new BookingManRepository(dbHelper);
        bookingRepo = new BookingRepository(dbHelper);

        currentDate = Calendar.getInstance().getTime();

        tvEventDate = (TextView) findViewById(R.id.tvEventDate);
        etEventName = (EditText) findViewById(R.id.etEventName);
        etEventPlace = (EditText) findViewById(R.id.etEventPlace);
        etEventPrice = (EditText) findViewById(R.id.etEventPrice);
        etEventRecipePrice = (EditText) findViewById(R.id.etEventRecipePrice);
        btnSaveEvent = (LinearLayout) findViewById(R.id.buttonSaveEvent);
        btnAddBookingMan = (LinearLayout) findViewById(R.id.btnAddEventBookingMan);
        btnAddBooking = (LinearLayout) findViewById(R.id.buttonAddBookingToEvent);

        btnSaveEvent.setOnClickListener(this);
        btnAddBookingMan.setOnClickListener(this);
        btnAddBooking.setOnClickListener(this);

        if (getIntent().getData() != null) {
            String data = getIntent().getData().getSchemeSpecificPart();
            String[] values = data.split(",");
            eventId = values.length > 0 ? Long.valueOf(values[0]) : -1;
        }
        if (eventId == -1){
            Event event = new Event();
            event.setCreateDate(new Date().getTime());
            tvEventDate.setText(DateExtension.getDate(currentDate.getTime()));
            eventId = eventRepo.insert(dbHelper.TABLE_EVENTS, event.getInsertedColumns());
            updateSpinnerBookingMan(null);
        }
        else{
            Event event = eventRepo.getEvent(eventId);
            tvEventDate.setText(DateExtension.getDate(event.getDateLong()));
            etEventName.setText(event.getName());
            etEventPlace.setText(event.getPlace());
            etEventPrice.setText(CurrencyExtension.getCurrencyDecimalWithoutRouble(event.getPrice()));
            tvEventDate.setText(DateExtension.getDate(event.getDateLong()));
            BookingMan bm = bmRepo.getBookingMan(event.getBookingManId());
            updateSpinnerBookingMan(bm != null ? bm : null);
            CalcSummaryPrices(false);
        }
        loadBooking();
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonSaveEvent:
                try {
                    updateEvent();
                    intent = new Intent();
                    intent.putExtra("all_is_ok", true);
                    setResult(RESULT_OK, intent);
                    finish();
                    Toast.makeText(this, "Событие сохранено", Toast.LENGTH_SHORT)
                            .show();
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При обновлении события возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.btnAddEventBookingMan:
                try{
                    showAddBookingManDialog();
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "При добавлении заказчика возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.buttonAddBookingToEvent:
                try{
                    updateEvent();
                    intent = new Intent(this, AddOrChooseBooking.class);
                    intent.setData(Uri.parse("eventId:" + eventId));
                    startActivityForResult(intent, 1);
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При добавлении заказа возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    private void updateEvent(){
        try {
            Event ev = new Event(
                    etEventName.getText().toString(),
                    etEventPlace.getText().toString(),
                    currentDate.getTime(),
                    bookingManId,
                    etEventPrice.getText().toString().length() > 0 ?
                            nf.parse(etEventPrice.getText().toString()).doubleValue() :
                            0
            );
            ev.setUpdateDate(new Date().getTime());
            eventRepo.update(DBHelper.TABLE_EVENTS, ev.getUpdatedColumns(), eventId);
        } catch(Exception e){
            Toast.makeText(this, "Не удалось обновить событие!", Toast.LENGTH_SHORT).show();
        }
    }

    DatePickerDialog.OnDateSetListener myCallBackDate = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            //todo переписать
            monthOfYear = monthOfYear + 1;
            currentDate = DateExtension.convertToDate(String.format("%s.%s.%s",
                            dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth,
                            monthOfYear < 10 ? "0" + monthOfYear : monthOfYear,
                            year),
                    "dd.MM.yyyy");
            tvEventDate.setText(String.format("%s.%s.%s",
                    dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth,
                    monthOfYear < 10 ? "0" + monthOfYear : monthOfYear,
                    year));
        }
    };

    //в эмуляторе Double.parseDouble работает без ошибок
    //а точнее текстбокс не позволяет ввести никакие данные, кроме "."
    //public static double myConvert(String doubleStr, Context context){
    //    double result = 0;
    //    try {
    //        if (doubleStr.equals(""))
    //            return result;
    //        String newString = "";
    //        if (doubleStr.indexOf(',') != -1) {
    //            newString = doubleStr.replace(',', '.');
    //            if (newString.split(".").length > 2)
    //                throw new Exception("В строке не может быть больше 1 разделителя целой и дробной части!");
    //            return Double.parseDouble(newString);
    //        }
    //        else
    //            return Double.parseDouble(doubleStr);
    //    }
    //    catch (Exception e)
    //    {
    //        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT)
    //                .show();
    //    }
    //
    //    return result;
    //}

    public void onclickDialogDate(View view) {
        showDialog(DIALOG_DATE);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            Calendar c = Calendar.getInstance();
            return new DatePickerDialog(this, myCallBackDate,
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if(data.getData() != null) {
            if (data.getData().getScheme().equals("bookingManId")) {
                bookingManId = Long.parseLong(data.getData().getSchemeSpecificPart());
                updateSpinnerBookingMan(bmRepo.getBookingMan(bookingManId));
            } else if (data.getData().getScheme().equals("bookingId")) {
                long bookingId = Long.parseLong(data.getData().getSchemeSpecificPart());
                eventRepo.setEventForBooking(bookingId, eventId);
                CalcSummaryPrices(true);
            }
        }
        loadBooking();
    }

    /**
     * загружает привязанные к событию заказы
     */
    private void loadBooking(){
        listViewBookings = (ListView) findViewById(R.id.listViewEventBookings);
        ba = new BookingAdapterWithDate(this, bookingRepo.getAllEventsBookings(eventId));
        listViewBookings.setAdapter(ba);
        listViewBookings.setOnItemClickListener(this);
        registerForContextMenu(listViewBookings);
        getSupportLoaderManager().initLoader(0, null, this);
        ListViewExtension.setListViewHeightBasedOnChildren(this, listViewBookings, ba);
    }

    private void updateSpinnerBookingMan(BookingMan currentBookingMan) {

        ArrayList<BookingMan> bmen = bmRepo.getAllBookingMen();
        ArrayList<String> sBMen = new ArrayList<String>();
        bmenMap = new HashMap<String, Long>();
        bmenIds = new HashMap<Integer, Long>();
        int i = 0;
        for (BookingMan bm : bmen) {
            sBMen.add(bm.getNameWithPhone());
            bmenMap.put(bm.getNameWithPhone(), bm.getId());
            bmenIds.put(i, bm.getId());
            i++;
        }

        if (bmenIds.size() > 0 && bookingManId == -1)
            bookingManId = bmenIds.get(0);

        SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, sBMen);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spBookingMen = (Spinner) findViewById(R.id.spinnerEventBookingMan);
        spBookingMen.setAdapter(adapter);
        spBookingMen.setPrompt("Заказчик");
        if (currentBookingMan != null)
            spBookingMen.setSelection(adapter.getPosition(currentBookingMan.getNameWithPhone()));
        else
            spBookingMen.setSelection(0);

        if (bmenMap.size() > 0 && bmenMap.containsKey(spBookingMen.getItemAtPosition(spBookingMen.getSelectedItemPosition()))
                && bookingManId == -1)
            bookingManId = bmenMap.get(spBookingMen.getItemAtPosition(spBookingMen.getSelectedItemPosition()).toString());

        spBookingMen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (bmenMap != null) {
                        bookingManId = bmenMap.get(parent.getItemAtPosition(position).toString());
                    }
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Что то пошло не так в spBookingMen.setOnItemSelectedListener", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bookingManId = -1;
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi;
        switch (item.getItemId()) {
            case MENU_UPDATE:
                try {
                    Intent intent = new Intent(this, AddBooking.class);
                    acmi = (AdapterView.AdapterContextMenuInfo) item
                            .getMenuInfo();
                    Booking b = bookingRepo.getBooking(acmi.id);
                    intent.setData(Uri.parse("data:" + b.getDateLong() + "," + acmi.id));
                    startActivityForResult(intent, 1);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При обновлении заказчика возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case MENU_DELETE:
                try
                {
                    acmi = (AdapterView.AdapterContextMenuInfo) item
                            .getMenuInfo();
                    Booking b = bookingRepo.getBooking(acmi.id);
                    b.setEventId(-1);
                    bookingRepo.update(DBHelper.TABLE_BOOKINGS, b.getUpdatedColumns(), acmi.id);
                    getSupportLoaderManager().getLoader(0).forceLoad();
                    Toast.makeText(this, "Заказ удален из события", Toast.LENGTH_SHORT)
                            .show();
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При удалении заказчика возникла ошибка", Toast.LENGTH_SHORT)
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
            Intent intent = new Intent(this, AddBooking.class);
            Booking b = bookingRepo.getBooking(id);
            intent.setData(Uri.parse("data:" + b.getDateLong() + "," + id));
            startActivityForResult(intent, 1);
        }
        catch(Exception e)
        {
            Toast.makeText(this, "При создании заказчика возникла ошибка", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void CalcSummaryPrices(boolean showEventPrice){
        double[] prices = eventRepo.getSummaryPrices(eventId);
        if (prices.length > 1) {
            etEventRecipePrice.setText(CurrencyExtension.getCurrencyWithoutRouble(prices[0]));
            if (showEventPrice)
                etEventPrice.setText(CurrencyExtension.getCurrencyWithoutRouble(prices[1]));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()) {
            case R.id.listViewEventBookings:
                menu.add(0, MENU_UPDATE, 0, "Изменить");
                menu.add(0, MENU_DELETE, 0, "Удалить");
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, bookingRepo, eventId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ba.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private static class MyCursorLoader extends CursorLoader {

        BookingRepository db;
        long eventId;

        public MyCursorLoader(Context context, BookingRepository db, long eventId) {
            super(context);
            this.db = db;
            this.eventId = eventId;
        }

        @Override
        public Cursor loadInBackground() {
            return db.getAllEventsBookings(eventId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (eventRepo != null)
            eventRepo.close();
    }

    public void showAddBookingManDialog() {
        DialogFragment dialog = AddBookingManDialog.newInstance();
        dialog.show(getSupportFragmentManager(), "AddBookingManDialog");
    }

    @Override
    public void onBookingManDialogPositiveClick(DialogFragment dialog, long bookingManId) {
        this.bookingManId = bookingManId;
        updateSpinnerBookingMan(bmRepo.getBookingMan(bookingManId));
    }

    @Override
    public void onBookingManDialogNegativeClick(DialogFragment dialog) {

    }

}

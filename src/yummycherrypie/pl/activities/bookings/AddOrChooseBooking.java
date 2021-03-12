package yummycherrypie.pl.activities.bookings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.activities.events.AddEvent;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.business_logic.Extensions.OtherExtensions;
import yummycherrypie.system.R;
import yummycherrypie.pl.adapters.BookingAdapterWithDateAndCheckbox;

/**
 * Created by piskarev on 21.09.2015.
 */
public class AddOrChooseBooking extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Button btnAddNewBoooking;
    private Button btnAddSelected;
    private ListView listViewBookings;

    private  DBHelper dbHelper;
    private BookingRepository dbr;

    private BookingAdapterWithDateAndCheckbox bAdapter;

    private long bookingId = -1;
    private long eventId = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_choose_booking);

        try {
            //добавляет кнопку "назад" в action bar
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch(NullPointerException e)
        {
            Toast.makeText(this, OtherExtensions.ERR_ADD_BACK_BUTTON_TO_ACTION_BAR, Toast.LENGTH_SHORT)
                    .show();
        }

        if (getIntent().getData() != null) {
            String data = getIntent().getData().getSchemeSpecificPart();
            String[] values = data.split(",");
            eventId = values.length > 0 ? Long.valueOf(values[0]) : -1;
        }

        btnAddNewBoooking = (Button) findViewById(R.id.buttonAddNewBooking);
        btnAddSelected = (Button) findViewById(R.id.buttonAddSelectedBooking);
        btnAddNewBoooking.setOnClickListener(this);
        btnAddSelected.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        dbr = new BookingRepository(dbHelper);

        bookingId = Long.valueOf(getIntent().getData().getSchemeSpecificPart());

        loadCurrentBookings();

    }

    //загружает только текущие и будущие заказы
    private void loadCurrentBookings() {
        //не смог найти решения, которое бы позволяло для CursorAdapter использовать мультиселект
        listViewBookings = (ListView) findViewById(R.id.listViewBookings);
        bAdapter = new BookingAdapterWithDateAndCheckbox(this, dbr.getAllCurrentBookingsArrayList());
        listViewBookings.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewBookings.setAdapter(bAdapter);
        listViewBookings.setOnItemClickListener(this);
    }

    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buttonAddNewBooking:
                try {
                    intent = new Intent(this, AddBooking.class);
                    intent.setData(Uri.parse("data:" + new Date().getTime() + "," + (-1)));
                    startActivityForResult(intent, 1);
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При добавлении заказа возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.buttonAddSelectedBooking:
                try{
                    boolean nothingSelected = true;
                    List<Integer> sbArray = bAdapter.getCheckedItemPositions();
                    for (int i = 0; i < sbArray.size(); i++) {
                        nothingSelected = false;
                        Booking b = (Booking)listViewBookings.getItemAtPosition(sbArray.get(i));
                        b.setEventId(eventId);
                        dbr.update(DBHelper.TABLE_BOOKINGS, b.getUpdatedColumns(), b.getId());
                    }
                    if (nothingSelected){
                        Toast.makeText(this, "Выберите хотя бы один элемент!", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else{
                        intent = new Intent(this, AddEvent.class);
                        intent.setData(Uri.parse("bookingId:" + Long.valueOf(-1).toString()));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                catch(Exception e)
                {
                    Toast.makeText(this, "При добавлении заказов возникла ошибка", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Intent intent = new Intent(this, AddEvent.class);
        intent.setData(Uri.parse("bookingId:" + id));
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadCurrentBookings();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbr != null)
            dbr.close();
    }
}

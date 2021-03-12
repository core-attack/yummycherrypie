package yummycherrypie.pl.activities.events;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.base_classes.Event;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.dal.repositories.EventRepository;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.activities.booking_men.AddOrChooseBookingMan;
import yummycherrypie.pl.adapters.EventAdapter;
import yummycherrypie.pl.adapters.SpinnerAdapter;
import yummycherrypie.system.R;

public class AddOrChooseEvent extends BaseFragmentActivity implements OnClickListener,
		AdapterView.OnItemClickListener {

	private int DIALOG_DATE = 1;

	private LinearLayout btnSaveEvent;
	private LinearLayout btnAddBookingMan;
	private EditText tbEventName;
	private EditText tbEventPlace;
	private TextView tbEventDate;
	private ListView listViewEvent;
	private Spinner spBookingMen;

	private DBHelper dbHelper;
	private EventRepository eventRepo;
	private BookingManRepository bmRepo;

	private long eventId = -1;
	private long bookingManId = -1;

	private Map<String, Long> bmenMap;
	private Map<Integer, Long> bmenIds;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_or_choose_event);

		btnSaveEvent = (LinearLayout) findViewById(R.id.buttonSaveEvent);
		tbEventName = (EditText) findViewById(R.id.etEventName);
		tbEventPlace = (EditText) findViewById(R.id.etEventPlace);
		tbEventDate = (TextView) findViewById(R.id.tvEventDate);
		listViewEvent = (ListView) findViewById(R.id.listViewEvents);
		btnAddBookingMan = (LinearLayout) findViewById(R.id.btnAddEventBookingMan);

		listViewEvent.setOnItemClickListener(this);

		btnSaveEvent.setOnClickListener(this);
		btnAddBookingMan.setOnClickListener(this);

		dbHelper = new DBHelper(this);
		eventRepo = new EventRepository(dbHelper);
		bmRepo = new BookingManRepository(dbHelper);

		tbEventDate.setText(DateExtension.getDate(Calendar.getInstance().getTimeInMillis()));

		eventId = Long.valueOf(getIntent().getData().getSchemeSpecificPart());
		loadEvents();
		updateSpinnerBookingMan("");
	}

	private void loadEvents(){
		Cursor cursor = eventRepo.getAllCurrentEvents();
		listViewEvent = (ListView) findViewById(R.id.listViewEvents);
		listViewEvent.setAdapter(new EventAdapter(this, cursor));
		listViewEvent.setOnItemClickListener(this);
		registerForContextMenu(listViewEvent);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.buttonSaveEvent:
				try {
					Event bt = new Event(
							tbEventName.getText().toString(),
							tbEventPlace.getText().toString(),
							DateExtension.getDateLong(tbEventDate.getText().toString()),
							bookingManId
					);
					bt.setCreateDate(new Date().getTime());
					eventId = eventRepo.insert(DBHelper.TABLE_EVENTS, bt.getInsertedColumns());
					intent = new Intent();
					intent.setData(Uri.parse("eventId:" + eventId));
					setResult(RESULT_OK, intent);
					finish();
					Toast.makeText(this, "Событие сохранено", Toast.LENGTH_SHORT)
							.show();
				}
				catch(Exception e) {
					Toast.makeText(this, "При обновлении типа заказа возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case R.id.btnAddEventBookingMan:
				try {
					intent = new Intent(this, AddOrChooseBookingMan.class);
					intent.setData(Uri.parse("bookingManId:" + (-1)));
					startActivityForResult(intent, 1);
				}
				catch(Exception e){
					Toast.makeText(this, "При добавлении заказчика возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
		}
	}

	private void updateSpinnerBookingMan(String bookingManName) {

		ArrayList<BookingMan> bmen = bmRepo.getAllBookingMen();
		ArrayList<String> sBMen = new ArrayList<String>();
		bmenMap = new HashMap<String, Long>();
		bmenIds = new HashMap<Integer, Long>();
		int i = 0;
		for (BookingMan bm : bmen) {
			sBMen.add(bm.getName());
			bmenMap.put(bm.getName(), bm.getId());
			bmenIds.put(i, bm.getId());
			i++;
		}
		//устанавливаем первого заказчика
		if (bmenIds.size() > 0)
			bookingManId = bmenIds.get(0);

		// адаптер
		SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, sBMen);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spBookingMen = (Spinner) findViewById(R.id.spinnerEventBookingMan);
		spBookingMen.setAdapter(adapter);
		spBookingMen.setPrompt("Заказчик");
		if (!bookingManName.equals(""))
			spBookingMen.setSelection(adapter.getPosition(bookingManName));
		else
			spBookingMen.setSelection(0);

		if (bmenMap.size() > 0 && bmenMap.containsKey(spBookingMen.getItemAtPosition(spBookingMen.getSelectedItemPosition())))
			bookingManId = bmenMap.get(spBookingMen.getItemAtPosition(spBookingMen.getSelectedItemPosition()).toString());

		spBookingMen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					if (bmenMap != null) {
						bookingManId = bmenMap.get(parent.getItemAtPosition(position).toString());
					}
				} catch (Exception e) {
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		Intent intent = new Intent();
		intent.setData(Uri.parse("eventId:" + id));
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * получение значения из дочерних активити
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}

		if (data.getData().getScheme().equals("bookingManId")) {
			bookingManId = Long.parseLong(data.getData().getSchemeSpecificPart());
			updateSpinnerBookingMan(bmRepo.getBookingMan(bookingManId).getName());
		}
	}

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

	DatePickerDialog.OnDateSetListener myCallBackDate = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {
			monthOfYear = monthOfYear + 1;
			tbEventDate.setText(String.format("%s.%s.%s",
					dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth,
					monthOfYear < 10 ? "0" + monthOfYear : monthOfYear,
					year));
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (eventRepo != null)
			eventRepo.close();
	}
}

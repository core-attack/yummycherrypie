package yummycherrypie.pl.activities.bookings;

import java.util.Date;

import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.activities.events.AddEvent;
import yummycherrypie.pl.adapters.BookingAdapterWithDate;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.system.R;

import android.content.Context;
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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class BookingsTab extends BaseFragmentActivity implements OnClickListener,
OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	private final int MENU_UPDATE = 1;
	private final int MENU_DELETE = 2;

	private DBHelper dbHelper;
	private BookingRepository dbr;

	private ListView listBookings;
	private Button btnAddBooking;
	private Button btnAddEvent;

	private BookingAdapterWithDate bookingAdapter;

	/**
	 * 0 - показывать только прошедшие заказы
	 * 1 - показывать текущие и будущие заказы за текущий месяц
	 * 2 - показывать будущие заказы (не включая текущий месяц)
	 * */
	private int tabNumber = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookings);

		dbHelper = new DBHelper(this);
		dbr = new BookingRepository(dbHelper);

		if (getIntent().getData() != null) {
			String data = getIntent().getData().getSchemeSpecificPart();
			tabNumber = Integer.valueOf(data);
		}

		btnAddBooking = (Button) findViewById(R.id.buttonAddNewBooking);
		btnAddEvent = (Button) findViewById(R.id.buttonAddNewEvent);
		btnAddEvent.setOnClickListener(this);
		btnAddBooking.setOnClickListener(this);

		listBookings = (ListView) findViewById(R.id.listViewAllBookings);
		loadBookings();

		//listBookings.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
//
		//	public void onSwipeRight() {
		//		Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
		//		Intent intent = new Intent();
//
		//		if (tabNumber < 2)
		//			intent.setData(Uri.parse("tab:" + (tabNumber + 1)));
		//		else
		//			intent.setData(Uri.parse("tab:" + (tabNumber)));
		//		setResult(RESULT_OK, intent);
		//		finish();
		//	}
//
		//	public void onSwipeLeft() {
		//		Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
		//		Intent intent = new Intent();
//
		//		if (tabNumber > 0)
		//			intent.setData(Uri.parse("tab:" + (tabNumber - 1)));
		//		else
		//			intent.setData(Uri.parse("tab:" + (tabNumber)));
		//		setResult(RESULT_OK, intent);
		//		finish();
		//	}
		//});

	}

	public void refreshCursor(){
		getSupportLoaderManager().restartLoader(0, null, this);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.buttonAddNewBooking:
			try {
				intent = new Intent(this, AddBooking.class);
				intent.setData(Uri.parse("data:" + (new Date()).getTime() + "," + (-1)));
				startActivityForResult(intent, 1);
				break;
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При создании заказа возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
			break;
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		getSupportLoaderManager().getLoader(0).forceLoad();
		Toast.makeText(this, "Заказ сохранен", Toast.LENGTH_SHORT)
		.show();
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		switch (v.getId()) {
			case R.id.listViewAllBookings: {
				menu.add(0, MENU_UPDATE, 0, "Изменить");
				menu.add(0, MENU_DELETE, 0, "Удалить");
				break;
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo acmi;
		switch (item.getItemId()) {
		case MENU_UPDATE:
			try {
				Intent intent = new Intent(this, AddBooking.class);
				acmi = (AdapterContextMenuInfo) item
						.getMenuInfo();
				intent.setData(Uri.parse("data:" + (new Date()).getTime() + "," + acmi.id));
				startActivityForResult(intent, 1);
				getSupportLoaderManager().getLoader(0).forceLoad();
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При обновлении заказа возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		case MENU_DELETE:
			try
			{
				acmi = (AdapterContextMenuInfo) item
						.getMenuInfo();
				dbr.delete(DBHelper.TABLE_BOOKINGS, acmi.id);
				getSupportLoaderManager().getLoader(0).forceLoad();
				Toast.makeText(this, "Заказ удален", Toast.LENGTH_SHORT)
				.show();
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При удалении заказа возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	//загружает только текущие и будущие заказы
	private void loadBookings() {

		bookingAdapter = new BookingAdapterWithDate(this, null);
		listBookings.setAdapter(bookingAdapter);
		listBookings.setOnItemClickListener(this);
		registerForContextMenu(listBookings);
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
		return new MyCursorLoader(this, dbr, tabNumber);
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
		int tabNumber;

		public MyCursorLoader(Context context, BookingRepository db, int tabNumber) {
			super(context);
			this.db = db;
			this.tabNumber = tabNumber;
		}

		@Override
		public Cursor loadInBackground() {
			switch (tabNumber){
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



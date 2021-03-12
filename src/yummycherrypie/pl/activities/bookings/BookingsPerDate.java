package yummycherrypie.pl.activities.bookings;

import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.activities.events.AddEvent;
import yummycherrypie.pl.adapters.BookingAdapterWithDate;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.pl.ListViewExtension;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class BookingsPerDate extends BaseFragmentActivity implements OnClickListener,
OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	private final int MENU_UPDATE = 1;
	private final int MENU_DELETE = 2;

	private DBHelper dbHelper;
	private BookingRepository dbr;

	private BookingAdapterWithDate bookingAdapter;

	private ListView listBookings;
	private Button btnAddBooking;
	private Button btnAddEvent;
	private TextView tvDate;

	private long bookingDate;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookings_per_date);

		tvDate = (TextView)findViewById(R.id.textViewBookingPerDate);
		btnAddBooking = (Button) findViewById(R.id.buttonAddNewBooking);
		btnAddEvent = (Button) findViewById(R.id.buttonAddNewEvent);
		btnAddBooking.setOnClickListener(this);
		btnAddEvent.setOnClickListener(this);

		dbHelper = new DBHelper(this);
		dbr = new BookingRepository(dbHelper);

		String data = getIntent().getData().getSchemeSpecificPart();
		String[] values = data.split(",");
		bookingDate = values.length > 0 ? Long.valueOf(values[0]) : -1;
		if (bookingDate == -1)
			Toast.makeText(this, "Не удалось прочитать дату заказа", Toast.LENGTH_SHORT)
					.show();

		tvDate.setText("Заказы на " + DateExtension.getDateString(bookingDate));

		loadBookings();
	}
	
	private void loadBookings(){
		bookingAdapter = new BookingAdapterWithDate(this, null);
		listBookings = (ListView) findViewById(R.id.listViewBookingsPerDate);
		listBookings.setAdapter(bookingAdapter);
		listBookings.setOnItemClickListener(this);
		registerForContextMenu(listBookings);
		ListViewExtension.setListViewHeightBasedOnChildren(this, listBookings);
		getSupportLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		switch (v.getId()) {
		case R.id.listViewBookingsPerDate:
			menu.add(0, MENU_UPDATE, 0, "Изменить");
			menu.add(0, MENU_DELETE, 0, "Удалить");

			break;
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
				intent.setData(Uri.parse("data:" + bookingDate + "," + acmi.id));
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


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
	  return new MyCursorLoader(this, dbr, bookingDate);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		bookingAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
	  
	static class MyCursorLoader extends CursorLoader {

	  BookingRepository db;
	  long date;
	    
	  public MyCursorLoader(Context context, BookingRepository db, long date) {
	    super(context);
	    this.db = db;
	    this.date = date;
	  }
	    
	  @Override
	  public Cursor loadInBackground() {
	    Cursor cursor = db.getAllBookings(date);
	    return cursor;
	  }
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try{
			Intent intent = new Intent(this, AddBooking.class);
			intent.setData(Uri.parse("data:" + bookingDate + "," + id));
			startActivityForResult(intent, 1);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "При создании заказа возникла ошибка", Toast.LENGTH_SHORT)
					.show();
		}
		
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.buttonAddNewBooking:
				try{
					intent = new Intent(this, AddBooking.class);
					intent.setData(Uri.parse("data:" + bookingDate + "," + (-1)));
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
	
	// получаем значение типа торта из другого activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		//loadBookings();
		getSupportLoaderManager().getLoader(0).forceLoad();
		Toast.makeText(this, "Заказ сохранен", Toast.LENGTH_SHORT)
		.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbr != null)
			dbr.close();
	}
}

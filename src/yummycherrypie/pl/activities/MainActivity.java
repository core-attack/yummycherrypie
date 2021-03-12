package yummycherrypie.pl.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import yummycherrypie.business_logic.Extensions.OtherExtensions;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.pl.OnSwipeTouchListener;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.activities.booking_men.BookingMen;
import yummycherrypie.pl.activities.booking_types.BookingTypes;
import yummycherrypie.pl.activities.bookings.AddBooking;
import yummycherrypie.pl.activities.bookings.BookingsPager;
import yummycherrypie.pl.activities.bookings.BookingsPerDate;
import yummycherrypie.pl.activities.components.Components;
import yummycherrypie.pl.activities.events.Events;
import yummycherrypie.pl.activities.recipes.Recipes;
import yummycherrypie.pl.activities.statistics.Statistics;
import yummycherrypie.pl.activities.tools.Tools;
import yummycherrypie.pl.adapters.CalendarAdapter;
import yummycherrypie.pl.notifications.BaseNotification;
import yummycherrypie.system.R;


public class MainActivity extends FragmentActivity implements OnClickListener {

	private DBHelper dbHelper;
	private BookingRepository dbr;

	private Button btnComponents;
	private Button btnAllBookings;
	private Button btnRecipes;
	private Button btnBookingMen;
	private Button btnBookingTypes;
	private Button btnEvents;
	private Button btnStatistic;

	private ProgressDialog progress;
	private Handler handler;


	public Calendar month;
	public CalendarAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		try {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(false);
			if (getTitle() != null) {
				SpannableString s = new SpannableString(getTitle());
				s.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(getApplicationContext(), StringExtension.DEFAULT_FONT), 0, s.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				actionBar.setTitle(s);
			}
		}
		catch(NullPointerException e)
		{
			Toast.makeText(getApplicationContext(), OtherExtensions.ERR_ADD_BACK_BUTTON_TO_ACTION_BAR, Toast.LENGTH_SHORT)
					.show();
		}

		dbHelper = new DBHelper(getApplicationContext());
		dbr = new BookingRepository(dbHelper);

		btnComponents = (Button) findViewById(R.id.btnOpenComponentsLayout);
		btnRecipes = (Button) findViewById(R.id.btnOpenRecipesLayout);
		btnAllBookings = (Button) findViewById(R.id.btnOpenAllBookingsLayout);
		btnBookingMen = (Button) findViewById(R.id.btnOpenBookingMenLayout);
		btnBookingTypes = (Button) findViewById(R.id.btnOpenBookingTypeLayout);
		btnEvents = (Button) findViewById(R.id.btnOpenEventLayout);
		btnStatistic = (Button) findViewById(R.id.btnOpenStatistics);

		btnComponents.setOnClickListener(this);
		btnRecipes.setOnClickListener(this);
		btnAllBookings.setOnClickListener(this);
		btnBookingMen.setOnClickListener(this);
		btnBookingTypes.setOnClickListener(this);
		btnEvents.setOnClickListener(this);
		btnStatistic.setOnClickListener(this);

		fillCalendar();

		//startServices();

	}

	private void startServices(){
		Intent intent = new Intent(this, BaseNotification.class);
		startService(intent);
	}

	public void moveToNextMonth(){
		if (month.get(Calendar.MONTH) == month.getActualMaximum(Calendar.MONTH)) {
			month.set((month.get(Calendar.YEAR) + 1), month.getActualMinimum(Calendar.MONTH), 1);
		} else {
			month.set(Calendar.MONTH, month.get(Calendar.MONTH) + 1);
		}
		refreshCalendar();
	}

	public void moveToPreviousMonth(){
		if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) {
			month.set((month.get(Calendar.YEAR) - 1), month.getActualMaximum(Calendar.MONTH), 1);
		} else {
			month.set(Calendar.MONTH, month.get(Calendar.MONTH)-1);
		}
		refreshCalendar();
	}

	private void fillCalendar(){
		month = Calendar.getInstance();

		adapter = new CalendarAdapter(this, month);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		TextView title  = (TextView) findViewById(R.id.title);
		title.setText(String.format("%s %d", DateExtension.MONTHS[month.get(Calendar.MONTH)], month.get(Calendar.YEAR)));

		LinearLayout previous  = (LinearLayout) findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				moveToPreviousMonth();
			}
		});

		LinearLayout next  = (LinearLayout) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				moveToNextMonth();

			}
		});

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				TextView fullDate = (TextView) v.findViewById(R.id.fulldate);
				if (fullDate != null && !fullDate.getText().equals("")) {
					Date d = DateExtension.getDate(fullDate.getText().toString());
					if (d != null) {
						Calendar selectedDate = Calendar.getInstance();
						selectedDate.setTime(d);
						selectedDate.set(Calendar.HOUR_OF_DAY, OtherExtensions.DEFAULT_BOOKING_TIME);
						selectedDate.set(Calendar.MINUTE, selectedDate.getActualMinimum(Calendar.MINUTE));
						selectedDate.set(Calendar.SECOND, selectedDate.getActualMinimum(Calendar.SECOND));

						Format format = new SimpleDateFormat("dd.MM.yyyy");
						Toast.makeText(getApplicationContext(), format.format(selectedDate.getTime()), Toast.LENGTH_SHORT)
								.show();

						GoToCreatingBooking(selectedDate.getTimeInMillis());
					} else {
						Toast.makeText(getApplicationContext(), "Не удалось определить дату", Toast.LENGTH_SHORT)
								.show();
					}
				}

			}
		});

		gridview.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {

			public void onSwipeRight() {
				moveToPreviousMonth();
			}

			public void onSwipeLeft() {
				moveToNextMonth();
			}
		});
	}

	//переходим в активити по созданию заказа
	private void GoToCreatingBooking(long dateInMilliseconds){
		//если нет заказов на эту дату, открывай создание заказа, если есть, то список заказов
		try	{
			Cursor c = dbr.getAllBookings(dateInMilliseconds);
			Intent intent;
			if (!c.moveToFirst()){
				intent = new Intent(this, AddBooking.class);
			}
			else
				intent = new Intent(this, BookingsPerDate.class);
			intent.setData(Uri.parse("data:" + dateInMilliseconds + "," + (-1)));
			startActivityForResult(intent, 1);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "При создании заказа возникла ошибка", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void refreshCalendar()
	{
		TextView title  = (TextView) findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();

		title.setText(String.format("%s %d", DateExtension.MONTHS[month.get(Calendar.MONTH)], month.get(Calendar.YEAR)));
	}

	@Override
	public void onClick(View v) {

		Intent intent;
		switch (v.getId()) {
			case R.id.btnOpenComponentsLayout:
				Log.d("myLogs", "components");
				intent = new Intent(this, Components.class);
				startActivity(intent);
				break;
			case R.id.btnOpenRecipesLayout:
				Log.d("myLogs", "recipes");
				intent = new Intent(this, Recipes.class);
				startActivity(intent);
				break;
			case R.id.btnOpenAllBookingsLayout:
				Log.d("myLogs", "allbookings");
				intent = new Intent(this, BookingsPager.class);
				startActivity(intent);
				break;
			case R.id.btnOpenBookingMenLayout:
				Log.d("myLogs", "bookingMen");
				intent = new Intent(this, BookingMen.class);
				startActivity(intent);
				break;
			case R.id.btnOpenBookingTypeLayout:
				Log.d("myLogs", "bookingType");
				intent = new Intent(this, BookingTypes.class);
				startActivity(intent);
				break;
			case R.id.btnOpenEventLayout:
				Log.d("myLogs", "event");
				intent = new Intent(this, Events.class);
				startActivity(intent);
				break;
			case R.id.btnOpenStatistics:
				Log.d("myLogs", "Statistics");
				intent = new Intent(this, Statistics.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		refreshCalendar();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshCalendar();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbr != null)
			dbr.close();
		if (adapter != null)
			adapter.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.general_menu, menu);

		Typeface face = Typeface.createFromAsset(getAssets(), StringExtension.DEFAULT_FONT);

		String illegalArgumentExceptionReason = "LG";

		SpannableString title = new SpannableString(getString(R.string.menu_refresh));
		title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		MenuItem menuItem = menu.findItem(R.id.menu_refresh);
		menuItem.setTitle(BaseActivity.isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_refresh) : title);

		title = new SpannableString(getString(R.string.menu_add_booking));
		title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		menuItem = menu.findItem(R.id.menu_add_booking);
		menuItem.setTitle(BaseActivity.isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_add_booking) : title);

		title = new SpannableString(getString(R.string.menu_all_bookings));
		title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		menuItem = menu.findItem(R.id.menu_all_bookings);
		menuItem.setTitle(BaseActivity.isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_all_bookings) : title);

		title = new SpannableString(getString(R.string.menu_recipes));
		title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		menuItem = menu.findItem(R.id.menu_recipes);
		menuItem.setTitle(BaseActivity.isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_recipes) : title);

		title = new SpannableString(getString(R.string.menu_components));
		title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		menuItem = menu.findItem(R.id.menu_components);
		menuItem.setTitle(BaseActivity.isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_components) : title);

		title = new SpannableString(getString(R.string.menu_booking_men));
		title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		menuItem = menu.findItem(R.id.menu_booking_men);
		menuItem.setTitle(BaseActivity.isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_booking_men) : title);

		title = new SpannableString(getString(R.string.menu_tools));
		title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		menuItem = menu.findItem(R.id.menu_tools);
		menuItem.setTitle(BaseActivity.isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_tools) : title);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
			case R.id.menu_add_booking:{
				intent = new Intent(this, AddBooking.class);
				startActivity(intent);
			}
			break;
			case R.id.menu_all_bookings:{
				intent = new Intent(this, BookingsPager.class);
				startActivity(intent);
			}
			break;
			case R.id.menu_recipes:{
				intent = new Intent(this, Recipes.class);
				startActivity(intent);
			}
			break;
			case R.id.menu_components:{
				intent = new Intent(this, Components.class);
				startActivity(intent);
			}
			break;
			case R.id.menu_booking_men:{
				intent = new Intent(this, BookingMen.class);
				startActivity(intent);
			}
			break;
			case R.id.menu_tools:{
				intent = new Intent(this, Tools.class);
				startActivity(intent);
			}
			break;
			case R.id.menu_refresh:{
				intent = getIntent();
				finish();
				startActivity(intent);
			}
			break;
		}
		return true;
	}

}


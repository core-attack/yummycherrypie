package yummycherrypie.pl.activities.booking_men;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.adapters.BookingManAdapter;
import yummycherrypie.system.R;


public class BookingMen extends BaseFragmentActivity implements OnClickListener,
		OnItemClickListener, LoaderCallbacks<Cursor>  {

	private final int MENU_UPDATE = 1;
	private final int MENU_DELETE = 2;

	private DBHelper dbHelper;
	private BookingManRepository dbr;

	private ListView listBookingMen;
	private LinearLayout btnAddBookingMan;

	private BookingManAdapter scAdapter;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking_men);

		btnAddBookingMan = (LinearLayout) findViewById(R.id.buttonAddBookingMan);
		btnAddBookingMan.setOnClickListener(this);
		
		dbHelper = new DBHelper(this);
		dbr = new BookingManRepository(dbHelper);

		loadBookingMen();
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.buttonAddBookingMan:
			try{
				intent = new Intent(this, AddBookingMan.class);
				intent.setData(Uri.parse("bookingManId:" + (-1)));
				startActivityForResult(intent, 1);
				break;
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При создании заказчика возникла ошибка", Toast.LENGTH_SHORT)
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
		Toast.makeText(this, "Заказчик сохранен", Toast.LENGTH_SHORT)
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
				Intent intent = new Intent(this, AddBookingMan.class);
				AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
						.getMenuInfo();
				intent.setData(Uri.parse("bookingManId:" + acmi.id));
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
				final AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
				BookingMan bm = dbr.getBookingMan(acmi.id);

				new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Подтвердите удаление")
					.setMessage("Удалить запись '" + bm.getName() + "'?")
					.setPositiveButton("Да", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dbr.delete(DBHelper.TABLE_BOOKING_MEN, acmi.id);
							getSupportLoaderManager().getLoader(0).forceLoad();
							Toast.makeText(getApplicationContext(), "Заказчик удален", Toast.LENGTH_SHORT)
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
			Intent intent = new Intent(this, AddBookingMan.class);
			intent.setData(Uri.parse("bookingTypeId:" + id));
			startActivityForResult(intent, 1);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "При создании заказчика возникла ошибка", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void loadBookingMen() {
		// массив имен атрибутов, из которых будут читаться данные
		String[] from = { DBHelper.COLUMN_BOOKING_MEN_NAME, DBHelper.COLUMN_BOOKING_MEN_PHONE };

		// массив ID View-компонентов, в которые будут вставлять данные
		int[] to = { R.id.tvBookingManName, R.id.tvBookingManPhone };
		
		// создаем адаптер
		scAdapter = new BookingManAdapter(this, null);
		// определяем список и присваиваем ему адаптер
		listBookingMen = (ListView) findViewById(R.id.listViewBookingMen);
		listBookingMen.setAdapter(scAdapter);
		listBookingMen.setOnItemClickListener(this);
		registerForContextMenu(listBookingMen);
		getSupportLoaderManager().initLoader(0, null, this);
	}
	

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
		return new MyCursorLoader(this, dbr);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    scAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	private static class MyCursorLoader extends CursorLoader {

		BookingManRepository db;

		public MyCursorLoader(Context context, BookingManRepository db) {
			super(context);
			this.db = db;
		}

		@Override
		public Cursor loadInBackground() {
			return db.getAllRecords(DBHelper.TABLE_BOOKING_MEN);
		}
	}

	@Override
	protected void onDestroy() {
	  super.onDestroy();
	  if (dbr != null)
	  	dbr.close();
	}

}

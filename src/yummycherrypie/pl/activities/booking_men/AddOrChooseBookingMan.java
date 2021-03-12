package yummycherrypie.pl.activities.booking_men;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.dal.repositories.DataBaseRepository;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.adapters.BookingManAdapter;
import yummycherrypie.system.R;

public class AddOrChooseBookingMan extends BaseFragmentActivity implements OnClickListener, AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	private LinearLayout btnSaveBookingMan;
	private TextView tbBookingManName;
	private TextView tbBookingManPhone;
	private ListView listViewBookingMen;

	private DBHelper dbHelper;
	private BookingManRepository dbr;

	private BookingManAdapter bmAdapter;

	private long bookingManId = -1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_or_choose_booking_man);

		btnSaveBookingMan = (LinearLayout) findViewById(R.id.buttonSaveBookingMan);
		tbBookingManName = (TextView) findViewById(R.id.editTextBookingManName);
		tbBookingManPhone = (TextView) findViewById(R.id.editTextBookingManPhone);

		dbHelper = new DBHelper(this);
		dbr = new BookingManRepository(dbHelper);

		bookingManId = Long.valueOf(getIntent().getData().getSchemeSpecificPart());
	}

	private void loadBookingMen(){
		String[] from = { DBHelper.COLUMN_BOOKING_MEN_NAME, DBHelper.COLUMN_BOOKING_MEN_PHONE };

		// массив ID View-компонентов, в которые будут вставлять данные
		int[] to = { R.id.tvBookingManName, R.id.tvBookingManPhone };

		// создаем адаптер
		bmAdapter = new BookingManAdapter(this, null);
		// определяем список и присваиваем ему адаптер
		listViewBookingMen.setAdapter(bmAdapter);
		listViewBookingMen.setOnItemClickListener(this);
		registerForContextMenu(listViewBookingMen);
		getSupportLoaderManager().initLoader(0, null, this);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.buttonSaveBookingMan:
				try {
					BookingMan bm = new BookingMan(
							tbBookingManName.getText().toString(),
							tbBookingManPhone.getText().toString(),
							""
					);
					bm.setCreateDate(new Date().getTime());
					bookingManId = dbr.insert(DBHelper.TABLE_BOOKING_MEN, bm.getInsertedColumns());
					intent = new Intent();
					intent.setData(Uri.parse("bookingManId:" + bookingManId));
					setResult(RESULT_OK, intent);
					finish();
					Toast.makeText(this, "Заказчик сохранен", Toast.LENGTH_SHORT)
							.show();
				}
				catch(Exception e)
				{
					Toast.makeText(this, "При обновлении заказчика возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
				break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		Intent intent = new Intent();
		intent.setData(Uri.parse("bookingManId:" + id));
		setResult(RESULT_OK, intent);
		finish();
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
		return new MyCursorLoader(this, dbr);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		bmAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	private static class MyCursorLoader extends CursorLoader {

		DataBaseRepository db;

		public MyCursorLoader(Context context, DataBaseRepository db) {
			super(context);
			this.db = db;
		}

		@Override
		public Cursor loadInBackground() {
			Cursor cursor = db.getAllRecords(DBHelper.TABLE_BOOKING_MEN);
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

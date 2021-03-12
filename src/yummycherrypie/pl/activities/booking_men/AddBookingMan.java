package yummycherrypie.pl.activities.booking_men;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.pl.CurrencyExtension;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.activities.BaseDialogActivity;
import yummycherrypie.system.R;

public class AddBookingMan extends BaseDialogActivity implements OnClickListener {

	private final String TITLE = "Заказчик";

	private LinearLayout btnSaveBookingMan;
	private ImageButton btnCallPhone;
	private ImageButton btnOpenInVKApp;
	private EditText etBookingManName;
	private EditText etBookingManPhone;
	private EditText etBookingManVK;
	private TextView tvCountBookings;
	private TextView tvSumBookings;

	private DBHelper dbHelper;
	private BookingManRepository bmRepo;
	private BookingRepository bookingRepo;

	private long bookingManId = -1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_booking_man);

		SpannableString s = new SpannableString(TITLE);
		s.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		setTitle(s);

		btnCallPhone = (ImageButton) findViewById(R.id.btnCallPhone);
		btnOpenInVKApp = (ImageButton) findViewById(R.id.btnOpenInVKApp);
		btnSaveBookingMan = (LinearLayout) findViewById(R.id.buttonSaveBookingMan);
		etBookingManName = (EditText) findViewById(R.id.editTextBookingManName);
		etBookingManPhone = (EditText) findViewById(R.id.editTextBookingManPhone);
		etBookingManVK = (EditText) findViewById(R.id.editTextBookingManVK);
		tvCountBookings = (TextView) findViewById(R.id.tvCountBooking);
		tvSumBookings = (TextView) findViewById(R.id.tvSumBooking);
		btnSaveBookingMan.setOnClickListener(this);
		btnCallPhone.setOnClickListener(this);
		btnOpenInVKApp.setOnClickListener(this);

		dbHelper = new DBHelper(this);
		bmRepo = new BookingManRepository(dbHelper);
		bookingRepo = new BookingRepository(dbHelper);

		bookingManId = Long.valueOf(getIntent().getData().getSchemeSpecificPart());

		if (bookingManId == -1) {
			BookingMan bm = new BookingMan();
			bm.setCreateDate(new Date().getTime());
			bookingManId = bmRepo.insert(DBHelper.TABLE_BOOKING_MEN, bm.getInsertedColumns());
			tvCountBookings.setText("0");
			tvSumBookings.setText("0");
		}
		else {
			BookingMan bm = bmRepo.getBookingMan(bookingManId);
			etBookingManName.setText(bm.getName());
			etBookingManPhone.setText(bm.getRealPhone());
			etBookingManVK.setText(bm.getVkProfile());
			tvCountBookings.setText(String.valueOf(bookingRepo.getBookingManCountBooking(bm.getId())));
			tvSumBookings.setText(CurrencyExtension.getCurrencyDecimalWithoutRouble(bookingRepo.getBookingManSumsBooking(bm.getId())));
		}
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.buttonSaveBookingMan:
				try {
					BookingMan bm = new BookingMan(
							etBookingManName.getText().toString(),
							etBookingManPhone.getText().toString(),
							etBookingManVK.getText().toString()
					);
					bm.setUpdateDate(new Date().getTime());
					bmRepo.update(DBHelper.TABLE_BOOKING_MEN, bm.getUpdatedColumns(), bookingManId);
					intent = new Intent();

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
			case R.id.btnCallPhone:
				try{
					intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + etBookingManPhone.getText().toString()));
					startActivity(intent);
				}
				catch(Exception e)
				{
					Toast.makeText(this, "Не удалось набрать номер", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case R.id.btnOpenInVKApp:
				try{
					intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(etBookingManVK.getText().toString()));
					try {
						startActivity(intent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
				catch(Exception e)
				{
					Toast.makeText(this, "Не удалось перейти на профиль", Toast.LENGTH_SHORT)
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
		long typeId  = Long.valueOf(data.getData().getSchemeSpecificPart());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bmRepo != null)
			bmRepo.close();
	}


}

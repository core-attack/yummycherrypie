package yummycherrypie.pl.activities.booking_types;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import yummycherrypie.base_classes.BookingType;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingTypeRepository;
import yummycherrypie.system.R;
import yummycherrypie.pl.ImageViewExtension;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.activities.BaseDialogActivity;

public class AddBookingType extends BaseDialogActivity implements OnClickListener {

	private final String TITLE = "Тип заказа";
	private final String TITLE_ADD = "Добавить тип заказа";

	private Button btnSaveBookingType;
	private TextView tbBookingTypeName;
	private ImageView ivIcon;
	private CheckBox cbCountable;

	private DBHelper dbHelper;
	private BookingTypeRepository dbr;

	private long bookingTypeId = -1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_booking_type);

		btnSaveBookingType = (Button) findViewById(R.id.buttonSaveBookingType);
		tbBookingTypeName = (TextView) findViewById(R.id.editTextBookingTypeName);
		ivIcon = (ImageView) findViewById(R.id.ivAddBookintTypeImage);
		cbCountable = (CheckBox) findViewById(R.id.checkBoxCountable);

		btnSaveBookingType.setOnClickListener(this);

		dbHelper = new DBHelper(this);
		dbr = new BookingTypeRepository(dbHelper);

		if (getIntent().getData() != null) {
			bookingTypeId = Long.valueOf(getIntent().getData().getSchemeSpecificPart());
		}
		if (bookingTypeId == -1) {
			BookingType bt = new BookingType();
			bt.setCreateDate(new Date().getTime());
			bookingTypeId = dbr.insert(DBHelper.TABLE_BOOKING_TYPES, bt.getInsertedColumns());
			setTitle(TITLE_ADD);
		}
		else
		{
			BookingType bt = dbr.getBookingType(bookingTypeId);
			tbBookingTypeName.setText(bt != null ? bt.getName() : "");
			ImageViewExtension.chooseImage(ivIcon, bookingTypeId, ImageViewExtension.Size.MAX);
			cbCountable.setChecked(bt.isCountable());
			setTitle(TITLE);
		}

		SpannableString s = new SpannableString(getTitle());
		s.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		setTitle(s);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.buttonSaveBookingType:
				try {
					BookingType bt = new BookingType(
							tbBookingTypeName.getText().toString(),
							cbCountable.isChecked()
					);
					bt.setUpdateDate(new Date().getTime());
					dbr.update(DBHelper.TABLE_BOOKING_TYPES, bt.getUpdatedColumns(), bookingTypeId);
					intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
					Toast.makeText(this, "Тип заказа сохранен", Toast.LENGTH_SHORT)
							.show();
				}
				catch(Exception e)
				{
					Toast.makeText(this, "При обновлении типа заказа возникла ошибка", Toast.LENGTH_SHORT)
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
	  if (dbr != null)
	  	dbr.close();
	}


}

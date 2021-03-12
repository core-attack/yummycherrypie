package yummycherrypie.pl.activities.booking_men;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.system.R;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.activities.BaseDialogFragmentActivity;

public class AddBookingManDialog extends BaseDialogFragmentActivity {

	private final String TITLE = "Добавить заказчика";

	private IDialogListener mListener;

	private EditText etBookingManName;
	private EditText etBookingManPhone;
	private EditText etBookingManVK;

	private DBHelper dbHelper;
	private BookingManRepository dbr;

	private long bookingManId = -1;

	public interface IDialogListener {
		void onBookingManDialogPositiveClick(DialogFragment dialog, long bookingManId);
		void onBookingManDialogNegativeClick(DialogFragment dialog);
	}

	public static AddBookingManDialog newInstance() {
		AddBookingManDialog f = new AddBookingManDialog();

		Bundle args = new Bundle();
		f.setArguments(args);

		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.add_or_choose_booking_man, null);
		builder.setView(view);

		SpannableString s = new SpannableString(TITLE);
		s.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(getActivity(), StringExtension.DEFAULT_FONT), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setTitle(s);

		etBookingManName = (EditText) view.findViewById(R.id.editTextBookingManName);
		etBookingManPhone = (EditText) view.findViewById(R.id.editTextBookingManPhone);
		etBookingManVK = (EditText) view.findViewById(R.id.editTextBookingManVK);

		dbHelper = new DBHelper(getActivity());
		dbr = new BookingManRepository(dbHelper);

		builder
				.setPositiveButton("ок", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							BookingMan bm = new BookingMan(
									etBookingManName.getText().toString(),
									etBookingManPhone.getText().toString(),
									etBookingManVK.getText().toString()
							);

							BookingMan existingBookingMan = dbr.isExist(bm.getRealPhone());

							if (existingBookingMan == null) {

								bm.setCreateDate(new Date().getTime());
								bookingManId = dbr.insert(DBHelper.TABLE_BOOKING_MEN, bm.getInsertedColumns());
								mListener.onBookingManDialogPositiveClick(AddBookingManDialog.this, bookingManId);
							} else {
								Toast.makeText(getActivity(), "Указанный телефонный номер уже используется!", Toast.LENGTH_SHORT)
										.show();
								mListener.onBookingManDialogPositiveClick(AddBookingManDialog.this, existingBookingMan.getId());
							}
						}
						catch(Exception e)
						{
							Toast.makeText(getActivity(), "При обновлении заказчика возникла ошибка", Toast.LENGTH_SHORT)
									.show();
						}
					}
				})
				.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onBookingManDialogNegativeClick(AddBookingManDialog.this);
					}
				});

		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (IDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IDialogListener");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dbr != null)
			dbr.close();
	}
}

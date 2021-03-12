package yummycherrypie.pl.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.Recipe;
import yummycherrypie.business_logic.Calculation;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.default_records.BookingTypesDefaultRecords;
import yummycherrypie.dal.repositories.BookingTypeRepository;
import yummycherrypie.dal.repositories.RecipeRepository;
import yummycherrypie.system.R;
import yummycherrypie.pl.CurrencyExtension;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.WeightExtension;

public class BookingAdapterWithDateAndCheckbox extends BaseAdapter {

	private Context context;
	private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();
	private DBHelper dbHelper;
	private BookingTypeRepository dbr;
	private ArrayList<Booking> bookings;


	public BookingAdapterWithDateAndCheckbox(Context c, ArrayList<Booking> bookings) {

		dbHelper = new DBHelper(c);
		dbr = new BookingTypeRepository(dbHelper);

		context = c;
		this.bookings = bookings;
		if(bookings != null) {
			for (int i = 0; i < bookings.size(); i++) {
				myChecked.put(i, false);
			}
		}
	}

	@Override
	public int getCount() {
		if (bookings != null)
			return bookings.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return bookings.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		protected TextView date;
		protected TextView name;
		protected TextView price;
		protected TextView weight;
		protected CheckBox check;
		protected ImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.item_booking_with_checkbox, null);

			holder.date = (TextView) convertView.findViewById(R.id.tvBookingDate);
			holder.price = (TextView) convertView.findViewById(R.id.tvBookingPrice);
			holder.weight = (TextView) convertView.findViewById(R.id.tvBookingWeight);
			holder.name = (TextView) convertView.findViewById(R.id.tvBookingRecipeName);
			holder.image = (ImageView) convertView.findViewById(R.id.ivImage);
			holder.check = (CheckBox) convertView.findViewById(R.id.checkBox);

			holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
					int getPosition = (Integer) compoundButton.getTag();
					myChecked.put(getPosition, compoundButton.isChecked());
				}
			});

			convertView.setTag(holder);
			convertView.setTag(R.id.tvBookingDate, holder.date);
			convertView.setTag(R.id.tvBookingPrice, holder.price);
			convertView.setTag(R.id.tvBookingWeight, holder.weight);
			convertView.setTag(R.id.tvBookingRecipeName, holder.name);
			convertView.setTag(R.id.ivImage, holder.image);
			convertView.setTag(R.id.checkBox, holder.check);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.check.setTag(position);

		DBHelper dbHelper = new DBHelper(context);
		RecipeRepository dbr = new RecipeRepository(dbHelper);

		try {
			Booking b = bookings.get(position);

			holder.date.setText(DateExtension.getDateString(b.getDateLong()));

			Recipe r = dbr.getRecipe(b.getRecipeId());
			boolean isCountable = false;

			if (r != null) {
				holder.name.setText(StringExtension.makeShortString(r.getName(), 25));
				isCountable = r.isCountable();
			} else {
				holder.name.setText("Рецепт не выбран");
			}
			holder.price.setText(CurrencyExtension.getCurrencyDecimalWithoutRouble(Calculation.getDiscountPrice(Calculation.getResultPrice(b.getCakePrice(), b.getCountProduct(), isCountable), b.getDiscount())));
			holder.weight.setText(WeightExtension.getWeightWithGramm(b.getWeight()));

			if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_CUPCAKE_ID) {
				holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cupcake_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_COOKIE_ID) {
				holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cookies_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_CAKE_ID) {
				holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cake_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_MARRIAGE_ID) {
				holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cake_marriage_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_CAKEPOPS_ID) {
				holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cakepops_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_PASTRY_ID) {
				holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_pastry_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_SPICE_CAKE_ID) {
				holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_spice_cake_36));
			} else
				holder.image.setVisibility(View.INVISIBLE);

			holder.check.setChecked(myChecked.get(position));
		}
		finally {
			if (dbr != null){
				dbr.close();
			}
		}
		return convertView;
	}

	public void toggleChecked(int position){
		if(myChecked.get(position)){
			myChecked.put(position, false);
		}else{
			myChecked.put(position, true);
		}

		notifyDataSetChanged();
	}

	public List<Integer> getCheckedItemPositions(){
		List<Integer> checkedItemPositions = new ArrayList<Integer>();

		for(int i = 0; i < myChecked.size(); i++){
			if (myChecked.get(i)){
				(checkedItemPositions).add(i);
			}
		}
		return checkedItemPositions;
	}

}
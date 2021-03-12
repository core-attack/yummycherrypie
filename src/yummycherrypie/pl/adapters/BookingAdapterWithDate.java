package yummycherrypie.pl.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.Recipe;
import yummycherrypie.business_logic.Calculation;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.default_records.BookingTypesDefaultRecords;
import yummycherrypie.dal.repositories.RecipeRepository;
import yummycherrypie.pl.CurrencyExtension;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.WeightExtension;
import yummycherrypie.system.R;

public class BookingAdapterWithDate extends AlternateItemsCursorAdapter {

    private final LayoutInflater mInflater;
	private Context context;
	private Cursor cursor;

    public BookingAdapterWithDate(Context context, Cursor cursor) {
        super(context, cursor);
        mInflater = LayoutInflater.from(context);
		this.context = context;
    }

	@Override
	public int getCount(){
		if (cursor != null)
			return cursor.getCount();
		return super.getCount();
	}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
         return mInflater.inflate(R.layout.item_booking, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);

		DBHelper dbHelper = new DBHelper(context);
		RecipeRepository dbr = new RecipeRepository(dbHelper);

		try {
			Booking b = new Booking(cursor);

			((TextView) view.findViewById(R.id.tvBookingDate)).setText(DateExtension.getDateString(b.getDateLong()));

			Recipe r = dbr.getRecipe(b.getRecipeId());
			boolean isCountable = false;

			if (r != null) {
				((TextView) view.findViewById(R.id.tvBookingRecipeName))
						.setText(StringExtension.makeShortString(r.getName(), 25));
				isCountable = r.isCountable();
			} else {
				((TextView) view.findViewById(R.id.tvBookingRecipeName))
						.setText("Рецепт не выбран");
			}

			((TextView) view.findViewById(R.id.tvBookingPrice)).setText(CurrencyExtension.getCurrencyDecimalWithoutRouble(Calculation.getDiscountPrice(Calculation.getResultPrice(b.getCakePrice(), b.getCountProduct(), isCountable), b.getDiscount())));
			((TextView) view.findViewById(R.id.tvBookingWeight)).setText(WeightExtension.getWeightWithGramm(b.getWeight()));

			ImageView icon = ((ImageView) view.findViewById(R.id.ivImage));

			if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_CUPCAKE_ID) {
				icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cupcake_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_COOKIE_ID) {
				icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cookies_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_CAKE_ID) {
				icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cake_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_MARRIAGE_ID) {
				icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cake_marriage_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_CAKEPOPS_ID) {
				icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_cakepops_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_PASTRY_ID) {
				icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_pastry_36));
			} else if (b.getBookingTypeId() == BookingTypesDefaultRecords.BOOKING_TYPE_SPICE_CAKE_ID) {
				icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_spice_cake_36));
			} else
				icon.setVisibility(View.INVISIBLE);
		}
		finally {
			if (dbr != null){
				dbr.close();
			}
		}
	}
}
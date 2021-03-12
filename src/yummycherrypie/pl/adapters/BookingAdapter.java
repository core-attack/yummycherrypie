package yummycherrypie.pl.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.base_classes.Recipe;
import yummycherrypie.business_logic.Calculation;
import yummycherrypie.pl.CurrencyExtension;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.dal.repositories.RecipeRepository;
import yummycherrypie.system.R;
import yummycherrypie.pl.WeightExtension;

/**
 * Created by CoreAttack on 27.07.2015.
 */
public class BookingAdapter extends SimpleCursorAdapter {

    private LayoutInflater lInflater;
    private Cursor cr;
    private int layout;
    private Context mContext;
    private Context appContext;

    public BookingAdapter(Context context, int layout, Cursor c, String[] from, int[] to){
        super(context, layout, c, from, to);
        this.layout = layout;
        this.mContext = context;
        this.lInflater = LayoutInflater.from(context);
        this.cr = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return lInflater.inflate(layout, null);
    }

    public void bindView(@NonNull View view, Context context, @NonNull Cursor cursor){

        DBHelper dbHelper = new DBHelper(context);
        RecipeRepository dbr = new RecipeRepository(dbHelper);
        BookingManRepository bmRepo = new BookingManRepository(dbHelper);

        try {
            super.bindView(view, context, cursor);

            Booking b = new Booking(cursor);
            if (b != null) {
                Recipe r = dbr.getRecipe(b.getRecipeId());
                boolean isCountable = false;

                if (r != null) {
                    ((TextView) view.findViewById(R.id.tvBookingRecipeName))
                            .setText(r.getName());
                    isCountable = r.isCountable();
                } else {
                    ((TextView) view.findViewById(R.id.tvBookingRecipeName))
                            .setText("Рецепт не выбран");
                }
                ((TextView) view.findViewById(R.id.tvBookingPrice)).setText(CurrencyExtension.getCurrency(Calculation.getDiscountPrice(Calculation.getResultPrice(b.getCakePrice(), b.getCountProduct(), isCountable), b.getDiscount())));

                ((TextView) view.findViewById(R.id.tvBookingWeight)).setText(WeightExtension.getWeightWithGramm(b.getWeight()));

                BookingMan bm = bmRepo.getBookingMan(b.getBookingManId());
                if (bm != null){
                        ((TextView) view.findViewById(R.id.tvBookingDate)).setText(bm.getNameWithPhone());
                } else {
                    ((TextView) view.findViewById(R.id.tvBookingDate)).setText("Заказчик не выбран");
                }
            }
        }
        finally {
            if (dbr != null){
                dbr.close();
            }
        }
    }
}

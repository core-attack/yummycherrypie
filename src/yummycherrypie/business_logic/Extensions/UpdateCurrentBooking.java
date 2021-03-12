package yummycherrypie.business_logic.Extensions;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.business_logic.Calculation;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.dal.repositories.RecipeLineRepository;
import yummycherrypie.dal.repositories.RecipeRepository;

/**
 * Created by CoreAttack on 15.12.2015.
 */
public class UpdateCurrentBooking {

    /**
     * Пересчитывает себестоимости заказов
     * */
    public static void doIt(Context context, BookingRepository br, RecipeLineRepository rlRepo,
                                               RecipeRepository recipeRepo, ComponentRepository compRepo){
        if (br != null){
            ArrayList<Booking> bookings = br.getAllCurrentBookingsArrayList();
            for(Booking booking : bookings){
                ArrayList<RecipeLine> recipeLines = rlRepo.getAllRecipeLinesArrayListByBooking(booking.getId());
                Calculation calc = new Calculation(recipeRepo, rlRepo, compRepo,
                        booking.getRecipeId(), booking.getId(), context, recipeLines);
                calc.setSummary(booking.getWeight());
                booking.setRecipePrice(calc.getSummary());
                booking.setUpdateDate(Calendar.getInstance().getTimeInMillis());
                br.update(DBHelper.TABLE_BOOKINGS, booking.getUpdatedColumns(), booking.getId());
            }
        }
        Toast.makeText(context, "Себестоимости текущих заказов пересчитаны", Toast.LENGTH_SHORT).show();
    }
}

package yummycherrypie.pl.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import yummycherrypie.business_logic.Extensions.OtherExtensions;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.activities.booking_men.BookingMen;
import yummycherrypie.pl.activities.bookings.AddBooking;
import yummycherrypie.pl.activities.bookings.BookingsPager;
import yummycherrypie.pl.activities.components.Components;
import yummycherrypie.pl.activities.recipes.Recipes;
import yummycherrypie.pl.activities.tools.Tools;
import yummycherrypie.system.R;

/**
 * Created by Nikolay_Piskarev on 12/7/2015.
 */
public class BaseFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        catch(NullPointerException e)
        {
            Toast.makeText(this, OtherExtensions.ERR_ADD_BACK_BUTTON_TO_ACTION_BAR, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //метод вызыватся только один раз при создании меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general_menu, menu);
        Typeface face = Typeface.createFromAsset(getAssets(), StringExtension.DEFAULT_FONT);

        String illegalArgumentExceptionReason = "LG";

        SpannableString title = new SpannableString(getString(R.string.menu_refresh));
        title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        MenuItem menuItem = menu.findItem(R.id.menu_refresh);
        menuItem.setTitle(isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_refresh) : title);

        title = new SpannableString(getString(R.string.menu_add_booking));
        title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuItem = menu.findItem(R.id.menu_add_booking);
        menuItem.setTitle(isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_add_booking) : title);

        title = new SpannableString(getString(R.string.menu_all_bookings));
        title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuItem = menu.findItem(R.id.menu_all_bookings);
        menuItem.setTitle(isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_all_bookings) : title);

        title = new SpannableString(getString(R.string.menu_recipes));
        title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuItem = menu.findItem(R.id.menu_recipes);
        menuItem.setTitle(isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_recipes) : title);

        title = new SpannableString(getString(R.string.menu_components));
        title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuItem = menu.findItem(R.id.menu_components);
        menuItem.setTitle(isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_components) : title);

        title = new SpannableString(getString(R.string.menu_booking_men));
        title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuItem = menu.findItem(R.id.menu_booking_men);
        menuItem.setTitle(isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_booking_men) : title);

        title = new SpannableString(getString(R.string.menu_tools));
        title.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuItem = menu.findItem(R.id.menu_tools);
        menuItem.setTitle(isManufacturer(illegalArgumentExceptionReason) ? getString(R.string.menu_tools) : title);

        return true;
    }

    // обновление меню
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
            }
            break;
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
            default: return false;
        }
        return true;
    }

}

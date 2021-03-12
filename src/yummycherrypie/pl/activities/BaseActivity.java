package yummycherrypie.pl.activities;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableString;

import java.text.NumberFormat;

import yummycherrypie.business_logic.Extensions.OtherExtensions;
import yummycherrypie.pl.StringExtension;

/**
 * Created by Nikolay_Piskarev on 12/24/2015.
 */
public class BaseActivity extends FragmentActivity {

    protected NumberFormat nf = NumberFormat.getInstance(OtherExtensions.DEFAULT_LOCALE);

    public static boolean isManufacturer(String company) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        return (manufacturer.contains(company) || model.contains(company));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            if (getTitle() != null) {
                SpannableString s = new SpannableString(getTitle());
                s.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, s.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                actionBar.setTitle(s);
            }
        }
    }

}

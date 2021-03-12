package yummycherrypie.pl.activities;

import android.support.v4.app.DialogFragment;

import java.text.NumberFormat;

import yummycherrypie.business_logic.Extensions.OtherExtensions;

/**
 * Created by Nikolay_Piskarev on 12/24/2015.
 */
public class BaseDialogFragmentActivity extends DialogFragment {

    protected NumberFormat nf = NumberFormat.getInstance(OtherExtensions.DEFAULT_LOCALE);

}

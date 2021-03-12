package yummycherrypie.pl.custom_controls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import yummycherrypie.pl.StringExtension;

/**
 * Created by Nikolay_Piskarev on 12/8/2015.
 */
public class CustomFontRoubleTextView extends TextView {

    public CustomFontRoubleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        } else {
            setTypeface(Typeface.createFromAsset(context.getAssets(), StringExtension.ROUBLE_FONT));
        }
    }
}

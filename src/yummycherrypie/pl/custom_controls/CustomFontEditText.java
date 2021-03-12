package yummycherrypie.pl.custom_controls;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import yummycherrypie.pl.StringExtension;

/**
 * Created by Nikolay_Piskarev on 12/8/2015.
 */
public class CustomFontEditText extends EditText {

    public CustomFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        } else {
            setTypeface(Typeface.createFromAsset(context.getAssets(), StringExtension.DEFAULT_FONT));
        }
    }
}

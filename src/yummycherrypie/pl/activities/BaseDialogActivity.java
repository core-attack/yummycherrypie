package yummycherrypie.pl.activities;

import android.os.Bundle;
import android.view.View;

import yummycherrypie.system.R;

/**
 * Created by Nikolay_Piskarev on 12/24/2015.
 */
public class BaseDialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //// TODO: 12/25/2015 красит разделитель только когда открываешь соотв. активити. До этого времени в диалоге добавления заказчиков, разделитель цвета по умолчанию. 
        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(getResources().getColor(R.color.action_bar_background));
        }
    }
}

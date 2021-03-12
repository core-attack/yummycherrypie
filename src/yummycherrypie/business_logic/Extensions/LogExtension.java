package yummycherrypie.business_logic.Extensions;

import android.util.Log;

/**
 * Created by Nikolay_Piskarev on 12/2/2015.
 */
public class LogExtension {

    /**
     * Именование лога
     * */
    public static final String LOG_TAG = "yummycherrypie";

    public static void Error(String s){
        Log.e(LOG_TAG, s);
    }

    public static void Debug(String s){
        Log.d(LOG_TAG, s);
    }

    public static void Info(String s){
        Log.i(LOG_TAG, s);
    }

    public static void Warning(String s){
        Log.w(LOG_TAG, s);
    }
}

package yummycherrypie.pl;

import yummycherrypie.business_logic.Extensions.OtherExtensions;

/**
 * Created by Nikolay_Piskarev on 12/8/2015.
 */
public class CurrencyExtension {

    /**
     * знак рубля
     * */
    public static final String ROUBLE = "r";//либо w

    public static String getCurrency(double currency){
        return String.format(OtherExtensions.DEFAULT_LOCALE, "%.2f %s", currency, ROUBLE);
    }

    public static String getCurrencyWithoutRouble(double currency){
        return String.format(OtherExtensions.DEFAULT_LOCALE, "%.2f", currency);
    }

    public static String getCurrencyDecimalWithoutRouble(double currency){
        return String.format(OtherExtensions.DEFAULT_LOCALE, "%d", Double.valueOf(currency).intValue());
    }

    public static String getCurrencyDecimal(double currency){
        return String.format(OtherExtensions.DEFAULT_LOCALE, "%,d %s", Double.valueOf(currency).intValue(), ROUBLE);
    }
}

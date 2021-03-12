package yummycherrypie.pl;

import yummycherrypie.business_logic.Extensions.OtherExtensions;

/**
 * Created by Nikolay_Piskarev on 12/10/2015.
 */
public class WeightExtension {

    /**
     * знак рубля
     * */
    public static final String GRAMM = "г.";

    public static String getWeight(double weight){
        return String.format(OtherExtensions.DEFAULT_LOCALE, "%.0f", weight);
    }

    public static String getWeightWithGramm(double weight){
        return String.format(OtherExtensions.DEFAULT_LOCALE, "%.0f %s", weight, GRAMM);
    }
}

package yummycherrypie.pl;

import yummycherrypie.business_logic.Extensions.OtherExtensions;

/**
 * Created by Nikolay_Piskarev on 12/16/2015.
 */
public class CountExtension {

    /**
     * знак количества
     * */
    public static final String COUNT = "шт.";

    public static String getWeight(int count){
        return String.format(OtherExtensions.DEFAULT_LOCALE, "%,d", count);
    }

    public static String getWeightWithCount(int count){
        return String.format(OtherExtensions.DEFAULT_LOCALE, "%,d %s", count, COUNT);
    }
}

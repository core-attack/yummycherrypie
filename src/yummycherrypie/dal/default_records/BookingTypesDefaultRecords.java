package yummycherrypie.dal.default_records;

import java.util.Date;

import yummycherrypie.base_classes.BookingType;
import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class BookingTypesDefaultRecords extends DataBaseDefaultRecords{

    public static final long BOOKING_TYPE_CAKE_ID = 1;
    public static final long BOOKING_TYPE_CUPCAKE_ID = 2;
    public static final long BOOKING_TYPE_MARRIAGE_ID = 3;
    public static final long BOOKING_TYPE_COOKIE_ID = 4;
    public static final long BOOKING_TYPE_CAKEPOPS_ID = 5;
    public static final long BOOKING_TYPE_PASTRY_ID = 6;
    public static final long BOOKING_TYPE_SPICE_CAKE_ID = 7;

    public BookingTypesDefaultRecords(DBHelper dbh){
        super(dbh);
    }

    /**
     * создание типов заказа по умолчанию
     */
    public void createDefaultRecords(boolean isDebug){
        dbr.deleteAll(DBHelper.TABLE_BOOKING_TYPES);

        String[] arrNames = { "Торт", "Капкейки", "Ярус торта", "Печенье",
                "Кейкпопсы", "Пирожные", "Пряники" };
        long[] ids = { BOOKING_TYPE_CAKE_ID, BOOKING_TYPE_CUPCAKE_ID, BOOKING_TYPE_MARRIAGE_ID, BOOKING_TYPE_COOKIE_ID,
                BOOKING_TYPE_CAKEPOPS_ID, BOOKING_TYPE_PASTRY_ID, BOOKING_TYPE_SPICE_CAKE_ID};
        boolean[] isCountable = { false, true, false, true, true, true, true};

        for (int i = 0; i < arrNames.length; i++) {
            BookingType bt = new BookingType(arrNames[i], isCountable[i]);
            bt.setId(ids[i]);
            bt.setCreateDate(new Date().getTime());
            dbr.insert(DBHelper.TABLE_BOOKING_TYPES, bt.getInsertedColumnsWithId());
            LogExtension.Debug("Record created: " + bt.toString());
        }
    }
}

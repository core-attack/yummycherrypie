package yummycherrypie.dal.default_records;

import java.util.Date;
import java.util.Random;

import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class BookingMenDefaultRecords extends DataBaseDefaultRecords{

    public BookingMenDefaultRecords(DBHelper dbh){
        super(dbh);
    }

    /**
     * создание заказчиков по умолчанию
     */
    public void createDefaultRecords(boolean isDebug){
        dbr.deleteAll(DBHelper.TABLE_BOOKING_MEN);

        if(isDebug) {
            Random r = new Random();
            String[] profiles = { "https://vk.com/evgeny_shevnin", "https://vk.com/faridada", "https://vk.com/milad", "https://vk.com/id262042732", "https://vk.com/id293061255" };
            for (int i = 0; i < 25; i++) {
                BookingMan bm = new BookingMan("Заказчик №" + (i + 1), "891294863" + i, profiles[r.nextInt(profiles.length)]);
                bm.setCreateDate(new Date().getTime());
                dbr.insert(DBHelper.TABLE_BOOKING_MEN, bm.getInsertedColumns());
                LogExtension.Debug("Record created: " + bm.toString());
            }
        }
    }
}

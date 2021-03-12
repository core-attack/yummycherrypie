package yummycherrypie.dal.default_records;

import java.util.Calendar;
import java.util.Date;

import yummycherrypie.base_classes.Event;
import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class EventsDefaultRecords extends DataBaseDefaultRecords{

    public EventsDefaultRecords(DBHelper dbh){
        super(dbh);
    }

    public void createDefaultRecords(boolean isDebug){
        dbr.deleteAll(DBHelper.TABLE_EVENTS);

        if(isDebug) {
            String[] arrNames = {"Свадьба", "День рождения", "Свадьба", "День рождения"};
            String[] arrPlaces = {"Кафе \"Три толстяка\"", "Дом молодежи", "", "Дом молодежи"};

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            for (int i = 0; i < arrNames.length; i++) {
                Event e = new Event(arrNames[i], arrPlaces[i], cal.getTimeInMillis(), i);
                e.setCreateDate(new Date().getTime());
                dbr.insert(DBHelper.TABLE_EVENTS, e.getInsertedColumns());
                LogExtension.Debug("Record created: " + e.toString());
            }
        }
    }
}

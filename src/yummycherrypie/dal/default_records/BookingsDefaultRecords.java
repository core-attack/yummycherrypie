package yummycherrypie.dal.default_records;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class BookingsDefaultRecords extends DataBaseDefaultRecords{

    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    final Random random = new Random();
    Context context;

    public BookingsDefaultRecords(DBHelper dbh){
        super(dbh);
    }

    /**
     * создание заказов по умолчанию
     */
    public void createDefaultRecords(boolean isDebug) {
        dbr.deleteAll(DBHelper.TABLE_BOOKINGS);
        if (isDebug) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            int countDays = 28;
            int firstMonth = currentMonth - 3 < 0 ? 11 - (3 - currentMonth) : currentMonth - 3;
            int lastMonth = currentMonth + 3 > 11 ? (currentMonth + 3) - 11 : currentMonth + 3;
            int firstYear = currentMonth - 3 < 0 ? currentYear - 1 : currentYear;
            int lastYear = currentMonth + 3 > 11 ? currentYear + 1 : currentYear;

            firstMonth = firstMonth + 1;
            lastMonth = lastMonth + 1;

            int year = firstYear;

            int currentId = 1;
            if (firstYear == lastYear) {
                for (int month = firstMonth; month <= lastMonth; month++) {
                    for (int day = 1; day < countDays; day++) {
                        CreateRecords(day, month, year, currentId);
                        currentId++;
                    }
                }
            } else {
                for (int month = firstMonth; month <= 11; month++) {
                    for (int day = 1; day < countDays; day++) {
                        CreateRecords(day, month, year, currentId);
                        currentId++;
                    }
                }

                for (int month = 0; month <= lastMonth; month++) {
                    for (int day = 1; day < countDays; day++) {
                        CreateRecords(day, month, year, currentId);
                        currentId++;
                    }
                }
            }
        }
    }

    private void CreateRecords(int day, int month, int year, int currentId) {
        CreateBooking(day, month, year);
        CreateRecipeLines(currentId);
    }

    private void CreateRecipeLines(int id){
        ArrayList<RecipeLine> list = new ArrayList<RecipeLine>();
        list.add(new RecipeLine(0, 7, 1, 1, id, RecipeLine.State.DEFAULT));
        list.add(new RecipeLine(543, 0, 4, 1, id, RecipeLine.State.DEFAULT));
        list.add(new RecipeLine(22, 0, 21, 1, id, RecipeLine.State.DEFAULT));
        list.add(new RecipeLine(32, 0, 11, 1, id, RecipeLine.State.DEFAULT));
        list.add(new RecipeLine(2, 0, 41, 1, id, RecipeLine.State.DEFAULT));
        list.add(new RecipeLine(324, 0, 11, 1, id, RecipeLine.State.DEFAULT));
        list.add(new RecipeLine(23, 0, 12, 1, id, RecipeLine.State.DEFAULT));
        list.add(new RecipeLine(2, 0, 13, 1, id, RecipeLine.State.DEFAULT));
        list.add(new RecipeLine(32, 0, 17, 1, id, RecipeLine.State.ADD_BY_USER));
        list.add(new RecipeLine(12, 0, 19, 1, id, RecipeLine.State.ADD_BY_USER));
        list.add(new RecipeLine(421, 0, 23, 1, id, RecipeLine.State.ADD_BY_USER));
        list.add(new RecipeLine(3, 0, 24, 1, id, RecipeLine.State.ADD_BY_USER));
        list.add(new RecipeLine(123, 0, 26, 1, id, RecipeLine.State.ADD_BY_USER));
        list.add(new RecipeLine(2, 0, 29, 1, id, RecipeLine.State.ADD_BY_USER));

        for (RecipeLine rl : list) {
            dbr.insert(DBHelper.TABLE_RECIPE_LINES, rl.getInsertedColumns());
            LogExtension.Debug("Record created: " + rl.toString());
        }
        list.clear();
    }

    private void CreateBooking(int day, int month, int year) {
        try {
            Date date = sdf.parse(String.format("%02d/%02d/%04d", day, month + 1, year));
            Booking b = new Booking(date.getTime(),
                    random.nextInt(4) + 1,
                    random.nextInt(5) + 1,
                    "comment1",
                    Double.parseDouble(new DecimalFormat("##.##").format(random.nextInt(10000) + random.nextDouble())),
                    Double.parseDouble(new DecimalFormat("##.##").format(random.nextInt(10000) + random.nextDouble())),
                    random.nextInt(10000),
                    Double.parseDouble(new DecimalFormat("##.##").format(random.nextDouble())),
                    random.nextInt(3),
                    random.nextInt(7) + 1,
                    random.nextInt(10) + 1,
                    random.nextInt(9) + 1);

            b.setCreateDate(new Date().getTime());
            dbr.insert(DBHelper.TABLE_BOOKINGS, b.getInsertedColumns());
        } catch(Exception e){
            LogExtension.Error(e.getMessage());

        }
    }
}

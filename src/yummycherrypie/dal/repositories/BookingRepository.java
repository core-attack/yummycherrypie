package yummycherrypie.dal.repositories;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.base_classes.Statistic;
import yummycherrypie.business_logic.Period;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class BookingRepository extends DataBaseRepository {

    private RecipeLineRepository rlr;

    public BookingRepository(DBHelper dbHelper){

        super(dbHelper);
        rlr = new RecipeLineRepository(dbHelper);
    }

    @Override
    public void delete(String tableName, long id) {
        ArrayList<RecipeLine> list = rlr.getAllRecipeLinesForBooking(id);
        for (RecipeLine r : list) {
            delete(dbHelper.TABLE_RECIPE_LINES, r.getId());
        }
        super.delete(tableName, id);
    }

    /**
     * возвращает заказ
     */
    public Booking getBooking(long id) {
        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null, DBHelper.COLUMN_ID + " = ?",
                new String[]{Long.toString(id)}, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    return new Booking(c);
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return null;
    }

    /**
     * Возвращает текущие все заказы (позднее или равны текущей даты)
     */
    public ArrayList getAllCurrentBookingsArrayList(){
        Calendar cal = Calendar.getInstance();
        long startDate = Period.getBorderOfDay(cal.getTime()).GetStartDateLong();
        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null, DBHelper.COLUMN_BOOKINGS_DATE + " >= ?",
                new String[] { Long.toString(startDate) }, null, null, DBHelper.COLUMN_BOOKINGS_DATE);
        ArrayList list = new ArrayList<Booking>();
        try{

            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Booking().getColumnIndexes(c);
                    do {
                        list.add(new Booking(c, map));
                    } while (c.moveToNext());
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return list;
    }

    /**
     * Возвращает все заказы
     */
    public ArrayList getAllBookingsArrayList(long date){
        Period period = Period.getBorderOfDay(date);
        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null,
                DBHelper.COLUMN_BOOKINGS_DATE + " >= ? and "
                        + DBHelper.COLUMN_BOOKINGS_DATE + " <= ?",
                new String[]{Long.toString(period.GetStartDateLong()), Long.toString(period.GetEndDateLong())}, null,
                null, null);
        ArrayList list = new ArrayList<Booking>();
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Booking().getColumnIndexes(c);
                    do {
                        list.add(new Booking(c, map));
                    } while (c.moveToNext());
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return list;
    }


    /**
     * Возвращает все заказы
     */
    public ArrayList getAllBookingsArrayList(){
        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null, null, null, null, null, DBHelper.COLUMN_BOOKINGS_DATE);
        ArrayList list = new ArrayList<Booking>();
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Booking().getColumnIndexes(c);
                    do {
                        list.add(new Booking(c, map));
                    } while (c.moveToNext());
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return list;
    }

    /**
     * Возвращает все заказы для события
     */
    public Cursor getAllEventsBookings(long eventId){
        return db.query(DBHelper.TABLE_BOOKINGS, null, DBHelper.COLUMN_BOOKINGS_EVENT_ID + " = ?",
                new String[] { Long.toString(eventId) }, null, null, null);
    }

    /**
     * Удаляет все заказы для события
     */
    public void deleteAllEventsBookings(long eventId){
        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null, DBHelper.COLUMN_BOOKINGS_EVENT_ID + " = ?",
                new String[]{Long.toString(eventId)}, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Booking().getColumnIndexes(c);
                    do {
                        delete(DBHelper.TABLE_BOOKINGS, new Booking(c, map).getId());
                    } while (c.moveToNext());
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
    }

    /**
     * Возвращает все текущие заказы (позднее или равны сегодняшней даты)
     */
    public Cursor getAllCurrentBookings(){
        Calendar cal = Calendar.getInstance();
        long startDate = Period.getBorderOfDay(cal.getTime()).GetStartDateLong();
        return db.query(DBHelper.TABLE_BOOKINGS, null, DBHelper.COLUMN_BOOKINGS_DATE + " >= ?",
                new String[] { Long.toString(startDate) }, null, null, DBHelper.COLUMN_BOOKINGS_DATE);
    }

    /**
     * Возвращает все прошедшие заказы (меньше текущей даты)
     */
    public Cursor getAllPastBookings(){
        Calendar cal = Calendar.getInstance();
        long startDate = Period.getBorderOfDay(cal.getTime()).GetStartDateLong();
        return db.query(DBHelper.TABLE_BOOKINGS, null, DBHelper.COLUMN_BOOKINGS_DATE + " < ?",
                new String[]{Long.toString(startDate)}, null, null, DBHelper.COLUMN_BOOKINGS_DATE);
    }

    /**
     * Возвращает все текущие заказы этого месяца(позднее или равны сегодняшней даты в пределах текущего месяца)
     */
    public Cursor getAllCurrentMonthBookings(){
        Period p = Period.getBorderOfMonth(Calendar.getInstance().getTime());

        Calendar cal = Calendar.getInstance();
        long startDate = Period.getBorderOfDay(cal.getTime()).GetStartDateLong();
        return db.query(DBHelper.TABLE_BOOKINGS,
                null,
                DBHelper.COLUMN_BOOKINGS_DATE + " >= ? and " +
                DBHelper.COLUMN_BOOKINGS_DATE + " < ?",
                new String[] {
                        Long.toString(startDate),
                        Long.toString(p.GetEndDateLong())

                }, null, null, DBHelper.COLUMN_BOOKINGS_DATE);
    }

    /**
     * Возвращает все будущие заказы (будущий и последующие месяцы)
     */
    public Cursor getAllFutureBookings(){
        Period p = Period.getBorderOfMonth(Calendar.getInstance().getTime());
        return db.query(DBHelper.TABLE_BOOKINGS, null, DBHelper.COLUMN_BOOKINGS_DATE + " > ?",
                new String[] { Long.toString(p.GetEndDateLong()) }, null, null, DBHelper.COLUMN_BOOKINGS_DATE);
    }

    /**
     * Возвращает вообще все заказы
     */
    public Cursor getAllBookings(){
        return db.query(DBHelper.TABLE_BOOKINGS, null, null, null, null, null, DBHelper.COLUMN_BOOKINGS_DATE);
    }

    /**
     * Возвращает все заказы в указанный период дат
     * @param period - период
     */
    public Cursor getAllBookings(Period period){
        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null,
                DBHelper.COLUMN_BOOKINGS_DATE + " >= ? and "
                        + DBHelper.COLUMN_BOOKINGS_DATE + " <= ?",
                new String[]{Long.toString(period.GetStartDateLong()), Long.toString(period.GetEndDateLong())}, null,
                null, null);
        return c;
    }

    /**
     * Возвращает количество заказов по дате
     */
    public short getAllBookingsCount(long date){
        //todo Переписать!
        Period period = Period.getBorderOfDay(date);

        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null,
                DBHelper.COLUMN_BOOKINGS_DATE + " >= ? and "
                        + DBHelper.COLUMN_BOOKINGS_DATE + " <= ?",
                new String[]{Long.toString(period.GetStartDateLong()), Long.toString(period.GetEndDateLong())}, null,
                null, null);
        short i = 0;
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        if (c != null)
            c.close();
        return i;
    }

    /**
     * Возвращает количество заказов по дате
     */
    public short getAllBookingsCount(Date date){
        Period period = Period.getBorderOfDay(date);
        //todo Переписать!
        Cursor c = db.query(DBHelper.TABLE_BOOKINGS, null,
                DBHelper.COLUMN_BOOKINGS_DATE + " >= ? and "
                        + DBHelper.COLUMN_BOOKINGS_DATE + " <= ?",
                new String[]{Long.toString(period.GetStartDateLong()), Long.toString(period.GetEndDateLong())}, null,
                null, null);
        short i = 0;
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    i++;
                } while (c.moveToNext());
            }
        }
        if (c != null)
            c.close();
        return i;
    }

    /**
     * Возвращает все заказы по дате
     */
    public Cursor getAllBookings(Date date){
        Period period = Period.getBorderOfDay(date);
        return db.query(DBHelper.TABLE_BOOKINGS, null,
                DBHelper.COLUMN_BOOKINGS_DATE + " >= ? and "
                        + DBHelper.COLUMN_BOOKINGS_DATE + " <= ?",
                new String[]{Long.toString(period.GetStartDateLong()), Long.toString(period.GetEndDateLong())}, null,
                null, null);
    }

    /**
     * Возвращает все заказы по дате
     */
    public Cursor getAllBookings(long date){
        Period period = Period.getBorderOfDay(date);
        return db.query(DBHelper.TABLE_BOOKINGS, null,
                DBHelper.COLUMN_BOOKINGS_DATE + " >= ? and "
                + DBHelper.COLUMN_BOOKINGS_DATE + " <= ?",
                new String[]{Long.toString(period.GetStartDateLong()), Long.toString(period.GetEndDateLong())}, null,
                null, null);
    }

    /**
     * Возвращает количество заказов для заказчика
     * */
    public int getBookingManCountBooking(long bookingManId){
        String s = String.format(
                "select count(*) as cnt " +
                " from %s" +
                " where %s = ?",
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID);
        Cursor c = db.rawQuery(s, new String[]{String.valueOf(bookingManId)});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countColIndex = c.getColumnIndex("cnt");
                    if (countColIndex != -1)
                        return c.getInt(countColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает сумму заказов по заказчику
     * */
    public double getBookingManSumsBooking(long bookingManId){
        String s = String.format(
                "select sum(%s) as s_cp, sum(%s) as s_rp " +
                        " from %s" +
                        " where %s = ?",
                DBHelper.COLUMN_BOOKINGS_CAKE_PRICE,
                DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID);
        Cursor c = db.rawQuery(s, new String[]{String.valueOf(bookingManId)});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int recipePriceColIndex = c.getColumnIndex("s_rp");
                    int cakePriceColIndex = c.getColumnIndex("s_cp");
                    if (recipePriceColIndex == -1 && cakePriceColIndex == -1)
                        return 0;
                    return c.getDouble(cakePriceColIndex) - c.getDouble(recipePriceColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает количество заказчиков за месяц
     * */
    public int getCountBookingManPerMonth(Date d) {
        Period p = Period.getBorderOfMonth(d);
        String s = String.format(
                "select count(distinct %s) as cnt " +
                " from %s" +
                " where %s >= ?" +
                " and %s <= ?" +
                " and %s != '-1'",
                DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID
                );

        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong()) });
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countColIndex = c.getColumnIndex("cnt");
                    if (countColIndex != -1)
                        return c.getInt(countColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает количество заказчиков за год
     * */
    public int getCountBookingManPerYear(int year) {
        Period p = Period.getBorderOfYear(year);
        String s = String.format(
                "select count(distinct %s) as cnt " +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?" +
                        " and %s != '-1'",
                DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID);

        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong()) });
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countColIndex = c.getColumnIndex("cnt");
                    if (countColIndex != -1)
                        return c.getInt(countColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает количество заказчиков за все время
     * */
    public int getCountBookingManPerAllTime() {
        String s = String.format(
                "select count(distinct %s) as cnt " +
                        " from %s" +
                        " where %s != '-1'",
                DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID);
        Cursor c = db.rawQuery(s, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countColIndex = c.getColumnIndex("cnt");
                    if (countColIndex != -1)
                        return c.getInt(countColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает количество заказов за месяц (принимает любой день необходимого месяца в качестве даты)
     * */
    public int getCountBookingPerMonth(Date d){
        Period p = Period.getBorderOfMonth(d);
        String s = String.format(
                "select count(*) as cnt " +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?",
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countColIndex = c.getColumnIndex("cnt");
                    if (countColIndex != -1)
                        return c.getInt(countColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает сумму заказов за месяц (принимает любой день необходимого месяца в качестве даты)
     * */
    public double[] getSumsBookingPerMonth(Date d){
        Period p = Period.getBorderOfMonth(d);
        String s = String.format(
                "select sum(%s) as s_cp, sum(%s) as s_rp " +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?",
                DBHelper.COLUMN_BOOKINGS_CAKE_PRICE,
                DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int recipePriceColIndex = c.getColumnIndex("s_rp");
                    int cakePriceColIndex = c.getColumnIndex("s_cp");
                    if (recipePriceColIndex == -1 && cakePriceColIndex == -1)
                        return new double[]{ 0, 0 };
                    return new double[] { c.getDouble(recipePriceColIndex), c.getDouble(cakePriceColIndex) };
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return new double[]{ 0, 0 };
    }

    /**
     * Возвращает количество заказов за год
     * */
    public int getCountBookingPerYear(int year){
        Period p = Period.getBorderOfYear(year);
        String s = String.format(
                "select count(*) as cnt " +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?",
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countColIndex = c.getColumnIndex("cnt");
                    if (countColIndex != -1)
                        return c.getInt(countColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает сумму заказов за год
     * */
    public double[] getSumBookingPerYear(int year){
        Period p = Period.getBorderOfYear(year);
        String s = String.format(
                "select sum(%s) as s_cp, sum(%s) as s_rp " +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?",
                DBHelper.COLUMN_BOOKINGS_CAKE_PRICE,
                DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int recipePriceColIndex = c.getColumnIndex("s_rp");
                    int cakePriceColIndex = c.getColumnIndex("s_cp");
                    if (recipePriceColIndex == -1 && cakePriceColIndex == -1)
                        return new double[]{ 0, 0 };
                    return new double[] { c.getDouble(recipePriceColIndex), c.getDouble(cakePriceColIndex) };
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return new double[]{ 0, 0 };
    }

    /**
     * Возвращает количество заказов по заказчику
     * */
    public int getCountBookingPerAllTime(){
        String s = String.format(
                "select count(*) as cnt " +
                        " from %s" ,
                DBHelper.TABLE_BOOKINGS);
        Cursor c = db.rawQuery(s, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countColIndex = c.getColumnIndex("cnt");
                    if (countColIndex != -1)
                        return c.getInt(countColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает всю статистику за месяц
     * */
    public Statistic getStatisticPerMonth(Date d){
        Period p = Period.getBorderOfMonth(d);
        Statistic stat = new Statistic();
        String s = String.format(
                "select sum(%s) as s_cp, sum(%s) as s_rp, sum(%s) as ts, count(*) as cnt " +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?",
                DBHelper.COLUMN_BOOKINGS_CAKE_PRICE,
                DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE,
                DBHelper.COLUMN_BOOKINGS_TIME_SPENT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int recipePriceColIndex = c.getColumnIndex("s_rp");
                    int cakePriceColIndex = c.getColumnIndex("s_cp");
                    int countTimeSpentIndex = c.getColumnIndex("ts");
                    int countBookingsColIndex = c.getColumnIndex("cnt");
                    if (recipePriceColIndex == -1 && cakePriceColIndex == -1 && countTimeSpentIndex == -1 && countBookingsColIndex == -1)
                        return stat;
                    stat = new Statistic(
                            c.getDouble(recipePriceColIndex),
                            c.getDouble(cakePriceColIndex),
                            c.getInt(countBookingsColIndex),
                            c.getInt(countTimeSpentIndex)) ;
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        stat.setCountBookingMen(getCountBookingManPerMonth(d));
        s = String.format(
                "select distinct %s as bt_id, sum(%s) as cp" +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?" +
                        " group by %s",
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID);
        c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int btColIndex = c.getColumnIndex("bt_id");
                    int countProductsColIndex = c.getColumnIndex("cp");
                    if (btColIndex != -1 && countProductsColIndex != -1) {
                        do {
                            stat.addCountProduct(c.getLong(btColIndex), c.getInt(countProductsColIndex));
                        } while (c.moveToNext());
                    }
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        s = String.format(
                "select distinct %s as bt_id, count(%s) as cp" +
                " from %s" +
                " where %s >= ?" +
                " and %s <= ?" +
                " group by %s",
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID);
        c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int btColIndex = c.getColumnIndex("bt_id");
                    int countBookingTypeColIndex = c.getColumnIndex("cp");
                    if (btColIndex != -1 && countBookingTypeColIndex != -1) {
                        do {
                            stat.addCountBookingTypesBookings(c.getLong(btColIndex), c.getInt(countBookingTypeColIndex));
                        } while (c.moveToNext());
                    }
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return stat;
    }

    /**
     * Возвращает всю статистику за год
     * */
    public Statistic getStatisticPerYear(int year){
        Period p = Period.getBorderOfYear(year);
        Statistic stat = new Statistic();
        String s = String.format(
                "select sum(%s) as s_cp, sum(%s) as s_rp, sum(%s) as ts, count(*) as cnt " +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?",
                DBHelper.COLUMN_BOOKINGS_CAKE_PRICE,
                DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE,
                DBHelper.COLUMN_BOOKINGS_TIME_SPENT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int recipePriceColIndex = c.getColumnIndex("s_rp");
                    int cakePriceColIndex = c.getColumnIndex("s_cp");
                    int countTimeSpentIndex = c.getColumnIndex("ts");
                    int countBookingsColIndex = c.getColumnIndex("cnt");
                    if (recipePriceColIndex == -1 && cakePriceColIndex == -1 && countTimeSpentIndex == -1 && countBookingsColIndex == -1)
                        return stat;
                    stat = new Statistic(
                            c.getDouble(recipePriceColIndex),
                            c.getDouble(cakePriceColIndex),
                            c.getInt(countBookingsColIndex),
                            c.getInt(countTimeSpentIndex)) ;
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        stat.setCountBookingMen(getCountBookingManPerYear(year));
        s = String.format(
                "select distinct %s as bt_id, sum(%s) as cp" +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?" +
                        " group by %s",
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID);
        c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int btColIndex = c.getColumnIndex("bt_id");
                    int countProductsColIndex = c.getColumnIndex("cp");
                    if (btColIndex != -1 && countProductsColIndex != -1) {
                        do {
                            stat.addCountProduct(c.getLong(btColIndex), c.getInt(countProductsColIndex));
                        } while (c.moveToNext());
                    }
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        s = String.format(
                "select distinct %s as bt_id, count(%s) as cp" +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?" +
                        " group by %s",
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID);
        c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int btColIndex = c.getColumnIndex("bt_id");
                    int countBookingTypeColIndex = c.getColumnIndex("cp");
                    if (btColIndex != -1 && countBookingTypeColIndex != -1) {
                        do {
                            stat.addCountBookingTypesBookings(c.getLong(btColIndex), c.getInt(countBookingTypeColIndex));
                        } while (c.moveToNext());
                    }
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return stat;
    }

    /**
     * Возвращает всю статистику за всё время
     * */
    public Statistic getStatisticPerAllTime(){
        Statistic stat = new Statistic();
        String s = String.format(
                "select sum(%s) as s_cp, sum(%s) as s_rp, sum(%s) as ts, count(*) as cnt " +
                        " from %s",
                DBHelper.COLUMN_BOOKINGS_CAKE_PRICE,
                DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE,
                DBHelper.COLUMN_BOOKINGS_TIME_SPENT,
                DBHelper.TABLE_BOOKINGS);
        Cursor c = db.rawQuery(s, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int recipePriceColIndex = c.getColumnIndex("s_rp");
                    int cakePriceColIndex = c.getColumnIndex("s_cp");
                    int countTimeSpentIndex = c.getColumnIndex("ts");
                    int countBookingsColIndex = c.getColumnIndex("cnt");
                    if (recipePriceColIndex == -1 && cakePriceColIndex == -1 && countTimeSpentIndex == -1 && countBookingsColIndex == -1)
                        return stat;
                    stat = new Statistic(
                            c.getDouble(recipePriceColIndex),
                            c.getDouble(cakePriceColIndex),
                            c.getInt(countBookingsColIndex),
                            c.getInt(countTimeSpentIndex)) ;
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        stat.setCountBookingMen(getCountBookingManPerAllTime());
        s = String.format(
                "select distinct %s as bt_id, sum(%s) as cp" +
                " from %s" +
                " group by %s",
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID);
        c = db.rawQuery(s, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int btColIndex = c.getColumnIndex("bt_id");
                    int countProductsColIndex = c.getColumnIndex("cp");
                    if (btColIndex != -1 && countProductsColIndex != -1) {
                        do {
                            stat.addCountProduct(c.getLong(btColIndex), c.getInt(countProductsColIndex));
                        } while (c.moveToNext());
                    }
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        s = String.format(
                "select distinct %s as bt_id, count(%s) as cp" +
                        " from %s" +
                        " group by %s",
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID);
        c = db.rawQuery(s, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int btColIndex = c.getColumnIndex("bt_id");
                    int countBookingTypeColIndex = c.getColumnIndex("cp");
                    if (btColIndex != -1 && countBookingTypeColIndex != -1) {
                        do {
                            stat.addCountBookingTypesBookings(c.getLong(btColIndex), c.getInt(countBookingTypeColIndex));
                        } while (c.moveToNext());
                    }
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return stat;
    }

    /**
     * Возвращает все месяцы, от первого до последнего, на которые были заказы (пустые не игнорирует)
     * */
    public ArrayList<String> getAllMonthes(Date first, Date last){
        ArrayList<String> monthes = new ArrayList<String>();

        String[] monthNames = new String[]{"Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"};

        Calendar counter = Calendar.getInstance();
        counter.setTime(first);
        while(counter.getTime().getTime() < last.getTime()){
            monthes.add(String.format("%s %s", monthNames[counter.get(Calendar.MONTH)], counter.get(Calendar.YEAR)));
            counter.set(Calendar.MONTH, counter.get(Calendar.MONTH) + 1);
        }
        return monthes;
    }

    /**
     * возвращает первый по времени заказ
     */
    public Booking getFirstBooking() {
        Cursor c = db.rawQuery("select * " +
                            " from bookings b1 " +
                            " where b1.date = " +
                            "(" +
                            " select min(b2.date)" +
                            " from bookings b2" +
                            ")", null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    return new Booking(c);
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return null;
    }

    /**
     * возвращает последний по времени заказ
     */
    public Booking getLastBooking() {
        Cursor c = db.rawQuery("select * " +
                " from bookings b1 " +
                " where b1.date = " +
                "(" +
                " select max(b2.date)" +
                " from bookings b2" +
                ")", null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    return new Booking(c);
                }
            }
        } finally {
            if (c != null)
                c.close();
        }
        return null;
    }

    /**
     * Возвращает сумму потраченных часов за месяц
     * */
    public int getTimeSpentPerMonth(Date d){
        Period p = Period.getBorderOfMonth(d);
        String s = String.format(
                "select sum(%s) as ts" +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?",
                DBHelper.COLUMN_BOOKINGS_TIME_SPENT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countProductColIndex = c.getColumnIndex("ts");
                    if (countProductColIndex == -1)
                        return 0;
                    return c.getInt(countProductColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает сумму потраченных часов за год
     * */
    public int getTimeSpentPerYear(int year){
        Period p = Period.getBorderOfYear(year);
        String s = String.format(
                "select sum(%s) as ts" +
                        " from %s" +
                        " where %s >= ?" +
                        " and %s <= ?",
                DBHelper.COLUMN_BOOKINGS_TIME_SPENT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countProductColIndex = c.getColumnIndex("ts");
                    if (countProductColIndex == -1)
                        return 0;
                    return c.getInt(countProductColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает сумму потраченных часов за всё время
     * */
    public int getTimeSpentPerAllTime(){
        String s = String.format(
                "select sum(%s) as ts" +
                        " from %s",
                DBHelper.COLUMN_BOOKINGS_TIME_SPENT,
                DBHelper.TABLE_BOOKINGS);
        Cursor c = db.rawQuery(s, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countProductColIndex = c.getColumnIndex("ts");
                    if (countProductColIndex == -1)
                        return 0;
                    return c.getInt(countProductColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает количество штук за месяц
     * */
    public int getCountProductPerMonth(Date d, long bookingTypeId){
        Period p = Period.getBorderOfMonth(d);
        String s = String.format(
                "select sum(%s) as cp" +
                        " from %s" +
                        " where %s = ?" +
                        " and %s >= ?" +
                        " and %s <= ?",
                DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ String.valueOf(bookingTypeId), Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countProductColIndex = c.getColumnIndex("cp");
                    if (countProductColIndex == -1)
                        return 0;
                    return c.getInt(countProductColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает количество штук за год
     * */
    public int getCountProductPerYear(int year, long bookingTypeId){
        Period p = Period.getBorderOfYear(year);
        String s = String.format(
                "select sum(%s) as cp" +
                        " from %s" +
                        " where %s = ?" +
                        " and %s >= ?" +
                        " and %s <= ?",
                DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID,
                DBHelper.COLUMN_BOOKINGS_DATE,
                DBHelper.COLUMN_BOOKINGS_DATE);
        Cursor c = db.rawQuery(s, new String[]{ String.valueOf(bookingTypeId), Long.toString(p.GetStartDateLong()), Long.toString(p.GetEndDateLong())});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countProductColIndex = c.getColumnIndex("cp");
                    if (countProductColIndex == -1)
                        return 0;
                    return c.getInt(countProductColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }

    /**
     * Возвращает количество штук за всё время
     * */
    public int getCountProductPerAllTime(long bookingTypeId){
        String s = String.format(
                "select sum(%s) as cp" +
                " from %s" +
                " where %s = ?",
                DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT,
                DBHelper.TABLE_BOOKINGS,
                DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID);
        Cursor c = db.rawQuery(s, new String[]{ String.valueOf(bookingTypeId)});
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int countProductColIndex = c.getColumnIndex("cp");
                    if (countProductColIndex == -1)
                        return 0;
                    return c.getInt(countProductColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return 0;
    }



}

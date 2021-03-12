package yummycherrypie.base_classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import yummycherrypie.pl.DateExtension;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.IDataBaseUsable;

/**
 * Created by piskarev on 15.09.2015.
 * Событие: много заказов на один день от одного заказчика
 */
public class Event implements IDataBaseUsable {

    public static final int DEFAULT_EVENT_ID = -1;

    private long id;
    private long dateLong;
    private long bookingManId;
    private String name;
    private String place;
    private double price;
    private long createDate;
    private long updateDate;

    /* геттеры-сеттеры ниже */

    /**
     * пустое событие
     * */
    public static final String EMPTY_EVENT = "Не выбрано";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDateLong() {
        return dateLong;
    }

    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }

    public long getBookingManId() {
        return bookingManId;
    }

    public void setBookingManId(long bookingManId) {
        this.bookingManId = bookingManId;
    }

    public String getName() {
        if (name != null)
            return name;
        return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        if (place != null)
            return place;
        return "";
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    /* контструкторы ниже */

    public Event(){}

    public Event(String name, String place, long dateLong, long bookingManId){
        this.name = name;
        this.place = place;
        this.dateLong = dateLong;
        this.bookingManId = bookingManId;
    }

    public Event(long id, String name, String place, long dateLong, long bookingManId){
        this.id = id;
        this.name = name;
        this.place = place;
        this.dateLong = dateLong;
        this.bookingManId = bookingManId;
    }

    public Event(long id, String name, String place, long dateLong, long bookingManId, double price){
        this.id = id;
        this.name = name;
        this.place = place;
        this.dateLong = dateLong;
        this.bookingManId = bookingManId;
        this.price = price;
    }

    public Event(String name, String place, long dateLong, long bookingManId, double price){
        this.name = name;
        this.place = place;
        this.dateLong = dateLong;
        this.bookingManId = bookingManId;
        this.price = price;
    }

    public Event (Cursor c, Map<String, Integer> map){
        this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
        this.name = c.getString(map.get(DBHelper.COLUMN_EVENTS_NAME));
        this.place = c.getString(map.get(DBHelper.COLUMN_EVENTS_PLACE));
        this.dateLong = c.getLong(map.get(DBHelper.COLUMN_EVENTS_DATE));
        this.bookingManId = c.getLong(map.get(DBHelper.COLUMN_EVENTS_BOOKING_MAN_ID));
        this.createDate = c.getLong(map.get(DBHelper.COLUMN_EVENTS_CREATE_DATE));
        this.updateDate = c.getLong(map.get(DBHelper.COLUMN_EVENTS_UPDATE_DATE));
        this.price = c.getLong(map.get(DBHelper.COLUMN_EVENTS_PRICE));
    }

    public Event (Cursor c){
        Map<String, Integer> map = getColumnIndexes(c);
        this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
        this.name = c.getString(map.get(DBHelper.COLUMN_EVENTS_NAME));
        this.place = c.getString(map.get(DBHelper.COLUMN_EVENTS_PLACE));
        this.dateLong = c.getLong(map.get(DBHelper.COLUMN_EVENTS_DATE));
        this.bookingManId = c.getLong(map.get(DBHelper.COLUMN_EVENTS_BOOKING_MAN_ID));
        this.createDate = c.getLong(map.get(DBHelper.COLUMN_EVENTS_CREATE_DATE));
        this.updateDate = c.getLong(map.get(DBHelper.COLUMN_EVENTS_UPDATE_DATE));
        this.price = c.getLong(map.get(DBHelper.COLUMN_EVENTS_PRICE));
    }

    public ContentValues getInsertedColumns() {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_EVENTS_DATE, dateLong);
        cv.put(DBHelper.COLUMN_EVENTS_NAME, name);
        cv.put(DBHelper.COLUMN_EVENTS_PLACE, place);
        cv.put(DBHelper.COLUMN_EVENTS_BOOKING_MAN_ID, bookingManId);
        cv.put(DBHelper.COLUMN_EVENTS_CREATE_DATE, createDate);
        cv.put(DBHelper.COLUMN_EVENTS_UPDATE_DATE, updateDate);
        cv.put(DBHelper.COLUMN_EVENTS_PRICE, price);
        return cv;
    }

    public ContentValues getInsertedColumnsWithId() {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_ID, id);
        cv.put(DBHelper.COLUMN_EVENTS_DATE, dateLong);
        cv.put(DBHelper.COLUMN_EVENTS_NAME, name);
        cv.put(DBHelper.COLUMN_EVENTS_PLACE, place);
        cv.put(DBHelper.COLUMN_EVENTS_BOOKING_MAN_ID, bookingManId);
        cv.put(DBHelper.COLUMN_EVENTS_CREATE_DATE, createDate);
        cv.put(DBHelper.COLUMN_EVENTS_UPDATE_DATE, updateDate);
        cv.put(DBHelper.COLUMN_EVENTS_PRICE, price);
        return cv;
    }

    public ContentValues getUpdatedColumns(){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_EVENTS_DATE, dateLong);
        cv.put(DBHelper.COLUMN_EVENTS_NAME, name);
        cv.put(DBHelper.COLUMN_EVENTS_PLACE, place);
        cv.put(DBHelper.COLUMN_EVENTS_BOOKING_MAN_ID, bookingManId);
        cv.put(DBHelper.COLUMN_EVENTS_UPDATE_DATE, updateDate);
        cv.put(DBHelper.COLUMN_EVENTS_PRICE, price);
        return cv;
    }

    public Map<String, Integer> getColumnIndexes(Cursor c){
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put(DBHelper.COLUMN_ID, c.getColumnIndex(DBHelper.COLUMN_ID));
        result.put(DBHelper.COLUMN_EVENTS_DATE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_DATE));
        result.put(DBHelper.COLUMN_EVENTS_NAME, c.getColumnIndex(DBHelper.COLUMN_EVENTS_NAME));
        result.put(DBHelper.COLUMN_EVENTS_PLACE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_PLACE));
        result.put(DBHelper.COLUMN_EVENTS_BOOKING_MAN_ID, c.getColumnIndex(DBHelper.COLUMN_EVENTS_BOOKING_MAN_ID));
        result.put(DBHelper.COLUMN_EVENTS_CREATE_DATE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_CREATE_DATE));
        result.put(DBHelper.COLUMN_EVENTS_UPDATE_DATE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_UPDATE_DATE));
        result.put(DBHelper.COLUMN_EVENTS_PRICE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_PRICE));
        return result;
    }

    public Map<String, Integer> getColumnIndexes(Cursor c, Map<String, Integer> result){
        result.put(DBHelper.COLUMN_ID, c.getColumnIndex(DBHelper.COLUMN_ID));
        result.put(DBHelper.COLUMN_EVENTS_DATE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_DATE));
        result.put(DBHelper.COLUMN_EVENTS_NAME, c.getColumnIndex(DBHelper.COLUMN_EVENTS_NAME));
        result.put(DBHelper.COLUMN_EVENTS_PLACE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_PLACE));
        result.put(DBHelper.COLUMN_EVENTS_BOOKING_MAN_ID, c.getColumnIndex(DBHelper.COLUMN_EVENTS_BOOKING_MAN_ID));
        result.put(DBHelper.COLUMN_EVENTS_CREATE_DATE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_CREATE_DATE));
        result.put(DBHelper.COLUMN_EVENTS_UPDATE_DATE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_UPDATE_DATE));
        result.put(DBHelper.COLUMN_EVENTS_PRICE, c.getColumnIndex(DBHelper.COLUMN_EVENTS_PRICE));
        return result;
    }

    /* остальное ниже */

    @Override
    public String toString() {
        return String.format("Event: id:%s; dateLong:%s; bookingManId:%s; name:%s; place:%s; " +
                        "price:%s; createDate:%s; updateDate:%s.",
                        id, DateExtension.getDate(dateLong), bookingManId, name, place, price,
                DateExtension.getDateTime(createDate), DateExtension.getDateTime(updateDate));
    }
}

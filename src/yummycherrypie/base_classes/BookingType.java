package yummycherrypie.base_classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import yummycherrypie.pl.DateExtension;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.IDataBaseUsable;

/**
 * Тип заказа
 * Created by piskarev on 18.09.2015.
 */
public class BookingType implements IDataBaseUsable {

    /* закрытие поля ниже */

    public static final int DEFAULT_BOOKING_TYPE_ID = -1;
    
    private long id;
    private String name;
    private boolean isCountable;
    private long createDate;
    private long updateDate;

    /* открытие поля ниже */

    public enum Types{
        CAKE,//торт
        CAKE_TIEF,//ярус торта
        CUPCAKE,//капкейки
        COOKIES//печенье
    }

    /* геттеры-сеттеры ниже */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        if (name != null)
            return name;
        return "";
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isCountable() { return isCountable; }

    public void setIsCountable(boolean isCountable) { this.isCountable = isCountable;  }

    /* контструкторы ниже */

    public BookingType() {}

    public BookingType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public BookingType(String name, boolean isCountable) {
        this.name = name;
        this.isCountable = isCountable;
    }

    public ContentValues getInsertedColumns() {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_NAME, name);
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_IS_COUNTABLE, isCountable);
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_CREATE_DATE, createDate);
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_UPDATE_DATE, updateDate);
        return cv;
    }

    public ContentValues getInsertedColumnsWithId() {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_ID, id);
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_NAME, name);
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_IS_COUNTABLE, isCountable);
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_CREATE_DATE, createDate);
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_UPDATE_DATE, updateDate);
        return cv;
    }

    public ContentValues getUpdatedColumns(){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_NAME, name);
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_IS_COUNTABLE, isCountable);
        cv.put(DBHelper.COLUMN_BOOKING_TYPES_UPDATE_DATE, updateDate);
        return cv;
    }

    public Map<String, Integer> getColumnIndexes(Cursor c) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put(DBHelper.COLUMN_ID, c.getColumnIndex(DBHelper.COLUMN_ID));
        result.put(DBHelper.COLUMN_BOOKING_TYPES_NAME, c.getColumnIndex(DBHelper.COLUMN_BOOKING_TYPES_NAME));
        result.put(DBHelper.COLUMN_BOOKING_TYPES_IS_COUNTABLE, c.getColumnIndex(DBHelper.COLUMN_BOOKING_TYPES_IS_COUNTABLE));
        result.put(DBHelper.COLUMN_BOOKING_TYPES_CREATE_DATE, c.getColumnIndex(DBHelper.COLUMN_BOOKING_TYPES_CREATE_DATE));
        result.put(DBHelper.COLUMN_BOOKING_TYPES_UPDATE_DATE, c.getColumnIndex(DBHelper.COLUMN_BOOKING_TYPES_UPDATE_DATE));
        return result;
    }

    public BookingType(Cursor c, Map<String, Integer> map) {
        this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
        this.name = c.getString(map.get(DBHelper.COLUMN_BOOKING_TYPES_NAME));
        this.isCountable = c.getInt(map.get(DBHelper.COLUMN_BOOKING_TYPES_IS_COUNTABLE)) == 1;
        this.createDate = c.getLong(map.get(DBHelper.COLUMN_BOOKING_TYPES_CREATE_DATE));
        this.updateDate = c.getLong(map.get(DBHelper.COLUMN_BOOKING_TYPES_UPDATE_DATE));
    }

    public BookingType(Cursor c) {
        Map<String, Integer> map = getColumnIndexes(c);
        this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
        this.name = c.getString(map.get(DBHelper.COLUMN_BOOKING_TYPES_NAME));
        this.isCountable = c.getInt(map.get(DBHelper.COLUMN_BOOKING_TYPES_IS_COUNTABLE)) == 1;
        this.createDate = c.getLong(map.get(DBHelper.COLUMN_BOOKING_TYPES_CREATE_DATE));
        this.updateDate = c.getLong(map.get(DBHelper.COLUMN_BOOKING_TYPES_UPDATE_DATE));
    }

    /* остальное ниже */

    @Override
    public String toString(){
        return String.format("BookingType: id:%s; name:%s; createDate:%s; updateDate:%s.",
                id, name, DateExtension.getDateTime(createDate), DateExtension.getDateTime(updateDate));
    }
}

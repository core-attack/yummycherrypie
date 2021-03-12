package yummycherrypie.base_classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import yummycherrypie.pl.DateExtension;
import yummycherrypie.pl.PhoneExtension;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.IDataBaseUsable;

/**
 * Created by CoreAttack on 27.07.2015.
 * Заказчик
 */
public class BookingMan implements IDataBaseUsable {

    /* закрытие поля ниже */

    public static final int DEFAULT_BOOKING_MAN_ID = -1;
    
    private long id;
    private String name;
    private String phone;
    private String vkProfile;
    private long createDate;
    private long updateDate;

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

    public String getPhone() {
        return PhoneExtension.beautyPhoneNumber(phone);
    }

    /**
     * Как хранится в БД
     * @return
     */
    public String getRealPhone(){
        if (phone != null)
            return phone;
        return "";
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNameWithPhone(){
        if (getPhone().isEmpty())
            return getName();
        return String.format("%s (%s)", getName(), getPhone());
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

    public String getVkProfile() {
        return vkProfile;
    }

    public void setVkProfile(String vkProfile) {
        this.vkProfile = vkProfile;
    }

    /* контструкторы ниже */

    public BookingMan(){}

    public BookingMan(long id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public BookingMan(String name, String phone, String vkProfile){
        this.name = name;
        this.phone = phone;
        this.vkProfile = vkProfile;
    }

    public ContentValues getInsertedColumns() {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_BOOKING_MEN_NAME, name);
        cv.put(DBHelper.COLUMN_BOOKING_MEN_PHONE, phone);
        cv.put(DBHelper.COLUMN_BOOKING_MEN_VK_PROFILE, vkProfile);
        return cv;
    }

    public ContentValues getInsertedColumnsWithId() {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_ID, id);
        cv.put(DBHelper.COLUMN_BOOKING_MEN_NAME, name);
        cv.put(DBHelper.COLUMN_BOOKING_MEN_PHONE, phone);
        cv.put(DBHelper.COLUMN_BOOKING_MEN_VK_PROFILE, vkProfile);
        return cv;
    }

    public ContentValues getUpdatedColumns(){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_BOOKING_MEN_NAME, name);
        cv.put(DBHelper.COLUMN_BOOKING_MEN_PHONE, phone);
        cv.put(DBHelper.COLUMN_BOOKING_MEN_VK_PROFILE, vkProfile);
        return cv;
    }

    public Map<String, Integer> getColumnIndexes(Cursor c){
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put(DBHelper.COLUMN_ID, c.getColumnIndex(DBHelper.COLUMN_ID));
        result.put(DBHelper.COLUMN_BOOKING_MEN_NAME, c.getColumnIndex(DBHelper.COLUMN_BOOKING_MEN_NAME));
        result.put(DBHelper.COLUMN_BOOKING_MEN_PHONE, c.getColumnIndex(DBHelper.COLUMN_BOOKING_MEN_PHONE));
        result.put(DBHelper.COLUMN_BOOKING_MEN_VK_PROFILE, c.getColumnIndex(DBHelper.COLUMN_BOOKING_MEN_VK_PROFILE));
        return result;
    }

    public BookingMan(Cursor c, Map<String, Integer> map) {
        this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
        this.name = c.getString(map.get(DBHelper.COLUMN_BOOKING_MEN_NAME));
        this.phone = c.getString(map.get(DBHelper.COLUMN_BOOKING_MEN_PHONE));
        this.vkProfile = c.getString(map.get(DBHelper.COLUMN_BOOKING_MEN_VK_PROFILE));
    }

    public BookingMan(Cursor c) {
        Map<String, Integer> map = getColumnIndexes(c);
        this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
        this.name = c.getString(map.get(DBHelper.COLUMN_BOOKING_MEN_NAME));
        this.phone = c.getString(map.get(DBHelper.COLUMN_BOOKING_MEN_PHONE));
        this.vkProfile = c.getString(map.get(DBHelper.COLUMN_BOOKING_MEN_VK_PROFILE));
    }

    /* остальное ниже */

    @Override
    public String toString() {
        return String.format("BookingType: id:%s; name:%s; phone:%s; vkProfile:%s; createDate:%s; updateDate:%s.",
                id, name, phone, vkProfile, DateExtension.getDateTime(createDate), DateExtension.getDateTime(updateDate));
    }

    @Override
    public boolean equals(Object bm){
        return this.getId() == ((BookingMan)bm).getId()
                && this.getName().equals(((BookingMan)bm).getName())
                && this.getRealPhone().equals(((BookingMan)bm).getRealPhone());
    }
}

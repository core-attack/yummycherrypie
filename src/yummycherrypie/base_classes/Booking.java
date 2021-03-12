package yummycherrypie.base_classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.IDataBaseUsable;
import yummycherrypie.pl.DateExtension;

/***
 * Заказ
 */
public class Booking implements IDataBaseUsable {

	/* закрытие поля ниже */

	private long id;
	private long dateLong;//дата в шестнадцатиричном формате
	private long recipeId = Recipe.DEFAULT_RECIPE_ID;
	private double weight;
	private double cakePrice;
	private double recipePrice;
	private double discount;
	private long bookingManId = BookingMan.DEFAULT_BOOKING_MAN_ID;
	private long bookingTypeId = BookingType.DEFAULT_BOOKING_TYPE_ID;
	private long eventId = Event.DEFAULT_EVENT_ID;
	private String comment;
	private long createDate;
	private long updateDate;
	private int countProduct;
	private int timeSpent;

	/* открытие поля ниже */

	public static final int DEFAULT_BOOKING_ID = -1;

	/* геттеры-сеттеры ниже */

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

	public long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(long recipeId) {
		this.recipeId = recipeId;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getCakePrice() {
		return cakePrice;
	}

	public void setCakePrice(double cakePrice) {
		this.cakePrice = cakePrice;
	}

	public double getRecipePrice() {
		return recipePrice;
	}

	public void setRecipePrice(double recipePrice) {
		this.recipePrice = recipePrice;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public long getBookingManId() {
		return bookingManId;
	}

	public void setBookingManId(long bookingManId) {
		this.bookingManId = bookingManId;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getComment() {
		if (comment != null)
			return comment;
		return "";
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public void setUpdateDate(long updateDate) { this.updateDate = updateDate;	}

	public long getBookingTypeId() { return bookingTypeId; }

	public void setBookingTypeId(long bookingTypeId) { this.bookingTypeId = bookingTypeId;	}

	public int getCountProduct() {	return countProduct; }

	public void setCountProduct(int countProduct) {	this.countProduct = countProduct; }

	public int getTimeSpent() {	return timeSpent; }

	public void setTimeSpent(int timeSpent) { this.timeSpent = timeSpent; }

	/* контструкторы ниже */

	public Booking(){}

	public Booking(long date, long recipeId,
				   long bookingManId, String commnet, double cakePrice,
				   double recipePrice, double weight, double discount, long eventId, long bookingTypeId, int countProduct, int timeSpent) {
		dateLong = date;
		this.recipeId = recipeId;
		this.bookingManId = bookingManId;
		this.comment = commnet;
		this.cakePrice = cakePrice;
		this.recipePrice = recipePrice;
		this.weight = weight;
		this.discount = discount;
		this.eventId = eventId;
		this.bookingTypeId = bookingTypeId;
		this.countProduct = countProduct;
		this.timeSpent = timeSpent;
	}

	public Booking(long id, long date, long recipeId,
				   long bookingManId, String commnet, double cakePrice,
				   double recipePrice, double weight, double discount, long eventId, long bookingTypeId, int countProduct, int timeSpent) {
		this.id = id;
		dateLong = date;
		this.recipeId = recipeId;
		this.bookingManId = bookingManId;
		this.comment = commnet;
		this.cakePrice = cakePrice;
		this.recipePrice = recipePrice;
		this.weight = weight;
		this.discount = discount;
		this.eventId = eventId;
		this.bookingTypeId = bookingTypeId;
		this.countProduct = countProduct;
		this.timeSpent = timeSpent;
	}

	public ContentValues getInsertedColumns() {
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_BOOKINGS_DATE, dateLong);
		cv.put(DBHelper.COLUMN_BOOKINGS_RECIPE_ID, recipeId);
		cv.put(DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID, bookingManId);
		cv.put(DBHelper.COLUMN_BOOKINGS_COMMENT, comment);
		cv.put(DBHelper.COLUMN_BOOKINGS_CAKE_PRICE, cakePrice);
		cv.put(DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE, recipePrice);
		cv.put(DBHelper.COLUMN_BOOKINGS_WEIGHT, weight);
		cv.put(DBHelper.COLUMN_BOOKINGS_DISCOUNT, discount);
		cv.put(DBHelper.COLUMN_BOOKINGS_EVENT_ID, eventId);
		cv.put(DBHelper.COLUMN_BOOKINGS_CREATE_DATE, createDate);
		cv.put(DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID, bookingTypeId);
		cv.put(DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT, countProduct);
		cv.put(DBHelper.COLUMN_BOOKINGS_TIME_SPENT, timeSpent);
		return cv;
	}

	public ContentValues getInsertedColumnsWithId() {
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_ID, id);
		cv.put(DBHelper.COLUMN_BOOKINGS_DATE, dateLong);
		cv.put(DBHelper.COLUMN_BOOKINGS_RECIPE_ID, recipeId);
		cv.put(DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID, bookingManId);
		cv.put(DBHelper.COLUMN_BOOKINGS_COMMENT, comment);
		cv.put(DBHelper.COLUMN_BOOKINGS_CAKE_PRICE, cakePrice);
		cv.put(DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE, recipePrice);
		cv.put(DBHelper.COLUMN_BOOKINGS_WEIGHT, weight);
		cv.put(DBHelper.COLUMN_BOOKINGS_DISCOUNT, discount);
		cv.put(DBHelper.COLUMN_BOOKINGS_EVENT_ID, eventId);
		cv.put(DBHelper.COLUMN_BOOKINGS_CREATE_DATE, createDate);
		cv.put(DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID, bookingTypeId);
		cv.put(DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT, countProduct);
		cv.put(DBHelper.COLUMN_BOOKINGS_TIME_SPENT, timeSpent);
		return cv;
	}

	public ContentValues getUpdatedColumns(){
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_BOOKINGS_DATE, dateLong);
		cv.put(DBHelper.COLUMN_BOOKINGS_RECIPE_ID, recipeId);
		cv.put(DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID, bookingManId);
		cv.put(DBHelper.COLUMN_BOOKINGS_COMMENT, comment);
		cv.put(DBHelper.COLUMN_BOOKINGS_CAKE_PRICE, cakePrice);
		cv.put(DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE, recipePrice);
		cv.put(DBHelper.COLUMN_BOOKINGS_WEIGHT, weight);
		cv.put(DBHelper.COLUMN_BOOKINGS_DISCOUNT, discount);
		cv.put(DBHelper.COLUMN_BOOKINGS_EVENT_ID, eventId);
		cv.put(DBHelper.COLUMN_BOOKINGS_UPDATE_DATE, updateDate);
		cv.put(DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID, bookingTypeId);
		cv.put(DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT, countProduct);
		cv.put(DBHelper.COLUMN_BOOKINGS_TIME_SPENT, timeSpent);
		return cv;
	}

	public Booking(Cursor c, Map<String, Integer> map) {
		this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
		this.dateLong = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_DATE));
		this.comment = c.getString(map.get(DBHelper.COLUMN_BOOKINGS_COMMENT));
		this.recipeId = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_RECIPE_ID));
		this.bookingManId = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID));
		this.recipePrice = c.getDouble(map.get(DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE));
		this.cakePrice = c.getDouble(map.get(DBHelper.COLUMN_BOOKINGS_CAKE_PRICE));
		this.weight = c.getDouble(map.get(DBHelper.COLUMN_BOOKINGS_WEIGHT));
		this.discount = c.getDouble(map.get(DBHelper.COLUMN_BOOKINGS_DISCOUNT));
		this.eventId = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_EVENT_ID));
		this.createDate = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_CREATE_DATE));
		this.updateDate = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_UPDATE_DATE));
		this.bookingTypeId = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID));
		this.countProduct = c.getInt(map.get(DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT));
		this.timeSpent = c.getInt(map.get(DBHelper.COLUMN_BOOKINGS_TIME_SPENT));
	}

	public Booking(Cursor c) {
		Map<String, Integer> map = getColumnIndexes(c);
		this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
		this.dateLong = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_DATE));
		this.comment = c.getString(map.get(DBHelper.COLUMN_BOOKINGS_COMMENT));
		this.recipeId = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_RECIPE_ID));
		this.bookingManId = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID));
		this.recipePrice = c.getDouble(map.get(DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE));
		this.cakePrice = c.getDouble(map.get(DBHelper.COLUMN_BOOKINGS_CAKE_PRICE));
		this.weight = c.getDouble(map.get(DBHelper.COLUMN_BOOKINGS_WEIGHT));
		this.discount = c.getDouble(map.get(DBHelper.COLUMN_BOOKINGS_DISCOUNT));
		this.eventId = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_EVENT_ID));
		this.createDate = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_CREATE_DATE));
		this.updateDate = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_UPDATE_DATE));
		this.bookingTypeId = c.getLong(map.get(DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID));
		this.countProduct = c.getInt(map.get(DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT));
		this.timeSpent = c.getInt(map.get(DBHelper.COLUMN_BOOKINGS_TIME_SPENT));
	}

	public Map<String, Integer> getColumnIndexes(Cursor c){
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put(DBHelper.COLUMN_ID, c.getColumnIndex(DBHelper.COLUMN_ID));
		result.put(DBHelper.COLUMN_BOOKINGS_DATE, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_DATE));
		result.put(DBHelper.COLUMN_BOOKINGS_COMMENT, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_COMMENT));
		result.put(DBHelper.COLUMN_BOOKINGS_RECIPE_ID, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_RECIPE_ID));
		result.put(DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_BOOKING_MAN_ID));
		result.put(DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_RECIPE_PRICE));
		result.put(DBHelper.COLUMN_BOOKINGS_CAKE_PRICE, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_CAKE_PRICE));
		result.put(DBHelper.COLUMN_BOOKINGS_WEIGHT, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_WEIGHT));
		result.put(DBHelper.COLUMN_BOOKINGS_DISCOUNT, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_DISCOUNT));
		result.put(DBHelper.COLUMN_BOOKINGS_EVENT_ID, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_EVENT_ID));
		result.put(DBHelper.COLUMN_BOOKINGS_CREATE_DATE, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_CREATE_DATE));
		result.put(DBHelper.COLUMN_BOOKINGS_UPDATE_DATE, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_UPDATE_DATE));
		result.put(DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_BOOKING_TYPE_ID));
		result.put(DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_COUNT_PRODUCT));
		result.put(DBHelper.COLUMN_BOOKINGS_TIME_SPENT, c.getColumnIndex(DBHelper.COLUMN_BOOKINGS_TIME_SPENT));
		return result;
	}

	/* остальное ниже */

	@Override
	public String toString() {
		return String.format("Booking: id:%s; dateLong:%s (%s); timeLong:%s; comment:%s; recipeId:%s; " +
						"bookingManId:%s; bookingTypeId:%s, recipePrice:%s; cakePrice:%s; weight:%s; " +
						"discount:%s; eventId:%s; createDate:%s; updateDate:%s; countProduct:%s; timeSpent:%s",
				id, DateExtension.getDate(dateLong), dateLong, DateExtension.getTime(dateLong), comment, recipeId,
				bookingManId, bookingTypeId, recipePrice, cakePrice, weight,
				discount, eventId, DateExtension.getDateTime(createDate), DateExtension.getDateTime(updateDate), countProduct, timeSpent);
	}

}

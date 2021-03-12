package yummycherrypie.base_classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.IDataBaseUsable;
import yummycherrypie.pl.DateExtension;

/***
 * Рецепт
 */
public class Recipe  implements IDataBaseUsable {

	public static final int DEFAULT_RECIPE_ID = -1;

	private long id;
	private String name;
	private double cakeWeight;
	private int countProduct;
	private long createDate;
	private long updateDate;
	private boolean isCountable;

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

	public double getCakeWeight() {
		return cakeWeight;
	}

	public void setCakeWeight(double cakeWeight) {
		this.cakeWeight = cakeWeight;
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

	public int getCountProduct() {	return countProduct;}

	public void setCountProduct(int countProduct) {	this.countProduct = countProduct;	}

	public void setIsCountable(boolean isCountable) {
		this.isCountable = isCountable;
	}

	/* контструкторы ниже */

	public Recipe(){}

	public Recipe(long id, String name, long createDate, long updateDate,
			double cakeWeight, int countProduct, boolean isCountable) {
		this.id = id;
		this.name = name;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.cakeWeight = cakeWeight;
		this.countProduct = countProduct;
		this.isCountable = isCountable;
	}

	public Recipe(long id, String name,
			double cakeWeight, int countProduct, boolean isCountable) {
		this.id = id;
		this.name = name;
		this.cakeWeight = cakeWeight;
		this.countProduct = countProduct;
		this.isCountable = isCountable;
	}

	public Recipe(String name, double cakeWeight, int countProduct, boolean isCountable) {
		this.name = name;
		this.cakeWeight = cakeWeight;
		this.countProduct = countProduct;
		this.isCountable = isCountable;
	}

	public Recipe(Cursor c, Map<String, Integer> map) {
		this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
		this.name = c.getString(map.get(DBHelper.COLUMN_RECIPES_NAME));
		this.createDate = c.getLong(map.get(DBHelper.COLUMN_RECIPES_CREATE_DATE));
		this.updateDate = c.getLong(map.get(DBHelper.COLUMN_RECIPES_UPDATE_DATE));
		this.cakeWeight = c.getDouble(map.get(DBHelper.COLUMN_RECIPES_CAKE_WEIGHT));
		this.countProduct = c.getInt(map.get(DBHelper.COLUMN_RECIPES_COUNT_PRODUCT));
		this.isCountable = c.getInt(map.get(DBHelper.COLUMN_RECIPES_IS_COUNTABLE)) == 1;
	}

	public Recipe(Cursor c) {
		Map<String, Integer> map = getColumnIndexes(c);
		this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
		this.name = c.getString(map.get(DBHelper.COLUMN_RECIPES_NAME));
		this.createDate = c.getLong(map.get(DBHelper.COLUMN_RECIPES_CREATE_DATE));
		this.updateDate = c.getLong(map.get(DBHelper.COLUMN_RECIPES_UPDATE_DATE));
		this.cakeWeight = c.getDouble(map.get(DBHelper.COLUMN_RECIPES_CAKE_WEIGHT));
		this.countProduct = c.getInt(map.get(DBHelper.COLUMN_RECIPES_COUNT_PRODUCT));
		this.isCountable = c.getInt(map.get(DBHelper.COLUMN_RECIPES_IS_COUNTABLE)) == 1;

	}

	public ContentValues getInsertedColumns() {
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_RECIPES_NAME, name);
		cv.put(DBHelper.COLUMN_RECIPES_CREATE_DATE, createDate);
		cv.put(DBHelper.COLUMN_RECIPES_CAKE_WEIGHT, cakeWeight);
		cv.put(DBHelper.COLUMN_RECIPES_COUNT_PRODUCT, countProduct);
		cv.put(DBHelper.COLUMN_RECIPES_IS_COUNTABLE, isCountable);
		return cv;
	}

	public ContentValues getInsertedColumnsWithId() {
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_ID, id);
		cv.put(DBHelper.COLUMN_RECIPES_NAME, name);
		cv.put(DBHelper.COLUMN_RECIPES_CREATE_DATE, createDate);
		cv.put(DBHelper.COLUMN_RECIPES_CAKE_WEIGHT, cakeWeight);
		cv.put(DBHelper.COLUMN_RECIPES_COUNT_PRODUCT, countProduct);
		cv.put(DBHelper.COLUMN_RECIPES_IS_COUNTABLE, isCountable);
		return cv;
	}

	public ContentValues getUpdatedColumns() {
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_RECIPES_NAME, name);
		cv.put(DBHelper.COLUMN_RECIPES_CAKE_WEIGHT, cakeWeight);
		cv.put(DBHelper.COLUMN_RECIPES_UPDATE_DATE, updateDate);
		cv.put(DBHelper.COLUMN_RECIPES_COUNT_PRODUCT, countProduct);
		cv.put(DBHelper.COLUMN_RECIPES_IS_COUNTABLE, isCountable);
		return cv;	
	}

	public Map<String, Integer> getColumnIndexes(Cursor c){
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put(DBHelper.COLUMN_ID, c.getColumnIndex(DBHelper.COLUMN_ID));
		result.put(DBHelper.COLUMN_RECIPES_NAME, c.getColumnIndex(DBHelper.COLUMN_RECIPES_NAME));
		result.put(DBHelper.COLUMN_RECIPES_CREATE_DATE, c.getColumnIndex(DBHelper.COLUMN_RECIPES_CREATE_DATE));
		result.put(DBHelper.COLUMN_RECIPES_UPDATE_DATE, c.getColumnIndex(DBHelper.COLUMN_RECIPES_UPDATE_DATE));
		result.put(DBHelper.COLUMN_RECIPES_CAKE_WEIGHT, c.getColumnIndex(DBHelper.COLUMN_RECIPES_CAKE_WEIGHT));
		result.put(DBHelper.COLUMN_RECIPES_COUNT_PRODUCT, c.getColumnIndex(DBHelper.COLUMN_RECIPES_COUNT_PRODUCT));
		result.put(DBHelper.COLUMN_RECIPES_IS_COUNTABLE, c.getColumnIndex(DBHelper.COLUMN_RECIPES_IS_COUNTABLE));
		return result;
	}

	/* остальное ниже */

	/**
	 * Если заполнена штучность, то на вес не обращаем внимания
	 * */
	public boolean isCountable(){
		return isCountable;
	}

	@Override
	public String toString(){
		return String.format("Recipe: id:%s; name:%s; createDate:%s; updateDate:%s; cakeWeight:%s; countProduct:%s; isCountable:%s",
				id, name, DateExtension.getDateTime(createDate), DateExtension.getDateTime(updateDate), cakeWeight, countProduct, isCountable);
	}
}

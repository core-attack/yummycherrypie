package yummycherrypie.base_classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import yummycherrypie.pl.DateExtension;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.IDataBaseUsable;
//НУЖНА ЗАЩИТА ОТ ПОВТОРЯЮЩИХСЯ ИМЕН 
/***
 * Ингредиент
 */
public class Component  implements IDataBaseUsable {

	/* закрытие поля ниже */

	public static final int DEFAULT_COMPONENT_ID = -1;

	private long id;
	private String name;
	private double price;
	private double weight;
	private boolean isCountable;//ингридиент штучный или нет
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isCountable() {
		return isCountable;
	}

	public void setIsCountable(boolean isCountable) {
		this.isCountable = isCountable;
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

	public Component(){}

	public Component(long id, String name, double price,
			double weight, boolean isCountable){
		this.id = id;
		this.name = name;
		this.price = price;
		this.weight = weight;
		this.isCountable = isCountable;
	}

	public Component(String name, double price,
					 double weight, boolean isCountable){
		this.name = name;
		this.price = price;
		this.weight = weight;
		this.isCountable = isCountable;
	}
	
	public ContentValues getInsertedColumns() {
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_COMPONENTS_NAME, name);
		cv.put(DBHelper.COLUMN_COMPONENTS_PRICE, price);
		cv.put(DBHelper.COLUMN_COMPONENTS_WEIGHT, weight);
		cv.put(DBHelper.COLUMN_COMPONENTS_IS_COUNTABLE, isCountable);
		cv.put(DBHelper.COLUMN_COMPONENTS_CREATE_DATE, createDate);
		return cv;
	}

	public ContentValues getInsertedColumnsWithId() {
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_ID, id);
		cv.put(DBHelper.COLUMN_COMPONENTS_NAME, name);
		cv.put(DBHelper.COLUMN_COMPONENTS_PRICE, price);
		cv.put(DBHelper.COLUMN_COMPONENTS_WEIGHT, weight);
		cv.put(DBHelper.COLUMN_COMPONENTS_IS_COUNTABLE, isCountable);
		cv.put(DBHelper.COLUMN_COMPONENTS_CREATE_DATE, createDate);
		return cv;
	}

	public ContentValues getUpdatedColumns(){
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_COMPONENTS_NAME, name);
		cv.put(DBHelper.COLUMN_COMPONENTS_PRICE, price);
		cv.put(DBHelper.COLUMN_COMPONENTS_WEIGHT, weight);
		cv.put(DBHelper.COLUMN_COMPONENTS_IS_COUNTABLE, isCountable);
		cv.put(DBHelper.COLUMN_COMPONENTS_UPDATE_DATE, updateDate);
		return cv;
	}

	public Map<String, Integer> getColumnIndexes(Cursor c){
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put(DBHelper.COLUMN_ID, c.getColumnIndex(DBHelper.COLUMN_ID));
		result.put(DBHelper.COLUMN_COMPONENTS_NAME, c.getColumnIndex(DBHelper.COLUMN_COMPONENTS_NAME));
		result.put(DBHelper.COLUMN_COMPONENTS_PRICE, c.getColumnIndex(DBHelper.COLUMN_COMPONENTS_PRICE));
		result.put(DBHelper.COLUMN_COMPONENTS_WEIGHT, c.getColumnIndex(DBHelper.COLUMN_COMPONENTS_WEIGHT));
		result.put(DBHelper.COLUMN_COMPONENTS_IS_COUNTABLE, c.getColumnIndex(DBHelper.COLUMN_COMPONENTS_IS_COUNTABLE));
		result.put(DBHelper.COLUMN_COMPONENTS_CREATE_DATE, c.getColumnIndex(DBHelper.COLUMN_COMPONENTS_CREATE_DATE));
		result.put(DBHelper.COLUMN_COMPONENTS_UPDATE_DATE, c.getColumnIndex(DBHelper.COLUMN_COMPONENTS_UPDATE_DATE));
		return result;
	}

	public Component(Cursor c, Map<String, Integer> map) {
		this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
		this.name = c.getString(map.get(DBHelper.COLUMN_COMPONENTS_NAME));
		this.weight = c.getDouble(map.get(DBHelper.COLUMN_COMPONENTS_WEIGHT));
		this.price = c.getDouble(map.get(DBHelper.COLUMN_COMPONENTS_PRICE));
		this.isCountable = c.getInt(map.get(DBHelper.COLUMN_COMPONENTS_IS_COUNTABLE)) == 1;
		this.createDate = c.getLong(map.get(DBHelper.COLUMN_COMPONENTS_CREATE_DATE));
		this.updateDate = c.getLong(map.get(DBHelper.COLUMN_COMPONENTS_UPDATE_DATE));
	}

	public Component(Cursor c) {
		Map<String, Integer> map = getColumnIndexes(c);
		this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
		this.name = c.getString(map.get(DBHelper.COLUMN_COMPONENTS_NAME));
		this.weight = c.getDouble(map.get(DBHelper.COLUMN_COMPONENTS_WEIGHT));
		this.price = c.getDouble(map.get(DBHelper.COLUMN_COMPONENTS_PRICE));
		this.isCountable = c.getInt(map.get(DBHelper.COLUMN_COMPONENTS_IS_COUNTABLE)) == 1;
		this.createDate = c.getLong(map.get(DBHelper.COLUMN_COMPONENTS_CREATE_DATE));
		this.updateDate = c.getLong(map.get(DBHelper.COLUMN_COMPONENTS_UPDATE_DATE));
	}

	/* остальное ниже */

	@Override
	public String toString(){
		return String.format("Component: id:%s; name:%s; weight:%s; price:%s; isCountable:%s; " +
						"createDate: %s; updateDate: %s.",
						id, name, weight, price, isCountable,
				DateExtension.getDateTime(createDate), DateExtension.getDateTime(updateDate));
	}
}

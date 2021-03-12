package yummycherrypie.base_classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.IDataBaseUsable;

/***
 * Строки рецепта (в т.ч. для доп. ингридиентов для заказа)
 */
public class RecipeLine implements IDataBaseUsable {

	public static final int DEFAULT_RECIPE_LINE_ID = -1;

	private long id;
	private double weight;
	private int count;/* количество штук */
	private long componentId;
	private long recipeId;
	private long bookingId = Booking.DEFAULT_BOOKING_ID;
	private int isDefault;/* был в рецепте (0) или пользователь ввел его вручную (1) */

	/* открытие поля ниже */

	/* взят из рецепта */
	public static int DEFAULT = 0;
	/* добавлен пользователем при создании заказа */
	public static int ADD_BY_USER = 1;

	public enum State {
		DEFAULT,
		ADD_BY_USER
	}

	/* геттеры-сеттеры ниже */

	public boolean isDefault(){ return isDefault == 0; }

	public State getState(){
		if (isDefault())
			return State.DEFAULT;
		else
			return State.ADD_BY_USER;
	}

	public void setIsDefault(boolean isDefault){
		if (isDefault)
			this.isDefault = 0;
		else
			this.isDefault = 1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getComponentId() {
		return componentId;
	}

	public void setComponentId(long componentId) {
		this.componentId = componentId;
	}

	public long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(long recipeId) {
		this.recipeId = recipeId;
	}

	public long getBookingId() {
		return bookingId;
	}

	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	/* контструкторы ниже */

	public RecipeLine(){}

	public RecipeLine(long id, double weight, int count,
					  long componentId, long recipeId, long bookingId, State state){
		this.id = id;
		this.weight = weight;
		this.count = count;
		this.componentId = componentId;
		this.recipeId = recipeId;
		this.bookingId = bookingId;
		setIsDefault(State.DEFAULT == state);
	}

	public RecipeLine(double weight, int count,
					  long componentId, long recipeId, long bookingId, State state){
		this.weight = weight;
		this.count = count;
		this.componentId = componentId;
		this.recipeId = recipeId;
		this.bookingId = bookingId;
		setIsDefault(State.DEFAULT == state);
	}

	public RecipeLine(Cursor c, Map<String, Integer> map ) {
		this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
		this.weight = c.getDouble(map.get(DBHelper.COLUMN_RECIPE_LINES_WEIGHT));
		this.count = c.getInt(map.get(DBHelper.COLUMN_RECIPE_LINES_COUNT));
		this.componentId = c.getLong(map.get(DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID));
		this.recipeId = c.getLong(map.get(DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID));
		this.bookingId = c.getLong(map.get(DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID));
		this.isDefault = c.getInt(map.get(DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT));
	}

	public RecipeLine(Cursor c) {
		Map<String, Integer> map = getColumnIndexes(c);
		this.id = c.getLong(map.get(DBHelper.COLUMN_ID));
		this.weight = c.getDouble(map.get(DBHelper.COLUMN_RECIPE_LINES_WEIGHT));
		this.count = c.getInt(map.get(DBHelper.COLUMN_RECIPE_LINES_COUNT));
		this.componentId = c.getLong(map.get(DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID));
		this.recipeId = c.getLong(map.get(DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID));
		this.bookingId = c.getLong(map.get(DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID));
		this.isDefault = c.getInt(map.get(DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT));
	}
	
	public RecipeLine(double weight, int count,
			long componentId, long recipeId){
		this.weight = weight;
		this.count = count;
		this.componentId = componentId;
		this.recipeId = recipeId;
	}
	
	public ContentValues getInsertedColumns(){
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_RECIPE_LINES_WEIGHT, weight);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_COUNT, count);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID, componentId);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID, recipeId);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID, bookingId);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT, isDefault);
		return cv;	
	}

	public ContentValues getInsertedColumnsWithId(){
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_ID, id);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_WEIGHT, weight);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_COUNT, count);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID, componentId);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID, recipeId);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID, bookingId);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT, isDefault);
		return cv;
	}

	public ContentValues getUpdatedColumns(){
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COLUMN_RECIPE_LINES_WEIGHT, weight);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_COUNT, count);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID, componentId);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID, recipeId);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID, bookingId);
		cv.put(DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT, isDefault);
		return cv;
	}

	public Map<String, Integer> getColumnIndexes(Cursor c){
		Map<String, Integer> result = new HashMap<String, Integer>();
		result.put(DBHelper.COLUMN_ID, c.getColumnIndex(DBHelper.COLUMN_ID));
		result.put(DBHelper.COLUMN_RECIPE_LINES_WEIGHT, c.getColumnIndex(DBHelper.COLUMN_RECIPE_LINES_WEIGHT));
		result.put(DBHelper.COLUMN_RECIPE_LINES_COUNT, c.getColumnIndex(DBHelper.COLUMN_RECIPE_LINES_COUNT));
		result.put(DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID, c.getColumnIndex(DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID));
		result.put(DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID, c.getColumnIndex(DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID));
		result.put(DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID, c.getColumnIndex(DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID));
		result.put(DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT, c.getColumnIndex(DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT));
		return result;
	}

	/* остальное ниже */

	@Override
	public String toString(){
		return String.format("RecipeLine: id:%s; weight:%s; count:%s; componentId:%s; recipeId:%s; bookingId:%s; isDefault:%s.",
				id, weight, count, componentId, recipeId, bookingId, isDefault);
	}
}

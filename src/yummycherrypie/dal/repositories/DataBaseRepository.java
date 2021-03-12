package yummycherrypie.dal.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.dal.DBHelper;

public class DataBaseRepository {

	public DBHelper dbHelper;
	public SQLiteDatabase db;

	/**
	 * закрыть подключение
	 * */
	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	public DataBaseRepository(){}

	public DataBaseRepository(DBHelper dbHelper) {
		this.dbHelper = dbHelper;
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * показать структуру таблиц в лог
	 */
	public void GetColumnsByAllTable(){
		Cursor c = db.rawQuery("select * from sqlite_master", null);
		try{
			LogExtension.Debug(c.toString());
		}
		finally {
			if (c != null)
				c.close();
		}
	}

	/**
	 * Показать в лог имена таблицы
	 */
	public void getColumnNames(String tableName){
		LogExtension.Debug("Get column names for table " + tableName);
		Cursor c = db.rawQuery("select * from " + tableName, null);
		try{
			for(String s : c.getColumnNames()){
				LogExtension.Debug(s);
			}
		}
		finally {
			if (c != null)
				c.close();
		}
	}

	/**
	 * Показать в лог все значения таблицы
	 */
	public void getAllValues(String tableName){
		LogExtension.Debug("Get column names for table " + tableName);
		Cursor c = db.rawQuery("select * from " + tableName, null);
		try{
			if (c.moveToFirst()){
				do{
					int count = c.getColumnCount();
					LogExtension.Debug("COLUMN - VALUE");
					for (int i = 0; i < count; i++) {
						LogExtension.Debug(c.getColumnName(i) + " - " + c.getString(i));
					}
				}while(c.moveToNext());
			}
		}
		finally {
			if (c != null)
				c.close();
		}
	}

	/**
	 * возвращает автоинкремент в значение по умолчанию
	 */
	public void setZeroToAutoincrement(String tableName){
		LogExtension.Debug("set 0 to autoincrement field for table " + tableName + "... ");
		db.execSQL("update sqlite_sequence set seq=0 where name='" + tableName + "'");
		LogExtension.Debug("success");
	}

	/**
	 * вставляет запись в БД
	 * @return ИД
	 */
	public long insert(String tableName, ContentValues cv) {
		return db.insert(tableName, null, cv);
	}

	/**
	 * обновляет запись в БД
	 */
	public void update(String tableName, ContentValues cv, long id) {
		int u_id = db.update(tableName, cv, DBHelper.COLUMN_ID + " = ?",
				new String[]{Long.toString(id)});
	}

	/**
	 * удаляет запись
	 */
	public void delete(String tableName, long id) {
		int d_id = db.delete(tableName, DBHelper.COLUMN_ID + " = ?",
				new String[]{Long.toString(id)});
	}

	/**
	 * Удаляет все записи таблицы
	 */
	public void deleteAll(String tableName) {
		int d_id = db.delete(tableName, null,
				null);
		LogExtension.Debug("DELETE ALL records: from table '" + tableName + "', count = "
				+ d_id);
		setZeroToAutoincrement(tableName);
	}

	/**
	 * для работы подгрузки курсора в списках всех тортов, ингридиентов и др.
	 */
	public Cursor getAllRecords(String tableName) {
		return db.query(tableName, null, null, null, null, null, null);
	}

	/**
	 * для работы подгрузки курсора в списках всех тортов, ингридиентов и др.
	 */
	public Cursor getAllRecords(String tableName, String orderBy) {
		return db.query(tableName, null, null, null, null, null, orderBy);
	}
}

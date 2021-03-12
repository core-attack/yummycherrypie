package yummycherrypie.dal;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Map;

/***
 * Классы, экземпляры которых хранятся в БД, должны реализовывать этот интерфейс
 */
public interface IDataBaseUsable {

	/***
	 * Возвращает колонки для вставки без id
	 */
	ContentValues getInsertedColumns();

	/***
	 * Возвращает колонки для обновления
	 */
	ContentValues getUpdatedColumns();

	/***
	 * Возвращает колонки для вставки с id
	 */
	ContentValues getInsertedColumnsWithId();

	/**
	 * Возвращает индексы колонок (полей класса)
	 * */
	Map<String, Integer> getColumnIndexes(Cursor c);

}

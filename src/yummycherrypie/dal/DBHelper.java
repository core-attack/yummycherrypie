package yummycherrypie.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.business_logic.FileHelper;

public class DBHelper extends SQLiteOpenHelper {

	/* закрытие поля */

	private static final int DB_VERSION = 28; // версия БД
	private static final String DB_NAME = "yummycherrypie_data_base";

	private static String DB_DIR = "/data/data/android.example/databases/";
	private static String DB_PATH = DB_DIR + DB_NAME;
	private static String OLD_DB_PATH = DB_DIR + "old_" + DB_NAME;

	Context context;

	/* именование таблиц */

	/**
	 * ингредиенты
	 */
	public static final String TABLE_COMPONENTS = "components";

	/**
	 * рецепты
	 */
	public static final String TABLE_RECIPES = "recipes";

	/**
	 * строки рецепта
	 */
	public static final String TABLE_RECIPE_LINES = "recipe_lines";

	/**
	 * заказы
	 */
	public static final String TABLE_BOOKINGS = "bookings";

	/**
	 * заказчики
	 */
	public static final String TABLE_BOOKING_MEN = "booking_men";

	/**
	 * события
	 */
	public static final String TABLE_EVENTS = "events";

	/**
	 * типы заказа
	 */
	public static final String TABLE_BOOKING_TYPES = "booking_types";

	/* именование колонок таблиц */

	public static final String COLUMN_ID = "_id";

	/* таблица ингредиенты */

	public static final String COLUMN_COMPONENTS_NAME = "name";
	public static final String COLUMN_COMPONENTS_PRICE = "price";
	public static final String COLUMN_COMPONENTS_WEIGHT = "weight";
	public static final String COLUMN_COMPONENTS_IS_COUNTABLE = "isCountable";
	public static final String COLUMN_COMPONENTS_CREATE_DATE = "createDate";
	public static final String COLUMN_COMPONENTS_UPDATE_DATE = "updateDate";

	/* таблица рецепты */

	public static final String COLUMN_RECIPES_NAME = "name";
	public static final String COLUMN_RECIPES_CREATE_DATE = "createDate";
	public static final String COLUMN_RECIPES_UPDATE_DATE = "updateDate";
	public static final String COLUMN_RECIPES_CAKE_WEIGHT = "cakeWeight";//размер порции
	public static final String COLUMN_RECIPES_CAKE_ID = "cakeId";
	public static final String COLUMN_RECIPES_COUNT_PRODUCT = "countProduct";//количество штук на порцию
	public static final String COLUMN_RECIPES_IS_COUNTABLE = "isCountable";

	/* таблица строки рецета*/

	public static final String COLUMN_RECIPE_LINES_WEIGHT = "weight";
	public static final String COLUMN_RECIPE_LINES_COUNT = "count";
	public static final String COLUMN_RECIPE_LINES_IS_DEFAULT = "isDefault";//была ли в рецепте или пользователь добавил её сам
	public static final String COLUMN_RECIPE_LINES_COMPONENT_ID = "componentId";
	public static final String COLUMN_RECIPE_LINES_BOOKING_ID = "bookingId";
	public static final String COLUMN_RECIPE_LINES_RECIPE_ID = "recipeId";

	/* таблица заказы */

	public static final String COLUMN_BOOKINGS_DATE = "date";
	public static final String COLUMN_BOOKINGS_WEIGHT = "weight";
	public static final String COLUMN_BOOKINGS_RECIPE_PRICE = "recipePrice";//себестоимость
	public static final String COLUMN_BOOKINGS_DISCOUNT = "discount";
	public static final String COLUMN_BOOKINGS_CAKE_PRICE = "cakePrice";
	public static final String COLUMN_BOOKINGS_RECIPE_ID = "recipeId";
	public static final String COLUMN_BOOKINGS_BOOKING_MAN_ID = "bookingManId";
	public static final String COLUMN_BOOKINGS_BOOKING_TYPE_ID = "bookingTypeId";
	public static final String COLUMN_BOOKINGS_EVENT_ID = "eventId";
	public static final String COLUMN_BOOKINGS_COMMENT = "comment";
	public static final String COLUMN_BOOKINGS_CREATE_DATE = "createDate";
	public static final String COLUMN_BOOKINGS_UPDATE_DATE = "updateDate";
	public static final String COLUMN_BOOKINGS_COUNT_PRODUCT = "countProduct";
	public static final String COLUMN_BOOKINGS_TIME_SPENT = "timeSpent";


	/* таблица заказчики*/

	public static final String COLUMN_BOOKING_MEN_NAME = "name";
	public static final String COLUMN_BOOKING_MEN_PHONE = "phone";
	public static final String COLUMN_BOOKING_MEN_VK_PROFILE = "vkProfile";
	public static final String COLUMN_BOOKING_MEN_CREATE_DATE = "createDate";
	public static final String COLUMN_BOOKING_MEN_UPDATE_DATE = "updateDate";

	/* таблица события*/

	public static final String COLUMN_EVENTS_DATE = "date";
	public static final String COLUMN_EVENTS_NAME = "name";
	public static final String COLUMN_EVENTS_PLACE = "place";
	public static final String COLUMN_EVENTS_BOOKING_MAN_ID = "bookingManId";
	public static final String COLUMN_EVENTS_PRICE = "price";
	public static final String COLUMN_EVENTS_CREATE_DATE = "createDate";
	public static final String COLUMN_EVENTS_UPDATE_DATE = "updateDate";

	/* таблица типы заказа*/

	public static final String COLUMN_BOOKING_TYPES_NAME = "name";
	public static final String COLUMN_BOOKING_TYPES_CREATE_DATE = "createDate";
	public static final String COLUMN_BOOKING_TYPES_UPDATE_DATE = "updateDate";
	public static final String COLUMN_BOOKING_TYPES_IS_COUNTABLE = "isCountable";

	public DBHelper(Context context) {

		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
		DB_PATH = context.getDatabasePath(DB_NAME).getAbsolutePath();
	}

	/**
	 * Действия при создании БД
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		LogExtension.Warning("--- Creating databases: ---");

		LogExtension.Warning("--- Create table " + TABLE_COMPONENTS + "... ---");
		db.execSQL("create table "
				+ TABLE_COMPONENTS + "("
				+ COLUMN_ID + " integer primary key autoincrement,"
				+ COLUMN_COMPONENTS_NAME + " text,"
				+ COLUMN_COMPONENTS_PRICE + " real,"// цена товара
				+ COLUMN_COMPONENTS_WEIGHT + " real,"// вес товара
				+ COLUMN_COMPONENTS_IS_COUNTABLE + " integer,"// 1- штучный (яйца), 0-весовой
				+ COLUMN_COMPONENTS_CREATE_DATE + " integer,"
				+ COLUMN_COMPONENTS_UPDATE_DATE + " integer"
				+ ");");
		LogExtension.Warning("--- Succes ---");

		LogExtension.Warning("--- Create table " + TABLE_RECIPES + "... ---");
		db.execSQL("create table "
				+ TABLE_RECIPES + " ("
				+ COLUMN_ID + " integer primary key autoincrement,"
				+ COLUMN_RECIPES_NAME + " text,"
				+ COLUMN_RECIPES_CREATE_DATE + " integer,"
				+ COLUMN_RECIPES_UPDATE_DATE + " integer,"
				+ COLUMN_RECIPES_CAKE_WEIGHT + " real,"
				+ COLUMN_RECIPES_CAKE_ID + " integer,"
				+ COLUMN_RECIPES_COUNT_PRODUCT + " integer,"
				+ COLUMN_RECIPES_IS_COUNTABLE + " integer"
				+ ");");
		LogExtension.Warning("--- Succes ---");

		LogExtension.Warning("--- Create table " + TABLE_BOOKINGS + "... ---");
		db.execSQL("create table "
				+ TABLE_BOOKINGS + " ("
				+ COLUMN_ID + " integer primary key autoincrement,"
				+ COLUMN_BOOKINGS_DATE + " integer,"
				+ COLUMN_BOOKINGS_WEIGHT + " real,"
				+ COLUMN_BOOKINGS_RECIPE_PRICE + " real,"// себестоимость
				+ COLUMN_BOOKINGS_DISCOUNT + " real,"// скидка
				+ COLUMN_BOOKINGS_CAKE_PRICE + " real,"// конечная стоимость
				+ COLUMN_BOOKINGS_COMMENT + " text,"
				+ COLUMN_BOOKINGS_RECIPE_ID + " integer,"
				+ COLUMN_BOOKINGS_BOOKING_MAN_ID + " integer,"
				+ COLUMN_BOOKINGS_BOOKING_TYPE_ID + " integer,"
				+ COLUMN_BOOKINGS_EVENT_ID + " integer,"
				+ COLUMN_BOOKINGS_CREATE_DATE + " integer,"
				+ COLUMN_BOOKINGS_UPDATE_DATE + " integer,"
				+ COLUMN_BOOKINGS_COUNT_PRODUCT + " integer,"
				+ COLUMN_BOOKINGS_TIME_SPENT + " integer"
				+ ");");
		LogExtension.Warning("--- Succes ---");

		LogExtension.Warning("--- Create table " + TABLE_RECIPE_LINES + "... ---");
		db.execSQL("create table "
				+ TABLE_RECIPE_LINES + " ("
				+ COLUMN_ID + " integer primary key autoincrement,"
				+ COLUMN_RECIPE_LINES_WEIGHT + " real,"
				+ COLUMN_RECIPE_LINES_COUNT + " integer,"
				+ COLUMN_RECIPE_LINES_IS_DEFAULT + " integer, "
				+ COLUMN_RECIPE_LINES_COMPONENT_ID + " integer,"
				+ COLUMN_RECIPE_LINES_BOOKING_ID + " integer,"
				+ COLUMN_RECIPE_LINES_RECIPE_ID + " integer"
				+ ");");
		LogExtension.Warning("--- Succes ---");

		LogExtension.Warning("--- Create table " + TABLE_BOOKING_MEN + "... ---");
		// таблица заказчиков
		db.execSQL("create table "
				+ TABLE_BOOKING_MEN + " ("
				+ COLUMN_ID + " integer primary key autoincrement,"
				+ COLUMN_BOOKING_MEN_NAME + " text,"
				+ COLUMN_BOOKING_MEN_PHONE + " text,"
				+ COLUMN_BOOKING_MEN_VK_PROFILE + " text,"
				+ COLUMN_BOOKING_MEN_CREATE_DATE + " integer,"
				+ COLUMN_BOOKING_MEN_UPDATE_DATE + " integer"
				+ ");");
		LogExtension.Warning("--- Succes ---");

		LogExtension.Warning("--- Create table " + TABLE_EVENTS + "... ---");
		// таблица событий
		db.execSQL("create table "
				+ TABLE_EVENTS + " ("
				+ COLUMN_ID + " integer primary key autoincrement,"
				+ COLUMN_EVENTS_NAME + " text,"
				+ COLUMN_EVENTS_PLACE + " text,"
				+ COLUMN_EVENTS_PRICE + " real,"
				+ COLUMN_EVENTS_BOOKING_MAN_ID + " integer,"
				+ COLUMN_EVENTS_DATE + " integer,"
				+ COLUMN_EVENTS_CREATE_DATE + " integer,"
				+ COLUMN_EVENTS_UPDATE_DATE + " integer"
				+ ");");
		LogExtension.Warning("--- Succes ---");

		LogExtension.Warning("--- Create table " + TABLE_BOOKING_TYPES + "... ---");
		// таблица событий
		db.execSQL("create table "
				+ TABLE_BOOKING_TYPES + " ("
				+ COLUMN_ID + " integer primary key autoincrement,"
				+ COLUMN_BOOKING_TYPES_NAME + " text,"
				+ COLUMN_BOOKING_TYPES_IS_COUNTABLE + " integer,"// 1- штучный, 0-весовой
				+ COLUMN_BOOKING_TYPES_CREATE_DATE + " integer,"
				+ COLUMN_BOOKING_TYPES_UPDATE_DATE + " integer"
				+ ");");
		LogExtension.Warning("--- Succes ---");

		LogExtension.Warning("--- All tables was successfully created! ---");
	}

	/**
	 * Действия при обновлении БД
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LogExtension.Warning(" --- onUpgrade database from " + oldVersion + " to "
				+ newVersion + " version --- ");
		if (oldVersion == 27 && newVersion == 28) {
			db.beginTransaction();
			try {
				LogExtension.Warning("--- Creating databases: ---");

				db.execSQL("drop table  if exists " + TABLE_COMPONENTS + ";");
				LogExtension.Warning("--- Create table " + TABLE_COMPONENTS + "... ---");
				db.execSQL("create table "
						+ TABLE_COMPONENTS + "("
						+ COLUMN_ID + " integer primary key autoincrement,"
						+ COLUMN_COMPONENTS_NAME + " text,"
						+ COLUMN_COMPONENTS_PRICE + " real,"// цена товара
						+ COLUMN_COMPONENTS_WEIGHT + " real,"// вес товара
						+ COLUMN_COMPONENTS_IS_COUNTABLE + " integer,"// 1- штучный (яйца), 0-весовой
						+ COLUMN_COMPONENTS_CREATE_DATE + " integer,"
						+ COLUMN_COMPONENTS_UPDATE_DATE + " integer"
						+ ");");
				LogExtension.Warning("--- Succes ---");

				db.execSQL("drop table  if exists " + TABLE_RECIPES + ";");
				LogExtension.Warning("--- Create table " + TABLE_RECIPES + "... ---");
				db.execSQL("create table "
						+ TABLE_RECIPES + " ("
						+ COLUMN_ID + " integer primary key autoincrement,"
						+ COLUMN_RECIPES_NAME + " text,"
						+ COLUMN_RECIPES_CREATE_DATE + " integer,"
						+ COLUMN_RECIPES_UPDATE_DATE + " integer,"
						+ COLUMN_RECIPES_CAKE_WEIGHT + " real,"
						+ COLUMN_RECIPES_CAKE_ID + " integer,"
						+ COLUMN_RECIPES_COUNT_PRODUCT + " integer,"
						+ COLUMN_RECIPES_IS_COUNTABLE + " integer"
						+ ");");
				LogExtension.Warning("--- Succes ---");

				db.execSQL("drop table  if exists " + TABLE_BOOKINGS + ";");
				LogExtension.Warning("--- Create table " + TABLE_BOOKINGS + "... ---");
				db.execSQL("create table "
						+ TABLE_BOOKINGS + " ("
						+ COLUMN_ID + " integer primary key autoincrement,"
						+ COLUMN_BOOKINGS_DATE + " integer,"
						+ COLUMN_BOOKINGS_WEIGHT + " real,"
						+ COLUMN_BOOKINGS_RECIPE_PRICE + " real,"// себестоимость
						+ COLUMN_BOOKINGS_DISCOUNT + " real,"// скидка
						+ COLUMN_BOOKINGS_CAKE_PRICE + " real,"// конечная стоимость
						+ COLUMN_BOOKINGS_COMMENT + " text,"
						+ COLUMN_BOOKINGS_RECIPE_ID + " integer,"
						+ COLUMN_BOOKINGS_BOOKING_MAN_ID + " integer,"
						+ COLUMN_BOOKINGS_BOOKING_TYPE_ID + " integer,"
						+ COLUMN_BOOKINGS_EVENT_ID + " integer,"
						+ COLUMN_BOOKINGS_CREATE_DATE + " integer,"
						+ COLUMN_BOOKINGS_UPDATE_DATE + " integer,"
						+ COLUMN_BOOKINGS_COUNT_PRODUCT + " integer,"
						+ COLUMN_BOOKINGS_TIME_SPENT + " integer"
						+ ");");
				LogExtension.Warning("--- Succes ---");

				db.execSQL("drop table  if exists " + TABLE_RECIPE_LINES + ";");
				LogExtension.Warning("--- Create table " + TABLE_RECIPE_LINES + "... ---");
				db.execSQL("create table "
						+ TABLE_RECIPE_LINES + " ("
						+ COLUMN_ID + " integer primary key autoincrement,"
						+ COLUMN_RECIPE_LINES_WEIGHT + " real,"
						+ COLUMN_RECIPE_LINES_COUNT + " integer,"
						+ COLUMN_RECIPE_LINES_IS_DEFAULT + " integer, "
						+ COLUMN_RECIPE_LINES_COMPONENT_ID + " integer,"
						+ COLUMN_RECIPE_LINES_BOOKING_ID + " integer,"
						+ COLUMN_RECIPE_LINES_RECIPE_ID + " integer"
						+ ");");
				LogExtension.Warning("--- Succes ---");

				db.execSQL("drop table  if exists " + TABLE_BOOKING_MEN + ";");
				LogExtension.Warning("--- Create table " + TABLE_BOOKING_MEN + "... ---");
				// таблица заказчиков
				db.execSQL("create table "
						+ TABLE_BOOKING_MEN + " ("
						+ COLUMN_ID + " integer primary key autoincrement,"
						+ COLUMN_BOOKING_MEN_NAME + " text,"
						+ COLUMN_BOOKING_MEN_PHONE + " text,"
						+ COLUMN_BOOKING_MEN_VK_PROFILE + " text,"
						+ COLUMN_BOOKING_MEN_CREATE_DATE + " integer,"
						+ COLUMN_BOOKING_MEN_UPDATE_DATE + " integer"
						+ ");");
				LogExtension.Warning("--- Succes ---");

				db.execSQL("drop table if exists " + TABLE_EVENTS + ";");
				LogExtension.Warning("--- Create table " + TABLE_EVENTS + "... ---");
				// таблица событий
				db.execSQL("create table "
						+ TABLE_EVENTS + " ("
						+ COLUMN_ID + " integer primary key autoincrement,"
						+ COLUMN_EVENTS_NAME + " text,"
						+ COLUMN_EVENTS_PLACE + " text,"
						+ COLUMN_EVENTS_PRICE + " real,"
						+ COLUMN_EVENTS_BOOKING_MAN_ID + " integer,"
						+ COLUMN_EVENTS_DATE + " integer,"
						+ COLUMN_EVENTS_CREATE_DATE + " integer,"
						+ COLUMN_EVENTS_UPDATE_DATE + " integer"
						+ ");");
				LogExtension.Warning("--- Succes ---");

				db.execSQL("drop table if exists " + TABLE_BOOKING_TYPES + ";");
				LogExtension.Warning("--- Create table " + TABLE_BOOKING_TYPES + "... ---");
				// таблица событий
				db.execSQL("create table "
						+ TABLE_BOOKING_TYPES + " ("
						+ COLUMN_ID + " integer primary key autoincrement,"
						+ COLUMN_BOOKING_TYPES_NAME + " text,"
						+ COLUMN_BOOKING_TYPES_IS_COUNTABLE + " integer,"// 1- штучный, 0-весовой
						+ COLUMN_BOOKING_TYPES_CREATE_DATE + " integer,"
						+ COLUMN_BOOKING_TYPES_UPDATE_DATE + " integer"
						+ ");");
				LogExtension.Warning("--- Succes ---");

				LogExtension.Warning("--- All tables was successfully created! ---");
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();

			}
		}
	}

	/**
	 * Удаляет БД
	 */
	public static boolean dropDataBase(Context context){
		return context.deleteDatabase(DB_NAME);
	}

	/**
	 * Путь к файлу БД
	 * */
	public static File pathDataBaseFile(Context context){ return context.getDatabasePath(DB_NAME); }

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
        /*
         * Close SQLiteOpenHelper so it will commit the created empty database
         * to internal storage.
         */
		close();

        /*
         * Open the database in the assets folder as the input stream.
         */
		InputStream myInput = context.getAssets().open(DB_NAME);

        /*
         * Open the empty db in interal storage as the output stream.
         */
		OutputStream myOutput = new FileOutputStream(DB_PATH);

        /*
         * Copy over the empty db in internal storage with the database in the
         * assets folder.
         */
		FileHelper.copyFile(myInput, myOutput);

        /*
         * Access the copied database so SQLiteHelper will cache it and mark it
         * as created.
         */
		getWritableDatabase().close();
	}

	/**
	 * Copies the database file at the specified location over the current
	 * internal application database.
	 * */
	public boolean importDatabase(String dbPath) throws IOException {

		close();
		File newDb = new File(dbPath);
		File oldDb = pathDataBaseFile(context);
		if (newDb.exists()) {
			FileHelper.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
			getWritableDatabase().close();
			return true;
		}
		return false;
	}


}

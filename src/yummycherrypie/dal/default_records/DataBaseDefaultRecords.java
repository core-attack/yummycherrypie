package yummycherrypie.dal.default_records;

import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.DataBaseRepository;

/**
 * Сброс БД до умолчаний
 * Created by CoreAttack on 28.07.2015.
 */
public abstract class DataBaseDefaultRecords {

    public DataBaseRepository dbr;

    public DataBaseDefaultRecords(){}

    public DataBaseDefaultRecords(DBHelper dbHelper){ this.dbr = new DataBaseRepository(dbHelper); }

    public abstract void createDefaultRecords(boolean isDebug);

}

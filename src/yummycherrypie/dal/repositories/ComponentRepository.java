package yummycherrypie.dal.repositories;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Map;

import yummycherrypie.base_classes.Component;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class ComponentRepository extends DataBaseRepository {

    public ComponentRepository(DBHelper dbHelper){
        super(dbHelper);
    }

    /**
     * возвращает ингридиент по ИД
     */
    public Component getComponent(long componentId) {
        Cursor c = db.query(DBHelper.TABLE_COMPONENTS, null, DBHelper.COLUMN_ID + " = ?",
                new String[] { Long.toString(componentId) }, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Component comp = new Component(c);
                    return comp;
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return null;
    }


    /**
     * возвращает имя компонента
     */
    public String getComponentName(long componentId) {
        Cursor c = db.query(DBHelper.TABLE_COMPONENTS, new String[] { DBHelper.COLUMN_COMPONENTS_NAME },
                DBHelper.COLUMN_ID + " = ?", new String[] { Long.toString(componentId) }, null,
                null, DBHelper.COLUMN_COMPONENTS_NAME, "1");
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int nameColIndex = c.getColumnIndex(DBHelper.COLUMN_COMPONENTS_NAME);
                    String name = c.getString(nameColIndex);
                    return name;
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return "Новый ингредиент";
    }

    /**
     * возвращает все ингридиенты
     */
    public ArrayList<Component> getAllComponents() {
        ArrayList<Component> listItems = new ArrayList<Component>();
        Cursor c = db.query(DBHelper.TABLE_COMPONENTS, null, null, null, null, null,
                "name");
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Component().getColumnIndexes(c);
                    do {
                        Component comp = new Component(c, map);
                        listItems.add(comp);

                    } while (c.moveToNext());
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return listItems;
    }
}

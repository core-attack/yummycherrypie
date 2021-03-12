package yummycherrypie.dal.repositories;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Map;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class RecipeLineRepository extends DataBaseRepository {

    public RecipeLineRepository(DBHelper dbHelper){
        super(dbHelper);
    }

    /**
     * Возаращает строку рецепта
     */
    public RecipeLine getRecipeLine(long recipeLineId){

        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, null, DBHelper.COLUMN_ID + " = ?",
                new String[] { Long.toString(recipeLineId) },
                null, null, DBHelper.COLUMN_ID);
        try	{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new RecipeLine().getColumnIndexes(c);
                    return new RecipeLine(c, map);
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
     * шаблон рецепта
     */
    public ArrayList<RecipeLine> getRecipeLines(long recipeId){
        ArrayList<RecipeLine> listItems = new ArrayList<RecipeLine>();
        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, null,
                DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID + " = ? and "
                        + DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID + " = ?"  ,
                new String[] { Long.toString(recipeId), Long.toString(Booking.DEFAULT_BOOKING_ID), },
                null, null, DBHelper.COLUMN_ID);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new RecipeLine().getColumnIndexes(c);
                    do {
                        listItems.add(new RecipeLine(c, map));
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

    /**
     * шаблон рецепта
     */
    public Cursor getRecipeLinesCursor(long recipeId){
        return db.query(DBHelper.TABLE_RECIPE_LINES, null,
                DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID + " = ? and "
                        + DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID + " = ?"  ,
                new String[] { Long.toString(recipeId), Long.toString(Booking.DEFAULT_BOOKING_ID), },
                null, null, DBHelper.COLUMN_ID);
    }

    /**
     * возвращает значение веса ингредиента из рецепта по умолчанию
     */
    public int getRecipeLinesCountFromTemplateRecipe(long recipeId, long componentId){
        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, null,
                DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID + " = ? and "
                        + DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID + " = ?",
                new String[]{Long.toString(recipeId), Long.toString(componentId)},
                null, null, DBHelper.COLUMN_ID);
        try {
            ArrayList<RecipeLine> listItems = new ArrayList<RecipeLine>();
            if (c != null) {
                if (c.moveToFirst()) {
                    int countColIndex = c.getColumnIndex(DBHelper.COLUMN_RECIPE_LINES_COUNT);
                    return c.getInt(countColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return -1;
    }

    /**
     * возвращает значение веса ингредиента из рецепта по умолчанию
     */
    public double getRecipeLinesWeightFromTemplateRecipe(long recipeId, long componentId){
        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, null,
                DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID + " = ? and "
                        + DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID + " = ?",
                new String[]{Long.toString(recipeId), Long.toString(componentId)},
                null, null, DBHelper.COLUMN_ID);
        try {
            ArrayList<RecipeLine> listItems = new ArrayList<RecipeLine>();
            if (c != null) {
                if (c.moveToFirst()) {
                    int weightColIndex = c.getColumnIndex(DBHelper.COLUMN_RECIPE_LINES_WEIGHT);
                    return c.getDouble(weightColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return -1;
    }

    /**
     * возвращает все строки определенного рецепта
     */
    public Cursor getAllRecipeLines(long recipeId) {
        String table = String.format("%s as comp inner join %s as rl on rl." + DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID + " = comp." + DBHelper.COLUMN_ID, DBHelper.TABLE_COMPONENTS, DBHelper.TABLE_RECIPE_LINES);
        String columns[] = { "comp." + DBHelper.COLUMN_COMPONENTS_NAME + " as name", "rl." + DBHelper.COLUMN_COMPONENTS_WEIGHT + " as weight "};
        String selection = "rl." + DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID+ " = ?";
        String[] selectionArgs = { Long.toString(recipeId) };
        return db.query(table, columns, selection, selectionArgs, null, null, null);
    }


    /**
     * проверяем наличие строки рецепта в таблице строк рецепта
     */
    public boolean recipeLinesExists(RecipeLine rl){
        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, null,
                DBHelper.COLUMN_ID + " = ?", new String[] { Long.toString(rl.getId()) }, null,
                null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    return true;
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return false;
    }

    /**
     * возвращает ингридиенты к заказу по заказу
     */
    public ArrayList<RecipeLine> getAllRecipeLinesArrayListByBooking(long bookingId) {
        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, null,
                String.format("%s = ?",	DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID),
                new String[] { Long.toString(bookingId) }, null,
                null, null);
        ArrayList<RecipeLine> list = new ArrayList<RecipeLine>();
        try{

            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new RecipeLine().getColumnIndexes(c);
                    do {
                        list.add(new RecipeLine(c, map));
                    } while (c.moveToNext());
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return list;
    }


    /**
     * удаление всех строк рецепта, привязанных к заказу
     * */
    public void deleteAllRecipeLinesByBooking(long bookingId) {
        Cursor c = getAllRecipeLinesByBooking(bookingId);
        try{

            if (c != null) {
                if (c.moveToFirst()) {
                    int idColIndex = c.getColumnIndex(DBHelper.COLUMN_ID);
                    do {
                        delete(dbHelper.TABLE_RECIPE_LINES, c.getLong(idColIndex));
                    } while (c.moveToNext());
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
    }


    /**
     * дополнительные ингридиенты к заказу
     */
    public Cursor getAllRecipeLinesByBooking(long bookingId){
        ArrayList<RecipeLine> listItems = new ArrayList<RecipeLine>();
        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, null,
                String.format("%s = ?",	DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID),
                new String[] { Long.toString(bookingId) }, null,
                null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                Map<String, Integer> map = new RecipeLine().getColumnIndexes(c);
                do {
                    listItems.add(new RecipeLine(c, map));
                } while (c.moveToNext());
            }
        }
        return c;
    }

    /**
     * возвращает все строки рецепта у заказа
     */
    public ArrayList<RecipeLine> getAllRecipeLinesForBooking(long bookingId) {
        ArrayList<RecipeLine> listItems = new ArrayList<RecipeLine>();
        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, new String[]{
                        DBHelper.COLUMN_ID,
                        DBHelper.COLUMN_RECIPE_LINES_WEIGHT,
                        DBHelper.COLUMN_RECIPE_LINES_COUNT,
                        DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID,
                        DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID,
                        DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT,
                        DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID},
                DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID + " = "
                        + bookingId, null, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new RecipeLine().getColumnIndexes(c);
                    do {
                        listItems.add(new RecipeLine(c, map));
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

    /**
     * возвращает все строки рецепта
     */
    public ArrayList<RecipeLine> getAllRecipeLinesForRecipe(long recipeId) {
        ArrayList<RecipeLine> listItems = new ArrayList<RecipeLine>();
        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, new String[]{
                        DBHelper.COLUMN_ID,
                        DBHelper.COLUMN_RECIPE_LINES_WEIGHT,
                        DBHelper.COLUMN_RECIPE_LINES_COUNT,
                        DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID,
                        DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID,
                        DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT,
                        DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID},
                DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID + " = ? and "
                        + DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID + " = ? ",//проверить, как работает выборка строк рецепта в добавлении или редактировании рецепта!!
                new String[] { Long.toString(recipeId), Long.toString(-1) }, null, null, "recipeId");
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new RecipeLine().getColumnIndexes(c);
                    do {
                        listItems.add(new RecipeLine(c, map));
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

    /**
     * возвращает все строки рецепта
     */
    public Cursor getAllRecipeLinesForRecipeCursor(long recipeId) {
        Cursor c = db.query(DBHelper.TABLE_RECIPE_LINES, new String[]{
                        DBHelper.COLUMN_ID,
                        DBHelper.COLUMN_RECIPE_LINES_WEIGHT,
                        DBHelper.COLUMN_RECIPE_LINES_COUNT,
                        DBHelper.COLUMN_RECIPE_LINES_COMPONENT_ID,
                        DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID,
                        DBHelper.COLUMN_RECIPE_LINES_IS_DEFAULT,
                        DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID},
                DBHelper.COLUMN_RECIPE_LINES_RECIPE_ID + " = ? and "
                        + DBHelper.COLUMN_RECIPE_LINES_BOOKING_ID + " = ? ",//проверить, как работает выборка строк рецепта в добавлении или редактировании рецепта!!
                new String[] { Long.toString(recipeId), Long.toString(-1) }, null, null, "recipeId");
        return c;
    }
}

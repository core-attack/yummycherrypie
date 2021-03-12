package yummycherrypie.dal.repositories;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Map;

import yummycherrypie.base_classes.Recipe;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class RecipeRepository extends DataBaseRepository {

    RecipeLineRepository rlr;

    public RecipeRepository(DBHelper dbHelper){
        super(dbHelper);
        rlr = new RecipeLineRepository(dbHelper);
    }

    /**
     * возвращает рецепт по ИД
     */
    public Recipe getRecipe(long recipeId) {
        Cursor c = db.query(DBHelper.TABLE_RECIPES, null, DBHelper.COLUMN_ID + " = ?",
                new String[] { Long.toString(recipeId) }, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Recipe().getColumnIndexes(c);
                    return new Recipe(c, map);
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
     * возвращает имя рецепта
     */
    public String getRecipeName(long id) {
        Cursor c = db.query(DBHelper.TABLE_RECIPES, new String[] { DBHelper.COLUMN_RECIPES_NAME }, DBHelper.COLUMN_ID + " = ?",
                new String[] { Long.toString(id) }, null, null, DBHelper.COLUMN_RECIPES_NAME);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int nameColIndex = c.getColumnIndex(DBHelper.COLUMN_RECIPES_NAME);
                    return c.getString(nameColIndex);
                }
            }
        }
        finally {
            if (c != null)
                c.close();
        }
        return "Рецепт не выбран";
    }


    /**
     * возвращает последний записанный рецепт (не сохраненный!)
     */
    public long getLastRecipeId() {
        Cursor c = db.query(DBHelper.TABLE_RECIPES, new String[] { "max(" + DBHelper.COLUMN_ID + ")" }, null,
                null, null, null, null);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    int idColIndex = c.getColumnIndex(DBHelper.COLUMN_ID);
                    return c.getLong(idColIndex);
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
     * возвращает все рецепты
     */
    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> listItems = new ArrayList<Recipe>();
        Cursor c = db.query(DBHelper.TABLE_RECIPES, null, null, null, null, null, DBHelper.COLUMN_RECIPES_NAME);
        try{
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Integer> map = new Recipe().getColumnIndexes(c);
                    do {
                        listItems.add(new Recipe(c, map));
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

    @Override
    public void delete(String tableName, long id) {
        ArrayList<RecipeLine> list = rlr.getAllRecipeLinesForRecipe(id);
        for (RecipeLine r : list) {
            delete(dbHelper.TABLE_RECIPE_LINES, r.getId());
        }
        super.delete(tableName, id);
    }
}

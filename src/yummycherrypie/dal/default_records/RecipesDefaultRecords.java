package yummycherrypie.dal.default_records;

import java.util.ArrayList;
import java.util.Date;

import yummycherrypie.base_classes.Recipe;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.dal.DBHelper;

/**
 * Created by Nikolay_Piskarev on 12/1/2015.
 */
public class RecipesDefaultRecords extends DataBaseDefaultRecords{

    public RecipesDefaultRecords(DBHelper dbh){
        super(dbh);
    }

    /**
     * создание рецептов по умолчанию
     */
    public void createDefaultRecords(boolean isDebug){
        dbr.deleteAll(DBHelper.TABLE_RECIPES);
        dbr.deleteAll(DBHelper.TABLE_RECIPE_LINES);

        String[] recipeNames = new String[] {
                "Шоколадный бисквит",
                "Сметанник",
                "Шифоновый бисквит с желтками",
                "Красный вельвет",
                "Цветной бисквит"
        };


        long id = 1;
        for (String recipeName : recipeNames){
            Recipe recipe = new Recipe(id, recipeName, 2000/*вес торта*/, 0, false);
            recipe.setCreateDate(new Date().getTime());
            long recipeId = dbr.insert(DBHelper.TABLE_RECIPES, recipe.getInsertedColumns());

            LogExtension.Debug("Record created: " + recipe.toString());

            switch ((int)recipeId)
            {
                case 1 /*"Шоколадный бисквит"*/:{
                    RecipeLine rl1 = new RecipeLine(0/*вес*/, 2/*количество*/, 1/*ингридиент ид*/, recipeId);//яйцо 2
                    RecipeLine rl2 = new RecipeLine(75, 0, 11, recipeId);//масло подсолнечное 75
                    RecipeLine rl3 = new RecipeLine(150, 0, 13, recipeId);//молоко 150
                    RecipeLine rl4 = new RecipeLine(120, 0, 5, recipeId);//сахар 120
                    RecipeLine rl5 = new RecipeLine(100, 0, 14, recipeId);//какао 100
                    RecipeLine rl6 = new RecipeLine(225, 0, 2, recipeId);//мука 225
                    RecipeLine rl7 = new RecipeLine(5, 0, 6, recipeId);//сода 5
                    RecipeLine rl8 = new RecipeLine(0, 2, 8, recipeId);//разрыхлитель 2
                    RecipeLine rl9 = new RecipeLine(30, 0, 15, recipeId);//кофе 30

                    ArrayList<RecipeLine> list = new ArrayList<RecipeLine>();
                    list.add(rl1);
                    list.add(rl2);
                    list.add(rl3);
                    list.add(rl4);
                    list.add(rl5);
                    list.add(rl6);
                    list.add(rl7);
                    list.add(rl8);
                    list.add(rl9);

                    for(RecipeLine rl : list){
                        dbr.insert(DBHelper.TABLE_RECIPE_LINES, rl.getInsertedColumns());
                        LogExtension.Debug("Record created: " + rl.toString());
                    }
                } break;
                case 2 /*"Сметанник"*/:{
                    RecipeLine rl1 = new RecipeLine(0, 3, 1, recipeId);//"яйцо 3"
                    RecipeLine rl2 = new RecipeLine(400, 0, 5, recipeId);//сахар 400
                    RecipeLine rl3 = new RecipeLine(750, 0, 17, recipeId);//сметана 750
                    RecipeLine rl4 = new RecipeLine(500, 0, 2, recipeId);//мука 500
                    RecipeLine rl5 = new RecipeLine(200, 0, 19, recipeId);//маргарин 200
                    RecipeLine rl6 = new RecipeLine(5, 0, 6, recipeId);//сода 5

                    ArrayList<RecipeLine> list = new ArrayList<RecipeLine>();
                    list.add(rl1);
                    list.add(rl2);
                    list.add(rl3);
                    list.add(rl4);
                    list.add(rl5);
                    list.add(rl6);

                    for(RecipeLine rl : list) {
                        dbr.insert(DBHelper.TABLE_RECIPE_LINES, rl.getInsertedColumns());
                        LogExtension.Debug("Record created: " + rl.toString());
                    }
                } break;
                case 3 /*"Шифоновый бисквит с желтками"*/:{
                    RecipeLine rl1 = new RecipeLine(0, 3, 1, recipeId);//яйцо 4
                    RecipeLine rl2 = new RecipeLine(120, 0, 5, recipeId);//сахар 120
                    RecipeLine rl3 = new RecipeLine(45, 0, 11, recipeId);//масло подсолнечное 45
                    RecipeLine rl4 = new RecipeLine(52, 0, 13, recipeId);//молоко 52
                    RecipeLine rl5 = new RecipeLine(90, 0, 2, recipeId);//мука 90
                    RecipeLine rl6 = new RecipeLine(30, 0, 4, recipeId);//крахмал кукурузный 30
                    RecipeLine rl7 = new RecipeLine(0, 1, 9, recipeId);//ванилин 1
                    RecipeLine rl8 = new RecipeLine(100, 0, 16, recipeId);//кокос 100
                    RecipeLine rl9 = new RecipeLine(0, 1, 8, recipeId);//разрыхлитель 1

                    ArrayList<RecipeLine> list = new ArrayList<RecipeLine>();
                    list.add(rl1);
                    list.add(rl2);
                    list.add(rl3);
                    list.add(rl4);
                    list.add(rl5);
                    list.add(rl6);
                    list.add(rl7);
                    list.add(rl8);
                    list.add(rl9);

                    for(RecipeLine rl : list) {
                        dbr.insert(DBHelper.TABLE_RECIPE_LINES, rl.getInsertedColumns());
                        LogExtension.Debug("Record created: " + rl.toString());
                    }
                } break;
                case 4 /*"Красный вельвет"*/:{

                    RecipeLine rl1 = new RecipeLine(200, 0, 2, recipeId);//200 гр. муки
                    RecipeLine rl2 = new RecipeLine(160, 0, 5, recipeId);//160 гр. сахара
                    RecipeLine rl3 = new RecipeLine(5, 0, 14, recipeId);//5 гр. какао
                    RecipeLine rl4 = new RecipeLine(3, 0, 7, recipeId);//3 гр соли
                    RecipeLine rl5 = new RecipeLine(15, 0, 8, recipeId);//15 гр разрыхлитель
                    RecipeLine rl6 = new RecipeLine(90, 0, 53, recipeId);//90 гр белка (3 шт)
                    RecipeLine rl7 = new RecipeLine(116, 0, 11, recipeId);//116 гр растительного масла
                    RecipeLine rl8 = new RecipeLine(120, 0, 12, recipeId);//120 мл. кефира
                    RecipeLine rl9 = new RecipeLine(1, 0, 9, recipeId);//1 гр ванилина
                    RecipeLine rl10 = new RecipeLine(30, 0, 52, recipeId);//30 гр. краситель

                    ArrayList<RecipeLine> list = new ArrayList<RecipeLine>();
                    list.add(rl1);
                    list.add(rl2);
                    list.add(rl3);
                    list.add(rl4);
                    list.add(rl5);
                    list.add(rl6);
                    list.add(rl7);
                    list.add(rl8);
                    list.add(rl9);
                    list.add(rl10);

                    for(RecipeLine rl : list) {
                        dbr.insert(DBHelper.TABLE_RECIPE_LINES, rl.getInsertedColumns());
                        LogExtension.Debug("Record created: " + rl.toString());
                    }
                } break;
                case 5 /*"Цветной бисквит"*/:{

                    RecipeLine rl1 = new RecipeLine(200, 0, 2, recipeId);//200 гр. муки
                    RecipeLine rl2 = new RecipeLine(160, 0, 5, recipeId);//160 гр. сахара
                    RecipeLine rl3 = new RecipeLine(5, 0, 14, recipeId);//5 гр. какао
                    RecipeLine rl4 = new RecipeLine(3, 0, 7, recipeId);//3 гр соли
                    RecipeLine rl5 = new RecipeLine(15, 0, 8, recipeId);//15 гр разрыхлитель
                    RecipeLine rl6 = new RecipeLine(90, 0, 53, recipeId);//90 гр белка (3 шт)
                    RecipeLine rl7 = new RecipeLine(116, 0, 11, recipeId);//116 гр растительного масла
                    RecipeLine rl8 = new RecipeLine(120, 0, 12, recipeId);//120 мл. кефира
                    RecipeLine rl9 = new RecipeLine(1, 0, 9, recipeId);//1 гр ванилина
                    RecipeLine rl10 = new RecipeLine(30, 0, 52, recipeId);//30 гр. краситель

                    ArrayList<RecipeLine> list = new ArrayList<RecipeLine>();
                    list.add(rl1);
                    list.add(rl2);
                    list.add(rl3);
                    list.add(rl4);
                    list.add(rl5);
                    list.add(rl6);
                    list.add(rl7);
                    list.add(rl8);
                    list.add(rl9);
                    list.add(rl10);

                    for(RecipeLine rl : list) {
                        dbr.insert(DBHelper.TABLE_RECIPE_LINES, rl.getInsertedColumns());
                        LogExtension.Debug("Record created: " + rl.toString());
                    }
                } break;
            }
            id++;
        }
    }
}

package yummycherrypie.business_logic;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import yummycherrypie.base_classes.Component;
import yummycherrypie.base_classes.Recipe;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.business_logic.Extensions.LogExtension;
import yummycherrypie.business_logic.Extensions.OtherExtensions;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.dal.repositories.RecipeLineRepository;
import yummycherrypie.dal.repositories.RecipeRepository;

/**
 * Created by CoreAttack on 31.07.2015.
 * Вычисления
 */
public class Calculation {

    /* закрытие поля */

    /**
     * Округление сумм до соответствующего числа
     */
    private static final double roundStep = 0;

    private ComponentRepository compRepo;
    private RecipeRepository recipeRepo;
    private RecipeLineRepository rlRepo;
    private long recipeId = -1;
    private long bookingId = -1;
    private Recipe recipe;
    private ArrayList<RecipeLine> recipeLines;
    private Context context;

    /**
     * Себестоимость рецепта
     */
    private double summary;

    /**
     * Было ли изменено программно количество яиц (округлено до большего целого)
     */
    private boolean isCountEggsChanged = false;

    /* открытые поля*/



    public double getSummary() { return summary; }

    /**
     * Сбросить до умолчаний
     */
    public void refresh(){
        isCountEggsChanged = false;
        summary = 0;
    }

    public Calculation(RecipeRepository recipeRepo, RecipeLineRepository rlRepo,
                       ComponentRepository compRepo, long recipeId, long bookingId, Context context,
                       ArrayList<RecipeLine> recipeLines){
        this.recipeRepo = recipeRepo;
        this.compRepo = compRepo;
        this.rlRepo = rlRepo;
        this.recipeId = recipeId;
        this.bookingId = bookingId;
        this.context = context;

        recipe = recipeRepo.getRecipe(recipeId);
        //recipeLines = rlRepo.getAllRecipeLinesArrayListByBooking(bookingId);
        this.recipeLines = recipeLines;
    }

    public Calculation(RecipeRepository recipeRepo, RecipeLineRepository rlRepo,
                       ComponentRepository compRepo, long recipeId, Context context){
        this.recipeRepo = recipeRepo;
        this.compRepo = compRepo;
        this.rlRepo = rlRepo;
        this.recipeId = recipeId;
        this.bookingId = bookingId;
        this.context = context;

        recipe = recipeRepo.getRecipe(recipeId);
        recipeLines = rlRepo.getAllRecipeLinesForRecipe(recipeId);
    }

    /**
     * Вычисление себестоимости рецепта по весу
     */
    public void setSummary(double newRecipeWeightOrCount){
        //размер порции
        double recipeWeightOrCount = recipe.isCountable() ? recipe.getCountProduct() : recipe.getCakeWeight();
        //вычисляем коэффициент изменения веса ингридиентов
        double coeffWeight = 1;
        if (recipeWeightOrCount > 0)
            coeffWeight = newRecipeWeightOrCount / recipeWeightOrCount;
        else
            Toast.makeText(context, recipe.isCountable() ? "Количество штук должно быть больше нуля!"
                    : "Вес порции должен быть больше нуля!", Toast.LENGTH_SHORT).show();

        LogExtension.Debug(String.format(OtherExtensions.DEFAULT_LOCALE, "currentWeightOrCount: %.2f", newRecipeWeightOrCount));
        LogExtension.Debug(String.format(OtherExtensions.DEFAULT_LOCALE, "recipeWeightOrCount: %.2f", recipeWeightOrCount));
        LogExtension.Debug(String.format(OtherExtensions.DEFAULT_LOCALE, "coeff: %.8f", coeffWeight));

        if (!isCountEggsChanged)
            summary = 0;
        double newWeight = 0;
        double currentPrice = 0;
        for (int i = 0; i < recipeLines.size(); i++) {
            Component c = compRepo.getComponent(recipeLines.get(i).getComponentId());
            //если количество яиц было изменено, то пересчитываем вес всех ингридиентов, кроме яиц
            if (isCountEggsChanged && (c.getName().toLowerCase().contains("яйцо")
                    || c.getName().toLowerCase().contains("яйца")))
                continue;
            //необходимое количество ингридиента
            //если штучный, то берем количество штук
            //ВАЖНО: на коэффициент мы умножаем первоначальное значение веса ингридиента рецепта,
            // а не измененное зачение веса ингридиента заказа
            int count = -1;
            double weight = -1;
            if (c.isCountable()) {
                 count = rlRepo.getRecipeLinesCountFromTemplateRecipe(recipeId,
                         recipeLines.get(i).getComponentId());
                 if ((c.getName().toLowerCase().contains("яйцо") || c.getName().toLowerCase().contains("яйца")))
                     newWeight = count * coeffWeight;
                 else
                     newWeight = Math.ceil(count * coeffWeight);
            }
            else {
                weight = rlRepo.getRecipeLinesWeightFromTemplateRecipe(recipeId,
                        recipeLines.get(i).getComponentId());
                newWeight = weight * coeffWeight;

                LogExtension.Debug(String.format(OtherExtensions.DEFAULT_LOCALE, "Вес ингридиента %s был %.2f", c.getName(), weight));
                LogExtension.Debug(String.format(OtherExtensions.DEFAULT_LOCALE, "Вес ингридиента %s стал %.2f", c.getName(), newWeight));

            }

            //Math.ceil(23.46) = 24
            //Math.floor(23.46) = 23
            //если ингридиент яйцо, то пересчитать
            if ((c.getName().toLowerCase().contains("яйцо") || c.getName().toLowerCase().contains("яйца"))
                    && (Math.floor(newWeight) != Math.ceil(newWeight)) && !c.getName().toLowerCase().contains("белок"))
            {
                double oldWeight = newWeight;
                LogExtension.Debug("Зашли округлять в большую сторону количество яиц... ");
                //здесь newWeight - это количество яиц

                newWeight = Math.ceil(newWeight);

                LogExtension.Debug(String.format(OtherExtensions.DEFAULT_LOCALE, "Округленное количество яиц = %.2f (было %.2f)", newWeight, oldWeight));

                if (c.getWeight() > 0)
                    currentPrice = (newWeight * c.getPrice()) / c.getWeight();
                else
                    currentPrice = (newWeight * c.getPrice());
                summary += currentPrice;

                LogExtension.Debug(String.format(OtherExtensions.DEFAULT_LOCALE, "Стоимость яиц = %.2f", currentPrice));

                //вес порции, на которую нужно округленное количество яиц
                double newCakeWeight = 0;
                if (count > 0)
                    newCakeWeight = (recipeWeightOrCount * newWeight) / count;
                else{
                    Toast.makeText(context, "Округленное количество яиц должно быть больше нуля!", Toast.LENGTH_SHORT).show();
                }

                recipeLines.get(i).setCount((int)newWeight);
                isCountEggsChanged = true;

                Toast.makeText(context, String.format(OtherExtensions.DEFAULT_LOCALE, "Минимально возможная порция: %.2f г", newCakeWeight), Toast.LENGTH_LONG)
                        .show();

                setSummary(newCakeWeight);

                break;
            }

            if (c.isCountable())
                recipeLines.get(i).setCount((int)newWeight);
            else
                recipeLines.get(i).setWeight(newWeight);
            //стоимость заданного количества ингридиентов
            if (c.getWeight() > 0)
                currentPrice = ((newWeight)  * c.getPrice()) / c.getWeight();
            else
                currentPrice = ((int)newWeight) * c.getPrice();//за штуку указываем вес штучных ингредиентов

            LogExtension.Debug(String.format(OtherExtensions.DEFAULT_LOCALE, "Стоимость ингридиента %s = %.2f", c.getName(), currentPrice));

            summary += currentPrice;
        }
    }

    /**
     * Обновляет в БД строки рецепта: изменяет количество или вес
     */
    public void updateRecipeLinesArray(){
        for(RecipeLine rl : recipeLines){
            //изменяем только шаблонные ингридиенты
            //добавленные пользователем не трогаем
            if (rl.isDefault()) {
                ContentValues cv = new ContentValues();
                if (rl.getCount() > 0)
                    cv.put(DBHelper.COLUMN_RECIPE_LINES_COUNT, rl.getCount());
                else
                    cv.put(DBHelper.COLUMN_RECIPE_LINES_WEIGHT, rl.getWeight());
                rlRepo.update(DBHelper.TABLE_RECIPE_LINES, cv, rl.getId());
            }
        }
    }

    /**
     * Возвращает коэффициент веса для калькулятора
     * @param currentWeight какой вес нам нужен
     * @param recipeWeight размер порции
     */
    public static double getWeightCoeff(double currentWeight, double recipeWeight){
        return currentWeight / recipeWeight;
    }

    /**
     * Вычисляет сумму со скидкой (округлять до 50 рублей в меньшую сторону)
     * @param price сумма
     * @param discount скидка в формате 0.1 = 10%
     */
    public static double getDiscountPrice(double price, double discount){
        return round(price - price * (discount / 100));
    }

    /**
     * Возвращает конечную выручку от заказа
     */
    public static double getFinallyPrice(double price, double discount, double recipePrice){
        return round(getDiscountPrice(price, discount) - recipePrice);
    }

    /**
     * Округление
     */
    public static double round(double value){
        if (roundStep != 0)
            return value - (value % roundStep);
        else
            return value;
    }

    /*
    * Возвращает стоимость заказа, либо стоимость штучного заказа
    * */
    public static double getResultPrice(double price, double count, boolean isCountableRecipe){
        if (isCountableRecipe){
            return price * count;
        }
        return price;
    }
}

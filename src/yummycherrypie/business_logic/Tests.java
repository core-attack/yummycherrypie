package yummycherrypie.business_logic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.base_classes.BookingType;
import yummycherrypie.base_classes.Component;
import yummycherrypie.base_classes.Event;
import yummycherrypie.base_classes.Recipe;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.pl.PhoneExtension;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.dal.repositories.BookingTypeRepository;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.dal.repositories.EventRepository;
import yummycherrypie.dal.repositories.RecipeLineRepository;
import yummycherrypie.dal.repositories.RecipeRepository;

/**
 * Created by piskarev on 18.09.2015.
 * Класс для тестирования приложения
 */
public class Tests {

    static final String LOG = "testLog";

    DBHelper dbHelper;
    SQLiteDatabase db;
    private BookingRepository bookingRepo;
    private RecipeRepository recipeRepo;
    private BookingManRepository bmRepo;
    private BookingTypeRepository btRepo;
    private ComponentRepository compRepo;
    private EventRepository eventRepo;
    private RecipeLineRepository rlRepo;

    Random r;
    Context context;

    public Tests(Context context){
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        compRepo = new ComponentRepository(dbHelper);
        bookingRepo = new BookingRepository(dbHelper);
        recipeRepo = new RecipeRepository(dbHelper);
        bmRepo = new BookingManRepository(dbHelper);
        btRepo = new BookingTypeRepository(dbHelper);
        eventRepo = new EventRepository(dbHelper);
        rlRepo = new RecipeLineRepository(dbHelper);
        this.context = context;
        r = new Random();

        if (runTests()){
            Log.d(LOG, "---");
            Log.d(LOG, "---");
            Log.d(LOG, "---");
            Log.d(LOG, "---");
            Log.d(LOG, "---");
            Log.d(LOG, "Тесты пройдены успешно!");
            Toast.makeText(context, "Тесты пройдены успешно!", Toast.LENGTH_SHORT)
                    .show();
        }
        else{
            Log.d(LOG, "---");
            Log.d(LOG, "---");
            Log.d(LOG, "---");
            Log.d(LOG, "---");
            Log.d(LOG, "---");
            Log.d(LOG, "Тесты не пройдены...");
            Toast.makeText(context, "Тесты не пройдены...", Toast.LENGTH_SHORT)
                    .show();
        }

        Log.d(LOG, "---");
        Log.d(LOG, "---");
        Log.d(LOG, "---");
        Log.d(LOG, "---");
        Log.d(LOG, "---");


    }

    protected void onDestroy() {
        compRepo.close();
    }

    private boolean runTests(){
        boolean result = true;

        if (!createRecordTest())
            result = false;
        if (!calcTest())
            result = false;
        if (!phoneTest())
            result = false;

        return result;
    }

    /**
     * генерирует случайную строку заданного размера
     * */
    private String generateRandomString(int size){

        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        String result = "";
        for (int i = 0; i < size; i++)
            result += alphabet[r.nextInt(alphabet.length)];
        return result;
    }

    /**
     * генерирует случайную строку заданного размера
     * */
    private String generateRandomStringOfNumbers(int size){

        char[] alphabet = "0123456789".toCharArray();
        String result = "";
        for (int i = 0; i < size; i++)
            result += alphabet[r.nextInt(alphabet.length)];
        return result;
    }

    /**
     * генерирует случайный int
     * */
    private int generateRandomInt(){
        return r.nextInt();
    }

    /**
     * генерирует случайный long
     * */
    private long generateRandomLong(){
        return r.nextLong();
    }

    /**
     * генерирует случайный double
     * */
    private double generateRandomDouble(){
        return r.nextLong() + r.nextDouble();
    }

    /**
     * генерирует случайную дату
     * */
    private long generateRandomDate(){
        Calendar c = Calendar.getInstance();
        c.set(r.nextInt(c.get(c.YEAR)), r.nextInt(12), r.nextInt(28), r.nextInt(24), r.nextInt(60), r.nextInt(60));
        return c.getTimeInMillis();
    }

    /**
     * генерирует случайный boolean
     * */
    private boolean generateRandomBoolean(){
        return r.nextInt(10) > 5 ? true: false;
    }

    /**
     * Тестируем вычисления
     * */
    private boolean calcTest(){
        try{

            Recipe recipe = new Recipe("Тестовый рецепт", 2000/*вес торта*/, 0, false);
            long recipeId = compRepo.insert(DBHelper.TABLE_RECIPES, recipe.getInsertedColumns());
            RecipeLine rl1 = new RecipeLine(0, 3, 1, recipeId);
            RecipeLine rl2 = new RecipeLine(120, 0, 5, recipeId);
            RecipeLine rl4 = new RecipeLine(600, 0, 2, recipeId);
            RecipeLine rl5 = new RecipeLine(210, 0, 19, recipeId);

            ArrayList<RecipeLine> list = new ArrayList<RecipeLine>();
            list.add(rl1);
            list.add(rl2);
            list.add(rl4);
            list.add(rl5);

            for(RecipeLine rl : list) {
                compRepo.insert(DBHelper.TABLE_RECIPE_LINES, rl.getInsertedColumns());
            }

            Component c1 = compRepo.getComponent(1);
            Component c2 = compRepo.getComponent(5);
            Component c3 = compRepo.getComponent(2);
            Component c4 = compRepo.getComponent(19);

            double[] arrWeight = new double[]{ 1000, 2000, 3000, 4000 };
            double[] arrPrice = new double[arrWeight.length];

            Log.d(LOG, "Считаем эталонные величины:");

            for(int i = 0; i < arrPrice.length; i++) {
                double comp1 = 0;
                double comp2 = 0;
                double comp3 = 0;
                double comp4 = 0;
                double coeff = 0;

                coeff = arrWeight[i] / recipe.getCakeWeight();
                Log.d(LOG, "coeff:" + coeff);

                switch (i) {
                    case 0: {
                        comp1 = 2 * c1.getPrice(); Log.d(LOG, String.format("%s (%.2f): %.2f", c1.getName(), c1.getPrice(), comp1));
                        comp2 = 80 * c2.getPrice() / c2.getWeight();  Log.d(LOG, String.format("%s (%.2f): %.2f", c2.getName(), c2.getPrice(), comp2));
                        comp3 = 400 * c3.getPrice() / c3.getWeight();  Log.d(LOG, String.format("%s (%.2f): %.2f", c3.getName(), c3.getPrice(), comp3));
                        comp4 = 140 * c4.getPrice() / c4.getWeight();  Log.d(LOG, String.format("%s (%.2f): %.2f", c4.getName(), c4.getPrice(), comp4));
                    }
                    break;
                    case 1: {
                        comp1 = 3 * c1.getPrice(); Log.d(LOG, String.format("%s (%.2f): %.2f", c1.getName(), c1.getPrice(), comp1));
                        comp2 = 120 * c2.getPrice() / c2.getWeight(); Log.d(LOG, String.format("%s (%.2f): %.2f", c2.getName(), c2.getPrice(), comp2));
                        comp3 = 600 * c3.getPrice() / c3.getWeight(); Log.d(LOG, String.format("%s (%.2f): %.2f", c3.getName(), c3.getPrice(), comp3));
                        comp4 = 210 * c4.getPrice() / c4.getWeight(); Log.d(LOG, String.format("%s (%.2f): %.2f", c4.getName(), c4.getPrice(), comp4));
                    }
                    break;
                    case 2: {
                        comp1 = 5 * c1.getPrice(); Log.d(LOG, String.format("%s (%.2f): %.2f", c1.getName(), c1.getPrice(), comp1));
                        comp2 = 200 * c2.getPrice() / c2.getWeight(); Log.d(LOG, String.format("%s (%.2f): %.2f", c2.getName(), c2.getPrice(), comp2));
                        comp3 = 1000 * c3.getPrice() / c3.getWeight(); Log.d(LOG, String.format("%s (%.2f): %.2f", c3.getName(), c3.getPrice(), comp3));
                        comp4 = 350 * c4.getPrice() / c4.getWeight(); Log.d(LOG, String.format("%s (%.2f): %.2f", c4.getName(), c4.getPrice(), comp4));
                    }
                    break;
                    case 3: {
                        comp1 = 6 * c1.getPrice(); Log.d(LOG, String.format("%s (%.2f): %.2f", c1.getName(), c1.getPrice(), comp1));
                        comp2 = 240 * c2.getPrice() / c2.getWeight(); Log.d(LOG, String.format("%s (%.2f): %.2f", c2.getName(), c2.getPrice(), comp2));
                        comp3 = 1200 * c3.getPrice() / c3.getWeight(); Log.d(LOG, String.format("%s (%.2f): %.2f", c3.getName(), c3.getPrice(), comp3));
                        comp4 = 420 * c4.getPrice() / c4.getWeight(); Log.d(LOG, String.format("%s (%.2f): %.2f", c4.getName(), c4.getPrice(), comp4));
                    }
                    break;
                }
                arrPrice[i] = comp1 + comp2 + comp3 + comp4;
                Log.d(LOG, String.format("Просчитали стоимость рецепта на %.2f: %.2f", arrWeight[i], arrPrice[i]));
            }

            Calculation calc = new Calculation(new RecipeRepository(dbHelper), new RecipeLineRepository(dbHelper), compRepo, recipeId, context);

            int j = 0;
            for(int i = 0; i < arrWeight.length; i++) {
                calc.refresh();
                calc.setSummary(arrWeight[i]);
                Log.d(LOG, String.format("Для веса %f г расчитанная стоимость (%f) %s эталонной (%f)",
                    arrWeight[i], calc.getSummary(), calc.getSummary() == arrPrice[i] ? "соответствует" : "НЕ соответствует", arrPrice[i]));
                if (calc.getSummary() == arrPrice[i])
                    j++;
            }

            recipeRepo.delete(DBHelper.TABLE_RECIPES, recipeId);

            //только если все тесты прошли успешно, признаем тест успешным
            return j == arrWeight.length;
        }
        catch (Exception e){
            Log.d(LOG, e.getMessage());
            Log.d(LOG, e.getStackTrace().toString());
            return false;
        }
    }

    /**
     * Тестируем запросы
     * */
    private boolean queryTest(){
        try{
            return false;
        }
        catch (Exception e){
            Log.d(LOG, e.getMessage());
            Log.d(LOG, e.getStackTrace().toString());
            return false;
        }
    }

    /**
     * Тестируем создание записей
     * */
    private boolean createRecordTest(){
        try {
            Log.d(LOG, "Creating records testing...");

            Booking booking = new Booking(
                    generateRandomDate(),
                    generateRandomDate(),
                    generateRandomLong(),
                    generateRandomLong(),
                    generateRandomString(15),
                    generateRandomDouble(),
                    generateRandomDouble(),
                    generateRandomDouble(),
                    generateRandomDouble(),
                    generateRandomLong(),
                    generateRandomLong(),
                    generateRandomInt(),
                    generateRandomInt()
            );
            booking.setCreateDate(generateRandomDate());
            Log.d(LOG, "Before insert: " + booking.toString());
            long bookingId = bookingRepo.insert(dbHelper.TABLE_BOOKINGS, booking.getInsertedColumns());
            Log.d(LOG, "After insert: " + bookingRepo.getBooking(bookingId).toString());
            booking.setId(bookingId);
            booking.setCreateDate(generateRandomDate());
            booking.setWeight(generateRandomDouble());
            booking.setEventId(generateRandomLong());
            booking.setRecipeId(generateRandomLong());
            booking.setBookingManId(generateRandomLong());
            booking.setCakePrice(generateRandomDouble());
            booking.setDiscount(generateRandomDouble());
            booking.setDateLong(generateRandomLong());
            booking.setRecipePrice(generateRandomDouble());
            booking.setComment(generateRandomString(15));
            booking.setUpdateDate(generateRandomDate());
            Log.d(LOG, "Before update: " + booking.toString());
            compRepo.update(dbHelper.TABLE_BOOKINGS, booking.getUpdatedColumns(), bookingId);
            Log.d(LOG, "After update: " + bookingRepo.getBooking(bookingId).toString());
            compRepo.delete(dbHelper.TABLE_BOOKINGS, bookingId);



            BookingMan bookingMan = new BookingMan(
                    generateRandomString(15),
                    generateRandomStringOfNumbers(9),
                    generateRandomString(10)

            );
            bookingMan.setCreateDate(generateRandomDate());
            Log.d(LOG, "Before insert: " + bookingMan.toString());
            long bookingManId = bmRepo.insert(dbHelper.TABLE_BOOKING_MEN, bookingMan.getInsertedColumns());
            Log.d(LOG, "After insert: " + bmRepo.getBookingMan(bookingManId).toString());
            bookingMan.setUpdateDate(generateRandomDate());
            bookingMan.setName(generateRandomString(15));
            bookingMan.setPhone(generateRandomStringOfNumbers(11));
            Log.d(LOG, "Before update: " + bookingMan.toString());
            compRepo.update(dbHelper.TABLE_BOOKING_MEN, bookingMan.getUpdatedColumns(), bookingManId);
            Log.d(LOG, "After update: " + bmRepo.getBookingMan(bookingManId).toString());
            compRepo.delete(dbHelper.TABLE_BOOKING_MEN, bookingManId);

            BookingType bType = new BookingType(generateRandomString(15), generateRandomBoolean());
            bType.setCreateDate(generateRandomDate());
            Log.d(LOG, "Before insert: " + bType.toString());
            long bTypeId = compRepo.insert(dbHelper.TABLE_BOOKING_TYPES, bType.getInsertedColumns());
            Log.d(LOG, "After insert: " + btRepo.getBookingType(bTypeId).toString());
            bType.setName(generateRandomString(15));
            bType.setUpdateDate(generateRandomDate());
            Log.d(LOG, "Before update: " + bType.toString());
            compRepo.update(dbHelper.TABLE_BOOKING_TYPES, bType.getUpdatedColumns(), bTypeId);
            Log.d(LOG, "After update: " + btRepo.getBookingType(bTypeId).toString());
            compRepo.delete(dbHelper.TABLE_BOOKING_TYPES, bTypeId);


            Component c = new Component(
                    generateRandomString(15),
                    generateRandomDouble(),
                    generateRandomDouble(),
                    generateRandomBoolean()
            );
            c.setCreateDate(generateRandomDate());
            Log.d(LOG, "Before insert: " + c.toString());
            long cId = compRepo.insert(dbHelper.TABLE_COMPONENTS, c.getInsertedColumns());
            Log.d(LOG, "After insert: " + compRepo.getComponent(cId).toString());
            c.setName(generateRandomString(15));
            c.setUpdateDate(generateRandomDate());
            c.setWeight(generateRandomDouble());
            c.setIsCountable(generateRandomBoolean());
            c.setPrice(generateRandomDouble());
            Log.d(LOG, "Before update: " + c.toString());
            compRepo.update(dbHelper.TABLE_COMPONENTS, c.getUpdatedColumns(), cId);
            Log.d(LOG, "After update: " + compRepo.getComponent(cId).toString());
            compRepo.delete(dbHelper.TABLE_COMPONENTS, cId);

            Event e = new Event(
                    generateRandomString(30),
                    generateRandomString(30),
                    generateRandomDate(),
                    generateRandomLong(),
                    generateRandomDouble()
            );
            e.setCreateDate(generateRandomDate());
            Log.d(LOG, "Before insert: " + e.toString());
            long eId = compRepo.insert(dbHelper.TABLE_EVENTS, e.getInsertedColumns());
            Log.d(LOG, "After insert: " + eventRepo.getEvent(eId).toString());
            e.setDateLong(generateRandomDate());
            e.setName(generateRandomString(40));
            e.setUpdateDate(generateRandomDate());
            e.setPrice(generateRandomDouble());
            e.setBookingManId(generateRandomLong());
            e.setPlace(generateRandomString(40));
            Log.d(LOG, "Before update: " + e.toString());
            compRepo.update(dbHelper.TABLE_EVENTS, e.getUpdatedColumns(), eId);
            Log.d(LOG, "After update: " + eventRepo.getEvent(eId).toString());
            compRepo.delete(dbHelper.TABLE_EVENTS, eId);

            Recipe r = new Recipe(
                    generateRandomString(20),
                    generateRandomDouble(),
                    generateRandomInt(),
                    generateRandomBoolean()
            );
            r.setCreateDate(generateRandomDate());
            Log.d(LOG, "Before insert: " + r.toString());
            long rId = compRepo.insert(dbHelper.TABLE_RECIPES, r.getInsertedColumns());
            Log.d(LOG, "After insert: " + recipeRepo.getRecipe(rId).toString());
            r.setName(generateRandomString(30));
            r.setUpdateDate(generateRandomDate());
            r.setCakeWeight(generateRandomDouble());
            Log.d(LOG, "Before update: " + r.toString());
            compRepo.update(dbHelper.TABLE_RECIPES, r.getUpdatedColumns(), rId);
            Log.d(LOG, "After update: " + recipeRepo.getRecipe(rId).toString());
            compRepo.delete(dbHelper.TABLE_RECIPES, rId);

            RecipeLine rl = new RecipeLine(
                    generateRandomDouble(),
                    generateRandomInt(),
                    generateRandomLong(),
                    generateRandomLong(),
                    generateRandomLong(),
                    RecipeLine.State.ADD_BY_USER
            );
            Log.d(LOG, "Before insert: " + rl.toString());
            long rlId = compRepo.insert(dbHelper.TABLE_RECIPE_LINES, rl.getInsertedColumns());
            Log.d(LOG, "After insert: " + rlRepo.getRecipeLine(rlId).toString());
            rl.setRecipeId(generateRandomLong());
            rl.setWeight(generateRandomDouble());
            rl.setBookingId(generateRandomLong());
            rl.setComponentId(generateRandomLong());
            rl.setCount(generateRandomInt());
            rl.setIsDefault(generateRandomBoolean());
            Log.d(LOG, "Before update: " + rl.toString());
            compRepo.update(dbHelper.TABLE_RECIPE_LINES, rl.getUpdatedColumns(), rlId);
            Log.d(LOG, "After update: " + rlRepo.getRecipeLine(rlId).toString());
            compRepo.delete(dbHelper.TABLE_RECIPE_LINES, rlId);

            Log.d(LOG, "Creating records test successfully competed!");

            return true;
        }
        catch (Exception e){
            Log.d(LOG, e.getMessage());
            Log.d(LOG, e.getStackTrace().toString());
            return false;
        }
    }

    private boolean phoneTest(){

        if (PhoneExtension.beautyPhoneNumber("89199119495").equals("+7 (919) 911-94-95") &&
                PhoneExtension.beautyPhoneNumber("79199119495").equals("+7 (919) 911-94-95") &&
                PhoneExtension.beautyPhoneNumber("69199119495").equals("+6 (919) 911-94-95") &&
                PhoneExtension.beautyPhoneNumber("9199119495").equals("+7 (919) 911-94-95"))
            return true;
        return false;
    }

}

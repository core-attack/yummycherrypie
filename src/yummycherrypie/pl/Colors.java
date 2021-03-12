package yummycherrypie.pl;

import android.graphics.Color;

/**
 * Created by Nikolay_Piskarev on 12/8/2015.
 */
public class Colors {

    public static final int PINK = Color.parseColor("#f9b6c4");
    public static final int ORANGE = Color.parseColor("#f8c7ae");
    public static final int YELLOW = Color.parseColor("#f3eda8");
    public static final int LIGHT_GREEN = Color.parseColor("#e4ecb2");
    public static final int DARK_GREEN = Color.parseColor("#cbe2d0");
    public static final int BLUE = Color.parseColor("#bdd1f3");
    public static final int MAGENTA = Color.parseColor("#d7c6d6");

    /**
     * дни недели в календаре
     * */
    public static final int COLOR_DEFAULT_DAY_BCKG = Color.parseColor("#99ffffff");

    /**
     * дни недели в календаре (не текущий месяц)
     * */
    public static final int COLOR_DEFAULT_DAY_BCKG_NOT_THIS_MONTH = Color.parseColor("#73ffffff");

    /**
     * фон заголовков списка
     * */
    public static final int COLOR_DEFAULT_LIST_CAPTION_BACKGROUND = Color.parseColor("#b38a76");

    /**
     * дни недели в календаре
     * */
    public static final int COLOR_DEFAULT_DAY_TEXT = Color.parseColor("#605142");

    /**
     * фон приложения
     * */
    public static final int COLOR_DEFAULT_BCKG = Color.parseColor("#e7cdb4");

    /**
     * рамка дней в календаре
     * */
    public static final int COLOR_DAY_BORDER = Color.parseColor("#e7bd98");

    /**
     * текущий день (число) календаря
     * */
    public static final int COLOR_CURRENT_DAY = Color.parseColor("#ffffff");

    /*
    * Текст текста в статистике
    * */
    public static final int COLOR_STATISTIC_TEXT = Color.parseColor("#402E15");

    /**
     * Цвет фона типа заказа в добавлении заказов
     * */
    public static final int COLOR_DEFAULT_BOOKING_TYPE_BACKGROUND_GRID_VIEW_ITEM = Color.parseColor("#80bfa78b");

    /**
     * Цвет фона типа заказа в добавлении заказов (выделенный)
     * */
    public static final int COLOR_DEFAULT_BOOKING_TYPE_BACKGROUND_GRID_VIEW_ITEM_SELECTED = Color.parseColor("#ffffff");

    /**
     * Цвет текста приложения
     * */
    public static final int COLOR_DEFAULT_TEXT = Color.parseColor("#402E15");

    public static final int EDIT_TEXT_ACTIVATE_COLOR = Color.parseColor("#7abfac");

    /*
        прозрачность обозначается так:
        обычный цвет #000000
        цвет с прозрачностью 60% #99000000
        100% — FF
        95% — F2
        90% — E6
        85% — D9
        80% — CC
        75% — BF
        70% — B3
        65% — A6
        60% — 99
        55% — 8C
        50% — 80
        45% — 73
        40% — 66
        35% — 59
        30% — 4D
        25% — 40
        20% — 33
        15% — 26
        10% — 1A
        5% — 0D
        0% — 00
    */
}

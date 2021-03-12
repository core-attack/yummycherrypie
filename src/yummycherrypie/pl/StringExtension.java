package yummycherrypie.pl;

/**
 * Created by CoreAttack on 01.12.2015.
 */
public class StringExtension {

    /**
     * Максимальное число букв в строке до её обрезания
     */
    private static final int COUNT_LETTERS = 15;

    public static final String DEFAULT_FONT = "fonts/NerisThin.ttf";

    public static final String COMPONENTS_FONT = "fonts/BadScript.ttf";

    public static final String ROUBLE_FONT = "fonts/rouble.otf";

    /**
     * Обрезает длинную строку
     */
    public static String makeShortString(String s){
        if (s.length() > COUNT_LETTERS)
            return String.format("%s...", s.substring(0, COUNT_LETTERS));
        return s;
    }

    /**
     * Обрезает длинную строку
     */
    public static String makeShortString(String s, int length){
        if (s.length() > length)
            return String.format("%s...", s.substring(0, length));
        return s;
    }
}

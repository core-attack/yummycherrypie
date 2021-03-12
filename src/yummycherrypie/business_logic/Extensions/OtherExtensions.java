package yummycherrypie.business_logic.Extensions;

import java.util.Locale;

public class OtherExtensions {

	public static final String VESRION = "1.34";

	/* Время по умолчанию при создании заказа */
	public static final Integer DEFAULT_BOOKING_TIME = 10;

	/*Локаль по умолчанию*/
	public static final Locale DEFAULT_LOCALE = Locale.getDefault();

	public static final String DEFAULT_CURRENCY_FORMAT = "%,.0f %s";

	/* сообщения об ошибке ниже */

	public static final String ERR_ADD_BACK_BUTTON_TO_ACTION_BAR = "Не удалось добавить кнопку \"Назад\" в шапку окна.";

	public static boolean include(long[] array, long value){
		for(long item : array){
			if (item == value)
				return true;
		}
		return false;
	}

}



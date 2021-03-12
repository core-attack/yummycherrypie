package yummycherrypie.pl;

/**
 * Created by CoreAttack on 01.12.2015.
 */
public class PhoneExtension {

    private static final int MAX_PHONE_LENGTH = 11;

    private static final String PHONE_TEMPLATE = "+%s (%s) %s-%s-%s";

    private static final String PHONE_TEMPLATE2 = "+7 (%s) %s-%s-%s";

    public static String beautyPhoneNumber(String phone){
        if (phone != null) {
            if (phone.length() == MAX_PHONE_LENGTH) {//89199118485
                return String.format(PHONE_TEMPLATE, phone.charAt(0) == '8' ? '7' : phone.charAt(0),
                        phone.substring(1, 4), phone.substring(4, 7), phone.substring(7, 9), phone.substring(9, 11));
            } else if (phone.length() == MAX_PHONE_LENGTH - 1)//9199118485
                return String.format(PHONE_TEMPLATE2,
                        phone.substring(0, 3), phone.substring(3, 6), phone.substring(6, 8), phone.substring(8, 10));
            else
                return phone;
        }
        else
            return "";

    }
}

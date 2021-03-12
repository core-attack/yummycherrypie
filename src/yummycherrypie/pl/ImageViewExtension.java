package yummycherrypie.pl;

import android.widget.ImageView;

import yummycherrypie.dal.default_records.BookingTypesDefaultRecords;
import yummycherrypie.system.R;

/**
 * Created by CoreAttack on 01.12.2015.
 */
public class ImageViewExtension {

    public enum Size{ XMIN, MIN, MIDDLE, MAX }

    public static void chooseImage(ImageView iv, long bookingTypeId, Size size){
        if (BookingTypesDefaultRecords.BOOKING_TYPE_CAKE_ID == bookingTypeId)
        {
            switch (size){
                case MAX: iv.setImageResource(R.drawable.cake); break;
                case MIDDLE: iv.setImageResource(R.drawable.icon_cake); break;
                case MIN: iv.setImageResource(R.drawable.icon_cake_36); break;
                case XMIN: iv.setImageResource(R.drawable.icon_cake_min); break;
            }
        }
        else if (BookingTypesDefaultRecords.BOOKING_TYPE_SPICE_CAKE_ID == bookingTypeId)
        {
            switch (size){
                case MAX: iv.setImageResource(R.drawable.spice_cake); break;
                case MIDDLE: iv.setImageResource(R.drawable.icon_spice_cake); break;
                case MIN: iv.setImageResource(R.drawable.icon_spice_cake_36); break;
                case XMIN: iv.setImageResource(R.drawable.icon_spice_cake_min); break;
            }
        }
        else if (BookingTypesDefaultRecords.BOOKING_TYPE_COOKIE_ID == bookingTypeId)
        {
            switch (size){
                case MAX: iv.setImageResource(R.drawable.cookies); break;
                case MIDDLE: iv.setImageResource(R.drawable.icon_cookies); break;
                case MIN: iv.setImageResource(R.drawable.icon_cookies_36); break;
                case XMIN: iv.setImageResource(R.drawable.icon_cookies_min); break;
            }
        }
        else if (BookingTypesDefaultRecords.BOOKING_TYPE_MARRIAGE_ID == bookingTypeId)
        {
            switch (size){
                case MAX: iv.setImageResource(R.drawable.marriage); break;
                case MIDDLE: iv.setImageResource(R.drawable.icon_cake_marriage); break;
                case MIN: iv.setImageResource(R.drawable.icon_cake_marriage_36); break;
                case XMIN: iv.setImageResource(R.drawable.icon_cake_marriage_min); break;
            }
        }
        else if (BookingTypesDefaultRecords.BOOKING_TYPE_CUPCAKE_ID == bookingTypeId)
        {
            switch (size){
                case MAX: iv.setImageResource(R.drawable.cupcake); break;
                case MIDDLE: iv.setImageResource(R.drawable.icon_cupcake); break;
                case MIN: iv.setImageResource(R.drawable.icon_cupcake_36); break;
                case XMIN: iv.setImageResource(R.drawable.icon_cupcake_min); break;
            }
        }
        else if (BookingTypesDefaultRecords.BOOKING_TYPE_PASTRY_ID == bookingTypeId)
        {
            switch (size){
                case MAX: iv.setImageResource(R.drawable.pastry); break;
                case MIDDLE: iv.setImageResource(R.drawable.icon_pastry); break;
                case MIN: iv.setImageResource(R.drawable.icon_pastry_36); break;
                case XMIN: iv.setImageResource(R.drawable.icon_pastry_min); break;
            }
        }
        else if (BookingTypesDefaultRecords.BOOKING_TYPE_CAKEPOPS_ID == bookingTypeId)
        {
            switch (size){
                case MAX: iv.setImageResource(R.drawable.cakepops); break;
                case MIDDLE: iv.setImageResource(R.drawable.icon_cakepops); break;
                case MIN: iv.setImageResource(R.drawable.icon_cakepops_36); break;
                case XMIN: iv.setImageResource(R.drawable.icon_cakepops_min); break;
            }
        }
        else
            iv.setImageResource(R.drawable.icon);
    }
}

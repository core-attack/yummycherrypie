package yummycherrypie.pl.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import yummycherrypie.pl.activities.bookings.BookingsFragment;

/**
 * Created by Nikolay_Piskarev on 12/15/2015.
 */
public class BookingsFragmentPagerAdapter extends FragmentPagerAdapter {

    private int pageCount = 0;

    public BookingsFragmentPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.pageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        return BookingsFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Прошедшие заказы";
            case 1: return "Заказы текущего месяца";
            case 2: return "Заказы следующих месяцев";
        }
        return "Новая страница";
    }

}
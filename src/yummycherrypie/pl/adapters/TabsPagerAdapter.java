package yummycherrypie.pl.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import yummycherrypie.pl.activities.bookings.BookingsFragment;

/**
 * Created by Nikolay_Piskarev on 12/17/2015.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {


    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public BookingsFragment getItem(int index) {
        return BookingsFragment.newInstance(index);
    }


    @Override
    public int getCount() {
        return 3;
    }
}

package yummycherrypie.pl.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import yummycherrypie.pl.activities.statistics.AllTimeStatisticFragment;
import yummycherrypie.pl.activities.statistics.MonthStatisticFragment;
import yummycherrypie.pl.activities.statistics.YearStatisticFragment;

/**
 * Created by Nikolay_Piskarev on 12/15/2015.
 */
public class StatisticFragmentPagerAdapter extends FragmentPagerAdapter {

    private int pageCount = 0;

    public StatisticFragmentPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.pageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return MonthStatisticFragment.newInstance(position);
            case 1: return YearStatisticFragment.newInstance(position);
            case 2: return AllTimeStatisticFragment.newInstance(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Месяц";
            case 1: return "Год";
            case 2: return "Всё время";
        }
        return "Новая страница";
    }

}

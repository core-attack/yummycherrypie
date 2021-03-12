package yummycherrypie.pl.activities.statistics;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.adapters.StatisticFragmentPagerAdapter;
import yummycherrypie.system.R;

public class Statistics extends BaseFragmentActivity {

    //private TabHost tabs;
    static final int PAGE_COUNT = 3;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new StatisticFragmentPagerAdapter(getSupportFragmentManager(), PAGE_COUNT);
        pager.setAdapter(pagerAdapter);

        PagerTitleStrip pts = (PagerTitleStrip) findViewById(R.id.PagerTabStrip);
        for (int i = 0; i < pts.getChildCount(); i++) {
            if (pts.getChildAt(i) instanceof TextView) {
                ((TextView)pts.getChildAt(i)).setTypeface(Typeface.createFromAsset(this.getAssets(), StringExtension.DEFAULT_FONT));
            }
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                //BookingsFragment frg = (BookingsFragment) pagerAdapter.instantiateItem(pager, position);
                //frg.refreshCursor();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

}

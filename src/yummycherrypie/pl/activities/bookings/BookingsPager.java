package yummycherrypie.pl.activities.bookings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.adapters.BookingsFragmentPagerAdapter;
import yummycherrypie.system.R;

/**
 * Created by Nikolay_Piskarev on 12/15/2015.
 */
public class BookingsPager extends BaseFragmentActivity {

    static final int PAGE_COUNT = 3;
    static final int DEFAULT_PAGE = 1;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_pager);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new BookingsFragmentPagerAdapter(getSupportFragmentManager(), PAGE_COUNT);
        pager.setAdapter(pagerAdapter);

        PagerTitleStrip pts = (PagerTitleStrip) findViewById(R.id.PagerTabStrip);
        for (int i=0; i < pts.getChildCount(); i++) {
            if (pts.getChildAt(i) instanceof TextView) {
                ((TextView)pts.getChildAt(i)).setTypeface(Typeface.createFromAsset(this.getAssets(), StringExtension.DEFAULT_FONT));
            }
        }

        pager.setCurrentItem(DEFAULT_PAGE);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                BookingsFragment frg = (BookingsFragment)pagerAdapter.instantiateItem(pager, position);
                frg.refreshCursor();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}
package yummycherrypie.pl.activities.bookings;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import yummycherrypie.system.R;
import yummycherrypie.pl.OnSwipeTouchListener;
import yummycherrypie.pl.adapters.TabsPagerAdapter;

/**
 * Created by Nikolay_Piskarev on 12/15/2015.
 */
public class BookingsTabHost extends FragmentActivity implements ActionBar.TabListener {
    private static final int ANIMATION_TIME = 240;
    ViewPager mViewPager;
    private View previousView;
    private View currentView;
    private GestureDetector gestureDetector;
    private int currentTab;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = { "Top Rated", "Games", "Movies" };
    TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookings_tab);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        viewPager.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() {
                Toast.makeText(getApplicationContext(), "right 2 ", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                Toast.makeText(getApplicationContext(), "left 2", Toast.LENGTH_SHORT).show();
            }
        });
        /*
        tabHost = (TabHost) findViewById(android.R.id.tabhost);//getTabHost();
        LocalActivityManager lsm = new LocalActivityManager(this, false);
        lsm.dispatchResume();
        tabHost.setup(lsm);

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Прошедшие");

        Intent intent1 = new Intent(this, BookingsTab.class);
        intent1.setData(Uri.parse("tab:" + Integer.valueOf(0).toString()));
        tabSpec.setContent(intent1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Текущие");
        Intent intent2 = new Intent(this, BookingsTab.class);
        intent2.setData(Uri.parse("tab:" + Integer.valueOf(1).toString()));
        tabSpec.setContent(intent2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Будущие");
        Intent intent3 = new Intent(this, BookingsTab.class);
        intent3.setData(Uri.parse("tab:" + Integer.valueOf(2).toString()));
        tabSpec.setContent(intent3);
        tabHost.addTab(tabSpec);


        tabHost.setOnTabChangedListener(this);

        FrameLayout fl = (FrameLayout) findViewById(android.R.id.tabcontent);
        fl.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeRight() {
                Toast.makeText(getApplicationContext(), "right 2 ", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(getApplicationContext(), "left 2", Toast.LENGTH_SHORT).show();
            }
        });

        //for (int i=0; i<tabHost.getChildCount(); i++) {
        //    if (tabHost.getChildAt(i) instanceof TextView) {
        //        ((TextView)tabHost.getChildAt(i)).setTypeface(Typeface.createFromAsset(this.getAssets(), StringExtension.DEFAULT_FONT));
        //    }
        //}
*/
    }

    private int moveLeft(){
        if (currentTab > 0)
            currentTab--;
        return currentTab;
    }

    private int moveRight(){
        if (currentTab < 2)
            currentTab++;
        return currentTab;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tabId, FragmentTransaction ds)
    {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }


}


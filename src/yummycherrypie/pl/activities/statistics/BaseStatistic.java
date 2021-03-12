package yummycherrypie.pl.activities.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import yummycherrypie.base_classes.BookingType;
import yummycherrypie.base_classes.Statistic;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.default_records.BookingTypesDefaultRecords;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.dal.repositories.BookingTypeRepository;
import yummycherrypie.pl.Colors;
import yummycherrypie.pl.CurrencyExtension;

/**
 * Created by Nikolay_Piskarev on 12/9/2015.
 */
public class BaseStatistic extends Fragment {

    protected TextView tvMonth;
    protected TextView tvYear;
    protected TextView tvProceeds;
    protected TextView tvCakePricesSum;
    protected TextView tvRecipePricesSum;
    protected TextView tvCountBookingsSum;
    protected TextView tvCountBookingMenSum;
    protected PieChart pieSumsChart;
    protected PieChart pieProductsChart;
    protected CombinedChart combinedChart;

    protected TextView tvCupcakes;
    protected TextView tvCakepops;
    protected TextView tvCookies;
    protected TextView tvPastry;
    protected TextView tvSpiceCake;
    protected TextView tvСakes;

    protected DBHelper dbHelper;
    protected BookingRepository br;

    protected static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    /**
     * 0 - показывать только прошедшие заказы
     * 1 - показывать текущие и будущие заказы за текущий месяц
     * 2 - показывать будущие заказы (не включая текущий месяц)
     * */
    protected int pageNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

    }

    protected void setPieSummaryStatistic(PieChart pieChart, String description, Statistic s){
        float percentHoleRadius = 40;
        float circleRadius = 0;
        float rotationAngle = 50;
        float sliceSpace = 3;
        float sectionShift = 10;
        int[] colors = new int[] {Colors.PINK, Colors.ORANGE, Colors.YELLOW, Colors.LIGHT_GREEN,
                Colors.DARK_GREEN, Colors.BLUE, Colors.MAGENTA};

        String[] xValues = { "Доходы", "Расходы" };

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(Double.valueOf(s.getSumCakePrice()).floatValue(), 0));
        yVals.add(new Entry(Double.valueOf(s.getSumRecipePrice()).floatValue(), 1));

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);
        float textSize = 16;

        BasePieChart.setValues(pieChart, description, percentHoleRadius,
                circleRadius, rotationAngle,
                sliceSpace, sectionShift, colors,
                yVals, xVals, textSize);
    }


    protected void setPieBookingTypeStatistic(PieChart pieChart, String description, Statistic s){
        float percentHoleRadius = 40;
        float circleRadius = 0;
        float rotationAngle = 50;
        Legend.LegendPosition lp = Legend.LegendPosition.ABOVE_CHART_RIGHT;
        float legendXEntrySpace = 7;
        float legendYEntrySpace = 5;
        float sliceSpace = 3;
        float sectionShift = 10;
        int[] colors = new int[] {Colors.PINK, Colors.ORANGE, Colors.YELLOW, Colors.LIGHT_GREEN,
                Colors.DARK_GREEN, Colors.BLUE, Colors.MAGENTA};

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        BookingTypeRepository btr = new BookingTypeRepository(new DBHelper(getActivity()));
        ArrayList<BookingType> bts = btr.getAllBookingTypes();

        HashMap<String, Float> hmap = new HashMap<String, Float>();

        String cakeName = "";

        for (int i = 0; i < bts.size(); i++){
            BookingType current = bts.get(i);
            float f = 0;
            try {
                f = Integer.valueOf(s.getCountBookingTypesBookings(current.getId())).floatValue();
            }
            catch (NullPointerException e){
                f = 0;
            }
            if (f > 0) {
                if (current.getId() == BookingTypesDefaultRecords.BOOKING_TYPE_MARRIAGE_ID){
                    hmap.put(cakeName, f + getZeroInsteadNull(hmap.get(cakeName)));
                }
                else {
                    hmap.put(current.getName(), f);
                }
            }
            if (current.getId() == BookingTypesDefaultRecords.BOOKING_TYPE_CAKE_ID){
                cakeName = current.getName();
            }
        }
        btr.close();

        hmap = sortByValues(hmap);

        int i = 0;
        for (String key : hmap.keySet()){
            xVals.add(key);
            yVals.add(new Entry(hmap.get(key), i));
            i++;
        }

        float textSize = 16;

        BasePieChart.setValues(pieChart, description, percentHoleRadius,
                circleRadius, rotationAngle,
                sliceSpace, sectionShift, colors,
                yVals, xVals, textSize);
    }

    protected void setLabels(Statistic s){
        tvProceeds.setText(CurrencyExtension.getCurrencyWithoutRouble(s.getSumCakePrice() - s.getSumRecipePrice()));
        tvCakePricesSum.setText(CurrencyExtension.getCurrencyWithoutRouble(s.getSumCakePrice()));
        tvRecipePricesSum.setText(CurrencyExtension.getCurrencyWithoutRouble(s.getSumRecipePrice()));
        tvCountBookingsSum.setText(String.valueOf(s.getCountBookings()));
        tvCountBookingMenSum.setText(String.valueOf(s.getCountBookingMen()));

        tvCupcakes.setText(String.format("%d шт.", getZeroInsteadNull(s.getCountProducts().get(BookingTypesDefaultRecords.BOOKING_TYPE_CUPCAKE_ID))));
        tvCakepops.setText(String.format("%d шт.", getZeroInsteadNull(s.getCountProducts().get(BookingTypesDefaultRecords.BOOKING_TYPE_CAKEPOPS_ID))));
        tvCookies.setText(String.format("%d шт.", getZeroInsteadNull(s.getCountProducts().get(BookingTypesDefaultRecords.BOOKING_TYPE_COOKIE_ID))));
        tvPastry.setText(String.format("%d шт.", getZeroInsteadNull(s.getCountProducts().get(BookingTypesDefaultRecords.BOOKING_TYPE_PASTRY_ID))));
        tvSpiceCake.setText(String.format("%d шт.", getZeroInsteadNull(s.getCountProducts().get(BookingTypesDefaultRecords.BOOKING_TYPE_SPICE_CAKE_ID))));
        tvСakes.setText(String.format("%d шт.", getZeroInsteadNull(s.getCountProducts().get(BookingTypesDefaultRecords.BOOKING_TYPE_CAKE_ID))
                + getZeroInsteadNull(s.getCountProducts().get(BookingTypesDefaultRecords.BOOKING_TYPE_MARRIAGE_ID))));
    }

    private int getZeroInsteadNull(Integer value){
        if (value == null)
            return 0;
        else
            return value;
    }

    private float getZeroInsteadNull(Float value){
        if (value == null)
            return 0;
        else
            return value;
    }

    protected LineData generateLineData(ArrayList<Entry> entriesLine) {

        LineData d = new LineData();

        LineDataSet lineDataSet = new LineDataSet(entriesLine, "Доход");
        lineDataSet.setColor(Colors.PINK);
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(Color.rgb(240, 238, 70));
        lineDataSet.setCircleSize(5f);
        lineDataSet.setFillColor(Color.rgb(240, 238, 70));
        lineDataSet.setDrawCubic(true);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(Color.rgb(240, 238, 70));

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(lineDataSet);

        return d;
    }

    protected BarData generateBarData(ArrayList<BarEntry> entriesBar) {

        BarData d = new BarData();

        BarDataSet barDataSet = new BarDataSet(entriesBar, "Расход");
        barDataSet.setColor(Colors.BLUE);
        barDataSet.setValueTextColor(Color.rgb(60, 220, 78));
        barDataSet.setValueTextSize(10f);
        d.addDataSet(barDataSet);

        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }

    protected void setCombientStatistic(CombinedChart combinedChart, String description, ArrayList<String> periods,  ArrayList<Entry> entriesLine, ArrayList<BarEntry> entriesBar){
        try {
            combinedChart.setDescription(description);

            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<String> vals = periods;
            for (int i = 0; i < vals.size(); i++)
                labels.add(vals.get(i));

            CombinedData combinedData = new CombinedData(labels);
            combinedData.setData(generateLineData(entriesLine));
            combinedData.setData(generateBarData(entriesBar));

            //анимация баров
            //combinedChart.animateXY(2000, 3000);

            combinedChart.setData(combinedData);
            combinedChart.invalidate();
        }
        catch (Exception e){
            Toast.makeText(getActivity(), "Что-то не так в BaseStatistic.setCombientStatistic", Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }
}

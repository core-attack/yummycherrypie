package yummycherrypie.pl.activities.statistics;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import yummycherrypie.pl.Colors;

/**
 * Created by Nikolay_Piskarev on 12/8/2015.
 */
public class BasePieChart {

    public static void setValues(PieChart chart,
                                 String description,
                                 float percentHoleRadius,
                                 float circleRadius,
                                 float rotationAngle,
                                 float sliceSpace,
                                 float sectionShift,
                                 int[] colors,
                                 ArrayList<Entry> yVals,
                                 ArrayList<String> xVals,
                                 float textSize
                                 ){
        chart.setDescription(description);

        chart.setUsePercentValues(true);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColorTransparent(true);
        chart.setHoleRadius(percentHoleRadius);
        chart.setTransparentCircleRadius(circleRadius);

        chart.setRotationAngle(rotationAngle);
        chart.setRotationEnabled(false);

        Legend l = chart.getLegend();
        l.setEnabled(false);
        //l.setPosition(lp);
        //l.setXEntrySpace(legendXEntrySpace);
        //l.setYEntrySpace(legendYEntrySpace);

        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setSliceSpace(sliceSpace);
        dataSet.setSelectionShift(sectionShift);
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(textSize);
        data.setValueTextColor(Colors.COLOR_STATISTIC_TEXT);

        chart.setData(data);

        chart.highlightValues(null);

        //анимация
        //chart.spin( 10000 /*время анимации*/, 0, -360f, Easing.EasingOption.EaseInOutQuart);

        chart.invalidate();

    }
}

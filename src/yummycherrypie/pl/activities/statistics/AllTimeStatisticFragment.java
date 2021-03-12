package yummycherrypie.pl.activities.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.Statistic;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.system.R;

public class AllTimeStatisticFragment extends BaseStatistic {

    ArrayList<Date> dateList;

    public static AllTimeStatisticFragment newInstance(int page) {
        AllTimeStatisticFragment pageFragment = new AllTimeStatisticFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_time_statistic, null);

        tvProceeds = (TextView) view.findViewById(R.id.tvProceeds);
        tvCakePricesSum = (TextView) view.findViewById(R.id.tvCakePricesSum);
        tvRecipePricesSum = (TextView) view.findViewById(R.id.tvRecipePricesSum);
        tvCountBookingsSum = (TextView) view.findViewById(R.id.tvCountBookingsSum);
        tvCountBookingMenSum = (TextView) view.findViewById(R.id.tvCountBookingMenSum);
        tvCakepops  = (TextView) view.findViewById(R.id.tvCakepops);
        tvCookies = (TextView) view.findViewById(R.id.tvCookies);
        tvPastry = (TextView) view.findViewById(R.id.tvPastry);
        tvSpiceCake = (TextView) view.findViewById(R.id.tvSpiceCake);
        tvСakes = (TextView) view.findViewById(R.id.tvСakes);
        tvCupcakes  = (TextView) view.findViewById(R.id.tvCupcakes);
        pieSumsChart = (PieChart) view.findViewById(R.id.chartPieStatistic);
        pieProductsChart = (PieChart) view.findViewById(R.id.chartPieProductsStatistic);
        combinedChart = (CombinedChart) view.findViewById(R.id.chartCombientStatistic);

        dbHelper = new DBHelper(getActivity());
        br = new BookingRepository(dbHelper);

        setStatistics();
        return view;
    }

    private void setStatistics(){
        Statistic s = br.getStatisticPerAllTime();

        setPieSummaryStatistic(pieSumsChart, "Соотношение прибыли и издержек за всё время", s);

        setPieBookingTypeStatistic(pieProductsChart, "Распределение заказов по типам за всё время", s);

        setLabels(s);

        Booking first = br.getFirstBooking();
        Booking last = br.getLastBooking();

        ArrayList<Entry> entriesLine = new ArrayList<Entry>();
        ArrayList<BarEntry> entriesBar = new ArrayList<BarEntry>();
        dateList = new ArrayList<Date>();

        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        begin.setTime(new Date(first.getDateLong()));
        end.setTime(new Date(last.getDateLong()));
        Calendar counter = begin;
        int countBars = 0;
        for (int year = begin.get(Calendar.YEAR); year <= end.get(Calendar.YEAR); year++){
            int beginMonth = 0;
            int endMonth = 0;
            if (year == begin.get(Calendar.YEAR) && year < end.get(Calendar.YEAR)) {
                beginMonth = begin.get(Calendar.MONTH);
                endMonth = counter.getActualMaximum(Calendar.MONTH);
            }
            else if (year == end.get(Calendar.YEAR) && year > begin.get(Calendar.YEAR)){
                beginMonth = counter.getActualMinimum(Calendar.MONTH);
                endMonth = end.get(Calendar.MONTH);
            }
            else if (year > begin.get(Calendar.YEAR) && year < end.get(Calendar.YEAR)){
                beginMonth = counter.getActualMinimum(Calendar.MONTH);
                endMonth = counter.getActualMaximum(Calendar.MONTH);
            } else if (begin.get(Calendar.YEAR) == end.get(Calendar.YEAR)){
                beginMonth = begin.get(Calendar.MONTH);
                endMonth = end.get(Calendar.MONTH);
            }

            for (int month = beginMonth; month <= endMonth; month++) {
                counter.set(Calendar.YEAR, year);
                counter.set(Calendar.MONTH, month);
                counter.set(Calendar.DAY_OF_MONTH, counter.getActualMinimum(Calendar.DAY_OF_MONTH));
                dateList.add(counter.getTime());
                double[] monthSums = br.getSumsBookingPerMonth(counter.getTime());
                entriesLine.add(new Entry(Double.valueOf(monthSums[0]).floatValue(), countBars));
                entriesBar.add(new BarEntry(Double.valueOf(monthSums[1]).floatValue(), countBars));
                countBars++;
            }
        }

        setCombientStatistic(combinedChart, "Динамика доходов и издержек за всё время",
                            br.getAllMonthes(new Date(first.getDateLong()), new Date(last.getDateLong())),
                            entriesLine,
                            entriesBar);

        combinedChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Date d = dateList.get(h.getXIndex());
                Statistic s = br.getStatisticPerMonth(d);
                setLabels(s);
                setPieSummaryStatistic(pieSumsChart, "Соотношение прибыли и издержек за выбранный месяц", s);
                setPieBookingTypeStatistic(pieProductsChart, "Распределение заказов по типам за выбранный месяц", s);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (br != null)
            br.close();
    }
}

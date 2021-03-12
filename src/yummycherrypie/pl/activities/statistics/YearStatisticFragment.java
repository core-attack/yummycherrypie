package yummycherrypie.pl.activities.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import yummycherrypie.base_classes.Statistic;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.system.R;

public class YearStatisticFragment extends BaseStatistic {

    TextView tvYear;

    Calendar currentCalendar;

    ArrayList<Date> dateList;

    public static YearStatisticFragment newInstance(int page) {
        YearStatisticFragment pageFragment = new YearStatisticFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.year_statistic, null);

        currentCalendar = Calendar.getInstance();

        tvYear = (TextView) view.findViewById(R.id.title);
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

        LinearLayout previous  = (LinearLayout) view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentCalendar.get(Calendar.YEAR) > currentCalendar.getActualMinimum(Calendar.YEAR)) {
                    currentCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR) - 1);
                }
                setStatistics();
            }
        });

        LinearLayout next  = (LinearLayout) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentCalendar.get(Calendar.YEAR) < currentCalendar.getActualMaximum(Calendar.YEAR)) {
                    currentCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR) + 1);
                }
                setStatistics();
            }
        });

        setStatistics();
        return view;
    }

    private void setStatistics(){

        tvYear.setText(String.format("%d", currentCalendar.get(Calendar.YEAR)));

        Statistic s = br.getStatisticPerYear(currentCalendar.get(Calendar.YEAR));

        setPieSummaryStatistic(pieSumsChart, "Соотношение прибыли и издержек за год", s);

        setPieBookingTypeStatistic(pieProductsChart, "Распределение заказов по типам за год", s);

        setLabels(s);

        String[] vals = new String[]{"Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"};
        ArrayList<String> periods = new ArrayList<String>();
        for (int i = 0; i < vals.length; i++){
            periods.add(vals[i]);
        }

        ArrayList<Entry> entriesLine = new ArrayList<Entry>();
        ArrayList<BarEntry> entriesBar = new ArrayList<BarEntry>();
        dateList = new ArrayList<Date>();

        Calendar date = currentCalendar;
        for (int month = currentCalendar.getActualMinimum(Calendar.MONTH); month <= currentCalendar.getActualMaximum(Calendar.MONTH); month++){
            date.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));
            date.set(Calendar.MONTH, month);
            date.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH));
            dateList.add(date.getTime());
            double[] monthSums = br.getSumsBookingPerMonth(date.getTime());
            entriesLine.add(new Entry(Double.valueOf(monthSums[0]).floatValue(), month));
            entriesBar.add(new BarEntry(Double.valueOf(monthSums[1]).floatValue(), month));
        }

        setCombientStatistic(combinedChart, "Динамика доходов и издержек за год",
                periods,
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

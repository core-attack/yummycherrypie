package yummycherrypie.pl.activities.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.Calendar;

import yummycherrypie.base_classes.Statistic;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.system.R;

/**
 * Created by CoreAttack on 03.12.2015.
 */
public class MonthStatisticFragment extends BaseStatistic {

    TextView tvMonth;

    Calendar currentCalendar;

    public static MonthStatisticFragment newInstance(int page) {
        MonthStatisticFragment pageFragment = new MonthStatisticFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_statistic, null);
        currentCalendar = Calendar.getInstance();

        tvMonth = (TextView) view.findViewById(R.id.title);
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
                if (currentCalendar.get(Calendar.MONTH) == currentCalendar.getActualMinimum(Calendar.MONTH)) {
                    currentCalendar.set((currentCalendar.get(Calendar.YEAR) - 1), currentCalendar.getActualMaximum(Calendar.MONTH), 1);
                } else {
                    currentCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH) - 1);
                }
                setStatistics(currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.YEAR));
            }
        });

        LinearLayout next  = (LinearLayout) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (currentCalendar.get(Calendar.MONTH) == currentCalendar.getActualMaximum(Calendar.MONTH)) {
                    currentCalendar.set((currentCalendar.get(Calendar.YEAR) + 1), currentCalendar.getActualMinimum(Calendar.MONTH), 1);
                } else {
                    currentCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH) + 1);
                }
                setStatistics(currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.YEAR));

            }
        });

        setStatistics(currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.YEAR));
        return view;
    }

    private void setStatistics(int month, int year){

        tvMonth.setText(String.format("%s %d",
                DateExtension.MONTHS[month],
                year));
        Statistic s = br.getStatisticPerMonth(currentCalendar.getTime());

        setPieSummaryStatistic(pieSumsChart, "Соотношение прибыли и издержек за месяц", s);

        setPieBookingTypeStatistic(pieProductsChart, "Распределение заказов по типам за месяц", s);

        setLabels(s);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (br != null)
            br.close();
    }
}

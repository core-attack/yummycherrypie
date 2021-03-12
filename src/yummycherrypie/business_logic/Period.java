package yummycherrypie.business_logic;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by piskarev on 11.09.2015.
 * Период времени
 */
public class Period {

    /* закрытие поля ниже */

    private long _startDate = 0;
    private long _endDate = 0;

    /* геттеры-сеттеры ниже */

    public long GetStartDateLong() {
        return _startDate;
    }

    public Date GetStartDate() {
        Date result = new Date();
        result.setTime(_startDate);
        return result;
    }

    public void SetStartDate(long _startDate) {
        this._startDate = _startDate;
    }

    public long GetEndDateLong() {
        return _endDate;
    }

    public Date GetEndDate() {
        Date result = new Date();
        result.setTime(_endDate);
        return result;
    }

    public void SetEndDate(long _endDate) {
        this._endDate = _endDate;
    }

    /* конструкторы ниже */

    public Period(long start, long end){
        _startDate = start;
        _endDate = end;
    }

    public Period(Date start, Date end){
        SetStartDate(start.getTime());
        SetEndDate(end.getTime());
    }

    /**
     *	Возвращает период, в качестве границ которого начальная и конечная даты месяца
     */
    public static Period getBorderOfMonth(Date d){
        Calendar current = Calendar.getInstance();
        current.setTime(d);

        Calendar calendarBegin = Calendar.getInstance();
        calendarBegin.set(Calendar.YEAR, current.get(Calendar.YEAR));
        calendarBegin.set(Calendar.MONTH, current.get(Calendar.MONTH));
        calendarBegin.set(Calendar.DAY_OF_MONTH, calendarBegin.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendarBegin.set(Calendar.HOUR_OF_DAY, calendarBegin.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendarBegin.set(Calendar.MINUTE, calendarBegin.getActualMinimum(Calendar.MINUTE));
        calendarBegin.set(Calendar.SECOND, calendarBegin.getActualMinimum(Calendar.SECOND));
        calendarBegin.set(Calendar.MILLISECOND, calendarBegin.getActualMinimum(Calendar.MILLISECOND));
        Date begin = calendarBegin.getTime();

        Calendar calendarEnd =  Calendar.getInstance();
        calendarEnd.set(Calendar.YEAR, current.get(Calendar.YEAR));
        calendarEnd.set(Calendar.MONTH, current.get(Calendar.MONTH));
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarEnd.set(Calendar.HOUR_OF_DAY, calendarEnd.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendarEnd.set(Calendar.MINUTE, calendarEnd.getActualMaximum(Calendar.MINUTE));
        calendarEnd.set(Calendar.SECOND, calendarEnd.getActualMaximum(Calendar.SECOND));
        calendarEnd.set(Calendar.MILLISECOND, calendarEnd.getActualMaximum(Calendar.MILLISECOND));
        Date end = calendarEnd.getTime();

        return new Period(begin, end);
    }

    /**
     * Возвращает временные границы суток
     */
    public static Period getBorderOfDay(Date d){
        Calendar current = Calendar.getInstance();
        current.setTime(d);

        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.YEAR, current.get(Calendar.YEAR));
        begin.set(Calendar.MONTH, current.get(Calendar.MONTH));
        begin.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        begin.set(Calendar.HOUR_OF_DAY, begin.getActualMinimum(Calendar.HOUR_OF_DAY));
        begin.set(Calendar.MINUTE, begin.getActualMinimum(Calendar.MINUTE));
        begin.set(Calendar.SECOND, begin.getActualMinimum(Calendar.SECOND));
        begin.set(Calendar.MILLISECOND, begin.getActualMinimum(Calendar.MILLISECOND));


        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, current.get(Calendar.YEAR));
        end.set(Calendar.MONTH, current.get(Calendar.MONTH));
        end.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, end.getActualMaximum(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, end.getActualMaximum(Calendar.MINUTE));
        end.set(Calendar.SECOND, end.getActualMaximum(Calendar.SECOND));
        end.set(Calendar.MILLISECOND, end.getActualMaximum(Calendar.MILLISECOND));

        return new Period(begin.getTimeInMillis(), end.getTimeInMillis());
    }

    /**
     * Возвращает временные границы суток
     */
    public static Period getBorderOfDay(long d){
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(d);

        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.YEAR, current.get(Calendar.YEAR));
        begin.set(Calendar.MONTH, current.get(Calendar.MONTH));
        begin.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        begin.set(Calendar.HOUR_OF_DAY, begin.getActualMinimum(Calendar.HOUR_OF_DAY));
        begin.set(Calendar.MINUTE, begin.getActualMinimum(Calendar.MINUTE));
        begin.set(Calendar.SECOND, begin.getActualMinimum(Calendar.SECOND));
        begin.set(Calendar.MILLISECOND, begin.getActualMinimum(Calendar.MILLISECOND));


        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, current.get(Calendar.YEAR));
        end.set(Calendar.MONTH, current.get(Calendar.MONTH));
        end.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, end.getActualMaximum(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, end.getActualMaximum(Calendar.MINUTE));
        end.set(Calendar.SECOND, end.getActualMaximum(Calendar.SECOND));
        end.set(Calendar.MILLISECOND, end.getActualMaximum(Calendar.MILLISECOND));

        return new Period(begin.getTimeInMillis(), end.getTimeInMillis());
    }

    public static Period getBorderOfYear(int year){
        Calendar begin =  Calendar.getInstance();
        begin.set(Calendar.YEAR, year);
        begin.set(Calendar.MONTH, begin.getActualMinimum(Calendar.MONTH));
        begin.set(Calendar.DAY_OF_MONTH, begin.getActualMinimum(Calendar.DAY_OF_MONTH));
        begin.set(Calendar.HOUR_OF_DAY, begin.getActualMinimum(Calendar.HOUR_OF_DAY));
        begin.set(Calendar.MINUTE, begin.getActualMinimum(Calendar.MINUTE));
        begin.set(Calendar.SECOND, begin.getActualMinimum(Calendar.SECOND));
        begin.set(Calendar.MILLISECOND, begin.getActualMinimum(Calendar.MILLISECOND));

        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, year);
        end.set(Calendar.MONTH, end.getActualMaximum(Calendar.MONTH));
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.HOUR_OF_DAY, end.getActualMaximum(Calendar.HOUR_OF_DAY));
        end.set(Calendar.MINUTE, end.getActualMaximum(Calendar.MINUTE));
        end.set(Calendar.SECOND, end.getActualMaximum(Calendar.SECOND));
        end.set(Calendar.MILLISECOND, end.getActualMaximum(Calendar.MILLISECOND));

        return new Period(begin.getTime(), end.getTime());
    }

}

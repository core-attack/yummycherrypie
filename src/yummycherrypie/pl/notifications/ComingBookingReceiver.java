package yummycherrypie.pl.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by CoreAttack on 25.12.2015.
 */
public class ComingBookingReceiver extends BroadcastReceiver {

    private final int REMIND_AT_HOUR_OF_DAY = 10;
    private final int REMIND_AT_MINUTE = 0;
    private final int REMIND_AT_SECOND = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, REMIND_AT_HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, REMIND_AT_MINUTE);
        calendar.set(Calendar.SECOND, REMIND_AT_SECOND);

        if (calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
            PendingIntent pi = PendingIntent.getService(context, 0,
                    new Intent(context, BaseNotification.class),PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pi);
        }*/
        //Intent intentStart = new Intent(context, MainActivity.class);
        //intentStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intentStart);

        Toast.makeText(context, "tick", Toast.LENGTH_SHORT).show();


    }
}
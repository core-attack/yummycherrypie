package yummycherrypie.pl.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import yummycherrypie.pl.activities.MainActivity;
import yummycherrypie.system.R;

public class BaseNotification extends Service {

    NotificationManager nm;

    public BaseNotification() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendNotif();
        return Service.START_STICKY;
    }

    void sendNotif() {
        // 1-я часть
        /*Notification notif = new Notification(R.drawable.icon, "Text in status bar",
                System.currentTimeMillis());

        // 3-я часть
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(MainActivity.FILE_NAME, "somefile");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 2-я часть
        notif.setLatestEventInfo(this, "BaseNotification1's title", "BaseNotification1's text", pIntent);

        // ставим флаг, чтобы уведомление пропало после нажатия
        //notif.flags |= BaseNotification1.START_STICKYFLAG_AUTO_CANCEL;
        notif.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;

        Notification.Builder d = new Notification.Builder();
        // отправляем
        nm.notify(1, notif);
        */
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("My Ticker")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setLights(0xff00ff00, 300, 100)
                .setContentTitle("My Title 1")
                .setContentText("My Text 1");
        nm.notify(0, builder.getNotification());
    }
}

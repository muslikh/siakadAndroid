package id.codemerindu.siakad;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class getPesan extends FirebaseMessagingService {

    private static final String TAG ="Test Test";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null)
        {
            sendNotif(remoteMessage);
        }
    }

    private void sendNotif(RemoteMessage remoteMessage)
    {
        Map<String ,String >data = remoteMessage.getData();
        String title = data.get("Judul");
        String content = data.get("Message");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.notif);
        String NOTIFICATION_CHANNEL_ID = "id1";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My notofications", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setDescription("deskripsi");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
//            notificationChannel.enableVibration(true);
//            notificationChannel.setShowBadge(true);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
        NotificationCompat.Builder notBuilder = new NotificationCompat.Builder(getApplicationContext(),NOTIFICATION_CHANNEL_ID);
        notBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setCustomContentView(contentView)
                .setSmallIcon(R.mipmap.ic_logo)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
//                .setStyle(new android.support.v7.app.ActionBar.DisplayOptions())

                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))
                .setContentText(content)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0))
                .setColor(getResources().getColor(R.color.colorPrimaryDark));
        notificationManager.notify(1,notBuilder.build());
    }
}

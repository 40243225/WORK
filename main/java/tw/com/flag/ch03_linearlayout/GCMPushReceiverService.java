package tw.com.flag.ch03_linearlayout;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by 40243_000 on 2016/7/10.
 */
public class GCMPushReceiverService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        sendNotification(message);

    }
    private void sendNotification(String message) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 30;//Your request code
        int defaults=0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        //Setup notification
        //Sound
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Build notification
        defaults |= Notification.DEFAULT_VIBRATE;
        long[] vibrate_effect =
                {500,200, 500,200,500,200, 500,200,};

        int i=0;
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this);
        if(message.equals("0"))
        {
            i=0;

            noBuilder
                    .setContentTitle("Warning")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setVibrate(vibrate_effect).setContentText("藥盒被開啟了!!").setSmallIcon(R.drawable.warnning).setLights(Color.RED, 1000, 1000);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(i, noBuilder.build());
            Intent in= new Intent(this,GCM.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("temp",i);
            startActivity(in);
        }

        else if(message.equals("1"))
        {
            i=1;
            noBuilder
                    .setContentTitle("Warning")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent).setContentText("家中老年人還沒吃藥唷").setSmallIcon(R.drawable.warnning).setLights(Color.RED, 1000, 1000);;
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(i, noBuilder.build());
            Intent in= new Intent(this,GCM.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("temp", i);
            startActivity(in);

        }
        else if(message.equals("2")) {
            i = 2;
            noBuilder
                    .setContentTitle("Notification")
                    .setAutoCancel(true).setContentIntent(pendingIntent)
                    .setContentText("家中老年人以確認吃藥了").setSmallIcon(R.drawable.m).setLights(Color.GREEN, 1000, 1000);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(i, noBuilder.build());
            Intent in= new Intent(this,GCM.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("temp", i);
            startActivity(in);
        }
        else if(message.equals("3")) {
            i = 3;
            noBuilder
                    .setContentTitle("Warning")
                    .setAutoCancel(true).setContentIntent(pendingIntent)
                    .setContentText("藥物的重量不對，請確認").setSmallIcon(R.drawable.warnning).setLights(Color.RED, 1000, 1000);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(i, noBuilder.build());
            Intent in= new Intent(this,GCM.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra("temp",i);
            startActivity(in);
        }

        //0 = ID of notification




    }

}


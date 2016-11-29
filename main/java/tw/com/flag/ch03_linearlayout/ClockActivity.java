package tw.com.flag.ch03_linearlayout;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by 40243_000 on 2016/9/9.
 */
public class ClockActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    Bundle bundle;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent,pi[];
    private Vibrator myVibrator;
    private Intent intent;
    int i;
    String i1;
    long c;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pi=new PendingIntent[100];
        bundle = getIntent().getExtras();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        intent = new Intent(this, Clock.class);

        pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        //
        i1 = bundle.getString("msg");
        i = Integer.valueOf(i1);
        //重設時間
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ClockActivity.class);
        intent.putExtra("msg",i1);
        pi[i] = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        c.set(c.get(c.YEAR),c.get(c.MONTH), c.get(Calendar.DAY_OF_MONTH)+1);
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi[i]);
        Log.e("重複明天時間",c.getTime()+"");

        //震動
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(new long[]{500,200, 500,200,500,200, 500,200}, 2);
        //音樂
        mediaPlayer = mediaPlayer.create(this,R.raw.pig);
        mediaPlayer.start();
        dialog();


    }

    private void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ClockActivity.this); //創建訊息方塊

        builder.setMessage("點擊查看是否已經吃藥");
        int j=i+1;
        builder.setTitle("第"+j+"次吃藥");
        builder.setCancelable(false);
        builder.setPositiveButton("查看", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss(); //dismiss為關閉dialog,Activity還會保留dialog的狀態
                mediaPlayer.stop();
                myVibrator.cancel();
                startActivity(intent);


            }

        });


        builder.create().show();

    }

}

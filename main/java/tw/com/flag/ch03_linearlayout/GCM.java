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
public class GCM extends AppCompatActivity {

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
        String m=null,t="Warnning";
        bundle= getIntent().getExtras();
        int temp=bundle.getInt("temp");
        if(temp==0)
        {
            m="藥盒在錯誤時間被開啟了!!";
        }
        else if(temp==1)
        {
            m="家中老年人還沒吃藥唷";
        }
        else if (temp==2)
        {
            m="家中老年人以確認吃藥了";
            t="Notification";
        }
        else if (temp==3)
        {
            m="藥物的重量不對，請重新確認";
        }
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(new long[]{500,200, 500,200,500,200, 500,200}, 2);
        //音樂

        dialog(t,m);


    }

    private void dialog(String title,String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(GCM.this); //創建訊息方塊
        intent=new Intent(this,Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setPositiveButton("查看", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss(); //dismiss為關閉dialog,Activity還會保留dialog的狀態
                myVibrator.cancel();
                startActivity(intent);


            }

        });


        builder.create().show();

    }

}

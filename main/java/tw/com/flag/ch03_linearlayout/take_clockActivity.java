package tw.com.flag.ch03_linearlayout;

/**
 * Created by 40243_000 on 10/28/2016.
 */

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;




public class take_clockActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator myVibrator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = mediaPlayer.create(this,R.raw.pig);
        mediaPlayer.start();
        myVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(new long[]{500,200, 500,200,500,200, 500,200}, 2);
        //创建一个闹钟提醒的对话框,点击确定关闭铃声与页面
        new AlertDialog.Builder(take_clockActivity.this).setTitle("鬧鐘").setMessage("君丞快起床~")
                .setPositiveButton("關閉鈴聲", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer.stop();
                        myVibrator.cancel();
                        take_clockActivity.this.finish();

                    }
                }).show();
    }
}


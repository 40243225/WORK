package tw.com.flag.ch03_linearlayout;

/**
 * Created by 40243_000 on 2016/8/29.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
/**
 * Created by Jay on 2015/10/25 0025.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bData = intent.getExtras();
        String i1=bData.getString("msg");
        String i2=bData.getString("msg2");
        Log.e(i2,i2);
        Intent i = new Intent(context, ClockActivity.class);
        Intent i0=new Intent(context, take_clockActivity.class);
        intent.putExtra("msg", i1);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(i2.equals("clock"))
            context.startActivity(i);
        else
            context.startActivity(i0);

    }
}
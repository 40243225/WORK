package tw.com.flag.ch03_linearlayout;

/**
 * Created by 40243_000 on 10/28/2016.
 */
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class takeMedicine extends AppCompatActivity {
    private Button doSetDate;
    private Button doSetTime;
    private TextView textDate;
    private TextView textTime;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private AlarmManager alarmManager;
    private PendingIntent pi;
    private Button btn_cancel;

    int s_year, s_hour, s_minute, s_month, s_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_layout);
        doFindView();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(takeMedicine.this, take_clockActivity.class);
        pi = PendingIntent.getActivity(takeMedicine.this, 20, intent, 0);
        GregorianCalendar calendar = new GregorianCalendar();
        datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textDate.setText(year + "/" +(monthOfYear + 1) + "/" + dayOfMonth);
                s_year = year;
                s_month = monthOfYear;
                s_day = dayOfMonth;
                timePickerDialog.show();

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH ), calendar.get(Calendar.DAY_OF_MONTH)

        );
        timePickerDialog = new TimePickerDialog(this, new OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textTime.setText((hourOfDay > 12 ? hourOfDay - 12 : hourOfDay)
                        + ":" + minute + " " + (hourOfDay > 12 ? "PM" : "AM"));
                s_hour = hourOfDay;
                s_minute = minute;
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(System.currentTimeMillis());
                c.set(Calendar.MONTH, s_month);
                c.set(Calendar.DAY_OF_MONTH, s_day);
                c.set(Calendar.HOUR_OF_DAY, s_hour);
                c.set(Calendar.MINUTE, s_minute);


                if (c.getTimeInMillis() < System.currentTimeMillis())//如果設置時間已過 設為明天響
                {
                    c.set(c.get(c.YEAR), c.get(c.MONTH), c.get(Calendar.DAY_OF_MONTH) + 1);
                }
                Log.e(String.valueOf(s_hour), String.valueOf(s_minute));
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                Log.e("HEHE", c.getTime() + "");   //这里的时间是一个unix时间戳
                Toast.makeText(takeMedicine.this, "設置成功~" + c.getTimeInMillis(),
                        Toast.LENGTH_SHORT).show();

            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE), false);
    }

    public void doFindView() {
        doSetDate = (Button) findViewById(R.id.buttonDate);
        textDate = (TextView) findViewById(R.id.datetext);
        textTime = (TextView) findViewById(R.id.timetext);
        btn_cancel=(Button) findViewById(R.id.btn_cancel);
    }

    public void setDate(View v) {
        datePickerDialog.show();
    }

    public void setTime(View v) {

    }
    public void cancel(View view) {
        alarmManager.cancel(pi);
        Toast.makeText(takeMedicine.this, "鬧鐘已取消", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.date, menu);
        return true;


    }
}

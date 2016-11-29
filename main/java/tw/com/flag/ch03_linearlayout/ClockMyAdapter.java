package tw.com.flag.ch03_linearlayout;

/**
 * Created by 40243_000 on 9/25/2016.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

public class ClockMyAdapter extends BaseAdapter {

    static String S_Time,S_N,S_No;
    private Context contx;
    private LayoutInflater inflater;
    String [] Time_H;
    String [] Time_M;
    String [] N;
    String[]En;
    String []No_array;
    Button revise,delete;
    String H,M,Time_String,cTime,method,n;;
    String JSON_STRING;
    String json_string,ip;

    private Intent intent;
    private AlarmManager alarmManager;
    private PendingIntent pi;
    private Activity mParentActivity;
    public ClockMyAdapter(Context c, Activity parentActivity, String [] time_h,String []time_m, String [] value, String []En, String []No_array){
        inflater = LayoutInflater.from(c);
        contx=c;
        mParentActivity=parentActivity;
        this.Time_H = time_h;
        this.Time_M = time_m;
        this.N = value;
        this.En=En;
        this.No_array=No_array;
        alarmManager = (AlarmManager) contx.getSystemService(contx.ALARM_SERVICE);
        intent = new Intent(contx, ClockActivity.class);
    }
    @Override
    public int getCount() {
        return Time_H.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int n;
        view = inflater.inflate(R.layout.eat_my,viewGroup,false);
        TextView Time2,N2,En2;
        revise = (Button) view.findViewById(R.id.revise);
        delete = (Button) view.findViewById(R.id.delete);
        Time2 = (TextView) view.findViewById(R.id.textTime4);
        En2=(TextView) view.findViewById(R.id.textEn);
        N2 = (TextView) view.findViewById(R.id.textN);
        Time2.setText(Time_H[i]+":"+Time_M[i]);
        N2.setText(N[i]);
        if(En[i].equals("0"))
            En2.setText("未吃藥");
        else {
            En2.setText("已吃藥");
            En2.setBackgroundColor(Color.GREEN);
        }
        revise.setOnClickListener(new LongClick(Time_H[i],N[i],No_array[i],i));
        delete.setOnClickListener(new Delete(No_array[i],i));
        return view;
    }
    //修改時間
    class LongClick extends AppCompatActivity implements View.OnClickListener {
        private String C_Time, C_N, C_No;
        private int i;
        public LongClick(String time, String n, String No,int i) {
            this.C_Time = time;
            this.C_N = n;
            this.C_No = No;
            this.i=i;
        }//用建構子獲得該item 的值

        @Override
        public void onClick(View view) {
            method ="revise";
            S_Time = C_Time;
            S_N = C_N;
            S_No = C_No;
            Calendar currentTime = Calendar.getInstance();
            new TimePickerDialog(contx, 0,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view,
                                              int hourOfDay, int minute) {
                            //设置当前时间
                            Calendar c = Calendar.getInstance();
                            c.setTimeInMillis(System.currentTimeMillis());
                            // 根据用户选择的时间来设置Calendar对象
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            H = TimeFix(hourOfDay);
                            M= TimeFix(minute);
                            c.set(Calendar.MINUTE, minute);
                            if(c.getTimeInMillis()<System.currentTimeMillis())//如果設置時間已過 設為明天響
                            {
                                c.set(c.get(c.YEAR),c.get(c.MONTH), c.get(Calendar.DAY_OF_MONTH)+1);
                            }
                            // ②设置AlarmManager在Calendar对应的时间启动Activity
                            cTime=String.valueOf(c.getTimeInMillis());
                            n=String.valueOf(i);
                            ClockBackgroundtask  backgroundTask =new ClockBackgroundtask (contx,mParentActivity);
                            backgroundTask.execute(method,H,M,cTime,n);
                            Toast.makeText(contx, "已修改時間為:"+H + ":" +M,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime
                    .get(Calendar.MINUTE), false).show();
        }
    }
    class Delete extends AppCompatActivity implements View.OnClickListener{
        private String C_No;
        private int i;
        public Delete(String No,int i){
            this.C_No=No;
            this.i=i;
        }//用建構子獲得該item 的值
        @Override
        public void onClick(View view) {

            pi = PendingIntent.getActivity(contx, i, intent,PendingIntent.FLAG_CANCEL_CURRENT);
            S_No=C_No;
            alarmManager.cancel(pi);
            pi=null;
            alarmManager=null;
            new GetJs().execute();
        }



    }
    class GetJs extends AsyncTask<Void,Void,String>
    {
        String json_url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Resources res=contx.getResources();
            ip=res.getString(R.string.ip);
            json_url=ip+"app105/deleteclock.php";
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("No","UTF-8")+"="+URLEncoder.encode(S_No,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                String reg;
                reg="1";
                return reg;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }





        @Override
        protected void onPostExecute(String result) {

            json_string=result;
            if(json_string.equals("1"))
            {
                Toast.makeText(contx,"取消成功!!", Toast.LENGTH_LONG).show();
                Intent intent =new Intent();
                intent.setClass(contx,Clock.class);
                contx.startActivity(intent);
            }
            else
            {
                Toast.makeText(contx,"取消失敗!!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private static String TimeFix(int c){
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}

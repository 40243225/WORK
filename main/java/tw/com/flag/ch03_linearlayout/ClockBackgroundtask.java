package tw.com.flag.ch03_linearlayout;

/**
 * Created by 40243_000 on 9/25/2016.
 */
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 40243_000 on 2016/7/6.
 */
public class ClockBackgroundtask extends AsyncTask<String,Void,String> {
    private Activity mParentActivity;
    AlertDialog alertDialog;
    Context ctx;
    String ip,set_url,revise_url;
    private AlarmManager alarmManager;
    private PendingIntent pi[];
    private Intent intent;
    long c,now;
    ClockBackgroundtask(Context ctx ,Activity parentActivity)
    {
        this.ctx=ctx;
        mParentActivity = parentActivity;
        alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        pi=new PendingIntent[100];

    }


    @Override

    protected void onPreExecute() {
        super.onPreExecute();
        Resources res=ctx.getResources();
        ip=res.getString(R.string.ip);
        set_url=ip+"app105/Setclock.php";
        revise_url=ip+"app105/reviseclock.php";
    }



    @Override
    protected String doInBackground(String... params) {


        String method=params[0];
        String Time_H=params[1];
        String Time_M=params[2];
        if(method.equals("set")) {

            try {
                String cTime=params[3];
                String i1=params[4];
                int i=Integer.valueOf(i1);
                c=Long.parseLong(cTime);
                now= System.currentTimeMillis();
                intent = new Intent(ctx, ClockActivity.class);
                intent.putExtra("msg",i1);
                intent.putExtra("msg2","clock");
                Log.e("i=",i+"");
                Log.e("now",now+"");
                URL url=new URL(set_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("Time_H","UTF-8")+"="+URLEncoder.encode(Time_H,"UTF-8")+"&"+
                        URLEncoder.encode("Time_M","UTF-8")+"="+URLEncoder.encode(Time_M,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                String reg;
                reg="set_s";
                return reg;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        else if(method.equals("revise"))
        {
            try {
                Log.e("hello","hello");
                String No=ClockMyAdapter.S_No;
                String cTime=params[3];
                String i1=params[4];
                int i=Integer.valueOf(i1);
                c=Long.parseLong(cTime);
                now= System.currentTimeMillis();
                intent = new Intent(ctx, ClockActivity.class);
                intent.putExtra("msg",i1);
                URL url=new URL(revise_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("Time_H","UTF-8")+"="+URLEncoder.encode(Time_H,"UTF-8")+"&"+
                        URLEncoder.encode("Time_M","UTF-8")+"="+URLEncoder.encode(Time_M,"UTF-8")+"&"+
                        URLEncoder.encode("No","UTF-8")+"="+URLEncoder.encode(No,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();
                String reg;
                reg="revise_s";
                return reg;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
    }
    protected void onPostExecute(String result) {
        String json_string=result;
        if(json_string.equals("set_s"))
        {

                Intent intent = new Intent();
                intent.setClass(mParentActivity,Clock.class);
                mParentActivity.startActivity(intent);

        }
        else if(json_string.equals("revise_s"))
        {
                Intent intent = new Intent();
                intent.setClass(mParentActivity, Clock.class);
                mParentActivity.startActivity(intent);

        }


    }
}


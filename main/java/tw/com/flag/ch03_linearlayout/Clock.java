package tw.com.flag.ch03_linearlayout;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Calendar;

public class Clock extends Activity {
    String No="Test001";
    String JSON_STRING;
    String json_string;

    Button btn_cancel;
    String H="12";
    String M="00";
    String method,Time,cTime;
    String i="0";
    String ip;
    public boolean onKeyDown(int keyCode,KeyEvent event){

        if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){   //確定按下退出鍵and防止重複按下退出鍵

            Intent intent = new Intent();
            intent.setClass(this,Main2Activity.class);
            startActivity(intent);

        }

        return false;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn_cancel =(Button)findViewById(R.id.btn_cancel);
        setContentView(R.layout.eat_medicine);
        new GetJs().execute();

    }
    public void setTime(View view)
    {
        method="set";

        Calendar currentTime = Calendar.getInstance();
        new TimePickerDialog(Clock.this, 0,
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
                        Time=H+":"+M;
                        c.set(Calendar.MINUTE, minute);
                        if(c.getTimeInMillis()<System.currentTimeMillis())//如果設置時間已過 設為明天響
                        {
                            c.set(c.get(c.YEAR),c.get(c.MONTH), c.get(Calendar.DAY_OF_MONTH)+1);
                        }
                        // ②设置AlarmManager在Calendar对应的时间启动Activity
                        cTime=String.valueOf(c.getTimeInMillis());
                        Log.e(cTime,"Time");
                        ClockBackgroundtask  backgroundTask =new ClockBackgroundtask (Clock.this,Clock.this);
                        backgroundTask.execute(method,H,M,cTime,i);
                        Toast.makeText(Clock.this, "已設定時間為:"+H + ":" +M,
                                Toast.LENGTH_SHORT).show();
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime
                .get(Calendar.MINUTE), false).show();


    }
    public void Display(String json_string)
    {

        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject=new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
            int count =0,n;
            String N="0",Enalbe,Time_H,Time_M,No;
            String N_array[]=new String[jsonArray.length()];
            String Enable_array[]=new String[jsonArray.length()];
            String Time_H_array[]=new String[jsonArray.length()];
            String Time_M_array[]=new String[jsonArray.length()];
            String No_array[]=new String[json_string.length()];
            while(count<jsonArray.length())
            {
                JSONObject JO=jsonArray.getJSONObject(count);
                N=JO.getString("Number");
                Enalbe=JO.getString("Enable");
                Time_H=JO.getString("Time_H");
                Time_M=JO.getString("Time_M");
                No=JO.getString("Time_No");
                N_array[count]=N;
                Enable_array[count]=Enalbe;
                Time_H_array[count]=Time_H;
                Time_M_array[count]=Time_M;
                No_array[count]=No;
                count++;
            }
            i=N;
            ListView ls;
            ls = (ListView) findViewById(R.id.Eat_listView);
            ls.setAdapter(new ClockMyAdapter(this,Clock.this,Time_H_array,Time_M_array,N_array,Enable_array,No_array));
        } catch (JSONException e) {
            i="0";
            e.printStackTrace();
        }
    }



    class GetJs extends AsyncTask<Void,Void,String>
    {
        String json_url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Resources res=Clock.this.getResources();
            ip=res.getString(R.string.ip);
            json_url=ip+"app105/Kitjson.php";
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String No1=No;
                URL url =new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("No","UTF-8")+"="+URLEncoder.encode(No1,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((JSON_STRING = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(JSON_STRING+="\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
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
            if(json_string==null)
            {
                Toast.makeText(getApplicationContext(),"Fuck?",Toast.LENGTH_LONG).show();
            }
            else
            {
                Display(json_string);
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

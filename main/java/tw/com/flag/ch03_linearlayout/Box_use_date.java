package tw.com.flag.ch03_linearlayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

/**
 * Created by 40243_000 on 10/27/2016.
 */
public class Box_use_date extends Activity {
    Bundle bundle ;
    String No="Test001";
    String date;
    String ip;
    String JSON_STRING,json_string;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_date_usage);
        bundle= getIntent().getExtras();
        date=bundle.getString("date");
        TextView date_text=(TextView)findViewById(R.id.textView5);
        date_text.setText("日期:"+date);
        new GetJs().execute();


    }
    public void Display(String json_string)
    {

        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject=new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
            int count =0;int ar1=0,ar2=1;
            String open_time,open_time_h,open_time_m;
            String open_weight,close_weight;
            String open_time_array[]=new String[jsonArray.length()];
            String open_weight_array[]=new String[jsonArray.length()];

            while(count<jsonArray.length())
            {
                JSONObject JO=jsonArray.getJSONObject(count);
                open_time_h=JO.getString("Open_Time_Hour");
                open_time_m=JO.getString("Open_Time_Min");
                if(open_time_h.length()==1)
                    open_time_h="0"+open_time_h;
                if(open_time_m.length()==1)
                    open_time_m="0"+open_time_m;
                open_time=open_time_h+":"+open_time_m;
                open_weight=JO.getString("Open_Weight");
                close_weight=JO.getString("Close_Weight");
                if(Integer.valueOf(open_time_h)>12)
                    open_time_array[count]=open_time+" PM";
                else
                    open_time_array[count]=open_time+" AM";
                open_weight_array[count]=open_weight+" g";
                count++;
            }
            ListView ls;

            ls = (ListView) findViewById(R.id.listView2);
            ls.setAdapter(new Box_use_Myadapter(this,open_time_array,open_weight_array));
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }



    class GetJs extends AsyncTask<Void,Void,String>
    {
        String json_url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Resources res=Box_use_date.this.getResources();
            ip=res.getString(R.string.ip);
            json_url=ip+"app105/box_date_json.php";
            Log.e("start","start");
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
                String data = URLEncoder.encode("box_number","UTF-8")+"="+URLEncoder.encode(No1,"UTF-8")+"&"+
                        URLEncoder.encode("Date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8");
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
}

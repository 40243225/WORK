package tw.com.flag.ch03_linearlayout;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
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
/**
 * Created by 40243_000 on 2016/8/3.
 */
public class HealthPassBookCode extends AppCompatActivity {
    String JSON_STRING;
    String json_string;
    int backCount = 0;
    String TDate,TNo,No,Disease;
    TextView txt_zero;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_pass_book_code);
        TDate=MyAdapter.Date;
        TNo=MyAdapter.No;
        No=Main2Activity.No;

        TextView txt_Dis =(TextView)findViewById(R.id.Disease);
        TextView txt_date =(TextView)findViewById(R.id.Date);

        Disease=MyAdapter.Disease;
        txt_date.setText("就醫日期:"+TDate);
        txt_Dis.setText("病名:"+Disease);

        new GetJs().execute();
    }
    public void Display(String json_string)
    {

        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            txt_zero =(TextView)findViewById(R.id.zero);
            txt_zero.setText("此次看診無任何服用藥物");
            jsonObject=new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
            int count =0;
            String No,DoctorCode,DoctorName;
            String Code[]=new String[jsonArray.length()];
            String Name[]=new String[jsonArray.length()];
            txt_zero.setText("");
            while(count<jsonArray.length())
            {
                JSONObject JO=jsonArray.getJSONObject(count);
                // No=JO.getString("No");
                DoctorCode=JO.getString("DoctorCode");
                Log.e("123",DoctorCode);
                DoctorName=JO.getString("DoctorName");
                Code[count]= DoctorCode;
                Name[count]=DoctorName;


                count++;
            }
            ListView ls;

            ls = (ListView) findViewById(R.id.listView1);
            ls.setAdapter(new MyAdapter2(this,Code,Name));

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

            Resources res=HealthPassBookCode.this.getResources();
            ip=res.getString(R.string.ip);
            json_url="http://140.130.35.74/app105/HealthCode_Json.php";
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
                String data = URLEncoder.encode("No","UTF-8")+"="+URLEncoder.encode(No1,"UTF-8")+"&"+
                        URLEncoder.encode("TNo","UTF-8")+"= "+URLEncoder.encode(TNo,"UTF-8") +"&"+
                        URLEncoder.encode("TDate","UTF-8")+"= "+URLEncoder.encode(TDate,"UTF-8");
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
                Toast.makeText(getApplicationContext(),"First Get JSON",Toast.LENGTH_LONG).show();
            }
            else
            {
                Display(json_string);
            }
        }
    }

}

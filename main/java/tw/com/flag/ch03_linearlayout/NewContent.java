package tw.com.flag.ch03_linearlayout;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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
public class NewContent extends AppCompatActivity {
    String JSON_STRING;
    String json_string;
    int backCount = 0;
    String NDate,NTitle,ID,ip;
    TextView txt_zero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_content);
        NDate=NewMyADapter.Date;
        NTitle=NewMyADapter.Title;
        TextView txt_Title =(TextView)findViewById(R.id.Title);
        TextView txt_date =(TextView)findViewById(R.id.Date);

        ID=NewMyADapter.ID;
        txt_date.setText(NDate);
        txt_Title.setText(NTitle);

        new GetJs().execute();
    }
    public void Display(String json_string)
    {

        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject=new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
            int count =0;
            String ID,Title=null,Content=null,Time=null;
            while(count<jsonArray.length())
            {
                JSONObject JO=jsonArray.getJSONObject(count);
                ID=JO.getString("ID");
                Title=JO.getString("Title");
                Content=JO.getString("Content");
                Time=JO.getString("Time");


                count++;
            }
            TextView txt_Title = (TextView)findViewById(R.id.Title);
            txt_Title.setText(Title);
            TextView txt_Date = (TextView)findViewById(R.id.Date);
            txt_Date.setText(Time);
            TextView txt_con = (TextView)findViewById(R.id.content);
            txt_con.setText(Content);
            txt_con.setMovementMethod(new ScrollingMovementMethod());
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
            Resources res=NewContent.this.getResources();
            ip=res.getString(R.string.ip);
            json_url=ip+"app105/AppNew2.php";
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url =new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("No","UTF-8")+"="+URLEncoder.encode(ID,"UTF-8");
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

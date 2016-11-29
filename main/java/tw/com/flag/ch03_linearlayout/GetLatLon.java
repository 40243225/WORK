package tw.com.flag.ch03_linearlayout;

/**
 * Created by 40243_000 on 2016/8/2.
 */


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by 40243_000 on 2016/8/1.
 */
public class GetLatLon extends AsyncTask<String,Void,StringBuilder[]> {
    public AsyncResponse delegate = null;

    BufferedReader reader;
    URL url;
    URLConnection connection;
    String line,json;
    @Override
    protected StringBuilder[] doInBackground(String... sKeyWord) {
        try {
            StringBuilder  builder[] = new StringBuilder[sKeyWord.length];
            for(int i=0;i<sKeyWord.length;i++)
                builder[i]=new StringBuilder();
            for(int i=0;i<sKeyWord.length;i++)
            {

                url  = new URL(String.format("http://maps.googleapis.com/maps/api/geocode/json?address=%s&sensor=false&language=zh-TW",
                        URLEncoder.encode(sKeyWord[i], "UTF-8")));//p=%s is KeyWord in
                connection = url.openConnection();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
                while ((line = reader.readLine()) != null) {builder[i].append(line+="\n");}

            }

            reader.close();

            return builder;

        } catch (IOException ex) {

        }
        return null;
    }
    protected void onPostExecute(StringBuilder[] json_string) {
        if(json_string==null)
        {
            Log.e("failed","failed");
        }
        else
        {
            JSONObject json ;
            JSONArray ja ;

            try{
                String adress[]=new String[json_string.length];
                double lon[]=new double[json_string.length];
                double lat[]=new double[json_string.length];
                for(int j=0;j<json_string.length;j++)
                {
                    String a=json_string[j].toString();
                    json = new JSONObject(a); //轉換json格式
                    ja = json.getJSONArray("results");//取得json的Array物件

                    for (int i = 0; i < ja.length(); i++) {
                        adress[j]=ja.getJSONObject(i).getString("formatted_address");
                        lat[j]=ja.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        lon[j]=ja.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    }
                }

                delegate.processFinish(adress,lat,lon);
            }
            catch(JSONException e)
            {

            }
        }
    }


}

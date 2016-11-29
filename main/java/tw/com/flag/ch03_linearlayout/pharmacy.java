package tw.com.flag.ch03_linearlayout;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;

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
import java.util.List;
import java.util.Locale;



public class pharmacy extends AppCompatActivity {
    String JSON_STRING;
    String json_string;
    String cirtname,areaname,ip;
    int backCount = 0;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    double longitude, latitude=121.564101;
    private boolean getService = false;     //是否已開啟定位服務
    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_maps);
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
            Toast.makeText(this, "讀取藥局資訊中....", Toast.LENGTH_LONG).show();
            //Locate();
            GPSTracker gps;
            gps = new GPSTracker(this);
            if(gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }
            getAddressByLocation(latitude,longitude);
            GetJs getjs=new GetJs();
            getjs.execute();
        } else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
            getService = true; //確認開啟定位服務
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)); //開啟設定頁面
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();*/
    }
    public void Display(String json_string)
    {

        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject=new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
            int count =0;
            String name,city,area,street,adress;
            String adr[]=new String[jsonArray.length()];
            String Pname[]=new String[jsonArray.length()];
            while(count<jsonArray.length())
            {
                JSONObject JO=jsonArray.getJSONObject(count);
                name=JO.getString("pharmacy");
                city=JO.getString("city");
                area=JO.getString("area");
                street=JO.getString("street");
                adress=city+area+street;
                adr[count]=adress;
                Pname[count]=name;

                count++;
            }
            Response response =new Response(this,pharmacy.this);
            response.onCreate(adr,jsonArray.length(),Pname);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    class GetJs extends AsyncTask<Void,Void,String>
    {
        String json_url;
        String city=cirtname,area=areaname;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Resources res=pharmacy.this.getResources();
            ip=res.getString(R.string.ip);
            json_url=ip+"app105/Json.php";
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
                String data = URLEncoder.encode("city","UTF-8")+"="+URLEncoder.encode(city,"UTF-8")+"&"+
                        URLEncoder.encode("area","UTF-8")+"="+URLEncoder.encode(area,"UTF-8");
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
            Log.e("結束了","結束了");

        }
    }
    public String getAddressByLocation(double latitude ,double longitude) {
        String returnAddress = "";
        try {
            Geocoder gc = new Geocoder(this, Locale.TAIWAN);        //地區:台灣

            //自經緯度取得地址
            List<android.location.Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);

            if (!Geocoder.isPresent()){ //Since: API Level 9
                returnAddress = "Sorry! Geocoder service not Present.";
            }
            cirtname=lstAddress.get(0).getAdminArea();  //台北市
            areaname= lstAddress.get(0).getLocality();  //中正區
            returnAddress = lstAddress.get(0).getAddressLine(0);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return returnAddress;
    }
}





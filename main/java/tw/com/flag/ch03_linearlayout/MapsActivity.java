package tw.com.flag.ch03_linearlayout;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by 40243_000 on 2016/8/1.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleApiClient client;
    Double latitude,longitude;
    private boolean getService = false;     //是否已開啟定位服務
    private LocationManager lms;
    private Location location;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    private LocationListener locationListener;
    Bundle bundle ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle= getIntent().getExtras();
        setContentView(R.layout.activity_maps);
        GPSTracker gps;
        gps = new GPSTracker(this);
        if(gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵

            Intent intent = new Intent();
            intent.setClass(MapsActivity.this, Main2Activity.class);
            intent.putExtra("No",Main2Activity.No);
            intent.putExtra("name",Main2Activity.user_name);
            startActivity(intent);
            MapsActivity.this.finish();
            return true;

        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(latitude, longitude);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        String Pname[]=bundle.getStringArray("名稱");
        String Adress[]=bundle.getStringArray("地址");
        double[] lat =bundle.getDoubleArray("經度");
        double[] lon= bundle.getDoubleArray("緯度");
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));

        for(int i=0;i<Pname.length;i++)
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat[i], lon[i]))
                    .snippet(Adress[i])
                    .title(Pname[i]));
        }



        Log.e("hello","hello");
    }



}







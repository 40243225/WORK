package tw.com.flag.ch03_linearlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {
    CallbackManager callbackManager;
    private AccessToken accessToken;
    private   String FBname,name;
    Bundle bundle ;
    Button btn;
    int year_x, month_x_, day_x;
    ImageView img;
    static final int DIALOG_ID = 0;
    static  int backCount=0;
    static String No,user_name;
    TextView hello;
    private SharedPreferences settings;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String data = "DATA";
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            backCount++;
            if (backCount == 1) {
                return true;

            }
            if (backCount == 2) {
                moveTaskToBack(true);
                return super.onKeyDown(keyCode, event);

            }
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                        backCount = 0;
                    } catch (Exception ex) {

                    }
                }
            }.start();
        }
        return false;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle= getIntent().getExtras();
        setContentView(R.layout.activity_main2);
        GCM();
        settings = getApplication().getSharedPreferences(data, Context.MODE_PRIVATE);
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x_ = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        No=settings.getString("No","");
        user_name=settings.getString("Name","");
        hello =(TextView)findViewById(R.id.Hello);
        hello.setText("Hello"+user_name +"!");
        Log.e(No,"ss0");

    }

    public void GCM()
    {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Registration success
                    String token = intent.getStringExtra("token");

                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    //Registration error
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {
                    //Tobe define
                }
            }
        };

        //Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }

    public void logout(View view)
    {
        settings.edit().clear().commit();
        Intent intent =new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
    }
    public void QR(View view)
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Main2Activity.this); //創建訊息方塊
        LayoutInflater factory = LayoutInflater.from(Main2Activity.this);
        img =new ImageView(Main2Activity.this);
        img.setImageResource(R.drawable.qr);
        builder.setTitle("QRCODE領藥");
        builder.setCancelable(false);
        builder.setView(img);
        builder.setPositiveButton("關閉", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

            }

        });

        builder.create().show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void box_use(View view)
    {
        Intent intent =new Intent();
        intent.setClass(this,Box_use.class);
        startActivity(intent);
    }
    public void NFC(View view)
    {
        Intent intent =new Intent();
        intent.setClass(this,NFCtag.class);
        startActivity(intent);
    }
    public void Search(View view)
    {
        Intent intent =new Intent();
        intent.setClass(this,pharmacy.class);
        startActivity(intent);
    }
    public void clock(View view)
    {
        Intent intent =new Intent();
        intent.setClass(this,Clock.class);
        startActivity(intent);
    }
    public void New(View view)
    {
        Intent intent =new Intent();
        intent.setClass(this,NEW.class);
        startActivity(intent);
    }
    public void ask(View view)
    {
        Intent intent =new Intent();
        intent.setClass(this,AskQuestion.class);
        startActivity(intent);
    }
    public void takeMedicine(View view)
    {
        Intent intent =new Intent();
        intent.setClass(this,takeMedicine.class);
        startActivity(intent);
    }
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }





    public void Health(View view)
    {
        startActivity(new Intent(this,HealthPass.class));
    }


      /*  Button.OnClickListener listener = new Button.OnClickListener(){



            @Override
            public void onClick(View arg0) {

// TODO Auto-generated method stub

                Intent intent = new Intent();

                intent.setClass(Main2Activity.this,timeActivity.class);

                startActivity(intent);

                finish();

            }



        };*/








}

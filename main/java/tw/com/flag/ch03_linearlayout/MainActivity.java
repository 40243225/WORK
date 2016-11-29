package tw.com.flag.ch03_linearlayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {
    int backCount=0;
    EditText ET_name,ET_pass;
    public static TextView txt;
    String login_name,login_pass;
    public static  String temp;
    EditText sname,fname,phone;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    private   String FBname,name;
    SharedPreferences settings;
    private static final String data = "DATA";
    private static final String user = "USER";
    private static final String pwd = "PWD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        if(!isConnected()) {
            dialog();
            //執行下載任務
        }
        else
         prepare();

    }
    public void dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //創建訊息方塊

        builder.setMessage("請先開啟網路!");

        builder.setTitle("錯誤!");
        builder.setCancelable(false);
        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }

        });


        builder.create().show();
    }
    public void prepare()
    {
        settings = getApplication().getSharedPreferences(data, Context.MODE_PRIVATE);
        ET_name=(EditText)findViewById(R.id.user_name);
        ET_pass=(EditText)findViewById(R.id.user_pass);
        txt =(TextView)findViewById(R.id.infomation);
        login();
    }
    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    public void userReg(View view)
    {
        startActivity(new Intent(this,Register.class));
    }
    public void userLogin(View view)
    {
        login_name=ET_name.getText().toString();
        login_pass=ET_pass.getText().toString();
        settings.edit()
                .putString(user, login_name)
                .putString(pwd, login_pass)
                .commit();
        login();
    }
    public void login()
    {
        String loginuser,loginpwd;
        String method = "login";
        loginuser=settings.getString(this.user, "");
        loginpwd=settings.getString(this.pwd, "");
        if(!(loginuser.equals(""))) {
            BackgroundTask backgroundTask = new BackgroundTask(this, MainActivity.this);
            backgroundTask.execute(method, loginuser, loginpwd);
        }
    }


    private View.OnClickListener sendFBtxt = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
        }
    };


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }



}







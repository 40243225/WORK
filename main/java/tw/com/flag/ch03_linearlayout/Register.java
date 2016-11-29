package tw.com.flag.ch03_linearlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by 40243_000 on 2016/7/6.
 */
public class Register extends Activity {
    EditText ET_NAME,ET_USER_NAME,ET_USER_PASS,ET_EMAIL,ET_TEL;

    String Name,user_name,user_pass,email,tel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        ET_NAME=(EditText)findViewById(R.id.name);
        ET_USER_NAME=(EditText)findViewById(R.id.new_user_name);
        ET_USER_PASS=(EditText)findViewById(R.id.new_user_pass);
        ET_EMAIL=(EditText)findViewById(R.id.new_email);
        ET_TEL=(EditText)findViewById(R.id.new_Tel);

    }
    public void userReg(View view)
    {
        Name=ET_NAME.getText().toString();
        user_name=ET_USER_NAME.getText().toString();
        user_pass=ET_USER_PASS.getText().toString();
        email=ET_EMAIL.getText().toString();
        tel=ET_TEL.getText().toString();
        String method="register";
        BackgroundTask backroundTask=new BackgroundTask(this,Register.this);
        backroundTask.execute(method,Name,user_name,user_pass,email,tel);
        finish();
    }


}


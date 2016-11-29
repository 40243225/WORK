package tw.com.flag.ch03_linearlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by 40243_000 on 2016/8/28.
 */
public class AskQuestion extends Activity {
    EditText ET_Title,ET_Email,ET_content;

    String  Title,content,email;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_layout);
        ET_Title=(EditText)findViewById(R.id.title);
        ET_Email=(EditText)findViewById(R.id.Email);
        ET_content=(EditText)findViewById(R.id.content);


    }
    public void userReg(View view)
    {
        Title=ET_Title.getText().toString();
        content=ET_content.getText().toString();
        email=ET_Email.getText().toString();

        String method="Ask";
        BackgroundTask backroundTask=new BackgroundTask(this,AskQuestion.this);
        backroundTask.execute(method,Title,content,email);
        finish();
    }


}


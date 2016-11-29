package tw.com.flag.ch03_linearlayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
 * Created by 40243_000 on 2016/7/6.
 */
public class BackgroundTask extends AsyncTask<String,Void,String[]> {
    private Activity mParentActivity;
    AlertDialog alertDialog;
    SharedPreferences settings;
    private static final String data = "DATA";
    Context ctx;
    String ip;

    BackgroundTask(Context ctx ,Activity parentActivity)
    {
        this.ctx=ctx;
        mParentActivity = parentActivity;
    }


    @Override

    protected void onPreExecute() {

        alertDialog =new AlertDialog.Builder(ctx).create();
        alertDialog .setTitle("Login Information...");
        settings = mParentActivity.getApplication().getSharedPreferences(data, Context.MODE_PRIVATE);
        Resources res=ctx.getResources();
        ip=res.getString(R.string.ip);
    }



    @Override
    protected String[] doInBackground(String... params) {

        String reg_url=ip+"app105/reg.php";
        String login_url=ip+"app105/login.php";
        String ask_url=ip+"app105/ask.php";
        String method=params[0];
        if(method.equals("register"))
        {
            String reg[]=new String[3];
            reg[1]="MAL";
            reg[2]="IOE";
            String name=params[1];
            String user_name=params[2];
            String user_pass=params[3];
            String email=params[4];
            String tel=params[5];
            try{
                URL url=new URL(reg_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("user","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("user_name","UTF-8")+"= "+URLEncoder.encode(user_name,"UTF-8")+"&"+
                        URLEncoder.encode("user_pass","UTF-8")+"= "+URLEncoder.encode(user_pass,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"= "+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("tel","UTF-8")+"= "+URLEncoder.encode(tel,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();

                reg[0]="Registration successful...!";
                return reg;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return reg;
            } catch (IOException e) {
                e.printStackTrace();
                return reg;
            }
        }
        else  if(method.equals("Ask"))
        {
            String username=Main2Activity.user_name;
            String reg[]=new String[3];
            reg[1]="MAL";
            reg[2]="IOE";
            String Title=params[1];
            String content=params[2];
            String email=params[3];
            try{
                URL url=new URL(ask_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS =httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("Title","UTF-8")+"="+URLEncoder.encode(Title,"UTF-8")+"&"+
                        URLEncoder.encode("Name","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                        URLEncoder.encode("content","UTF-8")+"= "+URLEncoder.encode(content,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"= "+URLEncoder.encode(email,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS= httpURLConnection.getInputStream();
                IS.close();

                reg[0]="Post sucessful...!";
                return reg;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return reg;
            } catch (IOException e) {
                e.printStackTrace();
                return reg;
            }
        }
        else if(method.equals("login")) {
            String login_name=params[1];
            String login_pass=params[2];
            try
            {
                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("login_name","UTF-8")+"="+URLEncoder.encode(login_name,"UTF-8")+"&"+
                        URLEncoder.encode("login_pass","UTF-8")+"="+URLEncoder.encode(login_pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream =httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String response[] =new String[3];
                for(int i=0;i<response.length;i++)
                    response[i]="";
                String line ="";
                int i=0;
                while((line =bufferedReader.readLine())!=null)
                {
                    response[i]+=line;
                    i++;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result[]) {
        if(result[0].equals("Registration successful...!")) {

            Toast.makeText(ctx, result[0], Toast.LENGTH_LONG).show();
        }
        else if(result[0].indexOf("1")!=-1)
        {
           Log.e(result[1],result[2]);
            MainActivity.temp="Sucess";
            MainActivity.txt.setText("hello"+result[2]);
            settings.edit()
                    .putString("No", result[1])
                    .putString("Name", result[2])
                    .commit();
            Intent intent = new Intent();
            intent.setClass(mParentActivity,Main2Activity.class);
            mParentActivity.startActivity(intent);
        }
        else if(result[0].equals("Post sucessful...!"))
        {
            Toast.makeText(ctx, result[0], Toast.LENGTH_LONG).show();
        }
        else
        {
            MainActivity.txt.setText("failed");
        }

    }


}

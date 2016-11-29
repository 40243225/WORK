package tw.com.flag.ch03_linearlayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class medicine extends AppCompatActivity {
    String JSON_STRING;
    String json_string;
    int backCount = 0;
    String Code,MedicineName,MedicineUrl=null;
    TextView txt_CNAME,txt_code,txt_Ename,txt_Cname,txt_indicate,txt_forms,txt_outward;
    ImageView img;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine);
        Code=MyAdapter2.DoctorCode;
        Button btn=(Button)findViewById(R.id.button12) ;
        new GetJs().execute();
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Medicine();
            }});
    }
    public void Medicine()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(medicine.this); //創建訊息方塊
        LayoutInflater factory = LayoutInflater.from(medicine.this);
        img =new ImageView(medicine.this);

        new AsyncTask<String, Void, Bitmap>()
        {
            @Override
            protected Bitmap doInBackground(String... params) //實作doInBackground
            {
                String url = params[0];
                return getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute(Bitmap result) //當doinbackground完成後
            {
                if(MedicineUrl==null)
                    img.setImageResource(R.drawable.nop);

                else
                    img. setImageBitmap (result);
                super.onPostExecute(result);

            }

        }.execute(MedicineUrl);
        builder.setTitle(MedicineName);
        builder.setCancelable(false);
        builder.setView(img);
        builder.setPositiveButton("關閉", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

            }

        });

        builder.create().show();

    }
    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    public void Display(String json_string)
    {

        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject=new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
            int count =0;
            String Code = null,Ename = null,CName = null,indication = null,drug = null,outward = null,outwardurl=null;
            while(count<jsonArray.length())
            {
                JSONObject JO=jsonArray.getJSONObject(count);
                // No=JO.getString("No");
                Code=JO.getString("code");
                Ename=JO.getString("Ename");
                CName=JO.getString("CName");
                indication=JO.getString("indication");
                drug=JO.getString("drug");
                outward=JO.getString("outward");
                outwardurl=JO.getString("outwardurl");
                count++;
            }
            String Cn= MyAdapter2.Cname;
            txt_CNAME=(TextView)findViewById(R.id.Title) ;
            txt_CNAME.setText(Cn);
            txt_code=(TextView)findViewById(R.id.textCode) ;
            txt_code.setText("  "+Code);
            txt_Ename=(TextView)findViewById(R.id.textEname);
            txt_Ename.setText(Ename);
            txt_Cname=(TextView)findViewById(R.id.textCname);
            txt_Cname.setText(Cn);
            MedicineName=Cn;
            if(outwardurl.length()>=69)
             MedicineUrl=outwardurl.substring(0,71);
            txt_indicate=(TextView)findViewById(R.id.textIndicate);
            Log.e(Integer.toString(indication.length()),"asd");
            if(indication.length()!=0)
            {
                TextView cc=(TextView)findViewById(R.id.textView3);
                cc.setText("");
                txt_indicate.setText(indication);
                txt_forms=(TextView)findViewById(R.id.textForm);
                txt_forms.setText(""+drug);
                txt_outward=(TextView)findViewById(R.id.textOutward);
                txt_outward.setText(""+outward);
            }

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
            Resources res=medicine.this.getResources();
            ip=res.getString(R.string.ip);
            json_url="http://140.130.35.74/app105/medicine_json.php";
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                String No=Code;
                Log.e("ads",No);
                URL url =new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("No","UTF-8")+"="+URLEncoder.encode(No,"UTF-8");
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
package tw.com.flag.ch03_linearlayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 40243_000 on 10/5/2016.
 */
public class Box_use extends Activity {
    TextView box_number,box_staus;
    String No="Test001";
    String JSON_STRING,json_string;
    private String Date[];
    private int S_count=1;
    String ip;
    private String[][] WO = new String[5][100];
    private String[][] Time = new String[5][100];
    List<Map<String, String>> groups;
    List<List<Map<String, String>>> childs;
    Calendar c = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_usage_layout);
        Button changeDate = (Button) findViewById(R.id.button10);
        changeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new DatePickerDialog(Box_use.this, d,
                        c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        for(int i=0;i<5;i++)
        {
            for(int j=0;j<100;j++)
            {
                WO[i][j]="0";
                Time[i][j]="0";
            }
        }
        new GetJs().execute();
    }
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int  year_x = year;
            String date;
            int month_x_ = monthOfYear + 1 ;
            int day_x = dayOfMonth ;
            if (day_x<10)
             date= year+"-"+month_x_+"-0"+dayOfMonth;
            else
                date= year+"-"+month_x_+"-"+dayOfMonth;
            Intent intent = new Intent();
            intent.setClass(Box_use.this,Box_use_date.class);
            intent.putExtra("date",date);
            startActivity(intent);
        }
    };

    private  void prepare()
    {
        ExpandableListView elv = (ExpandableListView) findViewById(R.id.mExpandableListView);
        int temp=S_count;
        // 準備一級清單中顯示的資料:2個一級清單,分別顯示"group1"和"group2"
        groups = new ArrayList<Map<String, String>>();
        childs = new ArrayList<List<Map<String, String>>>();
        if(S_count >=5)
            S_count=5;
        for(int i=0;i< S_count;i++)
        {
            Map<String, String> group1 = new HashMap<String, String>();
            group1.put("group", Date[i]);
            groups.add(group1);

            List<Map<String, String>> child = new ArrayList<Map<String, String>>();
            for(int j=0;j<WO[i].length;j++)
            {
                Map<String, String> childData = new HashMap<String, String>();
                    if(!Time[i][j].equals("0"))
                    {
                        childData.put("date", Time[i][j]);
                        childData.put("wo", WO[i][j]);
                        child.add(childData);
                    }
            }
            childs.add(child);

        }



        // 準備第一個一級清單中的二級清單資料:兩個二級清單,分別顯示"childData1"和"childData2"


        ExpandableAdapter viewAdapter = new ExpandableAdapter(this, groups,
                childs);
        elv.setAdapter(viewAdapter);
    }

    // 自訂的ExpandListAdapter
    class ExpandableAdapter extends BaseExpandableListAdapter {
        private Context context;
        List<Map<String, String>> groups;
        List<List<Map<String, String>>> childs;

        /*
         * 構造函數: 參數1:context物件 參數2:一級清單資料來源 參數3:二級清單資料來源
         */
        public ExpandableAdapter(Context context,
                                 List<Map<String, String>> groups,
                                 List<List<Map<String, String>>> childs) {
            this.groups = groups;
            this.childs = childs;
            this.context = context;
        }

        public Object getChild(int groupPosition, int childPosition) {
            return childs.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        // 獲取二級清單的View物件
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            @SuppressWarnings("unchecked")
            String time= ((Map<String, String>) getChild(groupPosition,
                    childPosition)).get("date");
            String wo= ((Map<String, String>) getChild(groupPosition,
                    childPosition)).get("wo");
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 獲取二級清單對應的佈局檔, 並將其各元素設置相應的屬性
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
                    R.layout.box_usage_child_item, null);
            TextView tv1 = (TextView) linearLayout.findViewById(R.id.date_text);
            TextView tv2 = (TextView) linearLayout.findViewById(R.id.weight_open_text);
            tv1.setText(time);
            tv2.setText(wo);


            return linearLayout;
        }

        public int getChildrenCount(int groupPosition) {
            return childs.get(groupPosition).size();
        }

        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        public int getGroupCount() {
            return groups.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        // 獲取一級清單View物件
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String text = groups.get(groupPosition).get("group");
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 獲取一級清單佈局檔,設置相應元素屬性
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
                    R.layout.box_usage_group_item, null);
            TextView textView = (TextView) linearLayout
                    .findViewById(R.id.group_tv);
            textView.setText(text);

            return linearLayout;
        }

        public boolean hasStableIds() {
            return false;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }
    public void Display(String json_string)
    {

        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject=new JSONObject(json_string);
            jsonArray=jsonObject.getJSONArray("server_response");
            int count =0;int ar1=0,ar2=1;
            String Box_number="null",Box_staus="非正常",getDate="0";
            String open_time,open_time_h,open_time_m;
            String open_weight,close_weight;
            String Date_array[]=new String[jsonArray.length()];
            while(count<jsonArray.length())
            {
                JSONObject JO=jsonArray.getJSONObject(count);
                Box_number=No;
                Box_staus=JO.getString("Power");
                open_time_h=JO.getString("Open_Time_Hour");
                open_time_m=JO.getString("Open_Time_Min");
                if(open_time_h.length()==1)
                    open_time_h="0"+open_time_h;
                if(open_time_m.length()==1)
                    open_time_m="0"+open_time_m;

                open_time=open_time_h+":"+open_time_m;
                open_weight=JO.getString("Open_Weight");
                close_weight=JO.getString("Close_Weight");
                float ow= Float.valueOf(open_weight);
                float cw=Float.valueOf(close_weight);
                float weight=ow-cw;
                open_weight=Float.toString(weight);
                if(count==0)//第一次執
                {
                    getDate=JO.getString("Date");
                    Date_array[count]=getDate;
                    Time[count][0]="開啟時間";
                    WO[count][0]="本次用藥重量";

                }
                if(!getDate.equals(JO.getString("Date")))
                {
                    getDate=JO.getString("Date");
                    Date_array[S_count]=getDate;
                    Time[S_count][0]="開啟時間";
                    WO[S_count][0]="本次用藥重量";
                    S_count++;
                    ar1++;
                    ar2=1;
                }
                if(S_count==5)
                    break;
                if(Integer.valueOf(open_time_h)>12)
                Time[ar1][ar2]=open_time+" PM";
                else
                    Time[ar1][ar2]=open_time+" AM";
                WO[ar1][ar2]=open_weight+" g";

                ar2++;
                count++;
            }
            Date=Date_array;
            box_number=(TextView)findViewById(R.id.box_number_text);
            box_staus=(TextView)findViewById(R.id.staus_text);
            box_number.setText(Box_number);
            if(Box_staus.equals("1"))
                box_staus.setText("正常");
            else
            {
                box_staus.setText("不正常");
                box_staus.setTextColor(android.graphics.Color.RED);
            }
            prepare();
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
            Resources res=Box_use.this.getResources();
            ip=res.getString(R.string.ip);
            json_url=ip+"app105/app_box_usage.php";
            Log.e("start","start");
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String No1=No;
                URL url =new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("box_number","UTF-8")+"="+URLEncoder.encode(No1,"UTF-8");
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
                Toast.makeText(getApplicationContext(),"Fuck?",Toast.LENGTH_LONG).show();
            }
            else
            {
                Display(json_string);
            }
        }

    }

}

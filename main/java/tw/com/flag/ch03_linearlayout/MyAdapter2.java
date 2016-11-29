package tw.com.flag.ch03_linearlayout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by 40243_000 on 2016/8/25.
 */
public class MyAdapter2 extends BaseAdapter {

    static String DoctorCode,No,Cname;
    private Context contx;
    private LayoutInflater inflater;
    String [] key;
    String [] value;
    public MyAdapter2(Context c, String [] key, String [] value ){
        inflater = LayoutInflater.from(c);
        contx=c;
        this.key = key;
        this.value = value;


    }
    @Override
    public int getCount() {
        return key.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.code_my,viewGroup,false);
        TextView key2,value2;
        key2 = (TextView) view.findViewById(R.id.key);
        value2 = (TextView) view.findViewById(R.id.value);
        key2.setText("藥品代碼:"+key[i]);
        value2.setText("藥品名稱:"+value[i]);
        view.setOnClickListener(new LongClick(value[i],key[i]));
        return view;
    }
    class LongClick extends AppCompatActivity implements View.OnClickListener{
        private String Date1,No1,Dis;
        public LongClick(String Code ,String Name){
            this.Date1 =Code;
            this.No1=Name;

        }//用建構子獲得該item 的值
        @Override
        public void onClick(View view) {
            DoctorCode=No1;
            Cname=Date1;
            Log.e("123",DoctorCode);
            No=No1;
            Intent intent=new Intent();
            intent.setClass(contx, HealthPassBookCode.class);
            contx.startActivity(new Intent(contx, medicine.class));
        }



    }
}

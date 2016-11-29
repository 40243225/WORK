package tw.com.flag.ch03_linearlayout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by 40243_000 on 2016/8/28.
 */
public class NewMyADapter extends BaseAdapter {

    static String Date,Title,ID;
    private Context contx;
    private LayoutInflater inflater;
    String [] key;
    String [] value;
    String [] dis;

    public NewMyADapter(Context c, String [] key, String [] value ,String dis[]){
        inflater = LayoutInflater.from(c);
        contx=c;
        this.key = key;
        this.value = value;
        this.dis=dis;
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
        view = inflater.inflate(R.layout.item_my,viewGroup,false);
        TextView key2,value2,content;

        key2 = (TextView) view.findViewById(R.id.key);
        value2 = (TextView) view.findViewById(R.id.value);
        key2.setText("標題:");
        value2.setText("發布日期:"+value[i]);

        key2.append(key[i]);
        view.setOnClickListener(new LongClick(value[i],key[i],dis[i]));
        return view;
    }
    class LongClick extends AppCompatActivity implements View.OnClickListener{
        private String Date1,No1,Dis;
        public LongClick(String Date ,String No,String Dis){
            this.Date1 =Date;
            this.No1=No;
            this.Dis=Dis;

        }//用建構子獲得該item 的值
        @Override
        public void onClick(View view) {
            Date=Date1;
            Title=No1;
            ID=Dis;
            Intent intent=new Intent();
            intent.setClass(contx,  NewContent.class);
            contx.startActivity(new Intent(contx, NewContent.class));
        }



    }
}

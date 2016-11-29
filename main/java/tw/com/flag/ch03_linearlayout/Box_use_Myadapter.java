package tw.com.flag.ch03_linearlayout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Box_use_Myadapter extends BaseAdapter {

    static String No;
    private Context contx;
    private LayoutInflater inflater;
    String [] key;
    String [] value;
    String [] dis;
    public Box_use_Myadapter(Context c, String [] key, String [] value ){
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
        view = inflater.inflate(R.layout.box_usage_child_item,viewGroup,false);
        TextView key2,value2;
        key2 = (TextView) view.findViewById(R.id.date_text);
        value2 = (TextView) view.findViewById(R.id.weight_open_text);
        key2.setText(key[i]);
        value2.setText(value[i]);
        return view;
    }

}

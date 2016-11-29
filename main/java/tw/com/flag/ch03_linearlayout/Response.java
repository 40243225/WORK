package tw.com.flag.ch03_linearlayout;

/**
 * Created by 40243_000 on 2016/8/2.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;



import com.google.android.gms.identity.intents.Address;

/**
 * Created by 40243_000 on 2016/8/1.
 */
public class Response  implements AsyncResponse {
    Context ctx;
    private Activity mParentActivity;
    Response(Context ctx ,Activity parentActivity)
    {
        this.ctx=ctx;
        mParentActivity = parentActivity;
    }
    String Adress[],Pname[];
    double lat[];
    double lon[];
    int max;
    int a;
    Intent intent = new Intent();
    GetLatLon getLatLon=new GetLatLon();
    public void onCreate(String adress[] ,int i,String pharmacy[])
    {

        getLatLon.delegate=this;
        getLatLon.execute(adress);
        max=i;
        Pname=new String[max];
        Adress=new String[max];
        lat=new double[max];
        lon=new double[max];
        Pname=pharmacy;
    }

    public void processFinish(String adress[],double latitude[],double longitude[]) {
      /* for(int i=0;i<adress.length;i++)
       {
           Log.e("名稱",Pname[i]);
           Log.e("地址",adress[i] );
           Log.e("經度",Double.toString(latitude[i]));
           Log.e("緯度",Double.toString(longitude[i]));
       }*/
        Intent intent = new Intent();
        intent.setClass(mParentActivity,MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("名稱",Pname);
        intent.putExtra("地址",adress);
        intent.putExtra("經度",latitude);
        intent.putExtra("緯度",longitude);
        mParentActivity.startActivity(intent);

    }


}

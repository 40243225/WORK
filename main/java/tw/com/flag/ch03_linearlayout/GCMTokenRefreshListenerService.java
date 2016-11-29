package tw.com.flag.ch03_linearlayout;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by 40243_000 on 2016/7/10.
 */
public class GCMTokenRefreshListenerService extends InstanceIDListenerService {
    /**
     * When token refresh, start service to get new token
     */
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this,GCMRegistrationIntentService.class);
        startService(intent);
    }
}
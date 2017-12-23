package in.laxmitech.pixelme;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

/**
 * Created by sc on 19/12/17.
 */

public class PixelMeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this, getString(R.string.ad_app_id));
    }
}

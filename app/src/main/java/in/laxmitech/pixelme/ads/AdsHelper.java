package in.laxmitech.pixelme.ads;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import in.laxmitech.pixelme.R;

/**
 * Created by sc on 19/12/17.
 */

public class AdsHelper {

    public static void showInterstitialAd(Context activityContest) {
        final InterstitialAd interstitialAd = new InterstitialAd(activityContest);
        interstitialAd.setAdUnitId(activityContest.getString(R.string.ad_interstitial_unit_id_1));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                interstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        });
    }

    public static void showBanner(final AdView adView) {
        /*adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(Constants.TestAds.BANNER_UNIT_ID);*/

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    public static void showRewardVideoAd(Context activityContext) {

    }
}

package in.laxmitech.pixelme.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.warkiz.widget.IndicatorSeekBar;

import in.laxmitech.pixelme.R;
import in.laxmitech.pixelme.ads.AdsHelper;
import nl.dionsegijn.pixelate.OnPixelateListener;
import nl.dionsegijn.pixelate.Pixelate;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private final int IMAGE_REQUEST_CODE = 11;
    private ImageView mImageView;
    private IndicatorSeekBar mDensitySeekbar;
    private RewardedVideoAd mRewardedVideoAd;

    private int mDensity = 0;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        loadAds();
    }

    private void loadAds() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdsHelper.showBanner(adView);

//        AdsHelper.showInterstitialAd(this);

//        AdsHelper.showRewardVideoAd(this);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Log.d("Ads", "Reward Ads Loaded");
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Log.d("Ads", "Reward Ads Opened");
            }

            @Override
            public void onRewardedVideoStarted() {
                Log.d("Ads", "Reward Ads Started");
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Log.d("Ads", "Reward Ads Closed");
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Log.d("Ads", "Reward Ads Rewards : " + rewardItem.getAmount());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Log.d("Ads", "Reward Ads Left-app");
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Log.d("Ads", "Reward Ads Failed To load");
            }
        });

        mRewardedVideoAd.loadAd(getString(R.string.ad_reward_unit_id_1), new AdRequest.Builder().build());
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.imgView);
        mDensitySeekbar = (IndicatorSeekBar) findViewById(R.id.density_seekbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.home_open_fab);
        fab.setOnClickListener(this);

        mDensitySeekbar.setOnSeekChangeListener(getSeekChangeListener());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_open_fab:
                onOpenFileClicked();
                break;

        }
    }

    private void onOpenFileClicked() {
        mImageView.setImageBitmap(null);
        mDensitySeekbar.setProgress(100);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AdsHelper.showInterstitialAd(this);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    setImageChosen(data.getData());
                    break;
            }
        }
    }

    private void setImageChosen(Uri uri) {
        if (uri != null) {
            mUri = uri;
            mImageView.setImageURI(mUri);
        }
    }

    private IndicatorSeekBar.OnSeekBarChangeListener getSeekChangeListener() {
        return new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {

            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                switch (seekBar.getId()) {
                    case R.id.density_seekbar:
                        mDensity = seekBar.getProgress();
                        pixelateImageAndShow();
                        break;

                }
            }
        };
    }

    private void pixelateImageAndShow() {
        mImageView.setImageURI(mUri);
        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        if (bitmap != null) {
            try {
                new Pixelate(bitmap)
                        .setDensity(mDensity)
                        .setListener(new OnPixelateListener() {
                            @Override
                            public void onPixelated(Bitmap bitmap, int density) {
                                if (bitmap != null) {
                                    mImageView.setImageBitmap(bitmap);
                                }
                            }
                        })
                        .make();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRewardedVideoAd.resume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRewardedVideoAd.pause(this);
    }

    @Override
    protected void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();

    }
}

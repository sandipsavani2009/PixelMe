package in.laxmitech.pixelme.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
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

import java.io.File;
import java.io.FileOutputStream;

import in.laxmitech.pixelme.Constants;
import in.laxmitech.pixelme.R;
import in.laxmitech.pixelme.ads.AdsHelper;
import nl.dionsegijn.pixelate.OnPixelateListener;
import nl.dionsegijn.pixelate.Pixelate;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private final int PERMISSION_REQUEST_CODE = 12;
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

        ImageView openFab = (ImageView) findViewById(R.id.home_open_fab);
        openFab.setOnClickListener(this);
        ImageView ShareFab = (ImageView) findViewById(R.id.home_share_fab);
        ShareFab.setOnClickListener(this);
        ImageView saveFab = (ImageView) findViewById(R.id.home_save_fab);
        saveFab.setOnClickListener(this);

        mDensitySeekbar.setOnSeekChangeListener(getSeekChangeListener());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_open_fab:
                onOpenFileClicked();
                break;

            case R.id.home_save_fab:
                onSaveFileClicked();
                break;

            case R.id.home_share_fab:
                opShareFileClicked();
                break;

        }
    }

    private void onSaveFileClicked() {
        if (isPermissionGranted()) {
            saveImage();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private void saveImage() {
        if (mImageView.getDrawable() != null) {
            BitmapDrawable draw = (BitmapDrawable) mImageView.getDrawable();
            Bitmap bitmap = draw.getBitmap();

            if (bitmap != null) {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + File.separator + Constants.Storage.DIR_NAME);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

                    outStream.flush();
                    outStream.close();

                    showMessage("Your Pixel photo saved!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showMessage("Could not save photo!");
                }
            }
        }

        AdsHelper.showInterstitialAd(this);
    }

    private void showMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "Something went wrong!";
        }
        Snackbar.make(findViewById(R.id.home_parent), msg, Snackbar.LENGTH_SHORT).show();
    }

    private void opShareFileClicked() {
        if (mImageView.getDrawable() != null) {
            Drawable mDrawable = mImageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();

            if (bitmap != null) {
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                        bitmap, "Image Description", null);
                Uri uri = Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Photo"));
            }
        }

        AdsHelper.showInterstitialAd(this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage();
                } else {
                    showMessage("Write permission required to save photo");
                }
                break;
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

    private boolean isPermissionGranted() {
        boolean isGranted = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                isGranted = true;
            }
        } else {
            isGranted = true;
        }

        return isGranted;
    }
}

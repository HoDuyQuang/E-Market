package vn.edu.dut.itf.e_market.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.models.Province;
import vn.edu.dut.itf.e_market.tasks.GetProvinceTask;
import vn.edu.dut.itf.e_market.tasks.SetDeviceTokenTask;
import vn.edu.dut.itf.e_market.utils.AppPref;


public class SplashActivity extends AppCompatActivity {
    private static final long DURATION = 700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_splash);
        ((TextView) findViewById(R.id.version)).setText(getString(R.string.version, getString(R.string.version_name)));

        // Animation logo
//		Animation fadeIn = new AlphaAnimation(0, 1);
//		fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
//		fadeIn.setDuration(DURATION);
        // findViewById(R.id.logo).startAnimation(fadeIn);
        new SetDeviceTokenTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//		logFacebookHash();
        initData();
    }

    private void logFacebookHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "vn.com.brycen.restaurant",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        if (!AppPref.getInstance(this).getBoolean(AppPref.KEY_PROVINCE)) {
            GetProvinceTask task = new GetProvinceTask(this) {
                @Override
                protected void onSuccess(List<Province> provinces) {
                    nextStep();
                }

                @Override
                protected void onError(int code) {
                    super.onError(code);
                }
            };
            task.setErrorDialog(getString(R.string.retry), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    initData();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            task.execute();
        } else {
            nextStep();
        }
    }

    private void nextStep() {
        if (AppPref.getInstance(this).getBoolean(AppPref.KEY_FIRST_OPEN_APP)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.loading_view).setVisibility(View.INVISIBLE);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, DURATION);
        } else {
            finish();
            startActivity(new Intent(this, LanguageActivity.class));
        }
    }
}

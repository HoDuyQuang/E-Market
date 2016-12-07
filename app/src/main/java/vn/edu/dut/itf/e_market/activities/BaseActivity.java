package vn.edu.dut.itf.e_market.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import vn.edu.dut.itf.e_market.R;
import vn.edu.dut.itf.e_market.models.RequestStatus;
import vn.edu.dut.itf.e_market.utils.AppPref;
import vn.edu.dut.itf.e_market.utils.CommonUtils;

/**
 * @author d_quang
 * 
 *         Base for all activity
 */

public abstract class BaseActivity extends AppCompatActivity implements IBaseActivityImpl {
	Toast mToast;
	protected RequestStatus requestStatus;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		loadLocale();
		requestStatus=new RequestStatus();
		requestStatus.setFirstSuccess(false);
		super.onCreate(savedInstanceState);
		setContentView(setLayout());
		findViews();
		initViews();
		initData();
		showData();
	}

	protected void toast(String message) {
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		mToast.show();
	}

	protected void toast(int stringId) {
		toast(getString(stringId));
	}

	protected void hideSoftKeyboard() {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
					Activity.INPUT_METHOD_SERVICE);
			if (getCurrentFocus()!=null) {
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean handleReturn = super.dispatchTouchEvent(ev);

		View view = getCurrentFocus();

		int x = (int) ev.getX();
		int y = (int) ev.getY();

		if (view instanceof EditText) {
			View innerView = getCurrentFocus();
			if (ev.getAction() == MotionEvent.ACTION_UP && !getLocationOnScreen(innerView).contains(x, y)) {
				InputMethodManager input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (getWindow().getCurrentFocus()!=null) {
					input.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
				}
			}
		}

		return handleReturn;
	}

	private Rect getLocationOnScreen(View innerView) {
		Rect mRect = new Rect();
		int[] location = new int[2];
		innerView.getLocationOnScreen(location);
		mRect.left = location[0];
		mRect.top = location[1];
		mRect.right = location[0] + innerView.getWidth();
		mRect.bottom = location[1] + innerView.getHeight();
		return mRect;
	}

	public void saveLocale(String lang) {
		AppPref.getInstance(this).putString(AppPref.KEY_LANGUAGE, lang);
		changeLang(CommonUtils.getAppLocale(this));
		// restartActivity();
	}

	public void loadLocale() {
		Locale language = CommonUtils.getAppLocale(this);
		changeLang(language);
	}

	@SuppressWarnings("deprecation")
	public void changeLang(Locale myLocale) {
		Locale.setDefault(myLocale);
		Configuration config = getResources().getConfiguration();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			config.setLocale(myLocale);
		} else {
			config.locale = myLocale;
		}
		getResources().updateConfiguration(config,
				getResources().getDisplayMetrics());

	}

	protected void restartActivity() {
		startActivity(getIntent());
		finish();
	}
	protected void showNoConnection(View mRootView) {
		if (mRootView != null) {
//			Snackbar.make(getString(R.string.no_internet_connection),Snackbar.LENGTH_SHORT).show();
//			TSnackbar.make(mRootView, "<b>"+getString(R.string.snack_network_error)+"</b><br/>"+getString(R.string.no_internet_connection), TSnackbar.LENGTH_LONG).setIcon(R.drawable.ic_network_unavailable).show();
		}
	}
}

package vn.edu.dut.itf.e_market.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

//import android.support.v4.content.SharedPreferencesCompat;

public class AppPref {
	public static final String KEY_FIRST_OPEN_APP = "FIRST_OPEN_APP";
	public static final String KEY_RESTAURANT_NAME = "RESTAURANT_NAME";
	public static final String KEY_PROVINCE = "LOAD_PROVINCE";
	public static final String KEY_CURRENCY = "LOAD_CURRENCY";
	public static final String KEY_LANGUAGE = "LANGUAGE";
	public static final String KEY_AUTH_TOKEN = "AUTH_TOKEN";
	public static final String KEY_DEVICE_TOKEN = "DEVICE_TOKEN";
	public static final String KEY_USER_FIRST_NAME = "USER_FIRST_NAME";
	public static final String KEY_USER_LAST_NAME = "USER_LAST_NAME";
	public static final String KEY_RESTAURANT_PHONE = "RESTAURANT_PHONE";
	public static final String KEY_COST_LOWEST = "COST_LOWEST";
	public static final String KEY_PUSH_NOTIFICATION = "PUSH_NOTIFICATION";
	public static final String KEY_SOCIAL = "SOCIAL";

	private static AppPref mInstance;

	public synchronized static AppPref getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new AppPref(context);
		}
		return mInstance;
	}

	private Editor mEditor;
	private SharedPreferences mSharedPreferences;

	AppPref(Context context) {
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		mEditor = mSharedPreferences.edit();
	}

	public void putBoolean(String key, boolean value) {
		mEditor.putBoolean(key, value);
		mEditor.commit();
		// SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		return mSharedPreferences.getBoolean(key, false);
	}

	public void putString(String key, String value) {
		if (value != null) {
			mEditor.putString(key, value);
		} else {
			mEditor.remove(key);
		}
		mEditor.commit();
		// SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
	}

	public String getString(String key, String defaultValue) {
		return mSharedPreferences.getString(key, defaultValue);
	}

	public String getString(String key) {
		return mSharedPreferences.getString(key, null);
	}

	@SuppressWarnings("unused")
	public void putInt(String key, int value) {
		mEditor.putInt(key, value);
		mEditor.commit();
		// SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
	}

	@SuppressWarnings("unused")
	public int getInt(String key, int defaultValue) {
		return mSharedPreferences.getInt(key, defaultValue);
	}

	@SuppressWarnings("unused")
	public int getInt(String key) {
		return mSharedPreferences.getInt(key, 0);
	}

	@SuppressWarnings("unused")
	public void putLong(String key, long value) {
		mEditor.putLong(key, value);
		mEditor.commit();
		// SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
	}
	@SuppressWarnings("unused")
	public long getLong(String key, long defaultValue) {
		return mSharedPreferences.getLong(key, defaultValue);
	}
	@SuppressWarnings("unused")
	public long getLong(String key) {
		return mSharedPreferences.getLong(key, 0);
	}

	public void putFloat(String key, float value) {
		mEditor.putFloat(key, value);
		mEditor.commit();
		// SharedPreferencesCompat.EditorCompat.getInstance().apply(mEditor);
	}

	public float getFloat(String key, float defaultValue) {
		return mSharedPreferences.getFloat(key, defaultValue);
	}
	@SuppressWarnings("unused")
	public float getFloat(String key) {
		return mSharedPreferences.getFloat(key, 0);
	}


}

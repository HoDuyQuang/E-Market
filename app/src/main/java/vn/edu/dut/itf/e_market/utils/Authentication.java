package vn.edu.dut.itf.e_market.utils;

import android.content.Context;
import android.content.Intent;

public class Authentication {
    public static final String ACTION_LOG_OUT = "vn.com.brycen.restaurant.LOG_OUT";
    public static final String ACTION_LOG_IN = "vn.com.brycen.restaurant.LOG_IN";

    public static boolean isLoggedIn(Context context) {
        return AppPref.getInstance(context).getString(AppPref.KEY_AUTH_TOKEN) != null;
    }

    public static String getAuthToken(Context context) {
        return AppPref.getInstance(context).getString(AppPref.KEY_AUTH_TOKEN);
    }

    public static String getDeviceToken(Context context) {
        return AppPref.getInstance(context).getString(AppPref.KEY_DEVICE_TOKEN,"");
    }

    public static String getDisplayName(Context context) {
        String name = "";
        if (AppPref.getInstance(context).getString(AppPref.KEY_USER_FIRST_NAME) != null && !AppPref.getInstance(context).getString(AppPref.KEY_USER_FIRST_NAME).equals("null")) {
            name += AppPref.getInstance(context).getString(AppPref.KEY_USER_FIRST_NAME);
        }
        if (AppPref.getInstance(context).getString(AppPref.KEY_USER_LAST_NAME) != null &&
                !AppPref.getInstance(context).getString(AppPref.KEY_USER_LAST_NAME).equals("null")) {
            name+=" ";
            name += AppPref.getInstance(context).getString(AppPref.KEY_USER_LAST_NAME);
        }
        return name;
    }

    public static void logout(Context context) {
        AppPref.getInstance(context).putString(AppPref.KEY_USER_FIRST_NAME, null);
        AppPref.getInstance(context).putString(AppPref.KEY_USER_LAST_NAME, null);
        AppPref.getInstance(context).putString(AppPref.KEY_AUTH_TOKEN, null);
        AppPref.getInstance(context).putString(AppPref.KEY_SOCIAL, null);
        context.sendBroadcast(new Intent(ACTION_LOG_OUT));
    }
}

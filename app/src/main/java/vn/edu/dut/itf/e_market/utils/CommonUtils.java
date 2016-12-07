package vn.edu.dut.itf.e_market.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import vn.edu.dut.itf.e_market.R;


public class CommonUtils {
    private static DecimalFormat formatterPrice;
    private static String currency;

    public static String formatPrice(Context context, float price) {
        if (formatterPrice == null) {
            formatterPrice = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            DecimalFormatSymbols symbols = formatterPrice.getDecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            formatterPrice.setDecimalFormatSymbols(symbols);
        }
        if (currency == null) {
            currency = AppPref.getInstance(context).getString(AppPref.KEY_CURRENCY, "");
        }
        return context.getString(R.string.price_with_unit, formatterPrice.format(price),
                currency);
    }

    public static String formatRate(Context context, float rate) {
        DecimalFormat twoDForm;
        twoDForm = new DecimalFormat("#.#");
        twoDForm.setDecimalFormatSymbols(new DecimalFormatSymbols(getAppLocale(context)));
        return twoDForm.format(rate);
    }

    public static String formatDiscount(Context context, float percent) {
        DecimalFormat twoDForm;
        twoDForm = new DecimalFormat("#.#");
        twoDForm.setDecimalFormatSymbols(new DecimalFormatSymbols(getAppLocale(context)));
        return twoDForm.format(percent) + "%";
    }

    public static String formatDate(Context context, Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",
                    CommonUtils.getAppLocale(context));
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        } else {
            return "";
        }
    }

    public static String formatDateApi(Context context, Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
                    CommonUtils.getAppLocale(context));
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        } else {
            return "";
        }
    }

    public static String formatTime(Context context, Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm",
                    CommonUtils.getAppLocale(context));
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        } else {
            return "";
        }
    }

    public static String formatDateTime(Context context, Date date) {
        if (date != null && AppPref.getInstance(context).getString(AppPref.KEY_DATETIME_FORMAT) != null) {
            SimpleDateFormat df;
            if (AppPref.getInstance(context).getString(AppPref.KEY_DATETIME_FORMAT).length() > 0) {
                try {
                    df = new SimpleDateFormat(AppPref.getInstance(context).getString(AppPref.KEY_DATETIME_FORMAT),
                            CommonUtils.getAppLocale(context));
                } catch (IllegalArgumentException e) {
                    df = new SimpleDateFormat("MM/dd/yyyy hh:mm a",
                            CommonUtils.getAppLocale(context));
                }
            } else {
                df = new SimpleDateFormat("MM/dd/yyyy hh:mm a",
                        CommonUtils.getAppLocale(context));
            }
            df.setTimeZone(TimeZone.getDefault());
            return df.format(date);
        } else {
            return "";
        }
    }


    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the Internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static Locale getAppLocale(Context context) {
        return new Locale(AppPref.getInstance(context).getString(AppPref.KEY_LANGUAGE, "en"));
    }

    public static String getRateString(Context context, float rate) {
        if (rate < 5) {
            return context.getString(R.string.review_bad);
        }
        if (rate < 7) {
            return context.getString(R.string.review_average);
        }
        if (rate < 9) {
            return context.getString(R.string.review_good);
        }
        return context.getString(R.string.review_excellent);
    }


    public static void setTextViewStrike(TextView textView, boolean isTrike) {
        if (isTrike) {
            textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            if ((textView.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String text) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    public static boolean isBurmese(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) >= 0x1000 && text.charAt(i) < 0x109F) {
                return true;
            }
        }
        return false;
    }

    public static String randomDeviceToken(Context context) {
        return context.getPackageName() + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis();
    }
}

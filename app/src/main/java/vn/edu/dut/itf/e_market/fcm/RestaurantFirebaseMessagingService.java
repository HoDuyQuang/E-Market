/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vn.edu.dut.itf.e_market.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import vn.com.brycen.restaurant.R;
import vn.com.brycen.restaurant.activities.FoodDetailActivity;
import vn.com.brycen.restaurant.activities.FoodDetailPendingActivity;
import vn.com.brycen.restaurant.activities.FoodReviewDetailActivity;
import vn.com.brycen.restaurant.activities.MainActivity;
import vn.com.brycen.restaurant.activities.RestaurantNotificationActivity;
import vn.com.brycen.restaurant.activities.RestaurantReviewDetailActivity;
import vn.com.brycen.restaurant.models.NotificationYou;
import vn.com.brycen.restaurant.utils.AppPref;

public class RestaurantFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (AppPref.getInstance(this).getBoolean(AppPref.KEY_PUSH_NOTIFICATION, true)) {
                try {
                    JSONObject object = new JSONObject(remoteMessage.getData().get("body"));


                    if (object.has("foodId") && object.getString("foodId").length() > 0) {
                        int foodId = -1;
                        foodId = object.getInt("foodId");
                        String content = object.getString("contents");
                        sendNotification(content, foodId);
                    } else if (object.has("reviewId") && object.getString("reviewId").length() > 0) {
                        String content = object.getString("contents");
                        int reviewType = object.getInt("reviewType");
                        String reviewId = object.getString("reviewId");
                        sendNotification(content, reviewType, reviewId);
                    } else {
                        String content = object.getString("contents");
                        sendNotification(content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String content, int reviewType, String reviewId) {
        if (icon == null) {
            icon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher);
        }
        Spannable sb = getSpannable(content);

//        sb.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 14, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        inboxStyle.addLine(sb);

        Intent intent;
        if (reviewType == NotificationYou.TYPE_REVIEW_RESTAURANT) {
            intent = new Intent(this, RestaurantReviewDetailActivity.class);
            intent.putExtra(RestaurantReviewDetailActivity.REVIEW_ID, reviewId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (reviewType == NotificationYou.TYPE_REVIEW_FOOD) {
            intent = new Intent(this, FoodReviewDetailActivity.class);
            intent.putExtra(FoodReviewDetailActivity.REVIEW_ID, reviewId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }


        showNotification(sb, intent);

    }

    private void showNotification(Spannable sb, Intent intent) {
        String title;
        if (AppPref.getInstance(this).getString(AppPref.KEY_RESTAURANT_NAME) != null && !AppPref.getInstance(this).getString(AppPref.KEY_RESTAURANT_NAME).equals("null")) {
            title = AppPref.getInstance(this).getString(AppPref.KEY_RESTAURANT_NAME);
        } else {
            title = getString(R.string.app_name);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle(title)
                .setContentText(sb)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (id >= Integer.MAX_VALUE) {
            id = 0;
        }
        Notification notification = notificationBuilder.build();

        notificationManager.notify(id++ /* ID of notification */, notification);
    }

    private void sendNotification(String content, int foodId) {
        Spannable sb = getSpannable(content);

        Intent intent = new Intent(this, FoodDetailPendingActivity.class);
        intent.putExtra(FoodDetailActivity.FOOD_ID, foodId);
        intent.putExtra("current", foodId + "");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        showNotification(sb, intent);

    }

    @NonNull
    private Spannable getSpannable(String content) {
        String tempContent = content;
        tempContent = tempContent.replaceAll("<b>", "");
        tempContent = tempContent.replaceAll("</b>", "");
        Spannable sb = new SpannableString(tempContent);
        try {
//            while (content.contains("<b>")) {
            int start = content.indexOf("<b>");
            content = content.replace("<b>", "");
            int end = content.indexOf("</b>");
            content = content.replace("</b>", "");
            sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    private void sendNotification(String content) {
        if (icon == null) {
            icon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher);
        }
        Spannable sb = getSpannable(content);
        Intent intent;

        intent = new Intent(this, RestaurantNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        showNotification(sb, intent);
    }

    static int id = 0;
    // [END receive_message]
    static Bitmap icon;
}

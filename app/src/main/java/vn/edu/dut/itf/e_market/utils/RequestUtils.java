package vn.edu.dut.itf.e_market.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.tasks.ServerUnavailableException;

public class RequestUtils {

    private static final String HEADER_TOKEN = "Token";
    private final static int CONNECTION_TIMEOUT = 10000;

    public static String sendGET(String url, Map<String, String> params, String token) throws NullPointerException, IOException, ServerUnavailableException {
        String urlQuery = url;
        if (params != null && !params.isEmpty()) {
            urlQuery = urlQuery + "?" + getQuery(params);
        }
        URL obj = new URL(urlQuery);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        if (token != null) {
            con.setRequestProperty(HEADER_TOKEN, token);
        }
        con.setRequestMethod("GET");
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String temp = getResponse(con);
            Log.d("TEST", temp);
            return temp;
        } else {
            throw new ServerUnavailableException();
        }

    }

    public static String sendGET(String url) throws IOException, ServerUnavailableException {
        return sendGET(url, null, null);
    }

    public static String sendGET(String url, Map<String, String> params) throws IOException, ServerUnavailableException {
        return sendGET(url, params, null);
    }

    public static String requestPOST(String url, String params) throws IOException {
        return requestPOST(url, params, null, null);
    }

    public static String requestPOST(String url, HashMap<String, String> urlParams) throws IOException {
        return requestPOST(url, null, null, urlParams);
    }

    public static String requestPOST(String url, String params, String token) throws IOException {
        return requestPOST(url, params, token, null);
    }

    public static String requestPOST(String url, String params, String token, HashMap<String, String> urlParams)
            throws IOException {
        if (urlParams != null && !urlParams.isEmpty()) {
            url = url + "?" + getQuery(urlParams);
        }
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        if (token != null) {
            con.setRequestProperty("Token", token);
            Log.d("Token", token);
        }
        if (params != null) {
            con.setRequestProperty("Content-Type", "application/json");
        }

        con.setRequestMethod("POST");
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setReadTimeout(CONNECTION_TIMEOUT);
        // For POST only - START
        con.setDoOutput(true);
        if (params != null) {
            OutputStream os = con.getOutputStream();
            os.write(params.getBytes());
            os.flush();
            os.close();
        }
        // For POST only - END
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getResponse(con);
        } else {
            return null;
        }
    }
    private static final String LINE_FEED = "\r\n";
    public static String sendPost(String url, String accessToken, Map<String, String> params, List<File> list) throws IOException {
//        Map<String, String> headers=new HashMap<>();
//        headers.put("Authorization", "Bearer " + accessToken);
//        MultipartUtility multipartUtility = new MultipartUtility(url,
//                "UTF-8",headers);
//        if (accessToken!=null) {
//            multipartUtility.addHeaderField();
//        }
//        if (params!=null) {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                multipartUtility.addFormField(entry.getKey(), entry.getValue());
//            }
//        }

//        if (list!=null){
//            multipartUtility.addFilePart();
//        }
//        if (mPhoto != null) {
//            multipartUtility.addFilePart(AVATAR_KEY, mPhoto);
//        }
//        return multipartUtility.connect();
//        JSONObject jsonObject = new JSONObject(response);
//        code = jsonObject.getInt(BaseWSControl.CODE_KEY);
        String boundary = "===" + System.currentTimeMillis() + "===";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setReadTimeout(CONNECTION_TIMEOUT);
        // For POST only - START
        con.setDoOutput(true);
        if (accessToken != null) {
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            Log.d("Token", accessToken);
        }

//        if (params != null) {
//            con.setRequestProperty("Content-Type", "application/json");
//        }

        con.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
//        PrintWriter writer = new PrintWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"),
//                true);

//        writer.append(LINE_FEED).flush();
//        writer.append("--" + boundary + "--").append(LINE_FEED);
//        writer.close();




//        if (params != null) {
//            OutputStream os = con.getOutputStream();
//            os.write(params.getBytes());
//            os.flush();
//            os.close();
//        }
        // For POST only - END
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getResponse(con);
        } else {
            return null;
        }
    }

    public  static  String ok() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"title\"\r\n\r\nTest1\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url(APIConfig.URL_28_POST_ORDER)
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoyLCJkZXZpY2VfdG9rZW4iOiJjT0JvaXdMazhnNDpBUEE5MWJIUG5lOUpOWHZ5U2ExOVBHbmoxaEhxdXhIUkF1NFIyalVaN0hqaHl1NEUwMTdDaHE3M0pXN3c5cjJNc0hQQ0YxYzUxQW9RMjM0dUxwS09NQWYwUmJkME1tc3ZxRGNwNVpNMHVGQS1GU0tuZW1jcFJJYm9nZFVmY1NuUU1UX3U0cmxoRXRmNyIsIm9zIjoiQU5EUk9JRCIsInVwZGF0ZWRfYXQiOiIyMDE2LTEyLTE0IDE2OjUxOjI2IiwiY3JlYXRlZF9hdCI6IjIwMTYtMTItMTQgMTY6NTE6MjYiLCJpZCI6OX0.IgNWk4UJcxPmhXlIclEM5LNJC_cP0d-aXK-J1sgs5dw")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "5d324bfc-7ecb-61c9-d4c2-a5e66f4800d4")
                .build();

        Response response = client.newCall(request).execute();
        return response.message();
    }

    public static String sendPOST(String url, Map<String, String> params) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setReadTimeout(15000);
        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(getQuery(params).getBytes());
        os.flush();
        os.close();
        // For POST only - END
        int responseCode = con.getResponseCode();
        // If success
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getResponse(con);
        } else {
            return null;
        }
    }

    private static String getQuery(Map<String, String> params)
            throws UnsupportedEncodingException, NullPointerException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=")
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        Log.e("request: ", String.valueOf(result));
        return result.toString();
    }

    private static String getResponse(HttpURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

}

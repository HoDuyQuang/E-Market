package vn.edu.dut.itf.e_market.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static String sendPost(String url, String accessToken, Map<String, String> params, List<File> list) throws IOException {
        MultipartUtility multipartUtility = new MultipartUtility(url,
                "UTF-8");
        if (accessToken!=null) {
            multipartUtility.addHeaderField("Authorization", "Bearer " + accessToken);
        }
        if (params!=null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                multipartUtility.addFormField(entry.getKey(), entry.getValue());
            }
        }

//        if (list!=null){
//            multipartUtility.addFilePart();
//        }
//        if (mPhoto != null) {
//            multipartUtility.addFilePart(AVATAR_KEY, mPhoto);
//        }
        return multipartUtility.connect();
//        JSONObject jsonObject = new JSONObject(response);
//        code = jsonObject.getInt(BaseWSControl.CODE_KEY);
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

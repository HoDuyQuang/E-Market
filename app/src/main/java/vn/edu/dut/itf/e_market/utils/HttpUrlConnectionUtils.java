package vn.edu.dut.itf.e_market.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUrlConnectionUtils {
    public static final int CONNECT_TIME_OUT = 30000;
    public static final int READ_TIME_OUT = 30000;

    public static String doPostRequest(String targetURL, Map<String, String> params) throws NullPointerException,IOException {
        URL url;
        HttpURLConnection connection = null;
        String urlParameters = buildPostParams(params);
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setConnectTimeout(CONNECT_TIME_OUT);
            connection.setReadTimeout(READ_TIME_OUT);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    static String buildPostParams(Map<String, String> params) throws UnsupportedEncodingException,NullPointerException {
        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0) {
                sbParams.append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
            } else {
                sbParams.append("&").append(key).append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
            }
            i++;
        }
        return sbParams.toString();
    }
}

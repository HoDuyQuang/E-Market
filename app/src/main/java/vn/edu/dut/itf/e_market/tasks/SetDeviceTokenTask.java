package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.utils.AppPref;
import vn.edu.dut.itf.e_market.utils.CommonUtils;
import vn.edu.dut.itf.e_market.utils.RequestUtils;


public class SetDeviceTokenTask extends BaseApiTask {
    private String mDeviceToken;

    public SetDeviceTokenTask(Context context) {
        super(context);
    }

    @Override
    String request() throws IOException, JSONException {
        return setToken(mContext);
    }

    private String setToken(Context context) throws IOException {
        HashMap<String, String> params = new HashMap<>();

        String token = FirebaseInstanceId.getInstance().getToken();

        if (token == null) {
            token = CommonUtils.randomDeviceToken(getContext());
        } else{
            params.put("os", "android");
            Log.e("Token",token);
        }
        mDeviceToken=token;
        params.put("deviceTokenNew", token);

        String oldToken = AppPref.getInstance(context).getString(AppPref.KEY_DEVICE_TOKEN);
        if (oldToken != null && !oldToken.trim().equals("") && !oldToken.equals(token)) {
            params.put("deviceTokenOld", oldToken);
        }

        return RequestUtils.requestPOST(APIConfig.URL_40_SET_DEVICE_TOKEN, params);
    }

    @Override
    protected void onError(int code) {

    }

    @Override
    protected void parseData(JSONObject jsonObject) throws JSONException {
        onSuccess();
        AppPref.getInstance(mContext).putString(AppPref.KEY_DEVICE_TOKEN, mDeviceToken);
        if (jsonObject.has("user")) {
            AppPref.getInstance(mContext).putString(AppPref.KEY_AUTH_TOKEN,
                    jsonObject.getJSONObject("user").getString("token"));
        }
    }

    protected void onSuccess() {

    }

}

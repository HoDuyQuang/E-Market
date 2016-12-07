package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.utils.AppPref;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.RequestUtils;


public class GetLogOutTask extends BaseApiTask {

	public GetLogOutTask(Context context) {
		super(context);
	}

	@Override
	String request() throws IOException, JSONException, ServerUnavailableException {
		HashMap<String, String> params = new HashMap<>();
		params.put("deviceToken", AppPref.getInstance(mContext).getString(AppPref.KEY_DEVICE_TOKEN));
		return RequestUtils.sendGET(APIConfig.URL_34_GET_LOGOUT, params, Authentication.getAuthToken(getContext()));
	}

	@Override
	protected void onError(int code) {
		Authentication.logout(mContext);
	}

	@Override
	protected void parseData(JSONObject result) throws JSONException {
		Authentication.logout(mContext);
		onSuccess();
	}

	protected void onSuccess() {
	}

}

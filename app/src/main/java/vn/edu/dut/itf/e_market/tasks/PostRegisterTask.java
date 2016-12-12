package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.utils.AppPref;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.RequestUtils;

public class PostRegisterTask extends BaseApiTask {
	private final FirebaseAuth mAuth;


	public PostRegisterTask(Context context, FirebaseAuth auth) {
		super(context);
		this.mAuth = auth;
	}

	@Override
	String request() throws IOException, JSONException {
		JSONObject objectRegister = new JSONObject();
		objectRegister.put("device_token", FirebaseInstanceId.getInstance().getToken());
		objectRegister.put("os", "ANDROID");
		objectRegister.put("firebase_id", mAuth.getCurrentUser().getUid());
		objectRegister.put("first_name",mAuth.getCurrentUser().getDisplayName());
		objectRegister.put("email",mAuth.getCurrentUser().getEmail());
		return RequestUtils.requestPOST(APIConfig.URL_32_POST_REGISTER, objectRegister.toString(), null);
	}

	@Override
	protected void parseData(JSONObject jsonObject) throws JSONException, JsonSyntaxException {

//		UserSession userSession= BaseModel.fromJson(jsonObject.getString("user"),UserSession.class);
//		AppPref.getInstance(mContext).putString(AppPref.KEY_USER_FIRST_NAME,
//				userSession.getFirstName());
//		AppPref.getInstance(mContext).putString(AppPref.KEY_USER_LAST_NAME,
//				userSession.getLastName());
		AppPref.getInstance(mContext).putString(AppPref.KEY_AUTH_TOKEN,
				jsonObject.getJSONObject("data").getString("access_token"));
		onSuccess();
	}

	protected void onSuccess() {
	}

	@Override
	protected void onError(int code) {
	}
}

package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.RequestUtils;

public class PostRegisterTask extends BaseApiTask {
	private String mFirstName;
	private String mLastName;
	private String mSocial;

	public static final int ERROR_EMAIL_PHONE_EXIST=2;

	public PostRegisterTask(Context context, String mUserName, String email, String mFirstName, String lastName, String social) {
		super(context);
		this.mFirstName = mFirstName;
		this.mLastName=lastName;
		this.mSocial=social;
	}

	@Override
	String request() throws IOException, JSONException {
		JSONObject objectRegister = new JSONObject();
		objectRegister.put("deviceToken", Authentication.getDeviceToken(getContext()));
		objectRegister.put("os", "ANDROID");
		objectRegister.put("firebase_id", mSocial);
		return RequestUtils.requestPOST(APIConfig.URL_32_POST_REGISTER, objectRegister.toString(), null);
	}

	@Override
	protected void parseData(JSONObject jsonObject) throws JSONException, JsonSyntaxException {
//		UserSession userSession= BaseModel.fromJson(jsonObject.getString("user"),UserSession.class);
//		AppPref.getInstance(mContext).putString(AppPref.KEY_USER_FIRST_NAME,
//				userSession.getFirstName());
//		AppPref.getInstance(mContext).putString(AppPref.KEY_USER_LAST_NAME,
//				userSession.getLastName());
//		AppPref.getInstance(mContext).putString(AppPref.KEY_AUTH_TOKEN,
//				userSession.getToken());
		onSuccess();
	}

	protected void onSuccess() {
	}

	@Override
	protected void onError(int code) {
	}
}

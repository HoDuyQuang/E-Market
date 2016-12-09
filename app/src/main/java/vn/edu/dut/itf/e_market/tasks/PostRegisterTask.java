package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import vn.com.brycen.restaurant.api.APIConfig;
import vn.com.brycen.restaurant.models.BaseModel;
import vn.com.brycen.restaurant.models.UserSession;
import vn.com.brycen.restaurant.utils.AppPref;
import vn.com.brycen.restaurant.utils.Authentication;
import vn.com.brycen.restaurant.utils.RequestUtils;
import vn.com.brycen.restaurant.utils.Validator;

public class PostRegisterTask extends BaseApiTask {
	private String mPhone;
	private String mUserName;
	private String mPassword;
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mSocial;

	public static final int ERROR_EMAIL_PHONE_EXIST=2;

	public PostRegisterTask(Context context, String mUserName, String mPassword, String mFirstName) {
		super(context);
		this.mUserName = mUserName;
		this.mPassword = mPassword;
		this.mFirstName = mFirstName;
		if (Validator.isValidEmail(mUserName)){
			mEmail=mUserName;
		} else if (Validator.isValidPhone(mUserName)){
			mPhone = mUserName;
		}
	}

	public PostRegisterTask(Context context, String mUserName, String email, String mFirstName, String lastName, String social) {
		super(context);
		this.mUserName = mUserName;
		this.mFirstName = mFirstName;
		this.mLastName=lastName;
		this.mEmail=email;
		this.mSocial=social;
	}

	@Override
	String request() throws IOException, JSONException {
		JSONObject objectRegister = new JSONObject();
		objectRegister.put("restaurantId", APIConfig.RESTAURANT_ID);
		objectRegister.put("deviceToken", Authentication.getDeviceToken(getContext()));
		objectRegister.put("userName", mUserName);
		objectRegister.put("password", mPassword);
		objectRegister.put("firstName", mFirstName);
		objectRegister.put("social", mSocial);
		objectRegister.put("email", mEmail);
		objectRegister.put("phone", mPhone);
		return RequestUtils.requestPOST(APIConfig.URL_32_POST_REGISTER, objectRegister.toString(), null);
	}

	@Override
	protected void parseData(JSONObject jsonObject) throws JSONException, JsonSyntaxException {
		UserSession userSession= BaseModel.fromJson(jsonObject.getString("user"),UserSession.class);
		AppPref.getInstance(mContext).putString(AppPref.KEY_USER_FIRST_NAME,
				userSession.getFirstName());
		AppPref.getInstance(mContext).putString(AppPref.KEY_USER_LAST_NAME,
				userSession.getLastName());
		AppPref.getInstance(mContext).putString(AppPref.KEY_AUTH_TOKEN,
				userSession.getToken());
		onSuccess();
	}

	protected void onSuccess() {
	}

	@Override
	protected void onError(int code) {
	}
}

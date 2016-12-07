package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.models.BaseModel;
import vn.edu.dut.itf.e_market.models.FoodDetail;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.RequestUtils;


public class GetFoodDetailTask extends BaseApiTask {

	private int mFoodId;

	public GetFoodDetailTask(Context context, int foodId) {
		super(context);
		this.mFoodId = foodId;
	}

	@Override
	String request() throws IOException, ServerUnavailableException {
		Map<String, String> mapParam = new HashMap<>();
		mapParam.put("foodId", mFoodId + "");
		return RequestUtils.sendGET(APIConfig.URL_37_GET_FOOD_DETAIL, mapParam, Authentication.getAuthToken(mContext));
	}

	@Override
	protected void onError(int code) {

	}

	@Override
	protected void parseData(JSONObject result) throws JSONException {
		FoodDetail info = BaseModel.fromJson(result.toString(), FoodDetail.class);
		onSuccess(info);
	}

	protected void onSuccess(FoodDetail info) {

	}

}

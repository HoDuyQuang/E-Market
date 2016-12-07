package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.utils.RequestUtils;


public class GetMenuTask extends BaseApiTask {

	private final int mFrom;
	private final int mCount;
	private boolean isTakeAway;

	public GetMenuTask(Context context, boolean isTakeAway, int from, int count) {
		super(context);
		this.isTakeAway = isTakeAway;
		this.mFrom=from;
		this.mCount=count;
	}

	@Override
	String request() throws IOException, ServerUnavailableException {
		HashMap<String, String> params = new HashMap<>();
		params.put("isTakeAway", isTakeAway ? "1" : "0");
		params.put("restaurantId", APIConfig.RESTAURANT_ID);
		params.put("from", String.valueOf(mFrom));
		params.put("count", String.valueOf(mCount));
		return RequestUtils.sendGET(APIConfig.URL_5_GET_LIST_MENU, params, null);
	}

	@Override
	protected void onError(int code) {

	}

	@Override
	protected void parseData(JSONObject jsonObject) throws JSONException, JsonSyntaxException {
//		ArrayList<Food> suggests = BaseModel.listFromJsonArray(jsonObject.getString("listSuggest"),
//				new TypeToken<ArrayList<Food>>() {
//				});
		onSuccess(categories,itemCount);
	}

	protected void onSuccess(List<Category> categories,int count) {

	}

}

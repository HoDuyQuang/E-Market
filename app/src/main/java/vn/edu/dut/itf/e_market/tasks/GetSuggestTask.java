package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.models.BaseModel;
import vn.edu.dut.itf.e_market.models.Food;
import vn.edu.dut.itf.e_market.utils.RequestUtils;


public class GetSuggestTask extends BaseApiTask {
	private boolean isTakeAway;

	public GetSuggestTask(Context context, boolean isTakeAway) {
		super(context);
		this.isTakeAway = isTakeAway;
	}

	@Override
	String request() throws IOException, ServerUnavailableException {
		HashMap<String, String> params = new HashMap<>();
		params.put("isTakeAway", isTakeAway ? "1" : "0");
		return RequestUtils.sendGET(APIConfig.URL_48_GET_SUGGEST, params, null);
	}

	@Override
	protected void onError(int code) {

	}

	@Override
	protected void parseData(JSONObject jsonObject) throws JSONException, JsonSyntaxException {
		ArrayList<Food> suggests = BaseModel.listFromJsonArray(jsonObject.getString("listSuggest"),
				new TypeToken<ArrayList<Food>>() {
				});
		onSuccess(suggests);
	}

	protected void onSuccess(ArrayList<Food> suggests) {
	}
}

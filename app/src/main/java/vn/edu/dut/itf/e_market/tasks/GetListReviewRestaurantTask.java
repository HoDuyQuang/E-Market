package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.models.BaseModel;
import vn.edu.dut.itf.e_market.models.Review;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.RequestUtils;


public class GetListReviewRestaurantTask extends BaseApiTask {


	private final int mStart;
	private final int mCount;

	public GetListReviewRestaurantTask(Context context, int start, int end) {
		super(context);
		this.mStart=start;
		this.mCount=end;
	}

	@Override
	String request() throws IOException, ServerUnavailableException {
		HashMap<String, String> params = new HashMap<>();
		params.put("from", mStart+"");
		params.put("count", mCount+"");
		params.put("restaurantId", APIConfig.RESTAURANT_ID);
		return RequestUtils.sendGET(APIConfig.URL_11_GET_LIST_REVIEW_RESTAURANT,params, Authentication.getAuthToken(mContext));
	}

	@Override
	protected void onError(int code) {

	}

	@Override
	protected void parseData(JSONObject jsonObject) throws JSONException {
//		RestaurantRate rate = BaseModel.fromJson(jsonObject.getJSONObject("restaurant").getString("rate"),
//				RestaurantRate.class);
		ArrayList<Review> reviewHistoryRestaurant = BaseModel.listFromJsonArray(jsonObject.getString("listReview"),
				new TypeToken<ArrayList<Review>>() {
				});
		int reviewCount = jsonObject.getInt("reviewCount");
		onSuccess(reviewHistoryRestaurant, reviewCount);
	}

	protected void onSuccess(ArrayList<Review> info, int reviewCount) {

	}

}

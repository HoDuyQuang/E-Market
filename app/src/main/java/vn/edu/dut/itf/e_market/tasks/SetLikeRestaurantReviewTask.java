package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.RequestUtils;

public class SetLikeRestaurantReviewTask extends BaseApiTask {

    private final String mReviewId;
    private final int mLike;

    public SetLikeRestaurantReviewTask(Context context, String reviewId, int like) {
        super(context);
        this.mReviewId=reviewId;
        this.mLike=like;
    }

    @Override
    String request() throws IOException, JSONException, ServerUnavailableException {
        HashMap<String, String> params = new HashMap<>();
        params.put("reviewId", mReviewId);
        params.put("like", mLike+"");
        return RequestUtils.sendGET(APIConfig.URL_41_LIKE_RESTAURANT_REVIEW, params, Authentication.getAuthToken(getContext()));
    }

    @Override
    protected void onError(int code) {

    }

    @Override
    protected void parseData(JSONObject result) throws JSONException {
        onSuccess();
    }

    protected void onSuccess() {
    }

}

package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.RequestUtils;


public class SetLikeRestaurantReviewCommentTask extends BaseApiTask {

    private final String mCommentId;
    private final int mLike;

    public SetLikeRestaurantReviewCommentTask(Context context, String commentId, int like) {
        super(context);
        this.mCommentId =commentId;
        this.mLike=like;
    }

    @Override
    String request() throws IOException, JSONException, ServerUnavailableException {
        HashMap<String, String> params = new HashMap<>();
        params.put("commentId", mCommentId);
        params.put("like", mLike+"");
        return RequestUtils.sendGET(APIConfig.URL_43_LIKE_RESTAURANT_REVIEW_COMMENT, params, Authentication.getAuthToken(getContext()));
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

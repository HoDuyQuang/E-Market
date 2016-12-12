package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.utils.Authentication;
import vn.edu.dut.itf.e_market.utils.RequestUtils;


public class PostOrderTask extends BaseApiTask {

    private String title;
    private int collegeId;
    private String content;
    private String address;
    private String phone;
    private String email;
    private int categoryId;


    public PostOrderTask(Context context, String title, String content, int collegeId, String address, String phone, String email, int categoryId) {
        super(context);
        this.title = title;
        this.content = content;
        this.collegeId = collegeId;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.categoryId = categoryId;
    }

    @Override
    String request() throws IOException, JSONException {
        Map<String, String> info = new HashMap<>();
        info.put("title", title);
        info.put("content", content);
        info.put("district", String.valueOf(collegeId));
        info.put("address", address);
        info.put("phone", phone);
        info.put("email", email);
        info.put("categoryId", String.valueOf(categoryId));
        return RequestUtils.sendPost(APIConfig.URL_28_POST_ORDER, Authentication.getAuthToken(mContext), info, null);
    }

    @Override
    protected void parseData(JSONObject result) throws JSONException, JsonSyntaxException {
        onSuccess();
    }

    protected void onSuccess() {
    }

    @Override
    protected void onError(int code) {
        // TODO Auto-generated method stub

    }
}

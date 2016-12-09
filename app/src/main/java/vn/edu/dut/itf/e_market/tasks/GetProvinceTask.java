package vn.edu.dut.itf.e_market.tasks;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import vn.edu.dut.itf.e_market.api.APIConfig;
import vn.edu.dut.itf.e_market.database.ProvinceDao;
import vn.edu.dut.itf.e_market.models.BaseModel;
import vn.edu.dut.itf.e_market.models.College;
import vn.edu.dut.itf.e_market.models.District;
import vn.edu.dut.itf.e_market.utils.AppPref;
import vn.edu.dut.itf.e_market.utils.RequestUtils;


public class GetProvinceTask extends BaseApiTask {

	public GetProvinceTask(Context context) {
		super(context);
	}

	@Override
	String request() throws IOException, ServerUnavailableException {
		HashMap<String, String> params = new HashMap<>();
		return RequestUtils.sendGET(APIConfig.URL_29_GET_LIST_PROVINCES, params);
	}

	@Override
	protected void onError(int code) {

	}

	@Override
	protected void parseData(JSONObject jsonObject) throws JSONException {
		Type listType = new TypeToken<List<District>>() {
		}.getType();
		List<District> district = BaseModel.getGson().fromJson(jsonObject.getJSONObject("data").getString("districts"), listType);
		listType = new TypeToken<List<College>>() {
		}.getType();
		List<College> listCollege = BaseModel.getGson().fromJson(jsonObject.getJSONObject("data").getString("colleges"), listType);

		ProvinceDao dao = new ProvinceDao(mContext);
		dao.deleteAll();
		dao.saveDistrict(district);
		dao.saveCollege(listCollege);
		AppPref.getInstance(mContext).putBoolean(AppPref.KEY_PROVINCE, true);

		onSuccess(district);

	}

	protected void onSuccess(List<District> districts) {
	}
}

package vn.edu.dut.itf.e_market.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;

import vn.com.brycen.restaurant.api.BooleanSerializer;
import vn.com.brycen.restaurant.api.DateTimeSerializer;
import vn.com.brycen.restaurant.api.LatLongSerializer;
import vn.com.brycen.restaurant.api.OpenTimeSerializer;

public abstract class BaseModel {

	private static Gson mGson;

	public static <T extends BaseModel> T fromJson(String json, Class<T> clazz) {
		return getGson().fromJson(json, clazz);
	}

	public static <T extends BaseModel> ArrayList<T> listFromJsonArray(String jsonArrayString,
																	   TypeToken<ArrayList<T>> typeToken) {
		return getGson().fromJson(jsonArrayString, typeToken.getType());
	}

	public static Gson getGson() {
		if (mGson == null) {
			GsonBuilder b = new GsonBuilder();
			BooleanSerializer serializer = new BooleanSerializer();
			b.registerTypeAdapter(Boolean.class, serializer);
			b.registerTypeAdapter(boolean.class, serializer);
			b.registerTypeAdapter(LatLng.class, new LatLongSerializer());
			b.registerTypeAdapter(OpenTime.class, new OpenTimeSerializer());
			b.registerTypeAdapter(Date.class, new DateTimeSerializer());
			mGson = b.create();
		}
		return mGson;
	}
}

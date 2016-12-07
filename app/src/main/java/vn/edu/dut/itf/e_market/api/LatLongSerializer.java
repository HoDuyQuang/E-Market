package vn.edu.dut.itf.e_market.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class LatLongSerializer implements JsonDeserializer<LatLng> {

	@Override
	public LatLng deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		JsonObject object = arg0.getAsJsonObject();
		return new LatLng(object.get("lat").getAsDouble(), object.get("long").getAsDouble());
	}
}

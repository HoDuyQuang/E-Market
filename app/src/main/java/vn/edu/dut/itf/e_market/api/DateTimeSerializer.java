package vn.edu.dut.itf.e_market.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {

        String str = arg0.getAsString();
        SimpleDateFormat formatter;
        if (str != null) {
            if (str.contains("/")) {
                formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
            } else if (str.contains(":")) {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
            }
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                return formatter.parse(str);
            } catch (ParseException e) {
                // throw new JsonParseException("Can't parse \"" + str + "\" to " +
                // OpenTime.class.getName());
                e.printStackTrace();
                return null;
            }
        } else{
            return null;
        }

    }
}

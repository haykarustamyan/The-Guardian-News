package am.highapps.theguardiannews.di.network;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

public class DateTimeConverter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

    @Override
    public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
        final DateTimeFormatter fmt = ISODateTimeFormat.dateTimeParser();
        return new JsonPrimitive(fmt.print(src));
    }

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
         if (json.getAsString() == null || json.getAsString().isEmpty()) {
            return null;
        }

        final DateTimeFormatter fmt = ISODateTimeFormat.dateTimeParser();
        return fmt.parseDateTime(json.getAsString());
    }
}


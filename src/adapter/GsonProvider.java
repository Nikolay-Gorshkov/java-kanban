package adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(java.time.Duration.class, new DurationAdapter())
            .registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static Gson getGson() {
        return gson;
    }
}
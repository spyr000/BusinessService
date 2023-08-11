package cs.vsu.businessservice.config;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.json.Json;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GsonConfig {
    @Bean
    JsonDeserializer<LocalDateTime> localDateJsonDeserializer() {
        return (jsonElement, type, jsonDeserializationContext) -> LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
    }

    @Bean
    JsonSerializer<Json> springfoxJsonToGsonAdapter() {
        return (json, type, context) -> JsonParser.parseString(json.value());
    }

    @Bean
    public Gson gson(JsonDeserializer<LocalDateTime> jsonDeserializer) {
        var gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, jsonDeserializer);
        gsonBuilder.registerTypeAdapter(Json.class, springfoxJsonToGsonAdapter());

        return gsonBuilder.setPrettyPrinting().create();
    }

//    private final Gson gson = new GsonBuilder()
//            .registerTypeAdapter(Json.class, jsonSerializer())
//            .create();

//    private static class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {
//        @Override
//        public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
//            return JsonParser.parseString(json.value());
//        }
//    }
}

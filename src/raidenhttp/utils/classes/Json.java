package raidenhttp.utils.classes;

// Imports
import com.google.gson.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Json {
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();

    public static JsonElement toJson(Object object) {
        return gson.toJsonTree(object);
    }

    public static JsonElement parseObject(Reader json) {
        return new JsonParser().parse(json);
    }

    public static JsonElement parseObject(String json) {
        return new JsonParser().parse(json);
    }

    public static String toJsonString(Object object) {
        return gson.toJson(object);
    }

    /**
     * Encode an object to a JSON string
     */
    public static String encode(Object object) {
        return gson.toJson(object);
    }

    /**
     * Decodes an JSON.
     * @throws JsonSyntaxException The JSON is invalid.
     */
    public static <T> T decode(JsonElement jsonElement, Class<T> classType) throws JsonSyntaxException {
        return gson.fromJson(jsonElement, classType);
    }

    public static <T> T decode(Reader jsonElement, Class<T> classType) throws JsonSyntaxException {
        return gson.fromJson(jsonElement, classType);
    }

    public static <T> T decode(String jsonData, Class<T> classType) {
        try {
            return gson.fromJson(jsonData, classType);
        } catch (JsonSyntaxException ex) {
            return null;
        }
    }

    public static <T> T loadToClass(Reader fileReader, Class<T> classType) {
        return gson.fromJson(fileReader, classType);
    }

    public static <T> T loadToClass(Path filename, Class<T> classType) throws IOException {
        try (var fileReader = Files.newBufferedReader(filename, java.nio.charset.StandardCharsets.UTF_8)) {
            return loadToClass(fileReader, classType);
        }
    }

    /**
     * Checks if a given object matches a body structure.
     * @param obj The given object
     */
    public static boolean isFullStructure(Object obj) {
        if (obj == null) {
            return true;
        }

        for (java.lang.reflect.Field field : obj.getClass().getFields()) {
            try {
                if (field.get(obj) == null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                return true;
            }
        }
        return true;
    }
}
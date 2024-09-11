package raidenhttp.utils;

// Imports
import raidenhttp.Main;
import raidenhttp.utils.classes.Json;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public final class Language {
    private static final Map<String, Language> cachedLanguages = new ConcurrentHashMap<>();
    private final Map<String, String> translations = new ConcurrentHashMap<>();

    /**
     * Returns the language and country codes. Like (en-us)
     * @param locale The language object
     * @return The language and country.
     */
    public static String getLanguageCode(Locale locale) {
        return String.format("%s-%s", locale.getLanguage(), locale.getCountry());
    }

    /** Reads a file and creates a language instance. */
    private Language(LanguageStreamDescription description) {
        try {
            var object = Json.decode(FileUtils.readFromInputStream(description.getLanguageFile()), JsonObject.class);
            assert object != null;

            object.entrySet().forEach(entry -> putFlattenedKey(translations, entry.getKey(), entry.getValue()));
        } catch (Exception exception) {
            Main.getLogger().warn("Failed to load language file: {}", description.getLanguageCode(), exception);
        }
    }

    /**
     * Creates a language instance from a code.
     *
     * @param langCode The language code.
     * @return A language instance.
     */
    public static Language getLanguage(String langCode) {
        if (cachedLanguages.containsKey(langCode)) {
            return cachedLanguages.get(langCode);
        }
        var description = Language.getLanguageFileDescription(langCode);
        var actualLanguageCode = description.getLanguageCode();
        Language languageInst;

        if (description.getLanguageFile() != null) {
            languageInst = new Language(description);
            cachedLanguages.put(actualLanguageCode, languageInst);
        } else {
            languageInst = cachedLanguages.get(actualLanguageCode);
            cachedLanguages.put(langCode, languageInst);
        }

        return languageInst;
    }

    /**
     * Returns the translated value from the key while substituting arguments.
     *
     * @param key The key of the translated value to return.
     * @param args The arguments to substitute.
     * @return A translated value with arguments substituted.
     */
    public static String translate(String key, Object... args) {
        String translated = Main.getLanguage().get(key);
        for (int i = 0; i < args.length; i++) {
            args[i] =
                switch (args[i].getClass().getSimpleName()) {
                    case "String" -> args[i];
                    case "TextStrings" -> ((TextStrings) args[i]).get(0).replace("\\\\n", "\\n");
                    default -> args[i].toString();
                };
        }

        try {
            return translated.formatted(args);
        } catch (Exception exception) {
            Main.getLogger().error("Failed to format string: {}", key, exception);
            return translated;
        }
    }

    /**
     * Returns the translated value from the key while substituting arguments.
     *
     * @param locale The locale to use.
     * @param key The key of the translated value to return.
     * @param args The arguments to substitute.
     * @return A translated value with arguments substituted.
     */
    public static String translate(Locale locale, String key, Object... args) {
        if (locale == null) {
            return translate(key, args);
        }

        var langCode = getLanguageCode(locale);
        var translated = getLanguage(langCode).get(key);
        for (var i = 0; i < args.length; i++) {
            args[i] =
                    switch (args[i].getClass().getSimpleName()) {
                        case "String" -> args[i];
                        case "TextStrings" -> ((TextStrings) args[i])
                                .getGC(langCode)
                                .replace("\\\\n", "\n"); // Note that we don't unescape \n for server console
                        default -> args[i].toString();
                    };
        }

        try {
            return translated.formatted(args);
        } catch (Exception ex) {
            Main.getLogger().error("Failed to format string: {}", key, ex);
            return translated;
        }
    }

    /**
     * Recursive helper function to flatten a Json tree Converts input like {"foo": {"bar": "baz"}} to
     * {"foo.bar": "baz"}
     *
     * @param map The map to insert the keys into
     * @param key The flattened key of the current element
     * @param element The current element
     */
    private static void putFlattenedKey(Map<String, String> map, String key, JsonElement element) {
        if (element.isJsonObject()) {
            element
                    .getAsJsonObject()
                    .entrySet()
                    .forEach(
                            entry -> {
                                String keyPrefix = key.isEmpty() ? "" : key + ".";
                                putFlattenedKey(map, keyPrefix + entry.getKey(), entry.getValue());
                            });
        } else {
            map.put(key, element.getAsString());
        }
    }

    /**
     * create a LanguageStreamDescription
     *
     * @param languageCode The name of the language code.
     */
    private static LanguageStreamDescription getLanguageFileDescription(String languageCode) {
        var fileName = languageCode + ".json";

        String actualLanguageCode = languageCode;
        InputStream file = FileUtils.getFileInputStream("resources\\languages\\" + fileName);
        if (file == null) {
            actualLanguageCode = "en-US";
            if (cachedLanguages.containsKey(actualLanguageCode)) {
                return new LanguageStreamDescription(actualLanguageCode, null);
            }
            file = FileUtils.getFileInputStream("resources\\languages\\en-US.json");
        }

        if (file == null)
            throw new RuntimeException("Unable to load en-US language. HTTP Server Halted!");

        return new LanguageStreamDescription(actualLanguageCode, file);
    }

    /**
     * Returns the value (as a string) from a nested key.
     *
     * @param key The key to look for.
     * @return The value (as a string) from a nested key.
     */
    public String get(String key) {
        if (translations.containsKey(key)) return translations.get(key);
        return "This value does not exist : " + key;
    }

    @Getter
    private static class LanguageStreamDescription {
        private final String languageCode;
        private final InputStream languageFile;

        public LanguageStreamDescription(String languageCode, InputStream languageFile) {
            this.languageCode = languageCode;
            this.languageFile = languageFile;
        }

    }

    @EqualsAndHashCode
    public static class TextStrings implements Serializable {
        public static final String[] ARR_LANGUAGES = {
            "EN", "JP"
        };
        public static final String[] ARR_GC_LANGUAGES = {
            "en-US", "ja-JP"
        };
        // Map "EN": 0, "CHS": 1, ..., "VI": 12
        public static final Map<String, Integer> MAP_LANGUAGES = new HashMap<>(IntStream.range(0, ARR_LANGUAGES.length).boxed().collect(Collectors.toMap(i -> ARR_LANGUAGES[i], i -> i)));
        // Map "en-US": 0, "zh-CN": 1, ...
        public static final Map<String, Integer> MAP_GC_LANGUAGES = new HashMap<>(IntStream.range(0, ARR_GC_LANGUAGES.length).boxed().collect(Collectors.toMap(i -> ARR_GC_LANGUAGES[i], i -> i, (i1, i2) -> i1)));
        public String[] strings = new String[ARR_LANGUAGES.length];

        public String get(int languageIndex) {
            return strings[languageIndex];
        }

        public String get(String languageCode) {
            return strings[MAP_LANGUAGES.getOrDefault(languageCode, 0)];
        }

        public String getGC(String languageCode) {
            return strings[MAP_GC_LANGUAGES.getOrDefault(languageCode, 0)];
        }
    }
}

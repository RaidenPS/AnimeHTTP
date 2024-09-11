package raidenhttp.config;

// Imports
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Utils
import raidenhttp.utils.classes.Json;

public final class ConfigManager {
    private static final File configFile = new File("./resources/settings.json");
    public static ConfigContainer httpConfig;

    public static void loadConfig() throws IOException {
        httpConfig = Json.loadToClass(configFile.toPath(), ConfigContainer.class);
    }

    public static void saveConfig()  {
        try (FileWriter file = new FileWriter(configFile)) {
            file.write(Json.encode(httpConfig));
        }
        catch (Exception ignored) {

        }
    }
}

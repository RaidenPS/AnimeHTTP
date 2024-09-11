package raidenhttp;

// Imports
import raidenhttp.authorization.AuthenticationSystem;
import raidenhttp.authorization.DefaultAuthentication;
import raidenhttp.config.*;
import raidenhttp.cryptography.Cryptography;
import raidenhttp.database.DatabaseManager;
import raidenhttp.routers.HTTPServer;
import raidenhttp.routers.gamerouters.*;
import raidenhttp.routers.httprouters.*;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import org.reflections.Reflections;
import org.slf4j.*;

// Translate
import raidenhttp.utils.Language;

public final class Main {
    @Getter private static final Logger logger = LoggerFactory.getLogger(Main.class);
    @Getter @Setter private static AuthenticationSystem authenticationSystem = new DefaultAuthentication();
    private static final HTTPServer httpServer;

    @Getter @Setter private static Language language;
    public static final Reflections reflector = new Reflections("raidenhttp.database.entities");

    static {
        loadConfig();
        loadLanguage();
        startEncryption();
        startDatabase();
        httpServer = new HTTPServer();
    }

    private static void loadConfig() {
        try {
            ConfigManager.loadConfig();
            logger.info("The config was loaded successfully.");
        }
        catch (IOException e) {
            logger.warn("Unable to load the config, using a remade one.");
            ConfigManager.httpConfig = new ConfigContainer();
            ConfigManager.saveConfig();
        }
    }

    private static void loadLanguage() {
        language = Language.getLanguage(Language.getLanguageCode(ConfigManager.httpConfig.httpLang));
    }

    private static void startDatabase()  {
        try {
            DatabaseManager.initialize(ConfigManager.httpConfig.dbInfo.url, ConfigManager.httpConfig.dbInfo.name);
            logger.info("Database initialized successfully. URL: {}", ConfigManager.httpConfig.dbInfo.url);
        }catch (Exception e) {
            logger.error("Unable to start the database. (Server aborted) Error: {}", e.getMessage());
            System.exit(1);
        }

    }

    private static void startEncryption() {
        try {
            Cryptography.loadCryptoKeys();
            logger.info("Encryption initialized successfully.");
        }catch (Exception e) {
            logger.error("Unable to load the encryption keys. (Server aborted) Error: {}", e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        /// HTTP Errors
        httpServer.addRouter(BadMessageHTTPError.class);
        httpServer.addRouter(Index.class);
        httpServer.addRouter(ForbiddenHTTPError.class);
        httpServer.addRouter(NoFileFoundHTTPError.class);
        httpServer.addRouter(ServerHTTPError.class);

        /// Game Resources
        httpServer.addRouter(WebstaticRouter.class);

        /// Rendering (For registration and forgotten password)
        httpServer.addRouter(RenderingRouter.class);

        /// In-game requests
        httpServer.addRouter(AuthorizationRequestRouter.class);
        httpServer.addRouter(GameLoggingRequestRouter.class);
        httpServer.addRouter(GenericRequestRouter.class);
        httpServer.addRouter(RegionRequestRouter.class);
        httpServer.addRouter(RegisterRequestRouter.class);
        httpServer.addRouter(LauncherRequestRouter.class);
        httpServer.addRouter(LoginRequestRouter.class);
        httpServer.addRouter(PaymentRequestRouter.class);
        httpServer.addRouter(AndroidGenericRequestRouter.class);
        httpServer.addRouter(CaptchaRequestRouter.class);
        httpServer.addRouter(AnnouncementRequestRouter.class);

        try {
            httpServer.start();
        }catch (Exception e) {
            logger.error("Unable to start the http server. Error: {0}", e.getCause());
            System.exit(1);
        }
    }
}
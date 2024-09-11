package raidenhttp.config;

// Imports
import raidenhttp.config.objects.Region;
import raidenhttp.enums.RegionEnvironments;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ConfigContainer {
    public Locale httpLang = Locale.US;
    public APIInformation apiInfo = new APIInformation();
    public DBInformation dbInfo = new DBInformation();
    public GameInformation gameInfo = new GameInformation();

    /// Connection information
    public boolean useHTTPSProtocol = false;
    public String certPassword = "GENSHINIMPACTCERTIFICATE";
    public String host = "127.0.0.1";
    public int port = 8881;

    /// Debug
    public boolean logRequests = false;
    public boolean collectGameLogs = false;

    /// Maintenance
    public boolean isMaintenance = false;
    public int maintenanceStartDate = 0;
    public int maintenanceEndDate = 0;
    public String maintenanceUrl = "https://www.hoyolab.com/";
    public String maintenanceMsg = "Head on over to HoYoLAB to discuss new content updates and check out the latest game news!";

    /// Update
    public String forceUpdateUrl = "127.0.0.1";

    /// Rate limit
    public int maxRequests = 1000;
    public int timeMillis = 1000;
    public Set<String> badIPS = new HashSet<>();
    public boolean enableIPWhitelist = false;
    public Set<String> whiteListIPS = new HashSet<>();

    /// Genshin miscellaneous
    public boolean enable_captcha = true;
    public boolean disableRegistrations = false;
    public boolean enable_guests = true;
    public boolean is_heartbeat_required = false;
    public boolean is_realname_required = false;
    public boolean is_guardian_required = false;
    public boolean display_retpoint = false;
    public boolean enable_consent_banner = false;
    public boolean crypt_password = true; // Disable if login send raw password.
    public RegionEnvironments envType = RegionEnvironments.PRODUCTION_OS;

    /// Launcher constants
    public String const_launcher_id = "8fANlj5K7I";

    /// Android miscellaneous
    public int android_report_interval_seconds = 3;
    public boolean android_enable_device_report = true;
    public int firebase_device_blacklist_version = 1;
    public boolean firebase_blacklist_devices_switch = false;

    /// Regions
    public List<Region> regions = new ArrayList<>(List.of(
            new Region("os_jp69", "Localhost", "DEV_PUBLIC", "127.0.0.1", 8882)
    ));

    public static class APIInformation {
        public String ipinfodb = "";
    }

    public static class DBInformation {
        public String url = "mongodb://localhost:27017";
        public String name = "raidenps";
        public int startPlayerCounterPosition = 100000;
    }

    public static class GameInformation {
        public String gameServerIP = "127.0.0.1";
        public int gameServerPort = 8882;
        public String gameVersion = "4.6.0";
        public String gateTicket = "I_AM_SPYWARE_GIVE_ME_DATA_UWU";
    }
}
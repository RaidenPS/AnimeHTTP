package raidenhttp.utils;

// Imports
import raidenhttp.config.ConfigManager;
import raidenhttp.enums.PlatformType;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.*;

public final class Utils {
    /**
     * @return Formatted announcement url.
     */
    public static String getAnnouncementUrl() {
        String protocol = ConfigManager.httpConfig.useHTTPSProtocol ? "https" : "http";
        String clientType = switch (ConfigManager.httpConfig.envType) {
            case PRODUCTION_CN, BETA_CN, PRODUCTION_PRE_RELEASE_CN, HOTFIX_CN, SANDBOX_CN, BETA_CN_PRE, TEST_CN, PET_CN -> "hk4e_cn";
            default -> "hk4e_global";
        };

        return "%s://sdk.hoyoverse.com/announcements/index.tmpl?sdk_presentation_style=fullscreen&announcement_version=1.46&sdk_screen_transparent=true&game_biz=%s&auth_appid=announcement&game=hk4e#/".formatted(protocol, clientType);
    }

    /**
     * Fetches all international country phone number indexes.
     * @return The phone number and country codes.
     */
    public static JsonArray getAreaCode() {
        String[][] countryCodes = {
            {"CN", "86"}, {"AF", "93"}, {"AL", "355"}, {"DZ", "213"}, {"AS", "1684"}, {"AD", "376"},
            {"AO", "244"}, {"AI", "1264"}, {"AG", "1268"}, {"AR", "54"}, {"AM", "374"}, {"AW", "297"},
            {"AU", "61"}, {"AT", "43"}, {"AZ", "994"}, {"BS", "1242"}, {"BH", "973"}, {"BD", "880"},
            {"BB", "1246"}, {"BY", "375"}, {"BE", "32"}, {"BZ", "501"}, {"BJ", "229"}, {"BM", "1441"},
            {"BT", "975"}, {"BO", "591"}, {"BA", "387"}, {"BW", "267"}, {"BR", "55"}, {"BN", "673"},
            {"BG", "359"}, {"BF", "226"}, {"BI", "257"}, {"KH", "855"}, {"CM", "237"}, {"CA", "1"},
            {"CV", "238"}, {"KY", "1345"}, {"CF", "236"}, {"TD", "235"}, {"CL", "56"}, {"CO", "57"},
            {"KM", "269"}, {"CK", "682"}, {"CR", "506"}, {"HR", "385"}, {"CU", "53"}, {"CW", "599"},
            {"CY", "357"}, {"CZ", "420"}, {"CD", "243"}, {"DK", "45"}, {"DJ", "253"}, {"DM", "1767"},
            {"DO", "1809"}, {"EC", "593"}, {"EG", "20"}, {"SV", "503"}, {"GQ", "240"}, {"ER", "291"},
            {"EE", "372"}, {"ET", "251"}, {"FO", "298"}, {"FJ", "679"}, {"FI", "358"}, {"FR", "33"},
            {"GF", "594"}, {"PF", "689"}, {"GA", "241"}, {"GM", "220"}, {"GE", "995"}, {"DE", "49"},
            {"GH", "233"}, {"GI", "350"}, {"GR", "30"}, {"GL", "299"}, {"GD", "1473"}, {"GP", "590"},
            {"GU", "1671"}, {"GT", "502"}, {"GN", "224"}, {"GW", "245"}, {"GY", "592"}, {"HT", "509"},
            {"HN", "504"}, {"HK", "852"}, {"HU", "36"}, {"IS", "354"}, {"IN", "91"}, {"ID", "62"},
            {"IR", "98"}, {"IQ", "964"}, {"IE", "353"}, {"IL", "972"}, {"IT", "39"}, {"CI", "225"},
            {"JM", "1876"}, {"JP", "81"}, {"JO", "962"}, {"KZ", "7"}, {"KE", "254"}, {"KI", "686"},
            {"KW", "965"}, {"KG", "996"}, {"LA", "856"}, {"LV", "371"}, {"LB", "961"}, {"LS", "266"},
            {"LR", "231"}, {"LY", "218"}, {"LI", "423"}, {"LT", "370"}, {"LU", "352"}, {"MO", "853"},
            {"MK", "389"}, {"MG", "261"}, {"MW", "265"}, {"MY", "60"}, {"MV", "960"}, {"ML", "223"},
            {"MT", "356"}, {"MQ", "596"}, {"MR", "222"}, {"MU", "230"}, {"YT", "269"}, {"MX", "52"},
            {"MD", "373"}, {"MC", "377"}, {"MN", "976"}, {"ME", "382"}, {"MS", "1664"}, {"MA", "212"},
            {"MZ", "258"}, {"MM", "95"}, {"NA", "264"}, {"NP", "977"}, {"NL", "31"}, {"NC", "687"},
            {"NZ", "64"}, {"NI", "505"}, {"NE", "227"}, {"NG", "234"}, {"NO", "47"}, {"OM", "968"},
            {"PK", "92"}, {"PW", "680"}, {"BL", "970"}, {"PA", "507"}, {"PG", "675"}, {"PY", "595"},
            {"PE", "51"}, {"PH", "63"}, {"PL", "48"}, {"PT", "351"}, {"PR", "1787"}, {"QA", "974"},
            {"CG", "242"}, {"RE", "262"}, {"RO", "40"}, {"RU", "7"}, {"RW", "250"}, {"KN", "1869"},
            {"LC", "1758"}, {"PM", "508"}, {"VC", "1784"}, {"WS", "685"}, {"SM", "378"}, {"ST", "239"},
            {"SA", "966"}, {"SN", "221"}, {"RS", "381"}, {"SC", "248"}, {"SL", "232"}, {"SG", "65"},
            {"SX", "1721"}, {"SK", "421"}, {"SI", "386"}, {"SB", "677"}, {"SO", "252"}, {"ZA", "27"},
            {"KR", "82"}, {"ES", "34"}, {"LK", "94"}, {"SD", "249"}, {"SR", "597"}, {"SZ", "268"},
            {"SE", "46"}, {"CH", "41"}, {"SY", "963"}, {"TW", "886"}, {"TJ", "992"}, {"TZ", "255"},
            {"TH", "66"}, {"TL", "670"}, {"TG", "228"}, {"TO", "676"}, {"TT", "1868"}, {"TN", "216"},
            {"TR", "90"}, {"TM", "993"}, {"TC", "1649"}, {"UG", "256"}, {"UA", "380"}, {"AE", "971"},
            {"GB", "44"}, {"US", "1"}, {"UY", "598"}, {"UZ", "998"}, {"VU", "678"}, {"VE", "58"},
            {"VN", "84"}, {"VG", "1340"}, {"VI", "1284"}, {"YE", "967"}, {"ZM", "260"}, {"ZW", "263"}
        };

        JsonArray areas = new JsonArray();
        for(String[] info : countryCodes) {
            JsonObject area = new JsonObject();
            area.addProperty("country_code", info[0]);
            area.addProperty("area_code", info[1]);
            areas.add(area);
        }
        return areas;
    }

    /**
     * @return The dispatch domain.
     */
    public static String getDispatchDomain() {
        return "http%s://%s:%d".formatted(ConfigManager.httpConfig.useHTTPSProtocol ? "s" : "", ConfigManager.httpConfig.host, ConfigManager.httpConfig.port);
    }

    /**
     * Additional information of given IP Address.
     * @param ip The IP Address.
     */
    public static JsonObject getIpInfo(String ip) {
        String url = "https://api.ipinfodb.com/v3/ip-city/?ip=%s&format=json&key=%s".formatted(ip, ConfigManager.httpConfig.apiInfo.ipinfodb);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new JsonParser().parse(response.body()).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Masks a visible email address.
     * @param email The given email address.
     * @return Censored (masked) version of given email address.
     */
    public static String getMaskedEmailAddress(String email) {
        int atIndex = email.indexOf('@');
        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);

        if (localPart.length() > 2) {
            String maskedLocalPart = localPart.charAt(0) + "****" + localPart.charAt(localPart.length() - 1);
            return maskedLocalPart + domainPart;
        }
        return email;
    }

    /**
     * Gets the game biz id.
     * @return The game biz id. (HK4E_GLOBAL is for overseas version and HK4E_CN is chinese).
     */
    public static String getGameBiz() {
        return Utils.getGameBiz(4);
    }

    /**
     * Gets the game biz id of any game.
     * @param app_id The app id.
     * @return The game biz id. (XXX_GLOBAL is for overseas version and XXX_CN is chinese).
     */
    public static String getGameBiz(Integer app_id) {
        String game_name = getGameName(app_id);
        String biz = switch (game_name) {
            case "未定事件簿" -> /// Tears of themis
                    "nxx";
            case "原神海外" -> /// Genshin impact (Overseas)
                    "hk4e";
            case "崩坏3美服", "崩坏3-韩国", "崩坏3-东南亚", "崩坏3台服", "崩坏3日本" -> "bh3";
            case "崩坏RPG" -> /// Honkai Impact
                    "hkrpg";
            case "绝区零" -> /// ZZZ
                    "nap";
            case "HYG" -> /// HYG
                    "hyg";
            default -> "";
        };

        switch(ConfigManager.httpConfig.envType) {
            case PRODUCTION_CN, BETA_CN, PRODUCTION_PRE_RELEASE_CN, HOTFIX_CN, SANDBOX_CN, BETA_CN_PRE, TEST_CN, PET_CN:
                biz += "_cn";
                break;
            default:
                biz += "_global";
                break;
        }
        return biz;
    }

    /**
     * Gets the app required special fonts. (Chinese, Japanese, Russian, ...)
     * @param app_id The app id.
     * @return Array of all fonts in specific game.
     */
    public static JsonArray getGameFonts(Integer app_id) {
        String[][] gameFonts = {
                {"GENSHIN", "0", "0", "zh-cn.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/04/24/4398dec1a0ffa3d3ce99ef1424107550_974225940805884271.ttf", "4398dec1a0ffa3d3ce99ef1424107550"},
                {"GENSHIN", "0", "0", "ja.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/04/24/2c148f36573625fc03c82579abd26fb1_2385499392140544202.ttf", "2c148f36573625fc03c82579abd26fb1"},
                {"HONKAI", "0", "0", "zh-tw.ttf", "https://sdk.hoyoverse.com/upload/font-lib/2023/04/03/de760e04ca4f306d8b6541527fee2997_8472679074313216343.ttf", "de760e04ca4f306d8b6541527fee2997"},
                {"HONKAI", "0", "0", "ja.ttf", "https://sdk.hoyoverse.com/upload/font-lib/2023/04/03/20d288367d02854eb4126deca253e0be_5597006882288082694.ttf", "20d288367d02854eb4126deca253e0be"},
                {"HONKAI", "0", "0", "zh-cn.ttf", "https://sdk.hoyoverse.com/upload/font-lib/2023/04/03/de5cd121579e77d892428b353995e20a_8206319265421393552.ttf", "de5cd121579e77d892428b353995e20a"},
                {"ZZZ", "0", "0", "vi.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/02/20/0a8b673cc3409e2230be14743696226e_8335862322067168213.ttf", "0a8b673cc3409e2230be14743696226e"},
                {"ZZZ", "0", "0", "zn-cn.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/18/7f7b01ec2cac00007d40d9592f2580ac_8235882891105626857.ttf", "7f7b01ec2cac00007d40d9592f2580ac"},
                {"ZZZ", "0", "0", "zn-tw.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/18/6d2aa2d32797e4675188983c61e926db_758355612854684273.ttf", "6d2aa2d32797e4675188983c61e926db"},
                {"ZZZ", "0", "0", "en.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/18/7f7b01ec2cac00007d40d9592f2580ac_3133932868754354427.ttf", "7f7b01ec2cac00007d40d9592f2580ac"},
                {"ZZZ", "0", "0", "fr.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/29/d222505df90f85d6f2a2d60ded2b82bf_2458466084406195868.ttf", "d222505df90f85d6f2a2d60ded2b82bf"},
                {"ZZZ", "0", "0", "de.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/29/d222505df90f85d6f2a2d60ded2b82bf_3284642028458118416.ttf", "d222505df90f85d6f2a2d60ded2b82bf"},
                {"ZZZ", "0", "0", "es.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/29/d222505df90f85d6f2a2d60ded2b82bf_7200057103274866869.ttf", "d222505df90f85d6f2a2d60ded2b82bf"},
                {"ZZZ", "0", "0", "pt.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/29/d222505df90f85d6f2a2d60ded2b82bf_205303420579299358.ttf", "d222505df90f85d6f2a2d60ded2b82bf"},
                {"ZZZ", "0", "0", "ru.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/29/d222505df90f85d6f2a2d60ded2b82bf_2192354070051318754.ttf", "d222505df90f85d6f2a2d60ded2b82bf"},
                {"ZZZ", "0", "0", "ja.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/18/78aa5d2e79b56cb02046c3819121e8b4_748165568467427623.ttf", "78aa5d2e79b56cb02046c3819121e8b4"},
                {"ZZZ", "0", "0", "ko.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/18/9d228846e992d4f3b753a8bfed6edec0_8784861723264098444.ttf", "9d228846e992d4f3b753a8bfed6edec0"},
                {"ZZZ", "0", "0", "th.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/18/1488bafad11fe595e96b956e48a24b25_6611616052069476554.ttf", "1488bafad11fe595e96b956e48a24b25"},
                {"ZZZ", "0", "0", "id.ttf", "https://sdk.hoyoverse.com/sdk-public/2024/01/18/7f7b01ec2cac00007d40d9592f2580ac_7863616432837604663.ttf", "7f7b01ec2cac00007d40d9592f2580ac"}
        };

        JsonArray fonts = new JsonArray();
        for (String[] font : gameFonts) {
            if((app_id == 4 && ("GENSHIN".equals(font[0]))) || (app_id == 9 && ("GENSHIN".equals(font[0]))) || (app_id == 11 && ("HONKAI".equals(font[0]))) || (app_id == 15 && ("ZZZ".equals(font[0])))) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("font_id", font[1]);
                jsonObject.addProperty("app_id", Integer.valueOf(font[2]));
                jsonObject.addProperty("name", font[3]);
                jsonObject.addProperty("url", font[4]);
                jsonObject.addProperty("md5", font[5]);
                fonts.add(jsonObject);
            }
        }
        return fonts;
    }

    /**
     * Gets the specific game developed by Mihoyo.
     * @param app_id The game id
     * @return The game name. (Some of them are unknown)
     */
    public static String getGameName(Integer app_id) {
        return switch (app_id) {
            case 2 -> "未定事件簿";
            case 4 -> "原神海外"; /// Genshin overseas
            case 5 -> "崩坏3rd-欧美";
            case 6 -> "崩坏3-韩国";
            case 7 -> "崩坏3美服";
            case 8 -> "崩坏3-东南亚";
            case 9 -> "崩坏3台服";
            case 10 -> "未定事件薄";
            case 11 -> "崩坏RPG"; /// Honkai RPG
            case 12 -> "平台测试"; /// Test
            case 13 -> "崩坏3日本"; // Houkai 3d
            case 14 -> "崩3-欧洲";
            case 15 -> "绝区零"; /// ZZZ
            case 16 -> "云星穹铁道";
            case 17 -> "云原神境外";
            case 18 -> "HYG";
            default -> "";
        };
    }

    /**
     * Gets the specific genshin game name developed my Mihoyo. (Between overseas and chinese)
     * @param app_id - The genshin game id.
     * @return The game name.
     */
    public static String getGameNameGenshin(Integer app_id) {
        /// Special only for genshin versions.
        return switch (app_id) {
            case 4 -> "原神海外";
            case 9 -> "原神海外"; /// Sandboxed
            case 10 -> "原神";
            default -> "";
        };
    }

    /**
     * Returns all extensions for a given platform.
     * @param platformID The platform ID
     * @return A list contains all platform extensions. (Probably it's for anti cheat or something)
     */
    public static JsonArray getPlatformExtensions(Integer platformID) {
        JsonArray extensions = new JsonArray();
        PlatformType x = PlatformType.fromValue(platformID);
        switch(x) {
            case PLATFORM_IOS, PLATFORM_CLOUD_IOS:
                extensions.add("IDFV");
                extensions.add("model");
                extensions.add("osVersion");
                extensions.add("screenSize");
                extensions.add("vendor");
                extensions.add("cpuType");
                extensions.add("cpuCores");
                extensions.add("isJailBreak");
                extensions.add("networkType");
                extensions.add("proxyStatus");
                extensions.add("batteryStatus");
                extensions.add("chargeStatus");
                extensions.add("romCapacity");
                extensions.add("romRemain");
                extensions.add("ramCapacity");
                extensions.add("ramRemain");
                extensions.add("appMemory");
                extensions.add("accelerometer");
                extensions.add("gyroscope");
                extensions.add("magnetometer");
                extensions.add("deviceName");
                extensions.add("screenBrightness");
                extensions.add("isSimInserted");
                extensions.add("isPushEnabled");
                extensions.add("buildTime");
                extensions.add("appInstallTimeDiff");
                extensions.add("appUpdateTimeDiff");
                extensions.add("hasVpn");
                extensions.add("packageName");
                extensions.add("packageVersion");
                break;
            case PLATFORM_ANDROID, PLATFORM_CLOUD_ANDROID:
                extensions.add("adid");
                extensions.add("app_set_id");
                extensions.add("serialNumber");
                extensions.add("board");
                extensions.add("brand");
                extensions.add("hardware");
                extensions.add("cpuType");
                extensions.add("deviceType");
                extensions.add("display");
                extensions.add("hostname");
                extensions.add("manufacturer");
                extensions.add("productName");
                extensions.add("model");
                extensions.add("deviceInfo");
                extensions.add("sdkVersion");
                extensions.add("osVersion");
                extensions.add("devId");
                extensions.add("buildTags");
                extensions.add("buildType");
                extensions.add("buildUser");
                extensions.add("buildTime");
                extensions.add("screenSize");
                extensions.add("vendor");
                extensions.add("romCapacity");
                extensions.add("romRemain");
                extensions.add("ramCapacity");
                extensions.add("ramRemain");
                extensions.add("appMemory");
                extensions.add("accelerometer");
                extensions.add("gyroscope");
                extensions.add("magnetometer");
                extensions.add("isRoot");
                extensions.add("debugStatus");
                extensions.add("proxyStatus");
                extensions.add("emulatorStatus");
                extensions.add("isTablet");
                extensions.add("simState");
                extensions.add("ui_mode");
                extensions.add("sdCapacity");
                extensions.add("sdRemain");
                extensions.add("hasKeyboard");
                extensions.add("isMockLocation");
                extensions.add("ringMode");
                extensions.add("isAirMode");
                extensions.add("batteryStatus");
                extensions.add("chargeStatus");
                extensions.add("deviceName");
                extensions.add("appInstallTimeDiff");
                extensions.add("appUpdateTimeDiff");
                extensions.add("packageName");
                extensions.add("packageVersion");
                extensions.add("networkType");
                break;
            case PLATFORM_PC, PLATFORM_CLOUD_PC:
                extensions.add("cpuName");
                extensions.add("deviceModel");
                extensions.add("deviceName");
                extensions.add("deviceType");
                extensions.add("deviceUID");
                extensions.add("gpuID");
                extensions.add("gpuName");
                extensions.add("gpuAPI");
                extensions.add("gpuVendor");
                extensions.add("gpuVersion");
                extensions.add("gpuMemory");
                extensions.add("osVersion");
                extensions.add("cpuCores");
                extensions.add("cpuFrequency");
                extensions.add("gpuVendorID");
                extensions.add("isGpuMultiTread");
                extensions.add("memorySize");
                extensions.add("screenSize");
                extensions.add("engineName");
                extensions.add("addressMAC");
                extensions.add("packageVersion");
                break;
            case PLATFORM_WAP:
                extensions.add("userAgent");
                extensions.add("browserScreenSize");
                extensions.add("maxTouchPoints");
                extensions.add("isTouchSupported");
                extensions.add("browserLanguage");
                extensions.add("browserPlat");
                extensions.add("browserTimeZone");
                extensions.add("webGlRender");
                extensions.add("webGlVendor");
                extensions.add("numOfPlugins");
                extensions.add("listOfPlugins");
                extensions.add("screenRatio");
                extensions.add("deviceMemory");
                extensions.add("hardwareConcurrency");
                extensions.add("cpuClass");
                extensions.add("ifNotTrack");
                extensions.add("ifAdBlock");
                extensions.add("hasLiedLanguage");
                extensions.add("hasLiedResolution");
                extensions.add("hasLiedOs");
                extensions.add("hasLiedBrowser");
                extensions.add("canvas");
                extensions.add("webDriver");
                extensions.add("colorDepth");
                extensions.add("pixelRatio");
                extensions.add("packageName");
                extensions.add("packageVersion");
                extensions.add("webgl");
                break;
            case PLATFORM_MACOS, PLATFORM_CLOUD_MACOS:
                extensions.add("IDFV");
                extensions.add("model");
                extensions.add("osVersion");
                extensions.add("deviceName");
                extensions.add("screenSize");
                extensions.add("vendor");
                extensions.add("cpuType");
                extensions.add("cpuSpeed");
                extensions.add("cpuCores");
                extensions.add("proxyStatus");
                extensions.add("batteryStatus");
                extensions.add("chargeStatus");
                extensions.add("romCapacity");
                extensions.add("romRemain");
                extensions.add("ramCapacity");
                extensions.add("ramRemain");
                extensions.add("appMemory");
                break;
        }
        return extensions;
    }

    /**
     * Returns the platform name by given id. WORKS ONLY IN GENSHIN IMPACT.
     * @param id The id
     * @return The platform name. Like (PC, Android)
     */
    public static String getPlatformName(Integer id) {
        return switch (id) {
            case 1 -> "IOS";
            case 2 -> "Android";
            case 3 -> "PC";
            case 4, 5 -> "Browser";
            case 6 -> "PS";
            case 8 -> "CloudAndroid";
            case 9 -> "CloudPC";
            case 10 -> "CloudIOS";
            case 11 -> "PS5";
            case 12 -> "MacOS";
            case 13 -> "CloudMacOS";
            default -> "";
        };
    }

    /**
     * Returns all official android/IOS packages.
     * @param platformID The platform ID (Must me Android or IOS)
     * @return All packages.
     */
    public static JsonArray getPlatformPackageList(Integer platformID) {
        JsonArray packages = new JsonArray();
        PlatformType x = PlatformType.fromValue(platformID);
        switch(x) {
            case PLATFORM_ANDROID, PLATFORM_IOS:
                packages.add("com.mihoyo.accountsdkdemo");
                packages.add("com.miHoYo.GenshinImpact");
                packages.add("com.miHoYo.Yuanshen");
                packages.add("com.miHoYo.enterprise.HK4E");
                packages.add("com.miHoYo.enterprise.HK4E2");
                packages.add("com.miHoYo.genshinimpactcb");
                packages.add("com.miHoYo.yuanshencb");
                packages.add("com.miHoYo.bh3global");
                packages.add("com.miHoYo.bh3globalBeta");
                packages.add("com.miHoYo.bh3korea");
                packages.add("com.miHoYo.bh3korea.samsung");
                packages.add("com.miHoYo.bh3korea_beta");
                packages.add("com.miHoYo.bh3oversea");
                packages.add("com.miHoYo.bh3oversea.huawei");
                packages.add("com.miHoYo.bh3overseaBeta");
                packages.add("com.miHoYo.bh3rdJP");
                packages.add("com.miHoYo.bh3tw");
                packages.add("com.miHoYo.bh3twbeta");
                packages.add("com.miHoYo.bh3twmycard");
                packages.add("com.miHoYo.enterprise.NGHSoD");
                packages.add("com.miHoYo.enterprise.NGHSoDBak");
                packages.add("com.miHoYo.enterprise.NGHSoDBeta");
                packages.add("com.miHoYo.enterprise.NGHSoDQD");
                packages.add("com.miHoYo.HSoDv2.mix");
                packages.add("com.miHoYo.HSoDv2Beta");
                packages.add("com.miHoYo.HSoDv2Original");
                packages.add("com.miHoYo.HSoDv2OriginalENT");
                packages.add("com.miHoYo.tot.cht");
                packages.add("com.miHoYo.tot.glb");
                packages.add("com.miHoYo.wd");
                packages.add("com.miHoYo.cloudgames.ys");
                packages.add("com.miHoYo.cloudgames.ys.dev");
                packages.add("com.mihoyo.cloudgame");
                packages.add("com.mihoyo.cloudgamedev");
                packages.add("com.HoYoverse.enterprise.hkrpgoversea");
                packages.add("com.HoYoverse.hkrpgoversea");
                packages.add("com.HoYoverse.hkrpgoverseacbtest");
                packages.add("com.miHoYo.enterprise.hkrpg");
                packages.add("com.miHoYo.hkrpg");
                packages.add("com.miHoYo.hkrpgcb");
                packages.add("com.miHoYo.hkrpgoverseacb");
                break;
        }
        return packages;
    }

    /**
     * Returns the secret key of given platform.
     * @param platformID The platform ID
     * @return The secret key. (For android and IOS is not the same)
     */
    public static String getPlatformSecretKey(Integer platformID) {
        PlatformType x = PlatformType.fromValue(platformID);
        return switch(x) {
            case PLATFORM_IOS -> "qoJidkwJPsIh1T3oR4P8Y7L8ZjPJ0mVUaaGuf3LKw3TQf7Q6hdS2kJYCorMJYWK/66EcWTgLAo05SOm1QGMBnX5ORssQugEckSXmAcNJu4UDP6XhDgNZTTi3N7qURRnbf5EriWBLZr6HN/U/adSw9pxKHPU7wk6hifjhSZSTaGGOwVXOR+vcOUJyokGPFeRJXo9uC9vbPKoAG6SgPVu8P4/jrU8Jt+WtmZJqf0wtL4drxHcuMVmEhwJfp4wADxSJnnj9NZpp+GHhnHkZ+yZteozOu5qvGgn75iFI2QTMMkK6pxdaUKW+XvIau3x41uRwD6RDUdXcM0BBPKSyl5R8caU2zbSZNd72lGfAoYapfxtSxpl+h/4MXQEH/YbX8wpAlYWD/TaM1TqPe+4g9MbLYUY3y9s+QuGItTVBG+nvoDW26n4zRoLleFjThEiEHNWUdhkv1zmrT5AiS9z44zijINlKv2G9NjFMNF2ShpbtjcUVIWR+SNKDwUsVxJD1JqiGXqjUYkUpG5COZZcO3SIIcnbIoV+pCdCdaBBW+fbM4pdJVKWsk51/DdlcZ2ZOziPfykJDGCymmiqv9RiPZQJAeehs8zTe0UaBZSujOu0n6q/CHJvkJOwFIytqv/hTjV4POhbfOMh8tVRbyB8PyqGZXrY1GI6247yYJGB0k2blrZzPDJ49DrLk8XmVpQGv3oB2pHJ8z+dVLgR2PV0eblNpfC2QTqr88I6lBZA5VW5C2UaasfNkPzI5y7GpSmPo9t4lkYo8s0smq0xdb4V9caEjbk4kJUHb57h/Rz8fZbbyQIzUWb8jtCm+8n1VNOjFcTHECigouRP1PV8PkP1AUnbiFN+aVZWTTV8GrPGpZi8w5Vq+YaZ6ZBUZB9T415MCCEhqwhYdB7qZn/Q+/68mH56WdqiriwGd/dC5JSm4oplF+dbY8LC11YRrweLzM5CjgGPdHq+DKt6NvXphrzyRs5GrP0v2XBWphIdrgiYWGSRmgt/94GO5sYLYnbHHKO+X8gCrygY0QM4ANUoG1QbjGPfMReq25NHYobTy3Qulns4r2FtETqMSGuW0vhGwphA7LzSphFe90Uv6yxj8iFH2wK2vEF7dnhUQ7tJ72FomGGoVMcu+nY8Mt/dqUk5hWlhjzbLZAa5K62W5e70mp5bYNLuw/7yuMs1z0GKpHuIggWr0tDs0h5g8AnjCJ3FJQiFGbq3rXEwOpUa5QJbOuFw6A+Pc+h+WTHXc1JgYoPKoJSTlJ5SDufefiCXZbuHZnmCGlSWgudbdnVJAbudSslNPphfxD5yrooTQrEXbiKrI5gjEZaWzNmtZhY0wk9qW1/WqL9aulbmK39qcnKqyZN566/VUT4cekIZu9E1I/o4L+v+kWYRaqnCpykZayZmHQpkbb5rG6ImaAjco3snvG8Y4GqaD5w==";
            case PLATFORM_ANDROID -> "d4AG84f318di3+fObV0D+KPCMB66rrWKT6vzvkJOq7pJBXM9MKHweQLLSyb/uSCxQIuCG8f2c7PijtGcEQS4D3HKAPblWkSV05520QiVeh104UJsNvZiKlSgtXCg21meQqBxIlpBSEWUjxJ2eOfqwL1kD+7EZwNOmmYjQoiRq/i4hDyMqGIh6KJK3P3z/1+H5NJQERkT28UU7jeoRF/JwMNtoplRir2rXXKzRpwcGIq1c+pHjjuRGBmjPuBvrYjzXk5s3JxzLWse9DUf8tSh7N3Iut9lkJlHmWVT56IMh71oskcSMqWk993rXccyEpmkClkLHZrIw07yYACkqWj1aTuTF7jKGXhNEmwxe/VzEXaDrb+MC4JfaagCqCtJUQ6E8uCIJdpDVlT0nM9S6tt9myB4QIKmCd0uq+GfcU979zF6O65UTxycn0FmL4mXlB+0noDoMyyi+WTKSIjYdUOYw1v2BZSB14wcFCd8MK0X9CuL0Ir7IAbs+L7Ixgo7ziKOXNc6eXxUoMFROdDwqpMeTN/IY8sYWtvHqdit1H7wgWnNufYgH3o6VoQOFkAGBnNhSI7BvIcU3JIi4tKtz6t30rsqWzsaDpvHI7F6h5m148N22h1w3Sro58idPKwDIUpTUFAQvU0dLCkx+cZgEgw4A85lqxtEWsB+Sw2GHOC/olTP0k7FXg6+o5QQQR4X7tVbDJHzdWBKvq0i4CsXIG1bi1tQ5eygwo61K1l4cSSn+EaHfRQ35YLWRBt+en1z6Cjhgdk5cJBkuLlvMkI0fyAqjC/Mp0yEVb0hi5Epx5Gc46t0bXZPkyK5+XHy/vxoDKq7tS/VFbX/hlTXve2/MVtkvvK9EWffmRon1zVYKGXNCjrCbZlsKeHdYBGVwcj5m+pkFeogPXqGqdMeLOdG35h7C/zVjM+xp3ECo2eG3diJ+EfUWzmh58MgCBEFU2KKTWQTTF7fhummgJ9bocnG6MbETEoOV+CQr4opSjyhC+EgWGM5Tp5FO/6Qw5uia46I6qxrDRG29ylNMJW/2jIHcbM+cbnoDqZJHqjOit1XR2+vO952nDn9SNkAJwhDElRjhKJDuHYNLg4NTwswChJAUh3LRT4J7BHH8s/hrzZzys3463kSxV7EkUyN3M4ecUOWZ92rQHKQavKJGoKXT28gXy4QdcslCb05Iwf0WOyIrDumIZMhQuBKtsuf5WQTUy1z3PPnyUhCc2RuhDDsyZgmUoENAz82JMmZkvM6Zv31n6duwZlcdL1wzj2Sea33AKszd5wCqoGgje8lAVgILdaTFRL0qaO/vlYm5vGQYYF7KT5RDYWRlbQcQg821eTO4qbqK016ceeZBQQr5Aa8wzKjTpdfLJLILPQzyKuTo1UrXMAb/t7ghuxto9ePLIyQOf7yGJ2IWcUF63YlbCb4lbsxpjJqPEx66ZFhz9wjcK/oQuf1y6Kf3+Y/3OdaT00GBeQ9gl98";
            default -> "/vK5WTh5SS3SAj8Zm0qPWg==";
        };
    }

    /**
     * Checks if the specific language is supported.
     * @param language_id The language 2x letters (Like BR)
     * @return If the game is translated in your language.
     */
    public static boolean isGameTranslated(String language_id) {
        return switch (language_id.toLowerCase()) {
            case "vi", "zn-cn", "zn-tw", "en", "fr", "de", "es", "pt", "ru", "ja", "ko", "th", "id" -> true;
            default -> false;
        };
    }

    /**
     * Checks if the specific game biz key is valid.
     * @param game_biz Game biz key
     * @return If the game biz is valid for this game.
     */
    public static boolean isCorrectGameBiz(String game_biz) {
        return Utils.getGameBiz().equals(game_biz);
    }

    /**
     * Checks if the specific game biz key is valid for given game.
     * @param game_biz Game biz key
     * @param app_id Game application id.
     * @return If the game biz is valid for this game.
     */
    public static boolean isCorrectGameBiz(String game_biz, int app_id) {
        return Utils.getGameBiz(app_id).equals(game_biz);
    }

    /**
     * Checks if game version is correct.
     */
    public static boolean isGameVersionCorrect(String gameVersion) {
        if(gameVersion.length() < 10) return false;
        if(!gameVersion.startsWith("OS") && !gameVersion.startsWith("CN")) return false; ///  || !gameVersion.contains("CB")
        return gameVersion.contains("Win") || gameVersion.contains("iOS") || gameVersion.contains("Android") || gameVersion.contains("PS");
    }
}
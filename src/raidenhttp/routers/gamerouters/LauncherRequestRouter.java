package raidenhttp.routers.gamerouters;

// Modules
import raidenhttp.config.ConfigManager;
import raidenhttp.routers.Router;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

// Structures
import raidenhttp.utils.structures.StructureGetABTestLauncherRequest;

// Translation
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.classes.Json;

public class LauncherRequestRouter implements Router {

    /**
     * launcher -> launcher id. (For now plat-launcher)
     */
    private static void platLauncherRequest(Context ctx) {
        JsonObject response = new JsonObject();
        String launcher = ctx.queryParam("launcher");
        if(launcher == null || !launcher.equals("plat-launcher")) {
            response.addProperty("retcode", Retcodes.NO_LAUNCHER_CONFIG_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.invalidconfigkey"));
            response.add("data", null);
        }
        else {
            JsonObject data = new JsonObject();
            JsonObject vals = new JsonObject();
            vals.addProperty("apm_switch", "true");
            vals.addProperty("telemetry_switch", "true");

            data.add("vals", vals);

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * language -> language<br>
     * launcher_id -> launcher id<br>
     * type -> notification type<br>
     */
    private static void getNotificationRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/notifications.json");
                data.add("notifications", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("notifications", null);
            }
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * <br>client_type -> Platform name (like PC, IOS)
     * <br>plat_app -> Platform launcher app name.
     */
    private static void getParamsConfigRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String client = ctx.queryParam("client_type");
        String platApp = ctx.queryParam("plat_app");
        if(client == null || platApp == null) {
            response.addProperty("retcode", Retcodes.LAUNCHER_UNKNOWN_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error2"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/params_config.json");
                data.add("params_config", Json.parseObject(fileReader).getAsJsonObject());
            }catch(IOException e) {
                data.add("params_config", null);
            }

            if(data.get("params_config") != null && (client.equals("1") || client.equals("2"))) {
                data.get("params_config").getAsJsonObject().addProperty("multi_thread_io_read_enabled", true);
                data.get("params_config").getAsJsonObject().addProperty("multi_thread_io_write_enabled", true);
                data.get("params_config").getAsJsonObject().addProperty("buffer_size", 4);
            }

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void getABTestRequest(Context ctx) {
        JsonObject response = new JsonObject();
        var body = Json.decode(ctx.body(), StructureGetABTestLauncherRequest.class);
        if(body == null || !Json.isFullStructure(body) || Objects.equals(body.plat_app, "")) {
            response.add("data", null);
            response.addProperty("retcode", Retcodes.LAUNCHER_UNKNOWN_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error2"));
        }
        else {
            JsonObject data = new JsonObject();
            data.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            data.addProperty("success", true);
            data.addProperty("message", "");
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher_sophon_experiments.json");
                data.add("data", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("data", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * launcher_id -> Launcher ID
     */
    private static void getFEPackageRequest(Context ctx) {
        JsonObject response = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/FEPackage.json");
                response.add("data", Json.parseObject(fileReader).getAsJsonObject());
            }catch(IOException e) {
                response.add("data", null);
            }
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * type -> Launcher ID
     */
    private static void getAgreementVersionRequest(Context ctx) {
        JsonObject response = new JsonObject();
        String type = ctx.queryParam("type");
        if(type == null) {
            response.addProperty("retcode", Retcodes.LAUNCHER_AGREEMENT_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.agreement_not_found"));
            response.add("data", null);
        }
        else {
            JsonObject data = new JsonObject();
            data.addProperty("version", "2.0");

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * launcher_id -> Launcher ID.
     */
    private static void getAllGameBasicInfoRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/game_list.json");
                data.add("game_info_list", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("game_info_list", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * channel -> channel id<br>
     * game_ids -> launcher game ids<br>
     * launcher_id -> launcher id<br>
     * sub_channel -> sub channel
     */
    private static void getGameChannelSDKsRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/game_sdks_config.json");
                data.add("game_channel_sdks", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("game_channel_sdks", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * channel -> channel id<br>
     * game_ids -> launcher game ids<br>
     * launcher_id -> launcher id<br>
     * sub_channel -> sub channel
     */
    private static void getGameDeprecatedFileConfigsRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/games_deprecated_config.json");
                data.add("deprecated_file_configs", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("deprecated_file_configs", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * game_ids -> launcher game ids<br>
     * launcher_id -> launcher id
     */
    private static void getGameConfigsRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/game_launch_config.json");
                data.add("launch_configs", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("launch_configs", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * game_ids -> launcher game ids<br>
     * launcher_id -> launcher id
     */
    private static void getGameBranchesConfigRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/game_branches_config.json");
                data.add("game_branches", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("game_branches", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * game_ids -> launcher game ids<br>
     * launcher_id -> launcher id
     */
    private static void getGamePackagesRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/game_packages_config.json");
                data.add("game_packages", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("game_packages", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * game_ids -> launcher game ids<br>
     * launcher_id -> launcher id
     */
    private static void getGamePluginsRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/game_plugins_config.json");
                data.add("plugin_releases", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("plugin_releases", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * branch -> branch name<br>
     * package_id -> package id<br>
     * password -> password<br>
     * plat_app -> platform application id<br>
     */
    private static void getSophonChunkInfoRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        data.addProperty("build_id", "VvHG3LEx2pIg");
        data.addProperty("tag", ConfigManager.httpConfig.gameInfo.gameVersion); /// 4.8.0
        try {
            FileReader fileReader = new FileReader("./resources/config/launcher/sophon_manifests.json");
            data.add("manifests", Json.parseObject(fileReader).getAsJsonArray());
        }catch(IOException e) {
            data.add("manifests", null);
        }
        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * launcher_id -> launcher id<br>
     * game_id -> game id<br>
     * language -> language
     */
    private static void getGameContentRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/links_config.json");
                data.add("content", Json.parseObject(fileReader).getAsJsonObject());
            }catch(IOException e) {
                data.add("content", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * launcher_id -> launcher id<br>
     * language -> language
     */
    private static void getGamesRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String launcher = ctx.queryParam("launcher_id");
        if(!Objects.equals(launcher, ConfigManager.httpConfig.const_launcher_id)) {
            response.addProperty("retcode", Retcodes.LAUNCHER_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.launcher_not_found"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/launcher/games_config.json");
                data.add("games", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("games", null);
            }
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * app_id - application id<br>
     * app_version - application version<br>
     * key - key<br>
     * channel - channel and sub channel seperated by underscore. Example (1_3)<br>
     */
    private static void getLatestReleaseRequest(Context ctx) {
        JsonObject response = new JsonObject();
        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        try {
            FileReader fileReader = new FileReader("./resources/config/launcher/latest_game_release.json");
            response.add("data", Json.parseObject(fileReader).getAsJsonObject());
        }catch(IOException e) {
            response.add("data", null);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * key ->game keyid<br>
     * launcher_id -> game launcher id
     */
    private static void getLatestGameResource(Context ctx) {
        JsonObject response = new JsonObject();

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        try {
            FileReader fileReader = new FileReader("./resources/config/launcher/latest_game.json");
            response.add("data", Json.parseObject(fileReader).getAsJsonObject());
        }catch(IOException e) {
            response.add("data", null);
        }

        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://sdk-os-static.hoyoverse.com/combo/box/api/config/plat-launcher/plat-launcher
        javalin.get("combo/box/api/config/plat-launcher/plat-launcher", LauncherRequestRouter::platLauncherRequest);

        /// https://sg-public-api.hoyoverse.com/downloader/sophon/api/getABTest
        javalin.post("downloader/sophon/api/getABTest", LauncherRequestRouter::getABTestRequest);

        /// https://sg-public-api-static.hoyoverse.com/downloader/sophon/api/getParamsConfig
        javalin.get("downloader/sophon/api/getParamsConfig", LauncherRequestRouter::getParamsConfigRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getNotification
        javalin.get("hyp/hyp-connect/api/getNotification", LauncherRequestRouter::getNotificationRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameChannelSDKs
        javalin.get("hyp/hyp-connect/api/getGameChannelSDKs", LauncherRequestRouter::getGameChannelSDKsRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameDeprecatedFileConfigs
        javalin.get("hyp/hyp-connect/api/getGameDeprecatedFileConfigs", LauncherRequestRouter::getGameDeprecatedFileConfigsRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePlugins
        javalin.get("hyp/hyp-connect/api/getGamePlugins", LauncherRequestRouter::getGamePluginsRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePackages
        javalin.get("hyp/hyp-connect/api/getGamePackages", LauncherRequestRouter::getGamePackagesRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameConfigs
        javalin.get("hyp/hyp-connect/api/getGameConfigs", LauncherRequestRouter::getGameConfigsRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameBranches
        javalin.get("hyp/hyp-connect/api/getGameBranches", LauncherRequestRouter::getGameBranchesConfigRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getFEPackage
        javalin.get("hyp/hyp-connect/api/getFEPackage", LauncherRequestRouter::getFEPackageRequest);

		/// https://sg-public-api.hoyoverse.com/downloader/sophon_chunk/api/getBuild
        javalin.get("downloader/sophon_chunk/api/getBuild", LauncherRequestRouter::getSophonChunkInfoRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAgreementVersion
        javalin.get("hyp/hyp-connect/api/getAgreementVersion", LauncherRequestRouter::getAgreementVersionRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAllGameBasicInfo
        javalin.get("hyp/hyp-connect/api/getAllGameBasicInfo", LauncherRequestRouter::getAllGameBasicInfoRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameContent
        javalin.get("hyp/hyp-connect/api/getGameContent", LauncherRequestRouter::getGameContentRequest);

		/// https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGames
        javalin.get("hyp/hyp-connect/api/getGames", LauncherRequestRouter::getGamesRequest);

		/// https://sg-public-api.hoyoverse.com/ptolemaios_api/api/getLatestRelease
        javalin.options("ptolemaios_api/api/getLatestRelease", LauncherRequestRouter::getLatestReleaseRequest);

        /// https://sdk-os-static.hoyoverse.com/hk4e_global/mdk/launcher/api/resource
        javalin.get(game_biz + "/mdk/launcher/api/resource", LauncherRequestRouter::getLatestGameResource);
    }
}
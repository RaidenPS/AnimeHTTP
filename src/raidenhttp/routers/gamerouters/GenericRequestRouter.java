package raidenhttp.routers.gamerouters;

// Imports
import raidenhttp.config.ConfigManager;
import raidenhttp.database.DatabaseHelper;
import raidenhttp.routers.Router;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// Structures
import raidenhttp.utils.structures.StructureGenWaterMarkRequest;
import raidenhttp.utils.structures.StructureGetFPRequest;

// Translation
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.Utils;
import raidenhttp.utils.classes.Json;

public class GenericRequestRouter implements Router {
    /**
     * No Parameters
     */
    private static void sendGameMillis(Context ctx) {
        long milliTs = System.currentTimeMillis();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("code", Retcodes.RETCODE_SUCCESS);
        arguments.put("message", "app running");
        arguments.put("milliTs", String.valueOf(milliTs));
        ctx.result(Json.toJsonString(arguments)).contentType("application/json");
    }

    /**
     * Platform -> Client's platform name.
     */
    private static void getExtensionsList(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        try {
            String param = ctx.queryParam("platform");
            data.addProperty("code", 200);
            data.addProperty("msg", "ok");
            data.add("ext_list", Utils.getPlatformExtensions(Integer.valueOf(Objects.requireNonNull(param))));
            data.add("pkg_list", Utils.getPlatformPackageList(Integer.valueOf(Objects.requireNonNull(param))));
            data.addProperty("pkg_str", Utils.getPlatformSecretKey(Integer.valueOf(Objects.requireNonNull(param))));
        }catch (NullPointerException e) {
            data.addProperty("code", 403);
            data.addProperty("msg", translate("messages.http.parameter_error"));
            data.add("ext_list", new JsonArray());
            data.add("pkg_list", new JsonArray());
            data.addProperty("pkg_str", "");
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void getFP(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureGetFPRequest.class);
        JsonObject response = new JsonObject();

        if(bodyData == null) {
            response.addProperty("retcode", Retcodes.UNKNOWN_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.unknown_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
        }
        else {
            JsonObject data = new JsonObject();
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);

            if(!Json.isFullStructure(bodyData) || Objects.equals(bodyData.device_fp, "")) {
                data.addProperty("code", 403);
                data.addProperty("msg", translate("messages.http.parameter_error"));
            }
            else {
                data.addProperty("code", 200);
                data.addProperty("msg", "ok");
                data.addProperty("device_fp", bodyData.device_fp);
            }
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void getAreaCode(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        data.add("area_code", Utils.getAreaCode());

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * app_id - The game id.
     */
    private static void getFonts(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        try {
            String param = ctx.queryParam("app_id");
            data.add("fonts", Utils.getGameFonts(Integer.valueOf(Objects.requireNonNull(param))));

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        catch(NullPointerException e) {
            response.addProperty("retcode", Retcodes.APP_ID_NOT_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.appid_error"));
            response.add("data", null);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * device_id - Client's device identification.
     */
    private static void perfConfigVerify(Context ctx) {
        String device_id = ctx.queryParam("device_id");
        String platform = ctx.queryParam("platform");
        JsonObject response = new JsonObject();

        if(device_id == null || platform == null) {
            response.addProperty("code", Retcodes.RETCODE_FAILED);
            response.addProperty("message", translate("messages.http.parameter_error"));
        }
        else if(!(platform.equals("2") || platform.equals("7") || platform.equals("8") || platform.equals("11"))) {
            response.addProperty("code", Retcodes.RETCODE_FAILED);
            response.addProperty("message", translate("messages.http.platform_mismatch_error", platform));
        }
        else if(DatabaseHelper.getAccountByDeviceId(device_id) == null) {
            response.addProperty("code", Retcodes.RETCODE_FAILED);
            response.addProperty("message", "not matched");
        }
        else {
            response.addProperty("code", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No parameters
     */
    private static void getRedDotList(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject infos = new JsonObject();

        infos.addProperty("red_point_type", 2201);
        infos.addProperty("content_id", 184);
        infos.addProperty("display", ConfigManager.httpConfig.display_retpoint);

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("infos", infos);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * check_consent_banner -> Check consent banner.
     */
    private static void getDynamicClientConfig(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        data.addProperty("enable_consent_banner", ConfigManager.httpConfig.enable_consent_banner);
        data.addProperty("region_code", "");

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * biz_key -> Game biz key<br>
     * client_type -> Client platform id.
     */
    private static void drmSwitchRequest(Context ctx) {
        /// TODO: Investigation

        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        response.addProperty("retcode", Retcodes.INVALID_RETCODE_MODULE_ERROR_VALUE);
        response.addProperty("message", translate("messages.http.invalidmodule"));
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * client -> Client platform id.<br>
     * game_key -> game biz key.
     */
    private static void loadFirebaseBlacklist(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        String client = ctx.queryParam("client");
        if(client == null) {
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/firebase_blacklist.json");
                data.addProperty("device_blacklist", Json.parseObject(fileReader).getAsJsonObject().toString());
            }catch(IOException e) {
                data.add("device_blacklist", null);
            }

            data.addProperty("device_blacklist_version", ConfigManager.httpConfig.firebase_device_blacklist_version);
            data.addProperty("device_blacklist_switch", ConfigManager.httpConfig.firebase_blacklist_devices_switch);

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void getCurrentTimeNow(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        data.addProperty("now", String.valueOf(System.currentTimeMillis() / 1000));

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void getPrecacheResponse(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        JsonObject vals = new JsonObject();

        vals.addProperty("enable", true);

        data.add("vals", vals);

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void genWatermark(Context ctx) {
        /// TODO: Investigation

        var bodyData = Json.decode(ctx.body(), StructureGenWaterMarkRequest.class);
        JsonObject response = new JsonObject();

        if(bodyData == null || !Objects.equals(bodyData.app_id, "c7ahnfpzpuyo")) {
            response.addProperty("retcode", Retcodes.GAME_BIZ_MISSION_ERROR_VALUE);
            response.addProperty("message", "INVALID_TOKEN");
            response.add("data", null);
        }
        else {
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
        }

        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://apm-log-upload.mihoyo.com/_ts
        javalin.get("_ts", GenericRequestRouter::sendGameMillis);

        /// https://sg-public-data-api.hoyoverse.com/device-fp/api/getExtList
        javalin.get("device-fp/api/getExtList", GenericRequestRouter::getExtensionsList);

        /// https://sg-public-data-api.hoyoverse.com/device-fp/api/getFp
        javalin.post("device-fp/api/getFp", GenericRequestRouter::getFP);

        /// https://api-account-os.hoyoverse.com/account/auth/api/getAreaCode
        javalin.get("account/auth/api/getAreaCode", GenericRequestRouter::getAreaCode);

        /// https://log-upload-os.hoyoverse.com/perf/config/verify
        javalin.get("perf/config/verify", GenericRequestRouter::perfConfigVerify);

        /// https://hk4e-sdk-os-static.hoyoverse.com/combo/granter/api/getFont
        javalin.get(game_biz + "/combo/granter/api/getFont", GenericRequestRouter::getFonts);
        javalin.get("combo/granter/api/getFont", GenericRequestRouter::getFonts);

		/// https://hk4e-sdk-os-static.hoyoverse.com/combo/red_dot/list
        javalin.post(game_biz + "/combo/red_dot/list", GenericRequestRouter::getRedDotList);
        javalin.post("combo/granter/red_dot/list", GenericRequestRouter::getRedDotList);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_global/combo/granter/api/getDynamicClientConfig
        javalin.get(game_biz + "/combo/granter/api/getDynamicClientConfig", GenericRequestRouter::getDynamicClientConfig);

        /// https://sdk-os-static.hoyoverse.com/combo/box/api/config/sdk/drmSwitch
        javalin.get("/combo/box/api/config/sdk/drmSwitch", GenericRequestRouter::drmSwitchRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/loadFirebaseBlackList
        javalin.get(game_biz + "/mdk/shield/api/loadFirebaseBlackList", GenericRequestRouter::loadFirebaseBlacklist);

        /// https://api-payment.mihoyo.com/plutus/api/v2/timeNow
        javalin.get("plutus/api/v2/timeNow", GenericRequestRouter::getCurrentTimeNow);

        /// https://sdk-static.mihoyo.com/combo/box/api/config/sw/precache
        javalin.get("combo/box/api/config/sw/precache", GenericRequestRouter::getPrecacheResponse);

        /// https://sdk-common-api.hoyoverse.com/sdk_global/marker/api/genMark
        javalin.post("sdk_global/marker/api/genMark", GenericRequestRouter::genWatermark);
    }
}
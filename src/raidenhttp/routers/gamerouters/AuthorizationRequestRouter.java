package raidenhttp.routers.gamerouters;

// Imports
import raidenhttp.routers.Router;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

// Structures
import raidenhttp.utils.structures.StructureCompareProtocolVersionRequest;
import raidenhttp.utils.structures.Captcha.StructureLoginCaptchaRequest;

// Translation
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.Utils;
import raidenhttp.utils.classes.Json;

public class AuthorizationRequestRouter implements Router {
    /**
     * app_id -> Mihoyo's game id.
     * <br>channel_id -> channel id
     * <br>client_type -> platform type (like PC, IOS)
     */
    private static void getConfig(Context ctx) {
        JsonObject response = new JsonObject();
        String appId = ctx.queryParam("app_id");
        String channel_id = ctx.queryParam("channel_id");
        String clientType = ctx.queryParam("client_type");
        if(appId == null || channel_id == null || clientType == null || Utils.getGameNameGenshin(Integer.valueOf(appId)).isEmpty()) {
            response.addProperty("retcode", Retcodes.SYSTEM_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.system_error"));
            response.add("data", null);
        }
        else {

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            try {
                FileReader fileReader = new FileReader("./resources/config/getConfig_config.json");
                response.add("data", Json.parseObject(fileReader).getAsJsonObject());
            }catch(IOException e) {
                response.add("data", null);
            }

            if(response.get("data") != null) {
                response.get("data").getAsJsonObject().addProperty("protocol", channel_id.equals("1"));
                response.get("data").getAsJsonObject().addProperty("announce_url", Utils.getAnnouncementUrl());
                response.get("data").getAsJsonObject().addProperty("app_name", Utils.getGameNameGenshin(Integer.valueOf(appId)));
            }
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * game_key -> Game BIZ key
     * <br>client_id -> platform type (like PC, IOS)
     */
    private static void loadConfig(Context ctx) {
        JsonObject response = new JsonObject();
        String gameKey = ctx.queryParam("game_key");
        int client_id = Integer.parseInt(Objects.requireNonNull(ctx.queryParam("client")));
        if (!Utils.isCorrectGameBiz(gameKey) || client_id > 13 || client_id < 1) {
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
        }
        else {
            int x = client_id;
            switch(x){
                case 2:
                    x += 3;
                    break;
                case 6:
                    x = 26 - 3;
                    break;
                case 8:
                    x = 59 - 3;
                    break;
                case 9:
                    x = 60 - 3;
                    break;
                case 10:
                    x = 61 - 3;
                    break;
                case 11:
                    x = 27 - 3;
                    break;
                case 13:
                    x = 62 - 3;
                    break;
                default:
                    break;
            }

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            try {
                FileReader fileReader = new FileReader("./resources/config/loadConfig_config.json");
                response.add("data", Json.parseObject(fileReader).getAsJsonObject());
            }catch(IOException e) {
                response.add("data", null);
            }
            if(response.get("data") != null) {
                response.get("data").getAsJsonObject().addProperty("id", x + 3);
                response.get("data").getAsJsonObject().addProperty("game_key", gameKey);
                response.get("data").getAsJsonObject().addProperty("client", Utils.getPlatformName(client_id));
                response.get("data").getAsJsonObject().addProperty("name", gameKey.equals("hk4e_cn") ? Utils.getGameNameGenshin(10)  : Utils.getGameNameGenshin(4));
            }
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * biz_key -> Game BIZ key
     * <br>client_type -> platform type (like PC, IOS)
     */
    private static void comboRequest(Context ctx) {
        JsonObject response = new JsonObject();
        String gameKey = ctx.queryParam("biz_key");
        String clientType = ctx.queryParam("client_type");
        if(clientType == null || !Utils.isCorrectGameBiz(gameKey)) {
            response.addProperty("retcode", Retcodes.NO_CONFIG_FOUND_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.noconfig"));
            response.add("data", null);
        }
        else {
            try {
                FileReader fileReader = new FileReader("./resources/config/combo_config.json");
                response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
                response.addProperty("message", "OK");
                response.add("data", Json.parseObject(fileReader).getAsJsonObject().get(clientType).getAsJsonObject());
            }catch(Exception ignored) {
                response.addProperty("retcode", Retcodes.NO_CONFIG_FOUND_ERROR_VALUE);
                response.addProperty("message", translate("messages.http.noconfig"));
                response.add("data", null);
            }
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void compareProtocolVersionRequest(Context ctx) {
        JsonObject response = new JsonObject();
        var body = Json.decode(ctx.body(), StructureCompareProtocolVersionRequest.class);
        if(body == null || !Json.isFullStructure(body)) {
            response.add("data", null);
            response.addProperty("retcode", Retcodes.PROTOCOL_FAILED_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.protocol_load_failed"));
        }
        else {
            JsonObject protocol = new JsonObject();
            JsonObject data = new JsonObject();

            if(Objects.equals(body.language, "en") && Objects.equals(body.major, "13") && Objects.equals(body.minimum, "0")) {
                data.addProperty("modified", false);
                data.add("protocol", null);
            }
            else {
                protocol.addProperty("id", 0);
                protocol.addProperty("app_id", body.app_id);
                protocol.addProperty("language", body.language);
                protocol.addProperty("user_proto", "");
                protocol.addProperty("priv_proto", "");
                int major2 = switch (body.language) {
                    case "tr" -> 3;
                    case "es", "ja" -> 11;
                    case "fr", "zh-cn" -> 12;
                    case "en", "vi", "de" -> 13;
                    case "zh-tw" -> 17;
                    default -> 0;
                };
                protocol.addProperty("major", major2);
                protocol.addProperty("minimum", 0);
                protocol.addProperty("create_time", "0");
                protocol.addProperty("teenager_proto", "");
                protocol.addProperty("third_proto", "");
                protocol.addProperty("full_priv_proto", "");

                data.addProperty("modified", true);
                data.add("protocol", protocol);
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
    private static void getAgreementInfos(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        data.add("marketing_agreements", new JsonArray());

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * UNKNOWN
     */
    private static void getListNewerDevices(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        data.add("devices", new JsonArray());
        data.addProperty("latest_uid", "0");

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
    }

    /**
     *
     * mobile -> Mobile phone<br>
     * area -> Mobile phone country code.
     */
    private static void loginPhoneCaptcha(Context ctx) {
        JsonObject response = new JsonObject();
        var bodyData = Json.decode(ctx.body(), StructureLoginCaptchaRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            response.addProperty("retcode", Retcodes.PHONE_NUMBER_NOT_PROVIDED_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.invalid_phone_number_found"));
            response.add("data", null);
        }
        else {
            JsonObject data = new JsonObject();

            data.addProperty("protocol", true);
            data.addProperty("qr_enabled", false);
            data.addProperty("log_level", "DEBUG");

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://hk4e-sdk-os-static.hoyoverse.com/combo/granter/api/getConfig
        javalin.get(game_biz + "/combo/granter/api/getConfig", AuthorizationRequestRouter::getConfig);
        javalin.get("combo/granter/api/getConfig", AuthorizationRequestRouter::getConfig);

        /// https://hk4e-sk-os-static.hoyoverse.com/mdk/shield/api/loadConfig
        javalin.get(game_biz + "/mdk/shield/api/loadConfig", AuthorizationRequestRouter::loadConfig);
        javalin.get("mdk/shield/api/loadConfig", AuthorizationRequestRouter::loadConfig);

        /// https://sdk-os-static.hoyoverse.com/combo/box/api/config/sdk/combo
        javalin.get("combo/box/api/config/sdk/combo", AuthorizationRequestRouter::comboRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/combo/granter/api/compareProtocolVersion
        /// https://hk4e-sdk-os.hoyoverse.com/combo/granter/api/getProtocol
        javalin.post(game_biz + "/combo/granter/api/compareProtocolVersion", AuthorizationRequestRouter::compareProtocolVersionRequest);
        javalin.post(game_biz + "/combo/granter/api/getProtocol", AuthorizationRequestRouter::compareProtocolVersionRequest);
        javalin.post("combo/granter/api/compareProtocolVersion", AuthorizationRequestRouter::compareProtocolVersionRequest);
        javalin.post("combo/granter/api/getProtocol", AuthorizationRequestRouter::compareProtocolVersionRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_cn/mdk/shield/api/loginCaptcha
        javalin.post(game_biz + "/mdk/shield/api/loginCaptcha", AuthorizationRequestRouter::loginPhoneCaptcha);
        javalin.post("/mdk/shield/api/loginCaptcha", AuthorizationRequestRouter::loginPhoneCaptcha);

		/// https://api-account-os.hoyoverse.com/account/device/api/listNewerDevices
        javalin.post("account/device/api/listNewerDevices", AuthorizationRequestRouter::getListNewerDevices);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/agreement/api/getAgreementInfos
        /// https://hk4e-sdk-os.hoyoverse.com/mdk/agreement/api/getAgreementInfos
        javalin.get(game_biz + "/mdk/agreement/api/getAgreementInfos", AuthorizationRequestRouter::getAgreementInfos);
        javalin.get("mdk/agreement/api/getAgreementInfos", AuthorizationRequestRouter::getAgreementInfos);
    }
}
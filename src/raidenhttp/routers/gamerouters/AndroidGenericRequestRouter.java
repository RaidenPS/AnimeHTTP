package raidenhttp.routers.gamerouters;

// Imports
import raidenhttp.config.ConfigManager;
import raidenhttp.routers.Router;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

// Translate
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.classes.Json;

public class AndroidGenericRequestRouter implements Router {

    /**
     * app_id -> Android application id.<br>
     * client_type -> Client platform id.
     */
    private static void getAttributionReportConfig(Context ctx) {
        String client_type = ctx.queryParam("client_type");
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        if(Objects.equals(client_type, "2")) {
            data.addProperty("device_blacklist", "");
            data.addProperty("enabled", ConfigManager.httpConfig.android_enable_device_report);
            try {
                FileReader fileReader = new FileReader("./resources/config/android_report_config.json");
                data.addProperty("report_detail", Json.parseObject(fileReader).getAsJsonObject().toString());
            }catch(IOException e) {
                data.add("report_detail", null);
            }
            data.addProperty("report_interval_seconds", ConfigManager.httpConfig.android_report_interval_seconds);
        }
        else {
            data.addProperty("device_blacklist", "");
            data.addProperty("enabled", false);
            data.addProperty("report_detail", "");
            data.addProperty("report_interval_seconds", 0);
        }

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * app_id -> game application id<br>
     * channel -> game channel id<br>
     * language -> game language<br>
     * major -> game major<br>
     * minimum -> game minimum
     */
    private static void compareProtocolVersionRequest(Context ctx) {
        String app_id = ctx.queryParam("app_id");
        String channel = ctx.queryParam("channel_id");
        String language = ctx.queryParam("language");
        String major = ctx.queryParam("major");
        String minor = ctx.queryParam("minimum");

        JsonObject response = new JsonObject();
        if(app_id == null || channel == null || language == null) {
            response.add("data", null);
            response.addProperty("retcode", Retcodes.PROTOCOL_FAILED_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.protocol_load_failed"));
        }
        else {
            JsonObject protocol = new JsonObject();
            JsonObject data = new JsonObject();

            if(Objects.equals(language, "en") && Objects.equals(major, "13") && Objects.equals(minor, "0")) {
                data.addProperty("modified", false);
                data.add("protocol", null);
            }
            else {
                protocol.addProperty("id", 0);
                protocol.addProperty("app_id", app_id);
                protocol.addProperty("language", language);
                protocol.addProperty("user_proto", "");
                protocol.addProperty("priv_proto", "");
                int major2 = switch (language) {
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

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://sdk-common-static.hoyoverse.com/sdk_global/apphub/api/getAttributionReportConfig
        javalin.get("sdk_global/apphub/api/getAttributionReportConfig", AndroidGenericRequestRouter::getAttributionReportConfig);

        /// https://hk4e-sdk-os.hoyoverse.com/combo/granter/api/compareProtocolVersion
        javalin.get(game_biz + "/combo/granter/api/compareProtocolVersion", AndroidGenericRequestRouter::compareProtocolVersionRequest);
        javalin.get(game_biz + "/combo/granter/api/getProtocol", AndroidGenericRequestRouter::compareProtocolVersionRequest);
        javalin.get("combo/granter/api/compareProtocolVersion", AndroidGenericRequestRouter::compareProtocolVersionRequest);
        javalin.get("combo/granter/api/getProtocol", AndroidGenericRequestRouter::compareProtocolVersionRequest);
    }
}
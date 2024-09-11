package raidenhttp.routers.gamerouters;

// Modules
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import raidenhttp.cryptography.RC4;
import raidenhttp.database.DatabaseHelper;
import raidenhttp.routers.Router;
import com.google.gson.JsonObject;
import io.javalin.http.Context;
import io.javalin.Javalin;

// Structures
import raidenhttp.utils.structures.StructureBatchLogRequest;

// Utils
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.classes.Json;
import static raidenhttp.utils.Language.translate;

public class GameLoggingRequestRouter implements Router {
    /**
     * No Parameters
     */
    private static void uploadCrashLogDatabase(Context ctx) {
        JsonObject response = new JsonObject();
        try {
            response.addProperty("retcode", 0);
            response.addProperty("error", "");
            response.add("data", null);
            JsonArray obj = Json.parseObject(ctx.body()).getAsJsonArray();
            DatabaseHelper.gameLog(obj.get(0).toString(), "crashlogs");
            ctx.result(Json.toJsonString(response)).contentType("application/json");
        }
        catch (JsonSyntaxException e) {
            response.addProperty("retcode", -1);
            response.addProperty("error", "json unmarshal failed");
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
        }
    }

    /**
     * No Parameters
     */
    private static void uploadSDKLog(Context ctx) {
        JsonObject response = new JsonObject();
        JsonArray jsonArray = Json.parseObject(ctx.body()).getAsJsonArray();
        DatabaseHelper.gameLog(jsonArray.get(0).toString(), "sdklogs");
        response.addProperty("code", 0);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void uploadAndroidSDKLog(Context ctx) {
        JsonObject response = new JsonObject();
        JsonArray jsonArray = Json.parseObject(ctx.body()).getAsJsonArray();
        DatabaseHelper.gameLog(jsonArray.get(0).toString(), "androidsdklogs");
        response.addProperty("code", 0);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void uploadAPMLog(Context ctx) {
        JsonObject response = new JsonObject();
        JsonArray jsonArray = Json.parseObject(ctx.body()).getAsJsonArray();
        DatabaseHelper.gameLog(jsonArray.get(0).toString(), "apmlogs");
        response.addProperty("code", 0);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void uploadSophonLog(Context ctx) {
        JsonObject response = new JsonObject();
        JsonArray jsonArray = Json.parseObject(ctx.body()).getAsJsonArray();
        DatabaseHelper.gameLog(jsonArray.get(0).toString(), "sophonlogs");
        response.addProperty("code", 0);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void uploadPerfLog(Context ctx) {
        JsonObject response = new JsonObject();
        JsonArray jsonArray = Json.parseObject(ctx.body()).getAsJsonArray();
        DatabaseHelper.gameLog(jsonArray.get(0).toString(), "perflogs");
        response.addProperty("code", 0);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void uploadEventLog(Context ctx) {
        JsonObject response = new JsonObject();
        JsonArray jsonArray = Json.parseObject(ctx.body()).getAsJsonArray();
        DatabaseHelper.gameLog(jsonArray.get(0).toString(), "eventlogs");
        response.addProperty("code", 0);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void getBatchLog(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureBatchLogRequest.class);
        if (bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.SYSTEM_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.system_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }

        String rc4_key = "F#ju0q8I9HbmH8PMpJzzBee&p0b5h@Yb";
        DatabaseHelper.gameLog(RC4.rc4(rc4_key, bodyData.data, true), "h5log");

        JsonObject response = new JsonObject();
        response.addProperty("retcode", 0);
        response.addProperty("error", "");
        response.add("data", null);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void uploadApplicationCrashLog(Context ctx) {
        JsonObject response = new JsonObject();
        JsonArray jsonArray = Json.parseObject(ctx.body()).getAsJsonArray();
        DatabaseHelper.gameLog(jsonArray.get(0).toString(), "crashdumps");
        response.addProperty("code", 0);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void uploadYsCustomConfig(Context ctx) {
        JsonObject response = new JsonObject();
        JsonArray jsonArray = Json.parseObject(ctx.body()).getAsJsonArray();
        DatabaseHelper.gameLog(jsonArray.get(0).toString(), "ymlogs");
        response.addProperty("code", 0);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://ys-log-upload.mihoyo.com/crash/dataUpload
        javalin.post("crash/dataUpload", GameLoggingRequestRouter::uploadCrashLogDatabase);

        /// https://ys-log-upload.mihoyo.com/sdk/dataUpload
        javalin.post("sdk/dataUpload", GameLoggingRequestRouter::uploadSDKLog);
        javalin.post("client/event/dataUpload", GameLoggingRequestRouter::uploadSDKLog);

        /// https://ad-log-upload-os.hoyoverse.com/adsdk/dataUpload
        javalin.post("adsdk/dataUpload", GameLoggingRequestRouter::uploadAndroidSDKLog);
		
		/// https://apm-log-upload.mihoyo.com/apm/dataUpload
        javalin.post("apm/dataUpload", GameLoggingRequestRouter::uploadAPMLog);
		
        /// https://log-upload.mihoyo.com/sophon/dataUpload
        javalin.post("sophon/dataUpload", GameLoggingRequestRouter::uploadSophonLog);
		
        /// https://log-upload.mihoyo.com/perf/dataUpload
        javalin.post("perf/dataUpload", GameLoggingRequestRouter::uploadPerfLog);
		
		/// https://log-upload.mihoyo.com/event/dataUpload
        javalin.post("event/dataUpload", GameLoggingRequestRouter::uploadEventLog);
		
		/// https://log-upload.mihoyo.com/crashdump/dataUpload
        javalin.post("crashdump/dataUpload", GameLoggingRequestRouter::uploadApplicationCrashLog);

        /// https://minor-api.mihoyo.com/common/h5log/log/batch?topic=plat_apm_sdk
        javalin.post("common/h5log/log/batch", GameLoggingRequestRouter::getBatchLog);

        /// https://log-upload.mihoyo.com/ys_custom/dataUpload
        javalin.post("ys_custom/dataUpload", GameLoggingRequestRouter::uploadYsCustomConfig);
    }
}

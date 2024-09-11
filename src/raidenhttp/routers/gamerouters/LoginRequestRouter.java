package raidenhttp.routers.gamerouters;

// Imports
import raidenhttp.Main;
import raidenhttp.authorization.AuthenticationSystem;
import raidenhttp.config.ConfigManager;
import raidenhttp.routers.Router;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.FileReader;
import java.io.IOException;

// Structures
import raidenhttp.utils.structures.*;

// Translation
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.classes.Json;

public class LoginRequestRouter implements Router {
    /**
     * account -> Account name<br>
     * password -> Account password<br>
     * is_crypto -> Is password encrypted with AES?<br>
     */
    private static void loginAccountRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureLoginAccountRequest.class);
        if (bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }

        var response = Main.getAuthenticationSystem().getPasswordAuthenticator().authenticate(AuthenticationSystem.fromPasswordRequest(ctx, bodyData));
        ctx.json(response).contentType("application/json");
    }

    /**
     * app_id -> mihoyo's game id. (4 for genshin impact).<br>
     * channel_id -> always 1<br>
     * uid -> Account's id<br>
     * guest -> Is the account guest<br>
     * token -> Session token<br>
     * device -> Account's device id<br>
     * sign -> Signature<br>
     */
    private static void sessionKeyLoginRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureComboTokenRequest.class);
        if (bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.signature_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }

        var tokenData = Json.decode(bodyData.data, StructureComboTokenRequest.LoginTokenData.class);
        var responseData = Main.getAuthenticationSystem().getSessionKeyAuthenticator().authenticate(AuthenticationSystem.fromComboTokenRequest(ctx, bodyData, tokenData));
        ctx.json(responseData).contentType("application/json");
    }

    /**
     * uid -> account's id<br>
     * token -> token to verify<br>
     */
    private static void tokenLoginRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureLoginTokenRequest.class);
        if (bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }

        var responseData = Main.getAuthenticationSystem().getTokenAuthenticator().authenticate(AuthenticationSystem.fromTokenRequest(ctx, bodyData));
        ctx.json(responseData).contentType("application/json");
    }

    /**
     * app_id -> mihoyo's game id. (4 for genshin impact)<br>
     * app_sign -> application signature<br>
     * uid -> account's id<br>
     * scene_id -> experiment's scene<br>
     * experiment_id -> experiment<br>
     * params -> optional data.<br>
     */
    private static void experimentListRequest(Context ctx) {
        JsonObject response = new JsonObject();
        var bodyData = Json.decode(ctx.body(), StructureLoginExperimentsRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            response.addProperty("retcode", Retcodes.AUTHORIZATION_USER_FAILED_ERROR_VALUE);
            response.addProperty("success", false);
            response.addProperty("message", translate("messages.http.authentication_error"));
        }
        else {
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("success", true);
            response.addProperty("message", "");
            try {
                FileReader fileReader = new FileReader("./resources/config/game_experiments.json");
                response.add("data", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                response.add("data", null);
            }
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * action_ticket -> ticket id<br>
     * device -> device information<br>
     * way -> Is it request by email address or phone number
     */
    private static void preGrantByTicketRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureGrandByTicketRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.GRANT_DEVICE_PARAMETER_ERROR_VALUE);
            response.addProperty("success", false);
            response.addProperty("message", translate("messages.http.parameter_error"));
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }

        var response = Main.getAuthenticationSystem().getDeviceVerifyAuthenticator().handleSendCode(AuthenticationSystem.fromDeviceVerifySendCodeRequest(ctx, bodyData));
        ctx.json(response).contentType("application/json");
    }

    /**
     * ticket -> ticket id<br>
     * code -> code to verify<br>
     */
    private static void grantDeviceRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureGrantDeviceRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.GRANT_DEVICE_PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            return;
        }
        var response = Main.getAuthenticationSystem().getDeviceVerifyAuthenticator().handleVerifyCode(AuthenticationSystem.fromDeviceVerifyCodeRequest(ctx, bodyData));
        ctx.json(response).contentType("application/json");
    }

    /**
     * action_ticket -> ticket id.
     */
    private static void reactivateAccountRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureReactivateAccountRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }

        var responseData = Main.getAuthenticationSystem().getReactivateAccountAuthenticator().authenticate(AuthenticationSystem.fromReactivateAccountRequest(ctx, bodyData));
        ctx.json(responseData).contentType("application/json");
    }

    /**
     * No Parameters<br>
     */
    private static void beforeVerificationRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        data.addProperty("is_heartbeat_required", ConfigManager.httpConfig.is_heartbeat_required);
        data.addProperty("is_realname_required", ConfigManager.httpConfig.is_realname_required);
        data.addProperty("is_guardian_required", ConfigManager.httpConfig.is_guardian_required);

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);

        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void pingRequest(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        data.addProperty("stop", false);
        data.addProperty("msg", "");
        data.addProperty("internal", 0);

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
    }

    /**
     * client - user's platform id.<br>
     * g_version -> genshin impact version<br>
     * game_key -> game biz<br>
     * sign -> signature<br>
     */
    private static void loginGuestAccountRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureLoginGuestAccountRequest.class);
        if (bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }

        var response = Main.getAuthenticationSystem().getLoginGuestAuthenticator().authenticate(AuthenticationSystem.fromGuestLoginRequest(ctx, bodyData));
        ctx.json(response);
    }

    /**
     * aid -> account id<br>
     * token -> token<br>
     * token_type -> token type
     */
    private static void sessionLogoutRequest(Context ctx) {
        JsonObject response = new JsonObject();
        var bodyData = Json.decode(ctx.body(), StructureSessionLogoutRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
        }
        else {
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", new JsonObject());
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://hk4e-sdk-os.hoyoverse.com/mdk/shield/api/login
        javalin.post(game_biz + "/mdk/shield/api/login", LoginRequestRouter::loginAccountRequest);
        javalin.post("mdk/shield/api/login", LoginRequestRouter::loginAccountRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/combo/granter/login/v2/login
        javalin.post(game_biz + "/combo/granter/login/v2/login", LoginRequestRouter::sessionKeyLoginRequest);
        javalin.post("combo/granter/login/v2/login", LoginRequestRouter::sessionKeyLoginRequest);

        /// https://api-account-os.hoyoverse.com/account/device/api/preGrantByTicket
        javalin.post("account/device/api/preGrantByTicket", LoginRequestRouter::preGrantByTicketRequest);

        /// https://abtest-api-data-sg.hoyoverse.com/data_abtest_api/config/experiment/list
        javalin.post("data_abtest_api/config/experiment/list", LoginRequestRouter::experimentListRequest);

        /// https://api-account-os.hoyoverse.com/account/device/api/grant
        javalin.post("account/device/api/grant", LoginRequestRouter::grantDeviceRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/mdk/shield/api/verify
        javalin.post(game_biz + "/mdk/shield/api/verify", LoginRequestRouter::tokenLoginRequest);
        javalin.post("mdk/shield/api/verify", LoginRequestRouter::tokenLoginRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/mdk/shield/api/reactivateAccount
        javalin.post(game_biz + "/mdk/shield/api/reactivateAccount", LoginRequestRouter::reactivateAccountRequest);
        javalin.post("mdk/shield/api/reactivateAccount", LoginRequestRouter::reactivateAccountRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/combo/guard/api/ping
        javalin.post(game_biz + "/combo/guard/api/ping", LoginRequestRouter::pingRequest);
        javalin.post("combo/guard/api/ping", LoginRequestRouter::pingRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/mdk/guest/guest/v2/login
        javalin.post(game_biz + "/mdk/guest/guest/v2/login", LoginRequestRouter::loginGuestAccountRequest);
        javalin.post("mdk/guest/guest/v2/login", LoginRequestRouter::loginGuestAccountRequest);

        /// https://passport-api.mihoyo.com/account/ma-cn-session/app/logout
        javalin.post("account/ma-cn-session/app/logout", LoginRequestRouter::sessionLogoutRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_global/combo/granter/login/beforeVerify
        javalin.post(game_biz + "/combo/granter/login/beforeVerify", LoginRequestRouter::beforeVerificationRequest);
    }
}
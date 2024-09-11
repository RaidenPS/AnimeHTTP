package raidenhttp.routers.gamerouters;

// Imports
import raidenhttp.Main;
import raidenhttp.authorization.AuthenticationSystem;
import raidenhttp.routers.Router;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;

// Structures
import raidenhttp.utils.structures.StructureBindMobileRequest;
import raidenhttp.utils.structures.Captcha.*;

// Translation
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.classes.Json;
import raidenhttp.utils.structures.StructureVerifyEmailAddressCreateTicketRequest;

public class CaptchaRequestRouter implements Router {
    /**
     * action_type -> action type.<br>
     * account_id -> account id.<br>
     * game_token -> account token.
     */
    private static void verifyEmailAddressCreateTicketRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureVerifyEmailAddressCreateTicketRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }

        var response = Main.getAuthenticationSystem().getCaptchaAuthenticator().handleCreateTicket(AuthenticationSystem.fromVerifyEmailAddressRequest(ctx, bodyData));
        ctx.json(response);
    }

    /**
     * action_type -> Action type<br>
     * api_name -> request name<br>
     * username -> username / email
     */
    private static void checkAccountRiskCaptcha(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureAccountRiskCheckRequest.class);
        if(bodyData == null) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.THE_REQUEST_FAILED_NETWORK_AT_RISK_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.risk_network"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }
        var response = Main.getAuthenticationSystem().getCaptchaAuthenticator().handleCheckAccountRisk(AuthenticationSystem.fromAccountRiskCheckRequest(ctx, bodyData));
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * action_ticket -> Action ticket<br>
     * action_type -> Action type.
     */
    private static void sendEmailCaptchaByActionTicket(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureEmailCaptchaByActionTicketRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }
        var response = Main.getAuthenticationSystem().getCaptchaAuthenticator().handleSendEmailCaptchaByActionTicket(AuthenticationSystem.fromSendEmailCaptchaByActionTicketRequest(ctx, bodyData));
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * action_ticket -> Action ticket.<br>
     * action_type -> Action type.
     */
    private static void verifyEmailCaptchaByActionTicket(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureEmailCaptchaByActionTicketVerifyRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }
        var response = Main.getAuthenticationSystem().getCaptchaAuthenticator().handleVerifyEmailCaptchaByActionTicket(AuthenticationSystem.fromVerifyEmailCaptchaByActionTicketRequest(ctx, bodyData));
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * action_type -> Action type.<br>
     * action_ticket -> Action ticket.<br>
     * mobile -> submitted phone.<br>
     * safe_mobile -> is using safe mobile.
     */
    private static void mobileCaptchaRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureMobileCaptchaRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }
        var response = Main.getAuthenticationSystem().getCaptchaAuthenticator().handleMobileCaptcha(AuthenticationSystem.fromMobileCaptchaRequest(ctx, bodyData));
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * area_code -> phone area code<br>
     * ticket -> ticket id<br>
     * mobile -> mobile<br>
     * captcha -> captcha<br>
     * uid -> account id.
     */
    private static void bindMobileRequest(Context ctx) {
        var bodyData = Json.decode(ctx.body(), StructureBindMobileRequest.class);
        if(bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.PARAMETER_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }
        var response = Main.getAuthenticationSystem().getCaptchaAuthenticator().handleBindMobile(AuthenticationSystem.fromBindMobileRequest(ctx, bodyData));
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }


    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://api-account-os.hoyoverse.com/account/risky/api/check
        javalin.post("account/risky/api/check", CaptchaRequestRouter::checkAccountRiskCaptcha);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_cn/mdk/shield/api/emailCaptchaByActionTicket
        javalin.post(game_biz + "/mdk/shield/api/emailCaptchaByActionTicket", CaptchaRequestRouter::sendEmailCaptchaByActionTicket);
        javalin.post("/mdk/shield/api/emailCaptchaByActionTicket", CaptchaRequestRouter::sendEmailCaptchaByActionTicket);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_cn/mdk/shield/api/verifyEmailCaptcha
        javalin.post(game_biz + "/mdk/shield/api/verifyEmailCaptcha", CaptchaRequestRouter::verifyEmailCaptchaByActionTicket);
        javalin.post("/mdk/shield/api/verifyEmailCaptcha", CaptchaRequestRouter::verifyEmailCaptchaByActionTicket);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_cn/mdk/shield/api/mobileCaptcha
        javalin.post(game_biz + "/mdk/shield/api/mobileCaptcha", CaptchaRequestRouter::mobileCaptchaRequest);
        javalin.post("/mdk/shield/api/mobileCaptcha", CaptchaRequestRouter::mobileCaptchaRequest);

        /// https://passport-api.mihoyo.com/account/auth/api/bindMobile
        javalin.post("account/auth/api/bindMobile", CaptchaRequestRouter::bindMobileRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_cn/mdk/shield/api/actionTicket
        javalin.post(game_biz + "/mdk/shield/api/actionTicket", CaptchaRequestRouter::verifyEmailAddressCreateTicketRequest);
    }
}

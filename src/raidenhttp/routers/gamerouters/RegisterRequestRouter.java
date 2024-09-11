package raidenhttp.routers.gamerouters;

// Imports
import raidenhttp.Main;
import raidenhttp.authorization.AuthenticationSystem;
import raidenhttp.routers.Router;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Objects;

// Structures
import raidenhttp.utils.structures.StructureRegisterAccountRequest;

// Translation
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.Utils;
import raidenhttp.utils.classes.Json;

public class RegisterRequestRouter implements Router {
    /**
     * No Parameters
     */
    private static void getRegisterConfigRequest(Context ctx) {
        JsonObject data = new JsonObject();
        JsonObject response = new JsonObject();
        JsonObject ipInfo = Utils.getIpInfo(ctx.ip());
        JsonObject r1 = new JsonObject();
        if(ipInfo == null || Objects.equals(ipInfo.get("countryCode").getAsString(), "-")) {
            r1.addProperty("country_code", "US");
            r1.addProperty("language", "en-us");
            r1.addProperty("area_code", "0");
        }
        else {
            r1.addProperty("country_code", ipInfo.get("countryCode").getAsString());
            r1.addProperty("language", "en-us");
            r1.addProperty("area_code", ipInfo.get("zipCode").getAsString());
        }
        data.add("ip", r1);
        data.add("area_wl", new JsonArray());
        data.add("realname_wl", new JsonArray());
        data.addProperty("guardian_age_limit", 13);
        data.addProperty("disable_mmt", false);
        data.addProperty("show_birthday", false);

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);

        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No Parameters
     */
    private static void envelopeRequest(Context ctx) {
        ctx.result(Json.toJsonString(new JsonObject())).contentType("application/json");
    }

    /**
     * username - User's nickname<br>
     * email - User's email address<br>
     * password - User's password<br>
     * confirmpassword - Confirm password check
     */
    private static void registerAccountRequest(Context ctx) {
        // NOTE: This is different from the official servers.

        JsonObject data = new JsonObject();
        data.addProperty("username", ctx.formParam("username"));
        data.addProperty("email", ctx.formParam("email"));
        data.addProperty("password", Objects.requireNonNull(ctx.formParam("password")));
        data.addProperty("confirmpassword", Objects.requireNonNull(ctx.formParam("passwordv2")));
        data.addProperty("timestamp", String.valueOf(System.currentTimeMillis()));
        data.addProperty("ipaddress", ctx.ip());

        var bodyData = Json.decode(data, StructureRegisterAccountRequest.class);
        if (bodyData == null || !Json.isFullStructure(bodyData)) {
            JsonObject response = new JsonObject();
            response.addProperty("retcode", Retcodes.SYSTEM_ERROR_VALUE);
            response.addProperty("message", translate("messages.http.parameter_error"));
            response.add("data", null);
            ctx.result(Json.toJsonString(response)).contentType("application/json");
            return;
        }

        var response = Main.getAuthenticationSystem().getRegisterAuthenticator().authenticate(AuthenticationSystem.fromRegisterAccountRequest(ctx, bodyData));
        ctx.json(response);
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://api-account-os.hoyoverse.com/account/auth/api/getConfig
        javalin.post("account/auth/api/getConfig", RegisterRequestRouter::getRegisterConfigRequest);

        /// https://sentry.eks.hoyoverse.com/api/39/envelope/
        javalin.post("api/39/envelope", RegisterRequestRouter::envelopeRequest);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shield/api/registByEmail
        javalin.post(game_biz + "/mdk/shield/api/registByEmail", RegisterRequestRouter::registerAccountRequest);
    }
}
package raidenhttp.routers.gamerouters;

// Imports
import raidenhttp.routers.Router;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.FileReader;
import java.io.IOException;

// Translate
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.Utils;
import raidenhttp.utils.classes.Json;

public class PaymentRequestRouter implements Router {
    /**
     * game_biz -> Game biz key<br>
     * currency -> payment currency (optional)
     */
    private static void getListPriceTier(Context ctx) {
        JsonObject response = new JsonObject();
        String game_biz = ctx.queryParam("game_biz");
        String currency = ctx.queryParam("currency");

        if(!Utils.isCorrectGameBiz(game_biz)) {
            response.addProperty("retcode", Retcodes.GAME_BIZ_MISSION_ERROR_VALUE);
            response.addProperty("message", "game biz missing");
            response.add("data", null);
        }
        else {
            if(currency == null || currency.length() != 3) currency = "USD";
            JsonObject data = new JsonObject();

            data.addProperty("suggested_currency", currency);

            try {
                FileReader fileReader = new FileReader("./resources/config/payment/tiers.json");
                data.add("tiers", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("tiers", null);
            }
            data.addProperty("price_tier_version", "0");

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * V2
     */
    private static void getListPriceTierV2(Context ctx) {
        JsonObject response = new JsonObject();
        String game_biz = ctx.queryParam("game_biz");
        String currency = ctx.queryParam("currency");

        if(!Utils.isCorrectGameBiz(game_biz) || currency == null) {
            response.addProperty("retcode", Retcodes.RETCODE_FAILED);
            response.addProperty("message", translate("messages.http.invalid_arguments"));
            response.add("data", null);
        }
        else {
            JsonObject data = new JsonObject();
            data.addProperty("suggested_currency", currency);

            try {
                FileReader fileReader = new FileReader("./resources/config/payment/tiers.json");
                data.add("tiers", Json.parseObject(fileReader).getAsJsonArray());
            }catch(IOException e) {
                data.add("tiers", null);
            }
            data.addProperty("price_tier_version", String.valueOf(System.currentTimeMillis() / 1000));

            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * game_biz -> Game biz key.
     */
    private static void getCurrencyAndCountryByIp(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        data.addProperty("currency", "USD");
        data.addProperty("country", "");
        data.addProperty("price_tier_version", String.valueOf(System.currentTimeMillis() / 1000));

        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);

        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No parameters
     */
    private static void getListPaymentPlatformsAndroid(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        try {
            FileReader fileReader = new FileReader("./resources/config/payment/android_platforms.json");
            data.add("pay_plats", Json.parseObject(fileReader).getAsJsonArray());
        }catch(IOException e) {
            data.add("pay_plats", null);
        }
        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * No parameters
     */
    private static void getListPaymentPlatformsPC(Context ctx) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();

        try {
            FileReader fileReader = new FileReader("./resources/config/payment/pc_platforms.json");
            data.add("pay_plats", Json.parseObject(fileReader).getAsJsonArray());
        }catch(IOException e) {
            data.add("pay_plats", null);
        }
        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        response.add("data", data);
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/listPriceTier
        javalin.get(game_biz + "/mdk/shopwindow/shopwindow/listPriceTier", PaymentRequestRouter::getListPriceTier);
        javalin.get("mdk/shopwindow/shopwindow/listPriceTier", PaymentRequestRouter::getListPriceTier);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/listPriceTierV2
        javalin.get(game_biz + "/mdk/shopwindow/shopwindow/listPriceTierV2", PaymentRequestRouter::getListPriceTierV2);
        javalin.get("mdk/shopwindow/shopwindow/listPriceTierV2", PaymentRequestRouter::getListPriceTierV2);

        /// https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/getCurrencyAndCountryByIp
        javalin.get(game_biz + "/mdk/shopwindow/shopwindow/getCurrencyAndCountryByIp", PaymentRequestRouter::getCurrencyAndCountryByIp);
        javalin.get("mdk/shopwindow/shopwindow/getCurrencyAndCountryByIp", PaymentRequestRouter::getCurrencyAndCountryByIp);

        // https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/listPayPlat (Android)
        javalin.get(game_biz + "/mdk/luckycat/luckycat/listPayPlat", PaymentRequestRouter::getListPaymentPlatformsAndroid);
        javalin.get("mdk/luckycat/luckycat/listPayPlat", PaymentRequestRouter::getListPaymentPlatformsAndroid);

        // https://hk4e-sdk-os.hoyoverse.com/mdk/tally/tally/listPayPlat (PC)
        javalin.get(game_biz + "/mdk/tally/tally/listPayPlat", PaymentRequestRouter::getListPaymentPlatformsPC);
        javalin.get("mdk/tally/tally/listPayPlat", PaymentRequestRouter::getListPaymentPlatformsPC);
    }
}

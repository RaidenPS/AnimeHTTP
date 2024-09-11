package raidenhttp.routers.gamerouters;

// Imports
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.javalin.http.Context;
import raidenhttp.routers.Router;
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.classes.Json;
import java.io.FileReader;
import java.io.IOException;

public class AnnouncementRequestRouter implements Router {
    /**
     * ?bundle_id -> bundle id (hk4e_global??)<br>
     * channel_id -> channel id<br>
     * game -> hk4e<br>
     * game_biz -> game biz<br>
     * lang -> language (en)<br>
     * level -> player's level<br>
     * platform -> player's platform<br>
     * region -> region<br>
     * uid -> player's user id.<br>
     */
    private static void getAlertAnnouncement(Context ctx) {
        JsonObject response = new JsonObject();
        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        try {
            FileReader fileReader = new FileReader("./resources/config/announcement/getAlertAnn.json");
            response.add("data", Json.parseObject(fileReader).getAsJsonObject());
        }catch(IOException e) {
            response.add("data", null);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    /**
     * ?bundle_id -> bundle id (hk4e_global??)<br>
     * channel_id -> channel id<br>
     * game -> hk4e<br>
     * game_biz -> game biz<br>
     * lang -> language (en)<br>
     * level -> player's level<br>
     * platform -> player's platform<br>
     * region -> region<br>
     * uid -> player's user id.<br>
     */
    private static void getAlertPicture(Context ctx) {
        JsonObject response = new JsonObject();
        response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
        response.addProperty("message", "OK");
        try {
            FileReader fileReader = new FileReader("./resources/config/announcement/getAlertPic.json");
            response.add("data", Json.parseObject(fileReader).getAsJsonObject());
        }catch(IOException e) {
            response.add("data", null);
        }

        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://hk4e-api.mihoyo.com/common/hk4e_cn/announcement/api/getAlertAnn
        javalin.get("common/" + game_biz + "/announcement/api/getAlertAnn", AnnouncementRequestRouter::getAlertAnnouncement);

        /// https://hk4e-api.mihoyo.com/common/hk4e_cn/announcement/api/getAlertAnn
        javalin.get("common/" + game_biz + "/announcement/api/getAlertPic", AnnouncementRequestRouter::getAlertPicture);
    }
}

package raidenhttp.routers;

// Imports
import io.javalin.http.Handler;
import io.javalin.Javalin;
import raidenhttp.utils.Utils;

public interface Router {
    String game_biz = Utils.getGameBiz();

    void applyRoutes(Javalin javalin);

    default Javalin allRoutes(Javalin javalin, String path, Handler ctx) {
        javalin.get(path, ctx);
        javalin.post(path, ctx);
        javalin.put(path, ctx);
        javalin.patch(path, ctx);
        javalin.delete(path, ctx);
        return javalin;
    }
}
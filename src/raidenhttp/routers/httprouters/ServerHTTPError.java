package raidenhttp.routers.httprouters;

// Imports
import raidenhttp.routers.Router;
import io.javalin.Javalin;

public class ServerHTTPError implements Router {
    @Override
    public void applyRoutes(Javalin javalin) {
        javalin.error(500, ctx -> {
            ctx.contentType("text/html;charset=UTF-8");
            ctx.result("内部服务器错误 (Server Inside Error)");
        });
    }
}

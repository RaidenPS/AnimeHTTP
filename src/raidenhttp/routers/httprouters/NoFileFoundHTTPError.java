package raidenhttp.routers.httprouters;

/// Modules
import raidenhttp.routers.Router;
import io.javalin.Javalin;

public class NoFileFoundHTTPError implements Router {
    @Override
    public void applyRoutes(Javalin javalin) {
        javalin.error(404, ctx -> {
            ctx.contentType("text/html;charset=UTF-8");
            ctx.result("页面未找到 (404 File Not Found)");
        });
    }
}
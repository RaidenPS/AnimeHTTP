package raidenhttp.routers.httprouters;

// Imports
import raidenhttp.routers.Router;
import io.javalin.Javalin;

public class BadMessageHTTPError implements Router {
    @Override
    public void applyRoutes(Javalin javalin) {
        javalin.error(400, ctx -> {
            ctx.contentType("text/html;charset=UTF-8");
            ctx.result("错误请求 (Bad Message)");
        });
    }
}
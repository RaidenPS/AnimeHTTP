package raidenhttp.routers.httprouters;

// Imports
import io.javalin.Javalin;
import raidenhttp.routers.Router;

public class ForbiddenHTTPError implements Router {
    @Override
    public void applyRoutes(Javalin javalin) {
        javalin.error(403, ctx -> {
            ctx.contentType("text/html;charset=UTF-8");
            ctx.result("访问被拒绝 (403 Forbidden)");
        });
    }
}
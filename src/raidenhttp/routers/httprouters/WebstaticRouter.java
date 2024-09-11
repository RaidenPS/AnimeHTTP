package raidenhttp.routers.httprouters;

// Imports
import raidenhttp.routers.Router;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.nio.file.*;

public class WebstaticRouter implements Router {
    private static void getWebstaticFiles(Context ctx) {
        String path = Paths.get("").toAbsolutePath() + "/resources/html/" + ctx.path();
        Path path_obj = Paths.get(path);
        if(Files.exists(path_obj) && !Files.isDirectory(path_obj)) {
            try {
                if(path.contains(".html")) {
                    ctx.contentType("text/html");
                }
                ctx.result(Files.readString(path_obj));
            }catch(java.io.IOException ex) {
                ctx.contentType("text/html;charset=UTF-8");
                ctx.result(ex.getMessage());
            }
        }
        else {
            ctx.status(404);
        }
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        /// https://webstatic.hoyoverse.com/ -> http://127.0.0.1:8881/webstatic/
        javalin.get("webstatic/*", WebstaticRouter::getWebstaticFiles);

        /// https://launcher-webstatic.hoyoverse.com/ -> http://127.0.0.1:8881/launcher-public/
        javalin.get("launcher-public/*", WebstaticRouter::getWebstaticFiles);
    }
}

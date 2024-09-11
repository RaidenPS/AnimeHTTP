package raidenhttp.routers.httprouters;

// Imports
import raidenhttp.Main;
import raidenhttp.routers.Router;
import io.javalin.http.ContentType;
import io.javalin.Javalin;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Index implements Router {
    @Override
    public void applyRoutes(Javalin javalin) {
        javalin.get("/", ctx -> {
            // Send file
            File file = new File("./resources/html/index.html");
            var filePath = file.getPath();
            try {
                ContentType fromExtension = ContentType.getContentTypeByExtension(filePath.substring(filePath.lastIndexOf(".") + 1));
                ctx.contentType(fromExtension != null ? fromExtension : ContentType.TEXT_HTML);
                ctx.result(Files.readAllBytes(Path.of(file.getPath())));
            }
            catch (FileNotFoundException ignored) {
                Main.getLogger().info("File is not found.");
            }
        });
    }
}
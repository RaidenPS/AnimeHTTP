package raidenhttp.routers.httprouters;

// Imports
import raidenhttp.Main;
import raidenhttp.routers.Router;
import freemarker.template.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.*;
import java.util.*;

// Translate
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Utils;

public class RenderingRouter implements Router {
    private static final Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_33);

    private static Map<String, Object> getModelInfo(String templateName) {
        String dispatchDomain = Utils.getDispatchDomain();
        String game_biz = Utils.getGameBiz();
        Map<String, Object> model = new HashMap<>();
        if(Objects.equals(templateName, "/account/register.tmpl")) {
            model.put("title", translate("messages.template.accReg"));
            model.put("header", translate("messages.template.registrByEmail"));
            model.put("username_msg", translate("messages.template.username"));
            model.put("email_msg", translate("messages.template.email"));
            model.put("password_msg", translate("messages.template.password"));
            model.put("confirmpass_msg", translate("messages.template.confirmpass"));
            model.put("reg_btn", translate("messages.template.confirmReg"));
            model.put("regist_api", dispatchDomain + '/' + game_biz + "/mdk/shield/api/registByEmail");
        }
        if(Objects.equals(templateName, "/account/recover.tmpl")) {
            model.put("title", translate("messages.template.forgotpass"));
            model.put("header", translate("messages.template.recoverByEmail"));
            model.put("username_msg", translate("messages.template.username"));
            model.put("email_msg", translate("messages.template.email"));
            model.put("recover_btn", translate("messages.template.confirmRec"));
            model.put("recover_api", dispatchDomain + '/' + game_biz + "/mdk/shield/api/recoverByEmail");
        }
        return model;
    }

    private static void renderTemplates(Context ctx) {
        String templateName = ctx.path();
        if(templateName.equals("account")) {
            templateName = "index.tmpl";
        }

        try {
            StringWriter writer = new StringWriter();
            Template template = freemarkerConfig.getTemplate(templateName);
            template.process(getModelInfo(templateName), writer);
            ctx.html(writer.toString());
        }catch (Exception e) {
            Main.getLogger().debug(String.format("The template %s could not be loaded. ERROR: [%s]", templateName, e.getMessage()));
        }
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        try {
            freemarkerConfig.setDirectoryForTemplateLoading(new File("./resources/templates/"));
            Main.getLogger().debug("Loaded template config");
        }catch (Exception ex) {
            Main.getLogger().error("Could not load the template config.");
            System.exit(1);
        }
        javalin.get("*.tmpl", RenderingRouter::renderTemplates);
    }
}

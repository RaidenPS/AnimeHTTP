package raidenhttp.routers;

// Imports
import raidenhttp.Main;
import raidenhttp.config.ConfigManager;
import io.javalin.Javalin;
import io.javalin.json.JavalinGson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public final class HTTPServer {
    private final Javalin javalin;
    private final RateLimiter rateLimiter = new RateLimiter(ConfigManager.httpConfig.maxRequests, ConfigManager.httpConfig.timeMillis);
    private final IPBlackList ipBlackList = new IPBlackList(ConfigManager.httpConfig.badIPS, ConfigManager.httpConfig.whiteListIPS, ConfigManager.httpConfig.enableIPWhitelist);

    public HTTPServer() {
        this.javalin = Javalin.create(config -> {
            config.jetty.modifyServer(this::createServer);
            config.jsonMapper(new JavalinGson());
        });
    }

    public void addRouter(Class<? extends Router> router, Object... args) {
        var types = new Class<?>[args.length];
        for (var argument : args) types[args.length - 1] = argument.getClass();
        try {
            var constructor = router.getDeclaredConstructor(types);
            var routerInstance = constructor.newInstance(args);
            routerInstance.applyRoutes(this.javalin);
        } catch (Exception e) {
            Main.getLogger().error("An error occurred while add router to the http server. Error: ", e);
        }
    }

    private void createServer(Server server) {
        ServerConnector serverConnector;
        if(ConfigManager.httpConfig.useHTTPSProtocol) {
            SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
            sslContextFactory.setKeyStorePath("./resources/SSL/certificate.p12");
            sslContextFactory.setKeyStorePassword(ConfigManager.httpConfig.certPassword);
            serverConnector = new ServerConnector(server, sslContextFactory);
        }
        else {
            serverConnector  = new ServerConnector(server);
        }
        serverConnector.setPort(ConfigManager.httpConfig.port);
        serverConnector.setHost(ConfigManager.httpConfig.host);
        server.addConnector(serverConnector);
    }

    public void logRequests() {
        if(ConfigManager.httpConfig.logRequests) {
            this.javalin.before(ctx -> Main.getLogger().debug(String.format("[%s] Incoming Request: -> %s %s", ctx.ip(), ctx.method(), ctx.path())));
        }
    }

    public void start() {
        this.javalin.start(ConfigManager.httpConfig.host, ConfigManager.httpConfig.port);
        this.javalin.before(rateLimiter);
        this.javalin.before(ipBlackList);

        Main.getLogger().info(String.format("The http server started successfully. IP: %s:%s", ConfigManager.httpConfig.host, ConfigManager.httpConfig.port));
        this.logRequests();
    }
}
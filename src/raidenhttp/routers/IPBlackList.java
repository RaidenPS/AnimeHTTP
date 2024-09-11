package raidenhttp.routers;

// Imports
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.HashSet;
import java.util.Set;

public class IPBlackList implements Handler {
    private final Set<String> bannedIps = new HashSet<>();
    private final Set<String> whitelistIps = new HashSet<>();
    private final boolean whiteListEnabled;

    public IPBlackList(Set<String> initialBannedIps, Set<String> initialWhitelistIps, boolean whitelistEnabled) {
        this.bannedIps.addAll(initialBannedIps);
        this.whitelistIps.addAll(initialWhitelistIps);
        this.whiteListEnabled = whitelistEnabled;
    }

    @Override
    public void handle(Context ctx) {
        String clientIp = ctx.ip();
        if(whiteListEnabled) {
            if(!whitelistIps.contains(clientIp)) {
                ctx.status(403);
            }
        }

        if (bannedIps.contains(clientIp)) {
            ctx.status(403);
        }
    }
}

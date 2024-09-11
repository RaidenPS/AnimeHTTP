package raidenhttp.routers;

// Imports
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter implements Handler {
    private final int maxRequests;
    private final long timeWindowMillis;
    private final Map<String, RequestInfo> clientRequests = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequests, long timeWindowMillis) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeWindowMillis;
    }

    @Override
    public void handle(Context ctx) {
        String clientIp = ctx.ip();
        RequestInfo requestInfo = clientRequests.getOrDefault(clientIp, new RequestInfo(0, Instant.now().toEpochMilli()));

        long currentTime = Instant.now().toEpochMilli();
        if (currentTime - requestInfo.startTime > timeWindowMillis) {
            requestInfo = new RequestInfo(0, currentTime);
        }

        if (requestInfo.requestCount < maxRequests) {
            requestInfo.requestCount++;
            clientRequests.put(clientIp, requestInfo);
        } else {
            ctx.status(403);
        }
    }

    private static class RequestInfo {
        int requestCount;
        long startTime;

        RequestInfo(int requestCount, long startTime) {
            this.requestCount = requestCount;
            this.startTime = startTime;
        }
    }
}
package raidenhttp.utils.results;

public class ResultGrantDeviceRequest {
    public static class SessionInfo {
        public String game_token;
        public String login_ticket;
    }

    public int retcode;
    public String message;
    public SessionInfo data = new SessionInfo();
}
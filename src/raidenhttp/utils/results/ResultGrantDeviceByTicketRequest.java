package raidenhttp.utils.results;

public class ResultGrantDeviceByTicketRequest {
    public static class TicketInfo {
        public String ticket;
    }

    public int retcode;
    public boolean success;
    public String message;
    public TicketInfo data = new TicketInfo();
}

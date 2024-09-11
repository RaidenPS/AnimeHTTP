package raidenhttp.utils.results;

public class ResultVerifyEmailAddressCreateTicketRequest {
    public static class DataInfo {
        public String ticket;
    }

    public int retcode;
    public String message;
    public DataInfo data = new DataInfo();
}

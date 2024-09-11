package raidenhttp.utils.results;

public class ResultLoginGuestRequest {
    public static class AccountData {
        public int account_type;
        public boolean newly;
        public String guest_id;
    }

    public String message;
    public int retcode;
    public AccountData data = new AccountData();
}

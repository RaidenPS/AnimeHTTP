package raidenhttp.utils.results;

public class ResultReactivateAccountRequest {
    public static class AccountInfo {
        public String uid;
        public String name;
        public String email;
        public String mobile;
        public String is_email_verify;
        public String realname;
        public String identity_card;
        public String token;
        public String safe_mobile;
        public String facebook_name;
        public String google_name;
        public String twitter_name;
        public String game_center_name;
        public String apple_name;
        public String sony_name;
        public String tap_name;
        public String country;
        public String reactivate_ticket;
        public String area_code;
        public String device_grant_ticket;
        public String steam_name;
        public String unmasked_email;
        public int unmasked_email_type;
        public String cx_name;
    }

    public static class dataInfo {
        public AccountInfo account = new AccountInfo();
        public boolean device_grant_required;
        public boolean safe_moblie_required;
        public String realname_operation;
    }

    public int retcode;
    public String message;
    public dataInfo data = new dataInfo();
}
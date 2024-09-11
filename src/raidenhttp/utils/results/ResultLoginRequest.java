package raidenhttp.utils.results;

public class ResultLoginRequest {
	public static class VerifyData {
		public VerifyAccountData account = new VerifyAccountData();
		public boolean device_grant_required = false;
		public String realname_operation;
		public boolean realperson_required = false;
		public boolean safe_moblie_required = false;
		public boolean reactivate_required = false;
	}

	public static class VerifyAccountData {
		public String apple_name;
		public String area_code;
		public String country;
		public String cx_name;
		public String device_grant_ticket;
		public String email;
		public String facebook_name;
		public String game_center_name;
		public String google_name;
		public String identity_card;
		public String is_email_verify;
		public String mobile;
		public String name;
		public String reactivate_ticket;
		public String realname;
		public String safe_mobile;
		public String sony_name;
		public String steam_name;
		public String tap_name;
		public String token;
		public String twitter_name;
		public String uid;
		public String unmasked_email;
		public int unmasked_email_type=0;
	}

    public String message;
    public int retcode;
    public VerifyData data = new VerifyData();
}
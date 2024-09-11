package raidenhttp.utils.results;

// Imports
import java.util.ArrayList;

public class ResultComboTokenRequest {
    public static class FatigueRemindInfo {
        public String nickname;
        public int reset_point;
        public ArrayList<Integer> durations = new ArrayList<>();
    }

    public static class LoginDataInfo {
        public boolean guest;
    }

    public static class LoginData {
        public int account_type;
        public boolean heartbeat;
        public String combo_id;
        public String combo_token;
        public String open_id;
        public String data;
        public String fatigue_remind;
    }

    public String message;
    public int retcode;
    public LoginData data = new LoginData();
}

// response.data.fatigue_remind = String.format("{\"fatigue_remind\": {\"nickname\": \"%s\",\"reset_point\": 14400,\"durations\": [180, 240, 300]}}", account.getUsername());
/// TODO: Understand what is fatigue_remind.
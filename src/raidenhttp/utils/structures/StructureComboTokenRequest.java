package raidenhttp.utils.structures;

public class StructureComboTokenRequest {
    public int app_id;
    public int channel_id;
    public String data;
    public String device;
    public String sign;

    public static class LoginTokenData {
        public String uid;
        public String token;
        public boolean guest;
    }
}

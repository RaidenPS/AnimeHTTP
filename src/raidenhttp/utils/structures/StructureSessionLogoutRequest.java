package raidenhttp.utils.structures;

public class StructureSessionLogoutRequest {
    public String aid;

    public static class TokenInfo {
        public String token;
        public int token_type;
    }
}
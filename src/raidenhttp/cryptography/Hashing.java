package raidenhttp.cryptography;

// Imports
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hashing {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /// Encoding
    public static byte[] base64Decode(String toDecode) {
        return Base64.getDecoder().decode(toDecode);
    }
    public static String base64Encode(byte[] toEncode) {
        return Base64.getEncoder().encodeToString(toEncode);
    }

    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "";
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void xorEncrypt(byte[] packet, byte[] key) {
        for (int i = 0; i < packet.length; i++) {
            packet[i] ^= key[i % key.length];
        }
    }

    /// Hashing Algorithms
    public static String hashSha256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return base64Encode(hexString.toString().getBytes(StandardCharsets.UTF_8));
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}

package raidenhttp.cryptography;

public class RC4 {
    private static byte[] strToBytes(String str) {
        return str.getBytes();
    }

    private static String bytesToStr(byte[] bytes) {
        return new String(bytes);
    }

    public static String rc4(String key, String input, boolean decrypt) {
        byte[] keyBytes = strToBytes(key);
        byte[] inputBytes;

        if (decrypt) {
            inputBytes = Hashing.base64Decode(input);
        } else {
            inputBytes = strToBytes(input);
        }

        byte[] result = new byte[inputBytes.length];
        byte[] s = new byte[256];
        int j = 0;

        for (int i = 0; i < 256; i++) {
            s[i] = (byte) i;
        }
        for (int i = 0; i < 256; i++) {
            j = (j + s[i] + keyBytes[i % keyBytes.length]) & 0xFF;
            byte temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }

        int i = 0;
        j = 0;
        for (int y = 0; y < inputBytes.length; y++) {
            i = (i + 1) & 0xFF;
            j = (j + s[i]) & 0xFF;
            byte temp = s[i];
            s[i] = s[j];
            s[j] = temp;
            byte k = s[(s[i] + s[j]) & 0xFF];
            result[y] = (byte) (inputBytes[y] ^ k);
        }

        if (decrypt) {
            return bytesToStr(result);
        } else {
            return Hashing.base64Encode(result);
        }
    }
}

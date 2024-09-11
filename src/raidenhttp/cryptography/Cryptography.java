package raidenhttp.cryptography;

// Imports
import raidenhttp.Main;
import raidenhttp.utils.FileUtils;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.crypto.Cipher;

// Results
import raidenhttp.utils.results.ResultCurrentRegionRequest;

public class Cryptography {
    private static final String DIGIT_CHARACTERS = "0123456789";
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();
    private static String passwordPublicFileName = "";
    private static String passwordPrivateFileName = "";
    public static byte[] DISPATCH_SEED;
    public static byte[] DISPATCH_KEY;
    public static PrivateKey CUR_SIGNING_KEY;
    public static final Map<Integer, PublicKey> EncryptionKeys = new HashMap<>();
    private static final Map<Integer, PrivateKey> DecryptionKeys = new HashMap<>();

    public static byte[] createSessionKey(int length) {
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }

    public static String generateCode(int len) {
        StringBuilder code = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int index = secureRandom.nextInt(DIGIT_CHARACTERS.length());
            code.append(DIGIT_CHARACTERS.charAt(index));
        }
        return code.toString();
    }

    public static String generateActionToken(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public static void loadCryptoKeys() throws Exception {
        passwordPublicFileName = "pb_password_overseas.der";
        passwordPrivateFileName = "pv_password_overseas.der";

        DISPATCH_SEED = FileUtils.readFile("resources/crypto/dispatchSeed.bin");
        DISPATCH_KEY = FileUtils.readFile("resources/crypto/dispatchKey.bin");

        CUR_SIGNING_KEY = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(FileUtils.readFile("resources/crypto/certificates/SigningKey.der")));
        Pattern pattern = Pattern.compile("([0-9]+)\\.(pub|priv)\\.der");
        for (Path path : FileUtils.getPathsFromResource("resources/crypto/gamekeys")) {
            if (path.toString().endsWith(".pub.der")) {
                var m = pattern.matcher(path.getFileName().toString());
                if (m.matches()) {
                    var key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(FileUtils.readFile(path.toString())));
                    EncryptionKeys.put(Integer.valueOf(m.group(1)), key);
                }
            }
            else {
                if (path.toString().endsWith(".priv.der")) {
                    var m = pattern.matcher(path.getFileName().toString());
                    if (m.matches()) {
                        var key = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(FileUtils.readFile(path.toString())));
                        DecryptionKeys.put(Integer.valueOf(m.group(1)), key);
                    }
                }
            }
        }
        Main.getLogger().debug(String.format("Loaded total public keys: %d", EncryptionKeys.size()));
        Main.getLogger().debug(String.format("Loaded total private keys: %d", DecryptionKeys.size()));
    }

    public static String EncryptPassword(String password) throws Exception {
        byte[] key = FileUtils.readFile("resources/crypto/certificates/%s".formatted(passwordPublicFileName));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey private_key = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, private_key);
        return Hashing.base64Encode(cipher.doFinal(password.getBytes()));
    }

    public static String DecryptPassword(String password) throws Exception {
        byte[] key = FileUtils.readFile("resources/crypto/certificates/%s".formatted(passwordPrivateFileName));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey private_key = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); /// NoPadding
        cipher.init(Cipher.DECRYPT_MODE, private_key);
        return new String(cipher.doFinal(Hashing.base64Decode(password)), java.nio.charset.StandardCharsets.UTF_8);
    }

    public static ResultCurrentRegionRequest encryptAndSignRegionData(byte[] regionInfo, String key_id) throws Exception {
        if (key_id == null) {
            throw new Exception("Key ID was not set");
        }

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, EncryptionKeys.get(Integer.valueOf(key_id)));

        ByteArrayOutputStream encryptedRegionInfoStream = new ByteArrayOutputStream();

        int chunkSize = 256 - 11;
        int regionInfoLength = regionInfo.length;
        int numChunks = (int) Math.ceil(regionInfoLength / (double) chunkSize);
        for (int i = 0; i < numChunks; i++) {
            byte[] chunk = Arrays.copyOfRange(regionInfo, i * chunkSize, Math.min((i + 1) * chunkSize, regionInfoLength));
            byte[] encryptedChunk = cipher.doFinal(chunk);
            encryptedRegionInfoStream.write(encryptedChunk);
        }

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(CUR_SIGNING_KEY);
        privateSignature.update(regionInfo);

        var rsp = new ResultCurrentRegionRequest();
        rsp.content = Hashing.base64Encode(encryptedRegionInfoStream.toByteArray());
        rsp.sign = Hashing.base64Encode(privateSignature.sign());
        return rsp;
    }
 }

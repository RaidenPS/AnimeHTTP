package raidenhttp.database;

// Imports
import raidenhttp.config.ConfigManager;
import raidenhttp.enums.AccountType;
import raidenhttp.cryptography.*;
import com.mongodb.client.MongoCollection;
import dev.morphia.query.experimental.filters.Filters;
import io.netty.util.concurrent.FastThreadLocalThread;
import java.util.Objects;
import java.util.concurrent.*;
import org.bson.Document;

// Document objects
import raidenhttp.database.entities.Account;

// Structures
import raidenhttp.utils.structures.StructureRegisterAccountRequest;

// Utils
import static dev.morphia.query.experimental.filters.Filters.eq;
import raidenhttp.utils.Utils;

public final class DatabaseHelper {
    private static final ExecutorService eventExecutor =new ThreadPoolExecutor(6, 6, 60, TimeUnit.SECONDS,new LinkedBlockingDeque<>(), FastThreadLocalThread::new, new ThreadPoolExecutor.AbortPolicy());

    /**
        Returns an account object by given email address.
        @param email Email address
        @return Account object.
     */
    public static Account getAccountByEmail(String email) {
        return DatabaseManager.getHTTPDataStore().find(Account.class).filter(eq("email", email)).first();
    }

    /**
     Returns an account object by given email address that contains asterisks (masked).
     @param email Email address
     @return Account object.
     */
    public static Account getAccountByMaskedEmail(String email) {
        return DatabaseManager.getHTTPDataStore().find(Account.class).filter(eq("maskedEmail", email)).first();
    }

    /**
     Returns an account object (guest) by device id.
     @param deviceId Device identification.
     @return Account object of a guest member. A guest user have <b>isGuest</b> enabled.
     */
    public static Account getAccountByGuest(String deviceId) {
        return DatabaseManager.getHTTPDataStore().find(Account.class).filter(Filters.eq("deviceId", deviceId), Filters.eq("isGuest", true)).first();
    }

    /**
     Returns an account object by given user id.
     @param id User id
     @return Account object.
     */
    public static Account getAccountById(String id) {
        return DatabaseManager.getHTTPDataStore().find(Account.class).filter(eq("_id", id)).first();
    }

    /**
     Returns an account object by device id.
     @param deviceId Device identification.
     @return Account object.
     */
    public static Account getAccountByDeviceId(String deviceId) {
        return DatabaseManager.getHTTPDataStore().find(Account.class).filter(eq("deviceId", deviceId)).first();
    }

    /**
     Returns an account object by username.
     @param username Account name.
     @return Account object.
     */
    public static Account getAccountByName(String username) {
        return DatabaseManager.getHTTPDataStore().find(Account.class).filter(Filters.eq("username", username)).first();
    }

    /**
     Returns an account object by specific ticket type and id.
     @param ticket_type The ticket type (0 for device, 1 for reactivate and 2 for action).
     @param ticket  The ticket id.
     @return Account object.
     */
    public static Account getAccountByTicket(int ticket_type, String ticket) {
        String ticket_id = switch (ticket_type) {
            case 0 -> "deviceTicket";
            case 1 -> "reactivateTicket";
            case 2 -> "actionTicket";
            default -> "";
        };
        return DatabaseManager.getHTTPDataStore().find(Account.class).filter(eq(ticket_id, ticket)).first();
    }

    /// SAVES

    /**
     * Saves an account object.
     * @param account Account object
     */
    public static void saveAccount(Account account) {
        DatabaseHelper.saveAccountAsync(account);
    }

    /**
     * Inserts a log sent from the game.
     * @param message Log message (Json)
     * @param logName Log name.
     */
    public static void gameLog(String message, String logName) {
        if(!ConfigManager.httpConfig.collectGameLogs) return;

        MongoCollection<Document> collection = DatabaseManager.getHTTPDataStore().getDatabase().getCollection(logName);
        Document document = Document.parse(message);
        collection.insertOne(document);
    }

    /**
     * Creates a resident account.
     */
    public static void createAccount(StructureRegisterAccountRequest account) {
        if(ConfigManager.httpConfig.disableRegistrations) return;

        String country_code = Objects.requireNonNull(Utils.getIpInfo(account.ipaddress)).get("countryCode").getAsString();
        Account user = new Account();
        user.setUsername(account.username);
        try {
            if(ConfigManager.httpConfig.crypt_password) {
                user.setPassword(Hashing.hashSha256(Cryptography.EncryptPassword(account.password)));
            }
            else {
                user.setPassword(Hashing.hashSha256(account.password));
            }
        }
        catch (Exception e) {
            user.setPassword(Hashing.hashSha256(account.password));
        }
        user.setEmail(account.email);
        user.setMaskedEmail(Utils.getMaskedEmailAddress(account.email));
        user.setIpAddress(account.ipaddress);
        user.setId(String.valueOf(DatabaseManager.getNextId(Account.class)));
        user.setCountry(Objects.equals(country_code, "-") || Objects.equals(country_code, "") ? "JP": country_code);
        user.setRegPlatformType(3);
        user.setAccountType(AccountType.MIHOYO_DEFAULT);
        user.setGuest(false);
        DatabaseHelper.saveAccount(user);
    }

    /**
     * Creates a guest account by given device id, platform type and client's ip address.
     */
    public static Account createGuestAccount(String device, int platform, String ip_address) {
        if(!ConfigManager.httpConfig.enable_guests) return null;
        String country_code = Objects.requireNonNull(Utils.getIpInfo(ip_address)).get("countryCode").getAsString();

        Account user = new Account();
        user.setUsername("Guest_" + DatabaseManager.getNextId(Account.class));
        user.setId(String.valueOf(DatabaseManager.getNextId(Account.class)));
        user.setDeviceId(device);
        user.setGuest(true);
        user.setRegPlatformType(platform);
        user.setIpAddress(ip_address);
        user.setCountry(Objects.equals(country_code, "-") || Objects.equals(country_code, "") ? "JP": country_code);
        user.setAccountType(AccountType.MIHOYO_GUEST);
        user.save();
        return user;
    }

    /**
     * Saves an object on the account datastore.
     *
     * @param object The object to save.
     */
    public static void saveAccountAsync(Object object) {
        DatabaseHelper.eventExecutor.submit(() -> DatabaseManager.getHTTPDataStore().save(object));
    }
}
package raidenhttp.database.entities;

// Imports
import lombok.Getter;
import lombok.Setter;
import raidenhttp.enums.AccountType;
import raidenhttp.cryptography.*;
import raidenhttp.database.DatabaseHelper;
import dev.morphia.annotations.*;
import java.util.*;
import org.bson.Document;

@Entity(value = "accounts", useDiscriminator = false)
public class Account {
    @Getter @Setter @Id private String id;
    @Getter @Setter @Indexed(options = @IndexOptions(unique = true)) @Collation(locale = "simple", caseLevel = true) public String username;
    @Getter @Setter private String password;
    @Getter @Setter private String email;
    private String token;
    @Getter private String sessionKey; // Session token for dispatch server
    @Getter @Setter private String deviceId;
    @Getter private int accountType;
    @Setter private boolean isBanned;
    private String banReason;
    private int banStartTime;
    private int banEndTime;
    @Getter private boolean deviceVerify;
    @Getter private String deviceTicket;
    @Getter @Setter private String deviceVerifyCode;
    @Getter private boolean personVerify;
    @Getter private String realnameOperation;
    @Getter @Setter private boolean phoneVerify;
    @Getter private boolean needReactivation;
    @Getter private String reactivateTicket;
    @Getter @Setter private boolean isGuest;
    @Setter private int regPlatformType;
    @Setter @Getter private String country;
    @Getter @Setter private String ipAddress;
    @Getter private String phoneNumber;
    @Getter private String realName;
    @Getter private String identityCard;
    @Getter private String facebookName;
    @Getter private String googleName;
    @Getter private String twitterName;
    @Getter private String appleName;
    @Getter private String sonyName;
    @Getter private String tapName;
    @Getter private String steamName;
    @Getter private String cxName;
    private boolean isSuspended;
    @Getter @Setter private String riskActionType;
    @Setter private String actionTicket;
    @Setter private String gameBiz;
    @Getter @Setter private String safeMobile;
    @Getter @Setter private String emailCaptcha;
    @Getter @Setter private String mobileCaptcha;
    @Getter private String gameCenterName;
    @Getter private String unmaskedEmail;
    @Getter private int unmaskedEmailType;
    @Getter @Setter private String maskedEmail;
    @Getter private List<String> permissions;

    public Account() {
        this.realnameOperation = "NONE";
        this.unmaskedEmailType = 0;
        this.isSuspended = false;
        this.isBanned = false;
    }

    public void setDeviceVerify(boolean device_check) {
        this.deviceVerify = device_check;
        this.deviceTicket = (device_check) ? Cryptography.generateActionToken(15) : "";
    }

    public void setReactivateAccount(boolean reactivate_account) {
        this.needReactivation = reactivate_account;
        this.reactivateTicket = (reactivate_account) ? Cryptography.generateActionToken(15) : "";
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType.getValue();
    }

    public String generateSessionKey() {
        this.sessionKey = Hashing.bytesToHex(Cryptography.createSessionKey(32));
        this.save();
        return this.sessionKey;
    }

    public String generateLoginToken() {
        this.token = Hashing.bytesToHex(Cryptography.createSessionKey(32));
        this.save();
        return this.token;
    }

    public void save() {
        DatabaseHelper.saveAccount(this);
    }
}

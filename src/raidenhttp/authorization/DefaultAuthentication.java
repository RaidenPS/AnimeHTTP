package raidenhttp.authorization;

// Results
import raidenhttp.utils.results.*;

public final class DefaultAuthentication implements AuthenticationSystem {
    private final Authenticator<ResultLoginRequest> passwordAuthenticator = new Authenticators.PasswordAuthenticator();
    private final Authenticator<ResultLoginRequest> tokenAuthenticator = new Authenticators.TokenAuthenticator();
    private final Authenticator<ResultComboTokenRequest> sessionKeyAuthenticator = new Authenticators.SessionKeyAuthenticator();
    private final Authenticator<ResultRegisterAccountRequest> registerAccountAuthenticator = new Authenticators.RegisterAuthenticator();
    private final DeviceVerifyAuthenticator deviceVerifyAuthenticator = new Authenticators.DeviceVerifyAuthentication();
    private final Authenticator<ResultReactivateAccountRequest> reactivateAccountAuthenticator = new Authenticators.ReactivateAccountAuthenticator();
    private final Authenticator<ResultLoginGuestRequest> loginGuestRequestAuthenticator = new Authenticators.LoginGuestAuthenticator();
    private final CaptchaAuthenticator captchaAuthenticator = new Authenticators.CaptchaAuthentication();

    @Override
    public Authenticator<ResultLoginRequest> getPasswordAuthenticator() {
        return this.passwordAuthenticator;
    }

    @Override
    public Authenticator<ResultLoginRequest> getTokenAuthenticator() {
        return this.tokenAuthenticator;
    }

    @Override
    public Authenticator<ResultComboTokenRequest> getSessionKeyAuthenticator() {
        return this.sessionKeyAuthenticator;
    }

    @Override
    public Authenticator<ResultRegisterAccountRequest> getRegisterAuthenticator() {return this.registerAccountAuthenticator;}

    @Override
    public DeviceVerifyAuthenticator getDeviceVerifyAuthenticator() {return this.deviceVerifyAuthenticator;}

    @Override
    public Authenticator<ResultReactivateAccountRequest> getReactivateAccountAuthenticator() {return this.reactivateAccountAuthenticator;}

    @Override
    public Authenticator<ResultLoginGuestRequest> getLoginGuestAuthenticator() {return this.loginGuestRequestAuthenticator;}

    @Override
    public CaptchaAuthenticator getCaptchaAuthenticator() {return this.captchaAuthenticator;}
}

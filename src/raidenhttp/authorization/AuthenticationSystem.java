package raidenhttp.authorization;

// Imports
import io.javalin.http.Context;
import javax.annotation.Nullable;
import lombok.*;

// Structures and results
import raidenhttp.utils.structures.*;
import raidenhttp.utils.structures.Captcha.*;
import raidenhttp.utils.results.ResultLoginRequest;
import raidenhttp.utils.results.ResultComboTokenRequest;
import raidenhttp.utils.results.ResultRegisterAccountRequest;
import raidenhttp.utils.results.ResultReactivateAccountRequest;
import raidenhttp.utils.results.ResultLoginGuestRequest;

public interface AuthenticationSystem {

    /**
     * Generates an authentication request from a {@link StructureLoginAccountRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromPasswordRequest(Context ctx, StructureLoginAccountRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).passwordRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureLoginTokenRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromTokenRequest(Context ctx, StructureLoginTokenRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).tokenRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureComboTokenRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromComboTokenRequest(Context ctx, StructureComboTokenRequest jsonData, StructureComboTokenRequest.LoginTokenData tokenData) {
        return AuthenticationRequest.builder().context(ctx).sessionKeyRequest(jsonData).sessionKeyData(tokenData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureRegisterAccountRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromRegisterAccountRequest(Context ctx, StructureRegisterAccountRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).registerRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureGrandByTicketRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromDeviceVerifySendCodeRequest(Context ctx, StructureGrandByTicketRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).deviceVerifySendCodeRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureGrantDeviceRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromDeviceVerifyCodeRequest(Context ctx, StructureGrantDeviceRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).deviceVerifyCodeRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureReactivateAccountRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromReactivateAccountRequest(Context ctx, StructureReactivateAccountRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).reactivateAccountRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureLoginGuestAccountRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromGuestLoginRequest(Context ctx, StructureLoginGuestAccountRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).loginGuestRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureVerifyEmailAddressCreateTicketRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromVerifyEmailAddressRequest(Context ctx, StructureVerifyEmailAddressCreateTicketRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).verifyEmailAddressRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureAccountRiskCheckRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromAccountRiskCheckRequest(Context ctx, StructureAccountRiskCheckRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).accountRiskCheckRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureEmailCaptchaByActionTicketRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromSendEmailCaptchaByActionTicketRequest(Context ctx, StructureEmailCaptchaByActionTicketRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).sendEmailCaptchaByActionTicketRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureEmailCaptchaByActionTicketVerifyRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromVerifyEmailCaptchaByActionTicketRequest(Context ctx, StructureEmailCaptchaByActionTicketVerifyRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).verifyEmailCaptchaByActionTicketRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureMobileCaptchaRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromMobileCaptchaRequest(Context ctx, StructureMobileCaptchaRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).mobileCaptchaRequest(jsonData).build();
    }

    /**
     * Generates an authentication request from a {@link StructureBindMobileRequest} object.
     *
     * @param ctx The Javalin context.
     * @param jsonData The JSON data.
     * @return An authentication request.
     */
    static AuthenticationRequest fromBindMobileRequest(Context ctx, StructureBindMobileRequest jsonData) {
        return AuthenticationRequest.builder().context(ctx).bindMobileRequest(jsonData).build();
    }


    /**
     * This is the authenticator used for password authentication.
     *
     * @return An authenticator.
     */
    Authenticator<ResultLoginRequest> getPasswordAuthenticator();

    /**
     * This is the authenticator used for token authentication.
     *
     * @return An authenticator.
     */
    Authenticator<ResultLoginRequest> getTokenAuthenticator();

    /**
     * This is the authenticator used for session authentication.
     *
     * @return An authenticator.
     */
    Authenticator<ResultComboTokenRequest> getSessionKeyAuthenticator();


    /**
     * This is the authenticator used for handling register accounts.
     * @return An authenticator.
     */
    Authenticator<ResultRegisterAccountRequest> getRegisterAuthenticator();

    /**
     * This is the authenticator used for handling grant device requests.
     *
     * @return An authenticator.
     */
    DeviceVerifyAuthenticator getDeviceVerifyAuthenticator();

    /**
     * This is the authenticator used for handling restore account requests.
     *
     * @return An authenticator.
     */
    Authenticator<ResultReactivateAccountRequest> getReactivateAccountAuthenticator();

    /**
     * This is the authenticator used for handling guest account login requests.
     *
     * @return An authenticator.
     */
    Authenticator<ResultLoginGuestRequest> getLoginGuestAuthenticator();

    /**
     * This is the authenticator used for handling captcha requests.
     *
     * @return An authenticator.
     */
    CaptchaAuthenticator getCaptchaAuthenticator();

    /** A data container that holds relevant data for authenticating a client. */
    @Builder
    @AllArgsConstructor
    @Getter
    class AuthenticationRequest {
        @Nullable private final Context context;
        @Nullable private final StructureLoginAccountRequest passwordRequest;
        @Nullable private final StructureLoginTokenRequest tokenRequest;
        @Nullable private final StructureComboTokenRequest sessionKeyRequest;
        @Nullable private final StructureComboTokenRequest.LoginTokenData sessionKeyData;
        @Nullable private final StructureRegisterAccountRequest registerRequest;
        @Nullable private final StructureGrandByTicketRequest deviceVerifySendCodeRequest;
        @Nullable private final StructureGrantDeviceRequest deviceVerifyCodeRequest;
        @Nullable private final StructureReactivateAccountRequest reactivateAccountRequest;
        @Nullable private final StructureLoginGuestAccountRequest loginGuestRequest;
        @Nullable private final StructureVerifyEmailAddressCreateTicketRequest verifyEmailAddressRequest;
        @Nullable private final StructureAccountRiskCheckRequest accountRiskCheckRequest;
        @Nullable private final StructureEmailCaptchaByActionTicketRequest sendEmailCaptchaByActionTicketRequest;
        @Nullable private final StructureEmailCaptchaByActionTicketVerifyRequest verifyEmailCaptchaByActionTicketRequest;
        @Nullable private final StructureMobileCaptchaRequest mobileCaptchaRequest;
        @Nullable private final StructureBindMobileRequest bindMobileRequest;
    }
}
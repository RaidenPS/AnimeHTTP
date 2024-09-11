package raidenhttp.authorization;

// Imports
import raidenhttp.utils.results.Bind.ResultBindMobile;
import raidenhttp.utils.results.Captcha.*;
import raidenhttp.utils.results.ResultVerifyEmailAddressCreateTicketRequest;

public interface CaptchaAuthenticator {
    /**
     * Called when game sent request for create a ticket for given action.
     *
     * @param request The authentication request.
     */
    ResultVerifyEmailAddressCreateTicketRequest handleCreateTicket(AuthenticationSystem.AuthenticationRequest request);

    /**
     * Called when game sent request for send geetest captcha if needed.
     *
     * @param request The authentication request.
     */
    ResultAccountRiskCheck handleCheckAccountRisk(AuthenticationSystem.AuthenticationRequest request);

    /**
     * Called when game sent request for send email code by action ticket.
     *
     * @param request The authentication request.
     */
    ResultSendEmailCaptchaByActionTicket handleSendEmailCaptchaByActionTicket(AuthenticationSystem.AuthenticationRequest request);

    /**
     * Called when game sent request for verify a code sent by action ticket.
     *
     * @param request The authentication request.
     */
    ResultVerifyEmailCaptchaByActionTicket handleVerifyEmailCaptchaByActionTicket(AuthenticationSystem.AuthenticationRequest request);

    /**
     * Called when game sent request for handle the captcha when verify a mobile number.
     *
     * @param request The authentication request.
     */
    ResultMobileCaptcha handleMobileCaptcha(AuthenticationSystem.AuthenticationRequest request);

    /**
     * Called when game sent request for bind a mobile number.
     *
     * @param request The authentication request.
     */
    ResultBindMobile handleBindMobile(AuthenticationSystem.AuthenticationRequest request);
}

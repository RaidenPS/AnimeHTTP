package raidenhttp.authorization;

// Results
import raidenhttp.utils.results.ResultGrantDeviceByTicketRequest;
import raidenhttp.utils.results.ResultGrantDeviceRequest;

public interface DeviceVerifyAuthenticator {
    /**
     * Called when game sent request for grant device code.
     *
     * @param request The authentication request.
     */
    ResultGrantDeviceByTicketRequest handleSendCode(AuthenticationSystem.AuthenticationRequest request);

    /**
     * Called when server sent a grant device code to game for verification.
     *
     * @param request The authentication request.
     */
    ResultGrantDeviceRequest handleVerifyCode(AuthenticationSystem.AuthenticationRequest request);
}

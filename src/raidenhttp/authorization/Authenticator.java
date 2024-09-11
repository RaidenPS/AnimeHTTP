package raidenhttp.authorization;

// Results
import raidenhttp.utils.results.ResultLoginRequest;
import raidenhttp.utils.results.ResultComboTokenRequest;

/**
 * Handles username/password authentication from the client.
 *
 * @param <T> The response object type. Should be {@link ResultLoginRequest} or {@link
 *     ResultComboTokenRequest}
 */
public interface Authenticator<T> {

    /**
     * Attempt to authenticate the client with the provided credentials.
     *
     * @param request The authentication request wrapped in a {@link
     *     AuthenticationSystem.AuthenticationRequest} object.
     * @return The result of the login in an object.
     */
    T authenticate(AuthenticationSystem.AuthenticationRequest request);
}

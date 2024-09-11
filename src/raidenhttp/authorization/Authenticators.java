package raidenhttp.authorization;

// Imports
import raidenhttp.Main;
import raidenhttp.config.ConfigManager;
import raidenhttp.enums.CaptchaActionType;
import raidenhttp.cryptography.*;
import raidenhttp.database.DatabaseHelper;
import raidenhttp.database.entities.Account;
import java.util.Objects;

// Results
import raidenhttp.utils.classes.Json;
import raidenhttp.utils.results.*;
import raidenhttp.utils.results.Bind.ResultBindMobile;
import raidenhttp.utils.results.Captcha.*;

// Translation
import static raidenhttp.utils.Language.translate;

// Utils
import raidenhttp.utils.Utils;
import raidenhttp.utils.Retcodes;

public final class Authenticators {
    public static class PasswordAuthenticator implements Authenticator<ResultLoginRequest> {
        @Override
        public ResultLoginRequest authenticate(AuthenticationSystem.AuthenticationRequest request) {
            /// Asserts
            assert request.getPasswordRequest() != null;
            assert request.getContext() != null;

            var response = new ResultLoginRequest();
            String password = request.getPasswordRequest().password;
            String username = request.getPasswordRequest().account;
            boolean is_crypto = request.getPasswordRequest().is_crypto;

            if(!is_crypto) {
                password = Hashing.hashSha256(password);
            }
            else {
                try {
                    password = Hashing.hashSha256(Cryptography.DecryptPassword(password));
                } catch (Exception e) {
                    response.retcode = Retcodes.ACCOUNT_LOGIN_PASSWORD_DECRYPTION_FAILED_ERROR_VALUE;
                    response.message = translate("messages.http.login_account_failed1");
                    password = "";
                }
            }

            Account accountObject = (username.contains("@") ? DatabaseHelper.getAccountByEmail(username) : DatabaseHelper.getAccountByName(username));
            boolean canLogin;
            if(accountObject == null) {
                canLogin = false;
            }
            else {
                if(accountObject.isGuest()) canLogin = false;
                else if(!Objects.equals(password, "")) canLogin = Objects.equals(accountObject.getPassword(), password);
                else {
                    canLogin = Objects.equals(accountObject.getIpAddress(), request.getContext().ip());
                }
            }

            if(!canLogin){
                response.retcode = Retcodes.ACCOUNT_INVALID_FAILED_ERROR_VALUE;
                response.message = translate("messages.http.login_account_failed2");
                response.data = null;
            } else {
                if(Objects.equals(accountObject.getRiskActionType(), "login")) {
                    accountObject.setRiskActionType("sessionlogin");
                    accountObject.save();

                    response.retcode = Retcodes.RETCODE_SUCCESS;
                    response.message = "OK";
                    response.data.account.uid = accountObject.getId();
                    response.data.account.name = "";
                    response.data.account.email = Utils.getMaskedEmailAddress(accountObject.getEmail());
                    response.data.account.mobile = accountObject.getPhoneNumber();
                    response.data.account.is_email_verify = "1";
                    response.data.account.realname = accountObject.getRealName();
                    response.data.account.identity_card = accountObject.getIdentityCard();
                    response.data.account.token = accountObject.generateSessionKey();
                    response.data.account.facebook_name = accountObject.getFacebookName();
                    response.data.account.google_name = accountObject.getGoogleName();
                    response.data.account.twitter_name = accountObject.getTwitterName();
                    response.data.account.game_center_name = accountObject.getGameCenterName();
                    response.data.account.apple_name = accountObject.getAppleName();
                    response.data.account.sony_name = accountObject.getSonyName();
                    response.data.account.tap_name = accountObject.getTapName();
                    response.data.account.country = accountObject.getCountry();
                    response.data.account.reactivate_ticket = accountObject.getReactivateTicket();
                    response.data.account.area_code = "**";
                    response.data.account.device_grant_ticket = accountObject.getDeviceTicket();
                    response.data.account.steam_name = accountObject.getSteamName();
                    response.data.account.unmasked_email = accountObject.getUnmaskedEmail();
                    response.data.account.unmasked_email_type = accountObject.getUnmaskedEmailType();
                    response.data.account.safe_mobile = accountObject.getSafeMobile();
                    response.data.account.cx_name = accountObject.getCxName();
                    if(!Objects.equals(password, "") && !Objects.equals(accountObject.getIpAddress(), request.getContext().ip())) {
                        response.data.account.device_grant_ticket = Cryptography.generateActionToken(15);
                        response.data.device_grant_required = true;
                    }
                    else {
                        response.data.device_grant_required = accountObject.isDeviceVerify();
                    }
                    response.data.realperson_required = accountObject.isPersonVerify();
                    response.data.safe_moblie_required = accountObject.isPhoneVerify();
                    response.data.reactivate_required = accountObject.isNeedReactivation();
                    response.data.realname_operation = accountObject.getRealnameOperation(); // BIND_NAME
                }
                else {
                    accountObject.setRiskActionType("");
                    accountObject.save();

                    response.retcode = Retcodes.THE_REQUEST_FAILED_NETWORK_AT_RISK_ERROR_VALUE;
                    response.message = translate("messages.http.risk_network");
                    response.data = null;
                }
            }
            return response;
        }
    }

    public static class SessionKeyAuthenticator implements Authenticator<ResultComboTokenRequest> {
        @Override
        public ResultComboTokenRequest authenticate(AuthenticationSystem.AuthenticationRequest request) {
            /// Asserts
            assert request.getContext() != null;

            var response = new ResultComboTokenRequest();
            var requestData = request.getSessionKeyRequest();
            var loginData = request.getSessionKeyData();
            if(requestData == null || loginData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.session_key_failed");
                response.data = null;
                return response;
            }

            Account account;
            if(loginData.guest) {
                account = DatabaseHelper.getAccountByGuest(requestData.device);
                if(account != null) {
                    loginData.token = account.generateSessionKey();
                }
            }
            else {
                account = DatabaseHelper.getAccountById(loginData.uid);
            }

            boolean successfulLogin = (account != null && account.getSessionKey().equals(loginData.token));
            if(successfulLogin) {
                if(Objects.equals(account.getRiskActionType(), "sessionlogin")) {
                    account.setRiskActionType("");
                    account.setGameBiz(Utils.getGameBiz());
                    account.save();
                    if(!Objects.equals(account.getDeviceId(), requestData.device)) {
                        account.setDeviceId(requestData.device);
                    }

                    response.retcode = Retcodes.RETCODE_SUCCESS;
                    response.message = "OK";
                    response.data.open_id = account.getId();
                    response.data.combo_id = "0";
                    response.data.combo_token = account.generateLoginToken();
                    response.data.account_type = account.getAccountType();
                    response.data.heartbeat = false;
                    response.data.fatigue_remind = null;

                    ResultComboTokenRequest.LoginDataInfo dataInfo = new ResultComboTokenRequest.LoginDataInfo();
                    dataInfo.guest = account.isGuest();

                    response.data.data = Json.encode(dataInfo);
                }
                else {
                    account.setRiskActionType("");
                    account.save();

                    response.retcode = Retcodes.THE_REQUEST_FAILED_NETWORK_AT_RISK_ERROR_VALUE;
                    response.message = translate("messages.http.risk_network");
                    response.data = null;
                }
            }
            else {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.session_key_failed");
                response.data = null;
            }
            return response;
        }
    }

    public static class TokenAuthenticator implements Authenticator<ResultLoginRequest> {
        @Override
        public ResultLoginRequest authenticate(AuthenticationSystem.AuthenticationRequest request) {
            var response = new ResultLoginRequest();
            var requestData = request.getTokenRequest();
            if(requestData == null) {
                response.retcode = Retcodes.ACCOUNT_TOKEN_FAILED_ERROR_VALUE;
                response.message = translate("messages.http.access_token_failed");
                response.data = null;
                return response;
            }

            Account account = DatabaseHelper.getAccountById(requestData.uid);
            boolean successfulLogin = (account != null && account.getSessionKey().equals(requestData.token));
            if(successfulLogin) {
                account.setRiskActionType("sessionlogin");
                account.save();

                response.retcode = Retcodes.RETCODE_SUCCESS;
                response.message = "OK";
                response.data.account.uid = requestData.uid;
                response.data.account.name = "";
                response.data.account.email = Utils.getMaskedEmailAddress(account.getEmail());
                response.data.account.mobile = account.getPhoneNumber();
                response.data.account.is_email_verify = "1";
                response.data.account.realname = account.getRealName();
                response.data.account.identity_card = account.getIdentityCard();
                response.data.account.token = account.getSessionKey();
                response.data.account.facebook_name = account.getFacebookName();
                response.data.account.google_name = account.getGoogleName();
                response.data.account.twitter_name = account.getTwitterName();
                response.data.account.game_center_name = account.getGameCenterName();
                response.data.account.apple_name = account.getAppleName();
                response.data.account.sony_name = account.getSonyName();
                response.data.account.tap_name = account.getTapName();
                response.data.account.country = account.getCountry();
                response.data.account.reactivate_ticket = account.getReactivateTicket();
                response.data.account.area_code = "**";
                response.data.account.device_grant_ticket = account.getDeviceTicket();
                response.data.account.steam_name = account.getSteamName();
                response.data.account.unmasked_email = account.getUnmaskedEmail();
                response.data.account.unmasked_email_type = account.getUnmaskedEmailType();
                response.data.account.cx_name = account.getCxName();
                response.data.account.safe_mobile = account.getSafeMobile();
                response.data.device_grant_required = account.isDeviceVerify();
                response.data.realperson_required = account.isPersonVerify();
                response.data.safe_moblie_required = account.isPhoneVerify();
                response.data.realname_operation = account.getRealnameOperation();
            }
            else {
                response.retcode = Retcodes.ACCOUNT_TOKEN_FAILED_ERROR_VALUE;
                response.message = translate("messages.http.access_token_failed");
                response.data = null;
            }
            return response;
        }
    }

    public static class RegisterAuthenticator implements Authenticator<ResultRegisterAccountRequest> {
        public ResultRegisterAccountRequest authenticate(AuthenticationSystem.AuthenticationRequest request) {
            var response = new ResultRegisterAccountRequest();
            var requestData = request.getRegisterRequest();

            if(requestData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.parameter_error");
            }
            else if(!Objects.equals(requestData.password, requestData.confirmpassword)) {
                response.retcode = Retcodes.RETCODE_FAILED;
                response.message = translate("messages.http.register_account_failed1");
            }
            else if(DatabaseHelper.getAccountByName(requestData.username) != null) {
                response.retcode = Retcodes.RETCODE_FAILED;
                response.message = translate("messages.http.register_account_failed2");
            }
            else {
                try {
                    requestData.password = Hashing.hashSha256(Cryptography.EncryptPassword(requestData.password));
                    DatabaseHelper.createAccount(requestData);
                    response.retcode = Retcodes.RETCODE_SUCCESS;
                    response.message = "OK";
                }catch (Exception e) {
                    response.retcode = Retcodes.RETCODE_FAILED;
                    response.message = translate("messages.http.register_account_failed3");
                }
            }
            return response;
        }
    }

    public static class DeviceVerifyAuthentication implements DeviceVerifyAuthenticator {
        @Override
        public ResultGrantDeviceByTicketRequest handleSendCode(AuthenticationSystem.AuthenticationRequest request) {
            var response = new ResultGrantDeviceByTicketRequest();
            var requestData = request.getDeviceVerifySendCodeRequest();
            if(requestData == null) {
                response.retcode = Retcodes.GRANT_DEVICE_SYSTEM_ERROR_VALUE;
                response.message = translate("messages.http.system_error");
                response.data = null;
                return response;
            }

            Account user = DatabaseHelper.getAccountByTicket(0, requestData.action_ticket);
            if(user == null) {
                response.retcode = Retcodes.GRANT_DEVICE_SYSTEM_ERROR_VALUE;
                response.message = translate("messages.http.system_error");
                response.data = null;
                return response;
            }
            String verify_code = Cryptography.generateCode(6);
            Main.getLogger().info(String.format("The user %s requested grant device. The code is %s and ticket_id is %s", user.getUsername(), verify_code, requestData.action_ticket));
            user.setDeviceVerifyCode(verify_code);
            user.save();
            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.success = true;
            response.message = "OK";
            response.data.ticket = user.getDeviceTicket();
            return response;
        }

        @Override
        public ResultGrantDeviceRequest handleVerifyCode(AuthenticationSystem.AuthenticationRequest request) {
            var response = new ResultGrantDeviceRequest();
            var requestData = request.getDeviceVerifyCodeRequest();
            if(requestData == null) {
                response.retcode = Retcodes.SYSTEM_ERROR_VALUE;
                response.message = translate("messages.http.system_error");
                response.data = null;
                return response;
            }

            Account user = DatabaseHelper.getAccountByTicket(0, requestData.ticket);
            if(user == null) {
                response.retcode = Retcodes.SYSTEM_ERROR_VALUE;
                response.message = translate("messages.http.system_error");
                response.data = null;
                return response;
            }

            if(!Objects.equals(requestData.code, user.getDeviceVerifyCode())) {
                response.retcode = Retcodes.GRANT_DEVICE_INVALID_VERIFICATION_CODE_ERROR_VALUE;
                response.message = translate("messages.http.device_check_invalid_code");
                response.data = null;
                return response;
            }

            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.message = "OK";
            response.data.game_token = user.getSessionKey();
            response.data.login_ticket = user.getDeviceTicket();

            user.setDeviceVerifyCode("");
            user.setDeviceVerify(false);
            user.save();
            return response;
        }
    }

    public static class ReactivateAccountAuthenticator implements Authenticator<ResultReactivateAccountRequest> {
        @Override
        public ResultReactivateAccountRequest authenticate(AuthenticationSystem.AuthenticationRequest request) {
            var response = new ResultReactivateAccountRequest();
            var requestData = request.getReactivateAccountRequest();
            if(requestData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.parameter_error");
                response.data = null;
                return response;
            }

            Account user = DatabaseHelper.getAccountByTicket(1, requestData.action_ticket);
            if(user == null) {
                response.retcode = Retcodes.REACTIVATE_ACCOUNT_FAILED_ERROR_VALUE;
                response.message = translate("messages.http.reactivate_account_verify_fail");
                response.data = null;
                return response;
            }

            user.setReactivateAccount(false);
            user.save();

            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.message = "OK";
            response.data.account.uid = user.getId();
            response.data.account.name = "";
            response.data.account.email = Utils.getMaskedEmailAddress(user.getEmail());
            response.data.account.mobile = user.getPhoneNumber();
            response.data.account.is_email_verify = "1";
            response.data.account.realname = user.getRealName();
            response.data.account.identity_card = user.getIdentityCard();
            response.data.account.token = user.generateSessionKey();
            response.data.account.facebook_name = user.getFacebookName();
            response.data.account.google_name = user.getGoogleName();
            response.data.account.twitter_name = user.getTwitterName();
            response.data.account.game_center_name = user.getGameCenterName();
            response.data.account.apple_name = user.getAppleName();
            response.data.account.sony_name = user.getSonyName();
            response.data.account.tap_name = user.getTapName();
            response.data.account.country = user.getCountry();
            response.data.account.reactivate_ticket = user.getReactivateTicket();
            response.data.account.area_code = "**";
            response.data.account.device_grant_ticket = user.getDeviceTicket();
            response.data.account.steam_name = user.getSteamName();
            response.data.account.unmasked_email = user.getUnmaskedEmail();
            response.data.account.unmasked_email_type = user.getUnmaskedEmailType();
            response.data.account.cx_name = user.getCxName();
            response.data.account.safe_mobile = user.getSafeMobile();
            response.data.device_grant_required = user.isDeviceVerify();
            response.data.safe_moblie_required = user.isPhoneVerify();
            response.data.realname_operation = user.getRealnameOperation();
            return response;
        }
    }

    public static class LoginGuestAuthenticator implements Authenticator<ResultLoginGuestRequest> {
        public ResultLoginGuestRequest authenticate(AuthenticationSystem.AuthenticationRequest request) {
            var response = new ResultLoginGuestRequest();
            var requestData = request.getLoginGuestRequest();

            if(!ConfigManager.httpConfig.enable_guests) {
                response.retcode = Retcodes.RETCODE_CANCEL;
                response.message = translate("messages.http.guests_are_not_allowed");
                response.data = null;
                return response;
            }

            if(requestData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.system_error");
                response.data = null;
                return response;
            }

            if(!Utils.isGameVersionCorrect(requestData.g_version)) {
                response.retcode = Retcodes.GRANT_DEVICE_PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.invalid_game_name");
                response.data = null;
                return response;
            }

            Account user = DatabaseHelper.getAccountByGuest(requestData.device);
            if(user == null) {
                assert request.getContext() != null;
                user = DatabaseHelper.createGuestAccount(requestData.device, requestData.client, request.getContext().ip());
                if(user == null) {
                    response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                    response.message = translate("messages.http.system_error");
                    response.data = null;
                    return response;
                }
            }
            user.setRiskActionType("sessionlogin");
            user.save();

            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.message = "OK";
            response.data.account_type = user.getAccountType();
            response.data.guest_id = user.getId();
            response.data.newly = true;
            return response;
        }
    }

    public static class CaptchaAuthentication implements CaptchaAuthenticator {
        @Override
        public ResultVerifyEmailAddressCreateTicketRequest handleCreateTicket(AuthenticationSystem.AuthenticationRequest request) {
            var response = new ResultVerifyEmailAddressCreateTicketRequest();
            var requestData = request.getVerifyEmailAddressRequest();
            if(requestData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.parameter_error");
                response.data = null;
                return response;
            }

            Account user = DatabaseHelper.getAccountById(requestData.account_id);
            if(user == null) {
                response.retcode = Retcodes.SYSTEM_ERROR_VALUE;
                response.message = translate("messages.http.system_error");
                response.data = null;
                return response;
            }
            String ticket = Cryptography.generateActionToken(15);
            user.setActionTicket(ticket);
            user.save();
            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.message = "OK";
            response.data.ticket = ticket;
            return response;
        }

        @Override
        public ResultAccountRiskCheck handleCheckAccountRisk(AuthenticationSystem.AuthenticationRequest request) {
            var requestData = request.getAccountRiskCheckRequest();
            var response = new ResultAccountRiskCheck();
            if(requestData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.parameter_error");
                response.data = null;
                return response;
            }

            if(requestData.username != null || requestData.email != null) {
                Account user = (requestData.username != null) ? DatabaseHelper.getAccountByName(requestData.username) : DatabaseHelper.getAccountByMaskedEmail(requestData.email);
                if(user != null) {
                    user.setRiskActionType(requestData.action_type);
                    user.save();
                }
                else if(requestData.username.contains("@")) { // email
                    user = DatabaseHelper.getAccountByEmail(requestData.username);
                    if(user != null) {
                        user.setRiskActionType(requestData.action_type);
                        user.save();
                    }
                }
            }

            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.message = "OK";
            response.data.id = "";
            response.data.action = CaptchaActionType.ACTION_NONE.toString();
            response.data.geetest = null;
            return response;
        }

        @Override
        public ResultSendEmailCaptchaByActionTicket handleSendEmailCaptchaByActionTicket(AuthenticationSystem.AuthenticationRequest request) {
            var requestData = request.getSendEmailCaptchaByActionTicketRequest();
            var response = new ResultSendEmailCaptchaByActionTicket();
            if(requestData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.parameter_error");
                return response;
            }

            Account user = DatabaseHelper.getAccountByTicket(2, requestData.action_ticket);
            if(user == null || !Objects.equals(user.getRiskActionType(), "bind_mobile")) {
                response.retcode = Retcodes.THE_REQUEST_FAILED_NETWORK_AT_RISK_ERROR_VALUE;
                response.message = translate("messages.http.risk_network");
                return response;
            }

            String captcha = Cryptography.generateCode(6);
            Main.getLogger().info(String.format("The username %s generated a new email captcha (%s)", user.getUsername(), captcha));
            user.setEmailCaptcha(captcha);
            user.save();

            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.message = "OK";
            return response;
        }

        @Override
        public ResultVerifyEmailCaptchaByActionTicket handleVerifyEmailCaptchaByActionTicket(AuthenticationSystem.AuthenticationRequest request) {
            var requestData = request.getVerifyEmailCaptchaByActionTicketRequest();
            var response = new ResultVerifyEmailCaptchaByActionTicket();
            if(requestData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.parameter_error");
                return response;
            }

            Account user = DatabaseHelper.getAccountByTicket(2, requestData.action_ticket);
            if(user == null) {
                response.retcode = Retcodes.THE_REQUEST_FAILED_NETWORK_AT_RISK_ERROR_VALUE;
                response.message = translate("messages.http.risk_network");
                return response;
            }

            if(!Objects.equals(user.getEmailCaptcha(), requestData.captcha)) {
                response.retcode = Retcodes.RETCODE_FAILED;
                response.message = translate("messages.http.invalid_captcha");
                return response;
            }

            user.setEmailCaptcha("");
            user.save();

            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.message = "OK";
            return response;
        }

        @Override
        public ResultMobileCaptcha handleMobileCaptcha(AuthenticationSystem.AuthenticationRequest request) {
            var requestData = request.getMobileCaptchaRequest();
            var response = new ResultMobileCaptcha();
            if(requestData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.parameter_error");
                return response;
            }

            Account user = DatabaseHelper.getAccountByTicket(2, requestData.action_ticket);
            if(user == null || !Objects.equals(user.getRiskActionType(), "bind_mobile")) {
                response.retcode = Retcodes.THE_REQUEST_FAILED_NETWORK_AT_RISK_ERROR_VALUE;
                response.message = translate("messages.http.risk_network");
                return response;
            }

            String captcha = Cryptography.generateCode(6);
            Main.getLogger().info(String.format("The username %s generated a new mobile captcha (%s)", user.getUsername(), captcha));
            user.setMobileCaptcha(captcha);
            user.save();

            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.message = "OK";
            return response;
        }

        @Override
        public ResultBindMobile handleBindMobile(AuthenticationSystem.AuthenticationRequest request) {
            var requestData = request.getBindMobileRequest();
            var response = new ResultBindMobile();
            if(requestData == null) {
                response.retcode = Retcodes.PARAMETER_ERROR_VALUE;
                response.message = translate("messages.http.parameter_error");
                return response;
            }

            Account user = DatabaseHelper.getAccountByTicket(2, requestData.ticket);
            if(user == null || !Objects.equals(user.getRiskActionType(), "bind_mobile")) {
                response.retcode = Retcodes.THE_REQUEST_FAILED_NETWORK_AT_RISK_ERROR_VALUE;
                response.message = translate("messages.http.risk_network");
                return response;
            }

            if(!Objects.equals(user.getMobileCaptcha(), requestData.captcha)) {
                response.retcode = Retcodes.RETCODE_FAILED;
                response.message = translate("messages.http.invalid_captcha");
                return response;
            }

            user.setMobileCaptcha("");
            user.setActionTicket("");
            user.setSafeMobile(requestData.mobile);
            user.setPhoneVerify(false);
            user.setRiskActionType("sessionlogin");
            user.save();

            response.retcode = Retcodes.RETCODE_SUCCESS;
            response.message = "OK";
            return response;
        }
    }
}
# FAQ:
***
1. **What is this?**
> This is a reimplementation of anime game's http server.
***
2. **What operation system should I use?**
> Windows 10 as mine. (Microsoft Windows Version 22h2 19045.4529). Probably is going to work on Linux since WINAPI is not involved.
***
3. **What is supported?**
+ Login
+ Register
+ ~~Forgotten Password~~
+ Device check
+ Safemobile check
+ Game Logging (SDK, APM, Perf, Sophon, BatchLog, CrashLog, CrashDump, AndroidSDK)
+ Device Fingerprint
+ Firebase blacklist
+ Change between chinese and overseas versions. (Only on PC).
+ Launcher miscellaneous
+ Custom login and launcher experiments (**game_experiments.json** and **launcher_sophon_experiments.json**)
+ Regions (Maintenance and force update information).
+ Possibly support for all hoyoverse games like ZZZ and Honkai Impact. (They uses similar requests and responses.)
+ IP Whitelist/Blacklist
+ Error message translations
+ Captcha
***
4. **What is not supported?**
+ Login with mobile number in chinese versions.
+ Send Email & SMS for Device/safephone checks.
+ Thirdparty login
+ QR Scan in chinese version.
***
5. **Unimplemented requests (For now):**
- https://api-account-os.hoyoverse.com/account/auth/api/getCookieAccountInfoByGameToken (POST)
- https://api-account-os.hoyoverse.com/account/binding/api/getRegionByISO (GET)
- https://api-account-os.hoyoverse.com/account/device/api/listNewerDevices (GET)
- https://sg-public-api.hoyoverse.com/common/csc/client/addCliTempInfo (POST)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/guest/guest/bind (GET)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/shield/api/bindEmail (GET)
- https://gameapi-account.mihoyo.com/account/auth/api/bindRealname (POST)
- https://gameapi-account.mihoyo.com/account/idverifier/idverifier/bindRealperson (GET)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/shield/api/bindThirdparty (GET)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/shield/api/checkAccount (GET)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/granter/login/webLogin (POST)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/confirm (GET)
- https://gameapi-account.mihoyo.com/account/idverifier/idverifier/confirmRealperson (GET)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/shield/api/createMmt (GET)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/guest/guest/delete (GET/DELETE)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/shield/api/loginByAuthTicket (GET)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/shield/api/loginByThirdparty (GET)
- https://gameapi-account.mihoyo.com/account/auth/api/modifyRealname (GET)
- https://gameapi-account.mihoyo.com/account/device/api/ackNewerDevices (POST)
- https://passport-api.mihoyo.com/account/ma-cn-passport/passport/checkReactivateByActionTicket (GET)
- https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/createThirdpartyBindMobileActionTicket (POST)
- https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/createThirdpartyBindMobileCaptcha (POST)
- https://passport-api.mihoyo.com/account/ma-cn-verifier/verifier/getActionTicketInfo (GET)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/postman/device/setAlias
- https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/postman/device/delAlias
- https://hk4e-sdk.mihoyo.com/hk4e_cn/combo/panda/qrcode/scan (GET)
- https://sdk-static.mihoyo.com/combo/box/api/config/sw/precache (GET)
- https://gameapi-account.mihoyo.com/account/auth/api/getCookieAccountInfoByGameToken (GET)
- https://hk4e-sdk.mihoyo.com/hk4e_cn/mdk/shield/api/unBindThirdparty (GET)
- https://gameapi-account.mihoyo.com/account/idverifier/idverifier/verifyRealperson (GET)
***

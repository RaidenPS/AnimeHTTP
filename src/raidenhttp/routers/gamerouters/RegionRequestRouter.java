package raidenhttp.routers.gamerouters;

// Imports
import raidenhttp.Main;
import raidenhttp.config.ConfigManager;
import raidenhttp.enums.CoverSwitchType;
import raidenhttp.enums.ServerType;
import raidenhttp.cryptography.*;
import raidenhttp.routers.Router;
import com.google.gson.*;
import com.google.protobuf.ByteString;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

// Events
import raidenhttp.routers.events.region.*;

// Results
import raidenhttp.utils.structures.StructureRegionDataRequest;

// Protocol buffers
import raidenhttp.cache.protos.ForceUpdateInfoOuterClass.ForceUpdateInfo;
import raidenhttp.cache.protos.QueryCurrRegionHttpRspOuterClass.QueryCurrRegionHttpRsp;
import raidenhttp.cache.protos.QueryRegionListHttpRspOuterClass.QueryRegionListHttpRsp;
import raidenhttp.cache.protos.RegionInfoOuterClass.RegionInfo;
import raidenhttp.cache.protos.RegionSimpleInfoOuterClass.RegionSimpleInfo;
import raidenhttp.cache.protos.StopServerInfoOuterClass.StopServerInfo;

// Utils
import raidenhttp.utils.Utils;
import raidenhttp.utils.GameRetcodes;
import raidenhttp.utils.Retcodes;
import raidenhttp.utils.classes.Json;

public class RegionRequestRouter implements Router {
    private static final String dispatchDomain = Utils.getDispatchDomain();
    private static final Map<String, StructureRegionDataRequest> regions = new ConcurrentHashMap<>();
    private static String regionListResponse;

    /**
     * Region config for query_cur_region
     */
    private static ByteString getCurrentRegionCustomConfig() {
        JsonObject customConfig = new JsonObject();
        JsonArray engineCodeSwitch = new JsonArray();
        JsonArray coverSwitch = new JsonArray();
        JsonArray codeSwitch = new JsonArray();
        JsonArray il2cppCodeSwitch = new JsonArray();
        JsonObject mtrConfig = new JsonObject();
        JsonObject urlCheckConfig = new JsonObject();
        JsonObject reportNetDelayConfig = new JsonObject();

        coverSwitch.add(CoverSwitchType.FEEDBACK.getValue());
        coverSwitch.add(CoverSwitchType.SURVEY.getValue());
        coverSwitch.add(CoverSwitchType.USER_CENTER.getValue());
        //codeSwitch.add(4334);
        il2cppCodeSwitch.add(2);
        il2cppCodeSwitch.add(8);

        customConfig.add("codeSwitch", codeSwitch);
        customConfig.add("coverSwitch", coverSwitch);
        customConfig.add("engineCodeSwitch", engineCodeSwitch);
        customConfig.add("il2cppCodeSwitch", il2cppCodeSwitch);
        customConfig.addProperty("perf_report_enable", "true");
        customConfig.addProperty("perf_report_servertype", String.valueOf(ServerType.ASIA.getValue()));
        //customConfig.addProperty("perf_report_percent", "100");
        customConfig.addProperty("perf_report_config_url", "https://ys-log-upload-os.hoyoverse.com/perf/config/verify");
        customConfig.addProperty("perf_report_record_url", "https://ys-log-upload-os.hoyoverse.com/perf/dataUpload");
        customConfig.addProperty("perf_report_account_blacklist", "");
        customConfig.addProperty("perf_report_account_whitelist", "");
        customConfig.addProperty("perf_report_platform_blacklist", "");
        customConfig.addProperty("perf_report_platform_whitelist", "");
        customConfig.addProperty("post_client_data_url", "https://sg-public-api.hoyoverse.com/common/csc/client/addCliTempInfo?sign_type=2&auth_appid=csc&authkey_ver=1");
        customConfig.addProperty("homeItemFilter", "20");
        customConfig.addProperty("homeDotPattern", "true");
        customConfig.addProperty("photographShareTopics", "753");
        customConfig.addProperty("photographSharePlatform", "13");
        customConfig.addProperty("gachaShareTopics", "753");
        customConfig.addProperty("gachaSharePlatform", "13");
        customConfig.addProperty("douyinShareTopics", "753");
        customConfig.addProperty("bilibiliShareTopics", "753");

        mtrConfig.addProperty("isOpen", "true");
        mtrConfig.addProperty("maxTTL", "5");
        mtrConfig.addProperty("timeOut", "10");
        mtrConfig.addProperty("traceCount", "5");
        mtrConfig.addProperty("abortTimeOutCount", "0");
        mtrConfig.addProperty("autoTraceInterval", "0");
        customConfig.add("mtrConfig", mtrConfig);

        urlCheckConfig.addProperty("isOpen", "true");
        customConfig.add("urlCheckConfig", urlCheckConfig);

        reportNetDelayConfig.addProperty("openGateserver", "true");
        customConfig.add("reportNetDelayConfig", reportNetDelayConfig);

        var encryptedConfig = Json.encode(customConfig).getBytes();
        Hashing.xorEncrypt(encryptedConfig, Cryptography.DISPATCH_KEY);
        return ByteString.copyFrom(encryptedConfig);
    }

    /**
     * Region config for query_region_list
     */
    private static ByteString getAllRegionsCustomConfig() {
        var customConfig = new JsonObject();

        customConfig.addProperty("sdkenv", ConfigManager.httpConfig.envType.getValue());
        customConfig.addProperty("loadPatch", "true");
        customConfig.addProperty("checkdevice", "false");
        customConfig.addProperty("devicelist", "");
        customConfig.addProperty("showexception",  "true");
        customConfig.addProperty("regionConfig", "pm|fk|add");
        customConfig.addProperty("downloadMode", "0");
        customConfig.addProperty("regionDispatchType", "0");
        customConfig.addProperty("videoKey", "5578228838233776"); /// wtf mihoyo

        customConfig.addProperty("downloadThreadNum", "20"); /// NEED TEST
        customConfig.addProperty("downloadIgnore403", "false"); /// NEED TEST
        customConfig.addProperty("downloadNoAudioDiff", "false"); /// NEED TEST
        customConfig.addProperty("downloadExcludeUselessRes", "false"); /// NEED TEST
        customConfig.addProperty("downloadEnableXXhash", "true"); /// NEED TEST
        customConfig.addProperty("downloadEnableUltraVerify", "true"); /// NEED TEST
        customConfig.addProperty("downloadVerifyRetryNum", "3"); /// NEED TEST

        var encryptedConfig = Json.encode(customConfig).getBytes();
        Hashing.xorEncrypt(encryptedConfig, Cryptography.DISPATCH_KEY);
        return ByteString.copyFrom(encryptedConfig);
    }

    public RegionRequestRouter() {
        try {
            var servers = new ArrayList<RegionSimpleInfo>();
            var configuredRegions = new ArrayList<>(ConfigManager.httpConfig.regions);
            if (configuredRegions.isEmpty()) {
                Main.getLogger().warn("No Configured Game Servers found.");
                System.exit(1);
            }

            configuredRegions.forEach(region -> {
                var identifier = RegionSimpleInfo.newBuilder().setName(region.Name).setTitle(region.Title).setType(region.Type).setDispatchUrl(dispatchDomain + "/query_cur_region/" + region.Name).build();
                servers.add(identifier);

                var regionInfo =
                        RegionInfo.newBuilder()
                                .setUseGateserverDomainName(false)
                                .setGateserverIpv6Ip("::1")
                                .setGateserverIp(region.Ip)
                                .setGateserverPort(region.Port)
                                .setGameBiz(Utils.getGameBiz())
                                //.setResourceUrl("127.0.0.1")
                                //.setClientDataVersion(69420)
                                //.setClientSilenceDataVersion(69421)
                                .build();

                var updatedQuery =
                        QueryCurrRegionHttpRsp.newBuilder()
                                .setRetcode(GameRetcodes.RETCODE_SUCCESS)
                                .setRegionInfo(regionInfo)
                                .setClientSecretKey(ByteString.copyFrom(Cryptography.DISPATCH_SEED))
                                .setRegionCustomConfigEncrypted(RegionRequestRouter.getCurrentRegionCustomConfig())
                                .setClientRegionCustomConfigEncrypted(RegionRequestRouter.getAllRegionsCustomConfig())
                                .setConnectGateTicket(ConfigManager.httpConfig.gameInfo.gateTicket)
                                .build();
                regions.put(region.Name, new StructureRegionDataRequest(updatedQuery, Hashing.base64Encode(updatedQuery.toByteString().toByteArray())));
            });

            // Create an updated region list.
            var updatedRegionList =
                    QueryRegionListHttpRsp.newBuilder()
                            .setRetcode(GameRetcodes.RETCODE_SUCCESS)
                            .addAllRegionList(servers)
                            .setClientSecretKey(ByteString.copyFrom(Cryptography.DISPATCH_SEED))
                            .setClientCustomConfigEncrypted(RegionRequestRouter.getAllRegionsCustomConfig())
                            .setEnableLoginPc(true)
                            .build();
            regionListResponse = Hashing.base64Encode(updatedRegionList.toByteString().toByteArray());
        } catch (Exception ex) {
            Main.getLogger().error("Unable to init the region. ", ex);
            System.exit(1);
        }
    }
    /**
     * version -> Game version
     * <br>channel_id -> Game channel ID
     * <br>platform -> Client's platform name (like PC, IOS)
     */
    private static void queryRegionList(Context ctx) {
        if (ctx.queryParamMap().containsKey("version") && ctx.queryParamMap().containsKey("channel_id")) {
            String versionName = ctx.queryParam("version");
            assert versionName != null;

            // Determine the region list to use based on the version and platform.
            if(Utils.isGameVersionCorrect(versionName)) {
                QueryAllRegionsEvent event = new QueryAllRegionsEvent(regionListResponse);
                event.call();
                ctx.result(event.getRegionList());
                return;
            }
        }
        QueryAllRegionsEvent event = new QueryAllRegionsEvent("CAESGE5vdCBGb3VuZCB2ZXJzaW9uIGNvbmZpZw=="); // CP///////////wE=
        event.call();
        ctx.result(event.getRegionList());
    }

    /**
     * version -> Game Version
     * <br>key_id -> Dispatch encryption key
     * <br>egionName -> Game region name.
     * <br>dispatchSeed -> Dispatch encryption seed.
     */
    private static void queryCurrentRegion(Context ctx) {
        String versionName = ctx.queryParam("version");
        String key_id = ctx.queryParam("key_id");
        String regionName = ctx.pathParam("region");
        String dispatchSeed = ctx.queryParam("dispatchSeed");
        String platformName = ctx.queryParam("platform");
        String regionData = "CAESGE5vdCBGb3VuZCB2ZXJzaW9uIGNvbmZpZw==";
        var region = regions.get(regionName);
        if(versionName == null || key_id == null || dispatchSeed == null || platformName == null || region == null) {
            ctx.result(regionData);
            return;
        }

        regionData = region.getBase64();
        var clientVersion = versionName.replaceAll(Pattern.compile("[a-zA-Z]").pattern(), "");
        var versionCode = clientVersion.split("\\.");
        var versionMajor = Integer.parseInt(versionCode[0]);
        var versionMinor = Integer.parseInt(versionCode[1]);
        var versionFix = Integer.parseInt(versionCode[2]);
        String[] gameVersion = ConfigManager.httpConfig.gameInfo.gameVersion.split("\\.");

        /// 37[>50]
        if (versionMajor >= 3 || (versionMajor == 2 && versionMinor == 7 && versionFix >= 50) || (versionMajor == 2 && versionMinor == 8)) {
            try {
                QueryCurrentRegionEvent event = new QueryCurrentRegionEvent(regionData);
                event.call();

                if(ConfigManager.httpConfig.isMaintenance) {
                    QueryCurrRegionHttpRsp rsp =
                            QueryCurrRegionHttpRsp.newBuilder()
                                .setRetcode(GameRetcodes.RETCODE_SERVER_UNDER_MAINTENANCE)
                                .setMsg("Under Maintenance")
                                .setRegionInfo(RegionInfo.newBuilder().build())
                                .setStopServer(
                                        StopServerInfo.newBuilder()
                                                .setUrl(ConfigManager.httpConfig.maintenanceUrl)
                                                .setStopBeginTime(ConfigManager.httpConfig.maintenanceStartDate)
                                                .setStopEndTime(ConfigManager.httpConfig.maintenanceEndDate)
                                                .setContentMsg(ConfigManager.httpConfig.maintenanceMsg)
                                                .build())
                                .buildPartial();
                    ctx.json(Cryptography.encryptAndSignRegionData(rsp.toByteArray(), key_id));
                    return;
                }

                if(versionMajor != Integer.parseInt(gameVersion[0]) || versionMinor != Integer.parseInt(gameVersion[1])) {
                    QueryCurrRegionHttpRsp rsp =
                            QueryCurrRegionHttpRsp.newBuilder()
                                .setRetcode(GameRetcodes.RETCODE_CLIENT_VERSION_MISMATCH)
                                .setMsg(String.format("Version update found. Please start the launcher to download the latest version.\n\nServer Version: %s\nClient Version: %s", ConfigManager.httpConfig.gameInfo.gameVersion, clientVersion))
                                .setRegionInfo(region.getRegionQuery().getRegionInfo())
                                .setForceUpdate(ForceUpdateInfo.newBuilder().setForceUpdateUrl(ConfigManager.httpConfig.forceUpdateUrl).build())
                                .buildPartial();
                    ctx.json(Cryptography.encryptAndSignRegionData(rsp.toByteArray(), key_id));
                    return;
                }
                var regionInfo = Hashing.base64Decode(event.getRegionInfo());
                ctx.json(Cryptography.encryptAndSignRegionData(regionInfo, key_id));
            } catch (Exception ex) {
                Main.getLogger().error(String.format("Problem with /query_cur_region %s", ex.getMessage()));
            }
        }
        else {
            QueryCurrentRegionEvent event = new QueryCurrentRegionEvent(regionData);
            event.call();
            ctx.result(event.getRegionInfo());
        }
    }

    /**
     * No Parameters
     */
    private static void queryServerAddress(Context ctx) {
        ctx.result("%s:%d".formatted(ConfigManager.httpConfig.gameInfo.gameServerIP, ConfigManager.httpConfig.gameInfo.gameServerPort));
    }

    /**
     * game -> game biz.<br>
     * region -> server region id.
     */
    private static void getGateAddressRequest(Context ctx) {
        String game = ctx.queryParam("game");
        String region = ctx.queryParam("region");
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        JsonArray ip_list = new JsonArray();
        if(game == null || region == null || regions.get(region) == null) {
            response.addProperty("retcode", Retcodes.RETCODE_FAILED);
            response.addProperty("message", "get address from config failed, err: get address from config failed no support region");
            response.add("data", null);
        }
        else {
            JsonObject region_data = new JsonObject();
            region_data.addProperty("ip", regions.get(region).getRegionQuery().getRegionInfo().getGateserverIp());
            region_data.addProperty("port", regions.get(region).getRegionQuery().getRegionInfo().getGateserverPort());
            ip_list.add(region_data);

            data.add("address_list", ip_list);
            response.addProperty("retcode", Retcodes.RETCODE_SUCCESS);
            response.addProperty("message", "OK");
            response.add("data", data);
        }
        ctx.result(Json.toJsonString(response)).contentType("application/json");
    }

    @Override
    public void applyRoutes(Javalin javalin) {
        javalin.get("/query_region_list", RegionRequestRouter::queryRegionList);
        javalin.get("/query_cur_region/{region}", RegionRequestRouter::queryCurrentRegion);
        javalin.get("/query_server_address", RegionRequestRouter::queryServerAddress);

        /// https://hk4e-beta-sdk-os.hoyoverse.com/dispatch/dispatch/getGateAddress
        javalin.get("dispatch/dispatch/getGateAddress", RegionRequestRouter::getGateAddressRequest);
    }
}
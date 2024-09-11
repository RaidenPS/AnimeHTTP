import requests
import json
import sys

def fetch_and_write(data, timeout=5):
    url, type_to_write, filename = data
    response = requests.get(url, timeout=timeout)
    response.raise_for_status()
    json_data = response.json()
    if 'data' in json_data and type_to_write in json_data['data']:
        content_to_write = json_data['data'][type_to_write]
        
        with open(filename, 'w') as file:
            json.dump(content_to_write, file, indent=4)
            
        print(f"Writing {type_to_write} to {filename}")

if __name__ == "__main__":
    CLIENT_TYPE = 3 #PC
    GAME_BIZ = "hk4e_global" # OVERSEAS
    CURRENCY = "JPY" # YEN
    PLATFORM_LAUNCHER_ID = "ddxf6vlr1reo"
    LAUNCHER_ID = "8fANlj5K7I"
    GENSHIN_GAME_OS_ID = "gopR6Cufr3"
    PACKAGE_ID = "ScSYQBFhu9"
    PACKAGE_PASSWORD = "bDL4JUHL625x"
    LAUNCHER_KEY = "gcStgarh"
    LAUNCHER_IDINT = 10

    # Example usage
    input_data = [
        # Payment
        [f"https://hk4e-sdk-os.hoyoverse.com/hk4e_global/mdk/shopwindow/shopwindow/listPriceTier?game_biz={GAME_BIZ}&currency={CURRENCY}", "tiers", "tiers.json"], # OK
        ["https://hk4e-sdk-os.hoyoverse.com/mdk/luckycat/luckycat/listPayPlat", "pay_plats", "android_platforms.json"], # OK
        [f"https://hk4e-sdk-os.hoyoverse.com/mdk/tally/tally/listPayPlat?currency={CURRENCY}", "pay_types", "pc_platforms.json"], # OK
        
        # Launcher
        [f"https://sg-public-api-static.hoyoverse.com/downloader/sophon/api/getParamsConfig?client_type={CLIENT_TYPE}&plat_app={PLATFORM_LAUNCHER_ID}", "params_config", "params_config.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getNotification?launcher_id={LAUNCHER_ID}", "notifications", "notifications.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameChannelSDKs?channel=1&game_ids[]={GENSHIN_GAME_OS_ID}&launcher_id={LAUNCHER_ID}&sub_channel=3", "game_channel_sdks", "game_sdks_config.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameDeprecatedFileConfigs?channel=1&game_ids[]={GENSHIN_GAME_OS_ID}&launcher_id={LAUNCHER_ID}&sub_channel=3", "deprecated_file_configs", "games_deprecated_config.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePlugins?game_ids[]={GENSHIN_GAME_OS_ID}&launcher_id={LAUNCHER_ID}", "plugin_releases", "game_plugins_config.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGamePackages?game_ids[]={GENSHIN_GAME_OS_ID}&launcher_id={LAUNCHER_ID}", "game_packages", "game_packages_config.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameConfigs?game_ids[]={GENSHIN_GAME_OS_ID}&launcher_id={LAUNCHER_ID}", "launch_configs", "launch_game_configs.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameBranches?game_ids[]={GENSHIN_GAME_OS_ID}&launcher_id={LAUNCHER_ID}", "game_branches", "game_branches_config.json"], # OK
        [f"https://sg-public-api.hoyoverse.com/downloader/sophon_chunk/api/getBuild?branch=main&package_id={PACKAGE_ID}&password={PACKAGE_PASSWORD}&plat_app={PLATFORM_LAUNCHER_ID}", "manifests", "sophon_manifests.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getAllGameBasicInfo?launcher_id={LAUNCHER_ID}&language=en-us&game_id={GENSHIN_GAME_OS_ID}", "game_info_list", "game_list.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGameContent?launcher_id={LAUNCHER_ID}&game_id={GENSHIN_GAME_OS_ID}&language=en-us", "content", "links_config.json"], # OK
        [f"https://sg-hyp-api.hoyoverse.com/hyp/hyp-connect/api/getGames?launcher_id={LAUNCHER_ID}&language=en-us", "games", "games_config.json"], # OK
        [f"https://sdk-os-static.hoyoverse.com/hk4e_global/mdk/launcher/api/resource?key={LAUNCHER_KEY}&launcher_id={LAUNCHER_IDINT}", "data", "latest_game.json"] # OK
        
    ]
    
    for data in input_data:
        fetch_and_write(data)
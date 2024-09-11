package raidenhttp.config.objects;

/// FIXME: INVESTIGATE EngineCodeSwitch!
public enum EngineCodeSwitchType {
    DISABLE_IOS_IGNORE_UI_IMMEDIATE_SKINNING(25000),
    DISABLE_ANDROID_INVALIDATEATTACHMENTS_NEW_MODE(26000),
    DISABLE_ANDROID_DEPTH_RESOLVE(27000),
    DISABLE_ADRENO_BUGGY_IVALIDATEFRAMEBUFFER(27001),
    DISABLE_PERMATERIAL_PROPERTY_UNSHARE_FIX(27002),
    DISABLE_ADRENO_VRS_FOR_TREES(28000),
    DISABLE_ANDROID_VERTEX_BUFFER_SPLIT(28001),
    DISABLE_TEXTURESTREAMING_PERSISTENTMIP_FIX(28002),
    DISABLE_PARTICLE_BOUNDING_CALCULATION_FIX(28003),
    DISABLE_IOS_TERRAIN_VTF_DIRTY_SYNC_FIX(28004),
    DISABLE_MOBIOLE_COLORGRADINGMASK_DONTSTOREDEPTH_FIX(30000),
    DISABLE_HIZCULLING_AABBEXTERNT_SCALE(30001),
    DISABLE_SCREENSHADOWMASK_RESET_ON_MOBILE(30002),
    DISABLE_SSAOHQ_USE_HALF_NORMAL(32000),
    DISABLE_SHADER_DELETE_FIX(32001),
    DISABLE_RESET_DELAY_RIGIDBODY_OPTIMIZATION(33000),
    DISABLE_WIN_DISPLAY_SIZE_CHANGE_FIX(33001),
    DISABLE_DELAY_SELECT_OS_FONT(33002),
    UNKNOWN_ENGINE_CODE_35000(35000),
    UNKNOWN_ENGINE_CODE_35001(35001),
    UNKNOWN_ENGINE_CODE_35002(35002),
    UNKNOWN_ENGINE_CODE_35003(35003),
    UNKNOWN_ENGINE_CODE_35006(35006),
    UNKNOWN_ENGINE_CODE_35007(35007),
    UNKNOWN_ENGINE_CODE_35008(35008),
    UNKNOWN_ENGINE_CODE_35009(35009),
    UNKNOWN_ENGINE_CODE_36000(36000),
    UNKNOWN_ENGINE_CODE_36001(36001),
    UNKNOWN_ENGINE_CODE_36002(36002),
    UNKNOWN_ENGINE_CODE_36003(36003),
    UNKNOWN_ENGINE_CODE_36006(36006),
    UNKNOWN_ENGINE_CODE_36008(36008),
    UNKNOWN_ENGINE_CODE_36009(36009),
    UNKNOWN_ENGINE_CODE_37000(37000),
    UNKNOWN_ENGINE_CODE_37001(37001),
    UNKNOWN_ENGINE_CODE_37002(37002),
    UNKNOWN_ENGINE_CODE_38000(38000),
    UNKNOWN_ENGINE_CODE_38001(38001),
    UNKNOWN_ENGINE_CODE_38002(38002),
    UNKNOWN_ENGINE_CODE_38003(38003),
    UNKNOWN_ENGINE_CODE_40000(40000),
    UNKNOWN_ENGINE_CODE_40001(40001),
    UNKNOWN_ENGINE_CODE_40002(40002),
    UNKNOWN_ENGINE_CODE_40003(40003),
    UNKNOWN_ENGINE_CODE_40004(40004),
    UNKNOWN_ENGINE_CODE_40005(40005),
    UNKNOWN_ENGINE_CODE_41000(41000),
    UNKNOWN_ENGINE_CODE_41001(41001),
    UNKNOWN_ENGINE_CODE_41002(41002),
    UNKNOWN_ENGINE_CODE_42000(42000),
    UNKNOWN_ENGINE_CODE_42001(42001),
    UNKNOWN_ENGINE_CODE_42002(42002),
    UNKNOWN_ENGINE_CODE_42003(42003),
    UNKNOWN_ENGINE_CODE_42004(42004),
    UNKNOWN_ENGINE_CODE_43000(43000),
    UNKNOWN_ENGINE_CODE_43001(43001),
    UNKNOWN_ENGINE_CODE_43002(43002),
    UNKNOWN_ENGINE_CODE_43003(43003),
    UNKNOWN_ENGINE_CODE_43005(43005),
    UNKNOWN_ENGINE_CODE_43006(43006),
    UNKNOWN_ENGINE_CODE_43007(43007),
    UNKNOWN_ENGINE_CODE_43008(43008),
    UNKNOWN_ENGINE_CODE_43009(43009),
    UNKNOWN_ENGINE_CODE_44000(44000),
    UNKNOWN_ENGINE_CODE_44001(44001),
    UNKNOWN_ENGINE_CODE_44002(44002),
    UNKNOWN_ENGINE_CODE_44003(44003),
    UNKNOWN_ENGINE_CODE_44004(44004),
    UNKNOWN_ENGINE_CODE_44005(44005),
    UNKNOWN_ENGINE_CODE_44006(44006),
    UNKNOWN_ENGINE_CODE_44007(44007),
    UNKNOWN_ENGINE_CODE_44008(44008),
    UNKNOWN_ENGINE_CODE_44009(44009),
    UNKNOWN_ENGINE_CODE_44010(44010),
    UNKNOWN_ENGINE_CODE_45000(45000),
    UNKNOWN_ENGINE_CODE_45001(45001),
    UNKNOWN_ENGINE_CODE_45002(45002),
    UNKNOWN_ENGINE_CODE_46000(46000),
    UNKNOWN_ENGINE_CODE_46001(46001),
    UNKNOWN_ENGINE_CODE_46002(46002),
    UNKNOWN_ENGINE_CODE_46003(46003),
    UNKNOWN_ENGINE_CODE_46004(46004),
    UNKNOWN_ENGINE_CODE_47000(47000),
    UNKNOWN_ENGINE_CODE_47001(47001),
    UNKNOWN_ENGINE_CODE_47002(47002),
    UNKNOWN_ENGINE_CODE_47003(47003),
    UNKNOWN_ENGINE_CODE_47004(47004),
    UNKNOWN_ENGINE_CODE_47005(47005),
    UNKNOWN_ENGINE_CODE_47006(47006),
    UNKNOWN_ENGINE_CODE_48001(48001),
    UNKNOWN_ENGINE_CODE_48002(48002),
    UNKNOWN_ENGINE_CODE_48003(48003),
    UNKNOWN_ENGINE_CODE_48004(48004),
    UNKNOWN_ENGINE_CODE_48005(48005),
    UNKNOWN_ENGINE_CODE_48006(48006),
    UNKNOWN_ENGINE_CODE_48007(48007),
    UNKNOWN_ENGINE_CODE_48008(48008),
    UNKNOWN_ENGINE_CODE_48010(48010),
    UNKNOWN_ENGINE_CODE_50000(50000),
    UNKNOWN_ENGINE_CODE_50001(50001),
    UNKNOWN_ENGINE_CODE_50002(50002),
    UNKNOWN_ENGINE_CODE_50003(50003),
    UNKNOWN_ENGINE_CODE_50004(50004),
    UNKNOWN_ENGINE_CODE_50005(50005),
    UNKNOWN_ENGINE_CODE_50006(50006),
    UNKNOWN_ENGINE_CODE_50007(50007),
    UNKNOWN_ENGINE_CODE_50008(50008),
    UNKNOWN_ENGINE_CODE_50009(50009),
    UNKNOWN_ENGINE_CODE_50010(50010),
    UNKNOWN_ENGINE_CODE_50011(50011),
    UNKNOWN_ENGINE_CODE_50012(50012),
    UNKNOWN_ENGINE_CODE_50013(50013),
    UNKNOWN_ENGINE_CODE_50014(50014),
    UNKNOWN_ENGINE_CODE_50015(50015),
    UNKNOWN_ENGINE_CODE_51000(51000),
    UNKNOWN_ENGINE_CODE_51001(51001),
    UNKNOWN_ENGINE_CODE_51003(51003);

    private final int value;
    EngineCodeSwitchType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
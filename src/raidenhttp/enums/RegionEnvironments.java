package raidenhttp.enums;

public enum RegionEnvironments {
    PRODUCTION_CN(0),
    SANDBOX_CN(1),
    PRODUCTION_OS(2),
    SANDBOX_OS(3),
    PRODUCTION_PRE_RELEASE_CN(4),
    PRODUCTION_PRE_RELEASE_OS(5),
    TEST_CN(6),
    TEST_OS(7),
    PET_CN(8),
    BETA_CN(9),
    BETA_CN_PRE(10),
    BETA_OS(11),
    BETA_OS_PRE(12),
    HOTFIX_CN(19),
    HOTFIX_OS(22);

    private final int value;
    RegionEnvironments(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

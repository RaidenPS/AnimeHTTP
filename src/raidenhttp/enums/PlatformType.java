package raidenhttp.enums;

public enum PlatformType {
    PLATFORM_IOS(1),
    PLATFORM_ANDROID(2),
    PLATFORM_PC(3),
    PLATFORM_WEB(4),
    PLATFORM_WAP(5),
    PLATFORM_PS4(6),
    PLATFORM_CLOUD_ANDROID(8),
    PLATFORM_CLOUD_PC(9),
    PLATFORM_CLOUD_IOS(10),
    PLATFORM_PS5(11),
    PLATFORM_MACOS(12),
    PLATFORM_CLOUD_MACOS(13);

    private final int value;
    PlatformType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PlatformType fromValue(int value) {
        for (PlatformType type : PlatformType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
package raidenhttp.enums;

public enum AccountType {
    MIHOYO_GUEST(0),
    MIHOYO_DEFAULT(1),
    MIHOYO_XIAOMI(11),
    MIHOYO_COOLPAD(12),
    MIHOYO_YYB(13),
    MIHOYO_BILIBILI(14),
    MIHOYO_HUAWEI(15),
    MIHOYO_MEIZU(16),
    MIHOYO_OPPO(18),
    MIHOYO_VIVO(19),
    MIHOYO_UC(20),
    MIHOYO_WANDOJIA(21),
    MIHOYO_LENOVO(22),
    MIHOYO_JINLI(23),
    MIHOYO_BAIDU(24),
    MIHOYO_DANGLE(26);

    private final int value;
    AccountType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
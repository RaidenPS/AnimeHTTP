package raidenhttp.enums;

public enum ServerType {
    NONE(0),
    AMERICA(1),
    EUROPE(2),
    ASIA(3),
    CHINA(4);

    private final int value;
    ServerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

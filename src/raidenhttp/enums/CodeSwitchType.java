package raidenhttp.enums;

/// FIXME: INVESTIGATE CodeSwitch!

public enum CodeSwitchType {
    DEFAULT(0);

    private final int value;
    CodeSwitchType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
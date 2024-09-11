package raidenhttp.enums;

public enum CaptchaActionType {
    ACTION_NONE(0),
    ACTION_GEETEST(1);

    private final int value;
    CaptchaActionType(int value) {
        this.value = value;
    }
}
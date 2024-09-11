package raidenhttp.enums;

/// FIXME: INVESTIGATE CoverSwitch!
public enum CoverSwitchType {
    DEFAULT(0),
    GACHA(1),
    MALL(2),
    BATTLE_PASS(3),
    BULLETIN(4),
    MAIL(5),
    TIME(6),
    COMMUNITY(7),
    HANDBOOK(8),
    FEEDBACK(9),
    QUEST(10),
    MAP(11),
    TEAM(12),
    FRIENDS(13),
    AVATAR_LIST(14),
    CHARACTER(15),
    ACTIVITY(16),
    MULTIPLAYER(17),
    RECHARGE_CARD(18),
    EXCHANGE_CODE(19),
    GUIDE_RATING(20),
    SHARE(21),
    MCOIN(22),
    BATTLE_PASS_RECHARGE(23),
    ACHIEVEMENT(24),
    PHOTOGRAPH(25),
    NETWORK_LATENCY_ICON(26),
    USER_CENTER(27),
    ACCOUNT_BINDING(28),
    RECOMMEND_PANEL(29),
    CODEX(30),
    REPORT(31),
    DERIVATIVE_MALL(32),
    EDIT_NAME(33),
    EDIT_SIGNATURE(34),
    RESIN_CARD(35),
    FILE_INTEGRITY_CHECK(36),
    ACTIVITY_H5(37),
    SURVEY(38),
    CONCERT_PACKAGE(39),
    PLAYER_PROFILE_CLOUD_GAME(40),
    BATTLE_PASS_DISCOUNT_PRICE(41);

    private final int value;
    CoverSwitchType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

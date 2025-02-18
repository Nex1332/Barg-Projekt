package de.semenchenko.entity.enums;

public enum BotState {
    WAIT_FOR_EMAIL_STATE("WAIT_FOR_EMAIL_STATE"),
    WAIT_FOR_CITY_STATE("WAIT_FOR_CITY_STATE"),
    BASE_STATE("BASE_STATE");

    private final String value;

    BotState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static BotState fromValue(String v) {
        for (BotState u: BotState.values()) {
            if (u.value.equals(v)) {
                return u;
            }
        }
        return null;
    }
}

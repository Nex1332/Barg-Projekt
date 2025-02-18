package de.semenchenko.entity.enums;

public enum UserState {
    REGISTERED("REGISTERED"),
    NOT_REGISTERED("NOT_REGISTERED"),
    CONFIRM_EMAIL("CONFIRM_EMAIL"),
    CONFIRM_CITY("CONFIRM_CITY");

    private final String value;

    UserState(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static UserState fromValue(String v) {
        for (UserState u: UserState.values()) {
            if (u.value.equals(v)) {
                return u;
            }
        }
        return null;
    }
}

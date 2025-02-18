package de.semenchenko.service.enums;

public enum ServiceCommand {
    HELP("/help"), // Dispatcher
    REG("/reg"), // Node -> User-Ms
    START("/start"), // Dispatcher
    WEATHER("/weather"), // Weather-Ms
    SETTING("/settings"), // Node -> User-Ms
    CANCEL("/cancel"); // User-MS

    private final String value;

    ServiceCommand(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ServiceCommand fromValue(String v) {
        for (ServiceCommand c: ServiceCommand.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }
}
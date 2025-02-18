package de.semenchenko.service.enums;

public enum ServiceCommand {
    HELP("/help"), // Node
    REG("/reg"), // Node -> User-Ms
    START("/start"), // Dispatcher
    WEATHER("/weather"), // Weather-Ms
    SETTING("/settings"), // Node
    CANCEL("/cancel"),
    RESET("/reset"); // Node

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

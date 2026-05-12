package ru.dom.votingsystem.action;

public enum ActionType {
    CONSOLE,
    MESSAGE;

    public static ActionType fromToken(final String token) {
        for (ActionType type : values()) {
            if (type.name().equalsIgnoreCase(token)) {
                return type;
            }
        }
        return null;
    }
}

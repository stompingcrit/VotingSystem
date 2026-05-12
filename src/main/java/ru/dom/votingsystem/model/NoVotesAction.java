package ru.dom.votingsystem.model;

public enum NoVotesAction {
    NOTHING,
    RANDOM;

    public static NoVotesAction fromString(final String raw) {
        if (raw == null) {
            return NOTHING;
        }
        try {
            return valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return NOTHING;
        }
    }
}

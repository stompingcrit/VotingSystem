package ru.dom.votingsystem.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class ColorUtil {
    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    private ColorUtil() {
    }

    public static Component colorize(final String text) {
        return SERIALIZER.deserialize(text == null ? "" : text);
    }
}

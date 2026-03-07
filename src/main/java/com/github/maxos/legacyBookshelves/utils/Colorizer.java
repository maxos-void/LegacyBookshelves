package com.github.maxos.legacyBookshelves.utils;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;

public class Colorizer {

    private static final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static String colorize(String msg) {
        return legacySerializer.serialize(
                LegacyComponentSerializer.legacyAmpersand().deserialize(msg)
        );
    }

    public static List<String> colorizeList(List<String> listMsg) {
        return listMsg.stream().map(Colorizer::colorize).toList();
    }

}

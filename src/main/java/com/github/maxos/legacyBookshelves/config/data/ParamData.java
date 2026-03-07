package com.github.maxos.legacyBookshelves.config.data;


public record ParamData(
        boolean canPutSimple,
        boolean canStack,
        int frequencySaveDb,
        // настройки инвентаря
        String title,
        int size
) {}

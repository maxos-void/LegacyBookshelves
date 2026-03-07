package com.github.maxos.legacyBookshelves.config.impl;

import com.github.maxos.legacyBookshelves.config.Config;
import com.github.maxos.legacyBookshelves.config.data.MessagesData;
import com.github.maxos.legacyBookshelves.file.FileManager;
import com.github.maxos.legacyBookshelves.utils.Colorizer;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class MessagesConfig extends Config {

    private static final String MAIN_SECTION = "messages";

    public MessagesConfig(FileManager file) {
        super(file);
    }

    @Getter
    private MessagesData msgData;

    @Override
    protected void parseConfig() {
        ConfigurationSection msgSection = getSection(MAIN_SECTION);
        if (msgSection == null) return;

        // TODO(Дописать парсинг сообщений)

    }

    @Override
    protected void setDefaultValues() {
        msgData = new MessagesData(
                new HashMap<>(

                ), Colorizer.colorize("&7[&6L&bB&7]")
        );
    }

}

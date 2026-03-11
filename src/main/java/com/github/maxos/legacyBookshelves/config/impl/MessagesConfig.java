package com.github.maxos.legacyBookshelves.config.impl;

import com.github.maxos.legacyBookshelves.config.Config;
import com.github.maxos.legacyBookshelves.config.data.MessagesData;
import com.github.maxos.legacyBookshelves.file.FileManager;
import com.github.maxos.legacyBookshelves.utils.Colorizer;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessagesConfig extends Config {

    private static final String MAIN_SECTION = "messages";

    public MessagesConfig(FileManager file) {
        super(file);
    }

    @Getter
    private String prefix;
    @Getter
    private MessagesData msgData;

    @Override
    protected void parseConfig() {
        ConfigurationSection msgSection = getSection(MAIN_SECTION);
        if (msgSection == null) return;

        String prefix = msgSection.getString("prefix");
        this.prefix = prefix != null
                ? prefix
                : Colorizer.colorize("&7[&6L&bB&7]");


        msgData = new MessagesData(
                Colorizer.colorize(msgSection.getString("reload"))
        );

    }

    @Override
    protected void setDefaultValues() {
        msgData = new MessagesData(
                Colorizer.colorize("&aПлагин успешно перезагружен &e[{time} ms]")
        );
    }

}

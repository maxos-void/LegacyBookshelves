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
    private MessagesData msgData;

    @Override
    protected void parseConfig() {
        ConfigurationSection msgSection = getSection(MAIN_SECTION);
        if (msgSection == null) return;

        String prefix = msgSection.getString("prefix");
        HashMap<String, String> msgMap = new HashMap<>();

        Set<String> keys = msgSection.getKeys(false);
        keys.forEach(key ->
                msgMap.put(key, Colorizer.colorize(msgSection.getString(key)))
        );

        msgData = new MessagesData(msgMap, prefix);

    }

    @Override
    protected void setDefaultValues() {
        Map<String, String> defaultMap = Map.of(
                "reload", Colorizer.colorize("&aПлагин успешно перезагружен &e[{time} ms]")
        );
        msgData = new MessagesData(
                defaultMap, Colorizer.colorize("&7[&6L&bB&7]")
        );
    }

}

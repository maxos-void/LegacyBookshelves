package com.github.maxos.legacyBookshelves.config;

import com.github.maxos.legacyBookshelves.file.FileManager;
import com.github.maxos.legacyBookshelves.utils.log.FastLog;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import static com.github.maxos.legacyBookshelves.utils.log.FastLog.sendLog;
import static com.github.maxos.legacyBookshelves.utils.log.LogType.ERR;

public abstract class Config {

    protected final FileManager file;
    protected FileConfiguration config;

    public Config(FileManager file) {
        this.file = file;
        this.config = file.getConfig();
        parseConfig();
    }

    protected abstract void parseConfig();
    protected abstract void setDefaultValues();

    protected final ConfigurationSection getSection(String sectionKey) {
        ConfigurationSection section = config.getConfigurationSection(sectionKey);
        if (section == null) {
            sendLog(
                    ERR,
                    "Не удалось получить секцию " + sectionKey + "в конфигурционном файле! (null)"
            );
            // устанавливаем по дефолту
            setDefaultValues();
        }
        return section;
    }

    protected final void reload() {
        file.reloadConfig();
        config = file.getConfig();
        parseConfig();
    }

}

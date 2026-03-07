package com.github.maxos.legacyBookshelves.file;

import com.github.maxos.legacyBookshelves.LegacyBookshelves;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class FileManager {
    private final LegacyBookshelves plugin;
    private final String fileName;
    private FileConfiguration roleConfig;
    private File configFile;

    public FileManager(LegacyBookshelves plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        saveDefaultConfig();
        reloadConfig();
    }

    private void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), fileName);
        }

        if (!configFile.exists()) {
            plugin.getLogger().info("Создание " + fileName + "...");
            plugin.saveResource(fileName, false);
            plugin.getLogger().info("Создан файл " + fileName);
        }
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), fileName);
        }

        roleConfig = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultStream = plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
            roleConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (roleConfig == null) {
            reloadConfig();
        }
        return roleConfig;
    }

    public void saveConfig() {
        if (roleConfig == null || configFile == null) {
            return;
        }

        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Не удалось сохранить " + fileName + " ", ex);
        }
    }
}

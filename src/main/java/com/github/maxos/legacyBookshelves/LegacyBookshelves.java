package com.github.maxos.legacyBookshelves;

import com.github.maxos.legacyBookshelves.config.impl.MessagesConfig;
import com.github.maxos.legacyBookshelves.config.impl.ParamConfig;
import com.github.maxos.legacyBookshelves.file.FileManager;
import com.github.maxos.legacyBookshelves.listeners.BookshelvesClickListener;
import com.github.maxos.legacyBookshelves.shelf.ShelfManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LegacyBookshelves extends JavaPlugin {

    private FileManager settingsFile;
    private ParamConfig paramConfig;
    private MessagesConfig msgConfig;

    private ShelfManager shelfManager;

    private BookshelvesClickListener bookshelvesClickListener;

    @Override
    public void onEnable() {
        settingsFile = new FileManager(this, "settings.yml");
        paramConfig = new ParamConfig(settingsFile);
        msgConfig = new MessagesConfig(settingsFile);

        shelfManager = new ShelfManager(paramConfig.getParamData());

        bookshelvesClickListener = new BookshelvesClickListener(shelfManager);

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(bookshelvesClickListener, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

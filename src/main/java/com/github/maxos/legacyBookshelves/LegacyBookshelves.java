package com.github.maxos.legacyBookshelves;

import com.github.maxos.legacyBookshelves.command.LbsCommand;
import com.github.maxos.legacyBookshelves.command.LbsTabCompleter;
import com.github.maxos.legacyBookshelves.config.impl.MessagesConfig;
import com.github.maxos.legacyBookshelves.config.impl.ParamConfig;
import com.github.maxos.legacyBookshelves.database.DataBaseConnector;
import com.github.maxos.legacyBookshelves.database.DataBaseLoader;
import com.github.maxos.legacyBookshelves.database.DataBaseManager;
import com.github.maxos.legacyBookshelves.file.FileManager;
import com.github.maxos.legacyBookshelves.listeners.BookshelvesClickListener;
import com.github.maxos.legacyBookshelves.scheduler.Scheduler;
import com.github.maxos.legacyBookshelves.shelf.AutoSaver;
import com.github.maxos.legacyBookshelves.shelf.ShelfManager;
import com.github.maxos.legacyBookshelves.utils.log.FastLog;
import com.github.maxos.legacyBookshelves.utils.log.LogType;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LegacyBookshelves extends JavaPlugin {

    private static final String CONFIG_FILE_NAME = "settings.yml";
    private static final String COMMAND = "legacybookshelves";

    private final Scheduler scheduler = Scheduler.INSTANCE;

    private DataBaseConnector dbConnector;
    private DataBaseManager dbManager;
    private DataBaseLoader dbLoader;

    private FileManager settingsFile;
    private ParamConfig paramConfig;
    private MessagesConfig msgConfig;

    private ShelfManager shelfManager;

    private BookshelvesClickListener bookshelvesClickListener;

    private AutoSaver saver;

    private LbsCommand executor;
    private LbsTabCompleter tabCompleter;

    @Override
    public void onEnable() {

        scheduler.initialization(this);

        dbConnector = new DataBaseConnector(this);
        dbManager = new DataBaseManager();

        settingsFile = new FileManager(this, CONFIG_FILE_NAME);
        paramConfig = new ParamConfig(settingsFile);
        msgConfig = new MessagesConfig(settingsFile);

        shelfManager = new ShelfManager(paramConfig);
        dbLoader = new DataBaseLoader(shelfManager, dbManager);
        dbLoader.loadData();

        saver = new AutoSaver(dbManager, shelfManager, paramConfig);

        registerListeners();

        registerCommand();

        saveResource("help.txt", false);
    }

    @Override
    public void onDisable() {
        scheduler.stopAllTasks();
        saver.forceSave();
    }

    private void registerListeners() {
        PluginManager manager = Bukkit.getPluginManager();

        bookshelvesClickListener = new BookshelvesClickListener(shelfManager);

        manager.registerEvents(bookshelvesClickListener, this);
    }

    private void registerCommand() {
        executor = new LbsCommand(this, msgConfig);
        tabCompleter = new LbsTabCompleter();

        PluginCommand cmd = this.getCommand(COMMAND);
        if (cmd != null) {
            cmd.setExecutor(executor);
            cmd.setTabCompleter(tabCompleter);
        }
    }

    public Long reload() {
        long start = System.currentTimeMillis();
        paramConfig.reload();
        msgConfig.reload();

        saver.reloadTask();
        shelfManager.reloadShelves();

        long end = System.currentTimeMillis();
        return end - start;
    }

}

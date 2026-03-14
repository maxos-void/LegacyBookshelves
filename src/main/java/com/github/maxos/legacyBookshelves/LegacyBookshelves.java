package com.github.maxos.legacyBookshelves;

import com.github.maxos.legacyBookshelves.command.LbsCommand;
import com.github.maxos.legacyBookshelves.command.LbsTabCompleter;
import com.github.maxos.legacyBookshelves.config.impl.BlocksConfig;
import com.github.maxos.legacyBookshelves.config.impl.MessagesConfig;
import com.github.maxos.legacyBookshelves.config.impl.ParamConfig;
import com.github.maxos.legacyBookshelves.database.DataBaseConnector;
import com.github.maxos.legacyBookshelves.database.DataBaseLoader;
import com.github.maxos.legacyBookshelves.database.DataBaseManager;
import com.github.maxos.legacyBookshelves.file.FileManager;
import com.github.maxos.legacyBookshelves.listeners.BlockBreakListener;
import com.github.maxos.legacyBookshelves.listeners.BlockClickListener;
import com.github.maxos.legacyBookshelves.listeners.InventoryClickListener;
import com.github.maxos.legacyBookshelves.listeners.PistonListener;
import com.github.maxos.legacyBookshelves.scheduler.Scheduler;
import com.github.maxos.legacyBookshelves.shelf.AutoSaver;
import com.github.maxos.legacyBookshelves.shelf.ShelfManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LegacyBookshelves extends JavaPlugin {

    private static final String CONFIG_FILE_NAME = "settings.yml";
    private static final String COMMAND = "legacybookshelves";

    private final Scheduler scheduler = Scheduler.INSTANCE;

    private PluginManager pluginManager;

    private DataBaseConnector dbConnector;
    private DataBaseManager dbManager;
    private DataBaseLoader dbLoader;

    private FileManager settingsFile;
    private ParamConfig paramConfig;
    private MessagesConfig msgConfig;
    private BlocksConfig blocksConfig;

    private ShelfManager shelfManager;

    private BlockClickListener blockClickListener;
    private InventoryClickListener inventoryClickListener;
    private BlockBreakListener blockBreakListener;
    private PistonListener pistonListener;

    private AutoSaver saver;

    private LbsCommand executor;
    private LbsTabCompleter tabCompleter;


    @Override
    public void onEnable() {

        pluginManager = Bukkit.getPluginManager();

        scheduler.initialization(this);

        dbConnector = new DataBaseConnector(this);
        dbManager = new DataBaseManager();

        settingsFile = new FileManager(this, CONFIG_FILE_NAME);
        paramConfig = new ParamConfig(settingsFile);
        msgConfig = new MessagesConfig(settingsFile);
        blocksConfig = new BlocksConfig(settingsFile);

        shelfManager = new ShelfManager(blocksConfig);
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

        blockClickListener = new BlockClickListener(shelfManager, blocksConfig);
        blockBreakListener = new BlockBreakListener(shelfManager, blocksConfig);
        inventoryClickListener = new InventoryClickListener(/*blocksConfig*/);
        pistonListener = new PistonListener(shelfManager, blocksConfig);

        pluginManager.registerEvents(blockClickListener, this);
        pluginManager.registerEvents(blockBreakListener, this);
        pluginManager.registerEvents(inventoryClickListener, this);
        pluginManager.registerEvents(pistonListener, this);
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

    public void reload(CommandSender s) {
        scheduler.runAsyncTask(() -> {
            long start = System.currentTimeMillis();
            paramConfig.reloadConfig();
            msgConfig.reloadConfig();
            blocksConfig.reloadConfig();

            saver.reloadTask();
            shelfManager.reloadShelves();

            long result = System.currentTimeMillis() - start;
            scheduler.runSyncTask(() -> s.sendMessage(
                    msgConfig.getMsgData()
                            .reloadMsg()
                            .replace("{time}", Long.toString(result))
            ));
        });
    }

}

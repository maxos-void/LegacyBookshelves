package com.github.maxos.legacyBookshelves.shelf;

import com.github.maxos.legacyBookshelves.LegacyBookshelves;
import com.github.maxos.legacyBookshelves.config.impl.ParamConfig;
import com.github.maxos.legacyBookshelves.database.DataBaseManager;
import com.github.maxos.legacyBookshelves.database.service.SerializationService;
import org.bukkit.Bukkit;

public class AutoSaver {

    private final DataBaseManager dbManager;
    private final ShelfManager shelfManager;
    private final ParamConfig cfg;
    private final LegacyBookshelves plugin;

    public AutoSaver(
            DataBaseManager dbManager, ShelfManager shelfManager,
            ParamConfig cfg, LegacyBookshelves plugin
    ) {
        this.dbManager = dbManager;
        this.shelfManager = shelfManager;
        this.cfg = cfg;
        this.plugin = plugin;
        scheduler();
    }

    private int taskId;

    private void scheduler() {
        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin, () -> {

                    dbManager.saveShelves(
                            SerializationService.INSTANCE.shelfSerialization(shelfManager.getShelvesToInventory())
                    );

                }, 20L, 200L
        ).getTaskId();
    }

    public void reloadTask() {
        Bukkit.getScheduler().cancelTask(taskId);
        scheduler();
    }

}

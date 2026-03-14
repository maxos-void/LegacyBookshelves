package com.github.maxos.legacyBookshelves.shelf;

import com.github.maxos.legacyBookshelves.config.impl.ParamConfig;
import com.github.maxos.legacyBookshelves.database.DataBaseManager;
import com.github.maxos.legacyBookshelves.scheduler.Scheduler;
import com.github.maxos.legacyBookshelves.shelf.data.ShelfData;

import java.util.Set;

public class AutoSaver {

    private final DataBaseManager dbManager;
    private final ShelfManager shelfManager;
    private final ParamConfig cfg;

    public AutoSaver(
            DataBaseManager dbManager,
            ShelfManager shelfManager,
            ParamConfig cfg
    ) {
        this.dbManager = dbManager;
        this.shelfManager = shelfManager;
        this.cfg = cfg;

        asyncSave();
    }

    private final Scheduler scheduler = Scheduler.INSTANCE;
    private int taskId;

    private void asyncSave() {

        taskId = scheduler.runAsyncTaskTimer(cfg.getParamData().frequencySaveDb(), () -> {
            Set<ShelfData> data = shelfManager.getData();
            if (!data.isEmpty()) {
                dbManager.saveShelves(
                        data
                );
            }
        });
    }

    public void forceSave() {
        Set<ShelfData> data = shelfManager.getData();
        dbManager.saveShelves(
                data
        );
    }

    public void reloadTask() {
        scheduler.stopTask(taskId);
        asyncSave();
    }

}

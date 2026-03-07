package com.github.maxos.legacyBookshelves.inventory;

import com.github.maxos.legacyBookshelves.config.data.ParamData;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ShelfInventoryHolder implements InventoryHolder {

    private final Inventory shelfInventory;
    private ParamData cfg;

    public ShelfInventoryHolder(ParamData cfg) {
        this.cfg = cfg;
        this.shelfInventory = Bukkit.createInventory(this, cfg.size(), cfg.title());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return shelfInventory;
    }

}

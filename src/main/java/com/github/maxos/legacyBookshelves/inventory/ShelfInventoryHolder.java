package com.github.maxos.legacyBookshelves.inventory;

import com.github.maxos.legacyBookshelves.config.data.BlockData;
import com.github.maxos.legacyBookshelves.config.data.ParamData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ShelfInventoryHolder implements InventoryHolder {

    private final Inventory shelfInventory;

    @Getter
    private BlockData cfg;

    @Getter
    private final String title;

    public ShelfInventoryHolder(BlockData cfg) {
        this.cfg = cfg;
        title = cfg.inventoryTitle();
        this.shelfInventory = Bukkit.createInventory(this, cfg.inventorySize(), title);
        shelfInventory.setMaxStackSize(cfg.maxStackSize());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return shelfInventory;
    }

}

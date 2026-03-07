package com.github.maxos.legacyBookshelves.shelf;

import com.github.maxos.legacyBookshelves.config.data.ParamData;
import com.github.maxos.legacyBookshelves.inventory.ShelfInventoryHolder;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;

public class ShelfManager {

    private ParamData cfg;

    public ShelfManager(ParamData cfg) {
        this.cfg = cfg;
    }

    // хранилище наших полок (Блок полки -> её инвентарь)
    private final HashMap<Block, Inventory> shelvesToInventory = new HashMap<>();

    private void saveShelf(Block shelf) {
        InventoryHolder shelfHolder = new ShelfInventoryHolder(cfg);
        Inventory shelfInventory = shelfHolder.getInventory();

        shelvesToInventory.put(shelf, shelfInventory);
    //    inventoryToShelves.put(shelfInventory, shelf);
    }

    public Inventory getInventory(Block shelf) {
        Inventory shelfInventory = shelvesToInventory.get(shelf);

        if (shelfInventory == null) {
            saveShelf(shelf);
            shelfInventory = shelvesToInventory.get(shelf);
        }

        return shelfInventory;
    }

}

package com.github.maxos.legacyBookshelves.shelf;

import com.github.maxos.legacyBookshelves.config.data.ParamData;
import com.github.maxos.legacyBookshelves.database.DataBaseManager;
import com.github.maxos.legacyBookshelves.inventory.ShelfInventoryHolder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShelfManager {

    private ParamData cfg;

    public ShelfManager(ParamData cfg) {
        this.cfg = cfg;
    }

    // хранилище наших полок (Блок полки -> её инвентарь)
    @Getter
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

    public void loadShelfFromDb(Location blockLocation, Map<Integer, ItemStack> items) {
        Block shelf = blockLocation.getWorld().getBlockAt(blockLocation);
        saveShelf(shelf);

        Inventory shelfInventory = shelvesToInventory.get(shelf);
        // запихиваем шмотки
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            int slot = entry.getKey();
            ItemStack item = entry.getValue();

            shelfInventory.setItem(slot, item);

        }
    }

}

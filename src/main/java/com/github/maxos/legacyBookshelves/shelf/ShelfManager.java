package com.github.maxos.legacyBookshelves.shelf;

import com.github.maxos.legacyBookshelves.config.data.ParamData;
import com.github.maxos.legacyBookshelves.config.impl.ParamConfig;
import com.github.maxos.legacyBookshelves.database.service.DataPreparationService;
import com.github.maxos.legacyBookshelves.inventory.ShelfInventoryHolder;
import com.github.maxos.legacyBookshelves.shelf.data.ShelfData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.maxos.legacyBookshelves.shelf.ChangeType.*;

public class ShelfManager {

    private ParamConfig cfg;

    public ShelfManager(ParamConfig cfg) {
        this.cfg = cfg;
        cacheInfo();
    }

    // тутась хранилище наших полок (Блок полки -> её инвентарь)
    private final ConcurrentHashMap<Block, Inventory> shelvesToInventory = new ConcurrentHashMap<>();

    public Set<ShelfData> getData() {
        Set<ShelfData> data = DataPreparationService.INSTANCE.collectData(shelvesToInventory);
        return data;
    }

    private void saveShelf(Block shelf, Map<Block, Inventory> container) {
        InventoryHolder shelfHolder = new ShelfInventoryHolder(cfg.getParamData());
        Inventory shelfInventory = shelfHolder.getInventory();

        container.put(shelf, shelfInventory);
    //    inventoryToShelves.put(shelfInventory, shelf);
    }

    public Inventory getInventory(Block shelf) {
        Inventory shelfInventory = shelvesToInventory.get(shelf);

        if (shelfInventory == null) {
            saveShelf(shelf, shelvesToInventory);
            shelfInventory = shelvesToInventory.get(shelf);
        }

        return shelfInventory;
    }

    private String title;
    private int size;

    private void cacheInfo() {
        ParamData data = cfg.getParamData();
        title = data.title();
        size = data.size();
    }

    private ChangeType isInfoChanged() {

        ParamData data = cfg.getParamData();
        boolean titleChanged = !data.title().equals(title);
        boolean sizeChanged = data.size() != size;

        if (!titleChanged && !sizeChanged) return ChangeType.NONE;
        if (titleChanged && sizeChanged) return ALL;
        if (titleChanged) return ChangeType.TITLE;
        return ChangeType.SIZE;


    }

    public void reloadShelves() {
        ChangeType change = isInfoChanged();
        if (change == NONE) return;

        Set<ChangeType> changes = new HashSet<>();
        if (change == ALL) {
            changes.add(TITLE);
            changes.add(SIZE);
        } else {
            changes.add(change);
        }

        changeInventory(changes);

    }

    private void changeInventory(Set<ChangeType> changes) {
        cacheInfo();
        HashMap<Block, Inventory> newMap = new HashMap<>();

        for (Map.Entry<Block, Inventory> entry : shelvesToInventory.entrySet()) {
            Inventory oldInv = entry.getValue();
            Block block = entry.getKey();

            saveShelf(block, newMap);
            Inventory newInv = newMap.get(block);

            copyInventoryContents(oldInv, newInv, changes, block.getLocation());
        }

        shelvesToInventory.putAll(newMap);
    }

    private void copyInventoryContents(
            Inventory oldInv,
            Inventory newInv,
            Set<ChangeType> changes,
            Location location
    ) {
        if (changes.contains(TITLE) && !changes.contains(SIZE)) {
            newInv.setStorageContents(oldInv.getStorageContents().clone());
            return;
        }

        for (int slot = 0; slot < oldInv.getSize(); slot++) {
            ItemStack item = oldInv.getItem(slot);
            if (item == null || item.getType().isAir()) continue;

            transferItem(item, slot, newInv, location);
        }
    }

    private void transferItem(
            ItemStack item,
            int slot,
            Inventory newInv,
            Location location
    ) {
        if (slot < newInv.getSize()) {
            newInv.setItem(slot, item.clone());
            return;
        }

        int freeSlot = findFreeSlot(newInv);
        if (freeSlot != -1) {
            newInv.setItem(freeSlot, item.clone());
        } else {
            dropItem(location, item);
        }
    }

    private int findFreeSlot(Inventory inv) {
        for (int slot = 0; slot < inv.getSize(); slot++) {
            if (inv.getItem(slot) == null || inv.getItem(slot).getType().isAir()) {
                return slot;
            }
        }
        return -1;
    }

    private void dropItem(Location location, ItemStack item) {
        World world = location.getWorld();
        world.dropItemNaturally(location, item);
    }

    public void loadShelfFromDb(Location blockLocation, Map<Integer, ItemStack> items) {
        Block shelf = blockLocation.getWorld().getBlockAt(blockLocation);
        saveShelf(shelf, shelvesToInventory);

        Inventory shelfInventory = shelvesToInventory.get(shelf);
        // запихиваем шмотки
        if (items.size() > shelfInventory.getSize()) {
            Iterator<ItemStack> iterator = items.values().iterator();
            for (int slot = 0; slot < shelfInventory.getSize(); slot++) {
                ItemStack item = iterator.next();
                shelfInventory.setItem(slot, item);
            }

        } else {
            for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                int slot = entry.getKey();
                if (slot >= 0 && slot < shelfInventory.getSize()) {
                    shelfInventory.setItem(slot, entry.getValue());
                }
            }
        }
    }
}

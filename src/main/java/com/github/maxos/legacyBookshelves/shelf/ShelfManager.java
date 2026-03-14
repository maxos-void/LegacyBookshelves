package com.github.maxos.legacyBookshelves.shelf;

import com.github.maxos.legacyBookshelves.config.data.BlockData;
import com.github.maxos.legacyBookshelves.config.impl.BlocksConfig;
import com.github.maxos.legacyBookshelves.database.service.DataPreparationService;
import com.github.maxos.legacyBookshelves.inventory.ShelfInventoryHolder;
import com.github.maxos.legacyBookshelves.shelf.data.ShelfData;
import com.github.maxos.legacyBookshelves.utils.log.FastLog;
import com.github.maxos.legacyBookshelves.utils.log.LogType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShelfManager {

    private BlocksConfig blocksConfig;

    public ShelfManager(BlocksConfig blocksConfig) {
        this.blocksConfig = blocksConfig;
        //cacheInfo();
    }

    // тутась хранилище наших полок (Блок полки -> её инвентарь)
    private final ConcurrentHashMap<Block, Inventory> shelvesToInventory = new ConcurrentHashMap<>();

    public Set<ShelfData> getData() {
        clearInvalid();

        Set<ShelfData> data = DataPreparationService.INSTANCE.collectData(shelvesToInventory);
        return data;
    }

    private void clearInvalid() {
        Iterator<Map.Entry<Block, Inventory>> iterator = shelvesToInventory.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Block, Inventory> entry = iterator.next();
            Block b = entry.getKey();
            if (b == null || !blocksConfig.isUsableBlock(b.getType())) {
                iterator.remove();
            }
        }
    }

    private void saveShelf(Block shelf, Map<Block, Inventory> container) {
        InventoryHolder shelfHolder = new ShelfInventoryHolder(blocksConfig.getBlockData(shelf.getType()));
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

    public Inventory getBreakInventory(Block block) {
        Inventory inv = shelvesToInventory.get(block);
        shelvesToInventory.remove(block);
        return inv;
    }

    public void applyMoves(Map<Block, Inventory> moves, Set<Block> olds) {
        shelvesToInventory.keySet().removeAll(olds);
        shelvesToInventory.putAll(moves);
    }

    //public void moveBlock(Block oldBlock, Block newBlock) {
    //    Inventory inv = shelvesToInventory.get(oldBlock);
    //    shelvesToInventory.remove(oldBlock);
    //    shelvesToInventory.put(newBlock, inv);
    //}

    public void reloadShelves() {
        Iterator<Map.Entry<Block, Inventory>> iterator = shelvesToInventory.entrySet().iterator();
        while (iterator.hasNext()) {

            Map.Entry<Block, Inventory> entry = iterator.next();
            Block block = entry.getKey();
            Material blockMaterial = block.getType();

            Inventory oldInv = entry.getValue();
            oldInv.close();

            if (!blocksConfig.isUsableBlock(blockMaterial)) {
                iterator.remove();
            }

            BlockData newData = blocksConfig.getBlockData(blockMaterial);
            if (isChangedData(oldInv, newData)) {
                saveShelf(block, shelvesToInventory);

                int oldSize = oldInv.getSize();
                int newSize = newData.inventorySize();
                Inventory newInv = shelvesToInventory.get(block);

                if (newSize > oldSize || newSize == oldSize) {
                    newInv.setStorageContents(oldInv.getStorageContents());
                } else {
                    fillInventory(newInv, oldInv);
                }
            }

        }
    }

    // типа пытаемся засейвить как можно больше предметов со старого инвентаря
    // если новый размер стал меньше изначального
    private void fillInventory(Inventory newInv, Inventory oldInv) {
        ItemStack[] oldContents = oldInv.getStorageContents();
        int acceptVolume = newInv.getSize();
        int slot = 0;

        for (int i = 0; i < oldContents.length && slot < acceptVolume; i++) {
            ItemStack item = oldContents[i];
            if (item != null) {
                newInv.setItem(slot, item.clone());
                slot++;
            }
        }
    }

    private boolean isChangedData(Inventory oldInv, BlockData newData) {
        if (oldInv.getHolder() instanceof ShelfInventoryHolder holder) {

            return newData.maxStackSize() != oldInv.getMaxStackSize() ||
                    newData.inventorySize() != oldInv.getSize() ||
                    !holder.getTitle().equals(newData.inventoryTitle());

        }
        return false;
    }

    public void loadShelfFromDb(Location blockLocation, Map<Integer, ItemStack> items) {
        Block shelf = blockLocation.getWorld().getBlockAt(blockLocation);
        if (!blocksConfig.isUsableBlock(shelf.getType())) {
            return;
        }

        saveShelf(shelf, shelvesToInventory);

        if (items.isEmpty()) return;

        //FastLog.sendLog(LogType.WARN, items.toString());

        Inventory shelfInventory = shelvesToInventory.get(shelf);
        int oldSize = Collections.max(items.keySet());
        int newSize = shelfInventory.getSize();
        // запихиваем шмотки
        if (oldSize > newSize) {
            Iterator<ItemStack> iterator = items.values().iterator();
            for (int slot = 0; slot < newSize && iterator.hasNext(); slot++) {
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

    /*

    private String title;
    private int size;

    private void cacheInfo() {
        BlocksConfig data = blocksConfig.getBlockData();
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
        //cacheInfo();
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

    */
}

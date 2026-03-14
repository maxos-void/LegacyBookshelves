package com.github.maxos.legacyBookshelves.config.impl;

import com.github.maxos.legacyBookshelves.config.Config;
import com.github.maxos.legacyBookshelves.config.data.BlockData;
import com.github.maxos.legacyBookshelves.file.FileManager;
import com.github.maxos.legacyBookshelves.utils.Colorizer;
import com.github.maxos.legacyBookshelves.utils.log.FastLog;
import com.github.maxos.legacyBookshelves.utils.log.LogType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;


public class BlocksConfig extends Config {

    private static final String MAIN_SECTION = "blocks";
    private static final String INVENTORY_TITLE_KEY = "inventory-title";
    private static final String INVENTORY_SIZE_KEY = "inventory-size";
    private static final String ALLOWED_ITEMS_KEY = "allowed-items";
    private static final String MAX_STACK_SIZE_KEY = "max-stack-size";

    // дефолтные значения
    private static final String INVENTORY_TITLE_DEFAULT_VALUE = Colorizer.colorize("&0▶ КНИЖНАЯ ПОЛКА (NULL)");
    private static final int INVENTORY_SIZE_DEFAULT_VALUE = 36;
    private static final int MAX_STACK_DEFAULT_VALUE = 64;


    public BlocksConfig(FileManager file) {
        super(file);
    }

    //private Map<Material, EnumSet<ChangeType>> changeStatus = new EnumMap<>(Material.class);
    private Map<Material, BlockData> usableBlocks;
    //private boolean initCompleted = false;

    public BlockData getBlockData(Material material) {
        return usableBlocks.get(material);
    }

    public boolean isUsableBlock(Material blockMaterial) {
        return usableBlocks.containsKey(blockMaterial);
    }


    @Override
    protected void parseConfig() {
        ConfigurationSection blocksSection = getSection(MAIN_SECTION);
        if (blocksSection == null) return;

        //HashMap<Material, BlockData> usableBlocks = new HashMap<>();

        usableBlocks = new EnumMap<>(Material.class);

        Set<String> blocksId = blocksSection.getKeys(false);
        for (String blockId : blocksId) {

            ConfigurationSection blockSection = blocksSection.getConfigurationSection(blockId);
            if (blockSection == null) continue;

            Material blockMaterial = getMaterial(blockId);
            if (blockMaterial == null) continue;

            BlockData blockData = parseBlockData(blockSection);

            //if (initCompleted) compareData(blockData, blockMaterial);

            usableBlocks.put(blockMaterial, blockData);

        }

        //initCompleted = true;

        //this.usableBlocks = usableBlocks;

    }

    /*
    private void compareData(BlockData newData, Material blockMaterial) {
        BlockData oldData = usableBlocks.get(blockMaterial);
        if (oldData != null) {

            boolean isTitleChange = !Objects.equals(newData.inventoryTitle(), oldData.inventoryTitle());
            boolean isSizeChange = newData.inventorySize() != oldData.inventorySize();
            boolean isMaxStackChange = newData.maxStackSize() != oldData.maxStackSize();

            EnumSet<ChangeType> changes = EnumSet.noneOf(ChangeType.class);

            if (isTitleChange) changes.add(ChangeType.TITLE);
            if (isSizeChange) changes.add(ChangeType.SIZE);
            if (isMaxStackChange) changes.add(ChangeType.MAX_STACK);

            changeStatus.put(blockMaterial, changes);
        }

    }
     */

    private BlockData parseBlockData(ConfigurationSection blockSection) {

        int maxStackSize = blockSection.getInt(MAX_STACK_SIZE_KEY, MAX_STACK_DEFAULT_VALUE);
        int inventorySize = blockSection.getInt(INVENTORY_SIZE_KEY, INVENTORY_SIZE_DEFAULT_VALUE);
        String inventoryTitle = Colorizer.colorize(
                blockSection.getString(INVENTORY_TITLE_KEY, INVENTORY_TITLE_DEFAULT_VALUE)
        );

        List<String> allowedItemsId = blockSection.getStringList(ALLOWED_ITEMS_KEY);
        Set<Material> allowedItems = new HashSet<>();

        for (String materialId : allowedItemsId) {
            Material itemMaterial = getMaterial(materialId.toUpperCase());
            if (itemMaterial != null) allowedItems.add(itemMaterial);
        }

        return new BlockData(
                maxStackSize,
                inventorySize,
                inventoryTitle,
                allowedItems
        );
    }

    private Material getMaterial(String materialId) {
        try {
            return Material.valueOf(
                    materialId
            );
        } catch (IllegalArgumentException e) {
            FastLog.sendLog(LogType.ERR, e.getMessage());
            FastLog.sendLog(LogType.ERR, "Неизвестный материал: " + materialId);
            return null;
        }
    }

    @Override
    protected void setDefaultValues() {
        BlockData defaultBlockData = new BlockData(
                MAX_STACK_DEFAULT_VALUE,
                INVENTORY_SIZE_DEFAULT_VALUE,
                Colorizer.colorize(INVENTORY_TITLE_DEFAULT_VALUE),
                Set.of(Material.BOOK)
        );
        usableBlocks = Map.of(Material.BOOKSHELF, defaultBlockData);
    }
}

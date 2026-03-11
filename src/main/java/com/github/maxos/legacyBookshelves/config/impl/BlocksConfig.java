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
    private static final String INVENTORY_TITLE_DEFAULT_VALUE = Colorizer.colorize("&0▶ КНИЖНАЯ ПОЛКА");
    private static final int INVENTORY_SIZE_DEFAULT_VALUE = 36;
    private static final int MAX_STACK_DEFAULT_VALUE = 64;

    public BlocksConfig(FileManager file) {
        super(file);
    }

    private Map<Material, BlockData> usableBlocks;
    public BlockData getBlockData(Material material) {
        return usableBlocks.get(material);
    }

    @Override
    protected void parseConfig() {
        ConfigurationSection blocksSection = getSection(MAIN_SECTION);
        if (blocksSection == null) return;

        HashMap<Material, BlockData> usableBlocks = new HashMap<>();

        Set<String> blocksId = blocksSection.getKeys(false);
        for (String blockId : blocksId) {

            ConfigurationSection blockSection = blocksSection.getConfigurationSection(blockId);
            if (blockSection == null) continue;

            Material blockMaterial = getMaterial(blockId);
            if (blockMaterial == null) continue;

            BlockData blockData = parseBlockData(blockSection);

            usableBlocks.put(blockMaterial, blockData);

        }

        this.usableBlocks = usableBlocks;

    }

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
                INVENTORY_TITLE_DEFAULT_VALUE,
                Set.of(Material.BOOK)
        );
        usableBlocks = Map.of(Material.BOOKSHELF, defaultBlockData);
    }
}

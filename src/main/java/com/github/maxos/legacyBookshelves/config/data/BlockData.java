package com.github.maxos.legacyBookshelves.config.data;

import org.bukkit.Material;

import java.util.Set;

public record BlockData(
        int maxStackSize,
        int inventorySize,
        String inventoryTitle,
        Set<Material> allowedItems

) {}

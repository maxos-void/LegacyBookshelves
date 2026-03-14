package com.github.maxos.legacyBookshelves.listeners;

import com.github.maxos.legacyBookshelves.config.impl.BlocksConfig;
import com.github.maxos.legacyBookshelves.shelf.ShelfManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlockBreakListener implements Listener {

    private final ShelfManager manager;
    private final BlocksConfig cfg;

    public BlockBreakListener(ShelfManager manager, BlocksConfig cfg) {
        this.manager = manager;
        this.cfg = cfg;
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        Material m = b.getType();
        if (cfg.isUsableBlock(m)) {
            Inventory inv = manager.getBreakInventory(b);
            spawnItems(inv, b);
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onBlockBreakFromExplosion(BlockExplodeEvent e) {
        List<Block> bList = e.blockList();
        for (Block b : bList) {
            Material m = b.getType();
            if (cfg.isUsableBlock(m)) {
                Inventory inv = manager.getBreakInventory(b);
                spawnItems(inv, b);
            }

        }
    }

    private void spawnItems(Inventory inv, Block b) {
        if (inv != null) {
            Location loc = b.getLocation();
            World world = loc.getWorld();
            for (ItemStack item : inv.getStorageContents()) {
                if (item != null) world.dropItemNaturally(loc, item);
            }
        }
    }

}

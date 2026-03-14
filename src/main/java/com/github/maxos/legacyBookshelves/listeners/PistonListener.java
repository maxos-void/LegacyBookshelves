package com.github.maxos.legacyBookshelves.listeners;

import com.github.maxos.legacyBookshelves.config.impl.BlocksConfig;
import com.github.maxos.legacyBookshelves.shelf.ShelfManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class PistonListener implements Listener {

    private final ShelfManager manager;
    private final BlocksConfig cfg;

    public PistonListener(ShelfManager manager, BlocksConfig cfg) {
        this.manager = manager;
        this.cfg = cfg;
    }

    @EventHandler(ignoreCancelled = true)
    private void onMoveByPiston(BlockPistonExtendEvent e) {
        move(e.getBlocks(), e.getDirection());
    }

    @EventHandler(ignoreCancelled = true)
    private void onMoveByStickyPiston(BlockPistonRetractEvent e) {
        move(e.getBlocks(), e.getDirection());
    }

    private void move(List<Block> blocks, BlockFace direction) {
        Map<Block, Inventory> moves = new HashMap<>();
        Set<Block> olds = new HashSet<>();

        for (Block b : blocks) {
            if (!cfg.isUsableBlock(b.getType())) return;

            olds.add(b);
            Block newBlock = calculateDirection(direction, b.getLocation());
            if (newBlock != null) {
                moves.put(newBlock, manager.getInventory(b));
            }
        }

        manager.applyMoves(moves, olds);
    }

    private Block calculateDirection(BlockFace face, Location loc) {
        World w = loc.getWorld();
        switch (face) {
            case SOUTH -> {
                return w.getBlockAt(loc.getBlockX(), loc.getBlockY(), (loc.getBlockZ() + 1));

                //newBlock = w.getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                //FastLog.sendLog(LogType.INFO, "LOCATION: "
                //        + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ());
                //FastLog.sendLog(LogType.INFO, "NEW LOCATION: "
                //        + loc.getBlockX() + loc.getBlockY() + (loc.getBlockZ() + 1));
            }
            case EAST -> {
                return w.getBlockAt((loc.getBlockX() + 1), loc.getBlockY(), loc.getBlockZ());

                //FastLog.sendLog(LogType.INFO, "LOCATION: "
                //        + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ());
                //FastLog.sendLog(LogType.INFO, "NEW LOCATION: "
                //        + (loc.getBlockX() + 1) + loc.getBlockY() + loc.getBlockZ());
            }
            case NORTH -> {
                return w.getBlockAt(loc.getBlockX(), loc.getBlockY(), (loc.getBlockZ() - 1));

                //FastLog.sendLog(LogType.INFO, "LOCATION: "
                //        + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ());
                //FastLog.sendLog(LogType.INFO, "NEW LOCATION: "
                //        + loc.getBlockX() + loc.getBlockY() + (loc.getBlockZ() - 1));
            }
            case WEST -> {
                return w.getBlockAt((loc.getBlockX() - 1), loc.getBlockY(), loc.getBlockZ());

                //FastLog.sendLog(LogType.INFO, "LOCATION: "
                //        + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ());
                //FastLog.sendLog(LogType.INFO, "NEW LOCATION: "
                //        + (loc.getBlockX() - 1) + loc.getBlockY() + loc.getBlockZ());
            }

            case UP -> {
                return w.getBlockAt(loc.getBlockX(), (loc.getBlockY() + 1), loc.getBlockZ());

                //FastLog.sendLog(LogType.INFO, "LOCATION: "
                //        + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ());
                //FastLog.sendLog(LogType.INFO, "NEW LOCATION: "
                //        + loc.getBlockX() + (loc.getBlockY() + 1) + loc.getBlockZ());
            }
            case DOWN -> {
                return w.getBlockAt(loc.getBlockX(), (loc.getBlockY() - 1), loc.getBlockZ());

                //FastLog.sendLog(LogType.INFO, "LOCATION: "
                //        + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ());
                //FastLog.sendLog(LogType.INFO, "NEW LOCATION: "
                //        + loc.getBlockX() + (loc.getBlockY() - 1) + loc.getBlockZ());
            }
        }

        return null;
    }

}

package com.github.maxos.legacyBookshelves.listeners;

import com.github.maxos.legacyBookshelves.config.impl.BlocksConfig;
import com.github.maxos.legacyBookshelves.shelf.ShelfManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockClickListener implements Listener {

    private final ShelfManager manager;
    private final BlocksConfig blocksConfig;

    public BlockClickListener(ShelfManager manager, BlocksConfig blocksConfig) {
        this.manager = manager;
        this.blocksConfig = blocksConfig;
    }

    @EventHandler
    private void clickOnBlock(PlayerInteractEvent e) {

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock == null || !blocksConfig.isUsableBlock(clickedBlock.getType())) return;

        Player p = e.getPlayer();
        if (!p.isSneaking()) {
            p.openInventory(manager.getInventory(clickedBlock));
            e.setCancelled(true);
        }

        //clickedBlock.getLocation().getBlockX();
        //Bukkit.getWorld("").getBlockAt()

    }

}

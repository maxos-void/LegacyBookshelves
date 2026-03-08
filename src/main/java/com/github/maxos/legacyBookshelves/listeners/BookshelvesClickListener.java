package com.github.maxos.legacyBookshelves.listeners;

import com.github.maxos.legacyBookshelves.shelf.ShelfManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.AIR;

public class BookshelvesClickListener implements Listener {

    private final ShelfManager manager;

    public BookshelvesClickListener(ShelfManager manager) {
        this.manager = manager;
    }

    @EventHandler
    private void clickOnBlock(PlayerInteractEvent e) {

        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.BOOKSHELF) return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Player p = e.getPlayer();
            if (!p.isSneaking()) {
                p.openInventory(manager.getInventory(clickedBlock));
                e.setCancelled(true);
            }

        }

        //clickedBlock.getLocation().getBlockX();
        //Bukkit.getWorld("").getBlockAt()

    }

}

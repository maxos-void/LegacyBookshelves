package com.github.maxos.legacyBookshelves.listeners;

import com.github.maxos.legacyBookshelves.shelf.ShelfManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryCloseListener implements Listener {

    private final ShelfManager manager;

    public InventoryCloseListener(ShelfManager manager) {
        this.manager = manager;
    }

    @EventHandler
    private void onCloseInventory(InventoryCloseEvent e) {

        Inventory inv = e.getInventory();


    }

}

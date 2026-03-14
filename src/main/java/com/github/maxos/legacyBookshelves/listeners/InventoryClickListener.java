package com.github.maxos.legacyBookshelves.listeners;

import com.github.maxos.legacyBookshelves.config.impl.BlocksConfig;
import com.github.maxos.legacyBookshelves.inventory.ShelfInventoryHolder;
import com.github.maxos.legacyBookshelves.utils.log.FastLog;
import com.github.maxos.legacyBookshelves.utils.log.LogType;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

   //!public InventoryClickListener(BlocksConfig cfg) {
   //!    this.cfg = cfg;
   //!}


    @EventHandler
    private void onClick(InventoryClickEvent e) {
        //!FastLog.sendLog(LogType.INFO, "СЛОТЫ " + e.getRawSlot() + " " + e.getSlot());
        Inventory destInv = e.getInventory();
        if (destInv.getHolder() instanceof ShelfInventoryHolder holder) {

            ClickType c = e.getClick();
            if (c.equals(ClickType.NUMBER_KEY)) {
                hotbarClick(e, holder);
            }

            ItemStack item = e.getCurrentItem();
            if (item == null) return;

            if (!isOurMaterial(holder, item)) {
                e.setCancelled(true);
                return;
            }
            if (c.isShiftClick()) {
                Inventory pInv = e.getWhoClicked().getInventory();
                if (pInv != e.getClickedInventory()) return;

                e.setCancelled(true);

                int remains = addItemLimited(destInv, item, destInv.getMaxStackSize());
                item.setAmount(remains);
            }
        }
    }

    private boolean isOurMaterial(ShelfInventoryHolder holder, ItemStack item) {
        return holder.getCfg().allowedItems().contains(item.getType());
    }

    private void hotbarClick(InventoryClickEvent e, ShelfInventoryHolder holder) {
        ItemStack item = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
        if (item == null) return;
        if (!isOurMaterial(holder, item)) e.setCancelled(true);
    }

    public static int addItemLimited(Inventory inv, ItemStack item, int max) {
        int remaining = item.getAmount();

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack slot = inv.getItem(i);
            if (slot == null) continue;
            if (!slot.isSimilar(item)) continue;

            int space = max - slot.getAmount();
            if (space <= 0) continue;

            int add = Math.min(space, remaining);
            slot.setAmount(slot.getAmount() + add);
            remaining -= add;
            if (remaining == 0) return 0;
        }

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack slot = inv.getItem(i);
            if (slot != null) continue;

            int add = Math.min(max, remaining);
            ItemStack newStack = item.clone();
            newStack.setAmount(add);
            inv.setItem(i, newStack);

            remaining -= add;
            if (remaining == 0) return 0;
        }

        return remaining;
    }

}

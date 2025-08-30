package com.signition.samskybridge.listener;

import com.signition.samskybridge.Main;
import com.signition.samskybridge.upgrade.UpgradeService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuiListener implements Listener {
    private final Main plugin;
    private final UpgradeService upgrade;
    public GuiListener(Main plugin, UpgradeService upgrade){
        this.plugin = plugin;
        this.upgrade = upgrade;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Inventory inv = e.getInventory();
        String title = plugin.getConfig().getString("gui.title-upgrade","섬 업그레이드");
        if (inv == null || e.getView() == null || e.getCurrentItem() == null) return;
        if (!e.getView().getTitle().equals(title)) return;
        e.setCancelled(true);
        int slot = e.getRawSlot();
        if (slot == 11 || slot == 15){
            upgrade.click((org.bukkit.entity.Player)e.getWhoClicked(), slot, e.isShiftClick());
        }
    }
}
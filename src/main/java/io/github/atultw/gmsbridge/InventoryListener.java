package io.github.atultw.gmsbridge;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Objects;

public class InventoryListener implements Listener {

    public InventoryListener(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("hi");
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws IOException, InterruptedException, MaxChangedBlocksException, DataException {
        Player p = (Player) e.getWhoClicked(); // The player that clicked the item
        ItemStack clickedItem = e.getCurrentItem(); // The item that was clicked
        Inventory inventory = e.getInventory(); // The inventory that was clicked in

        //if (inventory.getType().getDefaultTitle() == "Duel Maps") {
        //    Bukkit.broadcastMessage("boop");
        //    e.setCancelled(true);
        //}

        // Using slots click is a best option for your inventory click's
        for (int i = 0; i < SelectorCommand.maps.size(); i++) {
            assert clickedItem != null;
            if (Objects.requireNonNull(clickedItem.getItemMeta()).getDisplayName().equals(SelectorCommand.maps.get(i).getArenaName())) {
                PlayerJoin.JoinPlayer(p, SelectorCommand.maps.get(i));
            }
        }
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(InventoryDragEvent e) {
        //if (e.getInventory() == SelectorCommand.inv) {
        Bukkit.broadcastMessage("lol");
        e.setCancelled(true);
        //}
    }

    @EventHandler
    public void onInventoryClick(InventoryMoveItemEvent e) {
        //if (e.getInventory() == SelectorCommand.inv) {
        Bukkit.broadcastMessage("moved");
        e.setCancelled(true);
        //}
    }
}

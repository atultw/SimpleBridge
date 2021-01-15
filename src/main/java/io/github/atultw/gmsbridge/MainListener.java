package io.github.atultw.gmsbridge;

import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashSet;

public class MainListener implements Listener {

    public MainListener(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("hi");
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws IOException, DataException {
        Player p = (Player) e.getWhoClicked(); // The player that clicked the item
        ItemStack clickedItem = e.getCurrentItem(); // The item that was clicked
        Inventory inventory = e.getInventory(); // The inventory that was clicked in

        //if (inventory.getType().getDefaultTitle() == "Duel Maps") {
        //    Bukkit.broadcastMessage("boop");
        //    e.setCancelled(true);
        //}

        // Using slots click is a best option for your inventory click's
        for (int i = 0; i < Maps.AllMaps.size(); i++) {
            assert clickedItem != null;
            if (clickedItem.getItemMeta().getDisplayName().equals(Maps.AllMaps.get(i).getArenaName())) {
                Join.Initialize(p, Maps.AllMaps.get(i));
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

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.teleport(new Location(Bukkit.getWorld("lobby"), 0, 66, 0));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        HashSet<Player> allwaiting = new HashSet<>();

        //check if the player involved is waiting in any maps, and if so remove them from the waitlist

        for (MapDef M : Join.Waiting.keySet()) {
            allwaiting.addAll(Join.Waiting.get(M));
        }
        if (allwaiting.contains(p)) {
            for (MapDef M : Join.Waiting.keySet()) {
                Join.Waiting.get(M).remove(p);
            }
        }
    }
}

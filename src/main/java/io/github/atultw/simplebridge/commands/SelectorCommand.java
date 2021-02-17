package io.github.atultw.simplebridge.commands;

import io.github.atultw.simplebridge.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SelectorCommand implements CommandExecutor {
    public static Inventory inv = Bukkit.createInventory(null, 9, "Duel Maps");

    // Nice little method to create a gui item with a custom name, and description
    protected static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        initializeItems();
        Bukkit.getPlayer(sender.getName()).openInventory(inv);
        return true;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        for (int i = 0; i < Maps.AllMaps.size(); i++) {
            String DisplayBlock = Maps.AllMaps.get(i).getDisplayBlock();
            String ArenaName = Maps.AllMaps.get(i).getArenaName();
            inv.setItem(i, createGuiItem(Material.DIAMOND_SWORD, ArenaName, "§aNature Arena", "§bPvP and earn points in a one minute battle!"));
            // Material.getMaterial(DisplayBlock)
        }
    }


}

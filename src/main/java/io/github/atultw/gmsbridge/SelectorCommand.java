package io.github.atultw.gmsbridge;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class SelectorCommand implements CommandExecutor {
    public static Inventory inv = Bukkit.createInventory(null, 9, "Duel Maps");
    public static List<MapDef> maps = Main.allArenas;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        initializeItems();
        Bukkit.getPlayer(sender.getName()).openInventory(inv);
        return true;
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        for (int i = 0; i < maps.size(); i++) {
            String DisplayBlock = this.maps.get(i).getDisplayBlock();
            String ArenaName = this.maps.get(i).getArenaName();
            inv.setItem(i, createGuiItem(Material.STONE, ArenaName, "§aFirst line of the lore", "§bSecond line of the lore"));
            // Material.getMaterial(DisplayBlock)
        }
    }

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


}

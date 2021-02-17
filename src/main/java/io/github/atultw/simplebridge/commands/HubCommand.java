package io.github.atultw.simplebridge.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        ((Player) commandSender).teleport(new Location(Bukkit.getWorld("lobby"), 0, 66, 0));
        return false;
    }
}

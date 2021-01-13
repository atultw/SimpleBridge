package io.github.atultw.gmsbridge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class SaveMapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            InternalSchematic.New(Maps.AllMaps.get(0), Maps.AllMaps.get(0).c1l.getWorld(), Maps.AllMaps.get(0).getC1l(), Maps.AllMaps.get(0).getC2l());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}

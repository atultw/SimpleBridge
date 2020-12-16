package io.github.atultw.gmsbridge;

import com.sk89q.worldedit.data.DataException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class SaveMapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            InternalSchematic.New(Main.allArenas.get(0), Main.allArenas.get(0).c1l.getWorld(), Main.allArenas.get(0).getC1l(), Main.allArenas.get(0).getC2l());
        } catch (IOException | DataException e) {
            e.printStackTrace();
        }
        return true;
    }
}

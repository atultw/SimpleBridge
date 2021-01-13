package io.github.atultw.gmsbridge;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class ReloadMapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        MapDef m = Maps.AllMaps.get(0);
        try {
            Game.Reset(m);
        } catch (DataException | IOException | WorldEditException e) {
            e.printStackTrace();
        }
        return true;
    }
}

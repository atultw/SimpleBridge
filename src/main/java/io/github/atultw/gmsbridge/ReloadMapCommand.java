package io.github.atultw.gmsbridge;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class ReloadMapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        MapDef m = Main.allArenas.get(0);
        try {
            Game.Reset(m);
        } catch (DataException | IOException | MaxChangedBlocksException e) {
            e.printStackTrace();
        }
        return true;
    }
}

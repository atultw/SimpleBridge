package io.github.atultw.gmsbridge;


import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class InternalSchematic {
    public static void New(MapDef m, World w, Location l1, Location l2) throws IOException, com.sk89q.worldedit.data.DataException {
        Bukkit.broadcastMessage("saving schematic");
        File file = new File(m.getArenaName() + ".schem");
        EditSession es = new EditSession(new BukkitWorld(w), -1);
        Vector vl1 = new Vector(l1.getBlockX(), l1.getBlockY(), l1.getBlockZ());
        Vector vl2 = new Vector(l2.getBlockX(), l2.getBlockY(), l2.getBlockZ());
        CuboidClipboard cc = new CuboidClipboard(vl2.subtract(vl1).add(new Vector(1, 1, 1)), vl1);
        cc.copy(es);
        SchematicFormat.MCEDIT.save(cc, file);
        es.flushQueue();
    }

    public static void Load(MapDef m, World w, Location l1) throws IOException, MaxChangedBlocksException, DataException {
        //load it
        Bukkit.broadcastMessage("loading and pasting schematic");
        File file = new File(m.getArenaName() + ".schem");
        EditSession es = new EditSession(new BukkitWorld(w), -1);
        CuboidClipboard cc = CuboidClipboard.loadSchematic(file);
        Vector vl1 = new Vector(l1.getBlockX(), l1.getBlockY(), l1.getBlockZ());
        cc.paste(es, vl1, false);

    }
}

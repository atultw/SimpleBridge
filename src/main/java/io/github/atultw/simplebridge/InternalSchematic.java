package io.github.atultw.simplebridge;


import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class InternalSchematic {
    public static void New(MapDef m, World w, Location l1, Location l2) throws IOException {
        try {
            File schematic = new File(m.getArenaName() + ".schematic");

            EditSession es = new EditSession(new BukkitWorld(w), -1);

            Vector min = new Vector(l1.getBlockX(), l1.getBlockY(), l1.getBlockZ());
            Bukkit.broadcastMessage("cords 1" + (l1.getBlockX() + l1.getBlockY() + l1.getBlockZ()));
            Vector max = new Vector(l2.getBlockX(), l2.getBlockY(), l2.getBlockZ());
            Bukkit.broadcastMessage("vect1 as string" + min);
            Bukkit.broadcastMessage("vect2 as string" + max);
            Bukkit.broadcastMessage("cords 2" + (l2.getBlockX() + l2.getBlockY() + l2.getBlockZ()));

            es.enableQueue();
            CuboidClipboard clipboard = new CuboidClipboard(max.subtract(min).add(new Vector(1, 1, 1)), min);
            clipboard.copy(es);
            SchematicFormat.MCEDIT.save(clipboard, schematic);
            es.flushQueue();

            Bukkit.broadcastMessage("Saved schematic!");
        } catch (IOException | DataException ex) {
            ex.printStackTrace();
        }


    }

    public static void loadSchem(MapDef m, World w, Location l1) throws IOException, com.sk89q.worldedit.data.DataException, MaxChangedBlocksException {

        try {
            File dir = new File(m.getArenaName() + ".schematic");

            EditSession editSession = new EditSession(new BukkitWorld(w), 999999999);
            editSession.enableQueue();

            SchematicFormat schematic = SchematicFormat.getFormat(dir);
            CuboidClipboard clipboard = schematic.load(dir);
            //clipboard.rotate2D(180);
            clipboard.paste(editSession, BukkitUtil.toVector(l1), false);
            editSession.flushQueue();
        } catch (DataException | IOException | MaxChangedBlocksException ex) {
            ex.printStackTrace();
        }
        /* use the clipboard here */

    }


}

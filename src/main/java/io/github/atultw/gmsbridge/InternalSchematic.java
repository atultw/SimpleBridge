package io.github.atultw.gmsbridge;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class InternalSchematic {
    public static void New(MapDef m, World w, Location l1, Location l2) throws IOException {
        File file = new File(m.getArenaName()+ ".schem");
        Vector bot = new Vector(l1.getBlockX(), l1.getBlockY(), l1.getBlockZ());
        Vector top = new Vector(l2.getBlockX(), l2.getBlockY(), l2.getBlockZ());
        CuboidRegion region = new CuboidRegion(new BukkitWorld(w), bot, top);
        Schematic schem = new Schematic(region);
        schem.save(file, ClipboardFormat.SCHEMATIC);
    }

    public static void Load(MapDef m, World w, Location l1) throws IOException, DataException, MaxChangedBlocksException {
        File file = new File(m.getArenaName()+ ".schem");
        Vector v = new Vector(l1.getBlockX(), l1.getBlockY(), l1.getBlockZ());
        EditSession es = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(w), WorldEdit.getInstance().getConfiguration().maxChangeLimit);
        SchematicFormat format = SchematicFormat.getFormat(file);
        CuboidClipboard cc = format.load(file);
        cc.paste(es, v, false);
    }
}

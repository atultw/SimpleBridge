package io.github.atultw.gmsbridge;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.command.SchematicCommands;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.SchematicWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class InternalSchematic {
    public void New(MapDef m) throws WorldEditException {
        Location l1 = m.getC1l();
        Location l2 = m.getC2l();
        CuboidRegion region = new CuboidRegion(l1.getWorld(), l1, l2);
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1);

        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
        Operations.complete(forwardExtentCopy);

    }
    public void Paste(MapDef m) throws IOException, WorldEditException, DataException {
        Location l = m.getC1l();
        File file = new File(m.getArenaName() + ".schem");
        Vector v = new Vector(l.getBlockX(), l.getBlockY(), l.getBlockZ()); // loc is your location variable.
        EditSession es = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(l.getWorld()), WorldEdit.getInstance().getConfiguration().maxChangeLimit)
        SchematicFormat format = SchematicFormat.getFormat(file);
        CuboidClipboard cc = format.load(file);
        cc.paste(es, v, false);
    }
}

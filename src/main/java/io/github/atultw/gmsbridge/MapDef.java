package io.github.atultw.gmsbridge;

// Define details for a map accessible where needed

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class MapDef {

    public String DisplayBlock;
    public Location CageLocation;
    public Location SpawnOneLocation;
    public Location SpawnTwoLocation;
    public Integer PlayersNeeded;
    public String ArenaName;
    public Location LobbyLocation;
    public Location c1l;
    public Location c2l;
    public List<Location> g1l = new ArrayList<>();
    public List<Location> g2l = new ArrayList<>();

    public MapDef(String dblock, Location cl, Location s1l, Location s2l, Location ll, int pn, String an, Location c1l, Location c2l, Location goal1l, Location goal2l) {
        this.DisplayBlock = dblock;
        this.CageLocation = cl;
        this.SpawnOneLocation = s1l;
        this.SpawnTwoLocation = s2l;
        this.PlayersNeeded = pn;
        this.ArenaName = an;
        this.c1l = c1l;
        this.c2l = c2l;
        this.g1l.add(goal1l);
        for (int i = -1; i < 1; i++) {
            Location la = new Location(goal1l.getWorld(), goal1l.getBlockX() + i, goal1l.getBlockY(), goal1l.getBlockZ());
            g1l.add(la);
            Location lb = new Location(la.getWorld(), la.getBlockX(), la.getBlockY(), la.getBlockZ() + 1);
            g1l.add(lb);
            //Location lc = new Location(la.getWorld(), la.getBlockX(), la.getBlockY(), la.getBlockZ()+2);
            //g1l.add(lc);
            Location ld = new Location(la.getWorld(), la.getBlockX(), la.getBlockY(), la.getBlockZ() - 1);
            g1l.add(ld);
            //Location le = new Location(la.getWorld(), la.getBlockX(), la.getBlockY(), la.getBlockZ()-2);
            //g1l.add(le);
        }
        this.g2l.add(goal2l);
        for (int i = -1; i < 1; i++) {
            Location la = new Location(goal2l.getWorld(), goal2l.getBlockX() + i, goal2l.getBlockY(), goal2l.getBlockZ());
            g2l.add(la);
            Location lb = new Location(la.getWorld(), la.getBlockX(), la.getBlockY(), la.getBlockZ() + 1);
            g2l.add(lb);
            //Location lc = new Location(la.getWorld(), la.getBlockX(), la.getBlockY(), la.getBlockZ()+2);
            //g2l.add(lc);
            Location ld = new Location(la.getWorld(), la.getBlockX(), la.getBlockY(), la.getBlockZ() - 1);
            g2l.add(ld);
            //Location le = new Location(la.getWorld(), la.getBlockX(), la.getBlockY(), la.getBlockZ()-2);
            //g2l.add(le);
        }
        this.LobbyLocation = ll;
    }

    public Location getCageLocation() {
        return this.CageLocation;
    }

    public String getDisplayBlock() {
        return this.DisplayBlock;
    }

    public String getArenaName() {
        return this.ArenaName;
    }

    public Location getSpawnOneLocation() {
        return this.SpawnOneLocation;
    }

    public Location getSpawnTwoLocation() {
        return this.SpawnTwoLocation;
    }

    public Location getLobbyLocation() {
        return this.LobbyLocation;
    }

    public Location getC1l() {
        return this.c1l;
    }

    public Location getC2l() {
        return this.c2l;
    }

}

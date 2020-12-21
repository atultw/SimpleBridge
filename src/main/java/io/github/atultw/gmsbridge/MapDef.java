package io.github.atultw.gmsbridge;

// Define details for a map accessible where needed

import org.bukkit.Location;

public class MapDef {

    String DisplayBlock;
    Location CageLocation;
    Location SpawnOneLocation;
    Location SpawnTwoLocation;
    Integer PlayersNeeded;
    String ArenaName;
    Location LobbyLocation;
    Location c1l;
    Location c2l;

    public MapDef(String dblock, Location cl, Location s1l, Location s2l, Location ll, int pn, String an, Location c1l, Location c2l) {
        this.DisplayBlock = dblock;
        this.CageLocation = cl;
        this.SpawnOneLocation = s1l;
        this.SpawnTwoLocation = s2l;
        this.PlayersNeeded = pn;
        this.ArenaName = an;
        this.c1l = c1l;
        this.c2l = c1l;
    }

    public Location getCageLocation(){
        return this.CageLocation;
    }

    public String getDisplayBlock(){
        return this.DisplayBlock;
    }

    public String getArenaName(){
        return this.ArenaName;
    }

    public Location getSpawnOneLocation(){
        return this.SpawnOneLocation;
    }

    public Location getSpawnTwoLocation(){
        return this.SpawnTwoLocation;
    }

    public Location getLobbyLocation(){
        return this.LobbyLocation;
    }

    public Location getC1l() {
        return this.c1l;
    }

    public Location getC2l() {
        return this.c2l;
    }

    public int getPlayerCount(){
        return this.PlayersNeeded;
    }


}

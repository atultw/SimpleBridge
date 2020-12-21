package io.github.atultw.gmsbridge;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public class Main extends JavaPlugin {
    private final Game g = new Game(this);
    FileConfiguration config;
    List<ConfigurationSection> arenasList;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.config = getConfig();

        //register listeners _____________
        new MainListener(this);

        // FOR LOOP loads the map info from config and saves it into mapdef objects for later use.
        // this loop also creates entries in gamemap
        for (String key : Objects.requireNonNull(config.getConfigurationSection("arenas")).getKeys(false)) {
            // get the section for the individual key
            ConfigurationSection thisArena = config.getConfigurationSection("arenas." + key);
            this.arenasList.add(config.getConfigurationSection("arenas." + key));

            // get the cage info
            assert thisArena != null;
            World cageW = Bukkit.getWorld(Objects.requireNonNull(thisArena.getString("cageworld")));
            int cageX = thisArena.getInt("cagex");
            int cageY = thisArena.getInt("cagey");
            int cageZ = thisArena.getInt("cagez");
            Location cageLoc = new Location(cageW, cageX, cageY, cageZ);
            String displayBlock = thisArena.getString("displayblock");

            World SpawnW = Bukkit.getWorld(Objects.requireNonNull(thisArena.getString("arenaworld")));

            // get the spawnone info
            int SpawnOneX = thisArena.getInt("xone");
            int SpawnOneY = thisArena.getInt("yone");
            int SpawnOneZ = thisArena.getInt("zone");
            Location SpawnOneLoc = new Location(SpawnW, SpawnOneX, SpawnOneY, SpawnOneZ);

            // get the spawntwo info
            int SpawnTwoX = thisArena.getInt("xtwo");
            int SpawnTwoY = thisArena.getInt("ytwo");
            int SpawnTwoZ = thisArena.getInt("ztwo");
            Location SpawnTwoLoc = new Location(SpawnW, SpawnTwoX, SpawnTwoY, SpawnTwoZ);

            // get the lobby info
            World LobbyW = Bukkit.getWorld(Objects.requireNonNull(thisArena.getString("lobbyworld")));
            int LobbyX = thisArena.getInt("xlobby");
            int LobbyY = thisArena.getInt("ylobby");
            int LobbyZ = thisArena.getInt("zlobby");
            Location LobbyLoc = new Location(LobbyW, LobbyX, LobbyY, LobbyZ);

            //corners
            int c1X = thisArena.getInt("c1x");
            int c1Y = thisArena.getInt("c1y");
            int c1Z = thisArena.getInt("c1z");
            Location C1Loc = new Location(SpawnW, c1X, c1Y, c1Z);

            int c2X = thisArena.getInt("c2x");
            int c2Y = thisArena.getInt("c2y");
            int c2Z = thisArena.getInt("c2z");
            Location C2Loc = new Location(SpawnW, c2X, c2Y, c2Z);

            // get the game info
            int PlayersNeeded = thisArena.getInt("players");
            String ArenaName = thisArena.getString("name");

            /**DEBUG**/ //Bukkit.getConsoleSender().sendMessage(cageLoc.toString());
            /**DEBUG**/ //Bukkit.getConsoleSender().sendMessage(Objects.requireNonNull(thisArena.getString("cageworld")));
            /**DEBUG**/ //assert cageW != null;
            /**DEBUG**/ //Bukkit.getConsoleSender().sendMessage(cageW.toString());


            MapDef arenaDataCurrent = new MapDef(displayBlock, cageLoc, SpawnOneLoc, SpawnTwoLoc, LobbyLoc, PlayersNeeded, ArenaName, C1Loc, C2Loc);

            // add to the relevant public lists
            Join.Waiting.put(arenaDataCurrent, null);
            Maps.AllMaps.add(arenaDataCurrent);
        }

        //register commands
        Objects.requireNonNull(this.getCommand("duel")).setExecutor(new SelectorCommand());
        Objects.requireNonNull(this.getCommand("reloadb2")).setExecutor(new ReloadMapCommand());
        Objects.requireNonNull(this.getCommand("saveb2")).setExecutor(new SaveMapCommand());
    }

}

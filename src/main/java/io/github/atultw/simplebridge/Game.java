package io.github.atultw.simplebridge;


import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Game implements Listener {
    static Main plugin;
    static HashSet<Player> allPlayersPlaying = new HashSet<>();
    public List<Player> PlayersPlaying = new ArrayList<>();
    static List<MapDef> MapsInUse = new ArrayList<>();
    static HashMap<Player, Integer> pointsCounter = new HashMap<>();


    //scheduler ids
    Integer taskInit;
    List<Integer> taskCountdown = new ArrayList<>();
    Integer taskSummary;
    Integer taskStop;

    Player p1;
    Player p2;
    MapDef m;

    //constructors
    public Game(Main p) {
        plugin = p;
    }

    public Game(Player p1, Player p2, MapDef ma) {
        this.p1 = p1;
        this.p2 = p2;
        this.m = ma;
    }

    public static void Reset(MapDef m) throws DataException, IOException, WorldEditException {
        InternalSchematic.loadSchem(m, m.getC1l().getWorld(), m.getC1l());
        InternalSchematic.New(m, m.getC1l().getWorld(), m.getC1l(), m.getC2l());
    }

    public void EditPoints(Player p, Integer n) {
        Integer oldPoints = pointsCounter.get(p);
        Integer newPoints = oldPoints + n;
        pointsCounter.put(p, newPoints);
    }

    public void Start() {

        //register listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);


        //remove from waiting
        Join.Waiting.get(m).remove(p1);
        Join.Waiting.get(m).remove(p2);
        //initialize points entries
        pointsCounter.put(p1, 0);
        pointsCounter.put(p2, 0);

        // add these players
        allPlayersPlaying.add(p1);
        allPlayersPlaying.add(p2);
        // mark map as in use
        MapsInUse.add(m);

        PlayersPlaying.add(p1);
        PlayersPlaying.add(p2);

        for (int i = 0; i < 5; i++) {
            final int count = i;
            taskCountdown.add(
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Bukkit.broadcastMessage("This message is shown after" + count * 10 + "seconds");
                        p1.sendTitle(ChatColor.RED.toString() + (5 - count) + "Seconds!", "to start");
                        p2.sendTitle(ChatColor.RED.toString() + (5 - count) + "Seconds!", "to start");
                    }, 20 * count)
            );
        }

        taskInit = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            //let players know
            p1.sendMessage("You are now dueling " + ChatColor.AQUA + p2.getName() + ChatColor.WHITE + "! Let the game begin! To score points, jump into the goal at the other base.");
            p2.sendMessage("You are now dueling " + ChatColor.AQUA + p1.getName() + ChatColor.WHITE + "! Let the game begin! To score points, jump into the goal at the other base.");

            //teleport the players
            p1.teleport(m.getSpawnOneLocation());
            p2.teleport(m.getSpawnTwoLocation());

            //gamemodes
            p1.setGameMode(GameMode.SURVIVAL);
            p2.setGameMode(GameMode.SURVIVAL);

            p1.setHealth(20.0);
            p1.setFoodLevel(20);

            p2.setHealth(20.0);
            p2.setFoodLevel(20);

            //give initial items
            p1.getInventory().clear();
            p2.getInventory().clear();
            ItemStack[] startItems = {new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_SWORD), new ItemStack(Material.WOOL, 64)};
            p1.getInventory().addItem(startItems);
            p2.getInventory().addItem(startItems);

            //fireworks!!
            FireworkHandler.launch(p1.getLocation(), Color.RED);
            FireworkHandler.launch(p2.getLocation(), Color.RED);
        }, 110L);


        for (int i = 1; i < 12; i++) {
            final int count = i;
            taskCountdown.add(
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        p1.sendTitle(120 - 10 * count + " " + ChatColor.BLUE + "Seconds!", "left till end of match");
                        p2.sendTitle(120 - 10 * count + " " + ChatColor.BLUE + "Seconds!", "left till end of match");
                    }, 200 * count)
            );
        }

        taskSummary = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            p1.sendTitle(ChatColor.RED + "All Done!", "GG! You got " + ChatColor.AQUA + pointsCounter.get(p1) + " Points!");
            p2.sendTitle(ChatColor.RED + "All Done!", "GG! You got " + ChatColor.AQUA + pointsCounter.get(p2) + " Points!");
        }, 2420L);


        taskStop = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            try {
                Stop();
            } catch (DataException | IOException | WorldEditException e) {
                e.printStackTrace();
            }
        }, 2430L);


    }

    public void Stop() throws DataException, IOException, WorldEditException {
        //cancel listeners
        HandlerList.unregisterAll(this);
        //stop the scheduled tasks
        plugin.getServer().getScheduler().cancelTask(taskInit);
        plugin.getServer().getScheduler().cancelTask(taskSummary);
        for (int i : taskCountdown) {
            plugin.getServer().getScheduler().cancelTask(i);
        }

        Bukkit.broadcastMessage("stopped cool");
        // clear inventories
        p1.getInventory().clear();
        p2.getInventory().clear();
        // teleport back to lobby
        p1.teleport(m.getLobbyLocation());
        p2.teleport(m.getLobbyLocation());
        // remove from the lists
        allPlayersPlaying.remove(p1);
        allPlayersPlaying.remove(p2);
        //manage points
        pointsCounter.remove(p1);
        pointsCounter.remove(p2);
        // remove players so game doesn't work anymore
        this.p1 = null;
        this.p2 = null;
        // reset from schematic
        Reset(m);
        // map no longer in use
        MapsInUse.remove(m);
    }

    @EventHandler
    public void onEDeath(PlayerDeathEvent event) {

        //if its not a player

        if (event.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player killed = (Player) event.getEntity();

        //do always
        if (killed == p1) {
            killed.setHealth(20.0);
            killed.setFoodLevel(20);
            killed.spigot().respawn();
            killed.teleport(m.getSpawnOneLocation());
        }

        if (killed == p2) {
            killed.setHealth(20.0);
            killed.setFoodLevel(20);
            killed.spigot().respawn();
            killed.teleport(m.getSpawnTwoLocation());
        }

        //just in case

        p1.setHealth(20.0);
        p1.setFoodLevel(20);

        p2.setHealth(20.0);
        p2.setFoodLevel(20);

        //do only if killed by opponent
        if (event.getEntity().getKiller() != null) {
            Player attacker = event.getEntity().getKiller();
            if (attacker == p1 | attacker == p2) {

                //prevent respawn screen
                killed.setHealth(20.0);
                killed.setFoodLevel(20);

                //increase points by 10 for kill
                EditPoints(attacker, 10);
                attacker.sendMessage(ChatColor.AQUA + "You Killed" + killed.getName() + "! +10 Points");
                FireworkHandler.launch(attacker.getLocation(), Color.BLUE);

                //decrease points by 10 for kill
                EditPoints(killed, -10);
                killed.sendMessage(ChatColor.AQUA + "You Died! -10 Points");
                FireworkHandler.launch(killed.getLocation(), Color.WHITE);

                //teleport to bed location instead of global spawn
                // if killed is player 0 of this game
                if (killed == p1) {
                    killed.teleport(m.getSpawnOneLocation());
                }

                if (killed == p2) {
                    killed.teleport(m.getSpawnTwoLocation());
                }
            }
        }

        event.setDeathMessage("");
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        //Location spawnLocation = new Location(world, x, y, z, yaw, pitch);
        if (event.getPlayer() == p1) {
            event.setRespawnLocation(m.getSpawnOneLocation());
        }
        //if second player...
        if (event.getPlayer() == p2) {
            event.setRespawnLocation(m.getSpawnTwoLocation());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() == p1 && e.getEntity() == p2) {
            Player damager = (Player) e.getDamager();
            Player receiver = (Player) e.getEntity();

            EditPoints(damager, 1);
            EditPoints(receiver, -1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent e) {
        if (m.g2l.contains(e.getTo().getBlock().getLocation())) {
            EditPoints(p1, 15);
            p1.sendMessage(ChatColor.GREEN + "Goal! 15 Points to you!");
            p1.teleport(m.getSpawnOneLocation());
            p2.teleport(m.getSpawnTwoLocation());
            p2.sendMessage(ChatColor.RED + "Opponent jumped through your goal!");
        }

        if (m.g1l.contains(e.getTo().getBlock().getLocation())) {
            EditPoints(p2, 15);
            p2.sendMessage(ChatColor.GREEN + "Goal! 15 Points to you!");
            p1.teleport(m.getSpawnOneLocation());
            p2.teleport(m.getSpawnTwoLocation());
            p1.sendMessage(ChatColor.RED + "Opponent jumped through your goal!");
        }

        if (e.getPlayer() == p1 || e.getPlayer() == p2) {
            if (e.getTo().getBlockY() < 50) {
                e.getPlayer().setHealth(0.0D);
                e.getPlayer().setHealth(20.0D);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws DataException, IOException, WorldEditException {
        Player p = e.getPlayer();
        if (p == p1 | p == p2) {
            Stop();
        }
    }
}

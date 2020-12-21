package io.github.atultw.gmsbridge;


import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Game implements Listener {
    private static Main plugin;
    Player p1;
    Player p2;
    MapDef m;

    public Game(Player p1, Player p2, MapDef ma) {
        this.p1 = p1;
        this.p2 = p2;
        this.m = ma;
    }

    public Game(Main plugin) {
        Game.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public List<Player> PlayersWaiting;
    public List<Player> PlayersPlaying;
    static HashSet<Player> allPlayersPlaying;
    static HashMap<Player, Integer> pointsCounter = new HashMap<>();

    public void EditPoints(Player p, Integer n) {
        Integer oldPoints = pointsCounter.get(p);
        Integer newPoints = oldPoints + n;
        pointsCounter.put(p, newPoints);
    }

    public void Start() throws IOException, DataException {
        // add these players
        allPlayersPlaying.add(p1);
        allPlayersPlaying.add(p2);

        ///**DEBUG**/Bukkit.broadcastMessage("start called");

        HashMap<Integer, Player> playersToSet = new HashMap<>();

        //SAVE THE SCHEMATIC FOR LATER
        InternalSchematic.New(m, m.getC1l().getWorld(), m.getC1l(), m.getC2l());

        //PUT THE PLAYERS INTO THE "PLAYING" LIST
        PlayersPlaying.add(p1);
        PlayersPlaying.add(p2);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            Bukkit.broadcastMessage("initializing game");
            //let players know
            p1.sendMessage("You are now dueling " + ChatColor.AQUA + p2.getName() + ChatColor.WHITE + "! Let the game begin! To score points, jump into the goal at the other base.");
            p2.sendMessage("You are now dueling " + ChatColor.AQUA + p1.getName() + ChatColor.WHITE + "! Let the game begin! To score points, jump into the goal at the other base.");

            //teleport the players
            p1.teleport(m.getSpawnOneLocation());
            p2.teleport(m.getSpawnTwoLocation());

            //give initial items
            p1.getInventory().clear();
            p2.getInventory().clear();
            ItemStack[] startItems = {new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_SWORD)};
            p1.getInventory().addItem(startItems);
        }, 20L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Bukkit.broadcastMessage("This message is shown after ten seconds");
            p1.sendTitle(ChatColor.BLUE + "50 Seconds!", "left till end of match");
            p2.sendTitle(ChatColor.BLUE + "50 Seconds!", "left till end of match");
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Bukkit.broadcastMessage("This message is shown after ten seconds");
            p1.sendTitle(ChatColor.AQUA + "40 Seconds!", "left till end of match");
            p2.sendTitle(ChatColor.AQUA + "40 Seconds!", "left till end of match");
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            /* DEBUG**/
            Bukkit.broadcastMessage("This message is shown after ten seconds");
            p1.sendTitle(ChatColor.GREEN + "30 Seconds!", "left till end of match");
            p2.sendTitle(ChatColor.GREEN + "30 Seconds!", "left till end of match");
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            /* DEBUG**/
            Bukkit.broadcastMessage("This message is shown after ten seconds");
            p1.sendTitle(ChatColor.DARK_GREEN + "20 Seconds!", "left till end of match");
            p2.sendTitle(ChatColor.DARK_GREEN + "20 Seconds!", "left till end of match");
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            /* DEBUG**/
            Bukkit.broadcastMessage("This message is shown after ten seconds");
            p1.sendTitle(ChatColor.GOLD + "10 Seconds!", "left till end of match");
            p2.sendTitle(ChatColor.GOLD + "10 Seconds!", "left till end of match");
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            /* DEBUG**/
            Bukkit.broadcastMessage("This message is shown after ten seconds");
            p1.sendTitle(ChatColor.RED + "All Done!", "GG! You got " + ChatColor.AQUA + pointsCounter.get(p1) + " Points!");
            p2.sendTitle(ChatColor.RED + "All Done!", "GG! You got " + ChatColor.AQUA + pointsCounter.get(p2) + " Points!");
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            /* DEBUG**/
            Bukkit.broadcastMessage("stopping");
            try {
                Stop();
            } catch (IOException | MaxChangedBlocksException | DataException e) {
                e.printStackTrace();
            }
        }, 15L);

    }

    public void Stop() throws IOException, DataException, MaxChangedBlocksException {
        Bukkit.broadcastMessage("stopped cool");
        // clear inventories
        PlayersPlaying.get(0).getInventory().clear();
        PlayersPlaying.get(1).getInventory().clear();

        // teleport back to lobby
        PlayersPlaying.get(0).teleport(m.getLobbyLocation());
        PlayersPlaying.get(1).teleport(m.getLobbyLocation());

        // remove from the public lists
        allPlayersPlaying.remove(PlayersPlaying.get(0));
        allPlayersPlaying.remove(PlayersPlaying.get(1));
        PlayersPlaying.remove(m);

        Reset(m);

    }

    public static void Reset(MapDef m) throws DataException, IOException, MaxChangedBlocksException {
        InternalSchematic.Load(m, m.getC1l().getWorld(), m.getC1l());
    }

    @EventHandler
    public void onEDeath(EntityDeathEvent event) {

        if (event.getEntity().getKiller() != null) {
            Player attacker = event.getEntity().getKiller();
            Player killed = (Player) event.getEntity();
            if (allPlayersPlaying.contains(attacker)) {
                //Do if player that died is inside a game.

                //prevent respawn screen
                killed.setHealth(20.0);
                killed.setFoodLevel(20);

                //increase points by 10 for kill
                EditPoints(attacker, 10);
                attacker.sendMessage(ChatColor.AQUA + "You Killed" + killed + "! +10 Points");


                //decrease points by 10 for kill
                EditPoints(killed, -10);
                killed.sendMessage(ChatColor.AQUA + "You Died! -10 Points");

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
            //removed check if player ------------------
            EditPoints(damager, 1);
            EditPoints(receiver, -1);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) throws IOException, DataException, MaxChangedBlocksException {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        //if first bed broken
        if (b.getLocation() == m.getSpawnOneLocation()) {
            if (p == p1) {
                //get second player in map of p and award points
                EditPoints(p1, 15);
                p.sendMessage(ChatColor.AQUA + "Bed Broken: 15 Points to you!");
                m.getSpawnOneLocation().getBlock().setType(Material.BED);

                MapDef thisMap = m;
                //reset map

                Reset(thisMap);

                //teleport back to spawn locations
                p1.teleport(thisMap.getSpawnOneLocation());
                p2.teleport(thisMap.getSpawnTwoLocation());
            }
        }

        //if second bed broken
        if (b.getLocation() == m.getSpawnTwoLocation()) {
            if (allPlayersPlaying.contains(p)) {
                //get first player in map of p and award points
                EditPoints(p1, 15);
                p.sendMessage(ChatColor.AQUA + "Bed Broken: 15 Points to you!");

                MapDef thisMap = m;
                //reset map

                Reset(thisMap);

                //teleport back to spawn locations
                p1.teleport(thisMap.getSpawnOneLocation());
                p2.teleport(thisMap.getSpawnTwoLocation());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) throws DataException, IOException, MaxChangedBlocksException {
        Player p = e.getPlayer();
        if (allPlayersPlaying.contains(p)) {
            Stop();
        }
    }
}

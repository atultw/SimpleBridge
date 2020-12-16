package io.github.atultw.gmsbridge;


import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Game {

    private static Main plugin;

    public Game(Main plugin) {
        Game.plugin = plugin;
    }

    static HashSet<Player> allPlayersPlaying;
    static HashMap<Player, Integer> pointsCounter = new HashMap<>();

    public void EditPoints(Player p, Integer n) {
        Integer oldPoints = pointsCounter.get(p);
        Integer newPoints = oldPoints + n;
        pointsCounter.put(p, newPoints);
    }

    public static void Start(Player p1, Player p2, MapDef m) throws InterruptedException, IOException, DataException, MaxChangedBlocksException {

        /**DEBUG**/Bukkit.broadcastMessage("start called");

        HashMap<Integer, Player> playersToSet = new HashMap<>();

        //SAVE THE SCHEMATIC FOR LATER
        InternalSchematic.New(m, m.getC1l().getWorld(), m.getC1l(), m.getC2l());

        //PUT THE PLAYERS INTO THE NEW HMAP
        playersToSet.put(1, p1);
        playersToSet.put(2, p2);
        GameMap.PlayersInGame.put(m, playersToSet);

        //let players know
        p1.sendMessage("You are now dueling " + ChatColor.AQUA + p2.getName() + ChatColor.WHITE + "! Let the game begin! To score points, jump into the goal at the other base.");
        p2.sendMessage("You are now dueling " + ChatColor.AQUA + p1.getName() + ChatColor.WHITE + "! Let the game begin! To score points, jump into the goal at the other base.");

        //teleport the players
        p1.teleport(m.getSpawnOneLocation());
        p2.teleport(m.getSpawnTwoLocation());

        //give initial items
        p1.getInventory().clear();
        p2.getInventory().clear();
        ItemStack[] startItems = {new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_SWORD)};
        p1.getInventory().addItem(startItems);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                /** DEBUG**/Bukkit.broadcastMessage("This message is shown after ten seconds");
                p1.sendTitle(ChatColor.BLUE + "50 Seconds!", "left till end of match");
                p2.sendTitle(ChatColor.BLUE + "50 Seconds!", "left till end of match");
            }
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                /** DEBUG**/Bukkit.broadcastMessage("This message is shown after ten seconds");
                p1.sendTitle(ChatColor.AQUA + "40 Seconds!", "left till end of match");
                p2.sendTitle(ChatColor.AQUA + "40 Seconds!", "left till end of match");
            }
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                /** DEBUG**/Bukkit.broadcastMessage("This message is shown after ten seconds");
                p1.sendTitle(ChatColor.GREEN + "30 Seconds!", "left till end of match");
                p2.sendTitle(ChatColor.GREEN + "30 Seconds!", "left till end of match");
            }
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                /** DEBUG**/Bukkit.broadcastMessage("This message is shown after ten seconds");
                p1.sendTitle(ChatColor.DARK_GREEN + "20 Seconds!", "left till end of match");
                p2.sendTitle(ChatColor.DARK_GREEN + "20 Seconds!", "left till end of match");
            }
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                /** DEBUG**/Bukkit.broadcastMessage("This message is shown after ten seconds");
                p1.sendTitle(ChatColor.GOLD + "10 Seconds!", "left till end of match");
                p2.sendTitle(ChatColor.GOLD + "10 Seconds!", "left till end of match");
            }
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                /** DEBUG**/Bukkit.broadcastMessage("This message is shown after ten seconds");
                p1.sendTitle(ChatColor.RED + "All Done!", "GG! You got " + ChatColor.AQUA + pointsCounter.get(p1) + " Points!");
                p2.sendTitle(ChatColor.RED + "All Done!", "GG! You got " + ChatColor.AQUA + pointsCounter.get(p2) + " Points!");
            }
        }, 200L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                /** DEBUG**/Bukkit.broadcastMessage("This message is shown after ten seconds");
                try {
                    Stop(p1, p2, m);
                } catch (IOException | MaxChangedBlocksException | DataException e) {
                    e.printStackTrace();
                }
            }
        }, 15L);

    }

    @EventHandler
    public void onEDeath(EntityDeathEvent event) {
        for (HashMap<Integer, Player> pl : GameMap.PlayersInGame.values()) {
            allPlayersPlaying.addAll(pl.values());
        }

        if (event.getEntity().getKiller() != null) {
            Player attacker = event.getEntity().getKiller();
            Player killed = (Player) event.getEntity();
            if (allPlayersPlaying.contains(attacker)){
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
                if (killed == GameMap.PlayersInGame.get(GameMap.getMapOfPlayer(killed)).get(0)) {
                    killed.teleport(GameMap.getMapOfPlayer(killed).getSpawnOneLocation());
                }

                if (killed == GameMap.PlayersInGame.get(GameMap.getMapOfPlayer(killed)).get(1)) {
                    killed.teleport(GameMap.getMapOfPlayer(killed).getSpawnTwoLocation());
                }
            }
        }

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        Player damager = (Player) e.getDamager();
        Player receiver = (Player) e.getEntity();
        //removed check if player ------------------
        EditPoints(damager, 1);
        EditPoints(receiver, -1);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) throws IOException, DataException, MaxChangedBlocksException {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        //if first bed broken
        if (b.getLocation() == GameMap.getMapOfPlayer(p).getSpawnOneLocation()) {
            if (allPlayersPlaying.contains(p)) {
                //get second player in map of p and award points
                EditPoints(GameMap.PlayersInGame.get(GameMap.getMapOfPlayer(p)).get(1), 15);
                p.sendMessage(ChatColor.AQUA + "Bed Broken: 15 Points to you!");
                GameMap.getMapOfPlayer(p).getSpawnOneLocation().getBlock().setType(Material.BED);

                MapDef thisMap = GameMap.getMapOfPlayer(p);
                //reset map

                Reset(thisMap);

                //teleport back to spawn locations
                GameMap.PlayersInGame.get(thisMap).get(0).teleport(thisMap.getSpawnOneLocation());
                GameMap.PlayersInGame.get(thisMap).get(1).teleport(thisMap.getSpawnTwoLocation());
            }
        }

        //if second bed broken
        if (b.getLocation() == GameMap.getMapOfPlayer(p).getSpawnTwoLocation()) {
            if (allPlayersPlaying.contains(p)){
                //get first player in map of p and award points
                EditPoints(GameMap.PlayersInGame.get(GameMap.getMapOfPlayer(p)).get(0), 15);
                p.sendMessage(ChatColor.AQUA + "Bed Broken: 15 Points to you!");

                MapDef thisMap = GameMap.getMapOfPlayer(p);
                //reset map

                Reset(thisMap);

                //teleport back to spawn locations
                GameMap.PlayersInGame.get(thisMap).get(0).teleport(thisMap.getSpawnOneLocation());
                GameMap.PlayersInGame.get(thisMap).get(1).teleport(thisMap.getSpawnTwoLocation());
            }
        }
    }

    public static void Stop(Player p1, Player p2, MapDef m) throws IOException, DataException, MaxChangedBlocksException {
        Bukkit.broadcastMessage("stopped cool");
        // clear inventories
        p1.getInventory().clear();
        p2.getInventory().clear();

        // teleport back to lobby
        p1.teleport(m.getLobbyLocation());
        p2.teleport(m.getLobbyLocation());

        // remove from the public lists
        allPlayersPlaying.remove(p1);
        allPlayersPlaying.remove(p2);
        GameMap.PlayersInGame.remove(m);

        Reset(m);

    }

    public static void Reset(MapDef m) throws DataException, IOException, MaxChangedBlocksException {
        InternalSchematic.Load(m, m.getC1l().getWorld(), m.getC1l());
    }
}

package io.github.atultw.gmsbridge;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Game {

    static HashSet<Player> allPlayersPlaying;
    static HashMap<Player, Integer> pointsCounter = new HashMap<>();

    public void EditPoints(Player p, Integer n) {
        Integer oldPoints = pointsCounter.get(p);
        Integer newPoints = oldPoints + n;
        pointsCounter.put(p, newPoints);
    }

    public static void Start(Player p1, Player p2, MapDef m) throws InterruptedException {

        HashMap<Integer, Player> playersToSet = new HashMap<>();

        //SAVE THE SCHEMATIC FOR LATER


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
        ItemStack[] startItems = {new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_SWORD)};
        p1.getInventory().addItem(startItems);

        Thread.sleep(10000);

        p1.sendTitle(ChatColor.BLUE + "50 Seconds!", "left till end of match", 1, 20, 1);
        p2.sendTitle(ChatColor.BLUE + "50 Seconds!", "left till end of match", 1, 20, 1);

        Thread.sleep(10000);

        p1.sendTitle(ChatColor.AQUA + "40 Seconds!", "left till end of match", 1, 20, 1);
        p2.sendTitle(ChatColor.AQUA + "40 Seconds!", "left till end of match", 1, 20, 1);

        Thread.sleep(10000);

        p1.sendTitle(ChatColor.GREEN + "30 Seconds!", "left till end of match", 1, 20, 1);
        p2.sendTitle(ChatColor.GREEN + "30 Seconds!", "left till end of match", 1, 20, 1);

        Thread.sleep(10000);

        p1.sendTitle(ChatColor.DARK_GREEN + "20 Seconds!", "left till end of match", 1, 20, 1);
        p2.sendTitle(ChatColor.DARK_GREEN + "20 Seconds!", "left till end of match", 1, 20, 1);


        Thread.sleep(10000);

        p1.sendTitle(ChatColor.GOLD + "10 Seconds!", "left till end of match", 1, 20, 1);
        p2.sendTitle(ChatColor.GOLD + "10 Seconds!", "left till end of match", 1, 20, 1);

        Thread.sleep(10000);

        p1.sendTitle(ChatColor.RED + "All Done!", "GG! You got " + ChatColor.AQUA + pointsCounter.get(p1) + " Points!", 1, 70, 1);
        p2.sendTitle(ChatColor.RED + "All Done!", "GG! You got " + ChatColor.AQUA + pointsCounter.get(p2) + " Points!", 1, 70, 1);

        Thread.sleep(500);

        Stop(p1, p2, m);
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
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block b = e.getBlock();

        //if first bed broken
        if (b.getLocation() == GameMap.getMapOfPlayer(p).getSpawnOneLocation()) {
            if (allPlayersPlaying.contains(p)){
                //get second player in map of p and award points
                EditPoints(GameMap.PlayersInGame.get(GameMap.getMapOfPlayer(p)).get(1), 15);
                p.sendMessage(ChatColor.AQUA + "Bed Broken: 15 Points to you!");
                GameMap.getMapOfPlayer(p).getSpawnOneLocation().getBlock().setType(Material.RED_BED);

                MapDef thisMap = GameMap.getMapOfPlayer(p);
                //reset map


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
                GameMap.getMapOfPlayer(p).getSpawnTwoLocation().getBlock().setType(Material.RED_BED);
                if (p == GameMap.PlayersInGame.get(GameMap.getMapOfPlayer(p)).get(1)) {
                    p.teleport(GameMap.getMapOfPlayer(p).getSpawnTwoLocation());
                }
            }
        }
    }

    public static void Stop(Player p1, Player p2, MapDef m) {

        // teleport back to lobby
        p1.teleport(m.getLobbyLocation());
        p2.teleport(m.getLobbyLocation());

        // remove from the public lists
        allPlayersPlaying.remove(p1);
        allPlayersPlaying.remove(p2);
        GameMap.PlayersInGame.get(m).remove(1);
        GameMap.PlayersInGame.get(m).remove(2);
    }
}

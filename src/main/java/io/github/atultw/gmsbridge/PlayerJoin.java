package io.github.atultw.gmsbridge;

// Handle sending player to map using gamemap class

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;


public class PlayerJoin {

    public static void JoinPlayer(Player p, MapDef m) throws InterruptedException {

        //add new players to waiting
        List<Player> playersInList = GameMap.PlayersWaiting.get(m);
        playersInList.add(p);
        GameMap.PlayersWaiting.put(m, playersInList);

        // player side stuff
        p.sendMessage("You entered the " + ChatColor.GREEN + m.getArenaName() + " arena! Wait a few moments for another player to join.");
        p.teleport(m.getCageLocation());

        //if players waiting in game less than two, after addition of this player p
        if (GameMap.PlayersWaiting.get(m).size() >= 2) {
            Player p1 = GameMap.PlayersWaiting.get(m).get(0);
            Player p2 = GameMap.PlayersWaiting.get(m).get(1);

            // actually start the game
            Game.Start(p1, p2, m);

            // remove the players from waiting
            GameMap.PlayersWaiting.get(m).remove(0);
            GameMap.PlayersWaiting.get(m).remove(0);
        }
    }

    // UNUSED METHOD
    /**public static void LeavePlayer(Player p, MapDef m){
        List<Player> phs = GameMap.PlayersWaiting.get(m);
        phs.remove(p);
        GameMap.PlayersWaiting.put(m, phs);
    }**/
}

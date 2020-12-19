package io.github.atultw.gmsbridge;

// Handle sending player to map using gamemap class

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PlayerJoin {
    // this method takes a player and map argument, then adds that player to the map.
    public static void JoinPlayer(Player p, MapDef m) throws InterruptedException, IOException, MaxChangedBlocksException, DataException {

        //add new players to waiting
        List<Player> pl = new ArrayList<>(Maps.PlayersWaiting.get(m));
        pl.add(p);
        Maps.PlayersWaiting.put(m, pl);

        // player side stuff
        p.sendMessage("You entered the " + ChatColor.GREEN + m.getArenaName() + ChatColor.WHITE + " arena! Wait a few moments for another player to join.");
        //Bukkit.broadcastMessage(m.getCageLocation().toString());
        p.teleport(m.getCageLocation());

        //if players waiting in game greater than or equal to two, after addition of this player p
        if (Maps.PlayersWaiting.get(m).size() >= 2) {
            Player p1 = Maps.PlayersWaiting.get(m).get(0);
            Player p2 = Maps.PlayersWaiting.get(m).get(1);
            /**DEBUG**/p1.sendMessage("youre in");
            /**DEBUG**/p2.sendMessage("youre in");

            //check if the map is already in use

            // actually start the game
            Game.Start(p1, p2, m);

            // remove the players from waiting
            Maps.PlayersWaiting.get(m).remove(0);
            Maps.PlayersWaiting.get(m).remove(0);
        }
    }

    // UNUSED METHOD
    /**public static void LeavePlayer(Player p, MapDef m){
     List<Player> phs = GameMap.PlayersWaiting.get(m);
     phs.remove(p);
     GameMap.PlayersWaiting.put(m, phs);
     }**/
}

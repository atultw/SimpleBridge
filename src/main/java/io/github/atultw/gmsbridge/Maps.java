package io.github.atultw.gmsbridge;

// Associate players with maps

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;

public class Maps {

    public static HashSet<MapDef> AllMaps;
    public static HashMap<Player, Game> PlayerToGame;


    /**
     * public <K, V> Set<K> getKeys(Map<K, V> map, V value) {
     * Set<K> keys = new HashSet<>();
     * for (Map.Entry<K, V> entry : map.entrySet()) {
     * if (entry.getValue().equals(value)) {
     * keys.add(entry.getKey());
     * }
     * }
     * return keys;
     * }
     **/


    public Game getGameOfPlayer(Player p) {
        return PlayerToGame.get(p);
    }

}

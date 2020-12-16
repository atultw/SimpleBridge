package io.github.atultw.gmsbridge;

// Associate players with maps

import org.bukkit.entity.Player;

import java.util.*;

public class GameMap {

    // use to find who is waiting in each map
    static HashMap<MapDef, List<Player>> PlayersWaiting = new HashMap<>();

    // use to find who is playing in maps
    static HashMap<MapDef, HashMap<Integer, Player>> PlayersInGame = new HashMap<MapDef, HashMap<Integer, Player>>();


    public <K, V> Set<K> getKeys(Map<K, V> map, V value) {
        Set<K> keys = new HashSet<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    public static MapDef getMapOfPlayer(Player p) {
        MapDef mapout = null;

        for (Map.Entry<MapDef, List<Player>> entry : PlayersWaiting.entrySet()) {
            if (PlayersWaiting.get(entry).contains(p)){
                mapout = entry.getKey();
            }
        }

        return mapout;
    }

    public List<Player> getPlayersOfMap (MapDef m) {
        return PlayersWaiting.get(m);
    }

}

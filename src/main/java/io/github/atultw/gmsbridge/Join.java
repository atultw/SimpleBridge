package io.github.atultw.gmsbridge;

import com.sk89q.worldedit.world.DataException;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Join {
    static HashMap<MapDef, List<Player>> Waiting = new HashMap<>();

    public static void Initialize(Player p, MapDef m) throws IOException, DataException {
        //add to list
        if (!Waiting.get(m).contains(p)) {
            Waiting.get(m).add(p);
        }

        p.teleport(m.getCageLocation());
        //if there are now 2 players send the first two into game
        if (Waiting.get(m).size() >= 2) {
            if (!(Game.MapsInUse.contains(m))) {
                Game g = new Game(Waiting.get(m).get(0), Waiting.get(m).get(1), m);
                g.Start();
            }
        }
    }

    // SEE MAINLISTENER FOR EVENT LISTENERS
}

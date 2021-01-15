package io.github.atultw.gmsbridge;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkHandler {
    public static void launch(Location l, Color color) {
        Firework fire = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(color)
                .trail(true)
                .withFade(Color.BLUE)
                .build();

        FireworkMeta meta = fire.getFireworkMeta();
        meta.setPower(1);
        meta.addEffect(effect);
        fire.setFireworkMeta(meta);
    }
}

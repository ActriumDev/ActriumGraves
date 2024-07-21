package xyz.actrium.graves.cooldown;

import java.util.HashMap;
import org.bukkit.entity.Player;

public class BasicCooldown {
    private final HashMap<Player, Long> cooldownMap;
    private final int cooldown;

    public BasicCooldown(int cooldown) {
        this.cooldown = cooldown;
        this.cooldownMap = new HashMap();
    }

    public boolean onCooldown(Player player) {
        if (!this.cooldownMap.containsKey(player)) {
            return false;
        } else {
            long time = this.cooldownMap.get(player);
            if (System.currentTimeMillis() - time >= (long)this.cooldown) {
                this.cooldownMap.remove(player);
            }

            return this.cooldownMap.containsKey(player);
        }
    }

    public void updatePlayer(Player player) {
        this.cooldownMap.put(player, System.currentTimeMillis());
    }
}
package com.github.braresa.autoclick.types;

import com.github.braresa.autoclick.AutoClick;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlayerAutoClickStatus {
    public Boolean isEnabled;
    public final Player player;
    private int count;

    public PlayerAutoClickStatus(Player targetPlayer) {
        player = targetPlayer;
        isEnabled = true;
        count = 1;
    }

    private int getTicksInterval() {
        FileConfiguration config = AutoClick.instance.getConfig();
            Map<String, ?> groupMap = config.getConfigurationSection("permission_groups").getValues(true);
            for(Map.Entry<String, ?> entry : groupMap.entrySet()) {
                String permission = entry.getKey();
                Integer cps = (Integer) entry.getValue();
                if(player.hasPermission(permission.replace("_", "."))) {
                    return cps;
                }
            }
            return 20;
    }

    public Boolean toggle() {
        isEnabled = !isEnabled;
        return isEnabled;
    }

    public Boolean click() {
        if(!isEnabled) {
            return false;
        }
        if(count >= getTicksInterval()) {
            count = 1;
            return true;
        } else {
            count += 1;
            return false;
        }
    }
}

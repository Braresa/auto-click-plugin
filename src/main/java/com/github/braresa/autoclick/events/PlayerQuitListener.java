package com.github.braresa.autoclick.events;

import com.github.braresa.autoclick.AutoClick;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        AutoClick.instance.removePlayerFromMap(event.getPlayer());
    }
}

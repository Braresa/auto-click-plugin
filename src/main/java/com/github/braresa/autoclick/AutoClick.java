package com.github.braresa.autoclick;

import com.github.braresa.autoclick.commands.AutoClickCommand;
import com.github.braresa.autoclick.events.PlayerQuitListener;
import com.github.braresa.autoclick.types.PlayerAutoClickStatus;
import com.github.braresa.autoclick.util.DamagePotencial;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public final class AutoClick extends JavaPlugin {

    public static AutoClick instance;
    private Map<Player, PlayerAutoClickStatus> autoClickPlayersStatus;
    private BukkitRunnable scheduler;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        autoClickPlayersStatus = new HashMap<>();
        createConfig();
        scheduler = new AutoClickSchedule();
        scheduler.runTaskTimer(this, 1, 1);
        this.getCommand("autoclick").setExecutor(new AutoClickCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getLogger().info("Plugin de auto click habilitado!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("Plugin de auto click desabilitado!");
        autoClickPlayersStatus.clear();
        scheduler.cancel();
    }

    private void createConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        if(!configFile.exists()) {
            saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        this.getLogger().info("Arquivo de configuração criado com sucesso!");
    }

    public void saveConfig() {
        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (Exception error) {
            this.getLogger().warning("Não foi possível salvar a configuração.");
            this.getLogger().warning(error.toString());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Boolean togglePlayerAutoClick(Player player) {
        if(autoClickPlayersStatus.containsKey(player)) {
            return autoClickPlayersStatus.get(player).toggle();
        } else {
            autoClickPlayersStatus.put(player, new PlayerAutoClickStatus(player));
            return true;
        }
    }

    public void removePlayerFromMap(Player player) {
        autoClickPlayersStatus.remove(player);
    }

    private class AutoClickSchedule extends BukkitRunnable {
        @Override
        public void run() {
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            for(Player player : onlinePlayers) {
                if(autoClickPlayersStatus.containsKey(player) && !player.isDead() && autoClickPlayersStatus.get(player).click()) {
                    List<Entity> nearbyEntities = player.getNearbyEntities(3, 3, 3);
                    for (Entity entity : nearbyEntities) {
                        if(entity instanceof LivingEntity && !(entity instanceof Player)) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            double damagePotencial = DamagePotencial.getDamagePotencial(player);
                            livingEntity.damage(damagePotencial, player);
                        }
                    }
                }
            }
        }
    }
}

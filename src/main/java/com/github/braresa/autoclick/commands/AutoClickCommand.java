package com.github.braresa.autoclick.commands;

import com.github.braresa.autoclick.AutoClick;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AutoClickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Você precisa ser um jogador para executar este comando!");
            return true;
        }

        Player player = (Player) commandSender;

        if(!player.hasPermission(AutoClick.instance.getConfig().getString("REQUIRED_PERMISSION"))) {
            commandSender.sendMessage(ChatColor.RED + "Você não tem permissão para executar este comando!");
            return true;
        }

        Boolean toggleStatus = AutoClick.instance.togglePlayerAutoClick(player);

        if(toggleStatus) {
            player.sendMessage(ChatColor.GREEN + "Auto Clicker ativado!");
        } else {
            player.sendMessage(ChatColor.RED + "Auto Clicker desativado!");
        }

        return true;
    }
}

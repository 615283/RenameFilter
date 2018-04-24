package com.georlegacy.general.renamefilter.commands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.georlegacy.general.renamefilter.RenameFilter;

public class RFCommand implements CommandExecutor {

    private RenameFilter plugin;
    public RFCommand(RenameFilter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length==1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&4RenameFilter&e] &aConfig has successfully been reloaded."));
            return true;
        }
        else {
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 0);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&4RenameFilter&e] &cIncorrect command usage!"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[&4RenameFilter&e] &c/rf reload - reloads config.yml"));
            return true;
        }
    }

}

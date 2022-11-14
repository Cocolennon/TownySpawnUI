package me.senkoco.townyspawnui.commands;

import me.senkoco.townyspawnui.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandSetDefaultItem implements CommandExecutor {
    static Plugin plugin = Bukkit.getPluginManager().getPlugin("TownySpawnUI");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (plugin != null) {
            if (plugin instanceof Main) {
                Main main = (Main) plugin;
            }
        }

        if(!sender.hasPermission("townyspawnui.set.default.item")) {
            sender.sendMessage(ChatColor.RED + "You can't do that!");
            return false;
        }
        if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return false;
        }

        Material material;
        try {
            material = Material.valueOf(args[0].toUpperCase());
        }catch(IllegalArgumentException e){
            sender.sendMessage(ChatColor.RED + "Please provide a valid item or block name!\n" + ChatColor.RED + "Example: nether_star (Insensitive to case, spaces must be replaced by underscores.");
            return false;
        }
        plugin.getConfig().set("menu.defaultItem", args[0].toUpperCase());
        sender.sendMessage(ChatColor.GREEN + "The default item for towns and nations in the menu now is " + material.name());
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: /townyadmin set default-menu-item <item-name>\n" + ChatColor.RED + "<> = Mandatory");
    }
}

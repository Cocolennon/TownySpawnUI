package me.senkoco.townyspawnui.commands;

import me.senkoco.townyspawnui.utils.menu.NationUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;
import java.util.List;

public class CommandSpawnUI implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;

        if (!sender.hasPermission("townyspawnui.menu.open")) {
            sender.sendMessage(ChatColor.RED + "You can't do that!");
            return false;
        }

        List<Inventory> inventories = new LinkedList<Inventory>(NationUtil.getPages());
        p.openInventory(inventories.get(0));

        return true;
    }
}

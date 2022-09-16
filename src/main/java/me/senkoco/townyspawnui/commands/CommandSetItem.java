package me.senkoco.townyspawnui.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.senkoco.townyspawnui.utils.metadata.MetaData.setBlockInMenu;

public class CommandSetItem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
            return false;
        }else if(args[0].equalsIgnoreCase("nation")){
            Resident res = TownyAPI.getInstance().getResident((Player)sender);
            if(!res.hasNation()){
                sender.sendMessage(ChatColor.RED + "You aren't in a nation!");
                return false;
            }else if(!res.isKing()){
                sender.sendMessage(ChatColor.RED + "You aren't the king of your nation!");
                return false;
            }else if(args.length == 1){
                sender.sendMessage(ChatColor.RED + "Please provide an item or block name to use");
                return false;
            }

            Material material;
            try {
                material = Material.valueOf(args[1]);
            }catch(IllegalArgumentException e){
                sender.sendMessage(ChatColor.RED + "Please provide a valid item or block name!\n" + ChatColor.RED + "Example: NETHER_STAR (must be full caps, and spaces replaced by underscores.");
                return false;
            }

            setBlockInMenu(res.getNationOrNull(), material.name());
            sender.sendMessage(ChatColor.GREEN + "Your nation's item/block in the menu now is: " + material.name());
        }else if(args[0].equalsIgnoreCase("town")){
            Resident res = TownyAPI.getInstance().getResident((Player)sender);
            if(!res.hasTown()){
                sender.sendMessage(ChatColor.RED + "You aren't in a town!");
                return false;
            }else if(!res.isMayor()){
                sender.sendMessage(ChatColor.RED + "You aren't the mayor of your town!");
                return false;
            }else if(args.length == 1){
                sender.sendMessage(ChatColor.RED + "Please provide an item or block name to use");
                return false;
            }

            Material material;
            try {
                material = Material.valueOf(args[1]);
            }catch(IllegalArgumentException e){
                sender.sendMessage(ChatColor.RED + "Please provide a valid item or block name!\n" + ChatColor.RED + "Example: NETHER_STAR (must be full caps, and spaces replaced by underscores.");
                return false;
            }

            setBlockInMenu(res.getTownOrNull(), material.name());
            sender.sendMessage(ChatColor.GREEN + "Your town's item/block in the menu now is: " + material.name());
        }
        return false;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: /tsm-set-item <nation/town> <item-name>\n" + ChatColor.RED + "<> = Mandatory");
    }
}

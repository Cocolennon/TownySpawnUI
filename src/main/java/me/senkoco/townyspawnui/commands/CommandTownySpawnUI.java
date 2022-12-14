package me.senkoco.townyspawnui.commands;

import me.senkoco.townyspawnui.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

public class CommandTownySpawnUI implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> info = new LinkedList<String>();
        info.add("§c§l=========================");
        info.add("§6§lTownySpawnUI v" + Main.getVersion());
        info.add("§6§lMade for Towny v0.98.4.5");
        if(Main.getUsingOldVersion()){
            info.add("§6§lAn update is available!");
        }else{
            info.add("§6§lYou're using the latest version");
        }
        info.add("§c§l=========================");

        sender.sendMessage(info.get(0) + "\n" + info.get(1) + "\n" + info.get(2) + "\n" + info.get(3) + "\n" + info.get(4));
        return true;
    }
}

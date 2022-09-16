package me.senkoco.townyspawnui;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.exceptions.KeyAlreadyRegisteredException;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import me.senkoco.townyspawnui.commands.CommandSetItem;
import me.senkoco.townyspawnui.commands.CommandSpawnUI;
import me.senkoco.townyspawnui.utils.commands.SimpleCommand;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("Plugin enabled!");
        registerCommands();
        getServer().getPluginManager().registerEvents(new MainListener(), this);
    }

    private void createCommand(SimpleCommand command) {
        CraftServer server = (CraftServer)getServer();
        server.getCommandMap().register(getName(), command);
    }

    private void registerCommands(){
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "spawn-menu", new CommandSpawnUI());
        createCommand(new SimpleCommand("towny-spawn-menu","Open the menu to travel to other towns", new CommandSpawnUI(),"spawn-menu", "town-list", "townlist", "tsm"));
        createCommand(new SimpleCommand("tsm-set-item", "Set the item that represents your nation/town", new CommandSetItem()));
    }
}

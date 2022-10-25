package me.senkoco.townyspawnui;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import me.senkoco.townyspawnui.commands.CommandSetItemTown;
import me.senkoco.townyspawnui.commands.CommandSetItemNation;
import me.senkoco.townyspawnui.commands.CommandSpawnUI;
import me.senkoco.townyspawnui.utils.commands.SimpleCommand;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import static com.palmergames.bukkit.towny.TownyCommandAddonAPI.addSubCommand;

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
        addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "spawn-menu", new CommandSpawnUI());
        addSubCommand(TownyCommandAddonAPI.CommandType.TOWN_SET, "menu-item", new CommandSetItemTown());
        addSubCommand(TownyCommandAddonAPI.CommandType.NATION_SET, "menu-item", new CommandSetItemNation());
        createCommand(new SimpleCommand("towny-spawn-menu","Open the menu to travel to other towns", new CommandSpawnUI(),"spawn-menu", "town-list", "townlist", "tsm"));
    }
}

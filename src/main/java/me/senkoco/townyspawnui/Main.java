package me.senkoco.townyspawnui;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import me.senkoco.townyspawnui.commands.CommandSetDefaultItem;
import me.senkoco.townyspawnui.commands.CommandSetItemTown;
import me.senkoco.townyspawnui.commands.CommandSetItemNation;
import me.senkoco.townyspawnui.commands.CommandSpawnUI;
import me.senkoco.townyspawnui.utils.UpdateChecker;
import me.senkoco.townyspawnui.utils.commands.SimpleCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.palmergames.bukkit.towny.TownyCommandAddonAPI.addSubCommand;

public class Main extends JavaPlugin {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        new UpdateChecker(this, 105225).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                getLogger().info("You are using an older version of TownySpawnUI, please update to version " + version);
            }
        });
        setUpConfig();
        getLogger().info("Plugin enabled!");
        registerCommandsAndListeners();
    }

    private void createCommand(SimpleCommand command) {
        CraftServer server = (CraftServer)getServer();
        server.getCommandMap().register(getName(), command);
    }

    private void registerCommandsAndListeners(){
        addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "spawn-menu", new CommandSpawnUI());
        addSubCommand(TownyCommandAddonAPI.CommandType.TOWN_SET, "menu-item", new CommandSetItemTown());
        addSubCommand(TownyCommandAddonAPI.CommandType.NATION_SET, "menu-item", new CommandSetItemNation());
        addSubCommand(TownyCommandAddonAPI.CommandType.TOWNYADMIN_SET, "default-menu-item", new CommandSetDefaultItem());
        createCommand(new SimpleCommand("towny-spawn-menu","Open the menu to travel to other towns", new CommandSpawnUI(),"spawn-menu", "town-list", "townlist", "tsm"));

        getServer().getPluginManager().registerEvents(new MainListener(), this);
    }

    public void setUpConfig(){
        config.addDefault("menu.defaultItem", "RED_STAINED_GLASS_PANE");
        config.options().copyDefaults(true);
        saveConfig();
    }
}

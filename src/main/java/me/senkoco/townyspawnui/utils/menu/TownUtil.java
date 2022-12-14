package me.senkoco.townyspawnui.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import me.senkoco.townyspawnui.Main;
import me.senkoco.townyspawnui.events.PlayerTeleportToTown;
import me.senkoco.townyspawnui.utils.metadata.MetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.bukkit.Bukkit.getPluginManager;

public class TownUtil {
    static Plugin plugin = Bukkit.getPluginManager().getPlugin("TownySpawnUI");

    public static void teleportToTown(Player player, String townName){
        Town town = TownyAPI.getInstance().getTown(townName);
        if(!player.hasPermission("townyspawnui.menu.teleport")) {
            player.sendMessage(ChatColor.RED + "You can't do that!");
            return;
        }
        if(!town.isPublic()) return;
        player.performCommand("t spawn " + townName + " -ignore");
        PlayerTeleportToTown playerTeleportToTown = new PlayerTeleportToTown(player, town);
        getPluginManager().callEvent(playerTeleportToTown);
    }

    public static List<Inventory> getPages(Nation nation){
        if (plugin != null) {
            if (plugin instanceof Main) {
                Main main = (Main) plugin;
            }
        }

        List<Town> allTownsInNation = new LinkedList<Town>(nation.getTowns());
        int townCount = allTownsInNation.size();

        int townsInPage = 0;
        int inventorySlots = 7;
        List<Inventory> inventories = new LinkedList<Inventory>();
        
        for(int pageNumber = 0; pageNumber < getPagesCount(townCount)+1; pageNumber++){
            Inventory newPage = Bukkit.createInventory(null, 27, "§6§l" + nation.getName() + "§f§l: §3§lTowns (" + (pageNumber+1) + ")");
            List<Town> towns = new LinkedList<Town>();
            if(pageNumber == getPagesCount(townCount))
                inventorySlots = townCount - townsInPage;
            for(int j = 0; j < inventorySlots; j++){
                towns.add(allTownsInNation.get(townsInPage));
                townsInPage++;
            }
            int menuSlot = 10;
            for(int k = 0; k < towns.size(); k++){
                Town town = towns.get(k);
                Material material = Material.valueOf(plugin.getConfig().getString("menu.defaultItem"));
                if(MetaDataUtil.hasMeta(town, MetaData.blockInMenu)){
                    material = Material.valueOf(MetaData.getBlockInMenu(town));
                }
                newPage.setItem(menuSlot, MainUtil.getItem(material, "§c§l" + town.getName(), town.getName(), setGlobalLore(town)));
                menuSlot++;
            }
            if(getPagesCount(townCount) > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, MainUtil.getItem(Material.ARROW, "§6§lNext Page", "" + (pageNumber + 1)));
                }else if(pageNumber == getPagesCount(townCount)){
                    newPage.setItem(21, MainUtil.getItem(Material.ARROW, "§6§lPrevious Page", "" + (pageNumber - 1)));
                }else{
                    newPage.setItem(23, MainUtil.getItem(Material.ARROW, "§6§lNext Page", "" + (pageNumber + 1)));
                    newPage.setItem(21, MainUtil.getItem(Material.ARROW, "§6§lPrevious Page", "" + (pageNumber - 1)));
                }
            }
            newPage.setItem(22, MainUtil.getItem(Material.ARROW, "§6§lBack to Nations", "0"));
            newPage.setItem(26, MainUtil.getItem(Material.BLACK_STAINED_GLASS_PANE, " ", nation.getName()));
            MainUtil.fillEmpty(newPage, MainUtil.getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "townMenu"));
            inventories.add(newPage);
        }
        return inventories;
    }

    public static int getPagesCount(int townsCount){
        return (int) townsCount / 7;
    }

    public static ArrayList<String> setGlobalLore(Town town){
        String spawnCost = String.valueOf(town.getSpawnCost());
        if(!town.isPublic()) { spawnCost = "Private"; }

        ArrayList<String> itemlore = new ArrayList<>();
        itemlore.add("§6§lNation§f§l: §3§l" + town.getNationOrNull().getName());
        itemlore.add("§6§lMayor§f§l: §2§l" + town.getMayor().getName());
        itemlore.add("§6§lResidents§f§l: §d§l" + town.getResidents().size());
        itemlore.add("§6§lSpawn Cost§f§l: §c§l" + spawnCost);
        return itemlore;
    }
}

package me.senkoco.townyspawnui.utils.menu;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import me.senkoco.townyspawnui.Main;
import me.senkoco.townyspawnui.utils.metadata.MetaData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.valueOf;

public class NationUtil {
    static Plugin plugin = Bukkit.getPluginManager().getPlugin("TownySpawnUI");
    static int menuSlot = 1;
    public static ItemStack noNation = MainUtil.getItem(Material.BLUE_STAINED_GLASS_PANE, "§c§lNation-less Towns", "noNation");
    public static ItemStack notPublic = MainUtil.getItem(Material.PURPLE_STAINED_GLASS_PANE, "§c§lPrivate Towns", "notPublic");

    public static List<Inventory> getPages(){
        if (plugin != null) {
            if (plugin instanceof Main) {
                Main main = (Main) plugin;
            }
        }

        List<Nation> allNations = new LinkedList<Nation>(TownyUniverse.getInstance().getNations());
        int nationCount = TownyUniverse.getInstance().getNations().size();

        int nationsCount = 0;
        int inventorySlots = 7;
        List<Inventory> inventories = new LinkedList<Inventory>();

        for(int pageNumber = 0; pageNumber < getPagesCount(nationCount)+1; pageNumber++){
            Inventory newPage = Bukkit.createInventory(null, 27, "§6§lTowny§f§l: §3§lNations (" + (pageNumber+1) + ")");
            List<Nation> nations = new LinkedList<Nation>();
            if(pageNumber == getPagesCount(nationCount))
                inventorySlots = nationCount - nationsCount;
            for(int j = 0; j < inventorySlots; j++){
                nations.add(allNations.get(nationsCount));
                nationsCount++;
            }
            int menuSlot = 10;
            for(int k = 0; k < nations.size(); k++){
                Nation nation = nations.get(k);
                Material material = Material.valueOf(plugin.getConfig().getString("menu.defaultItem"));
                if(MetaDataUtil.hasMeta(nation, MetaData.blockInMenu)){
                    material = Material.valueOf(MetaData.getBlockInMenu(nation));
                }
                newPage.setItem(menuSlot, MainUtil.getItem(material, "§c§l" + nation.getName(), nation.getName(), setGlobalLore(nation)));
                menuSlot++;
            }
            addNoNationsItems(newPage);
            addPrivatesItems(newPage);
            if(getPagesCount(nationCount) > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, MainUtil.getItem(Material.ARROW, "§6§lNext Page", "" + (pageNumber + 1)));
                }else if(pageNumber == getPagesCount(nationCount)){
                    newPage.setItem(21, MainUtil.getItem(Material.ARROW, "§6§lPrevious Page", "" + (pageNumber - 1)));
                }else{
                    newPage.setItem(23, MainUtil.getItem(Material.ARROW, "§6§lNext Page", "" + (pageNumber + 1)));
                    newPage.setItem(21, MainUtil.getItem(Material.ARROW, "§6§lPrevious Page", "" + (pageNumber - 1)));
                }
            }
            MainUtil.fillEmpty(newPage, MainUtil.getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "nationMenu"));
            inventories.add(newPage);
        }
        return inventories;
    }

    public static void addNoNationsItems(Inventory inv){
        if(TownyAPI.getInstance().getTownsWithoutNation().size() != 0){
            inv.setItem(22, noNation);
        }
    }

    public static void addPrivatesItems(Inventory inv){
        int privateTownsCount = 0;
        for(int j = 0; j < TownyAPI.getInstance().getTowns().size(); j++){
            if(!TownyAPI.getInstance().getTowns().get(j).isPublic()){
                privateTownsCount++;
            }
        }

        if(privateTownsCount != 0){
            inv.setItem(18, notPublic);
        }
    }

    public static ArrayList<String> setGlobalLore(Nation nation){
        ArrayList<String> itemlore = new ArrayList<>();
        itemlore.add("§6§lLeader§f§l: §3§l" + nation.getKing().getName());
        itemlore.add("§6§lCapital§f§l: §2§l" + nation.getCapital().getName());
        itemlore.add("§6§lTowns§f§l: §9§l" + nation.getTowns().size());
        itemlore.add("§6§lTotal Residents§f§l: §d§l" + nation.getResidents().size());
        return itemlore;
    }

    public static int getPagesCount(int nationsCount){
        return (int) nationsCount / 7;
    }

    public static void openTownsOfNation(InventoryClickEvent event, ItemStack current, Player player, boolean isTownMenu, Nation nation) {
        event.setCancelled(true);
        if (current == null || !current.getItemMeta().hasLocalizedName() || current.getItemMeta().getLocalizedName().equals("nationMenu"))
            return;
        if(current.getItemMeta().getDisplayName().equals("§6§lNext Page") || current.getItemMeta().getDisplayName().equals("§6§lPrevious Page")){
            if(!isTownMenu){
                MainUtil.openInventory(player, Integer.parseInt(current.getItemMeta().getLocalizedName()), NationUtil.getPages());
            }else{
                MainUtil.openInventory(player, Integer.parseInt(current.getItemMeta().getLocalizedName()), TownUtil.getPages(nation));
            }
            return;
        }
        if(current.getItemMeta().getDisplayName().equals("§6§lBack to Nations")){
            MainUtil.openInventory(player, Integer.parseInt(current.getItemMeta().getLocalizedName()), NationUtil.getPages());
        }
        MainUtil.openInventory(player, 0, TownUtil.getPages(TownyAPI.getInstance().getNation(current.getItemMeta().getLocalizedName())));
    }

    public static void openNationlessTowns(InventoryClickEvent event, Player player){
        event.setCancelled(true);
        Inventory inv = Bukkit.createInventory(null, 27, "§6§lNation-less: §3§lTowns");
        menuSlot = 1;
        for(int j = 0; j < TownyAPI.getInstance().getTownsWithoutNation().size(); j++){
            Town town = TownyAPI.getInstance().getTownsWithoutNation().get(j);
            if(town.isPublic()){
                Material material = Material.RED_STAINED_GLASS_PANE;
                if(MetaDataUtil.hasMeta(town, MetaData.blockInMenu)){
                    material = Material.valueOf(MetaData.getBlockInMenu(town));
                }
                ArrayList<String> itemlore = new ArrayList<>();
                itemlore.add("§6§lMayor§f§l: §2§l" + town.getMayor().getName());
                itemlore.add("§6§lResidents§f§l: §d§l" + town.getResidents().size());
                itemlore.add("§6§lSpawn Cost§f§l: §c§l" + town.getSpawnCost());
                inv.setItem(menuSlot, MainUtil.getItem(material, "§c§l" + town.getFormattedName(), town.getName(), itemlore));
                menuSlot++;
            }
        }
        inv.setItem(22, MainUtil.getItem(Material.ARROW, "§6§lBack to Nations", "1"));
        MainUtil.fillEmpty(inv, MainUtil.townFiller);
        player.openInventory(inv);
    }

    public static void openPrivateTowns(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        Inventory inv = Bukkit.createInventory(null, 27, "§6§lPrivate Towns");
        menuSlot = 1;
        for(int j = 0; j < TownyAPI.getInstance().getTowns().size(); j++){
            Town town = TownyAPI.getInstance().getTowns().get(j);
            if(!town.isPublic()){
                Material material = Material.RED_STAINED_GLASS_PANE;
                if(MetaDataUtil.hasMeta(town, MetaData.blockInMenu)){
                    material = Material.valueOf(MetaData.getBlockInMenu(town));
                }
                ArrayList<String> itemlore = new ArrayList<>();
                if(town.hasNation()) itemlore.add("§6§lNation§f§l: §3§l" + town.getNationOrNull().getName());
                itemlore.add("§6§lMayor§f§l: §2§l" + town.getMayor().getName());
                itemlore.add("§6§lResidents§f§l: §d§l" + town.getResidents().size());
                itemlore.add("§6§lSpawn Cost§f§l: §c§l" + town.getSpawnCost());
                inv.setItem(menuSlot, MainUtil.getItem(material, "§c§l" + town.getFormattedName(), town.getName(), itemlore));
                menuSlot++;
            }
        }
        inv.setItem(22, MainUtil.getItem(Material.ARROW, "§6§lBack to Nations", "1"));
        MainUtil.fillEmpty(inv, MainUtil.townFiller);
        player.openInventory(inv);
    }
}

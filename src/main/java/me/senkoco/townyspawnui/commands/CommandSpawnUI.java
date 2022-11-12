package me.senkoco.townyspawnui.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import me.senkoco.townyspawnui.utils.metadata.MetaData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class CommandSpawnUI implements CommandExecutor {
    static ItemStack noNation = getItem(Material.BLUE_STAINED_GLASS_PANE, "§c§lNation-less Towns", "noNation");
    static ItemStack notPublic = getItem(Material.PURPLE_STAINED_GLASS_PANE, "§c§lPrivate Towns", "notPublic");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;

        if(!sender.hasPermission("townyspawnui.menu.open")) {
            sender.sendMessage(ChatColor.RED + "You can't do that!");
            return false;
        }

        List<Inventory> inventories = new LinkedList<Inventory>(getPages());
        p.openInventory(inventories.get(0));

        return true;
    }

    public static void openInventory(Player player, int page) {
        List<Inventory> inventories = new LinkedList<Inventory>(getPages());
        player.openInventory(inventories.get(page));
    }

    public static ItemStack getItem(Material material, String newName, String localizedName){
        ItemStack it = new ItemStack(material, 1);
        ItemMeta itM = it.getItemMeta();
        if(newName != null) itM.setDisplayName(newName);
        if(localizedName != null) itM.setLocalizedName(localizedName);
        it.setItemMeta(itM);
        return it;
    }

    public static ItemStack getItem(Material material, String newName, String localizedName, ArrayList<String> itemlore){
        ItemStack it = new ItemStack(material, 1);
        ItemMeta itM = it.getItemMeta();
        if(newName != null) itM.setDisplayName(newName);
        if(localizedName != null) itM.setLocalizedName(localizedName);
        if(itemlore != null) itM.setLore(itemlore);
        it.setItemMeta(itM);
        return it;
    }

    public static void fillEmpty(Inventory inv, ItemStack item){
        for(int i = 0; i < inv.getSize(); i++){
            if(inv.getItem(i) == null){
                inv.setItem(i, item);
            }
        }
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

    public static void setGlobalLore(ArrayList<String> itemlore, Nation nation){
        itemlore.add("§6§lLeader§f§l: §3§l" + nation.getKing().getName());
        itemlore.add("§6§lCapital§f§l: §2§l" + nation.getCapital().getName());
        itemlore.add("§6§lTowns§f§l: §9§l" + nation.getTowns().size());
        itemlore.add("§6§lTotal Residents§f§l: §d§l" + nation.getResidents().size());
    }

    public static int getPagesCount(int nationsCount){
        return (int) nationsCount / 7;
    }

    public static List<Inventory> getPages(){
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
                Material material = Material.RED_STAINED_GLASS_PANE;
                if(MetaDataUtil.hasMeta(nation, MetaData.blockInMenu)){
                    material = Material.valueOf(MetaData.getBlockInMenu(nation));
                }
                ArrayList<String> itemlore = new ArrayList<>();
                setGlobalLore(itemlore, nation);
                newPage.setItem(menuSlot, getItem(material, "§c§l" + nation.getName(), nation.getName(), itemlore));
                menuSlot++;
            }
            addNoNationsItems(newPage);
            addPrivatesItems(newPage);
            if(getPagesCount(nationCount) > 0){
                if(pageNumber == 0){
                    newPage.setItem(23, getItem(Material.ARROW, "§6§lNext Page", "" + (pageNumber + 1)));
                }else if(pageNumber == getPagesCount(nationCount)){
                    newPage.setItem(21, getItem(Material.ARROW, "§6§lPrevious Page", "" + (pageNumber - 1)));
                }else{
                    newPage.setItem(23, getItem(Material.ARROW, "§6§lNext Page", "" + (pageNumber + 1)));
                    newPage.setItem(21, getItem(Material.ARROW, "§6§lPrevious Page", "" + (pageNumber - 1)));
                }
            }
            fillEmpty(newPage, getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "nationMenu"));
            inventories.add(newPage);
        }
        return inventories;
    }
}

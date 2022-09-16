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

public class CommandSpawnUI implements CommandExecutor {
    ItemStack noNation = getItem(Material.BLUE_STAINED_GLASS_PANE, "§c§lNation-less Towns", "noNation");
    ItemStack notPublic = getItem(Material.PURPLE_STAINED_GLASS_PANE, "§c§lPrivate Towns", "notPublic");
    int menuSlot = 10;
    static Inventory inv = Bukkit.createInventory(null, 27, "§6§lTowny§f§l: §3§lNations");
    static Inventory inv2 = Bukkit.createInventory(null, 27, "§6§lTowny§f§l: §3§lNations (2)");
    static Inventory inv3 = Bukkit.createInventory(null, 27, "§6§lTowny§f§l: §3§lNations (3)");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;

        if(!sender.hasPermission("townyspawnui.menu.open")) {
            sender.sendMessage(ChatColor.RED + "You can't do that!");
            return false;
        }

        List<Nation> Nations = new LinkedList<Nation>(TownyUniverse.getInstance().getNations());
        int nationsCount = TownyUniverse.getInstance().getNations().size();

        addNoNationsItems();
        addPrivatesItems();
        prepareInventories(nationsCount, Nations);

        fillEmpty(inv, getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "nationMenu"));
        fillEmpty(inv2, getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "nationMenu"));
        fillEmpty(inv3, getItem(Material.BLACK_STAINED_GLASS_PANE, " ", "nationMenu"));

        openInventory(p, 1);

        return false;
    }

    public ItemStack getItem(Material material, String newName, String localizedName){
        ItemStack it = new ItemStack(material, 1);
        ItemMeta itM = it.getItemMeta();
        if(newName != null) itM.setDisplayName(newName);
        if(localizedName != null) itM.setLocalizedName(localizedName);
        it.setItemMeta(itM);
        return it;
    }

    public ItemStack getItemLore(Material material, String newName, String localizedName, ArrayList<String> itemlore){
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

    public static void openInventory(Player player, int page) {
        if(page == 1)  player.openInventory(inv);
        else if(page == 2) player.openInventory(inv2);
        else if(page == 3) player.openInventory(inv3);
    }

    public void prepareInventories(int nationsCount, List<Nation> Nations){
        if(nationsCount > 7){
            inv.setItem(23, getItem(Material.ARROW, "§6§lNext Page", "page2"));
            inv2.setItem(21, getItem(Material.ARROW, "§6§lPrevious Page", "page1"));
            menuSlot = 10;
            for(int j = 0; j < 7; j++){
                String nationName = Nations.get(j).toString();
                Nation nation = TownyAPI.getInstance().getNation(nationName);
                Material material = Material.RED_STAINED_GLASS_PANE;
                if(MetaDataUtil.hasMeta(nation, MetaData.blockInMenu)){
                    material = Material.valueOf(MetaData.getBlockInMenu(nation));
                }
                ArrayList<String> itemlore = new ArrayList<>();
                setGlobalLore(itemlore, nation);
                inv.setItem(menuSlot, getItemLore(material, "§c§l" + nation.getName(), nation.getName(), itemlore));
                menuSlot++;
            }
            if(nationsCount > 14){
                inv2.setItem(23, getItem(Material.ARROW, "§6§lNext Page", "page3"));
                inv3.setItem(21, getItem(Material.ARROW, "§6§lPrevious Page", "page2"));
                menuSlot = 10;
                for(int j = 7; j < 14; j++){
                    String nationName = Nations.get(j).toString();
                    Nation nation = TownyAPI.getInstance().getNation(nationName);
                    Material material = Material.RED_STAINED_GLASS_PANE;
                    if(MetaDataUtil.hasMeta(nation, MetaData.blockInMenu)){
                        material = Material.valueOf(MetaData.getBlockInMenu(nation));
                    }
                    ArrayList<String> itemlore = new ArrayList<>();
                    setGlobalLore(itemlore, nation);
                    inv2.setItem(menuSlot, getItemLore(material, "§c§l" + nation.getName(), nation.getName(), itemlore));
                    menuSlot++;
                }
                inv2.setItem(21, getItem(Material.ARROW, "§6§lPrevious Page", "page1"));
                inv2.setItem(23, getItem(Material.ARROW, "§6§lNext Page", "page3"));
                menuSlot = 10;
                for(int j = 14; j < nationsCount; j++){
                    String nationName = Nations.get(j).toString();
                    Nation nation = TownyAPI.getInstance().getNation(nationName);
                    Material material = Material.RED_STAINED_GLASS_PANE;
                    if(MetaDataUtil.hasMeta(nation, MetaData.blockInMenu)){
                        material = Material.valueOf(MetaData.getBlockInMenu(nation));
                    }
                    ArrayList<String> itemlore = new ArrayList<>();
                    setGlobalLore(itemlore, nation);
                    inv3.setItem(menuSlot, getItemLore(material, "§c§l" + nation.getName(), nation.getName(), itemlore));
                    menuSlot++;
                }
            }else{
                menuSlot = 10;
                for(int j = 7; j < nationsCount; j++){
                    String nationName = Nations.get(j).toString();
                    Nation nation = TownyAPI.getInstance().getNation(nationName);
                    Material material = Material.RED_STAINED_GLASS_PANE;
                    if(MetaDataUtil.hasMeta(nation, MetaData.blockInMenu)){
                        material = Material.valueOf(MetaData.getBlockInMenu(nation));
                    }
                    ArrayList<String> itemlore = new ArrayList<>();
                    setGlobalLore(itemlore, nation);
                    inv2.setItem(menuSlot, getItemLore(material, "§c§l" + nation.getName(), nation.getName(), itemlore));
                    menuSlot++;
                }
            }
        }else{
            menuSlot = 10;
            for(int j = 0; j < nationsCount; j++){
                String nationName = Nations.get(j).toString();
                Nation nation = TownyAPI.getInstance().getNation(nationName);
                Material material = Material.RED_STAINED_GLASS_PANE;
                if(MetaDataUtil.hasMeta(nation, MetaData.blockInMenu)){
                    material = Material.valueOf(MetaData.getBlockInMenu(nation));
                }
                ArrayList<String> itemlore = new ArrayList<>();
                setGlobalLore(itemlore, nation);
                inv.setItem(menuSlot, getItemLore(material, "§c§l" + nation.getName(), nation.getName(), itemlore));
                menuSlot++;
            }
        }
    }

    public void addNoNationsItems(){
        if(TownyAPI.getInstance().getTownsWithoutNation().size() != 0){
            inv.setItem(22, noNation);
            inv2.setItem(22, noNation);
            inv3.setItem(22, noNation);
        }
    }

    public void addPrivatesItems(){
        int privateTownsCount = 0;
        for(int j = 0; j < TownyAPI.getInstance().getTowns().size(); j++){
            if(!TownyAPI.getInstance().getTowns().get(j).isPublic()){
                privateTownsCount++;
            }
        }

        if(privateTownsCount != 0){
            inv.setItem(18, notPublic);
            inv2.setItem(18, notPublic);
            inv3.setItem(18, notPublic);
        }
    }

    public void setGlobalLore(ArrayList<String> itemlore, Nation nation){
        itemlore.add("§6§lLeader§f§l: §3§l" + nation.getKing().getName());
        itemlore.add("§6§lCapital§f§l: §2§l" + nation.getCapital().getName());
        itemlore.add("§6§lTowns§f§l: §9§l" + nation.getTowns().size());
        itemlore.add("§6§lTotal Residents§f§l: §d§l" + nation.getResidents().size());
    }
}

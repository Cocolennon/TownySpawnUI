package me.senkoco.townyspawnui.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class CommandSpawnUI implements CommandExecutor {
    static Inventory inv = Bukkit.createInventory(null, 27, "§6§lTowny§f§l: §3§lNations");
    static Inventory inv2 = Bukkit.createInventory(null, 27, "§6§lTowny§f§l: §3§lNations (2)");
    static Inventory inv3 = Bukkit.createInventory(null, 27, "§6§lTowny§f§l: §3§lNations (3)");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;

        List<Nation> Nations = new LinkedList<Nation>(TownyUniverse.getInstance().getNations());
        int nationsCount = TownyUniverse.getInstance().getNations().size();

        if(nationsCount > 7){
            inv.setItem(23, getItem(Material.ARROW, "§6§lNext Page", "nextPageNations1"));
            inv2.setItem(21, getItem(Material.ARROW, "§6§lPrevious Page", "previousPageNations2"));
            int menuSlot = 10;
            for(int j = 0; j < 7; j++){
                String nationName = Nations.get(j).toString();
                Nation nation = TownyAPI.getInstance().getNation(nationName);
                inv.setItem(menuSlot, getItem(Material.RED_STAINED_GLASS_PANE, "§c§l" + nation.getName(), nation.getName()));
                menuSlot++;
            }
            inv.setItem(23, getItem(Material.ARROW, "§6§lNext Page", "nextPageNations1"));
            if(nationsCount > 14){
                inv2.setItem(23, getItem(Material.ARROW, "§6§lNext Page", "nextPageNations2"));
                inv3.setItem(21, getItem(Material.ARROW, "§6§lPrevious Page", "previousPageNations3"));
                menuSlot = 10;
                for(int j = 7; j < 14; j++){
                    String nationName = Nations.get(j).toString();
                    Nation nation = TownyAPI.getInstance().getNation(nationName);
                    inv2.setItem(menuSlot, getItem(Material.RED_STAINED_GLASS_PANE, "§c§l" + nation.getName(), nation.getName()));
                    menuSlot++;
                }
                inv2.setItem(21, getItem(Material.ARROW, "§6§lPrevious Page", "previousPageNations2"));
                inv2.setItem(23, getItem(Material.ARROW, "§6§lNext Page", "nextPageNations2"));
                menuSlot = 10;
                for(int j = 14; j < nationsCount; j++){
                    String nationName = Nations.get(j).toString();
                    Nation nation = TownyAPI.getInstance().getNation(nationName);
                    inv3.setItem(menuSlot, getItem(Material.RED_STAINED_GLASS_PANE, "§c§l" + nation.getName(), nation.getName()));
                    menuSlot++;
                }
            }else{
                menuSlot = 10;
                for(int j = 7; j < nationsCount; j++){
                    String nationName = Nations.get(j).toString();
                    Nation nation = TownyAPI.getInstance().getNation(nationName);
                    inv2.setItem(menuSlot, getItem(Material.RED_STAINED_GLASS_PANE, "§c§l" + nation.getName(), nation.getName()));
                    menuSlot++;
                }
            }
        }else{
            int menuSlot = 10;
            for(int j = 0; j < nationsCount; j++){
                String nationName = Nations.get(j).toString();
                Nation nation = TownyAPI.getInstance().getNation(nationName);
                inv.setItem(menuSlot, getItem(Material.RED_STAINED_GLASS_PANE, "§c§l" + nation.getName(), nation.getName()));
                menuSlot++;
            }
        }

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
}

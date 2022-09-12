package me.senkoco.townyspawnui;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import me.senkoco.townyspawnui.commands.CommandSpawnUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getLogger;

public class MainListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory inv = event.getInventory();
        Player player = (Player)event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if(inv.getItem(0) != null){
            if(inv.getItem(0).getItemMeta().getLocalizedName().equals("nationMenu")) {
                if(current.getItemMeta().getLocalizedName().equals("noNation")) {
                    openNationlessTowns(event, player);
                }else{
                    openTownsOfNation(event, current, player);
                }
            }else if(inv.getItem(0).getItemMeta().getLocalizedName().equals("townMenu")){
                event.setCancelled(true);
                if(current.getItemMeta().getLocalizedName().equals("townMenu")) return;
                if(current.getItemMeta().getLocalizedName().equals("page1")){
                    openTownsOfNation(event, current, player);
                }else{
                    teleportToTown(player, current.getItemMeta().getLocalizedName());
                }
            }
        }
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

    public void openTownsOfNation(InventoryClickEvent event, ItemStack current, Player player) {
        event.setCancelled(true);
        if (current == null || !current.getItemMeta().hasLocalizedName() || current.getItemMeta().getLocalizedName().equals("nationMenu"))
            return;
        switch (current.getItemMeta().getLocalizedName()) {
            case "page1":
                CommandSpawnUI.openInventory(player, 1);
                return;
            case "page2":
                CommandSpawnUI.openInventory(player, 2);
                return;
            case "page3":
                CommandSpawnUI.openInventory(player, 3);
                return;
            default:
                break;
        }
        Inventory inv = Bukkit.createInventory(null, 27, "§6§l" + current.getItemMeta().getLocalizedName() + "§f§l: §3§lTowns");
        Nation nation = TownyAPI.getInstance().getNation(current.getItemMeta().getLocalizedName());
        int menuSlot = 1;
        if (nation != null) {
            for (int j = 0; j < nation.getTowns().size(); j++) {
                Town town = nation.getTowns().get(j);
                ArrayList<String> itemlore = new ArrayList<>();
                itemlore.add("§6§lNation§f§l: §3§l" + town.getName());
                itemlore.add("§6§lMayor§f§l: §2§l" + town.getMayor().getName());
                itemlore.add("§6§lResidents§f§l: §d§l" + town.getResidents().size());
                itemlore.add("§6§lSpawn Cost§f§l: §c§l" + town.getSpawnCost());
                inv.setItem(menuSlot, getItemLore(Material.RED_STAINED_GLASS_PANE, "§c§l" + town.getFormattedName(), town.getName(), itemlore));
                inv.setItem(22, getItem(Material.ARROW, "§6§lBack to Nations", "page1"));
                menuSlot++;
            }
        }
        CommandSpawnUI.fillEmpty(inv, getItem(Material.BLACK_STAINED_GLASS_PANE, "§c§lClick on any town to teleport to it!", "townMenu"));
        player.openInventory(inv);
    }

    public void openNationlessTowns(InventoryClickEvent event, Player player){
        event.setCancelled(true);
        Inventory inv = Bukkit.createInventory(null, 27, "§6§lNation-less: Towns");
        int menuSlot = 1;
        for(int j = 0; j < TownyAPI.getInstance().getTownsWithoutNation().size(); j++){
            Town town = TownyAPI.getInstance().getTownsWithoutNation().get(j);
            ArrayList<String> itemlore = new ArrayList<>();
            itemlore.add("§6§lMayor§f§l: §2§l" + town.getMayor().getName());
            itemlore.add("§6§lResidents§f§l: §d§l" + town.getResidents().size());
            itemlore.add("§6§lSpawn Cost§f§l: §c§l" + town.getSpawnCost());
            inv.setItem(menuSlot, getItemLore(Material.RED_STAINED_GLASS_PANE, "§c§l" + town.getFormattedName(), town.getName(), itemlore));
            inv.setItem(22, getItem(Material.ARROW, "§6§lBack to Nations", "page1"));
            menuSlot++;
        }
        CommandSpawnUI.fillEmpty(inv, getItem(Material.BLACK_STAINED_GLASS_PANE, "§c§lClick on any town to teleport to it!", "townMenu"));
        player.openInventory(inv);
    }

    public void teleportToTown(Player player, String townName){
        if(!player.hasPermission("townyspawnui.menu.teleport")) {
            player.sendMessage(ChatColor.RED + "You can't do that!");
            return;
        }
        player.performCommand("t spawn " + townName + " -ignore");
        getLogger().info(player.getName() + " teleported to " + townName);
    }
}

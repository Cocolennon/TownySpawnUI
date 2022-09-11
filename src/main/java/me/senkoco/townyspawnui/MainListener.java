package me.senkoco.townyspawnui;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
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

public class MainListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event){
        Inventory inv = event.getInventory();
        Player player = (Player)event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if(inv.getItem(0).getItemMeta().getLocalizedName().equals("nationMenu")){
            openTownsOfNation(event, current, player);
        }else if(inv.getItem(0).getItemMeta().getLocalizedName().equals("townMenu")){
            event.setCancelled(true);
            teleportToTown(player, current.getItemMeta().getLocalizedName());
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
        switch(current.getItemMeta().getLocalizedName()) {
            case "nextPageNations1":
                CommandSpawnUI.openInventory(player, 2);
                break;
            case "nextPageNations2":
                CommandSpawnUI.openInventory(player, 3);
                break;
            case "previousPageNations2":
                CommandSpawnUI.openInventory(player, 1);
                break;
            case "previousPageNations3":
                CommandSpawnUI.openInventory(player, 2);
                break;
            default:
                break;
        }
        Inventory inv = Bukkit.createInventory(null, 27, "§6§l" + current.getItemMeta().getLocalizedName() + "§f§l: §3§lTowns");
        Nation nation = TownyAPI.getInstance().getNation(current.getItemMeta().getLocalizedName());
        int menuSlot = 1;
        for (int j = 0; j < nation.getTowns().size(); j++) {
            Town town = nation.getTowns().get(j);
            ArrayList<String> itemlore = new ArrayList<>();
            itemlore.add("§6§lNation§f§l: §3§l" + town.getName());
            itemlore.add("§6§lMayor§f§l: §2§l" + town.getMayor().getName());
            itemlore.add("§6§lResidents§f§l: §d§l" + town.getResidents().size());
            itemlore.add("§6§lSpawn Cost§f§l: §c§l" + town.getSpawnCost());
            inv.setItem(menuSlot, getItemLore(Material.RED_STAINED_GLASS_PANE, "§c§l" + town.getName(), town.getName(), itemlore));
            menuSlot++;
        }
        CommandSpawnUI.fillEmpty(inv, getItem(Material.BLACK_STAINED_GLASS_PANE, "§c§lClick on any town to teleport to it!", "townMenu"));
        player.openInventory(inv);
    }

    public void teleportToTown(Player player, String townName){
        if(player.hasPermission("townyspawnui.menu.teleport")){
            player.performCommand("t spawn " + townName + " -ignore");
        }else{
            player.sendMessage(ChatColor.RED + "You can't do that!");
        }
    }
}

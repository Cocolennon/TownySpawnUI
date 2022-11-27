package me.senkoco.townyspawnui;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
import me.senkoco.townyspawnui.commands.CommandSpawnUI;
import me.senkoco.townyspawnui.events.PlayerTeleportToTown;
import me.senkoco.townyspawnui.utils.metadata.MetaData;
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
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

import static java.lang.Integer.valueOf;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getPluginManager;

public class MainListener implements Listener {
    ItemStack townFiller = getItem(Material.BLACK_STAINED_GLASS_PANE, "§c§lClick on any town to teleport to it!", "townMenu");
    int menuSlot = 1;

    Plugin plugin = getPluginManager().getPlugin("TownySpawnUI");

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (plugin != null) {
            if (plugin instanceof Main) {
                Main main = (Main) plugin;
            }
        }

        Inventory inv = event.getInventory();
        Player player = (Player)event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if(inv.getItem(0) != null){
            if(inv.getItem(0).getItemMeta().getLocalizedName().equals("nationMenu")) {
                if(current.getItemMeta().getLocalizedName().equals("noNation")) {
                    openNationlessTowns(event, player);
                }else if(current.getItemMeta().getLocalizedName().equals("notPublic")){
                    openPrivateTowns(event, player);
                }else{
                    openTownsOfNation(event, current, player);
                }
            }else if(inv.getItem(0).getItemMeta().getLocalizedName().equals("townMenu")){
                event.setCancelled(true);
                if(current.getItemMeta().getLocalizedName().equals("townMenu")) return;
                if(current.getItemMeta().getDisplayName().equals("§6§lNext Page") || current.getItemMeta().getDisplayName().equals("§6§lPrevious Page") || current.getItemMeta().getDisplayName().equals("§6§lBack to Nations")) {
                    openTownsOfNation(event, current, player);
                }else{
                    teleportToTown(player, current.getItemMeta().getLocalizedName());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTeleportToTown(PlayerTeleportToTown event){
        Player player = event.getPlayer();
        Town town = event.getTown();

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "[Towny Spawn UI] " + ChatColor.DARK_AQUA + "You have successfully been teleported to " + town.getName());
        getLogger().info(player.getName() + " teleported to " + town.getName());
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
        if(current.getItemMeta().getDisplayName().equals("§6§lNext Page") || current.getItemMeta().getDisplayName().equals("§6§lPrevious Page") || current.getItemMeta().getDisplayName().equals("§6§lBack to Nations")){
            CommandSpawnUI.openInventory(player, valueOf(current.getItemMeta().getLocalizedName()));
            return;
        }
        Nation nation = TownyAPI.getInstance().getNation(current.getItemMeta().getLocalizedName());
        Inventory inv = Bukkit.createInventory(null, 27, "§6§l" + nation.getName() + "§f§l: §3§lTowns");
        menuSlot = 1;
        if (nation != null) {
            for (int j = 0; j < nation.getTowns().size(); j++) {
                Town town = nation.getTowns().get(j);
                if(town.isPublic()){
                    Material material = Material.valueOf(plugin.getConfig().getString("menu.defaultItem"));
                    if(MetaDataUtil.hasMeta(town, MetaData.blockInMenu)){
                        material = Material.valueOf(MetaData.getBlockInMenu(town));
                    }
                    ArrayList<String> itemlore = new ArrayList<>();
                    itemlore.add("§6§lNation§f§l: §3§l" + town.getNationOrNull().getName());
                    itemlore.add("§6§lMayor§f§l: §2§l" + town.getMayor().getName());
                    itemlore.add("§6§lResidents§f§l: §d§l" + town.getResidents().size());
                    itemlore.add("§6§lSpawn Cost§f§l: §c§l" + town.getSpawnCost());
                    inv.setItem(menuSlot, getItemLore(material, "§c§l" + town.getFormattedName(), town.getName(), itemlore));
                    menuSlot++;
                }
            }
        }
        inv.setItem(22, getItem(Material.ARROW, "§6§lBack to Nations", "1"));
        CommandSpawnUI.fillEmpty(inv, townFiller);
        player.openInventory(inv);
    }

    public void openNationlessTowns(InventoryClickEvent event, Player player){
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
                inv.setItem(menuSlot, getItemLore(material, "§c§l" + town.getFormattedName(), town.getName(), itemlore));
                menuSlot++;
            }
        }
        inv.setItem(22, getItem(Material.ARROW, "§6§lBack to Nations", "1"));
        CommandSpawnUI.fillEmpty(inv, townFiller);
        player.openInventory(inv);
    }

    public void openPrivateTowns(InventoryClickEvent event, Player player) {
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
                inv.setItem(menuSlot, getItemLore(material, "§c§l" + town.getFormattedName(), town.getName(), itemlore));
                menuSlot++;
            }
        }
        inv.setItem(22, getItem(Material.ARROW, "§6§lBack to Nations", "1"));
        CommandSpawnUI.fillEmpty(inv, townFiller);
        player.openInventory(inv);
    }

    public void teleportToTown(Player player, String townName){
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
}

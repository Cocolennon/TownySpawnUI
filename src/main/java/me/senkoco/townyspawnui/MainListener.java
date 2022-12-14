package me.senkoco.townyspawnui;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import me.senkoco.townyspawnui.events.PlayerTeleportToTown;
import me.senkoco.townyspawnui.utils.menu.NationUtil;
import me.senkoco.townyspawnui.utils.menu.TownUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getLogger;

public class MainListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player)event.getWhoClicked();
        ItemStack current = event.getCurrentItem();

        if(inv.getItem(0) != null){
            if(inv.getItem(0).getItemMeta().getLocalizedName().equals("nationMenu")) {

                if(current.getItemMeta().getLocalizedName().equals("noNation")) {
                    NationUtil.openNationlessTowns(event, player);
                }else if(current.getItemMeta().getLocalizedName().equals("notPublic")){
                    NationUtil.openPrivateTowns(event, player);
                }else{
                    NationUtil.openTownsOfNation(event, current, player, false, null);
                }
            }else if(inv.getItem(0).getItemMeta().getLocalizedName().equals("townMenu")){
                event.setCancelled(true);
                if(current.getItemMeta().getLocalizedName().equals("townMenu")) return;
                if(current.getItemMeta().getDisplayName().equals("§6§lNext Page") || current.getItemMeta().getDisplayName().equals("§6§lPrevious Page") || current.getItemMeta().getDisplayName().equals("§6§lBack to Nations")) {
                    NationUtil.openTownsOfNation(event, current, player, true, TownyAPI.getInstance().getNation(inv.getItem(26).getItemMeta().getLocalizedName()));
                }else{
                    TownUtil.teleportToTown(player, current.getItemMeta().getLocalizedName());
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(player.hasPermission("townyspawnui.*")) {
            if(Main.getUsingOldVersion()){
                player.sendMessage("§6§l[Towny Spawn UI] §3You are using an older version of TownySpawnUI, please update to version " + Main.getLatestVersion());
            }
        }
    }
}

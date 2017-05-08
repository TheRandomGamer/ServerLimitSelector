package me.therandomgamer.serverlimitselector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;


/**
 * Created by robin on 08/05/17.
 */
public class GUIHandler implements Listener {

    @EventHandler
    public void onItemClick(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.getItem().getType() == Material.COMPASS) {
            FileConfiguration c = Main.getMain().getConfig();
            int size = (((c.getConfigurationSection("servers").getKeys(false).size() / 9) + 1) * 9);
            Inventory gui = Bukkit.createInventory(null, size, Main.getMain().getConfig().getString("selector.name"));

            for (String server : c.getConfigurationSection("servers").getKeys(false)) {
                ItemStack is = new ItemStack((Material.getMaterial(c.getString("servers." + server + ".item")) == null) ? Material.DIRT : Material.getMaterial(c.getString("servers." + server + ".item")));
                ItemMeta ismeta = is.getItemMeta();
                ismeta.setDisplayName(c.getString("servers." + server + ".itemname"));

                int playersOnline = BungeeChannelConnection.getInstance().getServerPlayers(server);
                int maxPlayersOnline = c.getInt("servers." + server + ".maxplayers");
                ismeta.setLore(Arrays.asList(ChatColor.BOLD + "" + ((playersOnline < maxPlayersOnline) ? ChatColor.GREEN : ChatColor.RED) + "" + playersOnline + "/" + maxPlayersOnline));

                is.setItemMeta(ismeta);
                gui.addItem(is);
            }
            e.getPlayer().openInventory(gui);
        }
    }


}
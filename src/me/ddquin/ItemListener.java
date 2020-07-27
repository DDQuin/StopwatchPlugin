package me.ddquin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemListener implements Listener {

    private StopwatchMain plugin;

    public ItemListener(StopwatchMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickItem(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        if (!(item.equals(getStartItem()) || item.equals(getStopItem()) || item.equals(getPauseItem()))) {
            return;
        }
        if (!e.getPlayer().hasPermission("stopwatch.admin")) {
            e.getPlayer().sendMessage(ChatColor.RED + "You have no permission!");
            return;
        }
        if (item.equals(getStartItem())) {
            plugin.startStopwatch();
        } else if (item.equals(getStopItem())) {
            plugin.stopStopwatch(e.getPlayer());
        } else if (item.equals(getPauseItem())) {
            plugin.pauseStopwatch(e.getPlayer());
        }

    }

    @EventHandler
    public void onPlaceItem(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        if ( item.equals(getStartItem()) || item.equals(getStopItem()) || item.equals(getPauseItem()) ) {
            e.setCancelled(true);
        }
    }


    public static ItemStack getStartItem() {
        ItemStack startItem = new ItemStack(Material.SLIME_BLOCK, 1);
        ItemMeta i = startItem.getItemMeta();
        i.setDisplayName(ChatColor.GREEN + "Start Timer");
        startItem.setItemMeta(i);
        return startItem;
    }

    public static ItemStack getStopItem() {
        ItemStack stopItem = new ItemStack(Material.BARRIER, 1);
        ItemMeta i = stopItem.getItemMeta();
        i.setDisplayName( ChatColor.RED + "Stop Timer");
        stopItem.setItemMeta(i);
        return stopItem;
    }

    public static ItemStack getPauseItem() {
        ItemStack pauseItem = new ItemStack(Material.STONE, 1);
        ItemMeta i = pauseItem.getItemMeta();
        i.setDisplayName(ChatColor.GRAY + "Pause Timer");
        pauseItem.setItemMeta(i);
        return pauseItem;
    }

}

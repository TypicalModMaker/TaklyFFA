package dev.isnow.ffa.event;

import dev.isnow.ffa.TaklyFFA;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class PlaceBlockListener implements Listener {

    final ArrayList<Location> blocks = new ArrayList<>();

    @EventHandler
    public void liquid(PlayerBucketEmptyEvent e) {
        if(!TaklyFFA.INSTANCE.spawnManager.getCuboid().isIn(e.getBlockClicked().getRelative(e.getBlockFace()))) {
            if(!blocks.contains(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation())) {
                blocks.add(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(blocks.contains(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation())) {
                            blocks.remove(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation());
                            e.getBlockClicked().getWorld().getBlockAt(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation()).setType(Material.AIR);
                        }
                    }
                }.runTaskLater(TaklyFFA.INSTANCE.getPlugin(), TaklyFFA.INSTANCE.getPlugin().getConfig().getInt("block-break-time"));
            }
        } else {
            if(!e.getPlayer().hasPermission("takly.admin")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void liquid(PlayerBucketFillEvent e) {
        if(!TaklyFFA.INSTANCE.spawnManager.getCuboid().isIn(e.getBlockClicked().getRelative(e.getBlockFace())) && blocks.contains(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation())) {
            blocks.remove(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation());
        } else {
            if(!e.getPlayer().hasPermission("takly.admin")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(!TaklyFFA.INSTANCE.spawnManager.getCuboid().isIn(e.getBlock())) {
            if(!blocks.contains(e.getBlock().getLocation())) {
                blocks.add(e.getBlock().getLocation());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(blocks.contains(e.getBlock().getLocation())) {
                            blocks.remove(e.getBlock().getLocation());
                            e.getBlock().getWorld().getBlockAt(e.getBlock().getLocation()).setType(Material.AIR);
                            e.getPlayer().getInventory().addItem(new ItemStack(e.getBlock().getType(), 1));
                        }
                    }
                }.runTaskLater(TaklyFFA.INSTANCE.getPlugin(), TaklyFFA.INSTANCE.getPlugin().getConfig().getInt("block-break-time"));
            }
        } else {
            if(!e.getPlayer().hasPermission("takly.admin")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockBreakEvent e) {
        if(!TaklyFFA.INSTANCE.spawnManager.getCuboid().isIn(e.getBlock()) && blocks.contains(e.getBlock().getLocation())) {
            blocks.remove(e.getBlock().getLocation());
        } else {
            if(!e.getPlayer().hasPermission("takly.admin")) {
                e.setCancelled(true);
            }
        }
    }
}

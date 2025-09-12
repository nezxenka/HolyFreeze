package ru.nezxenka.holyfreeze;

import com.destroystokyo.paper.event.player.PlayerClientOptionsChangeEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class ListenerFreeze implements Listener {

    public HolyFreeze plugin;

    public ListenerFreeze(HolyFreeze plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void on(PlayerMoveEvent event){
        if(event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()){
            if(plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId())){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on1(PlayerMoveEvent event){
        if(event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()){
            if(plugin.statusFreezeHashMap.containsKey(event.getPlayer())){
                if(plugin.statusFreezeHashMap.get(event.getPlayer()) == StatusFreeze.AFK) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void on(EntityDamageEvent event){
        if(event.getEntity() instanceof Player) {
            if (plugin.freezeHashMap.containsKey(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(PlayerInteractEvent event){
        if (plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(InventoryClickEvent event){
        if (plugin.freezeHashMap.containsKey(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerTeleportEvent event){
        if (plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerDropItemEvent event){
        if (plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerAttemptPickupItemEvent event){
        if (plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerCommandPreprocessEvent event){
        if (plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId()) && !event.getMessage().startsWith("/m ") && !event.getMessage().startsWith("/msg ") && !event.getMessage().startsWith("/r ") && !event.getMessage().startsWith("/reply ") && !event.getMessage().startsWith("/message ")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerJumpEvent event){
        if (plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player) {
            if (plugin.freezeHashMap.containsKey(event.getDamager().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void on(PlayerJoinEvent event){
        if (plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId())) {
            plugin.freezeHashMap.get(event.getPlayer().getUniqueId()).updatePlayer();
        }
    }

    @EventHandler
    public void on(PlayerKickEvent event){
        if (plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId())) {
            plugin.freezeHashMap.get(event.getPlayer().getUniqueId()).stopKick(false);
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent event){
        if (plugin.freezeHashMap.containsKey(event.getPlayer().getUniqueId())) {
            plugin.freezeHashMap.get(event.getPlayer().getUniqueId()).stopKick(true);
        }
    }
}

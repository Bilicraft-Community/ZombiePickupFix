package com.bilicraft.zombiepickupfix;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class ZombiePickupFix extends JavaPlugin implements Listener {
    private final static ItemStack airItemStack = new ItemStack(Material.AIR);
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this);
        new BukkitRunnable(){
            @Override
            public void run() {
                Bukkit.getWorlds().forEach(world->world.getEntities().forEach(entity -> {
                    if(!(entity instanceof Zombie)){
                        return;
                    }
                    Zombie zombie = (Zombie)entity;
                    EntityEquipment equipment = zombie.getEquipment();
                    if(equipment == null){
                        return;
                    }
                    Material material = equipment.getItemInMainHand().getType();
                    if(material == Material.EGG || material == Material.GLOW_INK_SAC || material == Material.INK_SAC){
                       ItemStack oldItem = equipment.getItemInMainHand();
                       equipment.setItemInMainHand(airItemStack);
                       world.dropItemNaturally(entity.getLocation(),oldItem);
                       getLogger().info("Force Zombie at "+entity.getLocation()+" drop item in hand: "+oldItem.getType().name());
                    }
                }));
            }
        }.runTaskTimer(this,0,20*600);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
    public void onEntityPickup(EntityPickupItemEvent event){
        Material material = event.getItem().getItemStack().getType();
        if(!(event.getEntity() instanceof Zombie)){
            return;
        }
        if(material == Material.EGG || material == Material.GLOW_INK_SAC || material == Material.INK_SAC){
            event.setCancelled(true);
        }
    }
}

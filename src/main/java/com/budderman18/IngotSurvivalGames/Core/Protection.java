/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.budderman18.IngotSurvivalGames.Core;

import com.budderman18.IngotSurvivalGames.FileManager;
import com.budderman18.IngotSurvivalGames.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles protection-based events
 * You can block building,breaking, and block/entity interactions
 * This contains events and (possibly) has to be
 * within every plugin, rather than in IngotMiniGamesAPI
 * 
 */
public class Protection implements Listener {
    //plugin
    Plugin plugin = main.getInstance();
    //files
    final String ROOT = "";
    FileManager getdata = new FileManager();
    /*
    *
    * This method handles block breaking
    *
    */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //local vars
        FileConfiguration temp = getdata.getCustomData(plugin,"temp",ROOT);
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        //check if protection is enabled and player is ingame
        if (config.getBoolean("enable-protection") == true && temp.getBoolean(event.getPlayer().getName() + ".inGame") == true) {  
            event.setCancelled(true);
        }
    }
    /*
    *
    * This method handles block placing
    *
    */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        //local vars
        FileConfiguration temp = getdata.getCustomData(plugin,"temp",ROOT);
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        //check if protection is enabled and player is ingame
        if (config.getBoolean("enable-protection") == true && temp.getBoolean(event.getPlayer().getName() + ".inGame") == true) {
            event.setCancelled(true);
        }
    } 
    /*
    *
    * This method handles block interactions
    *
    */
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        //local vars
        FileConfiguration temp = getdata.getCustomData(plugin,"temp",ROOT);
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        //check if protection is enabled and player is ingame
        if (config.getBoolean("enable-protection") == true && temp.getBoolean(event.getPlayer().getName() + ".inGame") == true) {
            event.setCancelled(true);
        }
    }
    /*
    *
    * This method handles entity interactions (right click)
    *
    */
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        //local vars
        FileConfiguration temp = getdata.getCustomData(plugin,"temp",ROOT);
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        //check if protection is enabled and player is ingame
        if (config.getBoolean("enable-protection") == true && temp.getBoolean(event.getPlayer().getName() + ".inGame") == true) {
            event.setCancelled(true);
        }
    }
    /*
    *
    * This method handles attacking entities
    *
    */
    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        //local vars
        FileConfiguration temp = getdata.getCustomData(plugin,"temp",ROOT);
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        //check if protection is enabled and player is ingame
        if (config.getBoolean("enable-protection") == true && temp.getBoolean(event.getDamager().getName() + ".inGame") == true) {
            event.setCancelled(true);
        }
    }
}

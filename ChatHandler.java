/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.budderman18.IngotSurvivalGames.Core;

import com.budderman18.IngotSurvivalGames.FileManager;
import com.budderman18.IngotSurvivalGames.main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles everything involving chat
 * This contains events and (possibly) has to be
 * within every plugin, rather than in IngotMiniGamesAPI
 * 
 */
public class ChatHandler implements Listener {
    //plugin
    Plugin plugin = main.getInstance();
    //files
    final String ROOT = "";
    FileManager getdata = new FileManager();
    /*
    *
    * This method changes messages into the universal format 
    *
    */
    @EventHandler
    public void setMessage(AsyncPlayerChatEvent event) {
        //files
        FileConfiguration temp = getdata.getCustomData(plugin,"temp",ROOT);
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        //local vars
        String format = "%2$s";
        String message  = "";
        //check if chat is enabled and player is ingame
        if (config.getBoolean("Chat.enable") == true && temp.getBoolean(event.getPlayer().getName() + ".inGame") == true) {
            //get incoming message
            event.setFormat(format);
            message = config.getString("Chat.format");
            //swap out placeholders
            message = message.replaceAll("%player%", event.getPlayer().getName());
            message = message.replaceAll("%message%", event.getMessage());
            //send message
            event.setMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.budderman18.IngotSurvivalGames.Core;

import com.budderman18.IngotSurvivalGames.FileManager;
import com.budderman18.IngotSurvivalGames.main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
/**
 *
 * @author Kyle Collins
 */
public class TablistHandler {
    //plugin
    Plugin plugin = main.getInstance();
    //files
    final String ROOT = "";
    FileManager getdata = new FileManager();
    FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
    FileConfiguration temp = getdata.getCustomData(plugin,"temp",ROOT);
    /*
    *
    * This method sets the header for the tablist
    *
    */
    public void setHeader(Player player) {
        //check if tablist is enab;ed
        if (config.getBoolean("Tablist.enable") == true) {
            //set header
            player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&', config.getString("Tablist.header")));
        }
    } 
    /*
    *
    * This method sets the footer for the tablist
    *
    */
    public void setFooter(Player player) {
        //check if tablist is enabled
        if (config.getBoolean("Tablist.enable") == true) {
            //set footer
            player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', config.getString("Tablist.footer")));
        }
    } 
    /*
    *
    * This method removes players from the tablist
    * This has 2 settings depending on if the player is in game
    * DOES NOT WORK AT THE MOMENT, DO NOT USE!!!
    *
    */
    public void removePlayers(Player player) {
        // check if tablist is enabled
        if (config.getBoolean("Tablist.enable") == true) {
            //used to hide players that are not in the same game
            if (config.getBoolean("Tablist.hidePlayersNotInGame") == true) {
                //cycle between all online players
                for (Player key : Bukkit.getOnlinePlayers()) {
                    //check if player is in game
                    if (temp.getBoolean(key + ".inGame") == false) {
                        //delete player from list
                        key.setPlayerListName(" ");
                    }
                }
            }
            //used to hide players playing survivalgames
            if (config.getBoolean("Tablist.hidePlayersInGameFromServer") == true) {
                //cycle between all online players
                for (Player key : Bukkit.getOnlinePlayers()) {
                    //check if player is in game
                    if (temp.getBoolean(key + ".inGame") == true) {
                        //delete player from list
                        key.setPlayerListName(" ");
                    }
                }
            }
        }
    }
    /*
    *
    * This method resets the tablist
    * Used when leaving
    *
    */
    public void reset(Player player) {
        //check if tablist is enabled
        if (config.getBoolean("Tablist.enable") == true) {
            //reset header/footer
            player.setPlayerListHeaderFooter("", "");
            //cycle all online players
            for (Player key : Bukkit.getOnlinePlayers()) {
                //reset playername
                key.setPlayerListName(key.getName());
            }
        }
    }
}

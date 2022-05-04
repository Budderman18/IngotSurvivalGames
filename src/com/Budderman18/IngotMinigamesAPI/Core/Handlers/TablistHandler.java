package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
/**
 *
 * This class handles everything involving the tablist
 * This includes headers & footers, as well as what
 * players are in it
 * 
 */
public class TablistHandler {
    /**
    *
    * This method sets the header for the tablist
    *
    * @param player
    * @param header
    */
    public static void setHeader(Player player, String header) {
        //set header
        player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&', header));
    } 
    /**
    *
    * This method sets the footer for the tablist
    *
    * @param player
    * @param footer
    */
    public static void setFooter(Player player, String footer) {
        //set footer
        player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', footer));
    } 
    /**
    *
    * This method removes players from the tablist
    * This has 2 settings depending on if the player is in game
    * DOES NOT WORK AT THE MOMENT, DO NOT USE!!!
    *
    * @param player
    */
    @Deprecated
    public static void removePlayers(Player player) {
        //plugin
        Plugin plugin = Main.getInstance();
        //files
        final String ROOT = "";
        FileManager getdata = new FileManager(plugin);
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        FileConfiguration temp = getdata.getCustomData(plugin,"temp",ROOT);
        //check if tablist is enabled
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
    /**
    *
    * This method resets the tablist
    * Used when leaving
    *
    * @param player
    */
    public static void reset(Player player) {
        //reset header/footer
        player.setPlayerListHeaderFooter("", "");
        //cycle all online players
        for (Player key : Bukkit.getOnlinePlayers()) {
            //reset playername
            key.setPlayerListName(key.getName());
        }
    }
}
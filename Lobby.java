/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.budderman18.IngotSurvivalGames.Core;

import com.budderman18.IngotSurvivalGames.FileManager;
import com.budderman18.IngotSurvivalGames.main;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
/**
 * 
 * This class handles the core to the lobby
 * Anything lobby-based not specific to SurvivalGames
 * (except arena management) is handled here
 * 
 */
public class Lobby implements Listener {
    //plugin
    Plugin plugin = main.getInstance();
    //files
    final String ROOT = "";
    FileManager getdata = new FileManager();
    File tempf = new File("plugins/IngotSurvivalGames","temp.yml");
    FileConfiguration temp = getdata.getCustomData(plugin,"temp",ROOT);
    FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
    //core classes
    TablistHandler tablist = new TablistHandler();
    ScoreboardHandler scoreboard = new ScoreboardHandler();
    TitleHandler title = new TitleHandler();
    //global vars
    short currentPlayers = 0;
    short maxPlayers = 8;
    Player playerKey = null;
    /*
    *
    * This method handles lobby joining
    * Just specify the player and they're in!
    
    */
    public void joinLobby(Player player) {
        //local vars
        Location lobby =  new Location(Bukkit.getWorld(config.getString("Lobby.world")), config.getDouble("Lobby.x"), config.getDouble("Lobby.y"), config.getDouble("Lobby.z"),(float) config.getDouble("Lobby.yaw"),(float) config.getDouble("Lobby.pitch"));
        String lineString = null;
        String blankString = " ";
        //save changes
        temp.set(player.getName() + '.' + "inGame", true);
        try {
            temp.save(tempf);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //increase players in lobby
        currentPlayers++;
        //teleport to lobby
        player.teleport(lobby);
        //edit tablist
        tablist.setHeader(player);
        tablist.setFooter(player);
        //DO NOT USE REMOVE PLAYER METHODS, THEY DO NOT WORK
        //tablist.removePlayers(player);
        //display join title
        title.setTitle(player, config.getString("Title.title"), config.getString("Title.subtitle"));
        title.setActionBar(player, config.getString("Title.actionbar"));
        //setup lobby scoreboard
        scoreboard.clearScoreboard(player);
        scoreboard.setTitle(player, ChatColor.translateAlternateColorCodes('&', "&7[&6IngotSurvivalGames&7]"));
        //cycle through all players in temp
        for (String key : temp.getKeys(false)) {
            blankString = " ";
            //check if player needs to have their scoreboard updated
            if (temp.getBoolean(key + ".inGame") == true || key.equals(player.getName())) {
                //edit scoreboard
                for (byte i = 0; i < config.getInt("Scoreboard.maxLines"); i++) {
                    playerKey = Bukkit.getPlayer(key);
                    //check if line is null
                    if (config.getString("Scoreboard.line" + i) != null) {
                        lineString = config.getString("Scoreboard.line" + i);
                        lineString = lineString.replaceAll("%currentplayers%", Short.toString(currentPlayers));
                        lineString = lineString.replaceAll("%maxplayers%", Short.toString(maxPlayers));
                        scoreboard.setLine(playerKey, i, ChatColor.translateAlternateColorCodes('&', lineString));
                    } 
                    //needed to properly display spaces
                    else {
                        blankString = blankString.concat(" ");
                        scoreboard.setLine(playerKey, i, blankString);
                    }
                }
            }
            //tablist.removePlayers(player);
        }
    }
    /*
    *
    * This method handles lobby leaving
    * Just specify the player and they're out!
    *
    */
    public void leaveLobby(Player player) {
        //local vars
        Location exit = new Location(Bukkit.getWorld(config.getString("Exit.world")), config.getDouble("Exit.x"), config.getDouble("Exit.y"), config.getDouble("Exit.z"), (float) config.getDouble("Exit.yaw"), (float) config.getDouble("Exit.pitch"));
        String lineString = null;
        String blankString = " ";
        //decrease players in lobby
        currentPlayers--;
        //save changes
        temp.set(player.getName() + '.' + "inGame", false);
        try {
            temp.save(tempf);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //remove scoreboard
        scoreboard.clearScoreboard(player);
        //cycle through all players in temp
        for (String key : temp.getKeys(false)) {
            blankString = " ";
            //check if player is ingame
            if (temp.getBoolean(key + ".inGame") == true) {
                playerKey = Bukkit.getPlayer(key);
                for (byte i = 0; i < config.getInt("Scoreboard.maxLines"); i++) {
                    //check if line is null
                    if (config.getString("Scoreboard.line" + i) != null) {
                        lineString = config.getString("Scoreboard.line" + i);
                        lineString = lineString.replaceAll("%currentplayers%", Short.toString(currentPlayers));
                        lineString = lineString.replaceAll("%maxplayers%", Short.toString(maxPlayers));
                        scoreboard.setLine(playerKey, i, ChatColor.translateAlternateColorCodes('&', lineString));
                    } 
                    //needed to properly display blank lines
                    else {
                        blankString = blankString.concat(" ");
                        scoreboard.setLine(playerKey, i, blankString);
                    }
                }
            }
        }
        //teleport to the exit location
        player.teleport(exit);
        //reset tablist to original
        tablist.reset(player);
    }
}

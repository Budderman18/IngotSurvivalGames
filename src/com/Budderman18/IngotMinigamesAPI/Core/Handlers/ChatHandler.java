package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import com.budderman18.IngotMinigamesAPI.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles everything involving chat
 * 
 */
public class ChatHandler {
    //plugin
    private final Plugin plugin = Main.getInstance();
    //classes
    private final IngotPlayer IngotPlayer = new IngotPlayer(plugin);
    /**
    *
    * This method changes messages into the universal format 
    * Use this for every time a player chats that's playing
    *
    * @param player
    * @param message
    * @param format
    * @return 
    * formattedMessage
    */
    public static String convertMessage(Player player, String message, String format) {
        //swap out placeholders
        format = format.replaceAll("%player%", player.getName());
        format = format.replaceAll("%message%", message);
        //color support
        format = ChatColor.translateAlternateColorCodes('&', format);
        return format;
    }
    /**
    *
    * This method sends a given message to a player
    *
    * @param player
    * @param message
    */
    public static void sendMessage(Player player, String message) {
        //color support
        message = ChatColor.translateAlternateColorCodes('&', message);
        player.sendMessage(message);
    }
    /**
    *
    * This method sends a message to all online players
    * If you want to send to only inGame players, set inGameOnly to true
    *
    * @param message
    * @param inGameOnly
    */
    public void sendMessageToAll(String message, boolean inGameOnly) {
        //Ingot Player
        IngotPlayer currentIPlayer = null;
        boolean inGame = false;
        //color support
        message = ChatColor.translateAlternateColorCodes('&', message);
        //check if should only send to players ingame
        if (inGameOnly == true) {
            //cycle between all online players
            for (Player key : Bukkit.getOnlinePlayers()) {
                //get current ingotplayer
                currentIPlayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
                //check if player is ingame
                if (currentIPlayer.getInGame() == true) {
                    //send message
                    key.sendMessage(message);
                }
            }
        }
        //send to all players
        else {
            //send message
            Bukkit.broadcastMessage(message);
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 *
 * This class handles everything dealing with bossbars. 
 * You can set title, color and style. 
 * 
 */
public class BossbarHandler {
    private static final BossBar bossbar = Bukkit.createBossBar(null, BarColor.WHITE, BarStyle.SOLID);
    /**
    *
    * This method sets the title of the current bossbar.
    * It also adds the player to the bossbar, since this 
    * should be ran before anything.   
    *
    * @param player 
    * @param title 
    */
    public static void setBarTitle(Player player, String title) {
        bossbar.setTitle(ChatColor.translateAlternateColorCodes('&', title));
        bossbar.addPlayer(player);
    }
    /**
    *
    * This method changes the color of a certain boss color. 
    * It defaults to white if you put in an invalid color. 
    *
    * @param player
    * @param color
    */
    public static void setBarColor(Player player, String color) {
        BarColor bc = BarColor.WHITE;
        color = color.toUpperCase();
        if (color.contains("BLUE")) {
            bc = BarColor.BLUE;
        }
        if (color.contains("GREEN")) {
            bc = BarColor.GREEN;
        }
        if (color.contains("PINK")) {
            bc = BarColor.PINK;
        }
        if (color.contains("PURPLE")) {
            bc = BarColor.PURPLE;
        }
        if (color.contains("RED")) {
            bc = BarColor.RED;
        }
        if (color.contains("YELLOW")) {
            bc = BarColor.YELLOW;
        }
        bossbar.setColor(bc);
    }
    /**
    *
    * This method sets the progress/size of the bossbar. 
    *
    * @param player
    * @param size
    */
    public static void setBarSize(Player player, double size) {
        bossbar.setProgress(size);
    }
    /**
    *
    * This method changes the style of the bossbar. 
    * It defaults to solid if the input is invalid. 
    *
    * @param style
    */
    public static void setBarStyle(String style) {
        BarStyle bs = BarStyle.SOLID;
        style = style.toUpperCase();
        if (style.contains("SEGMENTED_6")) {
            bs = BarStyle.SEGMENTED_6;
        }
        if (style.contains("SEGMENTED_10")) {
            bs = BarStyle.SEGMENTED_10;
        }
        if (style.contains("SEGMENTED_12")) {
            bs = BarStyle.SEGMENTED_12;
        }
        if (style.contains("SEGMENTED_20")) {
            bs = BarStyle.SEGMENTED_20;
        }
        bossbar.setStyle(bs);
    }
    /**
    *
    * This method removes a player's bossbar. 
    *
    * @param player
    */
    public static void clearBar(Player player) {
        bossbar.removePlayer(player);
    }
    /**
    *
    * This method toggles weather a player can view the given bossbar. 
    *
    * @param visable
    */
    public static void displayBar(boolean visable) {
        bossbar.setVisible(visable);
    }
}

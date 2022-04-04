/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.budderman18.IngotSurvivalGames.Core;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * This class handles everything involving titles
 * 
 */
public class TitleHandler {
    /*
    *
    * This method sets the title
    * Also changes subtitle
    * You can specify fades and lengths in it
    *
    */
    public void setTitle(Player player, String title, String subtitle) {
        //send title
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subtitle), 10, 100, 10);
    }
    /*
    *
    * This method sets the action bar
    * Unfourtunatly, you can't specify fades and lengths
    * within action bars.
    *
    */
    public void setActionBar(Player player, String string) {
        //send actionbar
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', string)));
    }
}

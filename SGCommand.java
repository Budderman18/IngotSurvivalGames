package com.budderman18.IngotSurvivalGames.Commands;

import com.budderman18.IngotSurvivalGames.FileManager;
import com.budderman18.IngotSurvivalGames.Core.Lobby;
import com.budderman18.IngotSurvivalGames.main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * This class handles all the commands normal players use
 */
public class SGCommand implements TabExecutor {
    //retrive plugin instance
    Plugin plugin = main.getInstance();
    //used if the given file isnt in another folder
    final String ROOT = "";
    //imports classes
    Lobby lobby = new Lobby();
    //imports files;
    FileManager getdata = new FileManager();
    File tempf = new File("plugins/IngotWarn","temp.yml");
    FileConfiguration language = getdata.getCustomData(plugin, "language", ROOT);
    FileConfiguration temp = getdata.getCustomData(plugin, "temp", ROOT);
    //language variables
    String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message"));
    String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("No-Permission-Message"));
    String incorrectCommandMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Incorrect-Command-Message"));
    String SGJoinJoinedGameMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGJoin-Joined-Game-Message"));
    String SGJoinAlreadyPlayingMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGJoin-Already-Playing-Message"));
    String SGLeaveLeftGameMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGLeave-Left-Game-Message"));
    String SGLeaveNotPlayingMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGLeave-Not-Playing-Message"));
    String SGHelpStartMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-Start-Message"));
    String SGHelpJoinMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-Join-Message"));
    String SGHelpRandomJoinMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-RandomJoin-Message"));
    String SGHelpLeaveMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-Leave-Message"));
    String SGHelpListMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-List-Message"));
    String SGHelpHelpMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-Help-Message"));
    String SGHelpEndMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-End-Message"));
    //game variables
    byte gameCount = 0;
    /*
    * This method handles the SG command
    */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (cmd.getName().equalsIgnoreCase("sg")) {
            //check if command is empty to prevent errors
            if (args.length > 0) {
                if (sender.hasPermission("ingotsg.sg")) {
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sg.join")) {
                        //join feature
                        if (args[0].equalsIgnoreCase("join")) {
                            //check if player isnt ingame
                            if (temp.getBoolean(player.getName() + '.' + "inGame") == false) {
                                //notify player
                                sender.sendMessage(prefixMessage + SGJoinJoinedGameMessage);
                                //run joinLobby
                                lobby.joinLobby(player);
                            }
                            //run if player is in game
                            else {
                                //notify player
                                sender.sendMessage(prefixMessage + SGJoinAlreadyPlayingMessage);
                            }
                            return true;
                        }
                    }
                    if (sender.hasPermission("ingotsg.sg.randomJoin")) {
                        //randomJoin feature
                        if (args[0].equalsIgnoreCase("randomJoin")) {
                            //check if player isnt ingame
                            if (temp.getBoolean(player.getName() + '.' + "inGame") == false) {
                                //notify player
                                sender.sendMessage(prefixMessage + SGJoinJoinedGameMessage);
                                //run joinLobby
                                lobby.joinLobby(player);
                            }
                            //run if player is in game
                            else {
                                //notify player
                                sender.sendMessage(prefixMessage + SGJoinAlreadyPlayingMessage);
                            }
                            return true;
                        }
                    }
                    if (sender.hasPermission("ingotsg.sg.leave")) {
                        //leave feature
                        if (args[0].equalsIgnoreCase("leave")) {
                            //check if player isn't ingame
                            if (temp.getBoolean(player.getName() + '.' + "inGame") == true) {
                                //notify player
                                sender.sendMessage(prefixMessage + SGLeaveLeftGameMessage);
                                //run joinLobby
                                lobby.leaveLobby(player);
                            }
                            //run if player is inGame
                            else {
                                //notify player
                                sender.sendMessage(prefixMessage + SGLeaveNotPlayingMessage);
                            }
                            return true;
                        }
                    }
                    if (sender.hasPermission("ingotsg.sg.list")) {
                        //list feature
                        if (args[0].equalsIgnoreCase("list")) {
                            return true;
                        }
                    }
                    if (sender.hasPermission("ingotsg.sg.help")) {
                        //help feature
                        if (args[0].equalsIgnoreCase("help")) {
                            //send help menu
                            sender.sendMessage(SGHelpStartMessage);
                            sender.sendMessage(SGHelpJoinMessage);
                            sender.sendMessage(SGHelpRandomJoinMessage);
                            sender.sendMessage(SGHelpLeaveMessage);
                            sender.sendMessage(SGHelpListMessage);
                            sender.sendMessage(SGHelpHelpMessage);
                            sender.sendMessage(SGHelpEndMessage);
                            return true;
                        }
                    }
                    //no subpremission
                    else {
                        sender.sendMessage(prefixMessage + noPermissionMessage);
                        return true;
                    }
                }
                //no permission
                else {
                    sender.sendMessage(prefixMessage + noPermissionMessage);
                    return true;
                }
            }
            //no argument specified
            else {
                sender.sendMessage(prefixMessage + incorrectCommandMessage);
                return false;
            }
        }
        return false;
    }
    /*
    * This method handles tabcompletion when required
    */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //main command args
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("join");
            arguments.add("randomJoin");
            arguments.add("leave");
            arguments.add("list");
            arguments.add("help");
            return arguments;
        }
        return null;
    } 
}
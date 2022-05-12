package com.budderman18.IngotSurvivalGames.Commands;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotSurvivalGames.Core.Lobby;
import com.budderman18.IngotMinigamesAPI.Main;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * 
 * This class handles all the commands normal players use. 
 * 
 */
public class SGCommand implements TabExecutor {
    //retrive plugin instance
    private final Plugin plugin = Main.getInstance();
    //used if the given file isnt in another folder
    private final String ROOT = "";
    //imports classes
    private final Lobby lobby = new Lobby();
    private final Random random = new Random(); 
    private final Arena Arena = new Arena(plugin);
    private final IngotPlayer IngotPlayer = new IngotPlayer(plugin);
    //imports files;
    private FileConfiguration config = FileManager.getCustomData(plugin, "config", ROOT);
    private FileConfiguration language = FileManager.getCustomData(plugin, "language", ROOT);
    //language variables
    private String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message"));
    private String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("No-Permission-Message"));
    private String incorrectCommandMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Incorrect-Command-Message"));
    private String joinJoinedGameMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGJoin-Joined-Game-Message"));
    private String joinAlreadyPlayingMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGJoin-Already-Playing-Message"));
    private String joinArenaLacksSpawnsMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGJoin-Arena-Lacks-Spawns-Message"));
    private String joinArenaMissingMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGJoin-Arena-Missing-Message"));
    private String joinArenaFullMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGJoin-Arena-Full-Message"));
    private String joinArenaRunningMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGJoin-Arena-Running-Message"));
    private String leaveLeftGameMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGLeave-Left-Game-Message"));
    private String leaveNotPlayingMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGLeave-Not-Playing-Message"));
    private String listStartMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGList-Start-Message"));
    private String listStatusMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGList-Arena-Status-Message"));
    private String listEndMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGList-End-Message"));
    private String helpStartMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-Start-Message"));
    private String helpJoinMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-Join-Message"));
    private String helpRandomJoinMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-RandomJoin-Message"));
    private String helpLeaveMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-Leave-Message"));
    private String helpListMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-List-Message"));
    private String helpHelpMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-Help-Message"));
    private String helpEndMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGHelp-End-Message"));
    private String playerOnlyMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Player-Only-Message"));
    /**
    * 
    * This method verifies that a player can join the arena.
    * 
    */
    private boolean verifyJoinabble(CommandSender sender, Arena loadedArena, Player player, String[] args) {
        try {
            //load arena
            loadedArena = Arena.selectArena(args[1], false, null, null);
            //check if arena is not full
            if ((loadedArena.getCurrentPlayers() < loadedArena.getMaxPlayers()) && loadedArena.getMaxPlayers() != 0) {
                //check if arena is active
                if (loadedArena.getIsActive() == false && loadedArena.getCurrentSpawn() - 1 == loadedArena.getMaxPlayers()) {
                    if (sender.hasPermission(loadedArena.getPermission()) || sender.hasPermission("ingotsg.arenas.*")) {
                        //join lobby
                        lobby.joinLobby(player, loadedArena);
                        sender.sendMessage(prefixMessage + joinJoinedGameMessage + loadedArena.getName());
                        return true;
                    } else {
                        sender.sendMessage(prefixMessage + noPermissionMessage);
                    }
                } else if (loadedArena.getCurrentSpawn() - 1 != loadedArena.getMaxPlayers()) {
                    sender.sendMessage(prefixMessage + joinArenaLacksSpawnsMessage);
                    return true;
                } //run if arena is active
                else {
                    sender.sendMessage(prefixMessage + joinArenaRunningMessage);
                    return true;
                }
            } //run if game is full
            else {
                sender.sendMessage(prefixMessage + joinArenaFullMessage);
                return true;
            }
        } //run if there is no game specified
        catch (NullPointerException | IndexOutOfBoundsException ex) {
            sender.sendMessage(prefixMessage + joinArenaMissingMessage);
            //end command
            return true;
        }
        return false;
    }
    /**
    *
    * This method handles the SG command. 
    *
    * @param sender
    * @param cmd
    * @param label
    * @param args
    * @return 
    * command
    */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //files
        FileConfiguration arenaData = null;
        //ingot player
        IngotPlayer iplayer = null;
        boolean inGame = false;
        //get player
        Player player = Bukkit.getPlayer(sender.getName());
        //local vars
        List<String> name = null;
        List<String> table = null;
        List<String> paths = new ArrayList<>();
        byte randEnd = 1;
        byte randNum = 0;
        Arena loadedArena = null;
        String arenaName = null;
        List<String> templist = null;
        //check if command is /sg
        if (cmd.getName().equalsIgnoreCase("sg") && (sender instanceof Player)) {
            //obtain ingotplayer
            iplayer = IngotPlayer.selectPlayer(sender.getName(), false, null, null);
            inGame = iplayer.getInGame();
            //check if command is empty to prevent errors
            if (args.length > 0) {
                //check if player has main permission
                if (sender.hasPermission("ingotsg.sg")) {
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sg.join")) {
                        //join feature
                        if (args[0].equalsIgnoreCase("join")) {
                            //check if player isnt ingame
                            if (args.length > 1) {
                                //check if player is ingame
                                if (inGame == false) {
                                    //try to join game
                                    return verifyJoinabble(sender, loadedArena, player, args);
                                }
                                //run if player is in game
                                else {
                                    //run if player is already playing
                                    sender.sendMessage(prefixMessage + joinAlreadyPlayingMessage);
                                    //end command
                                    return true;
                                }
                            }
                            //run if arena doesn't exist
                            else {
                                sender.sendMessage(prefixMessage + joinArenaMissingMessage);
                                //end command
                                return true;
                            }
                        }
                    }
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sg.randomJoin")) {
                        //randomJoin feature
                        if (args[0].equalsIgnoreCase("randomJoin")) {
                            //check if random tables are enabled
                            if (args.length > 1 && config.getBoolean("RandomJoin.enable") == true) {
                                //check if player isnt ingame
                                if (inGame == false) {
                                    try {
                                        for (byte h=1; h < 127; h++) {
                                            if (!config.getStringList("RandomJoin.tables.Table" + h + ".arenas").isEmpty()) {
                                                templist = config.getStringList("RandomJoin.tables.Table" + h + ".arenas");
                                                //cycle through all tables
                                                for (byte i=1; i < templist.size(); i++) {
                                                    //check if name and table are equal
                                                    if (templist.get(i) != null) {
                                                        //increment end value
                                                        randEnd++;
                                                    }
                                                }
                                                //randomize arena
                                                randNum = (byte) random.nextInt(0, randEnd);
                                                //load arena
                                                for (byte i=0; i < config.getStringList("RandomJoin.tables.Table" + h + ".arenas").size(); i++) {
                                                    if (i == randNum) {
                                                        arenaName = config.getStringList("RandomJoin.tables.Table" + h + ".arenas").get(i);
                                                    }
                                                }
                                                player.sendMessage("RandNumber: " + randNum + " Arena: " + arenaName);
                                                loadedArena = Arena.selectArena(arenaName, false, null, null);
                                                //try to join game
                                                return verifyJoinabble(sender, loadedArena, player, args);
                                            }
                                            else {
                                                break;
                                            }
                                        }
                                    }
                                    //run is no table is specified
                                    catch (NullPointerException | IndexOutOfBoundsException ex) {
                                        sender.sendMessage(prefixMessage + joinArenaMissingMessage);
                                        //end command
                                        return true;
                                    }
                                }
                                //run if player is in game
                                else {
                                    sender.sendMessage(prefixMessage + joinAlreadyPlayingMessage);
                                    //end command
                                    return true;
                                }
                            }
                            //check if random tables are disabled
                            if (args.length > 0 && config.getBoolean("RandomJoin.enable") == false) {
                                //check if player isnt ingame
                                if (inGame == false) {
                                    try {
                                        //get files 
                                        name = FileManager.getArenas(plugin);
                                        table = name;
                                        //cycle through all tables
                                        for (byte i=0; i < name.size(); i++) {
                                            //check if name and table are equal
                                            if (name.get(i).equals(table.get(i))) {
                                                //increment random end value
                                                randEnd++;
                                            }
                                        }
                                        //randomize spawn
                                        randNum = (byte) random.nextInt(0, randEnd);
                                        //load arena
                                        loadedArena = Arena.selectArena(name.get(randNum), true, arenaData, paths);
                                        //try to join game
                                        return verifyJoinabble(sender, loadedArena, player, args);
                                    }
                                    //run if there's invalid args
                                    catch (NullPointerException | IndexOutOfBoundsException ex) {
                                        sender.sendMessage(prefixMessage + joinArenaMissingMessage);
                                        //end command
                                        return true;
                                    }
                                }
                                //run if player is in game
                                else {
                                    sender.sendMessage(prefixMessage + joinAlreadyPlayingMessage);
                                    //end command
                                    return true;
                                }
                            }
                            //run if arena doesn't exist
                            else {
                                sender.sendMessage(prefixMessage + joinArenaMissingMessage);
                                //end command
                                return true;
                            }
                        }
                    }
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sg.leave")) {
                        //leave feature
                        if (args[0].equalsIgnoreCase("leave")) {
                            //check if player isn't ingame
                            if (inGame == true) {
                                try {
                                    //load arena
                                    loadedArena = Arena.selectArena(iplayer.getGame(), false, null, null);
                                    //notify player
                                    sender.sendMessage(prefixMessage + leaveLeftGameMessage);
                                    //leave lobby
                                    lobby.leaveLobby(player, loadedArena, false);
                                }
                                //run if arena doesn't exist
                                catch (NullPointerException ex) {
                                    sender.sendMessage(prefixMessage + joinArenaMissingMessage);
                                    return true;
                                }
                            }
                            //run if player is inGame
                            else {
                                //notify player
                                sender.sendMessage(prefixMessage + leaveNotPlayingMessage);
                            }
                            return true;
                        }
                    }
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sg.list")) {
                        //list feature
                        if (args[0].equalsIgnoreCase("list")) {
                            //header
                            sender.sendMessage(listStartMessage);
                            //get loaded arenas
                            name = FileManager.getArenas(plugin);
                            Arena tempArena = null;
                            //cycle through loaded arenas
                            for (byte i=0; i < name.size(); i++) {
                                //arena
                                tempArena = Arena.selectArena(name.get(i), false, null, null);
                                sender.sendMessage(tempArena.getName() + listStatusMessage + tempArena.getStatus());
                            }
                            //footer
                            sender.sendMessage(listEndMessage);
                            //end command
                            return true;
                        }
                    }
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sg.help")) {
                        //help feature
                        if (args[0].equalsIgnoreCase("help")) {
                            //send help menu
                            sender.sendMessage(helpStartMessage);
                            sender.sendMessage(helpJoinMessage);
                            sender.sendMessage(helpRandomJoinMessage);
                            sender.sendMessage(helpLeaveMessage);
                            sender.sendMessage(helpListMessage);
                            sender.sendMessage(helpHelpMessage);
                            sender.sendMessage(helpEndMessage);
                            //end command
                            return true;
                        }
                    }
                    //no subpremission
                    else {
                        sender.sendMessage(prefixMessage + noPermissionMessage);
                        //end command
                        return true;
                    }
                }
                //no permission
                else {
                    sender.sendMessage(prefixMessage + noPermissionMessage);
                    //end command
                    return true;
                }
            }
            //no argument specified
            else {
                sender.sendMessage(prefixMessage + incorrectCommandMessage);
                //end command
                return false;
            }
        }
        else {
            sender.sendMessage(prefixMessage + playerOnlyMessage);
            //end command
            return true;
        }
        //should never have to run, but won't compile without it
        return false;
    }
    /**
    *
    * This method handles tabcompletion when required. 
    *
    * @param sender
    * @param command
    * @param alias
    * @param args
    * @return 
    * tab-completion
    */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> arguments = new ArrayList<>();
        List<String> name = null;
        //main command args
        if (args.length == 1) {
            arguments.add("join");
            arguments.add("randomJoin");
            arguments.add("leave");
            arguments.add("list");
            arguments.add("help");
        }
        //join command args
        if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
            //get all arenas
            name = FileManager.getArenas(plugin);
            //cycle through all arenas
            for (byte i=0; i < name.size(); i++) {
                //add argument
                arguments.add(name.get(i));
            }
        }
        //randomjoin args
        if (args.length == 2 && args[0].equalsIgnoreCase("randomJoin") && config.getBoolean("RandomJoin.enable") == true) {
            //cycle through all tables
            for (byte i=1; i < 127; i++) {
                if (config.getString("RandomJoin.tables.Table" + i + ".name") != null) {
                    arguments.add(config.getString("RandomJoin.tables.Table" + i + ".name"));
                }
                else {
                    break;
                }
            }
        }
        //return tab-completion
        return arguments;
    } 
}
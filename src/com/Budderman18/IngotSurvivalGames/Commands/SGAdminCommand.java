package com.budderman18.IngotSurvivalGames.Commands;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.Spawn;
import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Main;
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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * 
 * This class handles all the management commands staff will use. 
 * 
 */
public class SGAdminCommand implements TabExecutor {
    //retrive plugin instance
    private final Plugin plugin = Main.getInstance();
    //used if the given file isnt in another folder
    private final String ROOT = "";
    //imports files;
    private FileConfiguration language = FileManager.getCustomData(plugin, "language", ROOT);
    private FileConfiguration config = FileManager.getCustomData(plugin, "config", ROOT);
    private FileConfiguration arenaData = null;
    private File arenadataf = null;
    //classes
    private final Arena Arena = new Arena(plugin);
    private final Spawn Spawn = new Spawn(plugin);
    //global vars
    private int[] pos1 = null;
    private int[] pos2 = null;
    private Arena currentArena = null;
    private Spawn currentSpawn = null;
    private double tempx = 0;
    private double tempy = 0;
    private double tempz = 0;
    private List<Spawn> spawns = new ArrayList<>();
    private List<String> spawnNames = new ArrayList<>();
    //language
    private String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message"));
    private String noPermissionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("No-Permission-Message"));
    private String playerOnlyMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Player-Only-Message"));
    private String arenaInvalidArenaMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Invalid-Arena-Message"));
    private String arenaPos1SetMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Pos1-Set-Message"));
    private String arenaPos2SetMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Pos2-Set-Message"));
    private String arenaCreateInvalidNameMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Create-Invalid-Name-Message"));
    private String arenaCreateInvalidMaxPlayersMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Create-Invalid-Max-Players-Message"));
    private String arenaCreateInvalidPositionsMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Create-Invalid-Positions-Message"));
    private String arenaCreateWrongPositionsMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Create-Wrong-Positions-Message"));
    private String arenaCreateCreatedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Create-Arena-Created-Message"));
    private String arenaDeleteDeletedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Delete-Arena-Deleted-Message"));
    private String arenaEditEditedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Edit-Editied-Message"));
    private String arenaEditInvalidArgumentMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Edit-Invalid-Argument-Message"));
    private String arenaSelectSelectedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Select-Arena-Selected-Message"));
    private String arenaSelectInvalidArenaMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Select-Invalid-Arena-Message"));
    private String arenaSpawnCreateSpawnCreatedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Spawn-Create-Spawn-Created-Message"));
    private String arenaSpawnCreateInvalidSpawnMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Spawn-Create-Invalid-Spawn-Message"));
    private String arenaSpawnDeleteSpawnDeletedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Spawn-Delete-Spawn-Deleted-Message"));
    private String arenaSpawnDeleteInvalidSpawnMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Spawn-Delete-Invalid-Spawn-Message"));
    private String arenaSpawnListStart1Message = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Spawn-List-Start1-Message"));
    private String arenaSpawnListStart2Message = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Spawn-List-Start2-Message"));
    private String arenaSpawnListEndMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Spawn-List-End-Message"));
    private String arenaRegenerateRegenerated1Message = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Regenerate-Arena-Regenerated-1-Message"));
    private String arenaRegenerateRegenerated2Message = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Regenerate-Arena-Regenerated-2-Message"));
    private String arenaRegenerateInvalidArenaMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Arena-Regenerate-Invalid-Arena-Message"));
    private String helpStartMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Start-Message"));
    private String helpArenaMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Message"));
    private String helpArenaCreateMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Create-Message"));
    private String helpArenaDeleteMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Delete-Message"));
    private String helpArenaEditMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Edit-Message"));
    private String helpArenaRegenerateMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Regenerate-Message"));
    private String helpArenaSelectMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Select-Message"));
    private String helpArenaSpawnMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Spawn-Message"));
    private String helpArenaSpawnCreateMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Spawn-Create-Message"));
    private String helpArenaSpawnDeleteMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Spawn-Delete-Message"));
    private String helpArenaSpawnListMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Spawn-List-Message"));
    private String helpArenaPos1Message = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Pos1-Message"));
    private String helpArenaPos2Message = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Arena-Pos2-Message"));
    private String helpHelpMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Help-Message"));
    private String helpReloadMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Reload-Message"));
    private String helpVersionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-Version-Message"));
    private String helpEndMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdminHelp-End-Message"));
    private String reloadMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Reload-Message"));
    private String versionMessage = ChatColor.translateAlternateColorCodes('&', language.getString("SGAdmin-Version-Message"));
    /**
    *
    * This method handles the SGAdmin command. 
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
        //local vars
        String version = null;
        //get player
        Player player = Bukkit.getPlayer(sender.getName());
        //check if command is /sg
        if (cmd.getName().equalsIgnoreCase("sgadmin") && (sender instanceof Player)) {
            //check if command is empty to prevent errors
            if (args.length > 0) {
                //check if sender has default permission
                if (sender.hasPermission("ingotsg.sgadmin")) {
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sgadmin.arena")) {
                        //arena feature
                        if (args[0].equalsIgnoreCase("arena")) {
                            //check if player has permission(s)
                            if (sender.hasPermission("ingotsg.sgadmin.arena.create")) {
                                //create
                                if (args[1].equalsIgnoreCase("create")) {
                                    //check if arenaName is null
                                    if (args[2] == null) {
                                        sender.sendMessage(prefixMessage + arenaCreateInvalidNameMessage);
                                        //end command
                                        return true;
                                    }
                                    //check if maxPlayers is null
                                    if (args[3] ==  null) {
                                        sender.sendMessage(prefixMessage + arenaCreateInvalidMaxPlayersMessage);
                                        //end command
                                        return true;
                                    }
                                    //check if pos1 or pos2 is not set
                                    else if (pos1 == null || pos2 == null) {
                                        sender.sendMessage(prefixMessage + arenaCreateInvalidPositionsMessage);
                                        //end command
                                        return true;
                                    }
                                    //run if everything is set right
                                    else {
                                        //check if pos1 is not greater than pos2
                                        if (!(pos1[0] > pos2[0]) || pos1[1] > pos2[1] || pos1[2] > pos2[2]) {
                                            //create arena
                                            currentArena = Arena.createArena(pos1, pos2, player.getWorld().getName(), args[2], Byte.parseByte(args[3]), true, "/Arenas/" + args[2] + '/');
                                            currentArena.createArenaSchematic();
                                            //clear pos arrays
                                            pos1 = null;
                                            pos2 = null;
                                            sender.sendMessage(prefixMessage + arenaCreateCreatedMessage);
                                            //end command
                                            return true;
                                        }
                                        //run if pos1 is greater than pos1
                                        else {
                                            sender.sendMessage(prefixMessage + arenaCreateWrongPositionsMessage);
                                            //end command
                                            return true;
                                        }
                                    }
                                }
                            }
                            //check if player has permission(s)
                            if (sender.hasPermission("ingotsg.sgadmin.arena.delete")) {
                                //delete
                                if (args[1].equalsIgnoreCase("delete")) {
                                    try {
                                        //load arena
                                        currentArena = Arena.selectArena(args[2], false, null, null);
                                        //delete arena
                                        currentArena.deleteArena();
                                        sender.sendMessage(prefixMessage + arenaDeleteDeletedMessage);
                                        //end command
                                        return true;
                                    }
                                    //run if arena doesnt exist
                                    catch (IllegalArgumentException | ArrayIndexOutOfBoundsException ex) {
                                        sender.sendMessage(prefixMessage + arenaInvalidArenaMessage);
                                        //end command
                                        return true;
                                    }
                                }
                            }
                            //check if player has permission(s)
                            if (sender.hasPermission("ingotsg.sgadmin.arena.edit")) {
                                //edit
                                if (args[1].equalsIgnoreCase("edit")) {
                                    //check if there is a specifed arena
                                    if (args.length > 4) {
                                        try {
                                            //load arena
                                            currentArena = Arena.selectArena(args[3], false, null, null);
                                            //check if name is being changed
                                            if (args[2].equalsIgnoreCase("name")) {
                                                //set arena's name
                                                currentArena.setName(args[4], true,  "/Arenas/" + args[4] + '/');
                                                sender.sendMessage(prefixMessage + arenaEditEditedMessage + "name");
                                                //end command
                                                return true;
                                            }
                                            //check if maxPLayers is being changed
                                            if (args[2].equalsIgnoreCase("maxPlayers")) {
                                                try {
                                                    //set arena's maxPlayers
                                                    currentArena.setMaxPlayers(Byte.parseByte(args[4]), true);
                                                    sender.sendMessage(prefixMessage + arenaEditEditedMessage + "maxPlayers");
                                                    //end command
                                                    return true;
                                                }
                                                //run if maxPlayers is invalid
                                                catch (NumberFormatException ex) {
                                                    sender.sendMessage(prefixMessage + arenaEditInvalidArgumentMessage);
                                                    //end command
                                                    return true;
                                                }
                                            } 
                                            //run if edit args are invalid
                                            else {
                                                sender.sendMessage(prefixMessage + arenaEditInvalidArgumentMessage);
                                                //end command
                                                return true;
                                            }
                                        } 
                                        //run if arena is invalid
                                        catch (IllegalArgumentException ex) {
                                            sender.sendMessage(prefixMessage + arenaInvalidArenaMessage);
                                            //end command
                                            return true;
                                        }
                                    }
                                    //check if using selected arena
                                    if (args.length == 4) {
                                        try {
                                            //check if name is being changed
                                            if (args[2].equalsIgnoreCase("name")) {
                                                //set name
                                                currentArena.setName(args[3], true, "/Arenas/" + args[3] + '/');
                                                sender.sendMessage(prefixMessage + arenaEditEditedMessage + "name");
                                                //end command
                                                return true;
                                            }
                                            //check if maxPlayers is being changed
                                            if (args[2].equalsIgnoreCase("maxPlayers")) {
                                                try {
                                                    //set maxPlayers
                                                    currentArena.setMaxPlayers(Byte.parseByte(args[3]), true);
                                                    sender.sendMessage(prefixMessage + arenaEditEditedMessage + "maxPlayers");
                                                    //end command
                                                    return true;
                                                }
                                                //run if maxPlayers is invalid
                                                catch (NumberFormatException ex) {
                                                    sender.sendMessage(prefixMessage + arenaEditInvalidArgumentMessage);
                                                    //end command
                                                    return true;
                                                }
                                            } 
                                            //run if arguments are invalid
                                            else {
                                                sender.sendMessage(prefixMessage + arenaEditInvalidArgumentMessage);
                                                //end command
                                                return true;
                                            }
                                        }
                                        //run if arena is invalid
                                        catch (NullPointerException ex) {
                                            sender.sendMessage(prefixMessage + arenaInvalidArenaMessage);
                                            //end command
                                            return true;
                                        }
                                    } 
                                    //run if edit args are invalid
                                    else {
                                        sender.sendMessage(prefixMessage + arenaEditInvalidArgumentMessage);
                                        return true;
                                    }
                                }
                            }
                            //check if player has permission(s)
                            if (sender.hasPermission("ingotsg.sgadmin.arena.select")) {
                                //select
                                if (args[1].equalsIgnoreCase("select")) {
                                    //check if name was entered
                                    if (args.length > 2) {
                                        try {
                                            //load arena
                                            currentArena = Arena.selectArena(args[2], false, null, null);
                                            sender.sendMessage(prefixMessage + arenaSelectSelectedMessage);
                                            //end command
                                            return true;
                                        }
                                        //run if arena is invalid
                                        catch (IllegalArgumentException ex) {
                                            sender.sendMessage(prefixMessage + arenaInvalidArenaMessage);
                                            return true;
                                        }
                                    }
                                    //run if select args are invalid
                                    else {
                                        sender.sendMessage(prefixMessage + arenaSelectInvalidArenaMessage);
                                        //end command
                                        return true;
                                    }
                                }
                            }
                            //check if player has permission(s)
                            if (sender.hasPermission("ingotsg.sgadmin.arena.regenerate")) {
                                //regenerate
                                if (args[1].equalsIgnoreCase("regenerate")) {
                                    try {
                                        //check if name is not null
                                        if (args[2] != null) {
                                            try {
                                                sender.sendMessage(prefixMessage + arenaRegenerateRegenerated1Message);
                                                //select arena
                                                currentArena = Arena.selectArena(args[2], false, null, null);
                                                //regenerate arena
                                                currentArena.loadArenaSchematic();
                                                sender.sendMessage(prefixMessage + arenaRegenerateRegenerated2Message);
                                                //end command
                                                return true;
                                            }
                                            //run is name is invalid
                                            catch (IllegalArgumentException ex) {
                                                sender.sendMessage(prefixMessage + arenaInvalidArenaMessage);
                                                //end command
                                                return true;
                                            }
                                        }
                                        //run if regen args are invalid
                                        else {
                                            sender.sendMessage(prefixMessage + arenaRegenerateInvalidArenaMessage);
                                            //end command
                                            return true;
                                        }
                                    }
                                    //run if using selected arena
                                    catch (ArrayIndexOutOfBoundsException ex) {
                                        //check if arena is not null
                                        if (currentArena != null) {
                                            try {
                                                sender.sendMessage(prefixMessage + arenaRegenerateRegenerated1Message);
                                                //regenerate arena
                                                currentArena.loadArenaSchematic();
                                                sender.sendMessage(prefixMessage + arenaRegenerateRegenerated2Message);
                                                //end command
                                                return true;
                                            }
                                            //run if arena is somehow invalid
                                            catch (IllegalArgumentException exc) {
                                                sender.sendMessage(prefixMessage + arenaInvalidArenaMessage);
                                                //end command
                                                return true;
                                            }
                                        }
                                        //run if regen args are invalid
                                        else {
                                            sender.sendMessage(prefixMessage + arenaRegenerateInvalidArenaMessage);
                                            //end command
                                            return true;
                                        }
                                    }
                                }
                            }
                            //check if player has permission(s)
                            if (sender.hasPermission("ingotsg.sgadmin.arena.spawn")) {
                                //spawn
                                if (args[1].equalsIgnoreCase("spawn")) {
                                    //check if player has permission(s)
                                    if (sender.hasPermission("ingotsg.sgadmin.arena.spawn.create")) {
                                        //create spawn
                                        if (args[2].equalsIgnoreCase("create")) {
                                            //get files
                                            arenadataf = new File(plugin.getDataFolder() + "/Arenas/" + args[3] + '/', "settings.yml");
                                            arenaData = FileManager.getCustomData(plugin, "settings", "/Arenas/" + args[3] + '/');
                                            //get arenas
                                            currentArena = Arena.selectArena(args[3], false, null, null);
                                            //set positions
                                            tempx = player.getLocation().getX();
                                            tempy = player.getLocation().getY();
                                            tempz = player.getLocation().getZ();
                                            //check if positions should be centerized
                                            if (config.getBoolean("centerize-teleport-locations") == true) {
                                                //check if x is negative
                                                if (tempx < 0) {
                                                    //centerize x
                                                    tempx = (int) tempx;
                                                    tempx+=0.5;
                                                }
                                                //check if x is positive
                                                if (tempx >= 0) {
                                                    //centerize x
                                                    tempx = (int) tempx;
                                                    tempx-=0.5;
                                                }
                                                //set y
                                                tempy = (int) tempy;
                                                //check if z is negative
                                                if (tempz < 0) {
                                                    //centerize z
                                                    tempz = (int) tempx;
                                                    tempz+=0.5;
                                                }
                                                //check if z is positive
                                                if (tempz >= 0) {
                                                    //centerize z
                                                    tempz = (int) tempx;
                                                    tempz-=0.5;
                                                }
                                            }
                                            //cerate spawn
                                            currentSpawn = new Spawn(plugin);
                                            currentSpawn = Spawn.createSpawn("Spawn" + Long.toString(currentArena.getCurrentSpawn()), tempx, tempy, tempz);
                                            //add spawn to arena
                                            currentArena.addSpawn(currentSpawn);
                                            //set and save file
                                            arenaData.createSection("Spawnpoints.Spawn" + Long.toString(currentArena.getCurrentSpawn()));
                                            arenaData.set("Spawnpoints.Spawn" + Long.toString(currentArena.getCurrentSpawn()) + ".name", currentSpawn.getName());
                                            arenaData.set("Spawnpoints.Spawn" + Long.toString(currentArena.getCurrentSpawn()) + ".x", currentSpawn.getLocation()[0]);
                                            arenaData.set("Spawnpoints.Spawn" + Long.toString(currentArena.getCurrentSpawn()) + ".y", currentSpawn.getLocation()[1]);
                                            arenaData.set("Spawnpoints.Spawn" + Long.toString(currentArena.getCurrentSpawn()) + ".z", currentSpawn.getLocation()[2]);
                                            try {
                                                arenaData.save(arenadataf);
                                            } 
                                            catch (IOException ex) {
                                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            sender.sendMessage(prefixMessage + arenaSpawnCreateSpawnCreatedMessage);
                                            //end command
                                            return true;
                                        }
                                    }
                                    //check if player has permission(s)
                                    if (sender.hasPermission("ingotsg.sgadmin.arena.spawn.delete")) {
                                        //delete spawn
                                        if (args[2].equalsIgnoreCase("delete")) {
                                            //get files
                                            arenadataf = new File(plugin.getDataFolder() + "/Arenas/" + args[3] + '/', "settings.yml");
                                            arenaData = FileManager.getCustomData(plugin, "settings", "/Arenas/" + args[3] + '/');
                                            //get spawn
                                            currentSpawn = Spawn.selectSpawn(args[3], true, arenaData, arenaData.getStringList("Spawnpoints.Spawn" + Long.toString(currentArena.getCurrentSpawn())));
                                            //remove spawn
                                            currentArena.removeSpawn(currentSpawn);
                                            currentSpawn.deleteSpawn();
                                            //set and save file
                                            arenaData.set("Spawnpoints.Spawn" + Long.toString(currentArena.getCurrentSpawn()), null);
                                            try {
                                                arenaData.save(arenadataf);
                                            } 
                                            catch (IOException ex) {
                                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            sender.sendMessage(prefixMessage + arenaSpawnDeleteSpawnDeletedMessage);
                                            //end command
                                            return true;
                                        }
                                    }
                                    //check if player has permission(s)
                                    if (sender.hasPermission("ingotsg.sgadmin.arena.spawn.list")) {
                                        //list spawns
                                        if (args[2].equalsIgnoreCase("list")) {
                                            //get file
                                            arenaData = FileManager.getCustomData(plugin, "settings", "/Arenas/" + args[3] + '/');
                                            spawnNames = arenaData.getStringList("Spawnpoints");
                                            sender.sendMessage(arenaSpawnListStart1Message + args[3] + arenaSpawnListStart2Message);
                                            //cycle through all spawns
                                            for (byte i = 1; i < spawnNames.size(); i++) {
                                                //get spawn
                                                currentSpawn = Spawn.selectSpawn("Spawn" + Byte.toString(i), true, arenaData, arenaData.getStringList("Spawnpoints.Spawn" + Byte.toString(i)));
                                                sender.sendMessage(currentSpawn.getName() + ": x: " + Double.toString(currentSpawn.getLocation()[0]) + " y: " + Double.toString(currentSpawn.getLocation()[1]) + " z: " + Double.toString(currentSpawn.getLocation()[2]));
                                            }
                                            sender.sendMessage(arenaSpawnListEndMessage);
                                            //end command
                                            return true;
                                        }
                                    }
                                }
                            }
                            //check if player has permission(s)
                            if (sender.hasPermission("ingotsg.sgadmin.arena.pos1")) {
                                //pos1
                                if (args[1].equalsIgnoreCase("pos1")) {
                                    //set pos1 from player pos
                                    pos1 = new int[3];
                                    pos1[0] = player.getLocation().getBlockX();
                                    pos1[1] = player.getLocation().getBlockY();
                                    pos1[2] = player.getLocation().getBlockZ();
                                    sender.sendMessage(prefixMessage + arenaPos1SetMessage + pos1[0] + ',' + pos1[1] + ',' + pos1[2]);
                                    //end command
                                    return true;
                                }
                            }
                            //check if player has permission(s)
                            if (sender.hasPermission("ingotsg.sgadmin.arena.pos2")) {
                                //pos2
                                if (args[1].equalsIgnoreCase("pos2")) {
                                    //set pos2 from player pos
                                    pos2 = new int[3];
                                    pos2[0] = player.getLocation().getBlockX();
                                    pos2[1] = player.getLocation().getBlockY();
                                    pos2[2] = player.getLocation().getBlockZ();
                                    sender.sendMessage(prefixMessage + arenaPos2SetMessage + pos2[0] + ',' + pos2[1] + ',' + pos2[2]);
                                    //end command
                                    return true;
                                }
                            }
                            //no sub-sub permission
                            else {
                                sender.sendMessage(prefixMessage + noPermissionMessage);
                                return true;
                            }
                        }
                    }
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sgadmin.help")) {
                        //help feature
                        if (args[0].equalsIgnoreCase("help")) {
                            //output helplist
                            sender.sendMessage(helpStartMessage);
                            sender.sendMessage(helpArenaMessage);
                            sender.sendMessage(helpArenaCreateMessage);
                            sender.sendMessage(helpArenaDeleteMessage);
                            sender.sendMessage(helpArenaEditMessage);
                            sender.sendMessage(helpArenaRegenerateMessage);
                            sender.sendMessage(helpArenaSelectMessage);
                            sender.sendMessage(helpArenaSpawnMessage);
                            sender.sendMessage(helpArenaSpawnCreateMessage);
                            sender.sendMessage(helpArenaSpawnDeleteMessage);
                            sender.sendMessage(helpArenaSpawnListMessage);
                            sender.sendMessage(helpArenaPos1Message);
                            sender.sendMessage(helpArenaPos2Message);
                            sender.sendMessage(helpHelpMessage);
                            sender.sendMessage(helpReloadMessage);
                            sender.sendMessage(helpVersionMessage);
                            sender.sendMessage(helpEndMessage);
                            //end command
                            return true;
                        }
                    }
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sgadmin.version")) {
                        //version feature
                        if (args[0].equalsIgnoreCase("version")) {
                            //get version
                            version = language.getString("version");
                            sender.sendMessage(prefixMessage + versionMessage + version);
                            //end command
                            return true;
                        }
                    }
                    //check if player has permission(s)
                    if (sender.hasPermission("ingotsg.sgadmin.reload")) {
                        //reload feature
                        if (args[0].equalsIgnoreCase("reload")) {
                            //get files (excluding arenas atm)
                            language = FileManager.getCustomData(plugin, "language", ROOT);
                            FileConfiguration config = FileManager.getCustomData(plugin, "config", ROOT);
                            FileConfiguration playerdata = FileManager.getCustomData(plugin, "playerdata", ROOT);
                            try {
                                //load files
                                language.load(plugin.getDataFolder() + "/" + "language.yml");
                                config.load(plugin.getDataFolder() + "/" + "config.yml");
                                playerdata.load(plugin.getDataFolder() + "/" + "playerdata.yml");
                                Main.loadArenas();
                            } 
                            catch (IOException | InvalidConfigurationException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            sender.sendMessage(prefixMessage + reloadMessage);
                            //end command
                            return true;
                        }
                    }
                    //no sub permission
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
            //no arguments
            else {
                 //end command
                return false;
            }
        }
        //not a player
        else {
            sender.sendMessage(prefixMessage + playerOnlyMessage);
             //end command
            return false;
        }
        //should never have to run, but won't compile without it
        return false;
    }
    /**
    *
    * This method handles tab-completion when required. 
    *
    * @param sender
    * @param command
    * @param alias
    * @param args
    * @return 
    * Tab-Completion
    */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //create lists
        List<String> arguments = new ArrayList<>();
        List<String> name = null;
        //main command args
        if (args.length == 1) {
            arguments.add("arena");
            arguments.add("help");
            arguments.add("version");
            arguments.add("reload");
        }
        //arena main command args
        if (args.length == 2 && args[0].equalsIgnoreCase("arena")) {
            arguments.add("create");
            arguments.add("delete");
            arguments.add("edit");
            arguments.add("select");
            arguments.add("spawn");
            arguments.add("regenerate");
            arguments.add("pos1");
            arguments.add("pos2");
        }
        //edit command args
        if (args.length == 3 && args[1].equalsIgnoreCase("edit")) {
            arguments.add("name");
            arguments.add("maxPlayers");
        }
        //delete, regenerate, and select args
        if (args.length == 3 && (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("regenerate") || args[1].equalsIgnoreCase("select"))) {
            //get all arenas
            name = FileManager.getArenas(plugin);
            //cycle through al arenas
            for (byte i=0; i < name.size(); i++) {
                //add arena arg
                arguments.add(name.get(i));
            }
        }
        //spawn command args
        if (args.length == 3 && args[1].equalsIgnoreCase("spawn")) {
            arguments.add("create");
            arguments.add("delete");
            arguments.add("list");
        }
        //edit, spawn args
        if (args.length == 4 && (args[1].equalsIgnoreCase("edit") || args[1].equalsIgnoreCase("spawn"))) {
            //get all arenas
            name = FileManager.getArenas(plugin);
            //cycle through al arenas
            for (byte i=0; i < name.size(); i++) {
                //add arena arg
                arguments.add(name.get(i));
            }
        }
        //return tab-completion
        return arguments;
    } 
}

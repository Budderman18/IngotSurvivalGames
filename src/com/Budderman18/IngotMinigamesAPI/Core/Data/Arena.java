package com.budderman18.IngotMinigamesAPI.Core.Data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles everything involving arenas. 
 * This includes physical instances, schematics and settings. 
 * 
 */
public class Arena {
    /**
    *
    * This constructor updates the loaded instances list. 
    * It is done every time a new Arena class is created. 
    *
    * @param pluginn
    */
    public Arena(Plugin pluginn) {
        //set plugin var
        plugin = pluginn;
    }
    //plugin
    private static Plugin plugin = null;
    //instances
    private static List<Arena> arenas;
    //files
    private static final String ROOT = "";
    //selections
    private int[] pos1 = new int[3];
    private int[] pos2 = new int[3];
    private String world = null;
    private World realWorld = null;
    private String name = "ERROR";
    private String filePath = null;
    private boolean isActive = false;
    private int index = 0;
    //statuses
    /**
    *
    * This field displays an arena as waiting for players.
    * 
    */
    public static String WAITING = ChatColor.translateAlternateColorCodes('&', "&aWAITING");
    //statuses
    /**
    *
    * This field displays an arena as running
    * 
    */
    public static String RUNNING = ChatColor.translateAlternateColorCodes('&', "&eRUNNING");
    //statuses
    /**
    *
    * This field displays an arena as disabled.
    * 
    */
    public static String DISABLED = ChatColor.translateAlternateColorCodes('&', "&cDISABLED");
    //settings
    private String status = WAITING;
    private Permission arenaPerm = null;
    private byte currentPlayers = 0;
    private byte minPlayers = 0;
    private byte maxPlayers = 0;
    private List<Spawn> spawns = new ArrayList<>();
    private double[] lobbyPos = new double[5];
    private String lobbyWorld = "";
    private double[] exitPos = new double[5];
    private String exitWorld = "";
    private double[] specPos = new double[5];
    private double[] centerPos = new double[5];
    private int lobbyWaitTime = 0;
    private int lobbySkipTime = 0;
    private int gameWaitTime = 0;
    private int gameLengthTime = 0;
    private byte skipTimerAt = 0;
    //global vars
    private int currentSpawn = 1;
    private static int trueIndex = 0;
    /**
    *
    * This method creates a new arena
    * You must specify position arrays, the world,
    * arena's name, maxPlayers and filePath for files
    *
    * @param pos1
    * @param pos2
    * @param worldd
    * @param arenaName
    * @param minPlayerss
    * @param maxPlayerss
    * @param skipTimerAtt
    * @param lobbyWaitTimee
    * @param saveFile
    * @param gameWaitTimee
    * @param gameLengthh
    * @param lobbySkipTimee
    * @param lobbyWorldd
    * @param filePathh
    * @param exitWorldd
    * @param exitPoss
    * @param centerPoss
    * @param lobbyPoss
    * @param specPoss
    * @return 
    * newArena
    */
    public Arena createArena(int[] pos1, int[] pos2, String worldd, String arenaName, byte minPlayerss, byte maxPlayerss, byte skipTimerAtt, int lobbyWaitTimee, int lobbySkipTimee, int gameWaitTimee, int gameLengthh, double[] lobbyPoss, String lobbyWorldd, double[] exitPoss, String exitWorldd, double[] specPoss, double[] centerPoss, boolean saveFile, String filePathh) {
        //create arena object
        Arena newArena = new Arena(plugin);
        //files
        File arenaDataf = new File(plugin.getDataFolder() + filePathh, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", filePathh);
        //set arrays to prevent errors
        if (lobbyPoss == null) {
            lobbyPoss = new double[5];
        }
        if (exitPoss == null) {
            exitPoss = new double[5];
        }
        if (specPoss == null) {
            specPoss = new double[5];
        }
        if (centerPoss == null) {
            centerPoss = new double[5];
        }
        //arena.yml file
        if (saveFile == true) {
            arenaData.set("pos1.x", pos1[0]);
            arenaData.set("pos1.y", pos1[1]);
            arenaData.set("pos1.z", pos1[2]);
            arenaData.set("pos2.x", pos2[0]);
            arenaData.set("pos2.y", pos2[1]);
            arenaData.set("pos2.z", pos2[2]);
            arenaData.set("world", worldd);
            arenaData.set("name", arenaName);
            arenaData.set("minPlayers", minPlayerss);
            arenaData.set("maxPlayers", maxPlayerss);
            arenaData.set("skipTimerAt", skipTimerAtt);
            arenaData.set("lobby-wait-time", lobbyWaitTimee);
            arenaData.set("lobby-skip-time", lobbySkipTimee);
            arenaData.set("game-wait-time", gameWaitTimee);
            arenaData.set("game-length", gameLengthh);
            arenaData.set("Lobby.world", lobbyWorldd);
            arenaData.set("Lobby.x", lobbyPoss[0]);
            arenaData.set("Lobby.y", lobbyPoss[1]);
            arenaData.set("Lobby.z", lobbyPoss[2]);
            arenaData.set("Lobby.yaw", lobbyPoss[3]);
            arenaData.set("Lobby.pitch", lobbyPoss[4]);
            arenaData.set("Exit.world", exitWorldd);
            arenaData.set("Exit.x", exitPoss[0]);
            arenaData.set("Exit.y", exitPoss[1]);
            arenaData.set("Exit.z", exitPoss[2]);
            arenaData.set("Exit.yaw", exitPoss[3]);
            arenaData.set("Exit.pitch", exitPoss[4]);
            arenaData.set("Spec.x", specPoss[0]);
            arenaData.set("Spec.y", specPoss[1]);
            arenaData.set("Spec.z", specPoss[2]);
            arenaData.set("Spec.yaw", specPoss[3]);
            arenaData.set("Spec.pitch", specPoss[4]);
            arenaData.set("Center.x", centerPoss[0]);
            arenaData.set("Center.y", centerPoss[1]);
            arenaData.set("Center.z", centerPoss[2]);
            arenaData.set("Center.yaw", centerPoss[3]);
            arenaData.set("Center.pitch", centerPoss[4]);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //set fields
        newArena.pos1 = pos1;
        newArena.pos2 = pos2;
        newArena.world = worldd;
        newArena.name = arenaName;
        newArena.realWorld = Bukkit.getWorld(worldd);
        newArena.minPlayers = minPlayerss;
        newArena.maxPlayers = maxPlayerss;
        newArena.filePath = filePathh;
        newArena.index = trueIndex;
        newArena.skipTimerAt = skipTimerAtt;
        newArena.lobbyWaitTime = lobbyWaitTimee;
        newArena.lobbySkipTime = lobbySkipTimee;
        newArena.gameWaitTime = gameWaitTimee;
        newArena.gameLengthTime = gameLengthh;
        newArena.lobbyPos = lobbyPoss;
        newArena.lobbyWorld = lobbyWorldd;
        newArena.exitPos = exitPoss;
        newArena.exitWorld = exitWorldd;
        newArena.specPos = specPoss;
        newArena.centerPos = centerPoss;
        //return arena
        if (arenas == null) {
            arenas = new ArrayList<>();
        }
        newArena.arenaPerm = new Permission("ingotsg.arenas." + newArena.name);
        Bukkit.getPluginManager().addPermission(newArena.arenaPerm);
        arenas.add(newArena);
        trueIndex++;
        return newArena;
    }
    /**
    *
    * This method deletes the selected arena. 
    *
    */
    public void deleteArena() {
        //delete files
        try {
            FileUtils.deleteDirectory(new File(plugin.getDataFolder() + this.filePath));
        } 
        catch (IOException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
        //reset data
        this.pos1[0] = 0;
        this.pos1[1] = 0;
        this.pos1[2] = 0;
        this.pos2[0] = 0;
        this.pos2[1] = 0;
        this.pos2[2] = 0;
        this.world = "";
        this.name = "";
        this.realWorld = null;
        this.filePath = "";
        this.index = 0;
        Bukkit.getPluginManager().removePermission(arenaPerm);
        this.arenaPerm = null;
        this.maxPlayers = 0;
        this.minPlayers = 0;
        this.skipTimerAt = 0;
        this.lobbyPos = null;
        this.lobbyWorld = null;
        this.exitPos = null;
        this.exitWorld = null;
        this.specPos = null;
        this.centerPos = null;
        this.lobbyWaitTime = 0;
        this.lobbySkipTime = 0;
        this.gameWaitTime = 0;
        this.gameLengthTime = 0;
        trueIndex--;
        arenas.remove(index);
    }
    /**
    *
    * This method selects and returns a given this.Useful for swapping between loaded arenas. 
    * If you want to load from a file, you'll need the file object 
    *  and a list of what you want to load. Anything you don't specify 
    * will be pulled from the arena's RAM object (if possible). 
    *
    * @param namee
    * @param useFiles
    * @param file
    * @param dataPaths
    * @return 
    * selectedArena
    */
    public Arena selectArena(String namee, boolean useFiles, FileConfiguration file, List<String> dataPaths) {
        Arena selectedArena = new Arena(plugin);
        //cycle between all instances of arena
        for (Arena key : arenas) {
            //check if arena incstanc e name isn't null
            if (key.getName() != null) {
                //check if arena is what is requested
                if (key.getName().equals(namee)) {
                    //set selection data
                    selectedArena.name = namee;
                    selectedArena.pos1 = key.pos1;
                    selectedArena.pos2 = key.pos2;
                    selectedArena.world = key.world;
                    selectedArena.realWorld = Bukkit.getWorld(selectedArena.world);
                    selectedArena.minPlayers = key.minPlayers;
                    selectedArena.maxPlayers = key.maxPlayers;
                    selectedArena.skipTimerAt = key.skipTimerAt;
                    selectedArena.spawns = key.spawns;
                    selectedArena.status = key.status;
                    selectedArena.currentPlayers = key.currentPlayers;
                    selectedArena.filePath = key.filePath;
                    selectedArena.index = key.index;
                    selectedArena.currentSpawn = key.currentSpawn;
                    selectedArena.spawns = key.spawns;
                    selectedArena.lobbyWaitTime = key.lobbyWaitTime;
                    selectedArena.lobbySkipTime = key.lobbySkipTime;
                    selectedArena.gameWaitTime = key.gameWaitTime;
                    selectedArena.arenaPerm = key.arenaPerm;
                    selectedArena.lobbyPos = key.lobbyPos;
                    selectedArena.lobbyWorld = key.lobbyWorld;
                    selectedArena.exitPos = key.exitPos;
                    selectedArena.exitWorld = key.exitWorld;
                    selectedArena.specPos = key.specPos;
                    selectedArena.centerPos = key.centerPos;
                    //check if using files instead
                    if (useFiles == true) {
                        //obtain vars to set
                        for (short i = 0; i < dataPaths.size(); i++) {
                            //check if name var is being used
                            if (file.getString(dataPaths.get(i)).contains("name")) {
                                //obtain from file
                                selectedArena.name = file.getString(dataPaths.get(i));
                            }
                            //check if pos1'x var is being used
                            if (file.getString(dataPaths.get(i)).endsWith("pos1.x")) {
                                //obtain from file
                                selectedArena.pos1[0] = file.getInt(dataPaths.get(i));
                            }
                            //check if pos1'y var is being used
                            if (file.getString(dataPaths.get(i)).endsWith("pos1.y")) {
                                //obtain from file
                                selectedArena.pos1[1] = file.getInt(dataPaths.get(i));
                            }
                            //check if pos1'z var is being used
                            if (file.getString(dataPaths.get(i)).endsWith("pos1.z")) {
                                //obtain from file
                                selectedArena.pos1[2] = file.getInt(dataPaths.get(i));
                            }
                            //check if pos2'x var is being used
                            if (file.getString(dataPaths.get(i)).endsWith("pos2.x")) {
                                //obtain from file
                                selectedArena.pos2[0] = file.getInt(dataPaths.get(i));
                            }
                            //check if pos2'y var is being used
                            if (file.getString(dataPaths.get(i)).endsWith("pos2.y")) {
                                //obtain from file
                                selectedArena.pos2[1] = file.getInt(dataPaths.get(i));
                            }
                            //check if pos2'z var is being used
                            if (file.getString(dataPaths.get(i)).endsWith("pos2.z")) {
                                //obtain from file
                                selectedArena.pos2[2] = file.getInt(dataPaths.get(i));
                            }
                            //check if world var is being used
                            if (file.getString(dataPaths.get(i)).contains("world")) {
                                //obtain from file
                                selectedArena.world = file.getString(dataPaths.get(i));
                            }
                            //check if minPlayers var is being used
                            if (file.getString(dataPaths.get(i)).contains("minPlayers")) {
                                //obtain from file
                                selectedArena.minPlayers = (byte) file.getInt(dataPaths.get(i));
                            }
                            //check if maxPlayers var is being used
                            if (file.getString(dataPaths.get(i)).contains("maxPlayers")) {
                                //obtain from file
                                selectedArena.maxPlayers = (byte) file.getInt(dataPaths.get(i));
                            }
                        }
                    }
                    return key;
                }
            }
        }
        //check if arena is still null
        if (selectedArena.getName() == null) {
            //output error message
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, "COULD NOT LOAD ARENA " + namee + '!');
        }
        //return arena
        return selectedArena;
    }
    /**
    *
    * This method creates the .arena file. 
    * It is recommended to use this on every new arena, 
    * since a schematic is NOT created in createArena method. 
    * You do not need one for every arena if you don't need any data from it. 
    * .arena files take out a good amount of disk space.
    *
    */
    public void createArenaSchematic() {
        //files
        File schem = new File(plugin.getDataFolder() + this.filePath, "region.arena");
        FileConfiguration schematic = FileManager.getCustomData(plugin, "region.arena", this.filePath);
        //local vars
        List<String> savearena = new ArrayList<>();
        String tempValue = " ";
        String lastValue = " ";
        int currentIndex = 0;
        savearena.add("");
        currentIndex++;
        //cycle x coords
        for (int x = this.pos1[0] ; x < this.pos2[0] ; x++) {
            //cycle y coords
            for (int y = this.pos1[1] ; y < this.pos2[1] ; y++) {
                //cycle z corrds
                for (int z = this.pos1[2] ; z < this.pos2[2] ; z++) {
                    //temporary value
                    tempValue = this.realWorld.getBlockAt(x, y, z).getBlockData().getAsString().replace("minecraft:", ROOT);
                    //check if current index is equal to tempvalue
                    if (!lastValue.equalsIgnoreCase(tempValue)) {
                        //set block
                        savearena.add(this.realWorld.getBlockAt(x, y, z).getBlockData().getAsString().replace("minecraft:", ROOT) + ';' + x + ';' + y + ';' + z);
                        //save value for next reference
                        lastValue = tempValue;
                        currentIndex++;
                    }
                }
            }
        }
        //save file
        schematic.set("Arena:", savearena);
        try {
            schematic.save(schem);
        }
        catch (IOException ex) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
    *
    * This method regenerates the schematic of a given this. It's pretty optimized,  
    * but not recommended to reset after every match, especially if your arena is huge 
    * For reference, a 300x150x300 area of normal terrain 
    * takes 12-18 seconds to regenerate using an Intel i5-9300H 2.4 ghz CPU 
    * and uses 1.5 GB RAM to do so. 
    * That means it can regenerate up to 1,000,000 blocks per second. 
    * ONLY reset if there are lots of block changes (you allow building/breaking). 
    * It WILL CAUSE LAG with very big arenas and complex arenas (lots of different blocks). 
    * It cannot update only blockdata. It raises the regeneration time to around 50-60 seconds 
    * if enabled in its current state. This will be looked into in the future. 
    * Entities and metadata (storage data & signs) are not supported yet.
    *
    */
    public void loadArenaSchematic() {
        //file
        FileConfiguration schematic = FileManager.getCustomData(plugin, "region.arena", this.filePath);
        //load schematic list
        List<String> schemlist = schematic.getStringList("Arena:");
        if (schemlist.isEmpty()) {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT LOAD ARENA SCHEMATIC " + this.name + '!');
        }
        //local vars
        Material block = null;
        BlockData blockData = null;
        int currentIndex = 1;
        String tempBlock = null;
        BlockData tempBlockData = null;
        String lastBlock = "AIR";
        String lastBlockData = null;
        String[] tempBlockArray = new String[4];
        String[] tempArray = new String[2];
        List<String> currentBlockArray = new ArrayList<>();
        String[] blockDataArray = new String[127];
        //cycle x coords
        for (int xl = this.pos1[0]; xl <= this.pos2[0]; xl++) {
            //cycle y coords
            for (int yl = this.pos1[1]; yl <= this.pos2[1]; yl++) {
                //cycle z coords
                for (int zl = this.pos1[2]; zl <= this.pos2[2]; zl++) {
                    //reset temporary values
                    tempBlock = null;
                    tempBlockData = null;
                    tempBlockArray = new String[4];
                    tempArray = new String[2];
                    currentBlockArray = new ArrayList<>();
                    blockDataArray = new String[127];
                    //check if we're at the end of the file
                    if (currentIndex < schemlist.size()) {
                        //obtain string
                        tempBlock = schemlist.get(currentIndex);
                    }
                    //run if we're at the end
                    else {
                        //reset loop vars
                        xl = this.pos2[0] + 1;
                        yl = this.pos2[1] + 1;
                        zl = this.pos2[2] + 1;
                        //grab final string
                        tempBlock = schemlist.get(currentIndex-1);
                    }
                    //seperate sting into different parts
                    tempBlockArray = tempBlock.split(";");
                    //split block and block data
                    tempArray = tempBlockArray[0].split("\\[");
                    //add block to array
                    currentBlockArray.add(tempArray[0]);
                    //check if there is blockdata
                    if (tempArray.length > 1) {
                        //add block data to array
                        currentBlockArray.add(tempArray[1]);
                        //delete last bracket for later
                        currentBlockArray.set(1, tempArray[1].replace("]", ""));
                    }
                    //run if there's no blockdata
                    else {
                        //set placeholder for later
                        currentBlockArray.add("none");
                    }
                    //add position values
                    currentBlockArray.add(tempBlockArray[1]);
                    currentBlockArray.add(tempBlockArray[2]);
                    currentBlockArray.add(tempBlockArray[3]);
                    //reset block in array
                    currentBlockArray.set(0, tempArray[0]);
                    //check if loop is equal to the current block position
                    if (xl == Long.parseLong(currentBlockArray.get(2)) && yl == Long.parseLong(currentBlockArray.get(3)) && zl == Long.parseLong(currentBlockArray.get(4))) {
                        //set block
                        block = Material.getMaterial(currentBlockArray.get(0).toUpperCase());
                        //save block for later
                        lastBlock = currentBlockArray.get(0).toUpperCase(); 
                        //check again if block has blockdata
                        if (!currentBlockArray.get(1).equals("none")) {
                            //set blockdata
                            blockData = Material.getMaterial(currentBlockArray.get(0).toUpperCase()).createBlockData('[' + currentBlockArray.get(1) + ']');
                            //save blockdata for later
                            lastBlockData = currentBlockArray.get(1);
                        }
                        //run if there's no blockdata
                        else {
                            //erase blockdata vars
                            blockData = null;
                            lastBlockData = null;
                        }
                        //increment index
                        currentIndex++;
                    }
                    //run if loop is not equal to current block
                    else {
                        //set block from previous loop
                        block = Material.getMaterial(lastBlock);
                        //check if there's blockdata
                        if (lastBlockData != null) {
                            //set blockdata
                            blockData = Material.getMaterial(lastBlock).createBlockData('[' + lastBlockData + ']');
                        }
                    }
                    //check if block is somehow null to prevent errors
                    if (block == null) {
                        //set block to air
                        block = Material.AIR;
                    }
                    //change block in the world
                    this.realWorld.getBlockAt(xl, yl, zl).setType(block);
                    //check if there's blockdata
                    if (blockData != null) {
                        //change blockdata in world
                        this.realWorld.getBlockAt(xl, yl, zl).setBlockData(blockData);
                    }
                    //check if block has a vertical pair (doors, tall grass, etc.)
                    if (this.realWorld.getBlockAt(xl, yl, zl).getBlockData().getAsString().contains("half=upper")) {
                        //set blockdata for other pair
                        blockData = Material.getMaterial(currentBlockArray.get(0).toUpperCase()).createBlockData('[' + currentBlockArray.get(1).replace("half=upper", "half=lower") + ']');
                        //change block+blockdata for pair
                        this.realWorld.getBlockAt(xl, yl-1, zl).setType(block);
                        this.realWorld.getBlockAt(xl, yl-1, zl).setBlockData(blockData);
                    }
                    //check if block has a horizontal pair(beds, etc.)
                    else if (this.realWorld.getBlockAt(xl, yl, zl).getBlockData().getAsString().contains("part=head")) {
                        //set blockdata for other pair
                        blockData = Material.getMaterial(currentBlockArray.get(0).toUpperCase()).createBlockData('[' + currentBlockArray.get(1).replace("part=head", "part=foot") + ']');
                        //check if block is facing north
                        if (blockData.getAsString().contains("facing=north")) {
                            //change block+blockdata for pair
                            this.realWorld.getBlockAt(xl, yl, zl+1).setType(block);
                            this.realWorld.getBlockAt(xl, yl, zl+1).setBlockData(blockData);
                        }
                        //if previous check failed, check if block is facing south
                        else if (blockData.getAsString().contains("facing=south")) {
                            //change block+blockdata for pair
                            this.realWorld.getBlockAt(xl, yl, zl-1).setType(block);
                            this.realWorld.getBlockAt(xl, yl, zl-1).setBlockData(blockData);
                        }
                        //if previous check failed, check if block is facing west
                        else if (blockData.getAsString().contains("facing=east")) {
                            //change block+blockdata for pair
                            this.realWorld.getBlockAt(xl-1, yl, zl).setType(block);
                            this.realWorld.getBlockAt(xl-1, yl, zl).setBlockData(blockData);
                        }
                        //if previous check failed, check if block is facing east
                        else if (blockData.getAsString().contains("facing=east")) {
                            //change block+blockdata for pair
                            this.realWorld.getBlockAt(xl+1, yl, zl).setType(block);
                            this.realWorld.getBlockAt(xl+1, yl, zl).setBlockData(blockData);
                        }
                    }
                    //delete block and blockdata vars
                    block = null;
                    blockData = null;
                }
            }
        }
    }
    /**
    *
    * This method checks if a given player is inside the this. 
    *
    * @param player
    * @return 
    * inArena
    */
    public boolean isInArena(Player player) {
        //cycle x positions
        if (player.getLocation().getX() < this.pos1[0] || player.getLocation().getX() > this.pos2[0] || player.getLocation().getY() < this.pos1[1] || player.getLocation().getY() > this.pos2[1] || player.getLocation().getZ() < this.pos1[2] || player.getLocation().getZ() > this.pos2[2]) {
            return false;
        }
        else {
            //return if all checks fail
            return true;
        }
    }
        /**
    *
    * This method centerizes a specified location. 
    * You can choose weather or not yaw and pitch are used. 
    *
    * @param loc
    * @param affectRotation
    * @return
    */
    public float[] centerizeLocation(double[] loc, boolean affectRotation) {
        float[] newLoc = new float[5];
        newLoc[0] = (float) loc[0];
        newLoc[1] = (float) loc[1];
        newLoc[2] = (float) loc[2];
        //check if x is negative
        if (newLoc[0] < 0) {
            //centerize x
            newLoc[0] = (int) newLoc[0];
            newLoc[0] -= 0.5;
        }
        //check if x is positive
        else if (newLoc[0] >= 0) {
            //centerize x
            newLoc[0] = (int) newLoc[0];
            newLoc[0] += 0.5;
        }
        //set y
        newLoc[1] = (int) newLoc[1];
        //check if z is negative
        if (newLoc[2] < 0) {
            //centerize z
            newLoc[2] = (int) newLoc[2];
            newLoc[2] -= 0.5;
        }
        //check if z is positive
        else if (newLoc[2] >= 0) {
            //centerize z
            newLoc[2] = (int) newLoc[2];
            newLoc[2] += 0.5;
        }
        if (affectRotation == true) {
            newLoc[3] = (float) loc[3];
            newLoc[4] = (float) loc[4];
            //check if yaw should face at 0 degrees
            if (newLoc[3] >= -45 && newLoc[3] < 45) {
                newLoc[3] = (float) 0.0;
            }
            //check if yaw should face at 90 degrees
            else if (newLoc[3] >= 45 && newLoc[3] < 135) {
                newLoc[3] = (float) 90.0;
            }
            //check if yaw should face at 180 degrees
            else if (newLoc[3] >= 135 || newLoc[3] < -135) {
                newLoc[3] = (float) -180.0;
            }
            //check if yaw should face at 270 degrees
            else if (newLoc[3] >= -135 && newLoc[3] < -45) {
                newLoc[3] = (float) -90.0;
            }
            //reset pitch
            newLoc[4] = (float) 0.0;
        }
        return newLoc;
    }
    /**
    * 
    * This method gets the permission for the selected arena.  
    * 
    * @return 
    */
    public Permission getPermission() {
        if (arenas.contains(this)) {
            return arenas.get(this.index).arenaPerm;
        }
        return this.arenaPerm;
    }
    /**
    *
    * This method sets the min players of the selected this. 
    *
    * @param minPlayerss
    * @param updateFile
    */
    public void setMinPlayers(byte minPlayerss, boolean updateFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set maxPlayers
        minPlayers = minPlayerss;
        if (arenas.contains(this)) {
            maxPlayers = arenas.get(this.index).minPlayers;
        }
        //check if updateFIle is true
        if (updateFile == true) {
            //set and save file
            arenaData.set("maxPlayers", minPlayerss);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the min players of the selected arena
    *
    * @return 
    * maxPlayers
    */
    public byte getMinPlayers() {
        if (arenas.contains(this)) {
            return arenas.get(this.index).minPlayers;
        }
        return minPlayers;
    }
    /**
    *
    * This method sets the max players of the selected this. 
    *
    * @param maxPlayerss
    * @param updateFile
    */
    public void setMaxPlayers(byte maxPlayerss, boolean updateFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set maxPlayers
        maxPlayers = maxPlayerss;
        if (arenas.contains(this)) {
            maxPlayers = arenas.get(this.index).maxPlayers;
        }
        //check if updateFIle is true
        if (updateFile == true) {
            //set and save file
            arenaData.set("maxPlayers", maxPlayerss);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the max players of the selected arena
    *
    * @return 
    * maxPlayers
    */
    public byte getMaxPlayers() {
        if (arenas.contains(this)) {
            return arenas.get(this.index).maxPlayers;
        }
        return maxPlayers;
    }
    /**
    *
    * This method sets the current player count.
    * If you want to update the arena file, you can 
    * do that as well by setting updateFile to true. 
    *
    * @param currentPlayerss
    * @param updateFile
    */
    public void setCurrentPlayers(byte currentPlayerss, boolean updateFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //check if currentPLayers is negative
        if (currentPlayerss <= 0) {
            //set to 0
            currentPlayers = 0;
        }
        //check if currentPLayers isnt 0
        else {
            //set curretnPLayers
            currentPlayers = currentPlayerss;
        }
        if (arenas.contains(this)) {
            currentPlayers = arenas.get(this.index).currentPlayers;
        }
        //check if trying to update file
        if (updateFile == true) {
            //save file
            arenaData.set("currentPlayers", currentPlayerss);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the current player count.
    *
    * @return 
    * currentPlayers
    */
    public byte getCurrentPlayers() {
        if (arenas.contains(this)) {
            return arenas.get(this.index).currentPlayers;
        }
        return currentPlayers;
    }
    /**
    *
    * This method changes the name of the current this. 
    * If you want it so save (and move) files, set saveFile to true.
    *
    * @param namee
    * @param saveFile
    * @param filepathh
    */
    public void setName(String namee, boolean saveFile, String filepathh) {
        //set name var
        this.name = namee;
        if (arenas.contains(this)) {
            name = arenas.get(this.index).name;
        }
        //check if saveFile is true
        if (saveFile == true) {
            //files
            File arenaDataf = new File(plugin.getDataFolder() + filepathh, "settings.yml");
            FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", filepathh);
            //save file
            arenaData.set("name", namee);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
            //copy directory to new name's directory
            try {
                FileUtils.copyDirectory(new File(plugin.getDataFolder() + this.filePath), new File(plugin.getDataFolder() + filepathh));
            }
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
            //delete old directory
            try {
                FileUtils.deleteDirectory(new File(plugin.getDataFolder() + this.filePath));
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
            //update filepath
            this.filePath = filepathh;
        }
        
    }
    /**
    *
    * This method gets the current arena's name. 
    *
    * @return 
    * name
    */
    public String getName() {
        //return name
        if (arenas.contains(this)) {
            return arenas.get(this.index).name;
        }
        return this.name;
    }
    /**
    *
    * This method sets the current activity state of the selected this. 
    *
    * @param isActivee
    */
    public void setIsActive(boolean isActivee) {
        //set isActive
        this.isActive = isActivee;
        if (arenas.contains(this)) {
            isActive = arenas.get(this.index).isActive;
        }
    }
    /**
    *
    * This method gets the current activity state of the selected area. 
    *
    * @return 
    * isActive
    */
    public boolean getIsActive() {
        //return isActive
        if (arenas.contains(this)) {
            return arenas.get(this.index).isActive;
        }
        return this.isActive;
    }
    /**
    *
    * This method sets the pos1 of the current this. 
    * If you wish to save to the data file, you'll need the filepath. 
    * Setting pos1 does NOT update .arena files! You'll need to manually update them. 
    *
    * @param poss1
    * @param saveFile
    * @param filePathh
    */
    public void setPos1(int[] poss1, boolean saveFile, String filePathh) {
        //set pos1 var
        this.pos1 = poss1;
        if (arenas.contains(this)) {
            pos1 = arenas.get(this.index).pos1;
        }
        //check if updateFile is true
        if (saveFile == true) {
            //files
            File arenaDataf = new File(plugin.getDataFolder() + filePathh, "settings.yml");
            FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", filePathh);
            //set and save files
            arenaData.set("Pos1.x", poss1[0]);
            arenaData.set("Pos1.y", poss1[1]);
            arenaData.set("Pos1.z", poss1[2]);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the current pos1 of the selected this. 
    *
    * @return 
    * pos1[]
    */
    public int[] getPos1() {
        //return pos1
        if (arenas.contains(this)) {
            return arenas.get(this.index).pos1;
        }
        return this.pos1;
    }
    /**
    *
    * This method sets the pos1 of the current this. 
    * If you wish to save to the data file, you'll need the filepath. 
    * Setting pos2 does NOT update .arena files! You'll need to manually update them. 
    *
    * @param poss2
    * @param saveFile
    * @param filePathh
    */
    public void setPos2(int[] poss2, boolean saveFile, String filePathh) {
        //set pos2 var
        this.pos2 = poss2;
        if (arenas.contains(this)) {
            pos2 = arenas.get(this.index).pos2;
        }
        //check if updateFile is true
        if (saveFile == true) {
            //files
            File arenaDataf = new File(plugin.getDataFolder() + filePathh, "settings.yml");
            FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", filePathh);
            //set and save file
            arenaData.set("Pos2.x", poss2[0]);
            arenaData.set("Pos2.y", poss2[1]);
            arenaData.set("Pos2.z", poss2[2]);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the current pos2 of the selected arena. 
    *
    * @return 
    * pos2[]
    */
    public int[] getPos2() {
        //return pos2
        if (arenas.contains(this)) {
            return arenas.get(this.index).pos2;
        }
        return this.pos2;
    }
    /**
    *
    * This method sets the world and realworld of the selected arena. 
    *
    * @param worldd
    */
    public void setWorld(String worldd) {
        //set world vars
        this.world = worldd;
        this.realWorld = Bukkit.getWorld(worldd);
        if (arenas.contains(this)) {
            world = arenas.get(this.index).world;
            realWorld = Bukkit.getWorld(arenas.get(this.index).world);
        }
    }
    /**
    *
    * This method gets the world of the selected this.it only gets its name. 
    * If you need the physical world you need 
    * to load that in yourself with the return value here.
    * 
    * @return
    * world
    */
    public String getWorld() {
        //return world string
        if (arenas.contains(this)) {
            return arenas.get(this.index).world;
        }
        return this.world;
    }
    /**
    *
    * This method gets the current file path.
    *
    * @param filePathh
    * @param updateFiles
    */
    public void setFilePath(String filePathh, boolean updateFiles) {
        //check if updateFiles is true
        if (updateFiles == true) {
            //copy directory to new name's directory
            try {
                FileUtils.copyDirectory(new File(plugin.getDataFolder() + this.filePath), new File(plugin.getDataFolder() + filePathh));
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
            //delete old directory
            try {
                FileUtils.deleteDirectory(new File(plugin.getDataFolder() + this.filePath));
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //set filepath var
        this.filePath = filePathh;
        if (arenas.contains(this)) {
            filePath = arenas.get(this.index).filePath;
        }
    }
    /**
    *
    * This method gets the current file path.
    *
    * @return 
    * filePath
    */
    public String getFilePath() {
        //return filepath
        if (arenas.contains(this)) {
            return arenas.get(this.index).filePath;
        }
        return this.filePath;
    }
    /**
    *
    * This method sets the current amount of spawns in the selected this. 
    * It does not manage spawns in any way, so it should never change by 
    * more or less than one.
    *
    * @param currentSpawnn
    */
    public void setCurrentSpawn(int currentSpawnn) {
        //set currentSpawn
        this.currentSpawn = currentSpawnn;
        if (arenas.contains(this)) {
            currentSpawn = arenas.get(this.index).currentSpawn;
        }
    }
    /**
    *
    * This method gets the amount of spawns loaded in the selected this. 
    * It does not get any spawn data, just the amount. 
    *
    * @return
    * currentSpawn
    */
    public int getCurrentSpawn() {
        //return currentSpawn
        if (arenas.contains(this)) {
            return arenas.get(this.index).currentSpawn;
        }
        return this.currentSpawn;
    }
    /**
    *
    * This method adds a spawn to the selected arena. 
    *
     * @param spawnn
    */
    public void addSpawn(Spawn spawnn) {
        //increment currentSpawn
        if (arenas.contains(this)) {
            arenas.get(this.index).spawns.add(spawnn);
            arenas.get(this.index).currentSpawn++;
        }
    }
    /**
    *
    * This method deletes the given spawn from the selected arena. 
    *
    * @param spawnn
    */
    public void removeSpawn(Spawn spawnn) {
        //decrement currentSpawn
        if (arenas.contains(this)) {
            arenas.get(this.index).spawns.remove(spawnn);
            arenas.get(this.index).currentSpawn--;
        }
    }
    /**
    *
    * This method gets all the spawns from the selected arena
    *
    * @return 
    */
    public List<Spawn> getSpawns() {
        //obtain spawns
        if (arenas.contains(this)) {
            return arenas.get(this.index).spawns;
        }
        return this.spawns;
    }
    /**
    *
    * This method sets the current lobby position of the selected arena. 
    *
    * @param lobby
    * @param saveFile
    */
    public void setLobby(double[] lobby, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set lobby
        this.lobbyPos = lobby;
        if (arenas.contains(this)) {
            lobbyPos = arenas.get(this.index).lobbyPos;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("Lobby.x", lobby[0]);
            arenaData.set("Lobby.y", lobby[1]);
            arenaData.set("Lobby.z", lobby[2]);
            arenaData.set("Lobby.yaw", lobby[3]);
            arenaData.set("Lobby.pitch", lobby[4]);
            try {
                arenaData.save(arenaDataf);
            } catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method obtains the current lobby position of the selected arena. 
    *
    * @return 
    */
    public double[] getLobby() {
        //obtain lobbyPos
        if (arenas.contains(this)) {
            return arenas.get(this.index).lobbyPos;
        }
        return this.lobbyPos;
    }
    /**
    *
    * This method sets the lobbyWorld for the selected arena. 
    *
    * @param worldd
    * @param saveFile
    */
    public void setLobbyWorld(String worldd, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set center
        this.lobbyWorld = worldd;
        if (arenas.contains(this)) {
            lobbyWorld = arenas.get(this.index).lobbyWorld;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("Lobby.world", worldd);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the lobbyWorld for the selected arena. 
    *
    * @return
    */
    public String getLobbyWorld() {
        //obtain lobbyWaitTime
        if (arenas.contains(this)) {
            return arenas.get(this.index).lobbyWorld;
        }
        return this.lobbyWorld;
    }
    /**
    *
    * This method sets the current exit position of the selected arena. 
    *
    * @param exit
    * @param saveFile
    */
    public void setExit(double[] exit, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set exit
        this.exitPos = exit;
        if (arenas.contains(this)) {
            lobbyPos = arenas.get(this.index).exitPos;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("Exit.x", exit[0]);
            arenaData.set("Exit.y", exit[1]);
            arenaData.set("Exit.z", exit[2]);
            arenaData.set("Exit.yaw", exit[3]);
            arenaData.set("Exit.pitch", exit[4]);
            try {
                arenaData.save(arenaDataf);
            } catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method obtains the current exit position of the selected arena. 
    *
    * @return 
    */
    public double[] getExit() {
        //obtain exitPos
        if (arenas.contains(this)) {
            return arenas.get(this.index).exitPos;
        }
        return this.exitPos;
    }
    /**
    *
    * This method sets the lobbyWorld for the selected arena. 
    *
    * @param worldd
    * @param saveFile
    */
    public void setExitWorld(String worldd, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set center
        this.lobbyWorld = worldd;
        if (arenas.contains(this)) {
            exitWorld = arenas.get(this.index).exitWorld;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("Exit.world", worldd);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the lobbyWorld for the selected arena. 
    *
    * @return
    */
    public String getExitWorld() {
        //obtain lobbyWaitTime
        if (arenas.contains(this)) {
            return arenas.get(this.index).exitWorld;
        }
        return this.exitWorld;
    }
    /**
    *
    * This method sets the current spectator position of the selected arena. 
    *
    * @param spec
    * @param saveFile
    */
    public void setSpectatorPos(double[] spec, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set specPos
        this.specPos = spec;
        if (arenas.contains(this)) {
            lobbyPos = arenas.get(this.index).specPos;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("Spec.x", spec[0]);
            arenaData.set("Spec.y", spec[1]);
            arenaData.set("Spec.z", spec[2]);
            arenaData.set("Spec.yaw", spec[3]);
            arenaData.set("Spec.pitch", spec[4]);
            try {
                arenaData.save(arenaDataf);
            } catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method obtains the current spectator position of the selected arena. 
    *
    * @return 
    */
    public double[] getSpectatorPos() {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //obtain specPos
        if (arenas.contains(this)) {
            return arenas.get(this.index).specPos;
        }
        return this.specPos;
    }
    /**
    *
    * This method sets the current center position of the selected arena. 
    *
    * @param center
    * @param saveFile
    */
    public void setCenter(double[] center, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set center
        this.centerPos = center;
        if (arenas.contains(this)) {
            centerPos = arenas.get(this.index).centerPos;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("Center.x", center[0]);
            arenaData.set("Center.y", center[1]);
            arenaData.set("Center.z", center[2]);
            arenaData.set("Center.yaw", center[3]);
            arenaData.set("Center.pitch", center[4]);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method obtains the current center position of the selected arena. 
    *
    * @return 
    */
    public double[] getCenter() {
        //obtain centerPos
        if (arenas.contains(this)) {
            return arenas.get(this.index).centerPos;
        }
        return this.centerPos;
    }
    /**
    * 
    * This method sets the status of the selected arena.  
    * 
    * @param statuss
    * @param saveFile
    */
    public void setStatus(String statuss, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //load statuses
        String newStatus = "";
        if (statuss.equalsIgnoreCase("WAITING")) {
            newStatus = WAITING;
        }
        else if (statuss.equalsIgnoreCase("RUNNING")) {
            newStatus = RUNNING;
        }
        else if (statuss.equalsIgnoreCase("DISABLED")) {
            newStatus = DISABLED;
        }
        else {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT SET THE STATUS " + statuss + '!');
        }
        //set status
        this.status = statuss;
        if (arenas.contains(this)) {
            status = arenas.get(this.index).status;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("status", statuss);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method obtains the current status of the selected arena. 
    *
    * @return 
    */
    public String getStatus() {
        //obtain centerPos
        if (arenas.contains(this)) {
            return arenas.get(this.index).status;
        }
        return this.status;
    }
    /**
    * 
    * This method renames a specified status.  
    * 
    * @param statuss
    * @param newName
    */
    public void renameStatus(String statuss, String newName) {
        if (statuss.equalsIgnoreCase("WAITING")) {
            WAITING = ChatColor.translateAlternateColorCodes('&', newName);
        }
        if (statuss.equalsIgnoreCase("RUNNING")) {
            RUNNING = ChatColor.translateAlternateColorCodes('&', newName);
        }
        if (statuss.equalsIgnoreCase("DISABLED")) {
            DISABLED = ChatColor.translateAlternateColorCodes('&', newName);
        }
        else {
            Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, "COULD NOT SET THE STATUS " + statuss + '!');
        }       
    }
    /**
    *
    * This method sets the wait time for the selected arena. 
    *
    * @param waitTimee
    * @param saveFile
    */
    public void setLobbyWaitTime(int waitTimee, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set center
        this.lobbyWaitTime = waitTimee;
        if (arenas.contains(this)) {
            lobbyWaitTime = arenas.get(this.index).lobbyWaitTime;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("lobby-wait-time", waitTimee);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the wait time for the selected arena. 
    *
    * @return
    */
    public int getLobbyWaitTime() {
        //obtain lobbyWaitTime
        if (arenas.contains(this)) {
            return arenas.get(this.index).lobbyWaitTime;
        }
        return this.lobbyWaitTime;
    }
    /**
    *
    * This method sets the skip time for the selected arena. 
    *
    * @param waitTimee
    * @param saveFile
    */
    public void setLobbySkipTime(int waitTimee, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set center
        this.lobbySkipTime = waitTimee;
        if (arenas.contains(this)) {
            lobbySkipTime = arenas.get(this.index).lobbySkipTime;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("lobby-skip-time", waitTimee);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the skip time for the selected arena. 
    *
    * @return
    */
    public int getLobbySkipTime() {
        //obtain lobbySkipTime
        if (arenas.contains(this)) {
            return arenas.get(this.index).lobbySkipTime;
        }
        return this.lobbySkipTime;
    }
    /**
    *
    * This method sets the wait time for the selected arena. 
    *
    * @param waitTimee
    * @param saveFile
    */
    public void setGameWaitTime(int waitTimee, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set center
        this.gameWaitTime = waitTimee;
        if (arenas.contains(this)) {
            gameWaitTime = arenas.get(this.index).gameWaitTime;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("game-wait-time", waitTimee);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the wait time for the selected arena. 
    *
    * @return
    */
    public int getGameWaitTime() {
        //obtain lobbyWaitTime
        if (arenas.contains(this)) {
            return arenas.get(this.index).gameWaitTime;
        }
        return this.gameWaitTime;
    }
    /**
    *
    * This method sets the length time for the selected arena. 
    *
    * @param waitTimee
    * @param saveFile
    */
    public void setGameLengthTime(int waitTimee, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set center
        this.gameLengthTime = waitTimee;
        if (arenas.contains(this)) {
            gameLengthTime = arenas.get(this.index).gameLengthTime;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("game-length-time", waitTimee);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the length time for the selected arena. 
    *
    * @return
    */
    public int getGameLengthTime() {
        //obtain lobbyWaitTime
        if (arenas.contains(this)) {
            return arenas.get(this.index).gameLengthTime;
        }
        return this.gameLengthTime;
    }
    /**
    *
    * This method sets the skipTimerAt for the selected arena. 
    *
    * @param timerAt
    * @param saveFile
    */
    public void setSkipTimerAt(byte timerAt, boolean saveFile) {
        //files
        File arenaDataf = new File(plugin.getDataFolder() + this.filePath, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", this.filePath);
        //set center
        this.skipTimerAt = timerAt;
        if (arenas.contains(this)) {
            skipTimerAt = arenas.get(this.index).skipTimerAt;
        }
        if (saveFile == true) {
            //save file
            arenaData.set("skip-timer-at", skipTimerAt);
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Arena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
    *
    * This method gets the skipTimerAt for the selected arena. 
    *
    * @return
    */
    public byte getSkipTimerAt() {
        //obtain skipTimerAt
        if (arenas.contains(this)) {
            return arenas.get(this.index).skipTimerAt;
        }
        return this.skipTimerAt;
    }
}
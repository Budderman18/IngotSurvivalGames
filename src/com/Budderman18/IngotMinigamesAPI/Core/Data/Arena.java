package com.budderman18.IngotMinigamesAPI.Core.Data;

import com.budderman18.IngotMinigamesAPI.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
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
    * This constructor updates the loaded instances list
    * It is done every time a new Spawn class is created
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
    private String name = null;
    private String filePath = null;
    private boolean isActive = false;
    private int index = 0;
    //settings
    private byte currentPlayers = 0;
    private byte maxPlayers = 0;
    private List<Spawn> spawns = new ArrayList<>();
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
    * @param maxPlayerss
    * @param saveFile
    * @param filePathh
    * @return 
    * newArena
    */
    public Arena createArena(int[] pos1, int[] pos2, String worldd, String arenaName, byte maxPlayerss, boolean saveFile, String filePathh) {
        //create arena object
        Arena newArena = new Arena(plugin);
        //files
        File arenaDataf = new File(plugin.getDataFolder() + filePathh, "settings.yml");
        FileConfiguration arenaData = FileManager.getCustomData(plugin, "settings", filePathh);
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
            arenaData.set("maxPlayers", maxPlayerss);
            arenaData.set("currentPlayers", 0);
        }
        //set fields
        newArena.pos1 = pos1;
        newArena.pos2 = pos2;
        newArena.world = worldd;
        newArena.name = arenaName;
        newArena.realWorld = Bukkit.getWorld(worldd);
        newArena.maxPlayers = maxPlayerss;
        newArena.filePath = filePathh;
        newArena.index = trueIndex;
        //save files
        if (saveFile == true) {
            try {
                arenaData.save(arenaDataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //return arena
        if (arenas == null) {
            arenas = new ArrayList<>();
        }
        arenas.add(newArena);
        trueIndex++;
        return newArena;
    }
    /*
    *
    * This method deletes the selected arena
    *
    */
    public void deleteArena() {
        //delete files
        try {
            FileUtils.deleteDirectory(new File(plugin.getDataFolder() + this.filePath));
        } 
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
                    selectedArena.maxPlayers = key.maxPlayers;
                    selectedArena.currentPlayers = key.currentPlayers;
                    selectedArena.filePath = key.filePath;
                    selectedArena.index = key.index;
                    selectedArena.currentSpawn = key.currentSpawn;
                    selectedArena.spawns = key.spawns;
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
                                selectedArena.name = file.getString(dataPaths.get(i));
                            }
                            //check if maxPlayers var is being used
                            if (file.getString(dataPaths.get(i)).contains("maxPlayers")) {
                                //obtain from file
                                selectedArena.name = file.getString(dataPaths.get(i));
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
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, "COULD NOT LOAD ARENA " + namee + '!');
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
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
        for (int x=this.pos1[0]; x < this.pos2[0]; x++) {
            //cycle y positions
            for (int y=this.pos1[1]; y < this.pos2[1]; y++) {
                //cycle z positions
                for (int z=this.pos1[2]; z < this.pos2[2]; z++) {
                    //check if player is in arena
                    if ((int) player.getLocation().getX() == x && (int) player.getLocation().getY() == y && (int) player.getLocation().getZ() == z) {
                        return true;
                    }
                }
            }
        }
        //return if all checks fail
        return false;
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
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            //copy directory to new name's directory
            try {
                FileUtils.copyDirectory(new File(plugin.getDataFolder() + this.filePath), new File(plugin.getDataFolder() + filepathh));
            }
            catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            //delete old directory
            try {
                FileUtils.deleteDirectory(new File(plugin.getDataFolder() + this.filePath));
            } 
            catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            //delete old directory
            try {
                FileUtils.deleteDirectory(new File(plugin.getDataFolder() + this.filePath));
            } 
            catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
    * This method adds a spawn to the selected this. 
    *
     * @param spawnn
    */
    public void addSpawn(Spawn spawnn) {
        //add spawn object
        this.spawns.add(spawnn);
        //increment currentSpawn
        this.currentSpawn++;
        if (arenas.contains(this)) {
            arenas.get(this.index).spawns.add(spawnn);
            arenas.get(this.index).currentSpawn++;
        }
    }
    /**
    *
    * This method deletes the given spawn from the selected this. 
    *
    * @param spawnn
    */
    public void removeSpawn(Spawn spawnn) {
        //remove spawn object
        this.spawns.remove(spawnn);
        //decrement currentSpawn
        this.currentSpawn--;
        if (arenas.contains(this)) {
            arenas.get(this.index).spawns.remove(spawnn);
            arenas.get(this.index).currentSpawn--;
        }
    }
}

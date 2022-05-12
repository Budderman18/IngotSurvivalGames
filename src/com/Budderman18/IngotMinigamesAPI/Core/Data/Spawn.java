package com.budderman18.IngotMinigamesAPI.Core.Data;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * This method handles spawns for players
 * You can create your own as well as teleport to them
 * Includes random teleportation
 * 
 */
public class Spawn {
    /**
    *
    * This constructor updates the loaded instances list
    * It is done every time a new Spawn class is created
    *
    * @param pluginn
    */
    public Spawn(Plugin pluginn) {
        //set plugin
        plugin = pluginn;
    }
    //plugin
    private static Plugin plugin = null;
    //spawn counter list
    private static List<Spawn> spawns = new ArrayList<>();
    private static int trueIndex = 0;
    //global vars
    private String name = null;
    private double[] location = new double[5];
    private int index = 0;
    private boolean isOccupied = false;
    /**
    *
    * This method creates a new spawn.Spawns support decimals. 
    *
    * @param name
    * @param x
    * @param y
    * @param z
    * @param yaw
    * @param pitch
    * @return 
    * this
    */
    public Spawn createSpawn(String name, double x, double y, double z, double yaw, double pitch) {
        //create spawn object
        Spawn newSpawn = new Spawn(plugin);
        //create locaiton array
        newSpawn.location[0] = x;
        newSpawn.location[1] = y;
        newSpawn.location[2] = z;
        newSpawn.location[3] = yaw;
        newSpawn.location[4] = pitch;
        //save variables
        newSpawn.name = name;
        newSpawn.index = trueIndex;
        //return spawn
        spawns.add(newSpawn);
        trueIndex++;
        return newSpawn;
    }
    /**
    *
    * This method deletes a spawn. 
    *
    */
    public void deleteSpawn() {
        //reset variables
        this.location[0] = 0;
        this.location[1] = 0;
        this.location[2] = 0;
        this.location[3] = 0;
        this.location[4] = 0;
        this.name = null;
        this.index = 0;
        this.isOccupied = false;
        spawns.remove(this);
        trueIndex--;
    }
    /**
    *
    * This method selects any of the currently loaded spawns
    * If you want it to load from a file rather than from RAM,
    * you need your file to read from and a list of data to change
    * any unspecifed data should be set to null and will instead be
    * pulled from RAM (if it can).Positions MUST END WITH either x,y or z,
    * (all lowercase).The name must contain "name", all lowercase.
    *
    * @param namee
    * @param useFiles
    * @param file
    * @param dataPaths
    * @return 
    * selectedArena
    */
    public Spawn selectSpawn(String namee, boolean useFiles, FileConfiguration file, List<String> dataPaths) {
        Spawn selectedSpawn = new Spawn(plugin);
        //cycle through all loaded instances
        for (Spawn key : spawns) {
            //check if spawn name is not null
            if (key.getName() != null) {
                //check if spawn name if equal to namee
                if (key.getName().equals(namee)) {
                    //set vars
                    selectedSpawn.name = namee;
                    selectedSpawn.location = key.location;
                    selectedSpawn.index = key.index;
                    selectedSpawn.isOccupied = key.isOccupied;
                    //check is useFiles is true
                    if (useFiles == true) {
                        for (short i = 0; i < dataPaths.size(); i++) {
                            //check if path is name
                            if (file.getString(dataPaths.get(i)).contains("name")) {
                                //set name
                                selectedSpawn.name = file.getString(dataPaths.get(i));
                            }
                            //check if path is x
                            if (file.getString(dataPaths.get(i)).endsWith("x")) {
                                //set x
                                selectedSpawn.location[0] = file.getDouble(dataPaths.get(i));
                            }
                            //check if path is y
                            if (file.getString(dataPaths.get(i)).endsWith("y")) {
                                //set y
                                selectedSpawn.location[1] = file.getDouble(dataPaths.get(i));
                            }
                            //check if path is z
                            if (file.getString(dataPaths.get(i)).endsWith("z")) {
                                //set z
                                selectedSpawn.location[2] = file.getDouble(dataPaths.get(i));
                            }
                            //check if path is yaw
                            if (file.getString(dataPaths.get(i)).endsWith("yaw")) {
                                //set yaw
                                selectedSpawn.location[3] = file.getDouble(dataPaths.get(i));
                            }
                            //check if path is pitch
                            if (file.getString(dataPaths.get(i)).endsWith("pitch")) {
                                //set pitch
                                selectedSpawn.location[4] = file.getDouble(dataPaths.get(i));
                            }
                            //check if path is isOccupied
                            if (file.getString(dataPaths.get(i)).contains("isOccupied")) {
                                //set z
                                selectedSpawn.isOccupied = file.getBoolean(dataPaths.get(i));
                            }
                        }
                    }
                    return key;
                }
            }
        }
        //check if arena is still null
        if (selectedSpawn.getName() == null) {
            //output error message
            Logger.getLogger(Spawn.class.getName()).log(Level.SEVERE, null, "COULD NOT LOAD SPAWN " + namee + '!');
        }
        //return arena
        return selectedSpawn;
    }
    /**
    *
    * This method teleports the given player to the selected spawn. 
    *
    * @param player
    */
    public void moveToSpawn(Player player) {
        //create location from spawn
        Location location = new Location(Bukkit.getWorld("world"), this.location[0], this.location[1], this.location[2], (float) this.location[3], (float) this.location[4]);
        //teleport
        player.teleport(location);
    }
    /**
    *
    * This method teleports the given player to a 
    * random spawn in the designated list. 
    *
    * @param spawns
    * @param player
    * @return 
    */
    public static Spawn moveToRandomSpawn(List<Spawn> spawns, Player player) {
        //call random class
        Random random = new Random(); 
        //local vars
        long start = 0;
        long end = 0;
        long temploc = 0;
        //temporary spawn
        Spawn spawn = null;
        //determine end number
        end = spawns.size();
        //randomize
        temploc = random.nextLong(start, end);
        //set spawn to new random spawn
        spawn = spawns.get((int) temploc);
        //teleport
        spawn.moveToSpawn(player);
        return spawn;
    }
    /**
    *
    * This method sets the name of the selected spawn. 
    *
    * @param namee
    */
    public void setName(String namee) {
        //set name var
        this.name = namee;
        if (spawns.contains(this)) {
            name = spawns.get(this.index).name;
        }
    }
    /**
    *
    * This method obtains the name of the selected spawn. 
    *
    * @return 
    * name
    */
    public String getName() {
        //return name
        if (spawns.contains(this)) {
            return spawns.get(this.index).name;
        }
        return this.name;
    }
    /**
    *
    * This method sets the location of the selected spawn. 
    *
    * @param locationn
    */
    public void setLocation(double[] locationn) {
        //set location array
        this.location = locationn;
        if (spawns.contains(this)) {
            location = spawns.get(this.index).location;
        }
    }
    /**
    *
    * This method obtains the location of the selected spawn
    *
    * @return 
    * location
    */
    public double[] getLocation() {
        //return location
        if (spawns.contains(this)) {
            return spawns.get(this.index).location;
        }
        return this.location;
    }
    /**
    *
    * This method sets the isOccupied of the selected spawn. 
    *
    * @param isOccupiedd
    */
    public void setIsOccupied(boolean isOccupiedd) {
        //set location array
        this.isOccupied = isOccupiedd;
        if (spawns.contains(this)) {
            isOccupied = spawns.get(this.index).isOccupied;
        }
    }
    /**
    *
    * This method obtains the isOccupied of the selected spawn
    *
    * @return 
    */
    public boolean getIsOccupied() {
        //return location
        if (spawns.contains(this)) {
            return spawns.get(this.index).isOccupied;
        }
        return this.isOccupied;
    }
}
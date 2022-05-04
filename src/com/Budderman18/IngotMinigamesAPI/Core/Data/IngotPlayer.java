package com.budderman18.IngotMinigamesAPI.Core.Data;

import com.budderman18.IngotMinigamesAPI.Main;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles all playerdata managed by 
 * IMAPI plugins. Currently, its limited to 
 * username, uuid, inGame, isPlaying, isFrozen and game. 
 * 
 */
public class IngotPlayer {
    /**
    *
    * This constructor updates the loaded instances list. 
    * It is done every time a new Spawn class is created. 
    *
     * @param pluginn
    */
    public IngotPlayer(Plugin pluginn) {
        //set plugin var
        plugin = pluginn;
    }
    //plugin
    private static Plugin plugin = null;
    //global vars
    private String username = null;
    private String uuid = null;
    private boolean inGame = false;
    private boolean isPlaying = false;
    private boolean isFrozen = false;
    private String game = null;
    private static List<IngotPlayer> players;
    private int index;
    private static int trueIndex;
    private static boolean setIndex;
    /**
    *
    * This method creates a new player for IMAPI. 
    *
    * @param player
    * @return 
    */
    public IngotPlayer createPlayer(Player player) {
        //check if list is empty
        if (players == null) {
            players = new ArrayList<>();
        }
        if (index == 0 && setIndex == false) {
            index = -1;
            setIndex = true;
        }
        //create new iplayer
        IngotPlayer newPlayer = new IngotPlayer(plugin);
        //set vars
        newPlayer.username = player.getName();
        newPlayer.uuid = player.getUniqueId().toString();
        newPlayer.index = trueIndex;
        //return this
        players.add(newPlayer);
        trueIndex++;
        return newPlayer;
    }
    /**
    *
    * This method deletes a player from IMAPI. 
    *
    */
    public void deletePlayer() {
        //delete vars
        this.username = null;
        this.uuid = null;
        this.inGame = false;
        this.isPlaying = false;
        this.isFrozen = false;
        this.game = null;
        this.index = 0;
        trueIndex--;
        players.remove(index);
    }
    /**
    *
    * This method selects a given ingotplayer using their username. 
    *
    * @param namee
    * @param useFiles
    * @param file
    * @param dataPaths
    * @return 
    */
    public IngotPlayer selectPlayer(String namee, boolean useFiles, FileConfiguration file, List<String> dataPaths) {
        //cycle through all loaded instances
        IngotPlayer key = null;
        IngotPlayer selectedPlayer = new IngotPlayer(plugin);
        for (short i = 0; i < players.size(); i++) {
            key = players.get(i);
            //check if username is not null
            if (key.getUsername() != null) {
                //check if selected iplayer's name is equal to namee
                if (key.getUsername().equals(namee)) {
                    //set vars
                    selectedPlayer.username = namee;
                    selectedPlayer.uuid = Bukkit.getPlayer(namee).getUniqueId().toString();
                    selectedPlayer.inGame = key.inGame;
                    selectedPlayer.isPlaying = key.isPlaying;
                    selectedPlayer.isFrozen = key.isFrozen;
                    selectedPlayer.game = key.game;
                    selectedPlayer.index = key.index;
                    //check if useFIles is true
                    if (useFiles == true) {
                        //cycle through all paths
                        for (short j = 0; i < dataPaths.size(); i++) {
                            //check if path is username
                            if (file.getString(dataPaths.get(j)).contains("username")) {
                                //set username
                                selectedPlayer.username = file.getString(dataPaths.get(i));
                            }
                            //check if path is uuid
                            if (file.getString(dataPaths.get(j)).endsWith("uuid")) {
                                //set uuid
                                selectedPlayer.uuid = file.getString(dataPaths.get(i));
                            }
                        }
                    }
                    //return iplayer
                    return key;
                }
            }
        }
        //check if iplayer is still null
        if (selectedPlayer.getUsername() == null) {
            //output error message
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "COULD NOT LOAD PLAYER " + namee + '!');
        }
        //return iplayer
        return selectedPlayer;
    }
    /**
    *
    * This method sets the username of the selected iplayer. 
    *
    * @param usernamee
    */
    public void setUsername(String usernamee) {
        //set username
        this.username = usernamee;
        if (players.contains(this)) {
            players.get(this.index).username = usernamee;
        }
    }
    /**
    *
    * This method obtains the username of the current player. 
    *
    * @return 
    * username
    */
    public String getUsername() {
        //return username
        if (players.contains(this)) {
             return players.get(this.index).username;
        }
        return this.username;
    }
    /**
    *
    * This method changes the uuid of the current player. 
    *
    * @param uuidd
    */
    public void setUUID(String uuidd) {
        //set uuid
        this.uuid = uuidd;
        if (players.contains(this)) {
            players.get(this.index).uuid = uuidd;
        }
    }
    /**
    *
    * This method obtains the username of the current player. 
    *
    * @return 
    * uuid
    */
    public String getUUID() {
        //return uuid
        if (players.contains(this)) {
            return players.get(this.index).uuid;
        }
        return this.uuid;
    }
    /**
    *
    * This method changes the inGame of the current player. 
    *
    * @param inGamee
    */
    public void setInGame(boolean inGamee) {
        //set ingame
        this.inGame = inGamee;
        if (players.contains(this)) {
            players.get(this.index).inGame = inGamee;
        }
    }
    /**
    *
    * This method gets the inGame of the current player. 
    *
    * @return 
    * inGame
    */
    public boolean getInGame() {
        //return ingame
        if (players.contains(this)) {
            return players.get(this.index).inGame;
        }
        return this.inGame;
    }
    /**
    *
    * This method changes the isPlaying of the current player. 
    *
    * @param isPlayingg
    */
    public void setIsPlaying(boolean isPlayingg) {
        //set isPlaying
        this.isPlaying = isPlayingg;
        if (players.contains(this)) {
            players.get(this.index).isPlaying = isPlayingg;
        }
    }
    /**
    *
    * This method gets the isPlaying of the current player. 
    *
    * @return
    * isPlaying
    */
    public boolean getIsPlaying() {
        //return isPlaying
        if (players.contains(this)) {
            return players.get(this.index).isPlaying;
        }
        return this.isPlaying;
    }
    /**
    *
    * This method changes the isFrozen of the current player. 
    *
    * @param isFrozenn
    */
    public void setIsFrozen(boolean isFrozenn) {
        //set isFrozen
        this.isFrozen = isFrozenn;
        if (players.contains(this)) {
            players.get(this.index).isFrozen = isFrozenn;
        }
    }
    /**
    *
    * This method gets the frozen state of the selected player. 
    *
    * @return 
    * isFrozen
    */
    public boolean getIsFrozen() {
        //return isFrozen
        if (players.contains(this)) {
            return players.get(this.index).isFrozen;
        }
        return this.isFrozen;
    }
    /**
    *
    * This method changes the game the selected player is in. 
    *
    * @param gamee
    */
    public void setGame(String gamee) {
        //set game
        this.game = gamee;
        if (players.contains(this)) {
            players.get(this.index).game = gamee;
        }
    }
    /**
    *
    * This method gets the current game the player is in. 
    *
    * @return
    * game
    */
    public String getGame() {
        //return game
        if (players.contains(this)) {
            return players.get(this.index).game;
        }
        return this.game;
    }
}
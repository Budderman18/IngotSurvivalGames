package com.budderman18.IngotSurvivalGames.Core;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.ScoreboardHandler;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TablistHandler;
import com.budderman18.IngotMinigamesAPI.Core.Data.Timer;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TitleHandler;
import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.BossbarHandler;
import com.budderman18.IngotMinigamesAPI.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
* 
* This class handles game instances. 
* This includes joining, leaving, and freezing. 
*
*/
public class Game implements Listener {
    //plugin
    private final Plugin plugin = Main.getInstance();
    //root
    private final String ROOT = "";
    //local classes
    private final IngotPlayer IngotPlayer = new IngotPlayer(plugin);
    private IngotPlayer iplayer = null;
    //global vars
    private final short endAt = 60;
    private double currentSize = 1;
    private double currentTime = endAt;
    private Runnable trueTimer = null;
    /**
    *
    * This method updates the ScoreboardHandler for every player
    * This does NOT delete or create, only updates
    *
    */
    private void updateScoreboard(Arena arenaMethod) {
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //local vars
        String blankString = " ";
        String lineString = null;
        byte maxPlayers = arenaMethod.getMaxPlayers();
        //cycle through all online players
        for (Player key : Bukkit.getOnlinePlayers()) {
            blankString = " ";
            //obtain iplayer
            iplayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
            //check if player is ingame
            if (iplayer.getInGame() == true) {
                //set title
                ScoreboardHandler.setTitle(key, ChatColor.translateAlternateColorCodes('&', "&7[&6IngotSurvivalGames&7]"));
                //cycle through all lines
                for (byte i = 0; i < config.getInt("Scoreboard.maxLines"); i++) {
                    //check if line is not null
                    if (config.getString("Scoreboard.line" + i) != null) {
                        //set lines
                        lineString = config.getString("Scoreboard.line" + i);
                        lineString = lineString.replaceAll("%currentplayers%", Byte.toString(arenaMethod.getCurrentPlayers()));
                        lineString = lineString.replaceAll("%maxplayers%", Byte.toString(maxPlayers));
                        ScoreboardHandler.setLine(key, i, ChatColor.translateAlternateColorCodes('&', lineString));
                    } 
                    //if line isnt null, run this
                    else {
                        //add a space to null line and set
                        blankString = blankString.concat(" ");
                        ScoreboardHandler.setLine(key, i, blankString);
                    }
                }
            }
        }
    }
    /**
    *
    * This method handles game joining.
    * You can specify a player, arena, and weather or not to increment players. 
    *
    * @param player
    * @param arenaMethod
    * @param incrementPlayers
    */
    public void joinGame(Player player, Arena arenaMethod, boolean incrementPlayers) {
        //local vars
        byte currentPlayers = arenaMethod.getCurrentPlayers();
        //handles timer events ingame
        Runnable gameTime = () -> {
            currentSize -= (1 / (double) endAt);
            if (currentSize < 0) {
                currentSize = 0;
            }
            else if (currentSize > 1) {
                currentSize = 1;
            }
            BossbarHandler.setBarSize(player, currentSize);
            currentTime--;
            if (currentTime <= 0) {
                //cycle through all online players
                for (Player key : Bukkit.getOnlinePlayers()) {
                    //get iplayer object
                    iplayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
                    //check if player is both ingame and in arrenaMethod
                    if (iplayer.getInGame() == true && iplayer.getGame().equalsIgnoreCase(arenaMethod.getName())) {
                        //set end of match title
                        TitleHandler.setTitle(key, "&aThe game is over!", ROOT, 10, 100, 10);
                        //leave game
                        this.leaveGame(key, arenaMethod, true);
                    }
                }
                //end game
                arenaMethod.setIsActive(false);
            }
            else { 
                Timer.startTimer(plugin, 0, 1, trueTimer);
            }
        };
        //timer for the game to begin
        Runnable waitTime = () -> {
            //cycle through all online players
            for (Player key : Bukkit.getOnlinePlayers()) {
                //get iplayer object
                iplayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
                //check if player is both ingame and in arrenaMethod
                if (iplayer.getInGame() == true && iplayer.getGame().equalsIgnoreCase(arenaMethod.getName())) {
                    //unfreeze player
                    iplayer.setIsFrozen(false);
                    //output start title
                    TitleHandler.setTitle(key, "&aSTART!", ROOT, 10, 100, 10);
                }
            }
            //start game timer
            trueTimer = gameTime;
            Timer.startTimer(plugin, 0, 1, gameTime);
        };
        //get player
        iplayer = IngotPlayer.selectPlayer(player.getName(), false, null, null);
        //check if incrementPlayers is true
        if (incrementPlayers == true) {
            //increa=ment players
            currentPlayers++;
            arenaMethod.setCurrentPlayers(currentPlayers, false);
        }
        //freeze players and reset data
        iplayer.setInGame(true);
        iplayer.setIsPlaying(true);
        iplayer.setIsFrozen(true);
        iplayer.setGame(arenaMethod.getName());
        //output title
        TitleHandler.setTitle(player, "&aGame starts in &b10 &aseconds...", ROOT, 10, 100, 10);
        //start wait timer
        Timer.startTimer(plugin, 0, 10, waitTime);
    }
    /**
    *
    * This method handles game leaving.You can specify a player, arena, and weather or not to increment players. 
    *
    * @param player
    * @param arenaMethod
     * @param stillInGame
    */
    public void leaveGame(Player player, Arena arenaMethod, boolean stillInGame) {
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //exit
        Location exit = new Location(Bukkit.getWorld(config.getString("Exit.world")), config.getDouble("Exit.x"), config.getDouble("Exit.y"), config.getDouble("Exit.z"), (float) config.getDouble("Exit.yaw"), (float) config.getDouble("Exit.pitch"));
        //local vars
        byte currentPlayers = arenaMethod.getCurrentPlayers();
        //decrement currentPlayers
        currentPlayers--;
        arenaMethod.setCurrentPlayers(currentPlayers,false);
        //update playerdata
        iplayer = IngotPlayer.selectPlayer(player.getName(), false, null, null);
        //reset temp player data
        iplayer.setInGame(false);
        iplayer.setIsPlaying(false);
        iplayer.setIsFrozen(false);
        iplayer.setGame("none");
        //remove scoreboard
        this.updateScoreboard(arenaMethod);
        ScoreboardHandler.clearScoreboard(player);
        BossbarHandler.clearBar(player);
        //teleport to the exit location
        player.teleport(exit);
        //reset tablist to original
        TablistHandler.reset(player);
        //check if arena is empty
        if (currentPlayers <= 1) {
            arenaMethod.setIsActive(false);
        }
    }
}
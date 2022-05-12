package com.budderman18.IngotSurvivalGames.Core;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.ScoreboardHandler;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TablistHandler;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TimerHandler;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TitleHandler;
import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.BossbarHandler;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.ChatHandler;
import com.budderman18.IngotMinigamesAPI.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import static org.bukkit.GameMode.SPECTATOR;
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
    private double currentSize = 1;
    private double currentTime = 0;
    private Runnable trueTimer = null;
    private int waitTime = 0;
    private int gameTime = 0;
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
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //local vars
        byte currentPlayers = arenaMethod.getCurrentPlayers();
        waitTime = arenaMethod.getGameWaitTime();
        gameTime = arenaMethod.getGameLengthTime();
        currentTime = gameTime;
        //handles timer events ingame
        Runnable gameTimer = () -> {
            byte currentPlayerss = arenaMethod.getCurrentPlayers();
            currentSize -= (1 / (double) gameTime);
            if (currentSize < 0) {
                currentSize = 0;
            }
            else if (currentSize > 1) {
                currentSize = 1;
            }
            BossbarHandler.setBarSize(player, currentSize);
            currentTime--;
            for (Player key : Bukkit.getOnlinePlayers()) {
                iplayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
                if (iplayer.getIsPlaying() == true && key.getGameMode() == SPECTATOR) {
                    //set end of match title
                    if (config.getBoolean("Title.enable") == true) {
                        TitleHandler.setTitle(player, config.getString("Title.End.title"), config.getString("Title.End.subtitle"), config.getInt("Title.End.fadein"), config.getInt("Title.End.length"), config.getInt("Title.End.fadeout"));
                        TitleHandler.setActionBar(player, config.getString("Title.End.actionbar"));
                    }
                    iplayer.setIsPlaying(false);
                    currentPlayerss--;
                    arenaMethod.setCurrentPlayers(currentPlayerss, false);
                }
                if (currentPlayerss < 2) {
                    currentTime = 0;
                }
            }
            if (currentTime <= 0) {
                //cycle through all online players
                for (Player key : Bukkit.getOnlinePlayers()) {
                    //get iplayer object
                    iplayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
                    if (iplayer.getInGame() == true && iplayer.getGame() == arenaMethod.getName()) {
                        this.leaveGame(key, arenaMethod, false);
                    }
                }
                //end game
                arenaMethod.setIsActive(false);
                arenaMethod.setStatus("WAITING", false);
                waitTime = arenaMethod.getGameWaitTime();
                gameTime = arenaMethod.getGameLengthTime();
            }
            else { 
                TimerHandler.startTimer(plugin, 0, 1, trueTimer);
            }
        };
        //timer for the game to begin
        Runnable waitTimer = () -> {
            byte currentPlayerss = arenaMethod.getCurrentPlayers();
            //cycle through all online players
            for (Player key : Bukkit.getOnlinePlayers()) {
                //get iplayer object
                iplayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
                //check if player is both ingame and in arrenaMethod
                if (iplayer.getInGame() == true && iplayer.getGame().equalsIgnoreCase(arenaMethod.getName())) {
                    //unfreeze player
                    iplayer.setIsFrozen(false);
                    //output start title
                    if (config.getBoolean("Title.enable") == true) {
                        TitleHandler.setTitle(player, config.getString("Title.InGameRelease.title"), config.getString("Title.InGameRelease.subtitle"), config.getInt("Title.InGameRelease.fadein"), config.getInt("Title.InGameRelease.length"), config.getInt("Title.InGameRelease.fadeout"));
                        TitleHandler.setActionBar(player, config.getString("Title.InGameRelease.actionbar"));
                    }
                }
                if (iplayer.getInGame() == true && key.getGameMode() == SPECTATOR) {
                    currentPlayerss--;
                    arenaMethod.setCurrentPlayers(currentPlayerss, false);
                }
                if (arenaMethod.getCurrentPlayers() < 2) {
                    for (Player keys : Bukkit.getOnlinePlayers()) {
                        iplayer = IngotPlayer.selectPlayer(keys.getName(), false, null, null);
                        if (iplayer.getIsPlaying() == true && iplayer.getGame() == arenaMethod.getName()) {
                            ChatHandler ChatHandler = new ChatHandler();
                            ChatHandler.sendMessageToAll("&6&l" + iplayer.getUsername() + "&r&b&owins!", true);
                        }
                    }
                    this.leaveGame(key, arenaMethod, false);
                }
            }
            //start game timer
            trueTimer = gameTimer;
            TimerHandler.startTimer(plugin, 0, 1, gameTimer);
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
        if (config.getBoolean("Title.enable") == true) {
            TitleHandler.setTitle(player, config.getString("Title.InGameStart.title") + arenaMethod.getGameWaitTime() + " seconds.", config.getString("Title.InGameStart.subtitle"), config.getInt("Title.InGameStart.fadein"), config.getInt("Title.InGameStart.length"), config.getInt("Title.InGameStart.fadeout"));
            TitleHandler.setActionBar(player, config.getString("Title.InGameStart.actionbar"));
        }
        //start wait timer
        TimerHandler.startTimer(plugin, 0, waitTime, waitTimer);
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
        FileConfiguration arenaData = FileManager.getCustomData(plugin,"settings",arenaMethod.getFilePath());
        //exit
        Location exitLoc =  new Location(Bukkit.getWorld(arenaData.getString("Exit.world")), arenaData.getDouble("Exit.x"), arenaData.getDouble("Exit.y"), arenaData.getDouble("Exit.z"),(float) arenaData.getDouble("Exit.yaw"),(float) arenaData.getDouble("Exit.pitch"));
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
        iplayer.setGame(null);
        this.isSpectator(player, false);
        //remove scoreboard
        this.updateScoreboard(arenaMethod);
        ScoreboardHandler.clearScoreboard(player);
        BossbarHandler.clearBar(player);
        //teleport to the exit location
        player.teleport(exitLoc);
        //reset tablist to original
        TablistHandler.reset(player);
        //check if arena is empty
        if (currentPlayers <= 1) {
            arenaMethod.setIsActive(false);
            arenaMethod.setStatus("WAITING", false);
        }
        if (stillInGame == false) {
            //set leave title
            if (config.getBoolean("Title.enable") == true) {
                TitleHandler.setTitle(player, config.getString("Title.Leave.title"), config.getString("Title.Leave.subtitle"), config.getInt("Title.Leave.fadein"), config.getInt("Title.Leave.length"), config.getInt("Title.Leave.fadeout"));
                TitleHandler.setActionBar(player, config.getString("Title.Leave.actionbar"));
            }
        }
    }
    /**
    * 
    * This method changes a player into spectator mode. 
    * 
    * @param player
    * @param spectating
    */
    public void isSpectator(Player player, boolean spectating) {
        FileConfiguration config = FileManager.getCustomData(plugin, "config", ROOT);
        if (spectating == true) {
            player.setGameMode(GameMode.SPECTATOR);
            if (config.getBoolean("Title.enable") == true) {
                TitleHandler.setTitle(player, config.getString("Title.Death.title"), config.getString("Title.Death.subtitle"), config.getInt("Title.Death.fadein"), config.getInt("Title.Death.length"), config.getInt("Title.Death.fadeout"));
                TitleHandler.setActionBar(player, config.getString("Title.Death.actionbar"));
            }
        }
        else {
            player.setGameMode(GameMode.ADVENTURE);
        }
    }
}
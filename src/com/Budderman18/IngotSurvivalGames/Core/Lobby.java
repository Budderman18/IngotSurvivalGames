package com.budderman18.IngotSurvivalGames.Core;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.ScoreboardHandler;
import com.budderman18.IngotMinigamesAPI.Core.Data.Spawn;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TablistHandler;
import com.budderman18.IngotMinigamesAPI.Core.Data.Timer;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TitleHandler;
import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.BossbarHandler;
import com.budderman18.IngotMinigamesAPI.Main;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
/**
 * 
 * This class handles the core to the lobby
 * Anything lobby-based not specific to SurvivalGames
 * (except arena management) is handled here
 * 
 */
public class Lobby implements Listener {
    //plugin
    private final Plugin plugin = Main.getInstance();
    //files
    private final String ROOT = "";
    private FileConfiguration arenaData = null;
    //classes
    private final IngotPlayer IngotPlayer = new IngotPlayer(plugin);
    private final Spawn Spawn = new Spawn(plugin);
    private Game game = new Game();
    private Timer currentTimer = null;
    //global vars
    private IngotPlayer iplayer = null;
    /**
    *
    * This method updates the scoreboard for every player. 
    * This does NOT delete or create, only updates. 
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
            //check if player is ingame
            iplayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
            //check if player is inGame
            if (iplayer.getInGame() == true && iplayer.getGame().equals(arenaMethod.getName())) {
                //set title
                ScoreboardHandler.setTitle(key, ChatColor.translateAlternateColorCodes('&', "&7[&6IngotSurvivalGames&7]"));
                //cycle though scoreboard limit
                for (byte i = 0; i < config.getInt("Scoreboard.maxLines"); i++) {
                    //check if line is not null
                    if (config.getString("Scoreboard.line" + i) != null) {
                        //set lines
                        lineString = config.getString("Scoreboard.line" + i);
                        lineString = lineString.replaceAll("%currentplayers%", Byte.toString(arenaMethod.getCurrentPlayers()));
                        lineString = lineString.replaceAll("%maxplayers%", Byte.toString(maxPlayers));
                        ScoreboardHandler.setLine(key, i, ChatColor.translateAlternateColorCodes('&', lineString));
                    } 
                    //run if line is null
                    else {
                        //add a spaace to null line
                        blankString = blankString.concat(" ");
                        //set line
                        ScoreboardHandler.setLine(key, i, blankString);
                    }
                }
            }
        }
    }
    /**
    *
    * This method handles lobby joining. 
    * Just specify the player and they're in! 
    * 
    * @param player
    * @param arenaMethod
    */
    public void joinLobby(Player player, Arena arenaMethod) {
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //local vars
        Location lobbyloc =  new Location(Bukkit.getWorld(config.getString("Lobby.world")), config.getDouble("Lobby.x"), config.getDouble("Lobby.y"), config.getDouble("Lobby.z"),(float) config.getDouble("Lobby.yaw"),(float) config.getDouble("Lobby.pitch"));
        byte currentPlayers = arenaMethod.getCurrentPlayers();
        long start = 5;
        long end = 0;
        //create or obtain ingotplayer
        iplayer = IngotPlayer.selectPlayer(player.getName(), false, null, null);
        //save changes
        iplayer.setInGame(true);
        iplayer.setGame(arenaMethod.getName());
        //increase players in lobby
        currentPlayers++;
        arenaMethod.setCurrentPlayers(currentPlayers, false);
        //teleport to lobby
        player.teleport(lobbyloc);
        //edit tablist
        if (config.getBoolean("Tablist.enable") == true) {
            //set header and footer
            TablistHandler.setHeader(player, config.getString("Tablist.header"));
            TablistHandler.setFooter(player, config.getString("Tablist.footer"));
        }
        //DO NOT USE REMOVE PLAYER METHODS, THEY DO NOT WORK
        //tablist.removePlayers(player);
        //check if titles are enabled
        if (config.getBoolean("Title.enable") == true) {
            //set title and actionbar
            TitleHandler.setTitle(player, config.getString("Title.Join.title"), config.getString("Title.Join.subtitle"), config.getInt("Title.Join.fadein"), config.getInt("Title.Join.length"), config.getInt("Title.Join.fadeout"));
            TitleHandler.setActionBar(player, config.getString("Title.Join.actionbar"));
        }
        //check if scoreboards are enabled
        if (config.getBoolean("Scoreboard.enable") == true) {
            ScoreboardHandler.clearScoreboard(player);
            ScoreboardHandler.setTitle(player, ChatColor.translateAlternateColorCodes('&', "&7[&6IngotSurvivalGames&7]"));
        }
        //bossbar
        if (config.getBoolean("Bossbar.enable") == true) {
            BossbarHandler.setBarTitle(player, config.getString("Bossbar.title"));
            BossbarHandler.setBarColor(player, config.getString("Bossbar.color"));
            BossbarHandler.setBarSize(player, 1);
            BossbarHandler.displayBar(true);
        }
        Runnable action = () -> {
            //current player
            IngotPlayer currentIPlayer = new IngotPlayer(plugin);
            //spawns
            arenaData = FileManager.getCustomData(plugin, "settings", "/Arenas/" + arenaMethod.getName() + '/');
            List<String> currentSpawn = null;
            List<Spawn> spawns = new ArrayList<>(); 
            //cycle through all loaded spawn names
            for (short i = 1; i < 65534; i++) {
                currentSpawn = new ArrayList<>();
                currentSpawn.add(arenaData.getString("Spawnpoints.Spawn" + Short.toString(i) + ".name"));
                currentSpawn.add(arenaData.getString("Spawnpoints.Spawn" + Short.toString(i) + ".x"));
                currentSpawn.add(arenaData.getString("Spawnpoints.Spawn" + Short.toString(i) + ".y"));
                currentSpawn.add(arenaData.getString("Spawnpoints.Spawn" + Short.toString(i) + ".z"));
                if (currentSpawn.get(0) != null) {
                    spawns.add(Spawn.selectSpawn("Spawn" + Short.toString(i), false, null, null));
                    arenaMethod.addSpawn(spawns.get(i-1));
                }
                else {
                    break;
                }
            }
            //cycle through all players
            for (Player key : Bukkit.getOnlinePlayers()) {
                //get iplayerdata
                currentIPlayer = iplayer.selectPlayer(key.getName(), false, null, null);
                //check if iplayer is ingame
                if (currentIPlayer.getInGame() == true && currentIPlayer.getGame().equalsIgnoreCase(arenaMethod.getName())) {
                    //move to spawn
                    Spawn.moveToRandomSpawn(spawns, key);
                    //leave lobby and join game
                    this.leaveLobby(key, arenaMethod, true);
                }
            }
        };
        //start timer
        currentTimer = Timer.startTimer(plugin, start, end, action);
        //check if scoreboards are enabled
        if (config.getBoolean("Scoreboard.enable") == true) {
            //update Scoreboard
            this.updateScoreboard(arenaMethod);
        }
        //check if nobody is currently playing to prevent errors
        if (currentPlayers > 1) {
            currentTimer.endTimer(false, null);
        }
        //start timer
        currentTimer.startTimer(plugin, start, end, action);
    }
    /**
    *
    * This method handles lobby leaving
    * Just specify the player and they're out!
    * You can also specify if they should still be
    * considered ingame or not (useful to differenciate
    * entering a match and leaving the game entirely)
    *
    * @param player
    * @param arenaMethod
    * @param stillInGame
    */
    public void leaveLobby(Player player, Arena arenaMethod, boolean stillInGame) {
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //local vars
        Location exit = new Location(Bukkit.getWorld(config.getString("Exit.world")), config.getDouble("Exit.x"), config.getDouble("Exit.y"), config.getDouble("Exit.z"), (float) config.getDouble("Exit.yaw"), (float) config.getDouble("Exit.pitch"));
        byte currentPlayers = arenaMethod.getCurrentPlayers();
        iplayer = IngotPlayer.selectPlayer(player.getName(), false, null, null);
        //check if player should not be ingame
        if (stillInGame == false) {
            //decrease players in game
            currentPlayers--;
            arenaMethod.setCurrentPlayers(currentPlayers, false);
            //reset booleans
            iplayer.setInGame(false);
            iplayer.setIsPlaying(false);
            iplayer.setIsFrozen(false);
            iplayer.setGame("none");
            //remove scoreboard
            ScoreboardHandler.clearScoreboard(player);
            //update scoreboard
            this.updateScoreboard(arenaMethod);
            //clear bossbar
            BossbarHandler.clearBar(player);
            //teleport to the exit location
            player.teleport(exit);
            //reset tablist to original
            if (config.getBoolean("Tablist.enable") == true) {
                //reset tablist
                TablistHandler.reset(player);
            }
            if (config.getBoolean("Title.enable") == true) {
                //set title and actionbar
                TitleHandler.setTitle(player, config.getString("Title.Leave.title"), config.getString("Title.Leave.subtitle"), config.getInt("Title.Leave.fadein"), config.getInt("Title.Leave.length"), config.getInt("Title.Leave.fadeout"));
                TitleHandler.setActionBar(player, config.getString("Title.Leave.actionbar"));
            }
            //end timer
            currentTimer.endTimer(false, null);
            //check if arena is empty
            if (currentPlayers <= 1) {
                arenaMethod.setIsActive(false);
            }
        }
        //if player is still ingame
        else {
            //set booleans
            iplayer.setInGame(true);
            iplayer.setIsPlaying(true);
            //join game
            game.joinGame(player, arenaMethod, false);
            //start game
            arenaMethod.setIsActive(true);
        }
    }
}

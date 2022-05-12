package com.budderman18.IngotSurvivalGames.Core;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import static com.budderman18.IngotMinigamesAPI.Core.Data.Arena.RUNNING;
import static com.budderman18.IngotMinigamesAPI.Core.Data.Arena.WAITING;
import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.ScoreboardHandler;
import com.budderman18.IngotMinigamesAPI.Core.Data.Spawn;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TablistHandler;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TimerHandler;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.TitleHandler;
import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.BossbarHandler;
import com.budderman18.IngotMinigamesAPI.Main;
import java.util.ArrayList;
import java.util.Arrays;
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
    private TimerHandler currentTimer = null;
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
        arenaData = FileManager.getCustomData(plugin, "settings", arenaMethod.getFilePath());
        //local vars
        Location lobbyloc =  new Location(Bukkit.getWorld(arenaData.getString("Lobby.world")), arenaData.getDouble("Lobby.x"), arenaData.getDouble("Lobby.y"), arenaData.getDouble("Lobby.z"),(float) arenaData.getDouble("Lobby.yaw"),(float) arenaData.getDouble("Lobby.pitch"));
        byte currentPlayers = arenaMethod.getCurrentPlayers();
        long start = arenaMethod.getLobbyWaitTime();
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
            Spawn tempspawn = null;
            int tempx = 0;
            int tempy = 0;
            int tempz = 0;
            //cycle through all loaded spawn names
            for (short i = 0; i < arenaMethod.getSpawns().size(); i++) {
                currentSpawn = new ArrayList<>();
                currentSpawn.add(arenaMethod.getSpawns().get(i).getName());
                currentSpawn.add(Double.toString(arenaMethod.getSpawns().get(i).getLocation()[0]));
                currentSpawn.add(Double.toString(arenaMethod.getSpawns().get(i).getLocation()[1]));
                currentSpawn.add(Double.toString(arenaMethod.getSpawns().get(i).getLocation()[2]));
                if (currentSpawn.get(0) != null) {
                    spawns.add(Spawn.selectSpawn("Spawn" + Short.toString((short) (i + 1)), false, null, null));
                }
            }
            //cycle through all players
            for (Player key : Bukkit.getOnlinePlayers()) {
                currentIPlayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
                //check if iplayer is ingame
                if (currentIPlayer.getInGame() == true && currentIPlayer.getGame().equalsIgnoreCase(arenaMethod.getName())) {
                    //move to spawn
                    tempspawn = Spawn.moveToRandomSpawn(spawns, key);
                    while (tempspawn.getIsOccupied() == true) {
                        tempspawn = Spawn.moveToRandomSpawn(spawns, key);
                    }
                    tempspawn.setIsOccupied(true);
                    //leave lobby and join game
                    this.leaveLobby(key, arenaMethod, true);
                }
            }
        };
        //check if scoreboards are enabled
        if (config.getBoolean("Scoreboard.enable") == true) {
            //update Scoreboard
            this.updateScoreboard(arenaMethod);
        }
        //check if nobody is currently playing to prevent errors
        if (arenaMethod.getCurrentPlayers() > arenaMethod.getMinPlayers()) {
            currentTimer.endTimer(false, null);
            currentTimer = TimerHandler.startTimer(plugin, start, end, action);
        }
        //start timer
        if (arenaMethod.getCurrentPlayers() == arenaMethod.getMinPlayers()) {
            for (Player key : Bukkit.getOnlinePlayers()) {
                //current player
                IngotPlayer currentIPlayer = iplayer.selectPlayer(key.getName(), false, null, null);
                if (config.getBoolean("Title.enable") == true && currentIPlayer.getInGame() == true) {
                    //set title and actionbar
                    TitleHandler.setTitle(Bukkit.getPlayer(currentIPlayer.getUsername()), config.getString("Title.Start.title") + arenaMethod.getLobbyWaitTime() + " seconds.", config.getString("Title.Start.subtitle"), config.getInt("Title.Start.fadein"), config.getInt("Title.Start.length"), config.getInt("Title.Start.fadeout"));
                    TitleHandler.setActionBar(Bukkit.getPlayer(currentIPlayer.getUsername()), config.getString("Title.Start.actionbar"));
                }
            }
            currentTimer = TimerHandler.startTimer(plugin, start, end, action);
        }
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
        FileConfiguration arenaData = FileManager.getCustomData(plugin,"settings",arenaMethod.getFilePath());
        //local vars
        Location exitLoc = new Location(Bukkit.getWorld(arenaData.getString("Exit.world")), arenaData.getDouble("Exit.x"), arenaData.getDouble("Exit.y"), arenaData.getDouble("Exit.z"),(float) arenaData.getDouble("Exit.yaw"),(float) arenaData.getDouble("Exit.pitch"));
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
            player.teleport(exitLoc);
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
                arenaMethod.setStatus("WAITING", false);
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
            arenaMethod.setStatus("RUNNING", false);
        }
    }
}

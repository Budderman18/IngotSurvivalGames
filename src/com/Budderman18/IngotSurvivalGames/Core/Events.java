/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.budderman18.IngotSurvivalGames.Core;

import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Handlers.ChatHandler;
import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import static org.bukkit.GameMode.ADVENTURE;
import static org.bukkit.GameMode.SPECTATOR;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import static org.bukkit.entity.EntityType.PLAYER;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 *
 * This class handles protection-based events. 
 * You can block building, breaking, and block/entity interactions
 * This contains events and has to be within every plugin, 
 * rather than in IngotMiniGamesAPI. 
 * 
 */
public class Events implements Listener {
    //plugin
    private final Plugin plugin = Main.getInstance();
    //files
    private final String ROOT = "";
    //classes
    private final IngotPlayer IngotPlayer = new IngotPlayer(plugin);
    //global vars
    private boolean inGame = false;
    private boolean isPlaying = false;
    private boolean isFrozen = false;
    private IngotPlayer iplayer = null;
    /**
    *
    * This method handles block breaking. 
    *
    * @param event
    */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //obtain IngotPlayerdata
        iplayer = IngotPlayer.selectPlayer(event.getPlayer().getName(), false, null, null);
        inGame = iplayer.getInGame();
        //check if protection is enabled and player is ingame
        if (config.getBoolean("enable-protection") == true && inGame == true) {  
            //block event
            event.setCancelled(true);
        }
    }
    /**
    *
    * This method handles block placing. 
    *
    * @param event
    */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //get IngotPlayerdata
        iplayer = IngotPlayer.selectPlayer(event.getPlayer().getName(), false, null, null);
        inGame = iplayer.getInGame();
        //check if protection is enabled
        if (config.getBoolean("enable-protection") == true && inGame == true) {  
            //block event
            event.setCancelled(true);
        }
    } 
    /**
    *
    * This method handles block interactions
    *
    * @param event
    */
    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {       
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //get IngotPlayerdata
        iplayer = IngotPlayer.selectPlayer(event.getPlayer().getName(), false, null, null);
        inGame = iplayer.getInGame();
        isPlaying = iplayer.getIsPlaying();
        //check if player is currently playing
        if (isPlaying == true) {
            //don't cancel event
            event.setCancelled(false);
        }
        //if above checkl fails, check if protection is enabled and player is inGame
        else if (config.getBoolean("enable-protection") == true && inGame == true) { 
            //cancel event
            event.setCancelled(true);
        }
    }
    /**
    *
    * This method handles entity interactions (right click). 
    *
    * @param event
    */
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //get IngotPlayerdata
        iplayer = IngotPlayer.selectPlayer(event.getPlayer().getName(), false, null, null);
        inGame = iplayer.getInGame();
        //check if protection is enabled and player is inGame
        if (config.getBoolean("enable-protection") == true && inGame == true) { 
            //cancel event
            event.setCancelled(true);
        }
    }
    /**
    *
    * This method handles attacking entities. 
    * Players are not protected while playing. 
    *
    * @param event
    */
    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //check if entity is a player
        if (event.getEntityType() == PLAYER) {
            //get IngotPlayerdata
            iplayer = IngotPlayer.selectPlayer(event.getDamager().getName(), false, null, null); 
            inGame = iplayer.getInGame();
            isPlaying = iplayer.getIsPlaying();
            //check if player is currently playing
            if (isPlaying == true) {
                //don't cancel event
                event.setCancelled(false);
            }
            //if above check fails, check if protection is enabled and player is inGame
            else if (config.getBoolean("enable-protection") == true && inGame == true) { 
                //cancel event
                event.setCancelled(true);
            }
        }
    }
    /**
    *
    * This method handles player movement(including rotation changes).
    *
    * @param event
    */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        //local vars
        Location frozenpos = null;
        //get IngotPlayerdata
        iplayer = IngotPlayer.selectPlayer(event.getPlayer().getName(), false, null, null);
        isFrozen = iplayer.getIsFrozen();
        //get arena
        Arena arena = new Arena(plugin);
        Arena selectedArena = arena;
        if (iplayer.getGame() != null) {
            selectedArena = arena.selectArena(iplayer.getGame(), false, null, null);
        }
        //checkl if player is froxen
        if (isFrozen == true) {
            //obtain position player moves to
            frozenpos = event.getTo();
            //reset position to current position
            frozenpos.setX(event.getFrom().getX());
            frozenpos.setY(event.getFrom().getY());
            frozenpos.setZ(event.getFrom().getZ());
            //teleport player
            event.getPlayer().teleport(frozenpos);
        }
        //check if player is spectating and in arena
        if (iplayer.getGame() != null) {
            if (isPlaying == true && event.getPlayer().getGameMode() == SPECTATOR && selectedArena.isInArena(event.getPlayer()) == false) {
                //move player to center
                frozenpos = event.getPlayer().getLocation();
                frozenpos.setX(0);
                frozenpos.setY(64);
                frozenpos.setZ(0);
                event.getPlayer().teleport(frozenpos);
            }
        }
    }
    /**
    *
    * This method handles player chatting. 
    *
    * @param event
    */
    @EventHandler    
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        //files
        FileConfiguration config = FileManager.getCustomData(plugin,"config",ROOT);
        //local vars
        List<Player> players = new ArrayList<>();
        IngotPlayer currentIPlayer = null;
        Player player = null;
        //IngotPlayerdata
        iplayer = IngotPlayer.selectPlayer(event.getPlayer().getName(), false, null, null);
        inGame = iplayer.getInGame();
        //check if player is inGame and chat is enabled
        if (inGame == true && config.getBoolean("Chat.enable") == true) {
            //cancel event
            event.setCancelled(true);
            //cycle between all players
            for (Player key : Bukkit.getOnlinePlayers()) {
                //get IngotPlayer
                currentIPlayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
                inGame = currentIPlayer.getInGame();
                //checfk if player is inGame
                if (inGame == true && currentIPlayer.getGame().equals(iplayer.getGame())) {
                    //add player to list
                    players.add(key);
                }
            }
            //cycle between all assigned players
            for (short i = 0; i < players.size(); i++) {
                //send message to player
                player = players.get(i);
                player.sendMessage(ChatHandler.convertMessage(Bukkit.getPlayer(iplayer.getUsername()), event.getMessage(), config.getString("Chat.format")));
            }
        }
    }
    /**
    *
    * This method handles player deaths. 
    *
    * @param event
    */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        //files
        FileConfiguration arenaData = null;
        Location spec = null;
        Arena Arena = new Arena(plugin);
        Arena currentArena = null;
        if (event.getEntityType() == PLAYER)  {
            IngotPlayer selectedPlayer = iplayer.selectPlayer(event.getEntity().getName(), false, null, null);
            if (selectedPlayer.getIsPlaying() == true) {
                Game game = new Game();
                game.isSpectator(event.getEntity(), true);
                currentArena = Arena.selectArena(iplayer.getGame(), false, null, null);
                arenaData = FileManager.getCustomData(plugin, "settings", currentArena.getFilePath());
                spec = new Location(Bukkit.getWorld(currentArena.getWorld()), arenaData.getDouble("Spec.x"), arenaData.getDouble("Spec.y"), arenaData.getDouble("Spec.z"), (float) arenaData.getDouble("Spec.yaw"), (float) arenaData.getDouble("Spec.pitch"));
                event.getEntity().teleport(spec);
            }
        }
    }
    /**
    *
    * This method handles player joining. 
    *
    * @param event
    */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //files
        File playerdataf = new File(plugin.getDataFolder(), "playerdata.yml");
        FileConfiguration pd = FileManager.getCustomData(plugin, "playerdata", ROOT);
        //get player
        Player player = event.getPlayer();
        //create IngotPlayer object
        IngotPlayer newPlayer = IngotPlayer.createPlayer(player);
        if (pd.getString(player.getName()) == null) {
            //set data
            pd.createSection(player.getName());
            //save to file
            pd.set(event.getPlayer().getName() + ".uuid", event.getPlayer().getUniqueId().toString());
            try {
                pd.save(playerdataf);
            } 
            catch (IOException ex) {
                Logger.getLogger(Events.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            newPlayer.setUsername(player.getName());
            newPlayer.setUUID(pd.getString(event.getPlayer().getName() + ".uuid"));
        }
    }
    /**
    *
    * This method handles player leaving. 
    *
    * @param event
    */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        try {
            //IngotPlayerdata
            iplayer = IngotPlayer.selectPlayer(event.getPlayer().getName(), false, null, null);
            iplayer.deletePlayer();
            if (iplayer.getIsPlaying() == true) {
                event.getPlayer().setGameMode(ADVENTURE);
            }
        }
        catch (IndexOutOfBoundsException ex) {}
    }
}

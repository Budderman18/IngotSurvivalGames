package com.budderman18.IngotMinigamesAPI;

import com.budderman18.IngotMinigamesAPI.Core.Data.FileManager;
import com.budderman18.IngotMinigamesAPI.Core.Data.Arena;
import com.budderman18.IngotMinigamesAPI.Core.Data.IngotPlayer;
import com.budderman18.IngotMinigamesAPI.Core.Data.Spawn;
import com.budderman18.IngotSurvivalGames.Commands.SGAdminCommand;
import com.budderman18.IngotSurvivalGames.Commands.SGCommand;
import com.budderman18.IngotSurvivalGames.Core.Events;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * This class enables and disables the plugin. 
 * It also imports commands and handles events. 
 * 
 */
public final class Main extends JavaPlugin implements Listener { 
    //retrive plugin instance
    private static Main plugin;
    //classes
    private final IngotPlayer IngotPlayer = new IngotPlayer(plugin);
    private static final Arena Arena = new Arena(plugin);
    //version
    private static final String VERSION = "1.0";
    //global vars
    private final String ROOT = "";
    private final ConsoleCommandSender sender = getServer().getConsoleSender();
    /**
    *
    * This method retrieves the current plugin data
    *
    * @return 
    */
    public static Main getInstance() {
        return plugin;
    }
    /**
    *
    * This method loads in all arenas from the arenas folder
    * Useful for startup and reloading
    *
    */
    public static void loadArenas() {
        //files
        FileConfiguration arenaData = null;
        File loadArenas = new File(plugin.getDataFolder() + "/Arenas/");
        File temparenaf = null;
        //arena
        Arena temparena = null;
        int[] pos1 = new int[3];
        int[] pos2 = new int[3];
        String world = null;
        String name = null;
        byte minPlayers = 0;
        byte maxPlayers = 0;
        byte skipTimerAt = 0;
        int lobbyWaitTime = 0;
        int lobbySkipTime = 0;
        int gameWaitTime = 0;
        int gameLengthTime = 0;
        //Spawn
        Spawn spawn = new Spawn(plugin);
        Spawn tempspawn = null;
        List<String> currentSpawn = null;
        String namee = null;
        double x = 0;
        double y = 0;
        double z = 0;
        double[] lobby = new double[6];
        String lobbyWorld = null;
        double[] exit = new double[6];
        String exitWorld = null;
        double[] spec = new double[6];
        double[] center = new double[6];
        //language
        FileConfiguration language = FileManager.getCustomData(plugin, "language", "");
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message")); 
        String arenaLoadedMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Arena-Loaded-Message")); 
        for (String key : loadArenas.list()) {
            //get file
            temparenaf = new File(plugin.getDataFolder() + "/Arenas/" + key + '/', "settings.yml");
            arenaData = FileManager.getCustomData(plugin, "settings", "/Arenas/" + key + '/');
            //check if arena has a settings file
            if (temparenaf.exists()) {
                //get positions
                pos1[0] = arenaData.getInt("pos1.x");
                pos1[1] = arenaData.getInt("pos1.y");
                pos1[2] = arenaData.getInt("pos1.z");
                pos2[0] = arenaData.getInt("pos2.x");
                pos2[1] = arenaData.getInt("pos2.y");
                pos2[2] = arenaData.getInt("pos2.z");
                lobbyWorld = arenaData.getString("Lobby.world");
                lobby[0] = arenaData.getDouble("Lobby.x");
                lobby[1] = arenaData.getDouble("Lobby.y");
                lobby[2] = arenaData.getDouble("Lobby.z");
                lobby[3] = arenaData.getDouble("Lobby.yaw");
                lobby[4] = arenaData.getDouble("Lobby.pitch");
                exitWorld = arenaData.getString("Exit.world");
                exit[0] = arenaData.getDouble("Exit.x");
                exit[1] = arenaData.getDouble("Exit.y");
                exit[2] = arenaData.getDouble("Exit.z");
                exit[3] = arenaData.getDouble("Exit.yaw");
                exit[4] = arenaData.getDouble("Exit.pitch");
                spec[0] = arenaData.getDouble("Spec.x");
                spec[1] = arenaData.getDouble("Spec.y");
                spec[2] = arenaData.getDouble("Spec.z");
                spec[3] = arenaData.getDouble("Spec.yaw");
                spec[4] = arenaData.getDouble("Spec.pitch");
                center[0] = arenaData.getDouble("Center.x");
                center[1] = arenaData.getDouble("Center.y");
                center[2] = arenaData.getDouble("Center.z");
                center[3] = arenaData.getDouble("Center.yaw");
                center[4] = arenaData.getDouble("Center.pitch");
                //get world
                world = arenaData.getString("world");
                //get name
                name = arenaData.getString("name");
                //get player vars
                minPlayers = (byte) arenaData.getInt("minPlayers");
                maxPlayers = (byte) arenaData.getInt("maxPlayers");
                skipTimerAt = (byte) arenaData.getInt("skip-timer-at");
                //get timer vars
                lobbyWaitTime = arenaData.getInt("lobby-wait-time");
                lobbySkipTime = arenaData.getInt("lobby-start-time");
                gameWaitTime = arenaData.getInt("game-wait-time");
                gameLengthTime = arenaData.getInt("game-length-time");
                //create arena
                temparena = Arena.createArena(pos1, pos2, world, name, minPlayers, maxPlayers, skipTimerAt, lobbyWaitTime, lobbySkipTime, gameWaitTime, gameLengthTime, lobby, lobbyWorld, exit, exitWorld, spec, center, false, "/Arenas/" + key + '/');
                //cycle spawns
                for (short i = 1; i < 65534; i++) {
                    //load from file
                    currentSpawn = new ArrayList<>();
                    currentSpawn.add(arenaData.getString("Spawnpoints.Spawn" + Short.toString(i) + ".name"));
                    currentSpawn.add(arenaData.getString("Spawnpoints.Spawn" + Short.toString(i) + ".x"));
                    currentSpawn.add(arenaData.getString("Spawnpoints.Spawn" + Short.toString(i) + ".y"));
                    currentSpawn.add(arenaData.getString("Spawnpoints.Spawn" + Short.toString(i) + ".z"));
                    if (currentSpawn.get(0) != null) {
                        //set name
                        namee = currentSpawn.get(0);
                        //set position
                        x = Double.parseDouble(currentSpawn.get(1));
                        y = Double.parseDouble(currentSpawn.get(2));
                        z = Double.parseDouble(currentSpawn.get(3));
                        //create spawn
                        tempspawn = spawn.createSpawn(namee, x, y, z);
                        temparena.addSpawn(tempspawn);
                    }
                    else {
                        break;
                    }
                }
                //notify arena is loaded
                Bukkit.getServer().getConsoleSender().sendMessage(prefixMessage + arenaLoadedMessage + name);
            }
        }
    }
    /**
    *
    * This method creates files if needed. 
    * Only needed if file is missing (first usage). 
    *
    */
    private void createFiles() {
        //config file
        File configf = new File(getDataFolder(), "config.yml");
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
         }
        //config object
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configf);
        } 
        catch (IOException | InvalidConfigurationException e) {}
        //langauge file
        File languagef = new File(getDataFolder(), "language.yml");
        if (!languagef.exists()) {
            languagef.getParentFile().mkdirs();
            saveResource("language.yml", false);
         }
        //langauge object
        FileConfiguration language = new YamlConfiguration();
        try {
            language.load(languagef);
        } 
        catch (IOException | InvalidConfigurationException e) {}
        //playerdata
        File playerdataf = new File(getDataFolder(), "playerdata.yml");
        if (!playerdataf.exists()) {
            languagef.getParentFile().mkdirs();
            saveResource("playerdata.yml", false);
         }
        //pd object
        FileConfiguration pd = new YamlConfiguration();
        try {
            pd.load(playerdataf);
        } 
        catch (IOException | InvalidConfigurationException e) {}
        //arena folder
        File arenaFolder = new File(getDataFolder(), "/Arenas/");
        if (!arenaFolder.exists()) {
            arenaFolder.mkdirs();
        }
    } 
    /**
    *
    * Enables the plugin.
    * Checks if MC version isn't the latest.
    * If its not, warn the player about lacking support
    * Checks if server is running offline mode
    * If it is, disable the plugin
    * Also loads commands and events
    * Also loads in arenas and spawns
    *
    */
    @Override
    public void onEnable() {
        //create plugin instance
        plugin = this;
        //creates files if needed
        createFiles();
        //language variables
        FileConfiguration language = FileManager.getCustomData(plugin,"language",ROOT);
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message")); 
        String unsupportedVersionAMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-VersionA-Message")); 
        String unsupportedVersionBMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-VersionB-Message")); 
        String unsupportedVersionCMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsupported-VersionC-Message")); 
        String unsecureServerAMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerA-Message")); 
        String unsecureServerBMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerB-Message")); 
        String unsecureServerCMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Unsecure-ServerC-Message")); 
        String pluginEnabledMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Plugin-Enabled-Message")); 
        //check for correct version
        if (!(Bukkit.getVersion().contains("1.18.2"))) {
            sender.sendMessage(prefixMessage + unsupportedVersionAMessage);
            sender.sendMessage(prefixMessage + unsupportedVersionBMessage);
            sender.sendMessage(prefixMessage + unsupportedVersionCMessage); 
        }
        //check for online mode
        if ((getServer().getOnlineMode())) {
            sender.sendMessage(prefixMessage + unsecureServerAMessage);
            sender.sendMessage(prefixMessage + unsecureServerBMessage);
            sender.sendMessage(prefixMessage + unsecureServerCMessage);
            getServer().getPluginManager().disablePlugin(this);
        }
        //commands
        this.getCommand("sg").setExecutor(new SGCommand());
        this.getCommand("sgadmin").setExecutor(new SGAdminCommand());
        //events
        getServer().getPluginManager().registerEvents(new Events(),this);
        //load arenas
        this.loadArenas();
        //enable plugin
        getServer().getPluginManager().enablePlugin(this);
        sender.sendMessage(prefixMessage + pluginEnabledMessage);
    }
    /**
    *
    * This method disables the plugin
    * It also saves all files and clears
    * all loaded player data
    *
    */
    @Override
    public void onDisable() {
        plugin = this;
        //import files
        File configf = new File("plugins/IngotSurvivalGames","config.yml");
        File languagef = new File("plugins/IngotSurvivalGames","language.yml");
        File playerdataf = new File("plugins/IngotSurvivalGames","playerdata.yml");
        FileConfiguration config = FileManager.getCustomData(plugin, "config", ROOT);
        FileConfiguration language = FileManager.getCustomData(plugin, "language", ROOT);
        FileConfiguration pd = FileManager.getCustomData(plugin, "playerdata", ROOT);
        //language
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message")); 
        String pluginDisabledMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Plugin-Disabled-Message")); 
        //local vars
        IngotPlayer currentIPlayer = null;
        //reset iplayer booleans
        for (Player key : Bukkit.getOnlinePlayers()) {
            try {
                //get iplayerdata
                currentIPlayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
                //reset vars
                currentIPlayer.setInGame(false);
                currentIPlayer.setIsPlaying(false);
                currentIPlayer.setIsFrozen(false);
                currentIPlayer.setGame("none");
            }
            catch (IndexOutOfBoundsException ex) {}
        }
        //saves files
        try {
            config.save(configf);
        } 
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            language.save(languagef);
        } 
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            pd.save(playerdataf);
        } 
        catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //disables plugin
        getServer().getPluginManager().disablePlugin(this);
        sender.sendMessage(prefixMessage + pluginDisabledMessage);
    }
}
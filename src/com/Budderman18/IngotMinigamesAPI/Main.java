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
 * This class enables and disables the plugin
 * It also imports commands and handles events
 * 
 */
public class Main extends JavaPlugin implements Listener { 
    //retrive plugin instance
    private static Main plugin;
    //classes
    private final IngotPlayer IngotPlayer = new IngotPlayer(plugin);
    private static final Arena Arena = new Arena(plugin);
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
        byte maxPlayers = 0;
        //Spawn
        Spawn spawn = new Spawn(plugin);
        Spawn tempspawn = null;
        List<String> spawns = null;
        List<String> currentSpawn = null;
        String namee = null;
        double x = 0;
        double y = 0;
        double z = 0;
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
                //get world
                world = arenaData.getString("world");
                //get name
                name = arenaData.getString("name");
                //get maxPlayers
                maxPlayers = (byte) arenaData.getInt("maxPlayers");
                //create arena
                temparena = Arena.createArena(pos1, pos2, world, name, maxPlayers, false, plugin.getDataFolder() + "/Arenas/" + key);
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
    * This method creates files if needed
    * Only needed if file is missing (first usage)
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
        FileConfiguration language = FileManager.getCustomData(plugin,"language",ROOT);
        //creates files if needed
        createFiles();
        //language variables
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
        if (!(getServer().getOnlineMode())) {
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
            //get iplayerdata
            currentIPlayer = IngotPlayer.selectPlayer(key.getName(), false, null, null);
            //reset vars
            currentIPlayer.setInGame(false);
            currentIPlayer.setIsPlaying(false);
            currentIPlayer.setIsFrozen(false);
            currentIPlayer.setGame("none");
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

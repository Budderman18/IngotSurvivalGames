package com.budderman18.IngotSurvivalGames;

import com.budderman18.IngotSurvivalGames.Commands.SGAdminCommand;
import com.budderman18.IngotSurvivalGames.Commands.SGCommand;
import com.budderman18.IngotSurvivalGames.Core.ChatHandler;
import com.budderman18.IngotSurvivalGames.Core.Lobby;
import com.budderman18.IngotSurvivalGames.Core.Protection;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class enables and disables the plugin
 * It also imports commands and handles events
 */
public class main extends JavaPlugin implements Listener { 
    //globr vars
    final String ROOT = "";
    ConsoleCommandSender sender = getServer().getConsoleSender();
    //retrive plugin instance
    private static main plugin;
    
    public static main getInstance() {
        return plugin;
    }
    

    /*
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
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
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
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        //tempfile
        File tempf = new File(getDataFolder(), "temp.yml");
        if (!tempf.exists()) {
            languagef.getParentFile().mkdirs();
            saveResource("temp.yml", false);
         }
        //temp object
        FileConfiguration temp = new YamlConfiguration();
        try {
            language.load(languagef);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    } 
    /*
    *
    * Enables the plugin.
    * Checks if MC version isn't the latest.
    * If its not, warn the player about lacking support
    * Checks if server is running offline mode
    * If it is, disable the plugin
    * Also loads commands and events
    *
    */
    @Override
    public void onEnable() {
        //creates files if needed
        createFiles();
        plugin = this;
        FileManager getdata = new FileManager();
        //import files
        FileConfiguration config = getdata.getCustomData(plugin,"config",ROOT);
        FileConfiguration language = getdata.getCustomData(plugin,"language",ROOT);
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
            return;   
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
        getServer().getPluginManager().registerEvents(new Protection(),this);
        getServer().getPluginManager().registerEvents(new ChatHandler(),this);
        //enable plugin
        getServer().getPluginManager().enablePlugin(this);
        sender.sendMessage(prefixMessage + pluginEnabledMessage);
    }
    /*
    *
    * Disables the plugin.
    *
    */
    @Override
    public void onDisable() {
        plugin = this;
        FileManager getdata = new FileManager();
        //import files
        File configf = new File("plugins/IngotSurvivalGames","config.yml");
        File languagef = new File("plugins/IngotSurvivalGames","language.yml");
        File tempf = new File("plugins/IngotSurvivalGames","temp.yml");
        FileConfiguration config = getdata.getCustomData(plugin, "config", ROOT);
        FileConfiguration language = getdata.getCustomData(plugin, "language", ROOT);
        //language
        String prefixMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Prefix-Message")); 
        String pluginDisabledMessage = ChatColor.translateAlternateColorCodes('&', language.getString("Plugin-Disabled-Message")); 
        //saves files
        try {
            config.save(configf);
        } 
        catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            language.save(languagef);
        } 
        catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //deletes temporary data
        tempf.delete();
        //disables plugin
        getServer().getPluginManager().disablePlugin(this);
        sender.sendMessage(prefixMessage + pluginDisabledMessage);
    }
}

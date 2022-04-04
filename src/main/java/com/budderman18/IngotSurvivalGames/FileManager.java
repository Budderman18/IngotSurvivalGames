package com.budderman18.IngotSurvivalGames;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * This class checks for file versions and updates them if needed
 */
public class FileManager {
    //local vars
    Plugin plugin = main.getInstance();
    final String ROOT = "";
    /*
    * This method is used to read and write to a given file
    * Also handles YML loading
    */
    public YamlConfiguration getCustomData(Plugin plugin, String filename, String path) {
        //check if folder is a thing
        if (!plugin.getDataFolder().exists())
        {
            plugin.getDataFolder().mkdir();
        }
         //check if file broke somehow
        File file = new File(plugin.getDataFolder() + "/" + path, filename + ".yml");
        //load
        return YamlConfiguration.loadConfiguration(file);
    }
    //files
    File configf = new File("plugins/IngotWarn","config.yml");
    File languagef = new File("plugins/IngotWarn","language.yml");
    File tempf = new File("plugins/IngotWarn","temp.yml");
    FileConfiguration config = this.getCustomData(plugin,"config",ROOT);
    FileConfiguration language = this.getCustomData(plugin,"language",ROOT);
    FileConfiguration temp = this.getCustomData(plugin, "temp", ROOT);
    public void updateConfig() {
        
    }
    public void updateLanguage() {

    }
}

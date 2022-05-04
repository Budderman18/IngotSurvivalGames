package com.budderman18.IngotMinigamesAPI.Core.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * 
 * This class manages anything file related.
 * It can also auto-update files with new variables 
 * when the methods are configured.
 * 
 */
public class FileManager {
    /**
     * 
     * This constructor sets the class plugin to the specifed plugin.
     * This is nessecarry so files can be saved in the right place
     * 
     * @param pluginn 
     */
    public FileManager(Plugin pluginn) {
        this.plugin = pluginn;
    }
    //local vars
    private static Plugin plugin = null;
    private final String ROOT = "";
    /**
    *
    * This method is used to read and write to a given file
    * Also handles YML loading if its a yml file
    *
    * @param plugin
    * @param filename
    * @param path
    * @return 
    * FileConfiguration
    */
    public static YamlConfiguration getCustomData(Plugin plugin, String filename, String path) {
        //creat file object
        File file = null;
        //check if folder is a thing
        if (!plugin.getDataFolder().exists())
        {
            //create plugin folder
            plugin.getDataFolder().mkdir();
        }
        //check if file is a yml
        if (!filename.contains(".")) {
            file = new File(plugin.getDataFolder() + "/" + path, filename + ".yml");
        }
        //if file isn't yml, run this
        else {
            file = new File(plugin.getDataFolder() + "/" + path, filename);
        }
        //load
        return YamlConfiguration.loadConfiguration(file);
    }
    /**
    *
    * This method obtains all the loaded arena names
    *
    * @param plugin
    * @return 
    * arenas
    */
    public static List<String> getArenas(Plugin plugin) {
        File filePath = new File(plugin.getDataFolder() + "/Arenas/");
        File tempfile = null;
        FileConfiguration temp = null;
        String[] listingAllFiles = filePath.list();
        List<String> output = new ArrayList<>();
        for (String file : listingAllFiles) {
            tempfile = new File(file, "settings.yml");
            temp = FileManager.getCustomData(plugin, "settings", "/Arenas/" + file + '/');
            output.add(temp.getString("name"));
        }
        return output;
    }
}
package com.budderman18.IngotSurvivalGames.Commands;

import com.budderman18.IngotSurvivalGames.FileManager;
import com.budderman18.IngotSurvivalGames.main;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

/**
 * This class handles all the management commands staff will use
 */
public class SGAdminCommand implements TabExecutor {
    //retrive plugin instance
    Plugin plugin = main.getInstance();
    //used if the given file isnt in another folder
    final String ROOT = "";
    //imports files;
    FileManager getdata = new FileManager();
    /*
    * This method handles the SGAdmin command
    */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
    /*
    * This method handles tabcompletion when required
    */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    } 
}

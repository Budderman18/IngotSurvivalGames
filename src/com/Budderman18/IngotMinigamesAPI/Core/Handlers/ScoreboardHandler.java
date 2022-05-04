package com.budderman18.IngotMinigamesAPI.Core.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 *
 * This class handles everything involving scoreboards
 * This includes titles, lines and clearing
 * 
 */
public class ScoreboardHandler {
    //global vars
    private static final ScoreboardManager manager = Bukkit.getScoreboardManager();
    private static Scoreboard scoreboard = manager.getNewScoreboard();
    private static String title = "error";
    private static Objective objective = scoreboard.registerNewObjective("Scoreboard", "empty", title);
    /**
    *
    * This method changes the designated line of the scoreboard
    *
    * @param player
    * @param line
    * @param lineString
    */
    public static void setLine(Player player, byte line, String lineString) {
        //local vars
        Score score = null;
        //check if score does not exist
        if (objective.getScore(lineString).isScoreSet() == false) {
            //set score's line
            line = (byte) (16 - line);
            //set score
            score = objective.getScore(lineString); 
            score.setScore(line);
        }
        else {
            //get the current title
            title = objective.getDisplayName();
            //regenerate the scoreboard
            scoreboard = manager.getNewScoreboard();
            objective = scoreboard.registerNewObjective("Scoreboard", "empty", title);
            //set score's line
            line = (byte) (16 - line); 
            //set score
            score = objective.getScore(lineString);
            score.setScore(line);
        }
        //set display
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        //send scoreboard
        player.setScoreboard(scoreboard);
    }
    /** 
    *
    * This method sets the title of the scoreboard
    *
    * @param player
    * @param string
    */
    public static void setTitle(Player player, String string) {
        //set display
        objective.setDisplayName(string);
        title = string;
        //send scoreboard
        player.setScoreboard(scoreboard);
    }
    /**
    *
    * This method deletes the scoreboard of a given player
    *
    * @param player
    */
    public static void clearScoreboard(Player player) {
        //get blank scoreboard
        Scoreboard emptyScoreboard = manager.getNewScoreboard();
        //send scoreboard
        player.setScoreboard(emptyScoreboard);
    }
}
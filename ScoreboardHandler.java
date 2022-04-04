/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.budderman18.IngotSurvivalGames.Core;

import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 *
 * @author Kyle Collins
 */
public class ScoreboardHandler {
    //global vars
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard scoreboard = manager.getNewScoreboard();
    Objective objective = scoreboard.registerNewObjective("Scoreboard", "empty", "slots");
    /*
    *
    * This method sets a given line in the scoreboard
    *
    */
    public void setLine(Player player, byte line, String lineString) {
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
            String title = objective.getDisplayName();
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
    /*
    *
    * This method sets the title of the scoreboard
    *
    */
    public void setTitle(Player player, String string) {
        //set display
        objective.setDisplayName(string);
        //send scoreboard
        player.setScoreboard(scoreboard);
    }
    /*
    *
    * This method deletes the scoreboard of a given player
    *
    */
    public void clearScoreboard(Player player) {
        //get blank scoreboard
        Scoreboard emptyScoreboard = manager.getNewScoreboard();
        //send scoreboard
        player.setScoreboard(emptyScoreboard);
    }
}

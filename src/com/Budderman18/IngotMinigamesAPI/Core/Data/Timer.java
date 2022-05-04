package com.budderman18.IngotMinigamesAPI.Core.Data;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * This class handles everything around timers. 
 * Useful for determine when events should be run. 
 * 
 */
public class Timer {
    //plugin
    private static Plugin plugin = null;
    //global vars
    private long startTime = 0;
    private long endTime = 0;
    private Runnable action = null;
    private static int taskNumber = 0;
    public Timer(Plugin pluginn) {
        //set plugin
        plugin = pluginn;
    }
    /**
    *
    * This method creates a new Timer instance.
    * You can give it a name, time (in seconds), weather or not it should start running 
    * and weather or not it increases or decreases. 
    *
    * @param pluginn
    * @param startTimee
    * @param endTimee
    * @param actionn
    * @return 
    * newTimer
    */
    public static Timer startTimer(Plugin pluginn, long startTimee, long endTimee, Runnable actionn) {
        //create timer
        Timer timer = new Timer(pluginn);
        BukkitScheduler schedule = Bukkit.getServer().getScheduler();
        //setup package fields
        timer.startTime = startTimee;
        timer.endTime = endTimee;
        timer.action = actionn;
        Timer.taskNumber++;
        //create endVar
        long endVar = startTimee - endTimee;
        //absolute value of endVar
        if (endVar < 0) {
            endVar *= -1;
        }
        //start timer
        schedule.scheduleSyncDelayedTask(plugin, () -> {
            timer.endTimer(true, actionn);
        }, endVar * 20);
        return timer;
    }
    /**
    *
    * This method ends a given timer. 
    * There is no need to use this unless you want to cancel a timer early. 
    *
    * @param runActionOnEnd
    * @param action
    */
    public void endTimer(boolean runActionOnEnd, Runnable action) {
        BukkitScheduler schedule = Bukkit.getServer().getScheduler();
        schedule.cancelTasks(plugin);
        if (runActionOnEnd == true) {
            schedule.runTask(plugin,action);
        }
    }
    /**
    *
    * This method checks if the timer is currently running. 
    *
    * @return
    * isRunning
    */
    public boolean checkIfRunning() {
        BukkitScheduler schedule = Bukkit.getServer().getScheduler();
        boolean isRunning = false;
        isRunning = schedule.isCurrentlyRunning(taskNumber);
        return isRunning;
    }
}
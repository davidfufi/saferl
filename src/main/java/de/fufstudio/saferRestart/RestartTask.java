package de.fufstudio.saferRestart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartTask extends BukkitRunnable {

    private final SaferRestart plugin;
    private final Runnable actionToRun;
    private int countdownSeconds;
    private final String taskName;

    public RestartTask(SaferRestart plugin, int initialSeconds, String taskName, Runnable actionToRun) {
        this.plugin = plugin;
        this.countdownSeconds = initialSeconds;
        this.taskName = taskName;
        this.actionToRun = actionToRun;
    }

    // Diese Methode wird von /safercancel benötigt.
    public String getTaskName() {
        return this.taskName;
    }

    @Override
    public void run() {
        if (countdownSeconds == 60 || countdownSeconds == 30 || countdownSeconds == 15 || countdownSeconds == 10 || (countdownSeconds <= 5 && countdownSeconds > 0)) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "Der Server startet einen " + taskName + " in " + countdownSeconds + " Sekunden!");
        }

        if (countdownSeconds <= 0) {
            Bukkit.broadcastMessage(ChatColor.RED + "Der " + taskName + " wird jetzt ausgeführt!");
            actionToRun.run();
            plugin.setActiveRestartTask(null);
            this.cancel();
            return;
        }

        countdownSeconds--;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
    }
}
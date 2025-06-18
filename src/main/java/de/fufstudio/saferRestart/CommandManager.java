package de.fufstudio.saferRestart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandManager implements CommandExecutor {

    private final SaferRestart plugin;

    public CommandManager(SaferRestart plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("saferestart")) {
            // ... (unverändert)
            if (!sender.hasPermission("saferrestart.use")) {
                sender.sendMessage(ChatColor.RED + "Du hast keine Berechtigung für diesen Befehl.");
                return true;
            }
            if (plugin.isTaskRunning()) {
                sender.sendMessage(ChatColor.RED + "Es läuft bereits ein Countdown!");
                return true;
            }

            int duration = plugin.getConfig().getInt("timers.saferestart", 30);
            sender.sendMessage(ChatColor.GREEN + "Der Neustart-Timer wurde auf " + duration + " Sekunden gesetzt.");

            final List<String> commandsToRun = plugin.getConfig().getStringList("actions.saferestart");
            Runnable action = () -> {
                for (String cmd : commandsToRun) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            };

            RestartTask task = new RestartTask(plugin, duration, "Neustart", action);
            task.runTaskTimer(plugin, 0L, 20L);
            plugin.setActiveRestartTask(task);
            return true;
        }

        if (command.getName().equalsIgnoreCase("saferl")) {
            // ... (unverändert)
            if (!sender.hasPermission("saferrestart.reload")) {
                sender.sendMessage(ChatColor.RED + "Du hast keine Berechtigung für diesen Befehl.");
                return true;
            }
            if (plugin.isTaskRunning()) {
                sender.sendMessage(ChatColor.RED + "Es läuft bereits ein Countdown!");
                return true;
            }
            int duration = plugin.getConfig().getInt("timers.saferl", 10);
            sender.sendMessage(ChatColor.GREEN + "Der Reload-Timer wurde auf " + duration + " Sekunden gesetzt.");

            final List<String> commandsToRun = plugin.getConfig().getStringList("actions.saferl");
            Runnable action = () -> {
                for (String cmd : commandsToRun) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            };

            RestartTask task = new RestartTask(plugin, duration, "Reload", action);
            task.runTaskTimer(plugin, 0L, 20L);
            plugin.setActiveRestartTask(task);
            return true;
        }

        if (command.getName().equalsIgnoreCase("safercancel")) {
            if (!sender.hasPermission("saferrestart.cancel")) {
                sender.sendMessage(ChatColor.RED + "Du hast keine Berechtigung für diesen Befehl.");
                return true;
            }
            if (!plugin.isTaskRunning()) {
                sender.sendMessage(ChatColor.RED + "Es läuft kein Countdown, der abgebrochen werden könnte.");
                return true;
            }

            // Hier ist der Teil, der den Fehler verursacht hat
            RestartTask taskToCancel = plugin.getActiveRestartTask();
            String taskName = taskToCancel.getTaskName(); // Dies sollte jetzt funktionieren

            taskToCancel.cancel();
            plugin.setActiveRestartTask(null);

            Bukkit.broadcastMessage(ChatColor.YELLOW + "Der geplante " + taskName + " wurde von " + sender.getName() + " abgebrochen!");
            return true;
        }

        return false;
    }
}
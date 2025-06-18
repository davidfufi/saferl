package de.fufstudio.saferRestart;

import org.bukkit.plugin.java.JavaPlugin;

public final class SaferRestart extends JavaPlugin {

    private RestartTask activeRestartTask = null;

    @Override
    public void onEnable() {
        // Erstellt die config.yml, falls sie nicht existiert
        saveDefaultConfig();

        // Befehle registrieren
        CommandManager commandManager = new CommandManager(this);
        getCommand("saferestart").setExecutor(commandManager);
        getCommand("saferl").setExecutor(commandManager);
        getCommand("safercancel").setExecutor(commandManager);

        getLogger().info("SaferRestart wurde aktiviert!");
    }

    @Override
    public void onDisable() {
        // Wenn das Plugin deaktiviert wird, während ein Timer läuft, brich ihn ab.
        if (activeRestartTask != null) {
            activeRestartTask.cancel();
        }
        getLogger().info("SaferRestart wurde deaktiviert.");
    }

    // Methoden, um den aktiven Task zu verwalten
    public boolean isTaskRunning() {
        return activeRestartTask != null;
    }

    public void setActiveRestartTask(RestartTask task) {
        this.activeRestartTask = task;
    }

    public RestartTask getActiveRestartTask() {
        return activeRestartTask;
    }
}
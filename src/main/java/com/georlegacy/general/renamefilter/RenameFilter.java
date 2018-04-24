package com.georlegacy.general.renamefilter;

import com.georlegacy.general.renamefilter.commands.RFCommand;
import com.georlegacy.general.renamefilter.events.RenameListener;
import com.georlegacy.general.renamefilter.tasks.DictionaryUpdate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.Timer;

public class RenameFilter extends JavaPlugin {
    private ConfigHandler configHandler = new ConfigHandler(this);
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public GitHub gitHub;

    private void loadGithub() {
        try {
            gitHub = GitHub.connectAnonymously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateDictionary() {
        new DictionaryUpdate(this).runTaskTimerAsynchronously(this, 0l, 1000*60*60*12);
    }

    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new RenameListener(this), this);
        this.getCommand("rf").setExecutor(new RFCommand(this));
        getConfigHandler().load();
        this.loadGithub();
        updateDictionary();
    }

    @Override
    public void onDisable() {

    }
}

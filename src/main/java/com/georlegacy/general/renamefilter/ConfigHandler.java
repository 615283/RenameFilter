package com.georlegacy.general.renamefilter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

public class ConfigHandler {
    private RenameFilter rf;
    public ConfigHandler(RenameFilter rf) {
        this.rf = rf;
    }

    public static final String renameFilterPrefix = "&e[&4RenameFilter&e] ";

    public void load() {
        new File(rf.getDataFolder() + File.separator + "dictionaries").mkdirs();
        if (!new File(rf.getDataFolder() + File.separator + "config.yml").exists())
            rf.saveResource("config.yml", true);
    }

    public String cancelMsg() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(rf.getDataFolder() + File.separator + "config.yml"));
        return config.getString("rename-cancel-msg");
    }

    public String bypassMsg() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(rf.getDataFolder() + File.separator + "config.yml"));
        return config.getString("rename-bypass-msg");
    }

    private List<File> dictionaries() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(rf.getDataFolder() + File.separator + "config.yml"));
        List<String> dictionaries = config.getStringList("used-dictionaries");
        List<File> files = new ArrayList<File>();
        for (String s : dictionaries) {
            File f = new File(rf.getDataFolder() + File.separator + "dictionaries" + File.separator + s);
            if (f.exists()) {
                files.add(f);
            } else {
                rf.getLogger().warning("The dictionary " + s + " couldn't be loaded.");
            }
        }
        return files;
    }

    public List<String> getBannedWords() {
        List<String> words = new ArrayList<String>();
        for (File f : dictionaries()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String word;
                while ((word=br.readLine())!=null) {
                    words.add(word);
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return words;
    }
}

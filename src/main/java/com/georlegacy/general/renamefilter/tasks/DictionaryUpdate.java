package com.georlegacy.general.renamefilter.tasks;

import com.georlegacy.general.renamefilter.RenameFilter;
import org.bukkit.scheduler.BukkitRunnable;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class DictionaryUpdate extends BukkitRunnable {
    private RenameFilter rf;
    public DictionaryUpdate(RenameFilter rf) {
        this.rf = rf;
    }

    @Override
    public void run() {
        rf.getLogger().info("Checking words repository for updates...");
        try {
            GHRepository wordsrepo = rf.gitHub.getUser("615283").getRepository("Banned-Words");
            List<GHContent> wordfiles = wordsrepo.getDirectoryContent("dictionaries");
            for (GHContent wordfile : wordfiles) {
                long size = wordfile.getSize();
                String filename = wordfile.getName();
                File local = new File(rf.getDataFolder() + File.separator + "dictionaries" + File.separator + filename);
                if (!local.exists()) {
                    Files.copy(wordfile.read(), Paths.get(rf.getDataFolder() + File.separator + "dictionaries" + File.separator + wordfile.getName()), StandardCopyOption.REPLACE_EXISTING);
                    rf.getLogger().info("Copied " + filename + " successfully.");
                    continue;
                }
                long localsize = local.length();
                if (size==localsize) continue;
                Files.copy(wordfile.read(), Paths.get(rf.getDataFolder() + File.separator + "dictionaries" + File.separator + wordfile.getName()), StandardCopyOption.REPLACE_EXISTING);
                rf.getLogger().info("Updated " + filename + " successfully.");
            }
        } catch (IOException e) {
            rf.getLogger().severe("An exception occurred whilst attempting to find updates.");
            e.printStackTrace();
        }
    }

}

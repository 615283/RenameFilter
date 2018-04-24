package com.georlegacy.general.renamefilter.tasks;

import com.georlegacy.general.renamefilter.RenameFilter;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.TimerTask;

public class DictionaryUpdate extends TimerTask {
    private RenameFilter rf;
    public DictionaryUpdate(RenameFilter rf) {
        this.rf = rf;
    }

    @Override
    public void run() {
        rf.getLogger().info("Checking words repository for updates...");
        try {
            System.out.print(rf.gitHub);
            GHRepository wordsrepo = rf.gitHub.getUser("615283").getRepository("Banned-Words");
            List<GHContent> wordfiles = wordsrepo.getDirectoryContent("dictionaries");
            for (GHContent wordfile : wordfiles) {
                long size = wordfile.getSize();
                String filename = wordfile.getName();
                File local = new File(rf.getDataFolder() + File.separator + "dictionaries" + File.separator + filename);
                if (!local.exists()) {
                    Files.copy(wordfile.read(), local.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    rf.getLogger().info("Copied " + filename + "successfully.");
                    return;
                }
                long localsize = local.length();
                if (size==localsize) return;
                Files.copy(wordfile.read(), local.toPath(), StandardCopyOption.REPLACE_EXISTING);
                rf.getLogger().info("Updated " + filename + "successfully.");
            }
        } catch (IOException e) {
            rf.getLogger().severe("An exception occurred whilst attempting to find updates.");
            e.printStackTrace();
        }
    }

}

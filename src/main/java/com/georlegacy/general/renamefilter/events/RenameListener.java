package com.georlegacy.general.renamefilter.events;

import java.util.List;

import com.google.common.base.CharMatcher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.georlegacy.general.renamefilter.ConfigHandler;
import com.georlegacy.general.renamefilter.RenameFilter;

public class RenameListener implements Listener {
    private RenameFilter plugin;
    public RenameListener(RenameFilter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemRename(InventoryClickEvent e) {
        Player clicker = (Player) e.getWhoClicked();
        if (e.getCurrentItem()==null||e.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }
        if (e.getInventory().getType()==InventoryType.ANVIL) {
            int rawSlot = e.getRawSlot();
            if (rawSlot==2) {
                ItemStack item = e.getCurrentItem();
                if (item != null) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        if (meta.hasDisplayName()) {
                            String displayName = meta.getDisplayName();
                            List<String> list = plugin.getConfigHandler().getBannedWords();
                            for (String word : list) {
                                String convertedDisplayName = displayName
                                        .replace("3", "e")
                                        .replace("5", "s")
                                        .replace("4", "a")
                                        .replace("8", "b")
                                        .replace("1", "i")
                                        .replace("|", "i");
                                if (convertedDisplayName.toLowerCase().contains(word.toLowerCase())) {
                                    if (clicker.hasPermission("renamefilter.bypass")) {
                                        String bypassmsg = plugin.getConfigHandler().bypassMsg();
                                        clicker.playSound(clicker.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0);
                                        clicker.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigHandler.renameFilterPrefix + bypassmsg));
                                        return;
                                    }
                                    else {
                                        String failmsg = plugin.getConfigHandler().cancelMsg();
                                        e.setCancelled(true);
                                        clicker.giveExpLevels(0);
                                        clicker.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigHandler.renameFilterPrefix + failmsg));
                                        clicker.playSound(clicker.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 0);
                                    }
                                } else if ((!CharMatcher.ascii().matchesAllOf(displayName)) && plugin.getConfigHandler().banUnicode()) {
                                    if (clicker.hasPermission("renamefilter.bypass")) {
                                        String bypassmsg = plugin.getConfigHandler().bypassMsg();
                                        clicker.playSound(clicker.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0);
                                        clicker.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigHandler.renameFilterPrefix + bypassmsg));
                                        return;
                                    }
                                    else {
                                        String unicodemsg = plugin.getConfigHandler().unicodeMsg();
                                        e.setCancelled(true);
                                        clicker.giveExpLevels(0);
                                        clicker.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigHandler.renameFilterPrefix + unicodemsg));
                                        clicker.playSound(clicker.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 0);
                                    }
                                }
                            }
                            return;
                        }
                    }
                }
            }
            else {
                return;
            }
            return;
        }
    }
}

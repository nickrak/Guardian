package org.guardian.listeners;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;
import org.guardian.Config;
import org.guardian.Guardian;

public class HighPlayerListener extends PlayerListener {

    private Guardian plugin;

    public HighPlayerListener(final Guardian plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, this, Priority.High, plugin);
        Bukkit.getServer().getPluginManager().registerEvent(Type.PLAYER_COMMAND_PREPROCESS, this, Priority.High, plugin);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (Config.ninja && !event.getPlayer().hasPermission("guardian.see")) {
            Player player = event.getPlayer();
            String[] split = event.getMessage().split("\\s+");
            String command = split[0].substring(1);
            String args = split.length >= 2 ? split[1] : "";

            if ((command.equalsIgnoreCase("plugins") || command.equalsIgnoreCase("pl"))) {
                event.setCancelled(true);
                String message = "Plugins: ";
                List<String> output = new ArrayList<String>();
                for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
                    String name = pl.getDescription().getName();
                    if (!name.equalsIgnoreCase("Guardian")) {
                        output.add((pl.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + name);
                    }
                }
                for (String o : output) {
                    message += o + ChatColor.WHITE + ", ";
                }
                player.sendMessage(message.substring(0, message.length() - 2));
            } else if ((command.equalsIgnoreCase("version") || command.equalsIgnoreCase("ver")
                    || command.equalsIgnoreCase("icanhasbukkit")) && args.equals("Guardian")) {
                event.setCancelled(true);
                player.sendMessage("This server is not running any plugin by that name.");
                player.sendMessage("Use /plugins to get a list of plugins.");
            } else if (command.equalsIgnoreCase("guardian") || command.equalsIgnoreCase("gd")) {
                event.setCancelled(true);
                player.sendMessage("Unknown command. Type \"help\" for help.");
            }
        }
    }
}

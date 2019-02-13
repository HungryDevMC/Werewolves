package org.pixelgalaxy;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.pixelgalaxy.commands.GameCommands;
import org.pixelgalaxy.events.ColorChooserEvent;
import org.pixelgalaxy.events.LobbyJoinLeave;
import org.pixelgalaxy.events.disableInventoryInteract;
import org.pixelgalaxy.game.Team;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class WerewolfMain extends JavaPlugin {

    public static WerewolfMain plugin;
    public static FileConfiguration config;
    public static final String PREFIX = "§8§l[§4§lWereWolves§8§l] §a";

    // Give player random names and skins
    // Skip command for day

    @Override
    public void onEnable(){
        plugin = this;
        config = this.getConfig();

        File configFile = new File(getDataFolder() + "/config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }

        registerListeners(Arrays.asList(

                new LobbyJoinLeave(),
                new ColorChooserEvent(),
                new disableInventoryInteract()

        ));

        registerCommands(Arrays.asList(

                "game"

        ), Arrays.asList(

                new GameCommands()

        ));

        File images = new File(getDataFolder() + "/map_images/");
        if (!images.exists()) {
            images.mkdirs();
        }

        Team.loadAll();

    }

    @Override
    public void onDisable(){

        plugin = null;

    }

    private void registerListeners(List<Listener> listeners) {

        listeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));

    }

    private void registerCommands(List<String> commands, List<CommandExecutor> commandExecutors) {

        for (int i = 0; i < commands.size(); i++) {
            Bukkit.getLogger().info(commands.get(i));
            Bukkit.getLogger().info(commandExecutors.get(i).toString());

            String command = commands.get(i);
            CommandExecutor ce = commandExecutors.get(i);

            getCommand(command).setExecutor(ce);
        }

    }


}

package org.pixelgalaxy.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.pixelgalaxy.WerewolfMain;

/**
 * Created by robin on 25/08/2018.
 */
public class ConfigSavers {

    static WerewolfMain plugin = WerewolfMain.plugin;

    public static Location getLocation(String Path)
    {
        try
        {
            return new Location(Bukkit.getWorld(plugin.getConfig().getString(Path + ".W")), plugin.getConfig().getInt(Path + ".X"), plugin
                    .getConfig().getInt(Path + ".Y"), plugin.getConfig().getInt(Path + ".Z"), plugin.getConfig().getInt(".Yaw"), plugin
                    .getConfig().getInt(".Pitch"));
        }
        catch (Exception localException) {}
        return null;
    }

    public static Location getLocationWithout(String Path)
    {
        try
        {
            return new Location(Bukkit.getWorld(plugin.getConfig().getString(Path + ".W")), plugin.getConfig().getDouble(Path + ".X"), plugin
                    .getConfig().getDouble(Path + ".Y"), plugin.getConfig().getDouble(Path + ".Z"));
        }
        catch (Exception localException) {}
        return null;
    }

    public static void saveLocation(String Path, Location location)
    {

        plugin.getConfig().set(Path + ".X", Integer.valueOf((int)location.getX()));
        plugin.getConfig().set(Path + ".Y", Integer.valueOf((int)location.getY()));
        plugin.getConfig().set(Path + ".Z", Integer.valueOf((int)location.getZ()));
        plugin.getConfig().set(Path + ".W", location.getWorld().getName().toString());
        plugin.getConfig().set(Path + ".Yaw", Integer.valueOf((int)location.getYaw()));
        plugin.getConfig().set(Path + ".Pitch", Integer.valueOf((int)location.getPitch()));
        plugin.saveConfig();
    }

}

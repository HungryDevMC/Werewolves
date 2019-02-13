package org.pixelgalaxy.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.Lobby;
import org.pixelgalaxy.timers.GameDayNightTimer;

public class GameCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player){

            Player p = (Player) sender;

            if(label.equalsIgnoreCase("game")){

                switch(args.length){

                    case 1:

                        if(args[0].equalsIgnoreCase("setspawn")){
                            WerewolfMain.config.set("lobby_location", p.getLocation());
                            Lobby.setSpawnLoc(p.getLocation());
                            p.sendMessage(WerewolfMain.PREFIX + "You have set the spawn location to your current location.");
                        }else if(args[0].equalsIgnoreCase("skipday")){

                            GameDayNightTimer.currentGameTimer.cancel();
                            GameDayNightTimer timer = new GameDayNightTimer();
                            timer.runTaskTimer(WerewolfMain.plugin, 0, 20);

                        }

                        break;

                    case 2:

                        if(args[0].equalsIgnoreCase("setspawn")){

                            WerewolfMain.config.set("teams." + args[1].toLowerCase() + ".location", p.getLocation());
                            p.sendMessage(WerewolfMain.PREFIX + "You have set color " + args[1].toLowerCase() + "'s location to your current location.");

                        }

                        break;

                    default:

                        p.sendMessage(WerewolfMain.PREFIX + "§cUnknown arguments for the command /game, use /game help for more info.");

                        break;

                }

            }

        }

        return false;
    }
}

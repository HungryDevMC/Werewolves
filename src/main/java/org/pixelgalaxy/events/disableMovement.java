package org.pixelgalaxy.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.pixelgalaxy.game.GamePlayer;
import org.pixelgalaxy.timers.GameDayNightTimer;
import org.pixelgalaxy.timers.Time;

public class disableMovement implements Listener {

    @Getter @Setter
    private static GamePlayer gallowPlayer;

    @EventHandler
    public void onMove(PlayerMoveEvent e){

        if((GameDayNightTimer.getCurrentTime() != null && GameDayNightTimer.getCurrentTime().equals(Time.NIGHT)) || (gallowPlayer != null && e.getPlayer().equals(gallowPlayer.getPlayer()))){
            e.setCancelled(true);
        }

    }

}

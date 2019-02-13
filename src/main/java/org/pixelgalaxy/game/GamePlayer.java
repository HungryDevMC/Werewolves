package org.pixelgalaxy.game;

import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.Player;
import org.pixelgalaxy.game.roles.Role;

/**
 * Object to save player role and team
 */

@Data
@Builder
public class GamePlayer {

    private Team playerTeam;
    private Role playerRole;
    private Player player;

    public String getCustomName(){
        return getPlayer().getCustomName();
    }

}

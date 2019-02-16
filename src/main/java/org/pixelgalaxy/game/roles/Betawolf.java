package org.pixelgalaxy.game.roles;


import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.GamePlayer;

import java.util.List;

public class Betawolf extends Role {

    @Getter @Setter private static boolean isAlive = false;

    public Betawolf(){
        super(WerewolfMain.config.getString("role_info.betawolf.name"), RoleTeam.WOLVES, 1, WerewolfMain.config.getBoolean("role_info.betawolf.primary"), 0,
                true);
        setAlive(true);
    }

}

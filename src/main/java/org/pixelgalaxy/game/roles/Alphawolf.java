package org.pixelgalaxy.game.roles;

import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.GamePlayer;

import java.util.List;

public class Alphawolf extends Role {

    @Getter @Setter private static boolean isAlive = false;

    public Alphawolf(){
        super(WerewolfMain.config.getString("role_info.alphawolf.name"), RoleTeam.WOLVES, 1, WerewolfMain.config.getBoolean("role_info.alphawolf.primary"), 0,
                true);
        setAlive(true);
    }

}

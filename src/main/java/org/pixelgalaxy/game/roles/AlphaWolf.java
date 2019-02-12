package org.pixelgalaxy.game.roles;

import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;

public class AlphaWolf extends Role implements NightRole {

    @Getter @Setter private static boolean isAlive = false;

    public AlphaWolf(){
        super(WerewolfMain.config.getString("role_info.alpha.name"), RoleTeam.WOLVES, 1);
        setAlive(true);
    }

    @Override
    public void playNightRole() {

    }
}

package org.pixelgalaxy.game.roles;


import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;

public class Betawolf extends Role implements NightRole {

    @Getter @Setter private static boolean isAlive = false;

    public Betawolf(){
        super(WerewolfMain.config.getString("role_info.beta.name"), RoleTeam.WOLVES, 1);
        setAlive(true);
    }

    @Override
    public void playNightRole() {

    }
}

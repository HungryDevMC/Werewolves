package org.pixelgalaxy.game.roles;


import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;

public class Omegawolf extends Role implements NightRole {

    @Getter @Setter private static boolean isAlive = false;

    public Omegawolf(){
        super(WerewolfMain.config.getString("role_info.omega.name"), RoleTeam.WOLVES, 1);
        setAlive(true);
    }

    @Override
    public void playNightRole() {

    }
}

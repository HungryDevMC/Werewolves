package org.pixelgalaxy.game.roles;

import org.pixelgalaxy.WerewolfMain;

public class Murderer extends Role implements NightRole {

    public Murderer(){
        super(WerewolfMain.config.getString("role_info.murderer.name"), RoleTeam.MURDERER, 1);
    }

    @Override
    public void playNightRole() {

    }
}

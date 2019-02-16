package org.pixelgalaxy.game.roles;

import org.pixelgalaxy.WerewolfMain;

public class Murderer extends Role {

    public Murderer(){
        super(WerewolfMain.config.getString("role_info.murderer.name"), RoleTeam.MURDERER, 1, WerewolfMain.config.getBoolean("role_info.murderer.primary"), 0,
                true);
    }

}

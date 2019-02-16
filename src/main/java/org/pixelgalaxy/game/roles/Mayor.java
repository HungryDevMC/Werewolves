package org.pixelgalaxy.game.roles;

import org.pixelgalaxy.WerewolfMain;

public class Mayor extends Role {

    public Mayor(){
        super(WerewolfMain.config.getString("role_info.mayor.name"), RoleTeam.CITIZENS, WerewolfMain.config.getInt("role_info.mayor.max"), WerewolfMain.config.getBoolean("role_info.mayor.primary"), 4,
                true);
    }

}

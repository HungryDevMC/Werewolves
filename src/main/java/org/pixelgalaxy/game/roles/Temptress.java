package org.pixelgalaxy.game.roles;

import org.pixelgalaxy.WerewolfMain;

public class Temptress extends Role {

    public Temptress(){
        super(WerewolfMain.config.getString("role_info.temptress.name"), RoleTeam.CITIZENS, WerewolfMain.config.getInt("role_info.temptress.max"), WerewolfMain.config.getBoolean("role_info.temptress.primary"), 5,
                true);
    }

}

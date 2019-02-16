package org.pixelgalaxy.game.roles;

import org.pixelgalaxy.WerewolfMain;

public class Madman extends Role {

    public Madman(){
        super(WerewolfMain.config.getString("role_info.madman.name"), WerewolfMain.config.getInt("role_info.madman.max"), WerewolfMain.config.getBoolean("role_info.madman.primary"),
                false);
    }

}

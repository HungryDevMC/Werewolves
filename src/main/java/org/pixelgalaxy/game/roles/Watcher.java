package org.pixelgalaxy.game.roles;

import org.pixelgalaxy.WerewolfMain;

public class Watcher extends Role {

    public Watcher(){
        super(WerewolfMain.config.getString("role_info.watcher.name"), RoleTeam.CITIZENS, WerewolfMain.config.getInt("role_info.watcher.max"), WerewolfMain.config.getBoolean("role_info.watcher.primary"), 6,
                true);
    }

}

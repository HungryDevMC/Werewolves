package org.pixelgalaxy.game.roles;

import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;

public class Hero extends Role {

    @Getter @Setter private int uses = WerewolfMain.config.getInt("role_info.hero.uses");

    public Hero(){
        super(WerewolfMain.config.getString("role_info.hero.name"), RoleTeam.CITIZENS, WerewolfMain.config.getInt("role_info.hero.max"), WerewolfMain.config.getBoolean("role_info.hero.primary"), 2,
                true);
    }

}

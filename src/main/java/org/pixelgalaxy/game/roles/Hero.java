package org.pixelgalaxy.game.roles;

import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;

public class Hero extends Role implements NightRole {

    @Getter @Setter private static int maxUses = WerewolfMain.config.getInt("role_info.hero.uses");

    public Hero(){
        super(WerewolfMain.config.getString("role_info.hero.name"), RoleTeam.CITIZENS, WerewolfMain.config.getInt("role_info.hero.max"));
    }

    @Override
    public void playNightRole() {

    }


}

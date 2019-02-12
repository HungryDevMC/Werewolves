package org.pixelgalaxy.game.roles;

import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;

public class Protector extends Role implements NightRole {

    @Getter
    @Setter  private static int uses = WerewolfMain.config.getInt("role_info.protector.uses");

    public Protector(){
        super(WerewolfMain.config.getString("role_info.protector.name"), RoleTeam.CITIZENS, WerewolfMain.config.getInt("role_info.protector.max"));
    }

    @Override
    public void playNightRole() {

    }
}

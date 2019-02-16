package org.pixelgalaxy.game.roles;

import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;

public class Protector extends Role {

    @Getter @Setter private int uses = WerewolfMain.config.getInt("role_info.protector.uses");

    public Protector(){
        super(WerewolfMain.config.getString("role_info.protector.name"), RoleTeam.CITIZENS, WerewolfMain.config.getInt("role_info.protector.max"), WerewolfMain.config.getBoolean("role_info.protector.primary"), 3, true);
    }

}

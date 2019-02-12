package org.pixelgalaxy.game.roles;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
abstract class Role {

    private String roleName;
    private RoleTeam roleTeam;
    private int maxInGame;

    public Role(String roleName, int maxInGame){
        setRoleName(roleName);
    }

}

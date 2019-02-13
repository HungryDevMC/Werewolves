package org.pixelgalaxy.game.roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public abstract class Role {

    private String roleName;
    private RoleTeam roleTeam;
    private int maxInGame;

    @Getter private static List<Role> availableRoles = new ArrayList<>();

    public Role(String roleName, int maxInGame){
        setRoleName(roleName);
    }

    public static void loadAll(){



    }

    public static Role getRandom(){



    }

}

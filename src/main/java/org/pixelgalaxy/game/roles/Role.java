package org.pixelgalaxy.game.roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.pixelgalaxy.WerewolfMain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @SneakyThrows({ClassNotFoundException.class, InstantiationException.class, IllegalAccessException.class})
    public static void loadAll(){

        for(String key : WerewolfMain.config.getKeys(true)){

            if(key.matches("role_info.[a-zA-Z]$")){

                for(int i = 0; i<WerewolfMain.config.getInt(key + ".max"); i++){

                    Class roleClass = Class.forName("org.pixelgalaxy.game.roles." + StringUtils.capitalize(WerewolfMain.config.getString(StringUtils.substring(key, key.indexOf("role_info.") + 1, key.length()))));
                    Role role = (Role) roleClass.newInstance();

                    if(role instanceof Role) {
                        availableRoles.add(role);

                    }

                }

            }

        }

    }

    public static Role getRandom(){

        Random random = new Random();
        int rRoleIndex = random.nextInt(getAvailableRoles().size());
        Role role = availableRoles.get(rRoleIndex);
        availableRoles.remove(role);
        return role;
    }

}

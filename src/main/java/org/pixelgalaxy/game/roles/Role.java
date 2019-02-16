package org.pixelgalaxy.game.roles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
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
    private boolean primary;
    private int nightPriority;
    private boolean nightRole;

    @Getter private static List<Role> availableRoles = new ArrayList<>();

    public Role(String roleName, int maxInGame, boolean primary, boolean nightRole){
        setRoleName(roleName);
        setMaxInGame(maxInGame);
        setPrimary(primary);
        setNightRole(nightRole);
    }

    public Role(String roleName, RoleTeam roleTeam, int maxInGame, boolean primary, boolean nightRole){
        this(roleName, maxInGame, primary, nightRole);
        setRoleTeam(roleTeam);
    }

    @SneakyThrows({ClassNotFoundException.class, InstantiationException.class, IllegalAccessException.class})
    public static void loadAll(){

        for(String key : WerewolfMain.config.getKeys(true)){

            if(key.matches("role_info.[a-zA-Z]+$")){

                for(int i = 0; i<WerewolfMain.config.getInt(key + ".max"); i++){

                    Class roleClass = Class.forName("org.pixelgalaxy.game.roles." + StringUtils.capitalize(StringUtils.substring(key, key.indexOf(".") + 1, key.length())));
                    Role role = (Role) roleClass.newInstance();

                    if(role != null) {
                        availableRoles.add(role);

                        Bukkit.getLogger().info(role.getRoleName() + ": " + role.isPrimary());

                    }

                }

            }

        }

    }

    private static List<Role> getPrimaryRoles(){

        List<Role> primaries = new ArrayList<>();
        for(Role r : getAvailableRoles()){
            if(r.isPrimary()){
                primaries.add(r);
            }
        }
        return primaries;
    }

    public static Role getRandom(){

        Random random = new Random();
        int rRoleIndex;
        Role role;
        if(getPrimaryRoles().size() > 0){
            rRoleIndex = random.nextInt(getPrimaryRoles().size());
            role = getPrimaryRoles().get(rRoleIndex);
        }else{
            rRoleIndex = random.nextInt(getAvailableRoles().size());
            role = getAvailableRoles().get(rRoleIndex);
        }
        getAvailableRoles().remove(role);
        return role;
    }

    public boolean isBad(){
        return (this.getRoleTeam().equals(RoleTeam.WOLVES) || this.getRoleTeam().equals(RoleTeam.MURDERER));
    }
}

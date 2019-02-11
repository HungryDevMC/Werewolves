package org.pixelgalaxy.game;

import org.pixelgalaxy.WerewolfMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Role {

    CITIZEN(WerewolfMain.config.getString("role_info.citizen.name"), WerewolfMain.config.getInt("role_info.citizen.max"), 1),
    MAYOR(WerewolfMain.config.getString("role_info.mayor.name"), WerewolfMain.config.getInt("role_info.mayor.max"), 1, true),
    PROTECTOR(WerewolfMain.config.getString("role_info.protector.name"), WerewolfMain.config.getInt("role_info.protector.max"), 1),
    TEMPTRESS(WerewolfMain.config.getString("role_info.temptress.name"), WerewolfMain.config.getInt("role_info.temptress.max"), 1),
    HERO(WerewolfMain.config.getString("role_info.hero.name"), WerewolfMain.config.getInt("role_info.hero.max"), 1),
    WATCHER(WerewolfMain.config.getString("role_info.watcher.name"), WerewolfMain.config.getInt("role_info.watcher.max"), 1),
    ALPHA_WOLF(WerewolfMain.config.getString("role_info.alpha.name"), WerewolfMain.config.getInt("role_info.alpha.max"), 2, true),
    BETA_WOLF(WerewolfMain.config.getString("role_info.beta.name"), WerewolfMain.config.getInt("role_info.beta.max"), 2, true),
    OMEGA_WOLF(WerewolfMain.config.getString("role_info.omega.name"), WerewolfMain.config.getInt("role_info.omega.max"), 2),
    MURDERER(WerewolfMain.config.getString("role_info.murderer.name"), WerewolfMain.config.getInt("role_info.murderer.max"), 3),
    MADMAN(WerewolfMain.config.getString("role_info.madman.name"), WerewolfMain.config.getInt("role_info.madman.max"), 4);

    Role(String roleName, int maxInGame, int team, boolean priorityRole){
        this.roleName = roleName;
        this.maxInGame = maxInGame;
        this.priorityRole = priorityRole;
        this.team = team;
    }

    Role(String roleName, int maxInGame, int team){
        this(roleName, maxInGame, team, false);
    }

    private boolean priorityRole;
    private String roleName;
    private int maxInGame;
    private int team;

    public static final Map<Integer, String> ROLE_TEAM_NAMES = new HashMap<Integer, String>() {{

        put(1, "Citizens");
        put(2, "Wolves");
        put(3, "Murderer");

    }};

    public String getRoleName() {
        return roleName;
    }

    public boolean isPriorityRole() {
        return priorityRole;
    }

    public static List<Role> getSecondaryRoles(){

        List<Role> secondaryRoles = new ArrayList<>();
        for(Role role : Role.values()){
            if(!role.isPriorityRole()){
                for(int i = 0; i<role.getMaxInGame(); i++){
                    secondaryRoles.add(role);
                }
            }
        }
        return secondaryRoles;
    }

    public static List<Role> getPrimaryRoles(){

        List<Role> primaryRoles = new ArrayList<>();
        for(Role role : Role.values()){
            if(role.isPriorityRole()){
                for(int i = 0; i<role.getMaxInGame(); i++) {
                    primaryRoles.add(role);
                }
            }
        }
        return primaryRoles;
    }

    public int getTeam() {
        return team;
    }

    public int getMaxInGame() {
        return maxInGame;
    }
}

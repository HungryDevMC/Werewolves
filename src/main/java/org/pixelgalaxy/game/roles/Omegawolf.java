package org.pixelgalaxy.game.roles;


import lombok.Getter;
import lombok.Setter;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.Game;
import org.pixelgalaxy.game.GamePlayer;

public class Omegawolf extends Role {

    @Getter @Setter private static boolean isAlive = false;

    public Omegawolf(){
        super(WerewolfMain.config.getString("role_info.omegawolf.name"), RoleTeam.WOLVES, 1, WerewolfMain.config.getBoolean("role_info.omegawolf.primary"), 1,
                true);
        setAlive(true);
    }

    public static GamePlayer getTarget(){

        for(GamePlayer gp : Game.getTargetMap().keySet()){
            if(gp.getPlayerRole() instanceof Omegawolf){

                return Game.getTargetMap().get(gp);
            }
        }
        return null;
    }

}

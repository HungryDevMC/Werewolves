package org.pixelgalaxy.utils;

import org.pixelgalaxy.game.GamePlayer;

import java.util.Comparator;

public class RolePrioritySorter implements Comparator<GamePlayer> {

    @Override
    public int compare(GamePlayer o1, GamePlayer o2) {

        int priority1 = o1.getPlayerRole().getNightPriority();
        int priority2 = o2.getPlayerRole().getNightPriority();

        if(priority1 > priority2){
            return -1;
        }else if(priority1 < priority2){
            return 1;
        }
            return 0;
    }
}

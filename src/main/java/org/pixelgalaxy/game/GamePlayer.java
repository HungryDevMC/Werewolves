package org.pixelgalaxy.game;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GamePlayer {

    private Team playerTeam;
    private Role playerRole;

}

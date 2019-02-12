package org.pixelgalaxy.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Color;

@AllArgsConstructor
public enum Team {

    ORANGE(Color.ORANGE, 1),
    PURPLE(Color.PURPLE, 10),
    AQUA(Color.AQUA, 3),
    GREEN(Color.LIME, 5),
    WHITE(Color.WHITE, 0),
    RED(Color.RED, 14),
    BLUE(Color.BLUE, 11),
    YELLOW(Color.YELLOW, 4),
    PINK(Color.fromRGB(172, 141, 237), 6),
    BROWN(Color.fromBGR(40,71,114), 12),
    BLACK(Color.BLACK, 15),
    GRAY(Color.GRAY, 8);

    @Getter private Color teamColor;
    @Getter private int woolColor;

}

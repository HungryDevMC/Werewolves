package org.pixelgalaxy.timers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Time {

    DAY("Day"),
    NIGHT("Night");

    @Getter private String name;

}

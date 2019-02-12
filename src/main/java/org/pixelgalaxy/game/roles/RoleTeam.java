package org.pixelgalaxy.game.roles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RoleTeam {

    WOLVES("Wolves"),
    CITIZENS("Citizens"),
    MURDERER("Murderer");

    @Getter private String teamName;

}

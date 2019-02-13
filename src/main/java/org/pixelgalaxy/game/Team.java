package org.pixelgalaxy.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Color;
import org.pixelgalaxy.WerewolfMain;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@AllArgsConstructor
@Builder
public class Team {

    @Getter private String colorName;
    @Getter @Setter private Color teamColor;
    @Getter @Setter private int woolColor;

    @Getter private static Set<Team> availableTeams = new HashSet<>();

    public static void loadAll(){

        for(String key : WerewolfMain.config.getKeys(true)){

            if(key.matches("teams.[a-zA-Z]$")){

                String rgb = WerewolfMain.config.getString(key + ".rgb");
                availableTeams.add(Team.builder().woolColor(WerewolfMain.config.getInt(key + ".wool_color")).teamColor(Color.fromRGB(

                    Integer.valueOf(StringUtils.substring(rgb, 0, rgb.indexOf(":"))),
                    Integer.valueOf(StringUtils.substring(rgb, rgb.indexOf(":") + 1, rgb.lastIndexOf(":"))),
                    Integer.valueOf(StringUtils.substring(rgb, rgb.lastIndexOf(":") + 1, rgb.length()))

                )).colorName(StringUtils.substring(key, key.indexOf("teams.") + 1, key.indexOf("."))).build());

            }

        }

    }

    public void setColorName(String colorName) {
        this.colorName = StringUtils.capitalize(colorName);
    }

    public static Team getRandom(){

        Random random = new Random();
        int rColorIndex = random.nextInt(availableTeams.size());
        return (Team)availableTeams.toArray()[rColorIndex];
    }

}

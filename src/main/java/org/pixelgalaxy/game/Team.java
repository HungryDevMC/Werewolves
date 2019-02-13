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

    /**
     * Creates all the Team objects that are put in the config.yml
     */

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

    /**
     *
     * @param colorName the colorname in the config
     */

    public void setColorName(String colorName) {
        this.colorName = StringUtils.capitalize(colorName);
    }

    /**
     *
     * @return random team from the available teams list
     */

    public static Team getRandom(){

        Random random = new Random();
        int rColorIndex = random.nextInt(availableTeams.size());
        Team team = (Team)availableTeams.toArray()[rColorIndex];
        availableTeams.remove(team);
        return team;
    }

}

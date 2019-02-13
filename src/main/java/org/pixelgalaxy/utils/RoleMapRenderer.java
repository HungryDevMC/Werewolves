package org.pixelgalaxy.utils;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.game.roles.Role;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoleMapRenderer extends MapRenderer {

    private String imageName;
    private static List<Player> hasRendered = new ArrayList<>();

    public RoleMapRenderer(Role role){
        this.imageName = role.toString().toLowerCase();
    }

    /**
     * Creates a custom render to display image for each role on players their maps in inventory
     * @param mapView mapView of map in inv
     * @param mapCanvas Canvas of map in inv
     * @param player player that holds the map
     */

    @SneakyThrows(IOException.class)
    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if(!hasRendered.contains(player)) {
            BufferedImage img = ImageIO.read(new File(WerewolfMain.plugin.getDataFolder() + "/map_images/" + imageName + ".png"));

            mapView.setScale(MapView.Scale.FARTHEST);
            mapCanvas.drawImage(0, 0, img);
            hasRendered.add(player);
        }
    }

}

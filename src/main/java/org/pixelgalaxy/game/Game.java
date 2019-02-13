package org.pixelgalaxy.game;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_13_R1.Items;
import net.minecraft.server.v1_13_R1.WorldMap;
import net.minecraft.server.v1_13_R1.WorldServer;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.timers.GameDayNightTimer;
import org.pixelgalaxy.utils.CustomIS;
import org.pixelgalaxy.utils.RoleMapRenderer;
import org.pixelgalaxy.utils.ScoreHelper;

import java.time.LocalTime;
import java.util.*;

public class Game {

    public static final int NIGHT_CYCLE_TIME = WerewolfMain.config.getInt("cycle_time.night");
    public static final int DAY_CYCLE_TIME = WerewolfMain.config.getInt("cycle_time.day");
    public static final List<Role> NIGHT_ROLES = Arrays.asList(Role.ALPHA_WOLF, Role.OMEGA_WOLF, Role.HERO, Role.MURDERER, Role.MAYOR, Role.WATCHER, Role.TEMPTRESS, Role.BETA_WOLF);

    private static List<String> customNames = WerewolfMain.config.getStringList("names");

    @Getter @Setter private static boolean started = false;

    @Getter private static Set<GamePlayer> gamePlayers = new HashSet<>();
    @Getter private static Map<GamePlayer, GamePlayer> targetMap = new HashMap<>();

    public static void start(){
        setStarted(true);
        initGamePlayers();
        GameDayNightTimer.start();
    }

    private static void addGamePlayer(Player p, Team team, Role role){

        getGamePlayers().add(GamePlayer.builder().player(p).playerTeam(team).playerRole(role).build());

    }

    public static void initGamePlayers(){

        Collections.shuffle(Lobby.getCurrentPlayers());
        for(Player p : Lobby.getCurrentPlayers()){

            p.setGameMode(GameMode.ADVENTURE);
            p.setAllowFlight(false);
            p.setFlying(false);
            Random random = new Random();
            int randomTeamInt = random.nextInt(teams.size());
            Team team = teams.get(randomTeamInt);
            teams.remove(randomTeamInt);
            Role role;

            if(primaryRoles.size() > 0){

                int randomRoleInt = random.nextInt(primaryRoles.size());
                role = primaryRoles.get(randomRoleInt);
                primaryRoles.remove(randomRoleInt);

            }else{
                int randomRoleInt = random.nextInt(secondaryRoles.size());
                role = secondaryRoles.get(randomRoleInt);
                secondaryRoles.remove(randomRoleInt);
            }

            addGamePlayer(p, team, role);

            p.getEquipment().setChestplate(CustomIS.getColoredChest(team));

            net.minecraft.server.v1_13_R1.ItemStack itemStack = new net.minecraft.server.v1_13_R1.ItemStack(Items.FILLED_MAP);

            WorldServer worldServer = ((CraftWorld) p.getWorld()).getHandle();
            int id = worldServer.b("map");

            WorldMap worldMap = new WorldMap("map_" + id);
            Role playerRole = Game.getGamePlayers().get(p).getPlayerRole();
            worldMap.mapView.addRenderer(new RoleMapRenderer(playerRole));

            worldServer.a(worldMap.getId(), worldMap);

            itemStack.getOrCreateTag().setInt("map", id);

            p.getInventory().addItem(CraftItemStack.asBukkitCopy(itemStack));

            ItemMeta mapm = p.getInventory().getItem(0).getItemMeta();
            mapm.setDisplayName("§7Role: " + playerRole.getRoleName());
            p.getInventory().getItem(0).setItemMeta(mapm);
            p.updateInventory();

            p.setCustomNameVisible(true);
            int randomName = random.nextInt(customNames.size());
            String customName = customNames.get(randomName);
            p.setCustomName(customName);
            p.setDisplayName(customName);
            p.setPlayerListName(customName);
            customNames.remove(customName);

            ScoreHelper.updatePlayerScoreboard(p, LocalTime.of(0, 1, 0), "Night");

        }

    }

}

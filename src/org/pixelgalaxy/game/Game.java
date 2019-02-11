package org.pixelgalaxy.game;

import net.minecraft.server.v1_13_R1.Items;
import net.minecraft.server.v1_13_R1.WorldMap;
import net.minecraft.server.v1_13_R1.WorldServer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.pixelgalaxy.WerewolfMain;
import org.pixelgalaxy.timers.GameDayNightTimer;
import org.pixelgalaxy.utils.RoleMapRenderer;
import org.pixelgalaxy.utils.ScoreHelper;

import java.time.LocalTime;
import java.util.*;

public class Game {

    public static final int NIGHT_CYCLE_TIME = WerewolfMain.config.getInt("cycle_time.night");
    public static final int DAY_CYCLE_TIME = WerewolfMain.config.getInt("cycle_time.day");
    public static final List<Role> NIGHT_ROLES = Arrays.asList(Role.ALPHA_WOLF, Role.OMEGA_WOLF, Role.HERO, Role.MURDERER, Role.MAYOR, Role.WATCHER, Role.TEMPTRESS, Role.BETA_WOLF);

    private static List<String> customNames = WerewolfMain.config.getStringList("names");

    private static boolean started = false;
    private static Map<Player, GamePlayer> gamePlayers = new HashMap<>();

    private static Map<Role, Player> roleTargetMap = new HashMap<>();

    public static boolean isStarted() {
        return started;
    }

    public static void start(){
        started = true;
        initGamePlayers();
        GameDayNightTimer timer = new GameDayNightTimer();
        timer.runTaskTimer(WerewolfMain.plugin, 0, 20);
    }

    public static Map<Player, GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public static Map<Role, Player> getRoleTargetMap() {
        return roleTargetMap;
    }

    public static void addTarget(Player targeter, Player target){

        Role playerRole = Game.getGamePlayers().get(targeter).getPlayerRole();

        if(playerRole.equals(Role.ALPHA_WOLF) && DeathsInTheNight.isBetaIsAlive()){
            playerRole = Role.BETA_WOLF;
        }
        roleTargetMap.put(playerRole, target);

    }

    private static void addGamePlayer(Player p, Team team, Role role){

        gamePlayers.put(p, new GamePlayer(team, role));

    }

    public static ItemStack getColoredChest(Team team){

        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) chest.getItemMeta();
        meta.setColor(team.getTeamColor());
        chest.setItemMeta(meta);

        return chest;
    }

    public static ItemStack getColorChooser(){

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowm = arrow.getItemMeta();
        arrowm.setDisplayName("§cColor chooser");
        arrowm.setLore(Arrays.asList("§5With this tool you can", "§5set your targets."));
        arrow.setItemMeta(arrowm);
        return arrow;
    }

    public static void initGamePlayers(){

        List<Team> teams = new ArrayList<>(Arrays.asList(Team.values()));

        List<Role> primaryRoles = new ArrayList<>(Role.getPrimaryRoles());
        List<Role> secondaryRoles = new ArrayList<>(Role.getSecondaryRoles());
        List<Player> lobbyPlayers = Lobby.getCurrentPlayers();
        Collections.shuffle(lobbyPlayers);
        for(Player p : lobbyPlayers){

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

            p.getEquipment().setChestplate(getColoredChest(team));

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

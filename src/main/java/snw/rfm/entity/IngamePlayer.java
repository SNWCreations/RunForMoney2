package snw.rfm.entity;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IngamePlayer {
    private static final Map<String, IngamePlayer> playerMap = new HashMap<>();
    protected final Player player;
    protected Team team;

    private IngamePlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Player getBukkitPlayer() {
        return player;
    }

    public static IngamePlayer getWrappedPlayer(Player player) {
        return playerMap.computeIfAbsent(player.getUniqueId().toString(), i -> new IngamePlayer(player));
    }

    public static Collection<IngamePlayer> getAllKnownWrappers() {
        return playerMap.values();
    }
}

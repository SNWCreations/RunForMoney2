package snw.rfm.entity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import static snw.rfm.util.Util.pluginMsg;

public class Team {
    protected final String displayName;
    protected final org.bukkit.scoreboard.Team team;
    protected final Set<UUID> playerUuids = new HashSet<>();

    public Team(String displayName, org.bukkit.scoreboard.Team team) {
        this.displayName = displayName;
        this.team = team;
    }

    public void add(IngamePlayer player, boolean tip) {
        if (tip) {
            Optional.ofNullable(player.getTeam()).ifPresent(i -> {
                player.getBukkitPlayer().sendMessage(
                        pluginMsg(
                                ChatColor.YELLOW + String.format("你正在加入 %s 队伍，但你已经在 %s 队伍了，你将被从 %s 队伍移出，然后再加入 %s 队伍。", getDisplayName(), i.getDisplayName(), i.getDisplayName(), getDisplayName())
                        )
                );
                i.remove(player);
            });
        }
        team.addEntry(player.getBukkitPlayer().getName());
        playerUuids.add(player.getBukkitPlayer().getUniqueId());
    }

    public void add(IngamePlayer player) {
        add(player, true);
    }

    public boolean contains(IngamePlayer player) {
        return team.hasEntry(player.getBukkitPlayer().getName());
    }

    public boolean remove(IngamePlayer player) {
        playerUuids.remove(player.getBukkitPlayer().getUniqueId());
        return team.removeEntry(player.getBukkitPlayer().getName());
    }

    public int size() {
        return toBukkitPlayerSet().size();
    }

    public String getDisplayName() {
        return displayName;
    }

    public org.bukkit.scoreboard.Team getBukkitTeam() {
        return team;
    }

    public Set<OfflinePlayer> toBukkitOfflinePlayerSet() {
        return Collections.unmodifiableSet(
                playerUuids.stream()
                        .map(Bukkit::getOfflinePlayer)
                        .collect(Collectors.toSet())
        );
    }

    public Set<Player> toBukkitPlayerSet() {
        return Collections.unmodifiableSet(
                toBukkitOfflinePlayerSet().stream()
                        .filter(OfflinePlayer::isOnline)
                        .map(OfflinePlayer::getPlayer)
                        .collect(Collectors.toSet())
        );
    }

    public Set<String> toNameSet() {
        return Collections.unmodifiableSet(toBukkitOfflinePlayerSet().stream().map(OfflinePlayer::getName).collect(Collectors.toSet()));
    }

    public void clear() {
        team.getEntries().forEach(team::removeEntry);
        playerUuids.clear();
    }
}

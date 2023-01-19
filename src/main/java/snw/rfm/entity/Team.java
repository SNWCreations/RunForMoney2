package snw.rfm.entity;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import snw.rfm.BukkitHandle;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Team {
    protected final org.bukkit.scoreboard.Team team;
    protected final Set<UUID> playerUuids = new HashSet<>();

    public Team(org.bukkit.scoreboard.Team team) {
        this.team = team;
    }

    public void add(IngamePlayer player) {
        team.addEntry(player.getBukkitPlayer().getName());
        playerUuids.add(player.getBukkitPlayer().getUniqueId());
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

    public org.bukkit.scoreboard.Team getBukkitTeam() {
        return team;
    }

    public Set<OfflinePlayer> toBukkitOfflinePlayerSet() {
        return Collections.unmodifiableSet(
                playerUuids.stream()
                        .map(BukkitHandle::getOfflinePlayer)
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

    public void clear() {
        team.getEntries().forEach(team::removeEntry);
        playerUuids.clear();
    }
}

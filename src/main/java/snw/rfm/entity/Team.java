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

    public boolean add(Player player) {
        return add(IngamePlayer.getWrappedPlayer(player), true);
    }

    public boolean add(Player player, boolean tip) {
        return add(IngamePlayer.getWrappedPlayer(player), tip);
    }

    public boolean add(IngamePlayer player, boolean tip) {
        Team i = player.getTeam();
        if (i != null) {
            if (i == this) {
                return false; // already in this team?
            }
            if (tip) {
                player.getBukkitPlayer().sendMessage(
                        pluginMsg(
                                ChatColor.YELLOW + String.format("你正在加入 %s 队伍，但你已经在 %s 队伍了，你将被从 %s 队伍移出，然后再加入 %s 队伍。", getDisplayName(), i.getDisplayName(), i.getDisplayName(), getDisplayName())
                        )
                );
            }
            i.remove(player);
        }
        team.addEntry(player.getBukkitPlayer().getName());
        playerUuids.add(player.getBukkitPlayer().getUniqueId());
        player.setTeam(this);
        return true;
    }

    public void add(IngamePlayer player) {
        add(player, true);
    }

    public boolean contains(Player player) {
        return team.hasEntry(player.getName());
    }

    public boolean contains(IngamePlayer player) {
        return team.hasEntry(player.getBukkitPlayer().getName());
    }

    public boolean remove(IngamePlayer player) {
        if (player.getTeam() == this) {
            player.setTeam(null);
        }
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

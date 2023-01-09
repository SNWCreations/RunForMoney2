package snw.rfm.entity;

import org.bukkit.entity.Player;
import snw.rfm.Main;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

public class Team {
    protected final org.bukkit.scoreboard.Team team;

    public Team(org.bukkit.scoreboard.Team team) {
        this.team = team;
    }

    public void add(IngamePlayer player) {
        team.addEntry(player.getBukkitPlayer().getName());
    }

    public boolean contains(IngamePlayer player) {
        return team.hasEntry(player.getBukkitPlayer().getName());
    }

    public boolean remove(IngamePlayer player) {
        return team.removeEntry(player.getBukkitPlayer().getName());
    }

    public int size() {
        return team.getSize();
    }

    public org.bukkit.scoreboard.Team getBukkitTeam() {
        return team;
    }

    public Collection<Player> toBukkitPlayerList() {
        return Collections.unmodifiableCollection(
                team.getEntries().stream().map(
                        i -> Main.getInstance().getServer().getPlayer(UUID.fromString(i))
                ).collect(Collectors.toSet())
        );
    }

    public void clear() {
        team.getEntries().forEach(team::removeEntry);
    }
}

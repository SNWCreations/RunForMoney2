package snw.rfm.entity;

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
}

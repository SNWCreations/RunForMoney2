package snw.rfm.entity;

public class Team {
    protected final Game game;
    protected final org.bukkit.scoreboard.Team team;

    public Team(Game game, org.bukkit.scoreboard.Team team) {
        this.game = game;
        this.team = team;
    }

    public Game getGame() {
        return game;
    }

    public void add(IngamePlayer player) {
        team.addEntry(player.getPlayer().getName());
    }

    public boolean contains(IngamePlayer player) {
        return team.hasEntry(player.getPlayer().getName());
    }

    public boolean remove(IngamePlayer player) {
        return team.removeEntry(player.getPlayer().getName());
    }

    public org.bukkit.scoreboard.Team getTeam() {
        return team;
    }
}

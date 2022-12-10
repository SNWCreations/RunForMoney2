package snw.rfm.entity;

import org.bukkit.OfflinePlayer;

public class IngamePlayer {
    protected final Game game;
    protected final OfflinePlayer player;
    protected Team team;

    public IngamePlayer(Game game, OfflinePlayer player) {
        this.game = game;
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }
}

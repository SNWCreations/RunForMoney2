package snw.rfm.entity;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.OfflinePlayer;

public class GameManager {
    protected final Set<Game> games = new HashSet<>();
    
    public Set<Game> getGames() {
        return games;
    }

    public IngamePlayer getIngamePlayer(OfflinePlayer player) {
        for (Game g : getGames()) {
            for (IngamePlayer p : g.getPlayers()) {
                if (p.getPlayer() == player) {
                    return p;
                }
            }
        }
        return null;
    }
}

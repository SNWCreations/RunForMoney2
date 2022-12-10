package snw.rfm.entity;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

import snw.rfm.Main;

public class Game {
    protected final Set<IngamePlayer> players = new HashSet<>();
    protected volatile boolean running = false;
    protected volatile boolean used = false;

    public Game() {
        Main.getInstance().getGameManager().getGames().add(this);
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        Validate.isTrue(!isRunning(), "The game is running.");
        Validate.isTrue(!used, "This game is already used.");
        running = true;
        used = true;
        // TODO game initialization
    }

    public void stop() {
        // TODO game termination
    }

    public Set<IngamePlayer> getPlayers() {
        return players;
    }
}

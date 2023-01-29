package snw.rfm.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.Main;
import snw.rfm.entity.Game;

import java.util.concurrent.atomic.AtomicInteger;

public class CoinTimer extends BukkitRunnable {
    protected final Main main;
    protected final Game game;
    protected final AtomicInteger time;

    public CoinTimer(Main main, Game game, AtomicInteger time) {
        this.main = main;
        this.game = game;
        this.time = time;
    }

    @Override
    public void run() {
        if (game.isPaused()) return; // do nothing if paused
        if (time.getAndDecrement() > 0) {
            game.getCoinMap().increaseAll();
        } else {
            game.stop(); // It's time to stop!
            cancel();
        }
    }

    public void start() {
        runTaskTimer(main, 20L, 20L);
    }
}

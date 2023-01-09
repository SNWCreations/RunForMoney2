package snw.rfm.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.Main;
import snw.rfm.entity.Game;

public class CoinTimer extends BukkitRunnable {
    protected final Game game;
    protected int remainingSeconds;

    public CoinTimer(Game game, int remainingSeconds) {
        this.game = game;
        this.remainingSeconds = remainingSeconds;
    }

    @Override
    public void run() {
        if (remainingSeconds-- > 0) {
            game.getCoinMap().increaseAll();
        } else {
            game.stop(); // It's time to stop!
            cancel();
        }
    }

    public void start() {
        runTaskTimer(Main.getInstance(), 20L, 20L);
    }
}

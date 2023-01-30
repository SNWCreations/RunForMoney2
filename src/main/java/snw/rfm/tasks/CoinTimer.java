package snw.rfm.tasks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.Main;
import snw.rfm.entity.Game;
import snw.rfm.api.events.internal.RemoveTimeEvent;
import snw.rfm.api.events.internal.RequestTimeEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class CoinTimer extends BukkitRunnable implements Listener {
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
            HandlerList.unregisterAll(this);
        }
    }

    public void start() {
        runTaskTimer(main, 20L, 20L);
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onRequest(RequestTimeEvent e) {
        if (e.getGame() == game) {
            e.setData(time.get());
        }
    }

    @EventHandler
    public void onEdit(RemoveTimeEvent e) {
        if (e.getGame() == game) {
            int data = e.getData();
            time.set(time.get() - data);
            if (e.isAddCoin()) {
                game.getCoinMap().addAll(data);
            }
        }
    }
}

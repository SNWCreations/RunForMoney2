package snw.rfm.tasks;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.Main;
import snw.rfm.api.events.internal.RemoveTimeEvent;
import snw.rfm.api.events.internal.RequestTimeEvent;
import snw.rfm.commands.RFMTimerCommand;
import snw.rfm.entity.CoinMap;
import snw.rfm.entity.Game;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        CoinMap map = game.getCoinMap();
        if (map.isReversed()) { // 1 + 2 - 1 = 2
            time.addAndGet(2); // reverse
        }
        if (time.getAndDecrement() > 0) {
            map.increaseAll();

            String sec = String.valueOf(time.get() % 60);
            // noinspection DataFlowIssue
            new SendingActionBarMessage(
                    new TextComponent("剩余时间: " +
                            (time.get() / 60) + ":" + (sec.length() == 1 ? ("0" + sec) : sec)
                    ),
                    Bukkit.getOperators().stream()
                            .filter(OfflinePlayer::isOnline)
                            .filter(ServerOperator::isOp)
                            .map(OfflinePlayer::getPlayer)
                            .filter(IT -> RFMTimerCommand.ACTIVE.contains(IT.getUniqueId()))
                            .collect(Collectors.toList()),
                    20
            ).start(main);
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

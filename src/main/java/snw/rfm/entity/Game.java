package snw.rfm.entity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.api.item.internal.ItemClickDispatcher;
import snw.rfm.events.GameStartEvent;
import snw.rfm.events.GameStopEvent;
import snw.rfm.events.HunterReleasedEvent;
import snw.rfm.listeners.AttackListener;
import snw.rfm.tasks.CoinTimer;
import snw.rfm.tasks.HunterReleaseTimer;
import snw.rfm.util.ListenerList;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static snw.rfm.util.Util.fireEvent;
import static snw.rfm.util.Util.tempListener;

public class Game {
    protected final Main main;
    protected final CoinMap coinMap;
    protected final AtomicInteger timeRemaining;
    protected final ListenerList listeners;
    protected final AtomicBoolean pauseStatus;
    protected CoinTimer coinTimer;

    public Game(Main main) {
        this.main = main;
        coinMap = new CoinMap();
        timeRemaining = new AtomicInteger();
        listeners = new ListenerList(main);
        pauseStatus = new AtomicBoolean(false);
    }

    public void start() {
        timeRemaining.set(ConfigConstant.GAME_TIME * 60);
        fireEvent(new GameStartEvent(this));
        registerListener(new AttackListener(this));
        registerListener(new ItemClickDispatcher(main));
        tempListener(main, HunterReleasedEvent.class, i -> {
            if (i.getGame() != this) {
                return false;
            }
            coinTimer = new CoinTimer(main, this, timeRemaining);
            coinTimer.start();
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏开始");
            return true;
        });
        if (ConfigConstant.HUNTER_RELEASE_TIME > 0) {
            new HunterReleaseTimer(main, this, ConfigConstant.HUNTER_RELEASE_TIME).start();
        } else {
            fireEvent(new HunterReleasedEvent(this));
        }
    }

    public void stop() {
        Bukkit.getScheduler().runTaskAsynchronously(
                main,
                () -> fireEvent(new GameStopEvent(this))
        );
        for (Player player : TeamRegistry.RUNNER.toBukkitPlayerSet()) {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏结束");
        }
        coinTimer.cancel();
        listeners.clear();
        TeamRegistry.HUNTER.clear();
        TeamRegistry.RUNNER.clear();
        IngamePlayer.getAllKnownWrappers().clear();
    }

    public boolean isPaused() {
        return pauseStatus.get();
    }

    public void pause() {
        if (isPaused()) {
            throw new IllegalStateException("Already paused");
        }
        pauseStatus.set(true);
    }

    public void resume() {
        if (!isPaused()) {
            throw new IllegalStateException("Not paused");
        }
        pauseStatus.set(false);
    }

    public CoinMap getCoinMap() {
        return coinMap;
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }
}

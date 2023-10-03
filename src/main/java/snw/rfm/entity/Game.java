package snw.rfm.entity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.api.GameController;
import snw.rfm.api.events.GameStartEvent;
import snw.rfm.api.events.GameStopEvent;
import snw.rfm.api.events.HunterReleasedEvent;
import snw.rfm.item.internal.ItemClickDispatcher;
import snw.rfm.listeners.DamageListener;
import snw.rfm.listeners.PickupListener;
import snw.rfm.tasks.CoinTimer;
import snw.rfm.tasks.HunterReleaseTimer;
import snw.rfm.tasks.SlowItemTask;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static snw.rfm.util.Util.fireEvent;

public class Game {
    private static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false);

    protected final Main main;
    protected final CoinMap coinMap;
    protected final AtomicInteger timeRemaining;
    protected final List<Listener> listeners;
    protected final AtomicBoolean pauseStatus;
    protected final GameController controller;
    protected CoinTimer coinTimer;

    public Game(Main main) {
        this.main = main;
        coinMap = new CoinMap();
        timeRemaining = new AtomicInteger();
        listeners = new LinkedList<>();
        pauseStatus = new AtomicBoolean(false);
        controller = new GameControllerImpl(main, this);
    }

    public void start(int hunterReleaseTime) {
        timeRemaining.set(ConfigConstant.GAME_TIME * 60);
        fireEvent(new GameStartEvent(this));
        registerListener(new DamageListener(main, this));
        registerListener(new ItemClickDispatcher(main));
        registerListener(new PickupListener());
        registerListener(new HunterReleaseListener());

        for (Player player : TeamRegistry.HUNTER.toBukkitPlayerSet()) {
            player.addPotionEffect(SPEED);
        }

        SlowItemTask slowItemTask = new SlowItemTask(this);
        registerListener(slowItemTask);
        slowItemTask.start(main);
        if (hunterReleaseTime > 0) {
            new HunterReleaseTimer(main, this, hunterReleaseTime).start();
        } else {
            fireEvent(new HunterReleasedEvent(this));
        }
    }

    public void stop() {
        if (main.isEnabled()) { // prevent call event on disable
            Bukkit.getScheduler().runTaskAsynchronously(
                    main,
                    () -> fireEvent(new GameStopEvent(this))
            );
        }
        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏结束");
        for (Player hunter : TeamRegistry.HUNTER.toBukkitPlayerSet()) {
            hunter.removePotionEffect(PotionEffectType.SPEED); // remove speed
        }
        if (coinTimer != null) { // if you terminated the game before it starts?
            coinTimer.cancel();
        }
        listeners.forEach(HandlerList::unregisterAll);
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
        main.getServer().getPluginManager().registerEvents(listener, main);
        listeners.add(listener);
    }

    public GameController getController() {
        return controller;
    }

    class HunterReleaseListener implements Listener {
        @EventHandler(priority = EventPriority.LOWEST)
        public void on(HunterReleasedEvent e) {
            if (e.getGame() == Game.this) {
                if (!ConfigConstant.NO_TIMER) {
                    Game.this.coinTimer = new CoinTimer(main, Game.this, timeRemaining);
                    Game.this.coinTimer.start();
                }
                Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏开始");
                HandlerList.unregisterAll(this);
            }
        }
    }
}

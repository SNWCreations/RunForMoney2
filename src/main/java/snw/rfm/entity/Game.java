package snw.rfm.entity;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.events.GameStartEvent;
import snw.rfm.events.GameStopEvent;
import snw.rfm.events.HunterReleasedEvent;
import snw.rfm.listeners.AttackListener;
import snw.rfm.tasks.CoinTimer;
import snw.rfm.tasks.HunterReleaseTimer;
import snw.rfm.util.ListenerList;

import static snw.rfm.util.Util.fireEvent;
import static snw.rfm.util.Util.tempListener;

public class Game {
    protected final CoinMap coinMap;
    protected final Collection<Listener> listeners = new ListenerList();
    public CoinTimer coinTimer;

    public Game() {
        coinMap = new CoinMap();
    }

    public void start() {
        fireEvent(new GameStartEvent(this));
        registerListener(new AttackListener(this));
        tempListener(HunterReleasedEvent.class, i -> {
            if (i.getGame() != this) {
                return false;
            }
            coinTimer = new CoinTimer(this, ConfigConstant.GAME_TIME * 60);
            coinTimer.start();
            Main.getInstance().getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏开始");
            return true;
        });
        if (ConfigConstant.HUNTER_RELEASE_TIME > 0) {
            new HunterReleaseTimer(this, ConfigConstant.HUNTER_RELEASE_TIME).start();
        } else {
            fireEvent(new HunterReleasedEvent(this));
        }
    }

    public void stop() {
        Main.getInstance().getServer().getScheduler().runTaskAsynchronously(
                Main.getInstance(),
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

    public CoinMap getCoinMap() {
        return coinMap;
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }
}
